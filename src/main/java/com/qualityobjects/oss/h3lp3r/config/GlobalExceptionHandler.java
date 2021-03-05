package com.qualityobjects.oss.h3lp3r.config;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qualityobjects.oss.h3lp3r.domain.dto.ErrorBean;
import com.qualityobjects.oss.h3lp3r.exception.QOException;
import com.qualityobjects.oss.h3lp3r.exception.QORuntimeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

//  @Autowired
//  private DataBufferWriter bufferWriter;

@Slf4j
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		HttpStatus status = HttpStatus.I_AM_A_TEAPOT;

		Map<String, Object> errorbean = Map.of("error_info", ex.toString());

		if (exchange.getResponse().isCommitted()) {
			return Mono.error(ex);
		}

		exchange.getResponse().setStatusCode(status);
		return exchange.getResponse().writeWith(Mono.fromSupplier(() -> {
			DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
			try {
				return bufferFactory.wrap(objectMapper.writeValueAsBytes(errorbean));
			} catch (JsonProcessingException e) {
				log.warn("Error writing response", e);
				return bufferFactory.wrap(new byte[0]);
			}
        }));
	}
  
	
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
