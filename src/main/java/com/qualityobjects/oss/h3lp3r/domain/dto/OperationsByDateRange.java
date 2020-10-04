package com.qualityobjects.oss.h3lp3r.domain.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

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

    List<OpsInRange> buckets;

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

    public static OperationsByDateRange of(LocalDateTime since, String interval, Histogram dateRangeHistogram) {

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

        return OperationsByDateRange.builder() //
            .since(since) //
            .interval(interval) //
            .buckets(buckets) //
            .count(buckets.parallelStream().flatMapToLong(b -> LongStream.of(b.getCount())).sum())
            .build();
    }
}
