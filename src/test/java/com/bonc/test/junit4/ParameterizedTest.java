package com.bonc.test.junit4;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Parameterized.class)
/**
 * 参数化测试
 * @author litianlin
 * @date   2019年9月12日下午3:59:26
 * @Description TODO
 */
public class ParameterizedTest {

	private static final Logger log = LoggerFactory.getLogger(ParameterizedTest.class);

	@Parameters
	public static Collection<Integer[]> data() {

		return Arrays.asList(new Integer[][] {

				{ 0, 0 }, { 1, 1 }, { 2, 1 },

				{ 3, 2 }, { 4, 3 }, { 5, 5 },

				{ 6, 8 } });

	}

	private int value;

	private int expected;

	public ParameterizedTest(int input, int expected) {

		value = input;

		this.expected = expected;
		System.err.println("lalala");
	}

	@Test

	public void fibonacciNumberCall() {

		log.info("expected {} fib(value) {}", expected, fib(value));

		Assert.assertEquals(expected, fib(value));

	}

	public static int fib(int n) {

		if (n < 2) {

			return n;

		} else {

			return fib(n - 1) + fib(n - 2);

		}

	}

}
