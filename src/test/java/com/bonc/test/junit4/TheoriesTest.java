package com.bonc.test.junit4;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class TheoriesTest {
	public static final int MAX=15;
	
	@Theory//@TestOn，直接赋值
	public void test(@TestedOn(ints = { 1,2,4,5,6,7,8,9,0,20 }) int num,
			@TestedOn(ints = { 1,2,4,5,6,7,8,9,0,20 }) int divisor) {
		Assume.assumeTrue(divisor!=0);
		Assert.assertTrue(num/divisor+"larger than the MAX value:"+MAX, num/divisor<MAX);
		System.out.println(num);
	}
}
