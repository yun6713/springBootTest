package com.bonc.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 获取连接，执行sql
 * @author litianlin
 * @date   2019年6月28日上午9:54:45
 * @Description TODO
 */
public class DbUtils {
	public static final Map<String,String> DRIVERS = new MapBuilder<String,String>()
			.put("jdbc:oracle","oracle.jdbc.driver.OracleDriver")
			.put("jdbc:mysql","com.mysql.jdbc.Driver")
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
	public static void executeQuery(Connection conn,String sql,Callback callback) throws SQLException, Exception {
		if(!Objects.requireNonNull(sql, "sql can't be null").toLowerCase().trim().startsWith("select")) {
			throw new RuntimeException("Must be a query sql.");
		}
		try(
				Statement stmt = conn.createStatement();
			){
			 ResultSet rs = stmt.executeQuery(sql);
			 if(callback!=null) {
				 callback.callback(rs);
			 }
			}
	}
	public static class  MapBuilder<K,V>{
		private Map<K,V> map=new HashMap<>();
		public MapBuilder<K,V> put(K key,V value){
			map.put(key, value);
			return this;
		}
		public Map<K,V> build(){
			return map;
		}
	}
	public static interface Callback{
		void callback(ResultSet rs);
	}
}
