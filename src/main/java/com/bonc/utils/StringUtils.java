package com.bonc.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 字符串工具类，方法：判空，转驼峰命名、获取指定捕获组的所有匹配
 * @author litianlin
 * @date   2019年7月30日下午1:00:55
 * @Description TODO
 */
public class StringUtils {

	public static boolean isBlank(String str){
		return str==null||str.trim().isEmpty();
	}
	/**
	 * 按正则表达式，将首个捕获组转大写；默认为([a-z])([a-z]*)
	 * @param str
	 * @param reg
	 * @return
	 */
	public static String toUpperCaseByReg(String str,String reg){
		if(reg==null||isBlank(reg)) {
			reg="([a-z])([\\w]*)";
		}
		return toUpperCaseByReg(str,reg,1);
	}
	public static String toUpperCaseByReg(String str,String reg,int loc){
		//转为驼峰命名法
		StringBuffer stringbf = new StringBuffer();
		Matcher m = Pattern.compile(reg).matcher(str);
		if(m.groupCount()<loc)
			throw new RuntimeException("捕获组数量小于指定位置："+loc);
		while (m.find()) {
			String temp=m.group().replace(m.group(1), m.group(1).toUpperCase());
            m.appendReplacement(stringbf, temp);
        }
		return m.appendTail(stringbf).toString();
	}
	/**
	 * 按正则表达式，返回匹配指定捕获组的所有字段
	 * @param str
	 * @param reg
	 * @param loc指定位置捕获组，从1开始
	 * @return
	 */
	public static Set<String> getAllStrs(String str,String reg,int loc){
		Matcher m = Pattern.compile(reg).matcher(str);
		if(m.groupCount()<loc)
			throw new RuntimeException("捕获组数量小于指定位置："+loc);
		Set<String> result = new HashSet<>();
		while (m.find()) {
			result.add(m.group(loc));
        }
		return result;
	}
}
