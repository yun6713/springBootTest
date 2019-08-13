package com.bonc;

import javax.management.MBeanServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
/**
 * 输出项目路径
 * @author litianlin
 * @date   2019年7月5日上午11:12:40
 * @Description TODO
 */
@Component
public class PathPrintCommandRunner implements CommandLineRunner{
	private final static Logger LOG = LoggerFactory.getLogger(PathPrintCommandRunner.class);
	@Value("${server.address:localhost}:${server.port:8080}/${server.servlet.context-path:}")
	String addr;
	@Autowired
	MBeanServer mbs;
	@Override
	public void run(String... args) throws Exception {
		//输出项目路径
		if(LOG.isInfoEnabled())
			LOG.info(addr);
		
	}
}
