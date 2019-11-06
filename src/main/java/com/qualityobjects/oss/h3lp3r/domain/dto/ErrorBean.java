package com.qualityobjects.oss.h3lp3r.domain.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qualityobjects.oss.h3lp3r.exception.QOException;

@JsonInclude(Include.NON_NULL)
public class ErrorBean {

	private final boolean success = false;
	@JsonProperty("error_code")
	private int errorCode;
	@JsonProperty("error_msg")
	private String errorMessage;
	@JsonProperty("error_data")
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
	

	public boolean isSuccess() {
		return success;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
