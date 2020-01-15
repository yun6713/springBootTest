package com.bonc.test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

import org.apache.catalina.core.ApplicationContext;
import org.junit.Test;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

public class CommonTest5 {
	@Test
	/*
	 * 新旧历生日提醒
	 * 
	 */
	public void testCalen(){
		Calendar c=null;
		Calendar.getAvailableCalendarTypes().forEach(System.out::println);
		AutowireCapableBeanFactory beanFactory;
		ApplicationContext ac=null;
		
	}
	@Test
	public void testLA() throws InterruptedException{
		LongAccumulator LA=new LongAccumulator(Long::max,100L);
	    System.out.println(LA.get());
	    LA.accumulate(LA.get()+1);
	    Thread.sleep(100);
	    System.out.println(LA.get());
	}
	@Test
	public void testMove(){
		int a=-1;
		int b=1;
		System.out.println(a<<3);
		System.out.println(a>>3);
		System.out.println(a>>>3);
		System.out.println(a>>>32);
		System.out.println(1>>>33);
		System.out.println((~b|b)>>>3);
	}
	@Test
	public void testArray(){
		String[] strs={"1","2"};
		System.out.println(Arrays.toString(strs));
		Double d=Double.valueOf(".0");
		System.out.println(d);
		System.out.println(d>=0);
		System.out.println(d==0);
		System.out.println(Double.toHexString(d));
		System.out.println(Double.doubleToLongBits(d));
		System.out.println(Double.isNaN(d));
		System.out.println(d.equals(Double.MIN_VALUE));
		System.out.println(d.equals(Double.MIN_NORMAL));
		System.out.println(Double.MIN_VALUE==0.0);
		System.out.println(Double.MIN_NORMAL==0.0);
		double d2=0.0;
		System.out.println(d2);
		System.out.println(d2==-0.00);
		System.out.println(-0.0==0.0);
	}
//	10,二进制0b1010，八进制012，十进制10，十六进制0xA
	@Test
	public void testDouble(){
		double d2=0.0;
		int a=010;
		System.out.println(a);
//		d2&0xFFFFFFFF;
	}
	@Test
	public void testStringMatch(){
		String str="SELECT * from HR_DM.HR_DM_HOME_ACTIVE_ORG_MON hr",
			reg="^\\w{4,7}\\s[\\w\\W]+$";
		Pattern p=Pattern.compile(reg);
		System.out.println(str.matches(reg));
		System.out.println(Pattern.matches(reg, str));
	}
	public void testString(){
		String str=new String("lkk").intern();
		Lock lock=new ReentrantReadWriteLock().readLock();
		lock.lock();		
	}
	@Test
	public void classLoaderTest() throws ClassNotFoundException{
		System.out.println(Object.class.getClassLoader());
		System.out.println(this.getClass().getClassLoader().getParent());
		System.out.println(this.getClass().getClassLoader());
		System.out.println(Object.class==this.getClass().getClassLoader().loadClass("java.lang.Object"));
	}
}
