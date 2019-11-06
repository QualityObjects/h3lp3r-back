package com.qualityobjects.oss.h3lp3r.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qualityobjects.oss.h3lp3r.domain.dto.OpInput;
import com.qualityobjects.oss.h3lp3r.domain.dto.OpResponse;
import com.qualityobjects.oss.h3lp3r.domain.enums.Operation;
import com.qualityobjects.oss.h3lp3r.exception.InvalidInputDataException;
import com.qualityobjects.oss.h3lp3r.exception.QOException;
import com.qualityobjects.oss.h3lp3r.service.BaseEncodingService;

@RestController
@RequestMapping(path = "${services.url.prefix}/base")
public class BaseEncodingController {

	public static final String ACTION_ENCODE = "encode";
	public static final String ACTION_DECODE = "decode";

	@Autowired
	private BaseEncodingService baseService;
	

	@GetMapping(path = {"/64/{action}"})
	public OpResponse getRandomNames(@PathVariable("action") String action, @RequestParam MultiValueMap<String, String> params) throws QOException {
		Operation op;
		if (ACTION_ENCODE.equals(action) ) {
			op = Operation.ENC_BASE64;
		} else if (ACTION_DECODE.equals(action) ){
			op = Operation.DEC_BASE64;
		} else {
			throw new InvalidInputDataException(String.format("Operation not supported: '%s'", action));
		}
		OpInput input = new OpInput(op, params.toSingleValueMap());
		
		return baseService.base64(input);
	}
}
