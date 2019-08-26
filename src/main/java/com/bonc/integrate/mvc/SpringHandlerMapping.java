package com.bonc.integrate.mvc;

import java.net.URI;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * spring handlermapping相关
 * @author litianlin
 * @date   2019年8月26日上午10:29:42
 * @Description TODO
 */
public class SpringHandlerMapping {
	Controller a;
	RequestMapping b;
	//参数，注解参数
	PathVariable c;
	RequestParam c1;
	RequestBody c2;
	RequestHeader c3;
	CookieValue c4;
	RequestAttribute c5;
	SessionAttribute c6;
	ModelAttribute c7;
	//参数，非注解参数
	RedirectAttributes d;//重定向属性集
	ModelAndView d1;
	//返回处理
	ResponseBodyAdvice e1;
	ControllerAdvice e2;
	ExceptionHandler e3;
	Model e4;
	//工具类
	RequestContextUtils f1;
	RequestContextHolder f2;
	//映射执行
	HandlerMapping g;
	DispatcherServlet g1;
	HandlerAdapter g2;
	HandlerExecutionChain g3;
	public void test() {
		URI uri=UriComponentsBuilder.fromUriString("")
				.encode().build(new HashMap());
		
	}
}
