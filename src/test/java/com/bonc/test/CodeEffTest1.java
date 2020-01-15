package com.bonc.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import org.springframework.web.util.HtmlUtils;

public class CodeEffTest1{
//	spring HtmlUtils测试，转义、转回
	@Test
	public void testHtmlUtils(){
		String str="<lkk>";
		String escStr=HtmlUtils.htmlEscape(str);
		System.out.println(escStr);
		String orginStr=HtmlUtils.htmlUnescape(str);
		System.out.println(orginStr);
	}
//	Jsoup过滤用户输入
	@Test
	public void testJsoup(){
		String str=  "<p><a href='http://example.com/' onclick='stealCookies()'>Link</a></p>";
		System.out.println(Jsoup.clean(str, Whitelist.basic()));
		
	}
//	多实现接口时，接口有同名默认方法，InterfaceA.super.method()调用
	public class TestSuper implements Callable{

		@Override
		public Object call() throws Exception {
//			Callable.super.call();
			return null;
		}

	}
	public static class Lkk{
		static{
			System.out.println("lkk");
		}
		
	}
//	实例内部类不可定义静态成员
//	public class Lkk2{
//		public static final Lkk2 lkk=new Lkk2();
//		public Lkk2(){
//			System.out.println("lkk2");
//		}
//		
//	}
	static{
		System.out.println("ltl");
	}
//	成员类加载时间
	@Test
	public void classLoadTest(){
		Lkk lkk=new Lkk();
	}
	
//	测试重写后this指向
	@Test
	public void test(){
		new Son().print();
	}
	@Test
	public void testGeneric(){
		List<?> lkk=new ArrayList<>();
		lkk.add(null);
		List<String> list=Arrays.asList("lkk");
		List list1=list;
		List<Object> list2=new ArrayList<>();
//		不可转换
//		list2=list;
		List<?> list3=list1;
		list3=list2;
//		只能加入null
//		list3.add("1");
		list3.add(null);
		
	}
}
class Father{
	void print(){
		System.out.println("Father say.");
		System.out.println(this.getClass().getName());
		print();
	}
}
class Son extends Father{
	@Override
	void print() {
		System.out.println("Son say.");
		super.print();
	}
}
