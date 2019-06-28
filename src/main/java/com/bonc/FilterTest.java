package com.bonc;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;


@Configuration
public class FilterTest  {
	public static String ticket;
	
	@Bean
	public FilterRegistrationBean<Filter> test() {
		 FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
	    	//注入过滤器
	        registration.setFilter(new Filter() {
	        	@Override
	        	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
	        			throws IOException, ServletException {
	        		if(FilterTest.ticket!=null) {
	        			((HttpServletResponse) arg1).addCookie(new Cookie("ticket", ticket));
	        		}else {
		        		String ticket = arg0.getParameter("ticket");
		        		if(ticket!=null) {
		        			//TODO 验证ticket有效，设置ticket
		        			FilterTest.ticket=ticket;
		        			((HttpServletResponse) arg1).addCookie(new Cookie("ticket", ticket));
		        		}
	        		}
	        		arg2.doFilter(arg0, arg1);
	        	}
	        });
	        //拦截规则
	        registration.addUrlPatterns("/*");
	        //过滤器名称
	        registration.setName("testFilter");
	      //过滤器顺序
	        registration.setOrder(Ordered.HIGHEST_PRECEDENCE+10);
	        return registration;
	}
	
	

}
