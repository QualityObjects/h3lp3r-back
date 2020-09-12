package com.qualityobjects.oss.h3lp3r.service;

import java.nio.charset.Charset;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
	
	public OpResponse base64(OpInput input) throws QOException {
		OpResponse resp = new OpResponse();
		resp.setInput(input);
		
		String textInput = input.getParams().get(TEXT_INPUT_KEY);
		if (ObjectUtils.isEmpty(textInput)) {
			throw new InvalidInputDataException(String.format("Input param '%s' is mandatory", TEXT_INPUT_KEY));
		}
		String result;
	
		if (input.getAction() == Operation.ENC_BASE64) {
			result = Base64.encodeBase64String(textInput.getBytes(Charset.forName("utf8")));
		} else if (input.getAction() == Operation.DEC_BASE64) {
			result = new String(Base64.decodeBase64(textInput), Charset.forName("utf8"));
		} else {
			throw new InvalidInputDataException(String.format("Operation not supported: '%s'", input.getAction()));
		}
		resp.setResult(result);
		
		return resp;
	}

}
