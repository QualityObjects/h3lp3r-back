package com.qualityobjects.oss.h3lp3r.controller;

import com.qualityobjects.oss.h3lp3r.exception.QOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${services.url.prefix}")
public class RootController {


	@GetMapping(path = {"/", ""})
	public String index() throws QOException {
		return "H3lp3r REST API server";
	}

}
