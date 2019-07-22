package com.bonc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.bonc.repository.es.EsRepositoryMarker;

@Configuration
@EnableElasticsearchRepositories(basePackageClasses= {EsRepositoryMarker.class})
public class EsConfig {
	/*
	 * 自动配置ElasticsearchTemplate、TransportClient
	 * 手动配置缺。
	 */
}
