package com.bonc.test.junit;

import java.util.Arrays;
import java.util.List;

import org.junit.Assume;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class TheoriesTest {
	@DataPoints//标记public static方法，返回数组、Iterable
	public static List<Object> getData(){
		return Arrays.asList(1,2,312,4);
	}
	@Theory//有多少个变量，声明多少个形参；类似参数化模式，用形参替换成员变量。
	public void test1(int a,int b,int c){
		Assume.assumeTrue(a+b>c);
		Assume.assumeTrue(c+b>a);
		Assume.assumeTrue(a+c>b);
		System.out.println(String.format("%1$s,%2$s,%3$s", a,b,c));
	}

}
