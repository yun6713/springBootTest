
package com.bonc.test;

import java.util.Arrays;

import javax.persistence.criteria.Predicate;

import org.junit.Test;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.PartialUpdate;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.data.repository.CrudRepository;

import io.lettuce.core.ReadFrom;
@RedisHash
public class SpringDataTest {
	@Test
	public void test01() {
		Predicate p;
		QuerydslPredicateExecutor q;
	}
	@Test
	public void testLettuce() {
		RedisStandaloneConfiguration rsc =new RedisStandaloneConfiguration("server", 6379);
		LettuceConnectionFactory rcf =new LettuceConnectionFactory(rsc);
		rcf.setShareNativeConnection(false);
		RedisConnection rc = rcf.getConnection();
		LettuceClientConfiguration lcc = LettuceClientConfiguration.builder()
			      .readFrom(ReadFrom.SLAVE_PREFERRED) //优先读从
			      .build();
		rcf =new LettuceConnectionFactory(rsc,lcc);
		RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
				  .master("mymaster")
				  .sentinel("127.0.0.1", 26379)
				  .sentinel("127.0.0.1", 26380);
		rcf = new LettuceConnectionFactory(sentinelConfig);
		//获取哨兵连接
		RedisSentinelConnection sconn = rcf.getSentinelConnection();
		RedisTemplate rt = new RedisTemplate();
		
		rt.setConnectionFactory(rcf);
		HashOperations ho;
		BoundHashOperations bho;
		BoundValueOperations bvo;
		StringRedisTemplate srt = new StringRedisTemplate();
		srt.setConnectionFactory(rcf);
		ObjectHashMapper ohm;
		Jackson2HashMapper jhm;
		RedisClusterConfiguration rcc = new RedisClusterConfiguration(
				Arrays.asList("localhost:6379","127.0.0.1:6380"));
		LettuceConnectionFactory lcf = new LettuceConnectionFactory(rcc);
		RedisClusterConnection cconn = lcf.getClusterConnection();
		PartialUpdate p;
	}
	
}
