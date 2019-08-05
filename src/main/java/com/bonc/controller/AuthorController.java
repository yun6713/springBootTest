package com.bonc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.entity.jpa.User;
import com.bonc.security.springSecurity.UserDetailsImpl;
import com.bonc.utils.JwtUtils;
/**
 * spring security权限注解：@Secured、@PreAuthorize
 * 注解必须在@EnableGlobalMethodSecurity的属性中开启
 * 权限注解中ROLE大写。
 * @author litianlin
 * @date   2019年7月10日上午8:31:12
 * @Description TODO
 */
@RestController
public class AuthorController {
	//基于角色
	@Secured(value = { "ROLE_admin" })
	@RequestMapping("/author1")
	public String test() {
		return "author1 success";
	}
	//spEL，判定角色、操作权permission
	@PreAuthorize(value = "hasRole('db')")
	@RequestMapping("/author2")
	public String test1() {
		return "author2 success";
	}
	@PreAuthorize(value = "hasPermission('db')")
	@RequestMapping("/author3")
	public String test2() {
		User user = getAuthUser();
		return "author2 success,"+user.getUsername();
	}
	/**
	 * 添加db角色权限
	 * @return
	 */
	@RequestMapping("/addDb")
	public String test3() {
		Authentication auth = getAuthentication();
		List<GrantedAuthority> list = new ArrayList<>(auth.getAuthorities());
		list.add(new SimpleGrantedAuthority("ROLE_db"));
		SecurityContextHolder.getContext()
			.setAuthentication(new  UsernamePasswordAuthenticationToken(auth.getPrincipal(),auth.getCredentials(),list));
		return "addDb success";
	}
	@RequestMapping("/getJwt")
	public String getJwt() {
		return JwtUtils.generateToken(SecurityContextHolder.getContext().getAuthentication());
	}
	public static Authentication getAuthentication () {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	public static User getAuthUser() {
		//通过安全上下文获取UserDetails
		Object userDetails = getAuthentication ().getPrincipal();
		return ((UserDetailsImpl)userDetails).getUser();
	}
	
}
