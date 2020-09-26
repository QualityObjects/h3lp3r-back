package com.qualityobjects.oss.h3lp3r.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.qualityobjects.oss.h3lp3r.domain.document.OperationLog;
import com.qualityobjects.oss.h3lp3r.domain.enums.Operation;
import com.qualityobjects.oss.h3lp3r.exception.QOException;
import com.qualityobjects.oss.h3lp3r.repository.OperationLogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${services.url.prefix}/dummy")
public class DummyController {

	@Autowired
	private OperationLogRepository olRepository;
	
	@GetMapping(path = {"/", ""})
	public String index() throws QOException {
		return "Dummy services";
	}

	@PostMapping(path = {"/test"})
	public OperationLog post(HttpServletRequest hsr) throws QOException {
		OperationLog ol = new OperationLog();
		ol.setClientIp(hsr.getRemoteAddr());
		ol.setOperationTimestamp(LocalDateTime.now());
		ol.setAction(Operation.MD5);
		ol.setDuration(LocalDateTime.now().getNano() / 1000L);
		ol.setParams(Map.of("ts", LocalDateTime.now().toString(), "prueba", "Esto es una cadena", "number", "12243", "lista", List.of("uno", "dos", "tres")));
		return olRepository.save(ol);
	}

	@GetMapping(path = {"/test/{id}"})
	public OperationLog getRandomNames( @PathVariable("id") String id) throws QOException {
		OperationLog ol = olRepository.findById(id).orElseThrow();
		ol.getParams().put("date nano", ol.getOperationTimestamp().getNano());
		return ol;
	}
}
