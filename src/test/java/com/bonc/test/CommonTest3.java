package com.bonc.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import org.junit.Test;

import com.bonc.utils.FileUtils;
import com.bonc.utils.StringUtils;


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
	@Test
	public void findArgs() throws Exception {
		String content=FileUtils.file2String("classpath:test2");
		String pattern="params\\.get\\(\\s*\"\\s*([\\w]+)";
		Set<String> result=StringUtils.getAllStrs(content,pattern,1);
		System.out.println(result);
	}
	public static void main(String[] args) throws Exception {
		new CommonTest3().findArgs();
		System.out.println("abc".indexOf("c",1));
		System.out.println("abc".substring(0, 3));
	}
	@Test
	public void test01() throws Exception {
		String content=FileUtils.file2String("classpath:test2");
		content = content.replaceAll("\r\n", "<p>\r\n")+"<p>";
		System.out.println(content);
	}
}
