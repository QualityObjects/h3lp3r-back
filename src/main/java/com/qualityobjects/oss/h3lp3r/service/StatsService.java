package com.qualityobjects.oss.h3lp3r.service;

import com.qualityobjects.oss.h3lp3r.repository.OperationLogRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

	@Autowired
	private OperationLogRepository olRepository;

	@SuppressWarnings("unused") 
	private static final Logger LOG = LoggerFactory.getLogger(StatsService.class);

}
