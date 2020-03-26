package com.bonc.test.spring.aop.test2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;

import com.bonc.test.spring.aop.test1.AopTest;
@Configuration
public class AopTestConfig extends AopTest {
	@Bean
	@Scope("prototype")
	public AopTest aopTest2() {
		return new AopTest2();
	}
	@Order
	public static class AopTest2 extends AopTest {
		public void test() {
			System.out.println(this.getClass().getName());
		}
	}
}
