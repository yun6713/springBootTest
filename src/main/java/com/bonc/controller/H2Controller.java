package com.bonc.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.entity.jpa.Address;
import com.bonc.entity.jpa.Role;
import com.bonc.entity.jpa.User;
import com.bonc.mapper.UserOperation;
import com.bonc.repository.jpa.UserRepository;
import com.bonc.service.H2Service;

@RestController
@RequestMapping("/h2")
public class H2Controller {
	@Autowired
	H2Service h2Service;
	@Autowired
	UserRepository ur;
	@Autowired
	UserOperation uo;
	@RequestMapping("/find/{id}")
	public String find(@PathVariable("id") Integer id) {
		User u = h2Service.findUserById(id);
		return Optional.ofNullable(u).map(User::getUsername).orElse("noSuchOne");
	}
	@RequestMapping("/findRole/{id}")
	public Role findRole(@PathVariable("id") Integer id) {
		Role r = h2Service.findRoleById(id);
		return r;
	}
	@RequestMapping("/findAllUsers")
	public Object findAllUsers() {
		List<User> mybatis=uo.selectUsers();
		return ur.selectUsers();
	}
	@RequestMapping("/insertUser/{name}")
	public Object insertUser(@PathVariable String name) {
		User u = new User();
		u.setUsername(name);
		u.setPassword("b");
		Role role = h2Service.findRoleByName("ROLE_admin");
		if(role==null) {
			role=new Role();
			role.setRoleName("ROLE_admin");
			h2Service.saveRole(role);
		}
		u.setRoles(Arrays.asList(role));
		Address addr=new Address();
		addr.setAddr("cq City");
		u.setAddr(addr);
		h2Service.saveUser(u,true);
		return u.getuId()==null?"Failed":"Success";
	}
	@RequestMapping("/insertRole/{roleName}")
	public Object insertRole(@PathVariable String roleName) {
		Role role;
		if((role=h2Service.findRoleByName(roleName))!=null) {
			return role;
		}
		role = new Role();
		role.setRoleName(roleName);
		h2Service.saveRole(role);
		return role;
	}
	@RequestMapping("/deleteUser/{id}")
	public String deleteUser(@PathVariable("id") Integer id) {
		h2Service.deleteUserById(id);
		return "Success";
	}
}
