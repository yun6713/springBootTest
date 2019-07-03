package com.bonc;

import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;

@Component
public class InitCommandRunner implements CommandLineRunner{


	@Value("${server.port:8080}")
	String port;
	@Value("${server.servlet.context-path:}")
	String contextPath;
	@Value("${server.address:localhost}")
	String addr;

	@Override
	public void run(String... args) throws Exception {
		System.out.println(String.format("%1s:%2s/%3s",addr, port,contextPath));
	}
}
