package com.qualityobjects.oss.h3lp3r.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/*
curl -H "Content-Type: application/json" -XPOST meetup2.qodev.es:9200/operation_log/_search?pretty=true -d '{
  "aggs": {
    "ops_over_time": {
      "date_histogram": {
        "field": "operationTimestamp",
        "calendar_interval": "1m"
      },
      "aggs": {
          "operations": {
            "terms": {
              "field": "action"
            }
          },
          "avg_duration": {
            "avg": {
              "field": "duration"
            }
          }
        }
      }
  },
  "size": 0
}'


 */
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
	/*    SearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withQuery(matchAllQuery())
            .withSearchType(COUNT)
            .withIndices("articles").withTypes("article")
            .addAggregation(terms("subjects").field("subject"))
            .build();*/
	public OperationsByDateRange getAggregationsOnOperation(LocalDateTime since, DateHistogramInterval interval) throws IOException {
//		SearchRequestBuilder srb = new SearchRequestBuilder(client, action)
		// SearchRequest searchRequest = new SearchRequest("operation_log") //
		// 			.searchType(SearchType.QUERY_THEN_FETCH)
		// 			.addAggregation(null);
		
		
		LOG.info("Interval: {}", interval);
		TermsAggregationBuilder termsAgg = AggregationBuilders.terms("operations").field("operation");
		AvgAggregationBuilder avgAgg = AggregationBuilders.avg("avg_duration").field("duration");
		DateHistogramAggregationBuilder dhab = AggregationBuilders.dateHistogram("ops_over_time") //
				.field("operationTimestamp").fixedInterval(interval)				
				.subAggregation(termsAgg)
				.subAggregation(avgAgg);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()		
		.query(QueryBuilders.rangeQuery("operationTimestamp").from(since.format(ISO_DT_WITH_MILLIS_PATTERN)))
		.aggregation(dhab).size(0);
		LOG.info("searchSourceBuilder: {}", searchSourceBuilder.toString());
		SearchRequest searchRequest = new SearchRequest().source(searchSourceBuilder);


		SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);

		if (searchResponse.status() != RestStatus.OK) {
			throw new QORuntimeException("Error quering ElasticSearch");
		}
		
		Aggregations aggs = searchResponse.getAggregations();
		
		Histogram hist = aggs.get("ops_over_time");
		return OperationsByDateRange.of(since, "5m", hist);
		// XContentBuilder builder = XContentFactory.jsonBuilder();
		// builder.startObject();		
		// XContentBuilder frag = hist.toXContent(builder, ToXContent.EMPTY_PARAMS);
		// builder.endObject();
		//builder.endObject();
		
	}
}
