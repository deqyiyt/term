package com.fishkj.starter.term.start;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private SecurityProperties securityProperties;
	
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
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())   //在Spring Security 5.0中新增了多种加密方式，页改变了密码的格式
                .withUser("test").password(new BCryptPasswordEncoder().encode("test")).roles("USER")
                .and()
                .withUser(securityProperties.getUser().getName())
                .password(new BCryptPasswordEncoder().encode(securityProperties.getUser().getPassword()))
                .roles(securityProperties.getUser().getRoles().stream().toArray(String[]::new))
                ;
    }
}
