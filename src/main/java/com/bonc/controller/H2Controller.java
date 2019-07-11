package com.bonc.controller;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.entity.Role;
import com.bonc.entity.User;
import com.bonc.service.H2Service;

@RestController
@RequestMapping("/h2")
public class H2Controller {
	@Autowired
	H2Service h2Service;
	@RequestMapping("/find/{id}")
	public String find(@PathVariable("id") Integer id) {
		User u = h2Service.findUserById(id);
		return Optional.ofNullable(u).map(User::getUsername).orElse("noSuchOne");
	}
	@RequestMapping("/insertUser")
	public Object insertUser() {
		User u = new User();
		u.setUsername("b");
		u.setPassword("b");
		Role role = h2Service.findRoleByName("ROLE_admin");
		u.setRoles(Arrays.asList(role));
		h2Service.saveRole(role);
		h2Service.saveUser(u,true);
		return u.getuId()==null?"Failed":"Success";
	}
	@RequestMapping("/insertRole")
	public Object insertRole() {
		Role role;
		String roleName="ROLE_admin";
		if((role=h2Service.findRoleByName(roleName))!=null) {
			return role;
		}
		role = new Role();
		role.setRoleName(roleName);
		h2Service.saveRole(role);
		return role;
	}
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		h2Service.deleteUserById(id);
		return "Success";
	}
}
