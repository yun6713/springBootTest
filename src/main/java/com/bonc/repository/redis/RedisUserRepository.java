package com.bonc.repository.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bonc.entity.jpa.User;
@Repository
public interface RedisUserRepository extends CrudRepository<User,Integer>{

}
