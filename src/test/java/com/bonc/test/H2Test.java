package com.bonc.test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

import org.h2.server.web.DbStarter;
import org.h2.tools.Server;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;

public class H2Test {
	@Test
	public void test01() throws Exception{
		Server server;
		try {
			server = Server.createTcpServer("-ifNotExists").start();
			System.out.println(server.getURL());
//			testConnection();
			while(true){}
//			server.stop();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testH2Connection(){
		try {
		    Class.forName("org.h2.Driver");
		    Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/./test", "sa", "");
		    conn.isClosed();
		    } catch (Exception e) {
		    	e.printStackTrace();;
		    }
	}
	@Test
	public void testWebServer() throws SQLException {
		//,"-baseDir","D:\\h2\\db"
		Server server = Server.createTcpServer("-tcp","-tcpAllowOthers","-ifNotExists","-baseDir","D:\\h2\\db").start();
		server.stop();
	}
	@Test
	public void testConnection() throws Exception {
		Connection conn = DbUtils.getConnection("jdbc:h2:tcp://localhost/./h9test", "sa", "");
		DbUtils.executeQuery(conn, "SELECT * FROM TEST1 ", System.out::println);
		
	}
	@Test
	public void testDbStarter() throws Exception {
		DbStarter ds;
	}
	@Test
	public void testFileNameFilter() throws Exception {
		File file = ResourceUtils.getFile("D:\\eclipse-workspace\\springbootTest\\src\\main\\resources\\data");
		System.out.println(Arrays.toString(file.list()));
		file = ResourceUtils.getFile("D:\\eclipse-workspace\\springbootTest\\src\\main\\resources\\data\\role.csv");
		System.out.println(file.exists());
		System.out.println(file.getName());
		
	}
}
