package com.qualityobjects.oss.h3lp3r.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AspectConfiguration {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(AspectConfiguration.class);
	
}
