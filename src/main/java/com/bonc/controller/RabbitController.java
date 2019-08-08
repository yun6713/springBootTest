package com.bonc.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.utils.FileUtils;

@RestController
public class RabbitController {
	@Autowired
	AmqpTemplate at;
	@RequestMapping("/rabbit1")
	public String test() {
		at.convertAndSend("exchang1", "queueq", "hello world");
		
		return "success";
	}
}
