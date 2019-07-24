package com.bonc.controller;

import java.util.Optional;

import org.elasticsearch.client.transport.TransportClient;
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
	@Autowired
	TransportClient tc;
	@RequestMapping("/save")
	public EsUser save() {
		return eur.save(new EsUser(Optional.of(ur.findByUId(10001)).orElse(new User())));
	}
	@RequestMapping("/find/{info}")
	public Object find(@PathVariable("info") String info) {
		return eur.findByRolesContains(info);
//		return eur.findById(10001).orElse(new EsUser());
	}
	@RequestMapping("/like/{info}")
	public Object like(@PathVariable("info") String info) {
		return eur.findByRolesLike(info);
//		return eur.findById(10001).orElse(new EsUser());
	}
	@RequestMapping("/match/{info}")
	public Object match(@PathVariable("info") String info) {
		
		return eur.findByRolesLike(info);
//		return eur.findById(10001).orElse(new EsUser());
	}
}
