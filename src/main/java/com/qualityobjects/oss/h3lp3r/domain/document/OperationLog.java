package com.qualityobjects.oss.h3lp3r.domain.document;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.qualityobjects.oss.h3lp3r.domain.enums.Operation;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.lang.Nullable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(content = Include.NON_NULL)
@Document(indexName = "operation_log")
public class OperationLog {
    @Id
	private String id;

    @Field(type = FieldType.Ip)
	private String clientIp;

	@GeoPointField
	private GeoPoint location;

	@Builder.Default
	private Boolean success = true;

	@Nullable
	private String errorMsg;

    @JsonFormat(shape = Shape.STRING)
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
	private LocalDateTime operationTimestamp;

	@Field(type = FieldType.Keyword)
    private Operation operation;
	
	/**
	 * Operation duration in nanoseconds
	 */
	private Long duration;
	
	private Map<String, Object> params;

	private String userAgent;
}
