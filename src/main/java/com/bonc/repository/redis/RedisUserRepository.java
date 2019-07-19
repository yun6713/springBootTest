package com.bonc.repository.redis;

import org.springframework.data.repository.CrudRepository;

import com.bonc.entity.User;

public interface RedisUserRepository extends CrudRepository<User,Integer>{

}
