package com.bonc;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
/**
 * 日志：slf4j+logback<p>
 * 安全：spring security+oauth2；jwt<p>
 * 数据库：H2+druid<p>
 * 数据框架：mybatis、jpa、spring-boot-starter-data-*<p>
 * 缓存：ehcache、redis<p>
 * 消息：rabbitmq<p>
 * 测试：junit、MockMvc<p>
 * 加密：单向(PasswordEncoder、MD5)、双向(rsa、base64)<p>
 * 监控：spring boot admin+简单网页监控
 * @author litianlin
 * @date   2019年7月17日上午9:29:52
 * @Description TODO
 */
@SpringBootApplication
@ComponentScan(excludeFilters = {
		@Filter(type = FilterType.ASSIGNABLE_TYPE,
				classes = AutoConfigurationExcludeFilter.class) })
@EnableAsync//异步
@EnableScheduling//定时
@EnableCaching//开启缓存
@PropertySources(value = { 
		@PropertySource(ignoreResourceNotFound=true,value = { "classpath:config/${config.profile}/config.properties" })
		})
public class Application{
	public static void main(String[] args) {
		// 设置环境变量，解决Es的netty与Netty服务本身不兼容问题
		System.setProperty("es.set.netty.runtime.available.processors","false");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("lalalalalala");
			}
		});
		SpringApplication.run(Application.class, args);
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
	
	@Bean//异步调用未捕获异常处理
	public AsyncUncaughtExceptionHandler AsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {
			@Override
			public void handleUncaughtException(Throwable ex, Method method, Object... params) {
				ex.printStackTrace();
				LoggerFactory.getLogger(Application.class)
				.error("method:{},params:{}", method,params);
			}
			
		};
	}
}
