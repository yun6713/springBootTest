package com.bonc.test.junit4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
/**
 * 测试集不可包含本类，测试集元素必须为public<br/>
 * 测试类运行顺序与测试集顺序一致。
 * @author litianlin
 * @date   2019年9月12日下午4:05:05
 * @Description TODO
 */
@RunWith(Suite.class)
@SuiteClasses(value = { SuiteTest1.class,ParameterizedTest.class,
		RuleTest.class })
public class SuiteTest {
}
