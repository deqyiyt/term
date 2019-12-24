package com.fishkj.starter.term.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fishkj.starter.term.repo.TermServerRepository;
import com.fishkj.starter.term.service.MachineService;
import com.fishkj.starter.term.service.impl.MachineServiceImpl;

@ConditionalOnMissingBean(MachineService.class)
@Configuration
public class TermConfiguration {
	
	@Bean
	public MachineService getMachineService(TermServerRepository termServerRepository) {
		return new MachineServiceImpl(termServerRepository);
	}

    @EnableJpaRepositories("com.fishkj.starter.term.repo")
    @EntityScan("com.fishkj.starter.term.pojo")
    @ComponentScan(basePackages = {"com.fishkj.starter.term.service"})
    @EnableTransactionManagement
    @EnableJpaAuditing
    public static class ServerConfiguration { }
}