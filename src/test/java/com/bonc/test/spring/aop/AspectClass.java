package com.bonc.test.spring.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy
public class AspectClass {
	@Before("within(com.bonc.test.spring.aop.test1.*)")
	public void testWithin() {
		System.out.println("testWithin");
	};
	@Before("target(com.bonc.test.spring.aop.test1.AopTest)")
	public void testTarget() {
		System.out.println("testTarget");
	};
	//@within、@target
	@After("@within(com.bonc.test.spring.aop.test1.TestAop)")
	public void testWithinA() {
		System.out.println("testWithinA");
	};
	@After("@target(com.bonc.test.spring.aop.test1.TestAop)")
	public void testTargetA() {
		System.out.println("testTargetA");
	};
	@After("@within(org.springframework.core.annotation.Order)")
	public void testWithinA2() {
		System.out.println("testWithinA2");
	};
	@After("@target(org.springframework.core.annotation.Order)")
	public void testTargetA2() {
		System.out.println("testTargetA2");
	};
}
