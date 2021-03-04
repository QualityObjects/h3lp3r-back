package com.qualityobjects.oss.h3lp3r.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.qualityobjects.oss.h3lp3r.domain.dto.OperationsByDateRange;
import com.qualityobjects.oss.h3lp3r.exception.QORuntimeException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

	@Autowired
	private RestHighLevelClient esClient;

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(StatsService.class);

	public Integer getLastOperations(LocalDateTime since) {
		return null;
	}

	final static DateTimeFormatter ISO_DT_WITH_MILLIS_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

	public OperationsByDateRange getAggregationsOnOperation(LocalDateTime since, DateHistogramInterval interval) throws IOException {
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


		SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);

		if (searchResponse.status() != RestStatus.OK) {
			throw new QORuntimeException("Error quering ElasticSearch");
		}
		//LOG.info("{}", searchResponse.toString());
		Aggregations aggs = searchResponse.getAggregations();
		
		return OperationsByDateRange.of(since, interval.toString(), aggs);
		// XContentBuilder builder = XContentFactory.jsonBuilder();
		// builder.startObject();		
		// XContentBuilder frag = hist.toXContent(builder, ToXContent.EMPTY_PARAMS);
		// builder.endObject();
		//builder.endObject();
		
	}
}
