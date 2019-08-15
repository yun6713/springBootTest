package com.bonc;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 初始化spring boot admin server
 * @author litianlin
 * @date   2019年7月5日上午11:12:40
 * @Description TODO
 */
@Order
@RestController
@ConditionalOnExpression("'${spring.boot.admin.server.start-command:}'!=''")
public class SpringBootAdminServerRunner implements CommandLineRunner{
	private Process process;	
	private final static Logger LOG = LoggerFactory.getLogger(SpringBootAdminServerRunner.class);
	@Value("${spring.boot.admin.server.start-command:}")
	String command;
	@Override
	public void run(String... args) throws Exception {
		//TODO 关闭之前启动的server
		start();
	}
	private void start() {
		try {
			process=Runtime.getRuntime().exec(command);
			LOG.info("Start spring boot admin server success");
		} catch (IOException e) {
			LOG.error("Start spring boot admin server failed, cause:{}", e.getMessage());
			e.printStackTrace();
		}
	}
	@RequestMapping("/stopSpringbootAdminServer")
	public Object stopSpringbootAdminServer(){
		String result="Spring boot admin server doesn't run";
		if(process!=null &&process.isAlive()) {
			process.destroy();
			result="Stop spring boot admin server success";
			LOG.info("Stop spring boot admin server success");
		}
		return result;
	}
	@RequestMapping("/startSpringbootAdminServer")
	public Object startSpringbootAdminServer() throws Exception{
		String result="Spring boot admin server is running";
		if(process==null || !process.isAlive()) {
			start();
			result="Start spring boot admin server success";
		}
		return result;
	}
	@EventListener(classes= {ContextClosedEvent.class,ContextStoppedEvent.class})
	public void shutdown(ApplicationEvent ae) {
		System.out.println(ae);
		stopSpringbootAdminServer();
	}
}
