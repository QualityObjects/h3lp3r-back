package com.qualityobjects.oss.h3lp3r.exception;

import org.springframework.http.HttpStatus;

public class InvalidInputDataException extends QOException {

	private static final long serialVersionUID = 5207795630755628143L;

	public InvalidInputDataException() {
		super(HttpStatus.I_AM_A_TEAPOT.value(), ErrorCodes.INVALID_DATA, "Los datos de entrada no son válidos");
	}

	public InvalidInputDataException(String msg) {
		super(HttpStatus.I_AM_A_TEAPOT.value(), ErrorCodes.INVALID_DATA, msg);
	}

}
