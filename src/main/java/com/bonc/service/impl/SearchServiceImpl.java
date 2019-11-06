package com.bonc.service.impl;

import java.util.Map;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.support.ValueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import com.bonc.entity.DocEntity;
import com.bonc.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {
	@Autowired
//	es必须填写配置文件信息后才自动装配
	ElasticsearchTemplate et;
	@Override
	public Object searchDoc(String content, String types) {
		QueryBuilder qb=QueryBuilders.boolQuery()
			.should(QueryBuilders.matchQuery("content", content).boost(6))
			.should(QueryBuilders.matchQuery("title", content))
			.minimumShouldMatch(1)
			.must(QueryBuilders.termsQuery("type", types.toLowerCase().split(",")));
		System.out.println(qb);
		NativeSearchQuery nsq=new NativeSearchQueryBuilder()
				.withQuery(qb)
				.withSourceFilter(new FetchSourceFilter(null,new String[] {"metadata"}))
				.build();
		return et.queryForPage(nsq,  DocEntity.class)
				.getContent();
	}
	@Override
	public Object getTypes() {
		TermsAggregationBuilder builder = new TermsAggregationBuilder("lkk", ValueType.STRING)
			.field("type.keyword");
		NativeSearchQuery nsq=new NativeSearchQueryBuilder()
				.addAggregation(builder)
				.withIndices("doc")
				.build();
		return et.query(nsq, resp->{
			Map<String, Aggregation> aggs=resp.getAggregations().asMap();
//			System.out.println(aggs);
//			System.out.println(aggs.get("lkk"));
//			System.out.println();
			return ((Terms)aggs.get("lkk"))
					.getBuckets()
					.stream()
					.map(bucket->bucket.getKeyAsString())
					.collect(Collectors.toList());
		});
	}

	
}