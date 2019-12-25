package com.fishkj.starter.term.start;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/term/res/**","**.js", "**.css", "**.png", "**.jpg");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.headers().frameOptions().disable()
		.and()
			.authorizeRequests()
			.antMatchers("/term/terminal/**").permitAll()
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
}
