package com.bonc.test.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestBean2 {
	@Autowired
	public TestBean1 info;
	public TestBean1 info2;

	@Override
	public String toString() {
		return "TestBean2 [info=" + info + ", info2=" + info2 + "]";
	}
}
