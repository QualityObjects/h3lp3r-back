package com.qualityobjects.oss.h3lp3r.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories( 
		basePackages = {"com.qualityobjects.oss.h3lp3r.repository"}) 
@PropertySource({ "classpath:application.yml" })
public class DBConfiguration {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(DBConfiguration.class);

 
}