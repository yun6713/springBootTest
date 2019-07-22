package com.bonc.config;

import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.bonc.repository.es.EsRepositoryMarker;

@Configuration
@EnableElasticsearchRepositories(basePackageClasses= {EsRepositoryMarker.class})
public class EsConfig {
	/*
	 * 自动配置ElasticsearchTemplate、TransportClient
	 * 手动配置缺。
	 */
	//修改序列化策略为json，默认为jdk
	@Bean(name = { "elasticsearchTemplate" })
	public ElasticsearchTemplate elasticsearchTemplate(Client client) {
		ElasticsearchTemplate template = new ElasticsearchTemplate(client);
		return template;
	}
}
