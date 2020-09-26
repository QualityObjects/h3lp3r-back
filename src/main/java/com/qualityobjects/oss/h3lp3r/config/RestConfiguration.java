package com.qualityobjects.oss.h3lp3r.config;

import com.qualityobjects.oss.h3lp3r.domain.enums.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc()
public class RestConfiguration implements WebMvcConfigurer {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(RestConfiguration.class);

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
