package com.qualityobjects.oss.h3lp3r.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
@Data
public class OpResponse {

	private OpInput input;
	private Integer duration;
	private Object result;
}
