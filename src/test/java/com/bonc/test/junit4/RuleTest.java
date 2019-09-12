package com.bonc.test.junit4;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.core.env.PropertySource;

import com.alibaba.druid.pool.DruidDataSource;
import com.bonc.utils.CommonUtils;

/**
 * 规则测试，扩展junit4
 * @author litianlin
 * @date   2019年9月12日下午4:07:55
 * @Description TODO
 */
@RunWith(JUnit4.class)
public class RuleTest {
	@Rule
	public ContiPerfRule cpr=new ContiPerfRule();
	public static DruidDataSource dds;
	@BeforeClass
	public static void init() throws FileNotFoundException, IOException {
		dds=new DruidDataSource();
		Properties properties=new Properties();
		properties.put("druid.url", "jdbc:mysql://192.168.1.123:3306/test01");
		properties.put("druid.username", "root");
		properties.put("druid.password", "boncmysql");
		dds.configFromPropety(properties);
		System.out.println("inited");
	}
	@Test(timeout=100000)
	@PerfTest(invocations=100000,threads=10)
	public void testQuery() throws SQLException {
		String sql="select * from user";
		try(
				Connection conn=dds.getConnection();
				Statement stat=conn.createStatement();
				ResultSet rs=stat.executeQuery(sql);){
			
		}
	}
}
