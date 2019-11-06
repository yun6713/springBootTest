package com.bonc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {
	@Autowired
	ConversionService cs;
	@Override
	protected void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/test1");
		
	}
	@Override
	//异步请求配置:拦截器、线程池、延时
	protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setTaskExecutor(taskExecutor())
			.setDefaultTimeout(300);
	}
	@Bean
	public AsyncTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor pool=new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(13);
		return pool;
	}
	@Override//静态资源路径
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		 registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	}
	@Override//注册formatter格式化
	protected void addFormatters(FormatterRegistry registry) {
//		registry.addFormatter(new Formatter());
		
	}
}
