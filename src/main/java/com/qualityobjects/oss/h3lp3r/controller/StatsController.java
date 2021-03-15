package com.qualityobjects.oss.h3lp3r.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.qualityobjects.oss.h3lp3r.domain.dto.OperationsByDateRange;
import com.qualityobjects.oss.h3lp3r.exception.QOException;
import com.qualityobjects.oss.h3lp3r.service.StatsService;

import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${services.url.prefix}/stats")
public class StatsController {

	@Autowired
	private StatsService statsService;

	@GetMapping(path = { "/", "" })
	public String index() throws QOException {
		return "Stats API";
	}

	@GetMapping(path = { "/since_date" })
	public OperationsByDateRange sinceDate(@RequestParam(name = "since", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since, 
			@RequestParam(name = "interval", defaultValue = "10m") String interval) throws QOException, IOException {
		if (since == null) {
			since = LocalDateTime.now().minusHours(24);
		}		
		
		return statsService.getAggregationsOnOperation(since, new DateHistogramInterval(interval));
	}


}
