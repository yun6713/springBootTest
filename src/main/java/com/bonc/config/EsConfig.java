package com.bonc.config;

import java.util.List;

import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.bonc.repository.es.EsRepositoryMarker;

@Configuration
@EnableElasticsearchRepositories(basePackageClasses= {EsRepositoryMarker.class})
public class EsConfig {
	/*
	 * 自动配置ElasticsearchTemplate、ElasticsearchOperations、TransportClient
	 * 手动配置缺。
	 */
	@Bean(name = { "elasticsearchTemplate" })
	public ElasticsearchTemplate elasticsearchTemplate(Client client) {
		ElasticsearchTemplate template = new ElasticsearchTemplate(client);
		return template;
	}
}
