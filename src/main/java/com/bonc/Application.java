package com.bonc;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
/**
 * 日志：slf4j+logback<p>
 * 安全：spring security+oauth2；jwt<p>
 * 数据库：H2+druid<p>
 * 数据框架：mybatis、jpa、spring-boot-starter-data-*<p>
 * 缓存：ehcache、redis<p>
 * 测试：junit、MockMvc<p>
 * 加密：单向(PasswordEncoder、MD5)、双向(rsa、base64)<p>
 * 监控：spring boot admin+简单网页监控
 * @author litianlin
 * @date   2019年7月17日上午9:29:52
 * @Description TODO
 */
@SpringBootApplication
@EnableAsync
@EnableCaching//开启缓存
public class Application{
	public static void main(String[] args) {
		// 设置环境变量，解决Es的netty与Netty服务本身不兼容问题
		System.setProperty("es.set.netty.runtime.available.processors","false");
        SpringApplication.run(Application.class, args);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("lalalalalala");
			}
		});
	}
	/**
	 * 配置异步执行线程池TaskExecutor。
	 * @return
	 */
	@Bean
	public AsyncConfigurerSupport  asyncConfigurerSupport () {
		return new AsyncConfigurerSupport () {
			@Override
			public Executor getAsyncExecutor() {
				//配置以提高线程池效率，本处仅作示例。
				ExecutorService es=Executors.newFixedThreadPool(5);
				//特定类型，异步传递SecurityContext
				return new DelegatingSecurityContextExecutorService(es);
			}
		};
	}
	
}
