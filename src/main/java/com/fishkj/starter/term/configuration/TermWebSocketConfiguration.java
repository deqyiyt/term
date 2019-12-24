package com.fishkj.starter.term.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@ComponentScan(basePackages = {"com.fishkj.starter.term.controller", "com.fishkj.starter.term.socket", "com.fishkj.starter.term.manager", "com.fishkj.starter.term.spring", "com.fishkj.starter.term.init"})
public class TermWebSocketConfiguration {
 
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}