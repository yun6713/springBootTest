package com.bonc.security.springSecurity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.alibaba.fastjson.JSON;
import com.bonc.utils.MapUtils;
/**
 * 验证失败回调
 * @author litianlin
 * @date   2019年7月11日上午9:15:34
 * @Description TODO
 */
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		response.setHeader("Content-Type", "application/json;charset=utf-8");
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.getWriter().write("验证失败");
        response.getWriter().flush();
	}

}
