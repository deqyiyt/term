package com.fishkj.starter.term.manager;

import java.util.Map;

public interface VelocityManager {
	
	/**
	 * 通过Velocity模板替换字符串
	 * @author jiuzhou.hu
	 * @date 2019年12月25日 上午12:33:07
	 * @param templateContent		模板内容
	 * @param contextParameters		模板变量
	 * @return
	 */
	String parseVMContent(String templateContent, Map<String, Object> contextParameters);
}
