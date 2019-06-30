package com.bonc.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;


@Configuration
public class FilterTest  {
	@Bean
	public FilterRegistrationBean<Filter> sessionFilter() {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
    	//注入过滤器
        registration.setFilter(new Filter() {
        	@Override
        	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
        			throws IOException, ServletException {
        		//获取session，JSESSIONID自动放入resposne
        		((HttpServletRequest)arg0).getSession();
        		arg2.doFilter(arg0, arg1);
        	}
        });
        //拦截规则
        registration.addUrlPatterns("/*");
        //过滤器名称
        registration.setName("sessionFilter");
      //过滤器顺序
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE+8);
        return registration;
	}
	

}
