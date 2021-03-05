package com.qualityobjects.oss.h3lp3r.service;

import java.nio.charset.Charset;
import java.util.Base64;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import reactor.core.publisher.Mono;

import com.qualityobjects.oss.h3lp3r.domain.dto.OpInput;
import com.qualityobjects.oss.h3lp3r.domain.dto.OpResponse;
import com.qualityobjects.oss.h3lp3r.domain.enums.Operation;
import com.qualityobjects.oss.h3lp3r.exception.InvalidInputDataException;
import com.qualityobjects.oss.h3lp3r.exception.QOException;

@Service
public class BaseEncodingService {

	@SuppressWarnings("unused") 
	private static final Logger LOG = LoggerFactory.getLogger(BaseEncodingService.class);

	public static final String TEXT_INPUT_KEY = "text";
	
	public Mono<OpResponse> base64(OpInput input) throws QOException {
		
		final String textInput = input.getParams().get(TEXT_INPUT_KEY);
		if (ObjectUtils.isEmpty(textInput)) {
			throw new InvalidInputDataException(String.format("Input param '%s' is mandatory", TEXT_INPUT_KEY));
		}
		return Mono.fromCallable(() -> {
			String result;
	
			if (input.getAction() == Operation.ENC_BASE64) {
				result = Base64.getEncoder().encodeToString(textInput.getBytes(Charset.forName("utf8")));
			} else if (input.getAction() == Operation.DEC_BASE64) {				
				result = new String(Base64.getDecoder().decode(textInput), Charset.forName("utf8"));
			} else {
				throw new InvalidInputDataException(String.format("Operation not supported: '%s'", input.getAction()));
			}
			
			return OpResponse.builder() //
							.input(input) // 
							.result(result) //
							.build();
		});

	}

}
