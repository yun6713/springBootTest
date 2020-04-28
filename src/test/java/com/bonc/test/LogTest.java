package com.bonc.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.ResourceUtils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import net.sf.ehcache.config.CacheConfiguration;


public class LogTest {
	@Test
	public void log4jLayout() throws MalformedURLException, IOException {
		org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LogTest.class);
//		BasicConfigurator.configure();
		Properties p = new Properties();
		p.load(new FileSystemResource(ResourceUtils.getFile("classpath:log4jTestConfig.properties")).getInputStream());
		org.apache.log4j.PropertyConfigurator.configure(p);
		log.info("lalala");
		log.warn("lalala2");
	}
	@Test
	public void testLogback(){
		Logger log = LoggerFactory.getLogger(LogTest.class);
		log.info("hello world");
		LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
		StatusPrinter.print(lc);
	}
}
