package com.bonc.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * rabbitmq配置类
 * 配置COnnectionFactory、AmqpTemplate
 * @author litianlin
 * @date   2019年8月7日下午5:16:42
 * @Description TODO
 */
@Configuration
public class RabbitConfig {
//	@Bean(initMethod="afterPropertiesSet",destroyMethod="destroy")
//	public ConnectionFactory rabbitConnectionFactory() {
//		CachingConnectionFactory ccf=new CachingConnectionFactory(host,port);
//		ccf.setPassword(password);
//		ccf.setUsername(username);
//		ccf.setHost(host);
//		return ccf;
//	}
	@Bean(initMethod="afterPropertiesSet")
	@Scope("SCOPE_PROTOTYPE")
	public AmqpTemplate amqpTemplate(ConnectionFactory cf) {
		RabbitTemplate rt=new RabbitTemplate(cf);		
		return rt;
	}
	
}
