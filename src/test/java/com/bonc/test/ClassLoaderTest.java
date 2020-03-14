package com.bonc.test;

import org.junit.Test;

public class ClassLoaderTest {
	@Test
	public void classLoaders(){
//		启动类加载器
		ClassLoader cl=System.class.getClassLoader();
		System.out.println(cl==null?"null":cl.getClass().getName());
//		AppClassLoader
		System.out.println(ClassLoaderTest.class.getClassLoader().getClass().getName());
//ExtClassLoader
		System.out.println(ClassLoaderTest.class.getClassLoader().getParent().getClass().getName());
		System.out.println(ClassLoaderTest.class.getClassLoader().getParent().getParent().getClass().getName());
	}
	
	static{
		System.out.println("lkk");
	}
	static ClassLoaderTest clt=new ClassLoaderTest(7);
	public static final ClassLoaderTest clt1=new ClassLoaderTest(8);
	static int a=1,b;
	public ClassLoaderTest(int a){
		ClassLoaderTest.a=a;
		b=3;
		System.out.println("ClassLoaderTest:a="+a);
		System.out.println("ClassLoaderTest:b="+b);
	}
	public static void main(String[] args) {
		System.out.println("ClassLoaderTest.main:a="+a);
		System.out.println("ClassLoaderTest.main:b="+b);
	}
	public void classLoader(){
//		BootstrapClassLoader bcl;
//		ExtClassLoader ecl;
//		AppClassLoader acl;
		
		
	}
}
