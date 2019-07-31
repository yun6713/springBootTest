package com.bonc.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.bonc.utils.FileUtils;
/**
 * ehcache配置类，配置CacheManager、Cahce
 * key--String(Object时取hashCode),value--jdk序列化
 * @author litianlin
 * @date   2019年7月5日下午5:13:12
 * @Description TODO
 */
@Configuration
@ConditionalOnClass(net.sf.ehcache.CacheManager.class)
public class EhcacheConfig {
	/**
	 * 配置ehcacheManager
	 * @param loc
	 * @return
	 * @throws IOException 
	 */
	@Bean("ehcacheManager")
	@Primary
	public EhCacheManagerFactoryBean ehcacheManager(
			@Value("${spring.cache.ehcache.config}")String loc) throws IOException {
		EhCacheManagerFactoryBean fb = new EhCacheManagerFactoryBean();
		fb.setConfigLocation(FileUtils.getResource(loc));
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
