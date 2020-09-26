package com.qualityobjects.oss.h3lp3r;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
	"com.qualityobjects.oss.h3lp3r.config",
	"com.qualityobjects.oss.h3lp3r.controller",
	"com.qualityobjects.oss.h3lp3r.service",
	"com.qualityobjects.oss.h3lp3r.aspect",
})
public class H3lperApplication {

	public static void main(String[] args) {
		SpringApplication.run(H3lperApplication.class, args);
	}

}
