package com.fishkj.starter.term.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fishkj.starter.term.utils.Utils;

@RestController
public class TermResourceController {
	
	@Value("${fish.term.resource:fish/term/resources}")
	private String resourcePath;
	
	@GetMapping(value= "/term/**//{path:.+}.css", produces="text/css;charset=utf-8")
	public String css(HttpServletRequest request) {
		String filePath = resourcePath + request.getServletPath().replace("/term", "");
		return Utils.readFromResource(filePath);
	}
	
	@GetMapping(value= "/term/**//{path:.+}.js", produces="text/javascript;charset=utf-8")
	public String js(HttpServletRequest request) {
		String filePath = resourcePath + request.getServletPath().replace("/term", "");
		return Utils.readFromResource(filePath);
	}
	
	@GetMapping(value= {"/term/**//{path:.+}.jpg"}, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] jpeg(HttpServletRequest request) throws IOException {
		String filePath = resourcePath + request.getServletPath().replace("/term", "");
		return Utils.readByteArrayFromResource(filePath);
	}
	
	@GetMapping(value= {"/term/**//{path:.+}.gif"}, produces = MediaType.IMAGE_GIF_VALUE)
	public byte[] gif(HttpServletRequest request) throws IOException {
		String filePath = resourcePath + request.getServletPath().replace("/term", "");
		return Utils.readByteArrayFromResource(filePath);
	}
	
	@GetMapping(value= {"/term/**//{path:.+}.png"}, produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] png(HttpServletRequest request) throws IOException {
		String filePath = resourcePath + request.getServletPath().replace("/term", "");
		return Utils.readByteArrayFromResource(filePath);
	}
	
	@GetMapping(value= {"/term/**//{path:.+}.woff", "/term/**//{path:.+}.woff2", "/term/**//{path:.+}.ttf"}, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public byte[] stream(HttpServletRequest request) throws IOException {
		String filePath = resourcePath + request.getServletPath().replace("/term", "");
		return Utils.readByteArrayFromResource(filePath);
	}
}
