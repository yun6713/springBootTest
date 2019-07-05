package com.bonc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
@EnableWebSecurity//启用安全配置
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${spring.security.enabled:false}")
	String enable;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//禁用csrf
		http.csrf().disable()
			.headers().frameOptions().disable();
		//开启验证
		if(Boolean.valueOf(enable)) {
			http.authorizeRequests().anyRequest().authenticated()
			.and()
			.formLogin().loginPage("/login").permitAll();
		}else {
			http.authorizeRequests().anyRequest().permitAll();
		}
	}
}
