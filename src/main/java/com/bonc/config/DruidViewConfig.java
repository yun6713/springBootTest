package com.bonc.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.bonc.utils.MapUtils;

/**
 * 配置druid浏览器访问
 * @author litianlin
 * @date   2019年7月31日下午1:17:45
 * @Description TODO
 */
@Configuration
public class DruidViewConfig {
	@Bean
	public FilterRegistrationBean<WebStatFilter> druidFilter(){
		FilterRegistrationBean<WebStatFilter> frb=new FilterRegistrationBean<>();
		frb.setFilter(new WebStatFilter());
		frb.setInitParameters(MapUtils.builder("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*").build());
		frb.addUrlPatterns("/*");
		frb.setName("druidWebStatFilter");
		return frb;
	}
	//可从外部读入配置
	@Bean
	public ServletRegistrationBean<StatViewServlet> druidServlet(){
		ServletRegistrationBean<StatViewServlet> srb=new ServletRegistrationBean<>();
		srb.setServlet(new StatViewServlet());
		srb.setInitParameters(MapUtils.builder("loginUsername","admin")
				.put("loginPassword", "admin")
				.put("resetEnable", "false")
				.put("allow", "192.168.16.117,127.0.0.1")
				.put("deny", "192.168.16.118")
				.build());
		srb.addUrlMappings("/druid/*");
		return srb;
	}
}
