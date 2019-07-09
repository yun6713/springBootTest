package com.bonc.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class EurekaServer1ApplicationTests {
	
	@Test
	public void contextLoads() {
		
	}
	@Autowired
	ElasticsearchTemplate et;
	@org.junit.Test
	public void testJest() {
		System.out.println(et.getClass());
		Object obj = et.queryForAlias("jftest");
		System.out.println(obj);
	}
	@Test
	public void testBCryptPasswordEncoder(){
		String pwd="";
		BCryptPasswordEncoder b = new BCryptPasswordEncoder();
		System.out.println("{bcrypt}"+b.encode(pwd));
	}
}
