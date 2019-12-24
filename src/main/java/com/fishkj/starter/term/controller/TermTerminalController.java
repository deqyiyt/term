package com.fishkj.starter.term.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fishkj.starter.term.manager.VelocityManager;
import com.fishkj.starter.term.utils.Utils;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TermTerminalController {
	
	private final VelocityManager velocityManager;
	
	@Value("${fish.term.resource:fish/term/resources}")
	private String resourcePath;
	
	@GetMapping(value= "/term/terminal/{id}", produces="text/html; charset=utf-8")
	public String shell(@PathVariable(value="id") String id) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("id", id);
		String filePath = resourcePath + "/terminal.html";
		return velocityManager.parseVMContent(Utils.readFromResource(filePath), parameters);
	}
}
