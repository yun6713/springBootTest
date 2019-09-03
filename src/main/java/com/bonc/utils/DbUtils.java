package com.bonc.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import com.bonc.utils.MapUtils.MapBuilder;

/**
 * 获取连接，执行sql
 * @author litianlin
 * @date   2019年6月28日上午9:54:45
 * @Description TODO
 */
public class DbUtils {
	public static final Map<String,String> DRIVERS = new MapBuilder<String,String>()
			.put("jdbc:oracle","oracle.jdbc.driver.OracleDriver")
			.put("jdbc:mysql","com.mysql.cj.jdbc.Driver")
			.put("jdbc:h2", "org.h2.Driver")
			.build();
	public static Connection getConnection(String driverName,String url,String uname,String pwd) throws Exception {
		if(driverName==null) {
			throw new RuntimeException("Drivername is null");
		}
		Class.forName(driverName);
		return DriverManager.getConnection(url,uname,pwd);
	}
	public static Connection getConnection(String url,String uname,String pwd) throws Exception {
		String temp = url.toLowerCase().trim();
		temp=temp.substring(0, temp.indexOf(":", temp.indexOf(":")+1));
		String driver = DRIVERS.get(temp);
		if(driver==null) {
			throw new RuntimeException("UnSupported url");
		}
		return getConnection(driver,url,uname,pwd);
	}
	public static void executeNonQuery(Connection conn,String sql) throws SQLException, Exception {
		if(Objects.requireNonNull(sql, "sql can't be null").toLowerCase().trim().startsWith("select")) {
			throw new RuntimeException("Can't be a query sql.");
		}
		try(
				Statement stmt = conn.createStatement();
			){
			 stmt.execute(sql);		
			}
		
	}
	public static void executeQuery(Connection conn,String sql,Function<ResultSet,Void> callback) throws SQLException, Exception {
		if(!Objects.requireNonNull(sql, "sql can't be null").toLowerCase().trim().startsWith("select")) {
			throw new RuntimeException("Must be a query sql.");
		}
		try(
				Statement stmt = conn.createStatement();
			){
			 ResultSet rs = stmt.executeQuery(sql);
			 if(callback!=null) {
				 callback.apply(rs);
			 }
			}
	}
}
