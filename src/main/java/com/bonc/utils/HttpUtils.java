package com.bonc.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
/**
 * 获取req、resp。
 * @author litianlin
 * @date   2019年8月26日上午10:04:57
 * @Description TODO
 */
public class HttpUtils {
	public static HttpServletRequest getRequest() {
		return  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	public static HttpServletResponse getResponse() {
		return  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}
}
