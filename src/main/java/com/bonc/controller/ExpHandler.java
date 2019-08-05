package com.bonc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bonc.H2InitCommandRunner;

@ControllerAdvice
public class ExpHandler {
	private final static Logger LOG = LoggerFactory.getLogger(H2InitCommandRunner.class);
	@ExceptionHandler({Exception.class})
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR,
			reason="")
	@ResponseBody
	public String handleException(HttpServletRequest req,HttpServletResponse resp,Exception exp) {
		exp.printStackTrace();
		LOG.error(exp.getMessage());
		return exp.getMessage();
	}
}
