package com.bonc.designPattern.struct.proxy;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import com.bonc.designPattern.struct.proxy.StaticProxy.WaterBucket;
/**
 * cglib代理，基于继承。通过MethodInterceptor添加控制逻辑<br/>
 * 核心类：Enhancer、MethodInterceptor
 * @author litianlin
 * @date   2019年9月4日下午4:42:58
 * @Description TODO
 */
public class CglibProxy {
	public static void main(String[] args) {
		Enhancer en = new Enhancer();
		en.setSuperclass(WaterBucket.class);
		//设置拦截MethodInterceptor
		en.setCallback(new MethodInterceptorImpl());
		WaterBucket wb=(WaterBucket) en.create();
		System.out.println(wb.getWater());
	}
	public static class MethodInterceptorImpl implements MethodInterceptor{

		@Override
		public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
//			System.out.println(arg1.invoke(arg0, arg2));
			//必须执行父类方法，否则死循环
//			System.out.println(arg3.invokeSuper(arg0, arg2));
			return arg3.invokeSuper(arg0, arg2);
		}
		
	}
}
