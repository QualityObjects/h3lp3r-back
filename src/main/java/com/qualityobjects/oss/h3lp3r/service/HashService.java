package com.qualityobjects.oss.h3lp3r.service;

import java.security.NoSuchAlgorithmException;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.qualityobjects.oss.h3lp3r.common.HashHelper;
import com.qualityobjects.oss.h3lp3r.domain.dto.OpInput;
import com.qualityobjects.oss.h3lp3r.domain.dto.OpResponse;
import com.qualityobjects.oss.h3lp3r.exception.InvalidInputDataException;
import com.qualityobjects.oss.h3lp3r.exception.QOException;

@Service
public class HashService {

	@SuppressWarnings("unused") 
	private static final Logger LOG = LoggerFactory.getLogger(HashService.class);

	public static final String TEXT_INPUT_KEY = "text";
	
	public OpResponse hash(OpInput input) throws QOException {
		OpResponse resp = new OpResponse();
		resp.setInput(input);
		
		String textInput = input.getParams().get(TEXT_INPUT_KEY);
		if (ObjectUtils.isEmpty(textInput)) {
			throw new InvalidInputDataException(String.format("Input param '%s' is mandatory", TEXT_INPUT_KEY));
		}
		try {
			String result = HashHelper.hash(input.getAction().getCode(), textInput);
			resp.setResult(result);
		} catch (NoSuchAlgorithmException e) {
			throw new InvalidInputDataException(String.format("Hash algorithm not supported: %s", input.getAction().getCode()));
		}
		
		return resp;
	}

}
