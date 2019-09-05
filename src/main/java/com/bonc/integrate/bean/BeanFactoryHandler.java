package com.bonc.integrate.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SystemPropertyUtils;

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
	PropertyResolver f2;
	Environment g;
	ConfigurableEnvironment h;
	PropertySource i;
	PropertySources j;
	PropertySourceLoader k;
	PropertySourcesPropertyResolver l;
	ImportBeanDefinitionRegistrar m;
	SystemPropertyUtils s1;
	ApplicationContext l1;
	InitializingBean m1;
	DisposableBean n;
	ObjectUtils o;
	BeanDefinitionBuilder p;
	ResourceLoader q1;
	MessageSource r1;
	ResourceUtils s;     
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		ConfigurableEnvironment env = beanFactory.getBean(ConfigurableEnvironment.class);
		System.out.println(env.getProperty("test"));
		System.out.println(env.getProperty("spring.mail.username"));
		MutablePropertySources ps=env.getPropertySources();
		ps.get("applicationConfig: [classpath:/application.yml]");
		System.out.println(env.getProperty("test"));
				
	}

}
