package com.bonc.test.spring;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("nana")
public class TestBean implements InitializingBean, DisposableBean{
	@Value("lalala")
	String hi;
	public TestBean() {
		System.out.println("TestBean");
	}
	public void init() {
		System.out.println("init");
		this.hi="wangna";
	}
	@PostConstruct
	public void postConstruct() {
		System.out.println("postConstruct");
	}
	@PreDestroy
	public void preDestroy() {
		System.out.println("preDestroy");
	}
	@Override
	public void destroy() throws Exception {
		System.out.println("destroy");
	}
	public void des() throws Exception {
		System.out.println("des");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("afterPropertiesSet");
	}
}
