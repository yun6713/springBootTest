
package com.bonc.test;

import javax.persistence.criteria.Predicate;

import org.junit.Test;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import io.lettuce.core.ReadFrom;

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
		rcf.getConnection();
		LettuceClientConfiguration lcc = LettuceClientConfiguration.builder()
			      .readFrom(ReadFrom.SLAVE_PREFERRED) //优先读从
			      .build();
		rcf =new LettuceConnectionFactory(rsc,lcc);
	}
	
}
