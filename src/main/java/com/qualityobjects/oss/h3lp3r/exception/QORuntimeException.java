package com.qualityobjects.oss.h3lp3r.exception;

@SuppressWarnings("serial")
public class QORuntimeException extends RuntimeException {
	private final int httpStatus;

	public QORuntimeException(String msg) {
		this(msg, null);
	}

	public QORuntimeException(String msg, Throwable cause) {
		super(msg, cause);
		httpStatus = QOException.DEFAULT_APP_ERROR_STATUS_CODE;
	}

	public QORuntimeException(int httpStatus, String msg) {
		this(httpStatus, msg, null);
	}

	public QORuntimeException(int httpStatis, String msg, Throwable cause) {
		super(msg, cause);
		this.httpStatus = httpStatis;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

}
