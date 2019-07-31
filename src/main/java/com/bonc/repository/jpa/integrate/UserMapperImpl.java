package com.bonc.repository.jpa.integrate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.bonc.entity.jpa.User;
import com.bonc.mapper.UserOperation;
/**
 * 自定义实现类，后缀在@EnableJpaRepositories.repositoryImplementationPostfix定义<p>
 * 可用于整合mybatis
 * @author litianlin
 * @date   2019年7月31日上午11:13:38
 * @Description TODO
 */
@Component
public class UserMapperImpl implements UserMapper{
	@Autowired
	UserOperation uo;
	@Override
	public List<User> selectUsers() {
		return uo.selectUsers();
	}

}
