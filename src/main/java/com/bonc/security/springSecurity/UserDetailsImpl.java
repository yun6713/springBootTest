package com.bonc.security.springSecurity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bonc.entity.jpa.Role;
import com.bonc.entity.jpa.User;
/**
 * 根据User构建UserDetailsImpl，过期等可根据User属性判定。
 * 权限使用SimpleGrantedAuthority
 * @author litianlin
 * @date   2019年7月9日下午1:07:59
 * @Description TODO
 */
public class UserDetailsImpl implements UserDetails{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	List<SimpleGrantedAuthority> authorities;
	public UserDetailsImpl(User user) {
		if(user==null){ //赋值默认用户，防止报错
			user = new User();
		}
		this.user=user;
		this.authorities=user.getRoles().stream()
						.map(Role::getRoleName)
						.filter(Objects::nonNull)
						.distinct()
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getUsername();
	}

	public User getUser() {
		return user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
}
