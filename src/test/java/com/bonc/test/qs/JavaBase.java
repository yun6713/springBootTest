package com.bonc.test.qs;

import java.io.Externalizable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.junit.Test;

public class JavaBase {
	@Test
	public void javaBase1() {
		//==判定
		System.out.println(2==new Double(2.0));//true
	}
	@Test
	public void mathApi() {
		//Math api
		System.out.println(Math.round(-1.5));//.5时，向正无穷大取值
		System.out.println(Math.round(1.5));
		System.out.println(Math.ceil(1.5));//向上取整
		System.out.println(Math.floor(1.5));//向下取整
		System.out.println(Math.floorDiv(7, 2));
		System.out.println(Math.floorMod(7, 2));
		System.out.println(Math.floorDiv(-7, 2));
		System.out.println(Math.floorMod(-7, 2));
	}
	@Test
	public void strApi() {
		//String相关
		//f
		String a1 = new String("");
		String a2 = new String("").intern();//常量池对象
		System.out.println(a1==a2);
		//f
		String b1 = "wang";
		String b2=b1+"na";
		System.out.println("wangna"==b2);
		//t
		final String c1 = "wang";
		String c2=c1+"na";
		System.out.println("wangna"==c2);
	}
//	不可同时使用abstract final
//	public abstract final class A{}
	
	public void xlh(){
		//序列化接口
		Serializable s;
		Externalizable e;
		//jdk序列化
		ObjectOutputStream oos;
		ObjectInputStream ois;
		
	}
}
