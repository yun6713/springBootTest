package com.bonc.test.junit4;

import org.junit.Assume;
import org.junit.Rule;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
/**
 * 只能双参数
 * @author litianlin
 * @date   2019年9月12日下午5:52:40
 * @Description TODO
 */
@RunWith(Theories.class)
public class TheoriesTest2 {
	public static final int SIZE=20;
	@DataPoints
	public static int[] dataPoints() {
		int[] ints=new int[SIZE];
		for (int i = 0; i < SIZE; i++) {
			ints[i]=i;
		}
		return ints;
	}
//	@Rule
//	public ErrorCollector ec=new ErrorCollector();
	@Theory
	public void test(int a,int b,int c) {
		Assume.assumeTrue(a>0&&b>0&&c>0);
		try {
			if(triangleJudge(a,b,c))
			System.out.println(String.format("Can consist triangle:%1$s,%2$s,%3$s", a,b,c));
		}catch (Exception e) {
			
		}
	}
	public boolean triangleJudge(int a,int b,int c) throws Exception {
		if(a+b>c && a+c>b && b+c>a) {
			return true;
		}else {
//			ec.addError( new Exception(String.format("Can't consist triangle:{},{},{}", a,b,c)));
		}
		return false;
	}
}
