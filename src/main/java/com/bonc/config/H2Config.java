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
//	@Bean(name="h2Server",destroyMethod="stop")
//	@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
//	@ConditionalOnExpression("${h2.enableAppServer:true}==true")
	public Server h2Server() throws SQLException {
		Server server = Server.createTcpServer("-tcp","-tcpPort","9092","-tcpAllowOthers","-ifNotExists","-baseDir","D:\\h2\\db");
		server.start();
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
	@Lazy
//	@ConfigurationProperties(prefix="spring.datasource.first")
	public DruidDataSource h2DataSource() {
//		Properties p = new Properties();
//		firstDataSource().forEach((k,v)->{if(k!=null)p.put("druid."+k, v);});
//		DruidDataSource dds = DruidDataSourceBuilder.create().build();
//		dds.configFromPropety(p);
		return configDruidDataSource(firstDataSource(),druidConfig());
	}
	private DruidDataSource configDruidDataSource(Properties dbProps,Properties dsProps){
		Properties p = new Properties();
		dbProps.forEach((k,v)->{if(k!=null)p.put("druid."+k, v);});
		dsProps.forEach((k,v)->{if(k!=null)p.put("druid."+k, v);});
		DruidDataSource dds = DruidDataSourceBuilder.create().build();
		dds.configFromPropety(p);
		return dds;
	}
}
