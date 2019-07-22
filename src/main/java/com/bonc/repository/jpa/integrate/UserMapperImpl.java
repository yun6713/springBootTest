package com.bonc.repository.jpa.integrate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bonc.entity.jpa.User;
import com.bonc.mapper.UserOperation;
@Component
public class UserMapperImpl implements UserMapper{
	@Autowired
	UserOperation uo;
	@Override
	public List<User> selectUsers() {
		return uo.selectUsers();
	}

}
