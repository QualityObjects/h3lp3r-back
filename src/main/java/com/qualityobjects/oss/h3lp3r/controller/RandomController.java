package com.qualityobjects.oss.h3lp3r.controller;

import com.qualityobjects.oss.h3lp3r.domain.dto.OpInput;
import com.qualityobjects.oss.h3lp3r.domain.dto.OpResponse;
import com.qualityobjects.oss.h3lp3r.domain.enums.Operation;
import com.qualityobjects.oss.h3lp3r.exception.QOException;
import com.qualityobjects.oss.h3lp3r.service.RandomGeneratorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${services.url.prefix}/random")
public class RandomController {

	@Autowired
	private RandomGeneratorService randomGenService;
	
	@GetMapping(path = {"/number/decimal"})
	public OpResponse getRandomDec( @RequestParam MultiValueMap<String, String> params) throws QOException {
		OpInput input = new OpInput(Operation.RANDOM_NUM_DEC, params.toSingleValueMap());
		
		return randomGenService.randomNumber(input);
	}

	@GetMapping(path = {"/number/int"})
	public OpResponse getRandomInt( @RequestParam MultiValueMap<String, String> params) throws QOException {
		OpInput input = new OpInput(Operation.RANDOM_NUM_INT, params.toSingleValueMap());
		
		return randomGenService.randomNumber(input);
	}

	@GetMapping(path = {"/names"})
	public OpResponse getRandomNames( @RequestParam MultiValueMap<String, String> params) throws QOException {
		OpInput input = new OpInput(Operation.RANDOM_NAMES, params.toSingleValueMap());
		
		return randomGenService.randomNames(input);
	}
}
