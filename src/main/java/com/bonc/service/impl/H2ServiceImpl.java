package com.bonc.service.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bonc.entity.jpa.Role;
import com.bonc.entity.jpa.User;
import com.bonc.mapper.UserOperation;
import com.bonc.repository.jpa.RoleRepository;
import com.bonc.repository.jpa.UserRepository;
import com.bonc.service.H2Service;
@Service
public class H2ServiceImpl implements H2Service{
	@Autowired
	private UserRepository ur;
	@Autowired
	private RoleRepository rr;
	@Autowired
	UserOperation uo;
	@Autowired
	EntityManager entityManager;
	@Autowired
	PasswordEncoder pe;
	@Override
	public User saveUser(User user,boolean encrypt) {
		if(user.getuId()==null&&ur.findByUsername(user.getUsername())!=null)
			return user;
		//加密密码
		if(encrypt) {
			user.setPassword(pe.encode(user.getPassword()));
		}
		return ur.save(user);
	}
	@Override
	public User findUserById(Integer id) {
		List list = entityManager.createNativeQuery("select * from user").getResultList();
		List<User> users = uo.selectUsers();
//		users = ur.selectUsers();
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
	public Role findRoleById(Integer id) {
		return rr.findById(id).orElse(null);
	}
	@Override
	public void deleteRoleById(Integer id) {
		rr.deleteById(id);
	}
	@Override
	public Role findRoleByName(String roleName) {
		return rr.findByRoleName(roleName);
	}
	
}
