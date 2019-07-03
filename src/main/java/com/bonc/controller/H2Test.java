package com.bonc.controller;

import java.util.Arrays;
import java.util.Optional;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.entity.Role;
import com.bonc.entity.User;
import com.bonc.service.H2Service;

@RestController
@RequestMapping("/h2")
public class H2Test {

	@Autowired
	EntityManagerFactory entityManagerFactory;
	@Autowired
	H2Service h2Service;
	@RequestMapping("/test")
	public String test() {
		return "test";
	}
	@RequestMapping("/find/{id}")
	public String find(@PathVariable("id") Integer id) {
		User u = h2Service.findUserById(id);
//		UserRole ur = u.getUr();
		return Optional.ofNullable(u).map(User::getUsername).orElse("noSuchOne");
	}
	@RequestMapping("/insertUser")
	public Object insertUser() {
		User u = new User();
		u.setUsername("ltl");
		u.setPassword("lalala");
		Role role = new Role();
		role.setrId("1");
		role.setRole("admin");
		u.setRoles(Arrays.asList(role));
		h2Service.saveRole(role);
		h2Service.saveUser(u);
		return u.getuId();
	}
	@RequestMapping("/insertRole")
	public Object insertRole() {
		Role role = new Role();
		role.setrId("1");
		role.setRole("admin");
		h2Service.saveRole(role);
		return role;
	}
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		h2Service.deleteUserById(id);
		return "Success";
	}
}
