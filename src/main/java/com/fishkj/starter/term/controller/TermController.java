package com.fishkj.starter.term.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fishkj.starter.term.manager.VelocityManager;
import com.fishkj.starter.term.service.MachineService;
import com.fishkj.starter.term.socket.Machine;
import com.fishkj.starter.term.utils.Utils;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TermController {
	
	private final MachineService machineService;
	private final VelocityManager velocityManager;
	
	@Value("${fish.term.resource:fish/term/resources}")
	private String resourcePath;

	@GetMapping(value= "/term", produces="text/html; charset=utf-8")
	public String home() {
		String filePath = resourcePath + "/index.html";
		return Utils.readFromResource(filePath);
	}
	
	@PostMapping(value= "/term.json")
	public Map<String, Object> listData() {
		List<Machine> data = machineService.list();
		Map<String, Object> result = new HashMap<>();
		result.put("code", 0);
		result.put("count", data.size());
		result.put("data", data);
		return result;
	}
	
	@GetMapping(value= {"/term/get", "/term/get/{id}"}, produces="text/html; charset=utf-8")
	public String get(@PathVariable(value="id", required=false) String id, ModelMap model) {
		String filePath = resourcePath + "/get.html";
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("command", machineService.get(id).orElse(Machine.builder().port(22).build()));
		return velocityManager.parseVMContent(Utils.readFromResource(filePath), parameters);
	}
	
	@PutMapping(value= "/term")
	public HttpEntity<String> save(@RequestBody Machine machine) {
		machineService.saveOrUpdate(machine);
		return new HttpEntity<String>("success");
	}
	
	@DeleteMapping(value= "/term/{id}")
	public HttpEntity<String> remove(@PathVariable(value="id") String id) {
		machineService.remove(id);
		return new HttpEntity<String>("success");
	}
}
