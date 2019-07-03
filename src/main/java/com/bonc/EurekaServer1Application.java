package com.bonc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.repository.UserRepository;

@SpringBootApplication
@RestController
@EnableJpaRepositories(basePackageClasses= {UserRepository.class})
public class EurekaServer1Application implements CommandLineRunner{
	public static void main(String[] args) {
		SpringApplication.run(EurekaServer1Application.class, args);
	}

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
