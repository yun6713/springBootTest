package com.bonc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
/**
 * 日志：slf4j+logback
 * 安全：spring security+oauth2；jwt
 * 数据库：H2+druid
 * 数据框架：mybatis、jpa、spring-boot-starter-data-*
 * 缓存：ehcache、redis
 * 测试：junit、MockMvc
 * 加密：单向(PasswordEncoder、MD5)、双向(rsa、base64)
 * @author litianlin
 * @date   2019年7月17日上午9:29:52
 * @Description TODO
 */
@SpringBootApplication
@EnableCaching//开启缓存
public class Application{
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
