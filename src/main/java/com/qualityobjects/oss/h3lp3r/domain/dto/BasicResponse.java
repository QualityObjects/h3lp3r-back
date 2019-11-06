package com.qualityobjects.oss.h3lp3r.domain.dto;

public class BasicResponse {

	private boolean success;
	
	public BasicResponse() {}
	
	public BasicResponse(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	
}
