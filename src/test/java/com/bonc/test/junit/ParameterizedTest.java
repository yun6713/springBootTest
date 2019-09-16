package com.bonc.test.junit;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParameterizedTest {
	@Parameters
	public static List<Object> getData(){
		return Arrays.asList(new int[]{1,2,3},new int[]{12,3,4});
	}
	int[] a;
	
	public ParameterizedTest(int[] a) {
		super();
		this.a = a;
	}
	@Test
	public void test(){
		test1(a);
	}
	public void test1(int[] a){
		System.out.println(String.format("%1$s", Arrays.toString(a)));
	}
}
