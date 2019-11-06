package com.qualityobjects.oss.h3lp3r.domain.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.qualityobjects.oss.h3lp3r.domain.enums.Operation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(value = Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpInput {

	private Operation action;
	private Map<String, String> params;
	
}
