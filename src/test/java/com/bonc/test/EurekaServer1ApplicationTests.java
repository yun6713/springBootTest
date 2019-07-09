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
		String pwd="world";
		BCryptPasswordEncoder b = new BCryptPasswordEncoder();
		String hashPass = b.encode(pwd);
		System.out.println(hashPass);
		System.out.println(b.matches(pwd, hashPass));
		System.out.println(b.matches(pwd, "$2a$10$pjFDJCQk.0jObvnlQsp32.XLXHiGVaIlobaIS8FBVWdPOWEE/NjSC"));
	}
}
