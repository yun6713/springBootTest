package com.bonc.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bonc.controller.TestController;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	//配置swagger选择器、api信息
	@Bean
	public Docket api() {
		
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
	            .apis(RequestHandlerSelectors.any())//筛选handler
	            .paths(PathSelectors.any())//筛选path
	            .build()
	            .apiInfo(apiInfo());
	}
	@Bean
	public Docket api2() {
		
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("ltl")//区分多个Docket
				.select()
	            .apis(RequestHandlerSelectors.basePackage(TestController.class.getPackage().getName()))//筛选handler
	            .paths(PathSelectors.any())//筛选path
	            .build()
	            .apiInfo(apiInfo());
	}
	//api信息；也可通过ApiInfoBuilder构建。
	private ApiInfo apiInfo() {
		return new ApiInfo(
	            "Spring Boot 项目集成 Swagger 实例文档",
	            "DESC",
	            "API V1.0",
	            "Terms of service",
	            new Contact("ltl", "http://springfox.github.io/springfox/docs/current/", "litianlin@bonc.com.cn"),
                "Apache", 
                "http://www.apache.org/", 
                Collections.emptyList());
	}
}
