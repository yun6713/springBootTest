package com.bonc.designPattern.struct;
/**
 * 以对客户端透明的方式扩展对象的功能，是继承关系的一种替代。<br/>
 * 透明的装饰器模式，不改变调用方法；与被装饰对象实现同一接口。一般为半透明，即会增加方法。
 * @author litianlin
 * @date   2019年9月3日下午5:11:51
 * @Description TODO
 */
public class DecoratePattern {
	public static interface Drink{
		public String getDetail();
	}
	public static class Water implements Drink{

		@Override
		public String getDetail() {
			return "Water";
		}
		
	}
	//半透明，增加方法
	public static interface Add extends Drink{
		public String getInfo();
	}
	public static class Coffee implements Add{
		private Drink d;
		public Coffee(Drink d) {
			this.d=d;
		}
		@Override
		public String getDetail() {
			return d.getDetail()+"+Coffee,"+getInfo();
		}

		@Override
		public String getInfo() {
			return d instanceof Add?((Add)d).getInfo()+" Made In China"
					:" Made In China";
		}
		
	}
}
