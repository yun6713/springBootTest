package com.bonc;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@EnableAsync
@RestController
//@EnableCaching//开启缓存
public class Application{
	private static final Logger log=org.slf4j.LoggerFactory.getLogger(Application.class);
	static Process process;
	public static void main(String[] args) {
		// 设置环境变量，解决Es的netty与Netty服务本身不兼容问题
		try {
			process=Runtime.getRuntime().exec("java -jar D:\\eclipse-workspace\\springboot01-3\\target\\springcloud01-0.0.1-SNAPSHOT.jar");
		} catch (IOException e) {
			log.error("Start spring boot admin server failed, cause:{}", e.getMessage());
			e.printStackTrace();
		}
        System.setProperty("es.set.netty.runtime.available.processors","false");
        SpringApplication.run(Application.class, args);
	}
	@RequestMapping("/stopSpringbootAdminServer")
	public Object stopSpringbootAdminServer(){
		if(process.isAlive())
			process.destroy();
		return "Success";
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
