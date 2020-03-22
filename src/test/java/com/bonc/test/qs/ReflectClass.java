package com.bonc.test.qs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Test;
import org.springframework.util.ReflectionUtils;

public class ReflectClass {
	@Test
	public void classLoader(){
		//BootstrapClassLoader、ExtClassLoader、AppClassLoader
		ClassLoader cl;
		cl = ReflectClass.class.getClassLoader();
		System.out.println(cl);
		cl = cl.getParent();
		System.out.println(cl);
		cl = cl.getParent();
		System.out.println(cl);
		
	}

	public void element() {
		Package pack;
		Class clazz;
		Type type;
		Constructor cons;
		Method mtd;
		Parameter p;
		Type t;
		ParameterizedType pt;//带泛型类型
		Field fld;
		Annotation ann;
		Array array;
		
	}
	
	public void reflect(){
		ReflectionUtils ru2;//spring反射工具类
		
	}
}
