package com.bonc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {
	@Override
	protected void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/test1");
	}
}
