package com.bonc.test;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.server.web.DbStarter;
import org.h2.tools.Server;
import org.junit.Test;
import org.springframework.context.annotation.Bean;

public class H2Test {
	@Test
	public void testWebServer() throws SQLException {
		//,"-baseDir","D:\\h2\\db"
		Server server = Server.createTcpServer("-tcp","-tcpAllowOthers","-ifNotExists","-baseDir","D:\\h2\\db").start();
		server.stop();
	}
	@Test
	@Bean
	public void testConnection() throws Exception {
		Connection conn = DbUtils.getConnection("jdbc:h2:tcp://localhost/./h9test", "sa", "");
		DbUtils.executeQuery(conn, "SELECT * FROM TEST1 ", System.out::println);
		
	}
	@Test
	public void testDbStarter() throws Exception {
		DbStarter ds;
	}
}
