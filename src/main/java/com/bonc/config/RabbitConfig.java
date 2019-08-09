package com.bonc.config;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * rabbitmq配置类
 * 配置ConnectionFactory、AmqpTemplate
 * publish、consume可靠性通过ack机制完成
 * @author litianlin
 * @date   2019年8月7日下午5:16:42
 * @Description TODO
 */
@Configuration
@EnableRabbit
@RabbitListener(queues="queue1",
errorHandler="queue1ErrorHandler",
concurrency="5")//listener并发数，默认值在容器中设置
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
	public RabbitTemplate amqpTemplate(ConnectionFactory cf) {
		RabbitTemplate rt=new RabbitTemplate(cf);	
		//confirm在消息发送后异步返回，处理发送端发送失败。Channel断开时，ack立即返回false
		rt.setConfirmCallback((correlationData,ack,cause)->{
			if(!ack)//记录correlationData中信息
			LoggerFactory.getLogger(RabbitTemplate.class)
			.info("ConfirmCallback：{},{},{}",correlationData,ack,cause);
		});//publisher-confirm为true有效
		rt.setMessageConverter(jsonMessageConverter());//json序列化对象
		rt.setUsePublisherConnection(true);//publisher、consumer使用不同connection，避免死锁；相互嵌套时不可如此配置
//		rt.setChannelTransacted(transactional);//信道事务，影响性能
		MessageRecoverer mr;
		rt.setRetryTemplate(retry());
		rt.setRecoveryCallback(context->{LoggerFactory.getLogger(RabbitTemplate.class).error("setRecoveryCallback context:{}",context);return null;});
		return rt;
	}
	//重试配置,RabbitRetryTemplateCustomizer
	@Bean
	public RetryTemplate retry() {
		RetryTemplate rt=new RetryTemplate();
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
	    backOffPolicy.setInitialInterval(500);
	    backOffPolicy.setMultiplier(10.0);
	    backOffPolicy.setMaxInterval(10000);
	    rt.setBackOffPolicy(backOffPolicy);
		return rt;
	}
	/**
	 * 对象序列化策略，默认jdk；消息正文为byte[]，此处转String，后转byte[]
	 * @return
	 */
	@Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
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
	@RabbitHandler(isDefault=true)//queue1默认监听器
	public void defaultHandler(Object obj) {
		LoggerFactory.getLogger(RabbitConfig.class).info("Message:{}",obj);
	}
	@RabbitHandler(isDefault=false)
	public void queue1andler(String info) {
		LoggerFactory.getLogger(RabbitConfig.class).info("queue1 Message:{}",info);
	}

	/**
	 * RabbitListenerErrorHandler，listener异常处理，无默认实现；无法处理时异常上抛到容器
	 * ErrorHandler，listener容器异常处理，默认ConditionalRejectingErrorHandler
	 * @return
	 */
	@Bean("queue1ErrorHandler")
	public RabbitListenerErrorHandler rabbitListenerErrorHandler() {
		return new RabbitListenerErrorHandler() {
			@Override
			public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message,
					ListenerExecutionFailedException exception) throws Exception {
				LoggerFactory.getLogger(RabbitConfig.class).error("Message info:{};Error info:{}",message,exception.getMessage());
				throw exception;
			}
		};
	}
	
}
