package com.bonc.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.bonc.mapper.UserOperation;
import com.bonc.utils.MapUtils;
/**
 * 配置mybatis SQLSessionFactoryBean（dataSource\transactionManager\mapperLocation）<p>
 * mybatis跨库：配置DatabaseIdProvider，sql标签的databaseId属性，优先使用databaseId匹配的标签；<p>
 * 设置类型别名包，SqlSessionFactoryBean#setTypeAliasesPackage；别名默认类名首字母小写，@Alias指定。<p>
 * 日志扫描顺序：slf4j、jcl、log4j2、log4j、jul；<p>
 * springboot可通过mybatis.configuration.*进行完全控制<p>
 * 
 * @author litianlin
 * @date   2019年7月5日下午12:57:54
 * @Description TODO
 */
@Configuration
@MapperScan(basePackageClasses= {UserOperation.class},
sqlSessionFactoryRef="firstMybatis"
)
public class MybatisConfig {
	@Autowired
	DataSource dataSource;
	@Bean("firstMybatis")
	public SqlSessionFactoryBean first(DatabaseIdProvider databaseIdProvider,
			@Value("${mybatis.mapper-locations:}")String locs) throws IOException {
		SqlSessionFactoryBean sfb = new SqlSessionFactoryBean();
		sfb.setDataSource(dataSource);
		sfb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(locs));
		sfb.setDatabaseIdProvider(databaseIdProvider);
		sfb.setTypeAliasesPackage("com.bonc.entity.jpa");TransactionManager tm;
//		sfb.setTransactionFactory(transactionFactory());//默认值为SpringManagedTransactionFactory
		return sfb;		
	}
	/*
	 * 配置跨库支持，databaseName--databaseId关联
	 * 关联信息可从文件读取。
	 */
	@Bean
	public DatabaseIdProvider databaseIdProvider(){
		DatabaseIdProvider databaseIdProvider=new VendorDatabaseIdProvider();
		Properties p=new Properties();
		p.putAll(MapUtils.builder("SQL Server", "sqlserver")
				.put("MySQL", "mysql")
				.put("Oracle", "oracle")
				.put("DB2", "db2")
				.put("H2", "h2").build());
		databaseIdProvider.setProperties(p);
		return databaseIdProvider;
	}
	// 事务配置，用于@Transactional，绑定编程式事务。
	@Bean(name = "mybatisTransactionManager")
	public PlatformTransactionManager mybatisTransactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}
}
