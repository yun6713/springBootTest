package com.bonc.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CommonTest3 {
	@Test
	public void findList() throws Exception {
		List<String> list = new ArrayList<>();
		list.add("aaa");
		list.add("aaav");
		list.add("aaag");
		System.out.println(list.toString());
		Object a="1";
		System.out.println(a.equals("1"));
	}
}
