package com.bonc.repository.jpa.integrate;

import java.util.List;

import com.bonc.entity.jpa.User;

public interface UserMapper {
	List<User> selectUsers();
}
