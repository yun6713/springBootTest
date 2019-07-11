package com.bonc.security.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * oauth2认证服务器配置，类比springSecurity
 * @author Administrator
 *
 */
//@Configuration
//@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter{
	public static final String RESOURCE_ID="test1";
	
	@Autowired
	AuthenticationManager manager;
	@Autowired
	RedisConnectionFactory rcf;
	/*
	 * 配置权限查询
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		
//		clients.withClientDetails(clientDetailsService)
		clients.inMemory()
			//客户端模式
			.withClient("client_1").resourceIds(RESOURCE_ID)
			.authorizedGrantTypes("client_credentials", "refresh_token")
			.scopes("select").authorities("client").secret("$2a$10$LycPFgZnpC1PgRCnBJ/okOMZOOh4nv/J8yR6rM.qvULlSD9EkmvLm")
			.and()
			//密码模式
			.withClient("client_2").resourceIds(RESOURCE_ID)
			.authorizedGrantTypes("password", "refresh_token")
			.scopes("select").authorities("client").secret("$2a$10$LycPFgZnpC1PgRCnBJ/okOMZOOh4nv/J8yR6rM.qvULlSD9EkmvLm");
		
	}
	/*
	 * 配置非安全项；Configure the non-security features of the Authorization Server endpoints
	 * (non-Javadoc)
	 * @see org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter#configure(org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer)
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(new RedisTokenStore(rcf))
			.authenticationManager(manager);
	}
	/*
	 * 安全策略
	 * (non-Javadoc)
	 * @see org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter#configure(org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer)
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		//允许表单登录
		security.allowFormAuthenticationForClients();
	}
}
