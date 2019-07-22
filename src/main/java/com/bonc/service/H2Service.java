package com.bonc.service;

import com.bonc.entity.jpa.Role;
import com.bonc.entity.jpa.User;

public interface H2Service {
	User saveUser(User user,boolean encrypt);
	User findUserById(Integer id);
	void deleteUserById(Integer id);
	
	Role saveRole(Role role);
	Role findRoleById(Integer id);
	void deleteRoleById(Integer id);
	Role findRoleByName(String roleName);
	
}
