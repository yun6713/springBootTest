package com.bonc.designPattern.struct.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.bonc.designPattern.struct.proxy.StaticProxy.WaterBucket;
import com.bonc.designPattern.struct.proxy.StaticProxy.WaterContainer;
import com.bonc.designPattern.struct.proxy.StaticProxy.YinShuiJi;
/**
 * jdk动态代理；所有方法都会被拦截，便于统一增加逻辑。<br/>
 * 核心：InvocationHandler、Proxy.newProxyInstance()
 * @author litianlin
 * @date   2019年9月4日下午3:52:05
 * @Description TODO
 */
public class JdkProxy {
	public static void main(String[] args) {
		WaterContainer container=(WaterContainer) Proxy.newProxyInstance(JdkProxy.class.getClassLoader(),
				WaterBucket.class.getInterfaces(), new Handler(new YinShuiJi(new WaterBucket())));
		System.out.println(container.getWater());
		WaterContainer container2=(WaterContainer) Proxy.newProxyInstance(JdkProxy.class.getClassLoader(),
				WaterBucket.class.getInterfaces(), new GenericHandler<WaterContainer>(new YinShuiJi(new WaterBucket())));
		System.out.println(container2.getWater());
	}
	//代理执行器
	public static class Handler implements InvocationHandler{
		WaterContainer wc;
		public Handler(WaterContainer wc) {
			this.wc=wc;
		}
		@Override//proxy为Proxy子类实例，无意义
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Object result=method.invoke(wc, args);
			System.out.println("Do something...");
			return result;
		}
		
	}
	public static class GenericHandler<T> implements InvocationHandler{
		T t;
		public GenericHandler(T t) {
			this.t=t;
		}
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Object result=method.invoke(t, args);
			System.out.println("Do some generic things...");
			return result;
		}
		
	}
}
