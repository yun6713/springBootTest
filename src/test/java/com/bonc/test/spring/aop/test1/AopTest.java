package com.bonc.test.spring.aop.test1;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@TestAop
public class AopTest {
	public void test() {
		System.out.println(this.getClass().getName());
	}
	public void test2() {
		System.out.println("test2");
	}
}
