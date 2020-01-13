package com.bonc.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bonc.entity.Response;
import com.bonc.entity.jpa.User;
import com.bonc.service.TestService;
import com.bonc.utils.FileUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
public class TestController {
	static Logger log = LoggerFactory.getLogger(TestController.class);
	@Autowired
	TestService ts;
	@RequestMapping(value="/test")
	public Object test(HttpServletRequest req) {
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
	@RequestMapping("/dateFormat")
	public Date testDateFormat(@RequestParam Date date) throws IOException {
		Date d=new Date();
		return d;
	}
	@RequestMapping("/requestParam")
	public String testRequestParam(@RequestParam String str) throws IOException {
		return str;
	}
	@RequestMapping("/testAsyncReq")
	public Callable<String> testAsyncReq() throws IOException {
		return new Callable<String>() {
			@Override
			public String call() throws Exception {
				return "async test";
			}
			
		};
	}
	@RequestMapping("/testAsyncService")
	public String testAsyncService() throws IOException {
		ts.testAsyncService();
		ts.testAsyncSecurity();
		return "ok";
	}
	@ApiOperation("test class")
	@RequestMapping(value="/swagger",method=RequestMethod.POST)
	@ApiImplicitParam(name = "user", value = "用户", required = true, dataType="User")
	public Response<User> swagger(@RequestBody User user) {
		log.info(user.toString());
		return null;
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
