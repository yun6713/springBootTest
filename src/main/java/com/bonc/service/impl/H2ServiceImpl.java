package com.bonc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.entity.Role;
import com.bonc.entity.User;
import com.bonc.repository.RoleRepository;
import com.bonc.repository.UserRepository;
import com.bonc.service.H2Service;
@Service
public class H2ServiceImpl implements H2Service{
	@Autowired
	private UserRepository ur;
	@Autowired
	private RoleRepository rr;
	@Override
	public User saveUser(User user) {
		return ur.save(user);
	}
	@Override
	public User findUserById(Integer id) {
		return ur.findByUId(id);
	}
	@Override
	public void deleteUserById(Integer id) {
		ur.deleteById(id);
	}
	@Override
	public Role saveRole(Role role) {
		return rr.save(role);
	}
	@Override
	public Role findRoleById(String id) {
		return rr.findById(id).orElse(null);
	}
	@Override
	public void deleteRoleById(String id) {
		rr.deleteById(id);
	}
	
}
