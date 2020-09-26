package com.qualityobjects.oss.h3lp3r.config;

import java.nio.file.AccessDeniedException;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.qualityobjects.oss.h3lp3r.domain.dto.ErrorBean;
import com.qualityobjects.oss.h3lp3r.exception.QOException;
import com.qualityobjects.oss.h3lp3r.exception.QORuntimeException;

@ControllerAdvice
public class GlobalExceptionController extends ResponseEntityExceptionHandler  {

	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionController.class);


	@ResponseBody
	@ExceptionHandler(QOException.class)
	public ResponseEntity<ErrorBean> handleCustomException(QOException ex) {

		ErrorBean error = ErrorBean.of(ex);

		return ResponseEntity.status(ex.getHttpStatus()).body(error);
	}

	@ResponseBody
	@ExceptionHandler(QORuntimeException.class)
	public ResponseEntity<ErrorBean> handleCustomException(QORuntimeException ex) {
		ErrorBean error = new ErrorBean(ex.getHttpStatus(), ex.getMessage());

		return ResponseEntity.status(ex.getHttpStatus()).body(error);
	}
	
	@ResponseBody
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorBean> handleCustomException(AccessDeniedException ex) {
		ErrorBean error = new ErrorBean(HttpStatus.FORBIDDEN.value(), ex.getMessage());
		return ResponseEntity.status(403).body(error);
	}
	
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorBean> handleAllException(Exception ex) {
		LOG.error("Unexpected error: {}", ex, ex);
		int httpStatus = (ex instanceof HttpServerErrorException) ? HttpServerErrorException.class.cast(ex).getRawStatusCode() : HttpStatus.INTERNAL_SERVER_ERROR.value();   
		ErrorBean error = new ErrorBean(httpStatus, ex.getMessage());

		return ResponseEntity.status(httpStatus).body(error);
	}

}
