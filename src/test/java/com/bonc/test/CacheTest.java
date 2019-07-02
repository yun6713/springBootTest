package com.bonc.test;


import java.io.IOException;

import org.junit.Test;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.Configuration;

public class CacheTest {
	@Test
	public void testEhCache01() {
		EhCacheManagerFactoryBean ef= new EhCacheManagerFactoryBean();
		ef.setConfigLocation(new ClassPathResource("ehcache.xml"));
		ef.afterPropertiesSet();
		CacheManager cm = ef.getObject();
		Cache cache = cm.getCache("data");
		cache.put(new Element("hello","world"));
		cache.put(new Element("hello1","world2"));
		cache.flush();
		System.out.println(cache.get("hello"));
		
	}
	@Test
	public void testEhCache02() throws CacheException, IOException {
		CacheManager cm = CacheManager.create(new ClassPathResource("ehcache.xml").getInputStream());
		Cache cache = cm.getCache("data");
		cache.put(new Element("hello","world"));
		System.out.println(cache.get("hello"));
		
	}
}
