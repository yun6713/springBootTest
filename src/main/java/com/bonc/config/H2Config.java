package com.bonc.config;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

import org.h2.server.web.WebServlet;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.bonc.utils.MapUtils;

@Configuration
@ConditionalOnExpression("${h2.enabled:false}==true")
public class H2Config{
	@Bean//启动webServer
	public ServletRegistrationBean<WebServlet> registH2WebServlet() {
		ServletRegistrationBean<WebServlet> srb = new ServletRegistrationBean<>(new WebServlet());
		srb.setInitParameters(new MapUtils.MapBuilder<String, String>()
				.put("-webAllowOthers", "")
				.put("-trace", "")
				.put("-browser", "")
				.build());
		srb.addUrlMappings("/h2-console/*");
		return srb;
	}
	@Bean(name="h2Server",initMethod="start",destroyMethod="stop")
	@ConditionalOnExpression("${h2.enableAppServer:true}==true")
	@Value("${h2.serverConfig:}")
	public Server h2Server(String[] configs) throws SQLException {
		Server server = Server.createTcpServer(configs);
		System.out.println(server.getURL());
		return server;
	}

	@Bean("firstDataSource")
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
	@DependsOn("h2Server")
	public DruidDataSource h2DataSource() {
		return configDruidDataSource(firstDataSource(),druidConfig());
	}
	private DruidDataSource configDruidDataSource(Properties dbProps,Properties dsProps){
		Properties p = new Properties();
		dbProps.forEach((k,v)->{p.setProperty("druid."+k, Objects.toString(v));});
		dsProps.forEach((k,v)->{p.setProperty("druid."+k, Objects.toString(v));});
		DruidDataSource dds = DruidDataSourceBuilder.create().build();
		dds.configFromPropety(p);
		return dds;
	}
}
