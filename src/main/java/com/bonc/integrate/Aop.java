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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 * Aop测试类<br/>
 * 1，开启aop：@Configuration+@EnableAspectJAutoProxy<br/>
 * 2，声明切面：@Aspect+@Component<br/>
 * 3，声明切点：@Pointcut；execution表达式：[modifier] reType package.class.method(argTypes) [throws]<br/>
 * 4，声明通知：@Before、@After、@AfterReturning、@AfterThrowing、@Around；@Around必须声明返回值<br/>
 * 5，引入增强：@DeclareParents<br/>
 * @author litianlin
 * @date   2019年8月22日上午9:23:04
 * @Description TODO
 */
@Component
@Aspect
public class Aop {
	//aop增强
	@DeclareParents( value="com.bonc.controller.H2Controller", //需要增加功能的类，+子类；*全部
			defaultImpl = AopTestInterfaceImpl.class ) //新增功能实现类
			public AopTestInterface encoreable;  //新增功能接口
	public static interface AopTestInterface{
		String test();
	}
	public static class AopTestInterfaceImpl implements AopTestInterface{
		@Override
		public String test() {
			return "aop enhance test";
		}
	}
	private static final Logger log=LoggerFactory.getLogger(Aop.class);
	@Pointcut(value="execution(* com.bonc.controller.H2Controller.insertUser(java.lang.String)) && args(info)")
	public void pointcut(String info) {};
	@Before(value="pointcut(info)")
	public void before(JoinPoint jp,String info) {
		log.info("{}",info);
	}
	@After(value="pointcut(info)",argNames="jp,info")
	public void after(JoinPoint jp,String info) {
		log.info("{}",jp);
	}
	@AfterReturning(value="pointcut(info)",returning="obj")
	public void afterReturning(JoinPoint jp,String info,Object obj) {
		log.info("{}",obj);
	}
	@AfterThrowing(value="pointcut(info)",throwing="obj")
	public void afterThrowing(JoinPoint jp,String info,Object obj) {
		log.info("{}",obj);
	}
	@Around(value = "pointcut(info)")
	public Object around(ProceedingJoinPoint jp,String info) throws Throwable {
		Object obj=jp.proceed(jp.getArgs());
		log.info("{}","121");
		return obj;
	}
	
}
