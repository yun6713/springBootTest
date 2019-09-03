package com.bonc.designPattern.struct;

import java.util.Arrays;
import java.util.List;
/**
 * 接口适配，类适配，对象适配；调用的方法会改变。
 * @author litianlin
 * @date   2019年9月3日下午3:50:03
 * @Description TODO
 */
public class AdapterPattern {
	//原接口及实现类
	public static interface BasicInterface{
		public int translate(String str);
	}
	public static class Basic implements BasicInterface{
		public int translate(String str) {
			return Integer.valueOf(str);
		}
	}
	//新接口
	public static interface AdapterInterface{
		public List<Integer> translate(String str);
	}
	//适配器，对象适配
	public static class Adapter4Basic implements AdapterInterface{
		BasicInterface bi;
		public Adapter4Basic(BasicInterface bi) {
			this.bi=bi;
		}
		@Override
		public List<Integer> translate(String str) {
			return Arrays.asList(bi.translate(str));
		}
		
	}
}
