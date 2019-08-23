package com.bonc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {
	@Override
	protected void addViewControllers(ViewControllerRegistry registry) {
//		registry.addRedirectViewController("/", "/test1");
	}
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		 registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	     super.addResourceHandlers(registry);
	}
	@Override
	protected void addFormatters(FormatterRegistry registry) {
//		registry.addFormatter(new Formatter());
	}
}
