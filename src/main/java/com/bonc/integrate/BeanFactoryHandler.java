package com.bonc.integrate;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
/**
 * 操作bean Configuration metadata<br/>
 * 获取bean操作会跳过BeanPostProcessor<br/>
 * 
 * @author litianlin
 * @date   2019年8月16日上午10:06:46
 * @Description TODO
 */
@Component
public class BeanFactoryHandler implements BeanFactoryPostProcessor {
	PropertyPlaceholderConfigurer a;
	PropertySourcesPlaceholderConfigurer a2;//spring3.1后使用
	PropertyOverrideConfigurer b;
	EnvironmentPostProcessor e;
	AutowiredAnnotationBeanPostProcessor f;
	Environment g;
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
	}

}
