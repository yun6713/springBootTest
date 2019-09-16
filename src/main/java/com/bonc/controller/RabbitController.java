package com.bonc.controller;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.service.RabbitService;

@RestController
public class RabbitController {
	@Autowired
	RabbitTemplate rt;
	@Autowired
	RabbitService rs;
	@Autowired
	AmqpAdmin aa;
	@RequestMapping("/rabbit")
	public String test() {
		aa.declareQueue(new Queue("test"));
		return "success";
	}
	@RequestMapping("/rabbit1/{info}")
	public String test1(@PathVariable String info) {
		rs.asyncSend(info);
//		rt.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.QUEUE, info,new CorrelationData("lalala,happy!"));
//		rt.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.QUEUE, Integer.valueOf(1),new CorrelationData("lalala,happy2!"));
		return "success";
	}
}
