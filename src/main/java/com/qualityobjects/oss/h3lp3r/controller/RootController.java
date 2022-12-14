package com.qualityobjects.oss.h3lp3r.controller;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.qualityobjects.oss.h3lp3r.exception.QOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${services.url.prefix}")
public class RootController {

	@Autowired BuildProperties build;

	@GetMapping(path = { "/", "" })
	public String index() throws QOException {
		String formattedBuldTimstamp = build.getTime().atZone(ZoneId.of("Europe/Madrid")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		return String.format("H3lp3r REST API - v. %s\nBuild timestamp: %s\n", build.getVersion(), formattedBuldTimstamp);
	}

	@GetMapping(path = { "/ip" })
	public String ip(HttpServletRequest request) throws QOException {
		return RootController.getRealIp(request);
	}

	public static String getRealIp(HttpServletRequest request) {
		String clientIp = Optional.ofNullable(request.getHeader("X-Forwarded-For"))
				.orElse(Optional.ofNullable(request.getHeader("X-Real-IP")).orElse(request.getRemoteAddr()));
		return clientIp.contains(",") ? clientIp.substring(0, clientIp.indexOf(',')).trim() : clientIp;
	}


}
