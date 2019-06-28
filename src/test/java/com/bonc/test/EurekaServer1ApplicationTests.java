package com.bonc.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
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
}
