package com.qualityobjects.oss.h3lp3r.domain.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.qualityobjects.oss.h3lp3r.exception.QOException;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ErrorBean {

	private final boolean success = false;
	private int errorCode;
	private String errorMessage;
	private Map<String, Object> errorData;
	
	public Map<String, Object> getErrorData() {
		return errorData;
	}

	public void setErrorData(Map<String, Object> errorData) {
		this.errorData = errorData;
	}

	public ErrorBean() {}
	
	public ErrorBean(int code, String msg) {
		this.errorCode = code;
		this.errorMessage = msg;
	}
	
	public ErrorBean(QOException ex) {
		this.errorCode = ex.getCode();
		this.errorMessage = ex.getMessage();
		this.errorData = ex.getErrorData();
	}

	
}
