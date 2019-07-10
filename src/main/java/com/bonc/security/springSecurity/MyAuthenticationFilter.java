package com.bonc.security.springSecurity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class MyAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	protected MyAuthenticationFilter() {
//		super(new AntPathRequestMatcher("/login", "POST"));	}
		super("/mylogin");	
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		//TODO 获取登录信息
		String username="a";
		String password="a";
		//根据username、password构建token
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,password);
		//提交验证
		return this.getAuthenticationManager().authenticate(token);
	}

}
