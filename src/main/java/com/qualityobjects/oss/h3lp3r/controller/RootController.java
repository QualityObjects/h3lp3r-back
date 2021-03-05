package com.qualityobjects.oss.h3lp3r.controller;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.qualityobjects.oss.h3lp3r.exception.QOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "${services.url.prefix}")
public class RootController {

	@Autowired BuildProperties build;

	@GetMapping(path = { "/", "" })
	public Mono<String> index() throws QOException {
		String formattedBuldTimstamp = build.getTime().atZone(ZoneId.of("Europe/Madrid")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		return Mono.fromCallable(() -> String.format("H3lp3r REST API - v. %s\nBuild timestamp: %s\n", build.getVersion(), formattedBuldTimstamp));
	}

	@GetMapping(path = { "/ip" })
	public Mono<String> ip(ServerHttpRequest request) throws QOException {
		return Mono.fromCallable(() -> RootController.getRealIp(request));
	}

	public static String getRealIp(ServerHttpRequest request) {
		HttpHeaders headers = request.getHeaders();
		
		List<String> values = headers.getOrDefault("X-Forwarded-For", headers.getOrDefault("X-Real-IP", List.of(request.getRemoteAddress().getHostString())));
		String clientIp = values.get(0);

		return clientIp;
	}


}
