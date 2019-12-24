package com.fishkj.starter.term.manager;

import java.util.Map;

public interface VelocityManager {
	
	String parseVMContent(String templateContent, Map<String, Object> contextParameters);
}
