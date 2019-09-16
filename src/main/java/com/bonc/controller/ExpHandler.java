package com.bonc.controller;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.alibaba.fastjson.JSON;
import com.bonc.utils.HttpUtils;

@RestControllerAdvice
public class ExpHandler implements ResponseBodyAdvice<Object>{
	private final static Logger LOG = LoggerFactory.getLogger(ExpHandler.class);
	@ExceptionHandler({Exception.class})
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR,
			reason="")
	public String handleException(HttpServletRequest req,HttpServletResponse resp,Exception exp) {
		exp.printStackTrace();
		LOG.error(exp.getMessage());
		return exp.getMessage();
	}    
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
//		HttpServletRequest req=HttpUtils.getRequest();
//		boolean flag=EndpointRequest.toAnyEndpoint().matches(req);
//		if(flag && LOG.isDebugEnabled()) {
//			LOG.debug("request url:{}",req.getRequestURL().toString());
//		}
		//只包装controller包返回结果，避免其他包返回格式错误。
		String pName=returnType.getDeclaringClass().getPackage().getName();
		boolean flag=pName.startsWith("com.bonc.controller");
		return flag;
	}
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		//对String进行特殊处理，mvc converter根据controller返回类型决定，会报错
		return body instanceof ResultVO?body
			:body instanceof String?JSON.toJSONString(ResultVO.ok(body)):ResultVO.ok(body);
	}
	public static class ResultVO<T> {
		 	private int status = 1;

		    private String errorCode = "";

		    private String errorMsg = "";

		    private T resultBody;
		    public static <T> ResultVO<T> ok(T resultBody) {
		        return new ResultVO<T>(resultBody);
		    }
		    public static ResultVO<Object> err(String errorCode,String errorMsg) {
		        
		    	return new ResultVO<Object>(errorCode,errorMsg);
		    }
		    public ResultVO() {
		    }
		    public ResultVO(String errorCode,String errorMsg) {
				this.errorCode = errorCode;
				this.errorMsg = errorMsg;
		    }

		    public ResultVO(T resultBody) {
		        this.resultBody = resultBody;
		    }

			public int getStatus() {
				return status;
			}

			public void setStatus(int status) {
				this.status = status;
			}

			public String getErrorCode() {
				return errorCode;
			}

			public void setErrorCode(String errorCode) {
				this.errorCode = errorCode;
			}

			public String getErrorMsg() {
				return errorMsg;
			}

			public void setErrorMsg(String errorMsg) {
				this.errorMsg = errorMsg;
			}

			public T getResultBody() {
				return resultBody;
			}

			public void setResultBody(T resultBody) {
				this.resultBody = resultBody;
			}

			@Override
			public String toString() {
				return "ResultVO [status=" + status + ", errorCode=" + errorCode + ", errorMsg=" + errorMsg
						+ ", resultBody=" + resultBody + "]";
			}
		    
	}
}
