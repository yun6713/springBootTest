package com.bonc.repository.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bonc.entity.jpa.User;

public interface EsUserRepository extends ElasticsearchRepository<User, Integer>{

}
