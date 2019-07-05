package com.bonc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
/**
 * ehcache配置类，配置CacheManager、Cahce
 * @author litianlin
 * @date   2019年7月5日下午5:13:12
 * @Description TODO
 */
@Configuration
@ConditionalOnClass(net.sf.ehcache.CacheManager.class)
public class EhcacheConfig {
	/**
	 * 配置ecacheManager
	 * @param loc
	 * @return
	 */
	@Bean("ehcacheManager")
	@Primary
	public EhCacheManagerFactoryBean ehcacheManager(
			@Value("spring.cache.ehcache.config")String loc) {
		EhCacheManagerFactoryBean fb = new EhCacheManagerFactoryBean();
		fb.setConfigLocation(new ClassPathResource(loc));
		return fb;
	}
	/**
	 * 配置springboot整合的CacheManager
	 * @param ehcacheManager
	 * @return
	 */
	@Bean("cacheManager")
	@Primary
	@Autowired
	public CacheManager cacheManager(net.sf.ehcache.CacheManager ehcacheManager) {
		EhCacheCacheManager cm = new EhCacheCacheManager();
		cm.setCacheManager(ehcacheManager);
		return cm;
	}
}
