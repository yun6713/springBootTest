package com.bonc.service;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bonc.config.RabbitConfig;

@Service
public class RabbitService {
	@Autowired
	RabbitTemplate rt;
	@Async
	public void asyncSend(Object msg) {
		rt.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.QUEUE, msg,new CorrelationData("lalala,happy!"));
	}
}
