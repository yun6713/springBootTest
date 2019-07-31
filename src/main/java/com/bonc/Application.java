package com.bonc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * 日志：slf4j+logback<p>
 * 安全：spring security+oauth2；jwt<p>
 * 数据库：H2+druid<p>
 * 数据框架：mybatis、jpa、spring-boot-starter-data-*<p>
 * 缓存：ehcache、redis<p>
 * 测试：junit、MockMvc<p>
 * 加密：单向(PasswordEncoder、MD5)、双向(rsa、base64)<p>
 * @author litianlin
 * @date   2019年7月17日上午9:29:52
 * @Description TODO
 */
@SpringBootApplication
//@EnableCaching//开启缓存
public class Application{
	public static void main(String[] args) {
		// 设置环境变量，解决Es的netty与Netty服务本身不兼容问题
        System.setProperty("es.set.netty.runtime.available.processors","false");
        SpringApplication.run(Application.class, args);
	}
	
}
