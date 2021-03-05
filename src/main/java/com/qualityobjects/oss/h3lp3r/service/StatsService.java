package com.qualityobjects.oss.h3lp3r.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.qualityobjects.oss.h3lp3r.domain.dto.OperationsByDateRange;
import com.qualityobjects.oss.h3lp3r.exception.QORuntimeException;

import org.apache.commons.lang3.ObjectUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StatsService {

	@Autowired
	private ReactiveElasticsearchClient esClient;

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(StatsService.class);

	public Integer getLastOperations(LocalDateTime since) {
		return null;
	}

	final static DateTimeFormatter ISO_DT_WITH_MILLIS_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

	public Mono<OperationsByDateRange> getAggregationsOnOperation(LocalDateTime since, DateHistogramInterval interval) throws IOException {
		TermsAggregationBuilder termsAgg = AggregationBuilders.terms("operations").field("operation");
		AvgAggregationBuilder avgAgg = AggregationBuilders.avg("avg_duration").field("duration");
		DateHistogramAggregationBuilder dhab = AggregationBuilders.dateHistogram("ops_over_time") //
				.field("operationTimestamp").fixedInterval(interval)				
				.subAggregation(termsAgg)
				.subAggregation(avgAgg);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()		
		.query(QueryBuilders.rangeQuery("operationTimestamp").from(since.format(ISO_DT_WITH_MILLIS_PATTERN)))
		.aggregation(dhab)
		.aggregation(termsAgg) // Same aggregation for entire date range
		.size(0);
		//LOG.info("searchSourceBuilder: {}", searchSourceBuilder.toString());
		SearchRequest searchRequest = new SearchRequest().source(searchSourceBuilder);


		Flux<Aggregation> searchResponse = esClient.aggregate(searchRequest);
		return searchResponse.buffer().map(aggList -> {
			Histogram dateRangeHistogram = null; // aggs.get("ops_over_time");	
			Terms termsByoperations = null; // aggs.get("operations");

			for (Aggregation aggregation : aggList) {
				if ("ops_over_time".equals(aggregation.getName())) {
					dateRangeHistogram = (Histogram)aggregation;
				} else if ("operations".equals(aggregation.getName())) {
					termsByoperations = (Terms)aggregation;
				} 
			}
			if (ObjectUtils.anyNull(dateRangeHistogram, termsByoperations)) {
				throw new QORuntimeException("Missing aggregation in query results: " + aggList);
			}
			return OperationsByDateRange.of(since, interval.toString(), dateRangeHistogram, termsByoperations);
		}).as(Mono::from);
		
	}
}
