package com.bonc.security.springSecurity;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.alibaba.fastjson.JSON;
import com.bonc.utils.MapUtils;

public class MyAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	protected MyAuthenticationFilter() {
		super("/mylogin");	
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		//TODO 获取登录信息
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		//无参数时返回
		if(Objects.isNull(username)||Objects.isNull(password)) {
			response.setHeader("Content-Type", "application/json;charset=utf-8");
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			response.getWriter().write("username,password can't be null");
	        response.getWriter().flush();
	        return null;
		}
		//根据username、password构建token
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,password);
		//提交验证
		return this.getAuthenticationManager().authenticate(token);
	}

}
