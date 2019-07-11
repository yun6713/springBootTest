package com.bonc.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	@RequestMapping("/test")
	public String test(HttpServletRequest req) {
		return "success";
	}
	@RequestMapping("/test1")
	public String test1() {
		return "success1";
	}
}
