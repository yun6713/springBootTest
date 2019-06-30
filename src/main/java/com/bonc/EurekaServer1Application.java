package com.bonc;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.pool.DruidDataSource;
import com.bonc.entity.Role;
import com.bonc.entity.User;
import com.bonc.repository.RoleRepository;
import com.bonc.repository.UserRepository;

@SpringBootApplication
@RestController
@EnableJpaRepositories(basePackageClasses= {UserRepository.class})
public class EurekaServer1Application {
	public static void main(String[] args) {
		SpringApplication.run(EurekaServer1Application.class, args);
	}

	@Autowired
	private UserRepository ur;
	@Autowired
	private RoleRepository rr;
	@RequestMapping("/test")
	public String test() {
		return "test";
	}
	@RequestMapping("/jpa")
	public String testJpa() {
		User u = ur.findByUId(1);
//		UserRole ur = u.getUr();
		return u.getUsername();
	}
	@RequestMapping("/insert")
	public Object testInsert() {
		User u = new User();
		u.setUsername("ltl");
		u.setPassword("lalala");
		Role role = new Role();
		role.setrId("1");
		role.setRole("admin");
		u.setRoles(Arrays.asList(role));
		rr.save(role);
		ur.save(u);
		return u.getuId();
	}
}
