package com.bonc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.entity.jpa.User;
import com.bonc.repository.redis.RedisUserRepository;
import com.bonc.service.H2Service;

@RestController
@RequestMapping("/redis")
public class RedisController {
	@Autowired
	RedisUserRepository rur;
	@Autowired
	H2Service hs;
	@Autowired
	RedisTemplate<String,Object> rt;
	@RequestMapping("/save")
	public User save() {
		User user=hs.findUserById(1);
		return rur.save(user);
	}
	@RequestMapping("/test")
	public Object test() {
		rt.opsForValue().set("li", "tianlin");
		User user=hs.findUserById(1);
		rt.opsForValue().set("user", user);
		return rt.opsForValue().get("li");
	}
	@RequestMapping("/find")
	public User find() {
		return rur.findById(1).orElse(new User());
	}
}
