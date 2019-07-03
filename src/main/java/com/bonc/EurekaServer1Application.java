package com.bonc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.repository.UserRepository;

@SpringBootApplication
@RestController
@EnableJpaRepositories(basePackageClasses= {UserRepository.class})
public class EurekaServer1Application{
	public static void main(String[] args) {
		SpringApplication.run(EurekaServer1Application.class, args);
	}
	
}
