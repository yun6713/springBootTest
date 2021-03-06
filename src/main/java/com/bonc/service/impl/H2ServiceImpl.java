package com.bonc.service.impl;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	Cache redisCache;
	@Autowired
	PasswordEncoder pe;
	public H2ServiceImpl(@Qualifier("redisCacheManager")RedisCacheManager rcm) {
		redisCache=rcm.getCache("user");
	}
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
	@Transactional(transactionManager="primaryTransactionManager")
	@Override
	public User findUserById(Integer id) {
//		List list = entityManager.createNativeQuery("select * from user").getResultList();
//		List<User> users = uo.selectUsers();
//		users = ur.selectUsers();
		User u=ur.findByUId(id);
		return u;
	}
	@Override
	@Cacheable(cacheManager="cacheManager",cacheNames="data")
	public User findUserByUsername(String username) {		
		User user;
		if(redisCache.get(username)!=null) {
			user= (User) redisCache.get(username).get();
		}else {
			user=ur.findByUsername(username);
			redisCache.putIfAbsent(username, user);
		}
		return ur.findByUsername(username);
	}
	@Override
	public void deleteUserById(Integer id) {
		ur.deleteById(id);
	}
	@Override
	public Role saveRole(Role role) {
		return rr.save(role);
	}
	@Transactional(transactionManager="primaryTransactionManager")
	@Override
	public Role findRoleById(Integer id) {
		return rr.findByRId(id).orElse(null);
	}
	@Override
	public void deleteRoleById(Integer id) {
		rr.deleteById(id);
	}
	@Override
	@Cacheable()
	public Role findRoleByName(String roleName) {
		org.springframework.cache.interceptor.KeyGenerator k;
		return rr.findByRoleName(roleName);
	}
	
}
