package com.qualityobjects.oss.h3lp3r.controller;

import com.qualityobjects.oss.h3lp3r.exception.QOException;
import com.qualityobjects.oss.h3lp3r.service.StatsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${services.url.prefix}/stats")
public class StatsController {

	@Autowired
	private StatsService statsService;

	@GetMapping(path = {"/", ""})
	public String index() throws QOException {
		return "Stats API";
	}
}
