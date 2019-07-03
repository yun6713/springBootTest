package com.bonc.test;


import java.io.IOException;

import org.junit.Test;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

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
	@Test
	public void testRedis01() throws CacheException, IOException {
		JedisConnectionFactory jcf = new JedisConnectionFactory();
		
		//RedisConnectionFactory为接口，JedisConnectionFactory实现
		//RedisConnectionFactory rcf = new RedisConnectionFactory();
		
		RedisTemplate<String, String> rt = new RedisTemplate<>();
		rt.setConnectionFactory(jcf);
		RedisCacheConfiguration rcc = RedisCacheConfiguration.defaultCacheConfig();

		RedisCacheWriter cacheWriter= RedisCacheWriter.lockingRedisCacheWriter(jcf);
		RedisCacheManager rcm = new RedisCacheManager(cacheWriter, rcc);
		
	}
}
