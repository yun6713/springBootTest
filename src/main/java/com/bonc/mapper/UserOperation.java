package com.bonc.mapper;

import java.util.List;

import com.bonc.entity.jpa.User;

public interface UserOperation{
	List<User> selectUsers();
}
