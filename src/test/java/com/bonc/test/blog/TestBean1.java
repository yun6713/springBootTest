package com.bonc.test.blog;

import org.springframework.stereotype.Component;

@Component
public class TestBean1{
	public String info="lkk";

	@Override
	public String toString() {
		return "TestBean2 [info=" + info + "]";
	}
}
