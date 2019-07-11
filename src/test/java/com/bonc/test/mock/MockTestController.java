package com.bonc.test.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
/**
 * TestController测试用例，测试方法与controller方法同名
 * @author litianlin
 * @date   2019年7月11日下午2:34:21
 * @Description TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MockTestController {
	@Autowired
	WebApplicationContext wac;
	MockMvc mvc;
	@Before
	public void initMock() {
		mvc=MockMvcBuilders.webAppContextSetup(wac).build();;
	}
	@Test
	public void test() throws Exception {
		String url="/test";
		ResultActions ra=mvc.perform(MockMvcRequestBuilders.get(url))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("success"));
		System.out.println("TestController#test() success,result:"
				+ra.andReturn().getResponse().getContentAsString());
	}
	@Test
	public void test1() throws Exception {
		String url="/test1";
		ResultActions ra=mvc.perform(MockMvcRequestBuilders.get(url))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("success1"));
		System.out.println("TestController#test1() success,result:"
				+ra.andReturn().getResponse().getContentAsString());
	}
}
