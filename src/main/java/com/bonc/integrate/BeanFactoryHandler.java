package com.bonc.integrate;

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
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
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
	ApplicationContext l;
	InitializingBean m;
	DisposableBean n;
	ObjectUtils o;
	BeanDefinitionBuilder p;
	ResourceLoader q;
	MessageSource r;
	ResourceUtils s;     
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
	}

}
