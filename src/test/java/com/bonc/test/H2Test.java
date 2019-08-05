package com.bonc.test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.h2.tools.Server;
import org.junit.Test;
import org.springframework.core.env.PropertySource;
import org.springframework.util.ResourceUtils;

import com.bonc.utils.CommonUtils;
import com.bonc.utils.DbUtils;

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
		    PropertySource<?> ps=CommonUtils.loadYml("spring", "classpath:application.yml").get(0);
		    Class.forName(ps.getProperty("spring.datasource.first.driver-class-name").toString());
		    Connection conn = DriverManager.getConnection(ps.getProperty("spring.datasource.first.url").toString(),
		    		ps.getProperty("spring.datasource.first.username").toString(), ps.getProperty("spring.datasource.first.password").toString());
		    ResultSet rs = conn.createStatement().executeQuery("show columns from user");
		    System.out.println(rs);
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
		DbUtils.executeQuery(conn, "SELECT * FROM TEST1 ", (rs)->{System.out.println(rs);return null;});
		
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
