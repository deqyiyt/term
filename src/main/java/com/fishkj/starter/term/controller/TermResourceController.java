package com.fishkj.starter.term.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fishkj.starter.term.utils.JsonUtils;
import com.fishkj.starter.term.utils.ScriptsUtils;
import com.fishkj.starter.term.utils.Utils;

/**
 * 读取静态资源
 * @date: 2019年12月25日 上午12:50:49
 * @author: jiuzhou.hu
 */
@RestController
@RequestMapping(value="/term")
public class TermResourceController {
	
	@Value("${fish.term.resource:fish/term/resources}")
	private String resourcePath;
	
	@GetMapping(value= "/res/**//{path:.+}.css", produces="text/css;charset=utf-8")
	public String css(HttpServletRequest request) {
		String filePath = resourcePath + request.getServletPath().replace("/term/res", "");
		return Utils.readFromResource(filePath);
	}
	
	@GetMapping(value= "/res/**//{path:.+}.js", produces="text/javascript;charset=utf-8")
	public String js(HttpServletRequest request) {
		String filePath = resourcePath + request.getServletPath().replace("/term/res", "");
		return ScriptsUtils.obfuscateScript(Utils.readFromResource(filePath));
	}
	
	@GetMapping(value= {"/res/**//{path:.+}.jpg"}, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] jpeg(HttpServletRequest request) throws IOException {
		String filePath = resourcePath + request.getServletPath().replace("/term/res", "");
		return Utils.readByteArrayFromResource(filePath);
	}
	
	@GetMapping(value= {"/res/**//{path:.+}.gif"}, produces = MediaType.IMAGE_GIF_VALUE)
	public byte[] gif(HttpServletRequest request) throws IOException {
		String filePath = resourcePath + request.getServletPath().replace("/term/res", "");
		return Utils.readByteArrayFromResource(filePath);
	}
	
	@GetMapping(value= {"/res/**//{path:.+}.png"}, produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] png(HttpServletRequest request) throws IOException {
		String filePath = resourcePath + request.getServletPath().replace("/term/res", "");
		return Utils.readByteArrayFromResource(filePath);
	}
	
	@GetMapping(value= {"/res/**//{path:.+}.woff", "/res/**//{path:.+}.woff2", "/res/**//{path:.+}.ttf"}, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public byte[] stream(HttpServletRequest request) throws IOException {
		String filePath = resourcePath + request.getServletPath().replace("/term/res", "");
		return Utils.readByteArrayFromResource(filePath);
	}
	
	@GetMapping(value="/csrf.js", produces="application/javascript; charset=utf-8")
    public String fishkjConfig(CsrfToken token) {
    	StringBuffer srcipt = new StringBuffer("var fish = fish||{};");
    	Map<String, Object> map = new HashMap<>();
    	if(token != null) {
    		map.put("csrfHeaderName", token.getHeaderName());
    		map.put("csrfParameterName", token.getParameterName());
    		map.put("csrfToken", token.getToken());
    	}
    	
    	srcipt.append("fish.csrf=").append(JsonUtils.buildNormalBinder().toJson(map)).append(";");
    	
        return ScriptsUtils.obfuscateScript(srcipt.toString());
    }
}
