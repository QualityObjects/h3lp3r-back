package com.qualityobjects.oss.h3lp3r.domain.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation.Bucket;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Avg;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OperationsByDateRange {

    @JsonFormat(shape = Shape.STRING)
    LocalDateTime since;
    /**
     * "1d", "5m", "60s"...
     */
    String interval;

    List<OpsInRange> timeline;

    Map<String, Long> operations;

    Long count;

    @Data @Builder
    public static class OpsInRange {
        @JsonFormat(shape = Shape.STRING)
        LocalDateTime initRange;
        Long count;
        /**
         * In nanoseconds
         */
        Double avgDuration;
        Map<String, Long> countByOperation;
    }

    public static OperationsByDateRange of(LocalDateTime since, String interval, Aggregations aggs) {

        Histogram dateRangeHistogram = aggs.get("ops_over_time");

        List<OpsInRange> buckets = dateRangeHistogram.getBuckets().stream().map(bucket -> {
            LocalDateTime initRange = LocalDateTime.parse(bucket.getKeyAsString(), DateTimeFormatter.ISO_DATE_TIME);
            Terms countByOp = bucket.getAggregations().get("operations");
            Map<String, Long> countByOperation = countByOp.getBuckets().stream().collect(Collectors.toMap(Bucket::getKeyAsString, Bucket::getDocCount));
            Avg avgDur = bucket.getAggregations().get("avg_duration");
            double avgDurationMs = Double.isInfinite(avgDur.getValue()) ? 0.0 : avgDur.getValue() / 1000000.0;
            return OpsInRange.builder().initRange(initRange) //
                .count(bucket.getDocCount()) //
                .countByOperation(countByOperation) //
                .avgDuration(avgDurationMs) //
                .build();
        }).collect(Collectors.toList());

		Terms termsByoperations = aggs.get("operations");
        Map<String, Long> countByOperation = termsByoperations.getBuckets().stream().collect(Collectors.toMap(Bucket::getKeyAsString, Bucket::getDocCount));
        
        return OperationsByDateRange.builder() //
            .since(since) //
            .interval(interval) //
            .timeline(buckets) //
            .operations(countByOperation) //
            .count(countByOperation.values().parallelStream().mapToLong(Long::longValue).sum())
            .build();
    }
}
