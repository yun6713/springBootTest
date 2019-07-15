package com.bonc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bonc.utils.Slf4jHelper;

@ControllerAdvice
public class ExpHandler {
	@ExceptionHandler({Exception.class})
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR,
			reason="")
	public String handleException(HttpServletRequest req,HttpServletResponse resp,Exception exp) {
		Slf4jHelper.error(ExpHandler.class,exp.getMessage());
		return exp.getMessage();
	}
}
