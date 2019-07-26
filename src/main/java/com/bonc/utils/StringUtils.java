package com.bonc.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.ResourceUtils;

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
			reg="([a-z])([a-z]*)";
		}
		//转为驼峰命名法
		StringBuffer stringbf = new StringBuffer();
		Matcher m = Pattern.compile(reg, Pattern.CASE_INSENSITIVE).matcher(str);
		while (m.find()) {
            m.appendReplacement(stringbf, m.group(1).toUpperCase() + m.group(2));
        }
		return m.appendTail(stringbf).toString();
	}
}
