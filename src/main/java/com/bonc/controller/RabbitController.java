package com.bonc.controller;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitController {
	@Autowired
	AmqpTemplate at;
	@Autowired
	AmqpAdmin aa;
	@RequestMapping("/rabbit")
	public String test() {
		aa.declareQueue(new Queue("test"));
		return "success";
	}
	@RequestMapping("/rabbit1")
	public String test1() {
		at.convertAndSend("exchang1", "queueq", "hello world");
		
		return "success";
	}
}
