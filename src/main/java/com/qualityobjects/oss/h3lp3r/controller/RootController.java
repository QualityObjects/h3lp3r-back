package com.qualityobjects.oss.h3lp3r.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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
	
	@GetMapping(path = {"/ip"})
	public String ip(HttpServletRequest request) throws QOException {
		return RootController.getRealIp(request);
	}

	public static String getRealIp(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader("X-Real-IP"))
			.orElse(Optional.ofNullable(request.getHeader("X-Forwarded-For")).orElse(request.getRemoteAddr()));
	}
	

}
