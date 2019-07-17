package com.bonc.security.oauth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
@Configuration
@EnableResourceServer
@ConditionalOnExpression("${spring.security.enabled:}==true")
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(AuthServerConfig.RESOURCE_ID).stateless(true);
	}
	//配置安全访问权限
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		//标定作用范围，防止与spring security冲突。
			.requestMatchers().antMatchers(
//					"/h2/**",
					"/"+AuthServerConfig.RESOURCE_ID)
			.and()
			.authorizeRequests().anyRequest().authenticated();
	}
}
