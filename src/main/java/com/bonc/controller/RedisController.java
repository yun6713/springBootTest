package com.bonc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.entity.User;
import com.bonc.repository.jpa.UserRepository;
import com.bonc.repository.redis.RedisUserRepository;

@RestController
@RequestMapping("/redis")
public class RedisController {
	@Autowired
	RedisUserRepository rur;
	@Autowired
	UserRepository ur;
	@RequestMapping("/save")
	public User save() {
		return rur.save(ur.findByUId(10001));
	}
	@RequestMapping("/find")
	public User find() {
		return rur.findById(10001).orElse(new User());
	}
}
