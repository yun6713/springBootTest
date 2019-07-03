package com.bonc.service;

import com.bonc.entity.Role;
import com.bonc.entity.User;

public interface H2Service {
	User saveUser(User user);
	User findUserById(Integer id);
	void deleteUserById(Integer id);
	
	Role saveRole(Role role);
	Role findRoleById(String id);
	void deleteRoleById(String id);
	
}
