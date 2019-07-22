package com.bonc.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.entity.es.EsUser;
import com.bonc.entity.jpa.User;
import com.bonc.repository.es.EsUserRepository;
import com.bonc.repository.jpa.UserRepository;

@RestController
@RequestMapping("/es")
public class EsController {
	@Autowired
	EsUserRepository eur;
	@Autowired
	UserRepository ur;
	@RequestMapping("/save")
	public EsUser save() {
		return eur.save(new EsUser(Optional.of(ur.findByUId(10001)).orElse(new User())));
	}
	@RequestMapping("/find/{info}")
	public Object find(@PathVariable("info") String info) {
		return eur.findByRolesContains(info.toLowerCase());
//		return eur.findById(10001).orElse(new EsUser());
	}
}
