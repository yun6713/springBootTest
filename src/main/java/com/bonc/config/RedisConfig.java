package com.bonc.config;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.bonc.repository.redis.RedisUserRepository;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.lettuce.core.ReadFrom;
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
//	环境配置
	@Value("${spring.redis.host:localhost}")String host;
	@Value("${spring.redis.port:6379}")int port;
	@Value("${spring.redis.password:#{T(org.springframework.data.redis.connection.RedisPassword).none()}}")String password;
	@Value("${spring.redis.database:0}")int database;
	@Bean
	public RedisStandaloneConfiguration standaloneConfiguration(){		
		RedisStandaloneConfiguration rsc=new RedisStandaloneConfiguration();
		rsc.setHostName(host);
		rsc.setPort(port);
		rsc.setPassword(password);
		rsc.setDatabase(database);
		return rsc;
	}
	@Value("${spring.redis.sentinel.master:}")String master;
	@Value("${spring.redis.sentinel.nodes:}")String nodes;
	@Bean
	@ConditionalOnExpression("\"${spring.redis.sentinel.nodes:}\"!=\"\"")
	public RedisSentinelConfiguration sentinelConfiguration(){
		RedisSentinelConfiguration rsc=new RedisSentinelConfiguration();
		if(!"".equals(master)) {
			RedisNode masterNode=new RedisNode(host,port);
			masterNode.setName(master);
			rsc.setMaster(masterNode);			
		}
		if(!"".equals(nodes)) {
			Arrays.asList(nodes.split(",")).stream()
				.map(node->node.split(":"))
				.filter(strs->strs.length==2)
				.forEach(strs->rsc.addSentinel(new RedisNode(strs[0],Integer.valueOf(strs[1]))));
		}
		rsc.setPassword(password);
		rsc.setDatabase(database);
		return rsc;
	}
	@Value("${spring.redis.cluster.max-redirects:}")int maxRedirects;
	@Value("${spring.redis.cluster.nodes:}")String clusterNodes;
	@Bean
	@ConditionalOnExpression("\"${spring.redis.cluster.nodes:}\"!=\"\"")
	public RedisClusterConfiguration clusterConfiguration(){
		RedisClusterConfiguration rcc=new RedisClusterConfiguration();
		if(!"".equals(clusterNodes)) {
			Arrays.asList(nodes.split(",")).stream()
				.map(node->node.split(":"))
				.filter(strs->strs.length==2)
				.forEach(strs->rcc.addClusterNode(new RedisNode(strs[0],Integer.valueOf(strs[1]))));
		}
		rcc.setPassword(password);
		rcc.setMaxRedirects(maxRedirects);
		return rcc;
	}
	
//	客户端配置
	@Bean
	@ConfigurationProperties("spring.redis.lettuce.pool")
	public GenericObjectPoolConfig lettucePoolConfig() {
		return new GenericObjectPoolConfig();
	}
	@Bean
	@ConfigurationProperties("spring.redis.jedis.pool")
	public GenericObjectPoolConfig jedisPoolConfig() {
		return new GenericObjectPoolConfig();
	}
	@Value("${spring.redis.lettuce.shutdown-timeout:}")Duration shutdownTimeout;
	@Bean//返回池化客户端配置，不使用连接池，LettuceClientConfiguration.builder()
	public LettuceClientConfiguration lettuceClientConfiguration() {
		return LettucePoolingClientConfiguration.builder()
			      .readFrom(ReadFrom.SLAVE_PREFERRED) //优先读从
			      .shutdownTimeout(shutdownTimeout)
			      .poolConfig(lettucePoolConfig())
			      .build();
	}
	public JedisClientConfiguration jedisClientConfiguration() {
		return JedisClientConfiguration.builder()
				.usePooling()//使用连接池
				.poolConfig(jedisPoolConfig())
				.build();
	}
	/**
	 * 实现类：JedisConnectionFactory、LettuceConnectionFactory
	 * springboot1.x用jedis，springboot2.x用lettuce
	 * 传入配置RedisConfiguration、LettuceClientConfiguration
	 * @return
	 */
	@Bean
	public RedisConnectionFactory redisConnectionFactory(){
		return new LettuceConnectionFactory(standaloneConfiguration(),lettuceClientConfiguration());		
	}
//	@Bean
//	public RedisConnectionFactory jedisConnectionFactory(){
//		return new JedisConnectionFactory(standaloneConfiguration(),jedisClientConfiguration());		
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
	public RedisCacheConfiguration redisCacheConfiguration(){
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
