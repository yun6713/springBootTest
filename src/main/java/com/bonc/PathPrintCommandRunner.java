package com.bonc;

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

	@Value("${server.address:localhost}:${server.port:8080}/${server.servlet.context-path:}")
	String addr;
	
	@Override
	public void run(String... args) throws Exception {
		//输出项目路径
		System.out.println(addr);
		
	}
}
