package com.bonc.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.util.encoders.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.utils.FileUtils;

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
	@RequestMapping("/test2")
	public String test2() {
		return "success2";
	}
	@RequestMapping("/testRu")
	public String testRu() throws IOException {
		return FileUtils.file2String("classpath:es");
	}
	@RequestMapping("/testRu2")
	public String testRu2() throws IOException {
		//不可获取classpath的File
		return FileUtils.resource2String(new ClassPathResource("es"));
	}
	@RequestMapping("/projectPath")
	public String projectPath() throws IOException, URISyntaxException {
		//不可获取classpath的File
		return FileUtils.getProjectPath();
	}
}
