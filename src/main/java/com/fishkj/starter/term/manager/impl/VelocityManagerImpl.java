package com.fishkj.starter.term.manager.impl;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.springframework.stereotype.Service;

import com.fishkj.starter.term.manager.VelocityManager;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VelocityManagerImpl implements VelocityManager {

	@Override
	public String parseVMContent(String templateContent, Map<String, Object> contextParameters) {
		if (!RuntimeSingleton.isInitialized())
			throw new RuntimeException("Velocity initialize failed");
		if (log.isDebugEnabled()){
			log.debug("Start parsing velocity template");
		}
		try{
			VelocityContext context = new VelocityContext();
			for (String key : contextParameters.keySet()){
				context.put(key, contextParameters.get(key));
			}
			StringWriter writer = new StringWriter();
			Velocity.evaluate(context, writer, "jumbovm", templateContent);
			String result = writer.getBuffer().toString();
			return result;
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException("Parse Velocity Template Error");
		}
	}

}
