package com.bonc.service;

import com.bonc.entity.Role;
import com.bonc.entity.User;

public interface H2Service {
	User saveUser(User user,boolean encrypt);
	User findUserById(Integer id);
	void deleteUserById(Integer id);
	
	Role saveRole(Role role);
	Role findRoleById(Integer id);
	void deleteRoleById(Integer id);
	Role findRoleByName(String roleName);
	
}
