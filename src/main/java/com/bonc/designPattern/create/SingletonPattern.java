package com.bonc.designPattern.create;

import java.util.Arrays;

/**
 * 构造器私有，缓存实例。
 * 懒汉式、饿汉式、枚举类、内部类
 * @author Administrator
 *
 */
public class SingletonPattern {
	//静态私有内部类持有外部类实例
	private SingletonPattern(){}
	
	private static class InnerClassImpl{
		private static SingletonPattern inner=new SingletonPattern();
	}
	public SingletonPattern getInstance(){
		return InnerClassImpl.inner;
	}
	//懒汉式，获取时初始化实例；双层null判定，防止并发问题
	public static class LazyImpl{
		private static LazyImpl lazy;
		
		private LazyImpl(){
		}
		public static LazyImpl getInstance(){
			if(lazy==null){
				synchronized (lazy) {
					if(lazy==null){
						lazy=new LazyImpl();
					}
				}
			}
			return lazy;
		}
	}
	//饿汉式，类加载时即初始化实例
	public static class EagerImpl{
		
		public static EagerImpl eager=new EagerImpl();
		private EagerImpl(){}
		public static EagerImpl getInstance(){
			return eager;
		}
	}
	//枚举类，继承Enum；ordinal()返回索引值，0开始。
	public static enum EnumImpl{
		ENUM("enum"),
		LAZY("lazy"),
		EAGER("eager"),
		INNER_CLASS("inner_class");
		EnumImpl(String desc){
			this.desc=desc;
		}
		private String desc;

		public String getDesc() {
			return desc;
		}
	}
	public static void main(String[] args) {
		Arrays.asList(EnumImpl.values())
			.stream()
			.map(EnumImpl::ordinal)
			.map(i->1<<i)
			.forEach(System.out::println);
	}
}
