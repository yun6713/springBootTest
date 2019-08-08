package com.bonc.config;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

/**
 * rabbitmq配置类
 * 配置COnnectionFactory、AmqpTemplate
 * @author litianlin
 * @date   2019年8月7日下午5:16:42
 * @Description TODO
 */
@Configuration
@EnableRabbit
@RabbitListener(queues="queue1")
public class RabbitConfig {
	public static final String EXCHANGE="exchange1";
	public static final String QUEUE="queue1";
	public static final String BINDING="binding1";
//	@Bean(initMethod="afterPropertiesSet",destroyMethod="destroy")
//	public ConnectionFactory rabbitConnectionFactory() {
//		CachingConnectionFactory ccf=new CachingConnectionFactory(host,port);
////		ccf.setAddresses(addresses);//集群时设置地址
//		ccf.setPassword(password);
//		ccf.setUsername(username);
//		ccf.setHost(host);
//		return ccf;
//	}
	@Bean(initMethod="afterPropertiesSet")
//	@Scope("SCOPE_PROTOTYPE")
	@Primary
	public AmqpTemplate amqpTemplate(ConnectionFactory cf) {
		RabbitTemplate rt=new RabbitTemplate(cf);	
		rt.setConfirmCallback((correlationData,ack,cause)->{
			LoggerFactory.getLogger(RabbitTemplate.class)
			.info("ConfirmCallback：{},{},{}",correlationData,ack,cause);
		});
		return rt;
	}
	@Bean
	public Exchange exchange() {
		Exchange e=new DirectExchange(EXCHANGE);
		return e;
	}
	@Bean
	public Queue queue() {
		return new Queue(QUEUE);//默认持久化
	}
	@Bean
	public Binding binding() {
		return BindingBuilder.bind(queue())
				.to(exchange())
				.with(QUEUE)
				.noargs();
	}
	@RabbitHandler(isDefault=true)
	public void defaultHandler(Object obj) {
		LoggerFactory.getLogger(RabbitConfig.class).info("Message:{}",obj);
	}
	@RabbitHandler(isDefault=false)
	public void queue1andler(String info) {
		LoggerFactory.getLogger(RabbitConfig.class).info("queue1 Message:{}",info);
	}
}
