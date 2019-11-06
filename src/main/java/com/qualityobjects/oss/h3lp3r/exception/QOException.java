package com.qualityobjects.oss.h3lp3r.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class QOException extends Exception {


	public static final int DEFAULT_APP_ERROR_STATUS_CODE = HttpStatus.I_AM_A_TEAPOT.value();
	
	private final int httpStatus;
	private final int code;
	
	/**
	 * Parámetros específicos del error
	 */
	private final Map<String, Object> errorData = new HashMap<>();
	
	public QOException() {
		this(DEFAULT_APP_ERROR_STATUS_CODE, 0, "QO exception", null, null);
	}
	
	public QOException(int code, String msg) {
		this(DEFAULT_APP_ERROR_STATUS_CODE, code, msg, null, null);
	}

	public QOException(int code, String msg, Throwable cause) {
		this(DEFAULT_APP_ERROR_STATUS_CODE, code, msg, null, cause);
	}
	
	public QOException(int httpStatus, int code, String msg) {
		this(httpStatus, code, msg, null, null);
	}
	
	public QOException(int httpStatus, int code, String msg, Throwable cause) {
		this(httpStatus, code, msg, null, cause);
	}

	public QOException(int code, String msg, Map<String, Object> extraData) {
		this(code, msg, extraData, null);
	}

	public QOException(int code, String msg, Map<String, Object> extraData, Throwable cause) {
		this(DEFAULT_APP_ERROR_STATUS_CODE, code, msg, extraData, cause);
	}
	
	public QOException(int httpStatus, int code, String msg, Map<String, Object> extraData) {
		this(httpStatus, code, msg, extraData, null);
	}
	
	public QOException(int httpStatus, int code, String msg, Map<String, Object> extraData, Throwable cause) {
		super(msg, cause);
		this.httpStatus = httpStatus;
		this.code = code;
		if (extraData != null) {
			this.errorData.putAll(extraData);
		}
	}

	public int getHttpStatus() {
		return httpStatus;
	}
	
	public int getCode() {
		return code;
	}

	public Map<String, Object> getErrorData() {
		return errorData;
	}
	
	public static Map<String, Object> createMap(Object ...kv) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		for (int j = 0; j < kv.length; j+=2) {
			map.put(kv[j].toString(), kv[j+1]);
		}
		
		return map;
	}

	public static class ErrorCodes {
		public static final int UNKNOWN = 9999;

		public static final int ACCESS_DENIED = 1001;
		public static final int INVALID_CREDENTIALS = 1002;
		public static final int INVALID_TOKEN = 1003;
		
		public static final int DESCRIPTOR_UPDATE_ERROR = 2001;
		public static final int ELEMENT_NOT_FOUND = 2004;
		
		public static final int MISSING_REQUIRED_DATA = 3001;
		public static final int INVALID_DATA = 3002;
		
	}
}
