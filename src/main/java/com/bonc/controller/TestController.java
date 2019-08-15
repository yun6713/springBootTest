package com.bonc.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bonc.utils.FileUtils;

@RestController
public class TestController {
	@RequestMapping("/test")
	public Object test(HttpServletRequest req) {
		return new String[] {"success"};
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
	@ExceptionHandler
	@ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR)
	public Object handleException(Exception ex,HttpServletRequest req) {
		ex.printStackTrace();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("respCode", "11");
		String url=req.getServletPath();
		System.out.println(url);
		jsonObject.put("respMsg", "服务器异常");
		return jsonObject;
	}
}
