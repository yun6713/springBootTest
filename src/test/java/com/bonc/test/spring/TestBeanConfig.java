package com.bonc.test.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestBeanConfig {
	@Bean(value="nana",initMethod="init",destroyMethod="des")
	public TestBean testBean() {
		return new TestBean();
	}
	@Bean
	public BeanPostProcessor bpp() {
		return new BeanPostProcessor() {
			@Override
			public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
				System.out.println(1);
				return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
			}
			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				System.out.println(3);
				return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
			}
		};
	}
}
