package com.fishkj.starter.term.init;

import org.apache.velocity.runtime.RuntimeSingleton;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VelocityRunner implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		if(!RuntimeSingleton.isInitialized()) {
    		RuntimeSingleton.setProperty("resource.loader", "class");
    		RuntimeSingleton.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			try{
				RuntimeSingleton.init();
				if(log.isDebugEnabled()) {
					log.debug("Velocity初始化完成");
				}
			}catch (Exception e){
				if(log.isErrorEnabled()) {
					log.error("Velocity初始化失败");
				}
			}
    	} else {
    		if(log.isDebugEnabled()) {
				log.debug("Velocity已完成初始化");
			}
    	}
	}
}
