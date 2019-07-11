package com.bonc.security.springSecurity;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.bonc.utils.JwtUtils;

public class JwtAuthFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 根据jwt信息验证
		String jwtToken=request.getParameter("jwtToken");
		if(jwtToken!=null && JwtUtils.validateToken(jwtToken)) {
			SecurityContextHolder.getContext().setAuthentication(JwtUtils.getAuthentication(jwtToken));
		}
		chain.doFilter(request, response);
		//清除
		SecurityContextHolder.getContext().setAuthentication(null);
	}

}
