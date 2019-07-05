package com.bonc.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.bonc.mapper.UserOperation;
/**
 * 配置mybatis SQLSessionFactoryBean（ds\tm\ml）
 * @author litianlin
 * @date   2019年7月5日下午12:57:54
 * @Description TODO
 */
@Configuration
@MapperScan(basePackageClasses= {UserOperation.class},
sqlSessionFactoryRef="firstMybatis"
)
public class MybatisConfig {
	@Bean("firstMybatis")
	public SqlSessionFactoryBean first(DataSource dataSource,
			@Value("${mybatis.mapper-locations:}")String locs) throws IOException {
		SqlSessionFactoryBean sfb = new SqlSessionFactoryBean();
		sfb.setDataSource(dataSource);
		sfb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(locs));
		return sfb;		
	}
}
