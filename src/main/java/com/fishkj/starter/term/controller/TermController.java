package com.fishkj.starter.term.controller;

import java.security.Principal;
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

/**
 * 服务器配置
 * @date: 2019年12月25日 上午12:49:06
 * @author: jiuzhou.hu
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TermController {
	
	private final MachineService machineService;
	private final VelocityManager velocityManager;
	
	@Value("${fish.term.resource:fish/term/resources}")
	private String resourcePath;

	/**
	 * 列表页
	 * @author jiuzhou.hu
	 * @date 2019年12月25日 上午12:49:15
	 * @return
	 */
	@GetMapping(value= "/term", produces="text/html; charset=utf-8")
	public String home() {
		String filePath = resourcePath + "/index.html";
		return Utils.readFromResource(filePath);
	}
	
	/**
	 * 列表数据
	 * @author jiuzhou.hu
	 * @date 2019年12月25日 上午12:49:22
	 * @return
	 */
	@PostMapping(value= "/term.json")
	public Map<String, Object> listData(Principal principal) {
		String userName = principal == null?null:principal.getName();
		List<Machine> data = machineService.list(userName);
		Map<String, Object> result = new HashMap<>();
		result.put("code", 0);
		result.put("count", data.size());
		result.put("data", data);
		return result;
	}
	
	/**
	 * 根据ID查询
	 * @author jiuzhou.hu
	 * @date 2019年12月25日 上午12:49:31
	 * @param id	允许为空，为空时new一个对象
	 * @param model
	 * @return
	 */
	@GetMapping(value= {"/term/get", "/term/get/{id}"}, produces="text/html; charset=utf-8")
	public String get(@PathVariable(value="id", required=false) String id, ModelMap model) {
		String filePath = resourcePath + "/get.html";
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("command", machineService.get(id).orElse(Machine.builder().port(22).build()));
		return velocityManager.parseVMContent(Utils.readFromResource(filePath), parameters);
	}
	
	/**
	 * 新增/保存配置
	 * @author jiuzhou.hu
	 * @date 2019年12月25日 上午12:50:13
	 * @param machine
	 * @return
	 */
	@PutMapping(value= "/term")
	public HttpEntity<String> save(Principal principal, @RequestBody Machine machine) {
		String userName = principal == null?null:principal.getName();
		machine.setUserId(userName);
		machineService.saveOrUpdate(machine);
		return new HttpEntity<String>("success");
	}
	
	/**
	 * 根据ID删除配置
	 * @author jiuzhou.hu
	 * @date 2019年12月25日 上午12:50:25
	 * @param id
	 * @return
	 */
	@DeleteMapping(value= "/term/{id}")
	public HttpEntity<String> remove(@PathVariable(value="id") String id) {
		machineService.remove(id);
		return new HttpEntity<String>("success");
	}
}
