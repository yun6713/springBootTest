package com.bonc.integrate.bean;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
/**
 * 处理加密密码<br/>
 * aop使用BeanPostProcessor，返回代理类<br/>
 * 避免使用@Configuration+@Bean方式声明BeanPostProcessor，容器无法发现<br/>
 * @author litianlin
 * @date   2019年8月16日上午8:54:13
 * @Description TODO
 */
@Component
public class BeanHandler implements BeanPostProcessor{
	private static final Logger log=LoggerFactory.getLogger(BeanHandler.class);
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		//反射修改密码
		if(bean instanceof DataSource){
			String password=(String) invoke(bean,"getPassword",null,null);
			log.info("datasource password:{}",password);
		}
		return bean;
	}
	private Object invoke(Object target,String methodName,Class<?>[] pTypes,Object[] args) {
		Objects.requireNonNull(target, "Target can't be null.");
		Method method=ReflectionUtils.findMethod(target.getClass(), methodName, pTypes);
		if(method==null) {
			log.error("Can't find method,class:{},methodName:{}, paramTypes:{}", 
					target.getClass().getName(),methodName,pTypes==null?"":Arrays.toString(pTypes));
			throw new RuntimeException("Can't find method.");
		}
		if(!method.isAccessible())
			method.setAccessible(true);
		return ReflectionUtils.invokeMethod(method, target, args);
	}
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		//输出密码
		if(bean instanceof DataSource){
			String password=(String) invoke(bean,"getPassword",null,null);
			log.info("datasource password modified:{}",password);
		}
		return bean;
	}
}
