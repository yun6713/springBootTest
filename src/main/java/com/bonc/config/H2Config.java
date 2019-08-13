package com.bonc.config;

import java.sql.SQLException;
import java.util.List;
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
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.bonc.utils.MapUtils;
/**
 * 配置H2数据源，开启H2浏览器访问。
 * @author litianlin
 * @date   2019年7月31日上午10:54:11
 * @Description TODO
 */
@Configuration
@ConditionalOnExpression("${h2.enabled:false}==true")
public class H2Config{
//	@Bean//启动webServer，spring.h2.console.*自动配置。
	public ServletRegistrationBean<WebServlet> registH2WebServlet() {
		ServletRegistrationBean<WebServlet> srb = new ServletRegistrationBean<>(new WebServlet());
		srb.setInitParameters(new MapUtils.MapBuilder<String, String>()
				.put("-webAllowOthers", "")
				.put("-trace", "")
				.put("-browser", "")
				.build());
		srb.addUrlMappings("/h2console/*");
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
	@Primary
//	@ConfigurationProperties(prefix="spring.datasource.first")
	public DruidDataSource h2DataSource() {
		DruidDataSource dds = configDruidDataSource(firstDataSource(),druidConfig());
		List<Filter> filters=dds.getProxyFilters();
		filters.add(wallFilter());
		return dds;
//		return configDruidDataSource(firstDataSource(),druidConfig());
	}
	/**
	 * 手动配置防火墙，防止不必要报错
	 * @return
	 */
	@Bean
	public WallFilter wallFilter() {
		WallConfig config=new WallConfig();
		config.setNoneBaseStatementAllow(true);//执行DDL		
		WallFilter wall=new WallFilter();	
		wall.setConfig(config);
		//sql解析错误时处理策略，log.err输出，不抛异常
		wall.setLogViolation(true);
		wall.setThrowException(false);
		return wall;
	}
	private DruidDataSource configDruidDataSource(Properties dbProps,Properties dsProps){
		Properties p = new Properties();
		if(dbProps!=null)
			dbProps.forEach((k,v)->{p.setProperty("druid."+k, Objects.toString(v));});
		if(dsProps!=null)
			dsProps.forEach((k,v)->{p.setProperty("druid."+k, Objects.toString(v));});
		DruidDataSource dds = DruidDataSourceBuilder.create().build();
		dds.configFromPropety(p);
		return dds;
	}
}
