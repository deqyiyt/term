package com.fishkj.starter.term.start;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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
      .antMatchers("/login").permitAll()
      .antMatchers("/logout").permitAll()
      .antMatchers("/images/**").permitAll()
      .antMatchers("/js/**").permitAll()
      .antMatchers("/css/**").permitAll()
      .antMatchers("/fonts/**").permitAll()
      .antMatchers("/").permitAll()
      .anyRequest().authenticated()
      .and()
      .sessionManagement()
      .and()
      .logout()
      .and()
      .httpBasic();

  }
}
