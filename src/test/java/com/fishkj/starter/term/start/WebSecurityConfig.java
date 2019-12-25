package com.fishkj.starter.term.start;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.headers().frameOptions().disable()
		.and()
			.authorizeRequests()
			.antMatchers("/term/terminal/**", "/term/res/**").permitAll()
			.antMatchers("**.js", "**.css", "**.png", "**.jpg").permitAll()
			.anyRequest().authenticated()
		.and()
			.sessionManagement()
		.and()
			.logout()
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true)
		.and()
			.httpBasic();
	}
	@Bean
	public SessionRegistry getSessionRegistry(){
		return new SessionRegistryImpl();
	}
}
