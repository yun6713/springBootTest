package com.bonc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.entity.es.EsUser;
import com.bonc.entity.jpa.User;
import com.bonc.repository.es.EsUserRepository;
import com.bonc.repository.jpa.UserRepository;

@RestController
@RequestMapping("/es")
public class EsController {
	@Autowired
	EsUserRepository eur;
	@Autowired
	UserRepository ur;
	@Autowired
	ElasticsearchTemplate template;
	@RequestMapping("/save")
	public EsUser save() {
		return eur.save(new EsUser(Optional.of(ur.findByUId(10001)).orElse(new User())));
	}
	@RequestMapping("/find/{info}")
	public Object find(@PathVariable("info") String info) {
		return eur.findByRolesContains(info);
//		return eur.findById(10001).orElse(new EsUser());
	}
	@RequestMapping("/like/{info}")
	public Object like(@PathVariable("info") String info) {
		return eur.findByRolesLike(info);
//		return eur.findById(10001).orElse(new EsUser());
	}
	@RequestMapping("/match/{info}")
	public Object match(@PathVariable("info") String info) {
		Field hb=new HighlightBuilder.Field("roles")
				.preTags("<span>")
				.postTags("</span>")
				.fragmentSize(20);
		SearchQuery sq=new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchQuery("roles", info))
				.withHighlightFields(hb)
				.build();
		//高亮需自己解析
		Object obj=template.queryForPage(sq, EsUser.class,new SearchResultMapper() {

			@Override
			public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
				// TODO Auto-generated method stub
				List<T> chunk = new ArrayList<T>();

				for (SearchHit searchHit : response.getHits().getHits()) {
					Map<String, Object> smap = searchHit.getSourceAsMap();
					Map<String, HighlightField> hmap = searchHit.getHighlightFields();
					chunk.add((T)createEsUser(smap,hmap));
				}
				
				AggregatedPage<T> result=new AggregatedPageImpl<T>(chunk,pageable,response.getHits().getTotalHits());
				
				return result;
			}

			private EsUser createEsUser(Map<String, Object> smap, Map<String, HighlightField> hmap) {
				EsUser eu=new EsUser();
				eu.setuId((Integer)Optional.ofNullable(smap.get("uId")).orElse(0));
				eu.setUsername(Optional.ofNullable(smap.get("username")).orElse("").toString());
				eu.setRoles(Optional.ofNullable(hmap.get("roles").getFragments()[0]).orElse(null).toString());
				return eu;
			}
		});
//		return eur.findByRolesLike(info);
//		return eur.search(sq);
		return obj;
//		return eur.findById(10001).orElse(new EsUser());
	}
}
