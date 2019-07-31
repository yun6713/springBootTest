package com.bonc.config;

import java.nio.charset.StandardCharsets;
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
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.bonc.repository.redis.RedisUserRepository;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 配置jpa-redis使用，配置RedisConnectionFactory、RedisCacheManager<p>
 * 缓存在Configuration中配置序列化策略；nosql访问在Template中配置序列化策略。<p>
 * 缓存操作为原子性操作。
 * redis四种运行方式：单点、主从、哨兵、集群；对应4种连接方式。
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
	@Bean
	public RedisConfiguration redisConfiguration(){
		RedisStandaloneConfiguration rc=new RedisStandaloneConfiguration();
		return rc;
	}
	/**
	 * 实现类：JedisConnectionFactory、LettuceConnectionFactory
	 * springboot1.x用jedis，springboot2.x用lettuce
	 * 传入配置RedisConfiguration、LettuceClientConfiguration
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix="spring.redis.lettuce")
	public RedisConnectionFactory redisConnectionFactory(){
		LettuceConnectionFactory lcf = new LettuceConnectionFactory(redisConfiguration());
		return lcf;		
	}
	/**
	 * 配置RedisTemplate，设置key，value序列化策略
	 * @param redisConnectionFactory
	 * @return
	 */
	@Bean
	public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
		RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		

        redisTemplate.setValueSerializer(jsonSerializer());
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        redisTemplate.setKeySerializer(new StringRedisSerializer(StandardCharsets.UTF_8));
        redisTemplate.setHashKeySerializer(new StringRedisSerializer(StandardCharsets.UTF_8));
        redisTemplate.setHashValueSerializer(jsonSerializer());
		return redisTemplate;
	}
	private Jackson2JsonRedisSerializer<Object> jsonSerializer(){
		//使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);
        return serializer;
	}
	/**
	 * 配置缓存序列换策略
	 * @return
	 */
	@Bean
	@ConfigurationProperties("spring.cache.redis")
	public RedisCacheConfiguration config(){
		return RedisCacheConfiguration.defaultCacheConfig()
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer(StandardCharsets.UTF_8)))
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer()))
				.disableCachingNullValues();
	}
	//配置spring CacheManager bean
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
				.cacheDefaults(config)
//				.disableCreateOnMissingCache()
				.withInitialCacheConfigurations(configMap)
//				.transactionAware()//put/evict事务
				.build();
	}
}
