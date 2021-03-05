package com.qualityobjects.oss.h3lp3r.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import com.qualityobjects.oss.h3lp3r.domain.dto.OpInput;
import com.qualityobjects.oss.h3lp3r.domain.dto.OpResponse;
import com.qualityobjects.oss.h3lp3r.domain.enums.Operation;
import com.qualityobjects.oss.h3lp3r.exception.InvalidInputDataException;
import com.qualityobjects.oss.h3lp3r.exception.QOException;
import com.qualityobjects.oss.h3lp3r.service.HashService;

@RestController
@RequestMapping(path = "${services.url.prefix}/hash")
public class HashController {

	@Autowired
	private HashService hashService;

	@GetMapping(path = {"/", ""})
	public String index() throws QOException {
		return "H3lp3r Hash API";
	}

	@GetMapping(path = {"/{operation}"})
	public Mono<OpResponse> getHash(@PathVariable("operation") Operation op, @RequestParam MultiValueMap<String, String> params) throws QOException {
		if (op == null) {
			throw new InvalidInputDataException(String.format("Hash algorithm not supported"));
		}
		OpInput input = new OpInput(op, params.toSingleValueMap());
		
		return hashService.hash(input);
	}
}
