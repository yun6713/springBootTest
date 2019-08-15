package com.bonc.security.springSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bonc.service.H2Service;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	H2Service hs;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new UserDetailsImpl(hs.findUserByUsername(username));
	}

}
