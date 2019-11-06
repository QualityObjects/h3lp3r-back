package com.qualityobjects.oss.h3lp3r.exception;

public class InvalidCredentialsException extends QOException {

	private static final long serialVersionUID = 5207795630755628143L;

	public InvalidCredentialsException() {
		this("Credenciales no válidas");
	}

	public InvalidCredentialsException(String msg) {
		super(ErrorCodes.INVALID_CREDENTIALS, msg);
	}

}
