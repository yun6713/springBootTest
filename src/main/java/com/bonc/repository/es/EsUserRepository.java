package com.bonc.repository.es;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bonc.entity.es.EsUser;

public interface EsUserRepository extends ElasticsearchRepository<EsUser, Integer>{
	List<EsUser> findByRolesContains(String info);
}
