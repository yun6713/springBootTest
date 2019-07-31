package com.bonc.repository.jpa.integrate;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

import com.bonc.entity.jpa.User;
//@NoRepositoryBean//不暴露为bean,有自定义实现类时不可标记
public interface UserMapper {
	List<User> selectUsers();
}
