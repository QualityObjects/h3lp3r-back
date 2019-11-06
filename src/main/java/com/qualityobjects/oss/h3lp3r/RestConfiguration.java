package com.qualityobjects.oss.h3lp3r;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.qualityobjects.oss.h3lp3r.domain.enums.Operation;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.qualityobjects.oss.h3lp3r.controllers")
public class RestConfiguration implements WebMvcConfigurer {

	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(RestConfiguration.class);

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("/index.html");
		registry.addViewController("/**/{spring:\\w+}").setViewName("forward:/");

	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// specifying static resource location for themes related files(css etc)
		registry.addResourceHandler("/**", "/").addResourceLocations("/static/");
	}
	
	@Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(Operation.CONVERTER);
    }
	
	
	
}
