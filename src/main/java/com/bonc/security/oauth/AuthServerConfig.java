package com.bonc.security.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.alibaba.druid.pool.DruidDataSource;
import com.bonc.security.springSecurity.JwtAuthFilter;

/**
 * oauth2认证服务器配置，类比springSecurity
 * @author Administrator
 *
 */
@Configuration
@EnableAuthorizationServer
@ConditionalOnExpression("${spring.security.enabled:}==true")
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter{
	public static final String RESOURCE_ID="test1";
	
	@Autowired
	AuthenticationManager manager;
	@Autowired
	RedisConnectionFactory rcf;
	@Autowired
	DruidDataSource dataSource;
	@Autowired
	@Qualifier("myFilter")
	AbstractAuthenticationProcessingFilter aFilter;
	@Autowired
	JwtAuthFilter filter;
	/*
	 * 配置OAuth2的客户端相关信息
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.jdbc(dataSource);
//		clients.withClientDetails(clientDetailsService)
//		clients.inMemory()
//			//客户端模式
//			.withClient("client_1").resourceIds(RESOURCE_ID)
//			.authorizedGrantTypes("client_credentials", "refresh_token")
//			.scopes("select").authorities("client").secret("$2a$10$LycPFgZnpC1PgRCnBJ/okOMZOOh4nv/J8yR6rM.qvULlSD9EkmvLm")
//			.and()
//			//密码模式
//			.withClient("client_2").resourceIds(RESOURCE_ID)
//			.authorizedGrantTypes("password", "refresh_token")
//			.scopes("select").authorities("client").secret("$2a$10$LycPFgZnpC1PgRCnBJ/okOMZOOh4nv/J8yR6rM.qvULlSD9EkmvLm");
		
	}
	
	/*
	 *  配置AuthorizationServerEndpointsConfigurer众多相关类
	 * (non-Javadoc)
	 * @see org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter#configure(org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer)
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(new RedisTokenStore(rcf))
			.authenticationManager(manager);
	}
	/*
	 * 配置AuthorizationServer安全认证的相关信息
	 * (non-Javadoc)
	 * @see org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter#configure(org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer)
	 */
	@Autowired
	PasswordEncoder pe;
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		//允许表单登录
		security.allowFormAuthenticationForClients()
//		.and()
//		.addFilterAt(aFilter, UsernamePasswordAuthenticationFilter.class)
//		.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
//		.authorizeRequests()
//		.antMatchers("/test","/h2console/*","/oauth/*").permitAll()
//		.anyRequest().authenticated()
//		.and()
//		.formLogin().permitAll().defaultSuccessUrl("/test1") //loginPage()用于指定自定义的多路页面路径
//		.and()
//		.logout().permitAll().deleteCookies("JSESSIONID")
		;
		
//		security.passwordEncoder(pe);
	}

}
