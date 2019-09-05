package com.bonc.designPattern.struct.proxy;
/**
 * 静态代理
 * @author litianlin
 * @date   2019年9月4日下午2:50:56
 * @Description TODO
 */
public class StaticProxy {
	public static void main(String[] args) {
		System.out.println(
				new YinShuiJi(new WaterBucket()).getWater());
	}
	public static interface WaterContainer{
		public String getWater();
	}
	public static class WaterBucket implements WaterContainer{

		@Override
		public String getWater() {
			return "WaterBucket";
		}
		
	}
	public static class YinShuiJi implements WaterContainer{
		WaterBucket wb;//封装实际执行者，执行时加以控制
		public YinShuiJi(WaterBucket wb) {
			this.wb=wb;
		}

		@Override
		public String getWater() {
			String str=wb.getWater();
			return "Handle water from "+str;
		}
		
	}
}
