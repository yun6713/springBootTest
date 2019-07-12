package com.bonc.security.oauth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(AuthServerConfig.RESOURCE_ID).stateless(true);
	}
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			.and()
			.requestMatchers().anyRequest()
			.and()
			.anonymous()
			.and()
			.authorizeRequests().antMatchers("/test1/**").authenticated();
	}
}
