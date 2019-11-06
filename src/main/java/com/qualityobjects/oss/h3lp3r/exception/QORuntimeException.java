package com.qualityobjects.oss.h3lp3r.exception;

@SuppressWarnings("serial")
public class QORuntimeException extends RuntimeException {
	private final int httpStatus;

	public QORuntimeException(String msg) {
		super(msg);
		httpStatus = QOException.DEFAULT_APP_ERROR_STATUS_CODE;
	}

	public QORuntimeException(int httpStatis, String msg) {
		super(msg);
		this.httpStatus = httpStatis;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

}
