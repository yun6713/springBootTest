package com.bonc.config;

import java.sql.SQLException;
import java.util.Properties;

import org.h2.server.web.WebServlet;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.bonc.utils.MapUtils;

@Configuration
@ConditionalOnExpression("${h2.enabled:false}==true")
public class H2Config{
//	@Bean//启动webServer
	public ServletRegistrationBean<WebServlet> registH2WebServlet() {
		ServletRegistrationBean<WebServlet> srb = new ServletRegistrationBean<>(new WebServlet());
		srb.setInitParameters(new MapUtils.MapBuilder<String, String>()
				.put("-webAllowOthers", "")
				.put("-trace", "")
				.build());
		srb.addUrlMappings("/console/*");
		return srb;
	}
	@Bean(name="h2Server",initMethod="start",destroyMethod="stop")
	@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
	@ConditionalOnExpression("${h2.enableAppServer:true}==true")
	public Server h2Server() throws SQLException {
		return Server.createTcpServer("-tcp","-tcpAllowOthers","-ifNotExists","-baseDir","D:\\h2\\db");
	}
	@Bean("firstDataSourceProperties")
	@ConfigurationProperties(prefix="spring.datasource.first")
	public Properties firstDataSource() {
		return new Properties();
	}
	@Bean("druidConfig")
	@ConfigurationProperties(prefix="spring.datasource.druid")
	public Properties druidConfig() {
		return new Properties();
	}
	
	@Bean("h2DataSource")
	@Lazy
	public DruidDataSource h2DataSource(@Qualifier("firstDataSourceProperties")Properties firstDataSourceProperties,
			@Qualifier("druidConfig")Properties druidConfig) {
		firstDataSourceProperties.putAll(druidConfig);
		DruidDataSource dds = DruidDataSourceBuilder.create().build();
		dds.configFromPropety(firstDataSourceProperties);
		return dds;
	}
}
