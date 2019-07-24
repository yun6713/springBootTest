package com.bonc.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.bonc.repository.redis.RedisUserRepository;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 配置jpa-redis使用，配置RedisConnectionFactory、RedisCacheManager
 * @author Administrator
 *
 */
@Configuration
//不支持事务
@EnableRedisRepositories(
		redisTemplateRef="redisTemplate",
		basePackageClasses = { RedisUserRepository.class }
	)
public class RedisConfig {
//	public RedisConfiguration redisConfiguration(){
//		
//	}
	/**
	 * 实现类：JedisConnectionFactory、LettuceConnectionFactory
	 * springboot1.x用jedis，springboot2.x用lettuce
	 * 传入配置RedisConfiguration、LettuceClientConfiguration
	 * @return
	 */
//	@Bean
//	@ConfigurationProperties(prefix="spring.redis")
//	public RedisConnectionFactory redisConnectionFactory(){
//		RedisConnectionFactory rcf = new LettuceConnectionFactory();
//		return rcf;		
//	}
	/**
	 * 配置RedisTemplate，设置key，value序列化策略
	 * @param redisConnectionFactory
	 * @return
	 */
	@Bean
	public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
		RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		//使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);

        redisTemplate.setValueSerializer(serializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
        redisTemplate.setHashKeySerializer(StringRedisSerializer.UTF_8);
        redisTemplate.setHashValueSerializer(serializer);
		return redisTemplate;
	}
	@Bean
	@ConfigurationProperties("spring.cache.redis")
	public RedisCacheConfiguration config(){
		return RedisCacheConfiguration.defaultCacheConfig();
	}
	@Bean
	public RedisCacheManager redisCacheManager(RedisCacheConfiguration config,RedisConnectionFactory redisConnectionFactory){
        // 设置一个初始化的缓存空间set集合
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("timeGroup");
        cacheNames.add("user");

        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put("timeGroup", config);
        configMap.put("user", config.entryTtl(Duration.ofSeconds(120)));
		return RedisCacheManager.builder(redisConnectionFactory)
				.initialCacheNames(cacheNames)
				.withInitialCacheConfigurations(configMap)
				.build();
	}
}
