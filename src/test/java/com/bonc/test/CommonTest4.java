package com.bonc.test;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;
import com.bonc.utils.DbUtils;


public class CommonTest4 {
	@Test
	public void shArgs() throws SQLException, Exception {
		DbUtils.executeQuery(DbUtils.getConnection("jdbc:mysql://192.168.1.123:3306/demo?useUnicode=true&characterEncoding=UTF-8",
				"root", "boncmysql"), 
				"select * from camel_rel.camel_user union select * from camel.camel_user", rs->{
					try {
						while(rs.next()) {
							System.out.println(rs.getString("user_name"));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return null;
				});
	}
	@Test
	public void test1() throws Exception {
		
		System.out.println(new DruidDataSource() instanceof DataSource);
	}
}
