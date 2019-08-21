package com.bonc.integrate;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareParents;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class Aop {
	//aop增强
	@DeclareParents( value="com.bonc.controller.H2Controller", //需要增加功能的类，+子类；*全部
			defaultImpl = AopTestInterfaceImpl.class ) //新增功能实现类
			public static AopTestInterface encoreable;  //新增功能接口
	public static interface AopTestInterface{
		String test();
	}
	public static class AopTestInterfaceImpl implements AopTestInterface{
		@Override
		public String test() {
			return "aop test";
		}
	}
	@Pointcut(value="execution(* com.bonc.controller.H2Controller.insertUser(java.lang.String)) && args(info)")
	public void pointcut(String info) {};
	@Before(value="pointcut(info)")
	public void before(ProceedingJoinPoint jp,String info) {
		System.out.println(info);
	}
	@After(value="pointcut(info)",argNames="jp,info")
	public void after(ProceedingJoinPoint jp,String info) {
		System.out.println(jp);
	}
	@AfterReturning(value="pointcut(info)",returning="obj")
	public void afterReturning(ProceedingJoinPoint jp,String info,Object obj) {
		System.out.println(obj);
	}
	@AfterThrowing(value="pointcut(info)",throwing="obj")
	public void afterThrowing(ProceedingJoinPoint jp,String info,Object obj) {
		System.out.println(obj);
	}
	@Around(value = "pointcut(info)")
	public void around(ProceedingJoinPoint jp,String info) throws Throwable {
		jp.proceed(jp.getArgs());
		System.out.println(121);
	}
	
}
