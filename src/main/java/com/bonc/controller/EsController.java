package com.bonc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.entity.jpa.User;
import com.bonc.repository.es.EsUserRepository;
import com.bonc.repository.jpa.UserRepository;

@RestController
@RequestMapping("/es")
public class EsController {
	@Autowired
	EsUserRepository er;
	@Autowired
	UserRepository ur;
	@RequestMapping("/save")
	public User save() {
		return er.save(ur.findByUId(10001));
	}
	@RequestMapping("/find")
	public User find() {
		return er.findById(10001).orElse(new User());
	}
}
