package com.bonc.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bonc.service.TestService;

@Service
public class TestServiceImpl implements TestService{
	private static final Logger LOG=LoggerFactory.getLogger(TestServiceImpl.class);
	@Async
	public void testAsyncService() {
		LOG.info("la,lalalala");
	}
	@Async
	public void testAsyncSecurity() {
		LOG.info("security info:{}",
				SecurityContextHolder.getContext().getAuthentication());
	}
}
