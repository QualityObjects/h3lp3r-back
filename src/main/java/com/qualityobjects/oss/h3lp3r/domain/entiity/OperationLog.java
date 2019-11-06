package com.qualityobjects.oss.h3lp3r.domain.entiity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qualityobjects.oss.h3lp3r.domain.enums.Lang;
import com.qualityobjects.oss.h3lp3r.domain.enums.Operation;

import lombok.Data;

@Data
@Entity
@Table(name = "operation_log")
public class OperationLog {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="client_ip")
	@JsonProperty("client_ip")
	private String clientIp;

	@Column(name="operation_timestamp")
	@JsonProperty("operation_timestamp")
	private LocalDateTime operationTimestamp;

	@Enumerated(EnumType.STRING)
	private Operation action;
	
	private Integer duration;
	
	@Column(name="operation_data")
	@JsonProperty("operation_data")
	private String operationData;

	@Column(name="user_data")
	@JsonProperty("user_data")
	private String userData;
}
