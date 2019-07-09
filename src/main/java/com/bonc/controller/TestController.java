package com.bonc.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	@RequestMapping("/test")
	public String test() {
		return "success";
	}
	@PreAuthorize(value = "hasRole('db')")
	@RequestMapping("/test1")
	public String test1() {
		return "success1";
	}
}
