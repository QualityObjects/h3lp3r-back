package com.qualityobjects.oss.h3lp3r.config;

import com.qualityobjects.oss.h3lp3r.domain.enums.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.convert.ConverterBuilder;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableWebFlux
public class RxRestConfiguration implements WebFluxConfigurer {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(RxRestConfiguration.class);

	@Override
    public void addFormatters(FormatterRegistry registry) {
		
        registry.addConverter(ConverterBuilder.reading(String.class, Operation.class, (src) -> Operation.of(src)).getReadingConverter());
    }
	
}
