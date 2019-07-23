package com.bonc.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

public class CommonTest2 {
	@Test
	public void testSdf() {
		Timestamp timestamp=new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSSSSSSSS");
		String dateid=sdf.format(timestamp);
		System.out.println(dateid);
		System.out.println("NDIM.DIM_GW_LOCATION".matches("(?i)(pm|PMDEV|pure|pmdss|NDIM)\\.\\w+"));
	}
	@Test
	/*
	 * 读取文件内容到字符串
	 * 按空格拆分为数组
	 * 匹配字符串开头是否为模式名，是则加入Set
	 */
	public void getTableName() throws Exception {
		String path = "E:\\项目资料\\上海划小\\pms-wap-inner\\src\\config\\mobile\\sqlmap-mobileChannelTourShop.xml";
		//忽略大小写
		String pattern="(?i)(pm|PMDEV|pure|pmdss|NDIM|dim|xdim|dmcode|dw|PM_USER|DSS_USER)\\.\\w+";
		Set<String> schemaSet = new HashSet<>();
		Set<String> tableSet = new HashSet<>();
		String info = file2String(path);
//		System.out.println(info.length());
		String[] strs = info.split("\\s+");
		List<String> list = new ArrayList<>();
		Collections.addAll(list, strs);
		Stream<String> s = list.stream();
		list.stream().forEach(str->{
			str = str.trim();
			int loc;
			if((loc=str.indexOf('.'))>0) {
				String temp = str.substring(0, loc);
				if(temp.matches("\\w{2,8}"))
					schemaSet.add(str.substring(0, loc));
			}
			if(str.matches(pattern)) {
				tableSet.add(str);
			}
		});
		System.out.println(schemaSet);
		tableSet.stream().forEach(System.out::println);
	}
	@Test
	/*
	 * 批量获取，去重
	 */
	public void getNamespaces() throws Exception {
		String info = file2String("classpath:files.txt").replaceAll("\\s+", "");
		//io读取时首位不可见
//		System.out.println(info.codePointAt(0));
		if(info.codePointAt(0)==65279)
			info=info.substring(1);
		String[] files = info.split(",");
		Set<String> namespaceSet = new HashSet<>();
		for(String file:files) {
			namespaceSet.addAll(getNamespace(file));
		}
		namespaceSet.forEach(System.out::println);
	}
	@Test
	public void getNamespace() throws Exception {
		String path = "dss\\web\\mobile\\actionSh\\building\\BuildingAction.java";
		path = "E:\\项目资料\\上海划小\\pms-wap-inner\\src\\com\\bonc\\"+path;
		//忽略大小写
		String pattern="daoHelper\\.[a-zA-Z]+\\(\\s*\"([\\w\\.]+)";
		Set<String> namespaceSet = new HashSet<>();
		String info = file2String(path);
		Matcher matcher = Pattern.compile(pattern).matcher(info);
		while(matcher.find()){
			String namespace = matcher.group();
			namespaceSet.add(namespace.substring(namespace.indexOf("\"")+1,namespace.lastIndexOf(".")));
		}
		namespaceSet.stream().forEach(System.out::println);
	}
	//传入地址后缀，获取namespaceSet
	public Set<String> getNamespace(String addr) throws Exception {
		String path = "E:\\项目资料\\上海划小\\pms-wap-inner\\src\\com\\bonc\\"+addr;
		//忽略大小写
		String pattern="daoHelper\\.[a-zA-Z]+\\(\\s*\"([\\w\\.]+)";
		Set<String> namespaceSet = new HashSet<>();
		String info = file2String(path);
		Matcher matcher = Pattern.compile(pattern).matcher(info);
		while(matcher.find()){
			String namespace = matcher.group();
			namespaceSet.add(namespace.substring(namespace.indexOf("\"")+1,namespace.lastIndexOf(".")));
		}
//		namespaceSet.stream().forEach(System.out::println);
		return namespaceSet;
	}
	public String file2String(String path) throws IOException {
		File file = ResourceUtils.getFile(path);
		StringBuffer sb = new StringBuffer();
		try(
				FileReader fr = new FileReader(file);
			){
			CharBuffer cb = CharBuffer.allocate(1000);
			while(fr.read(cb)!=-1) {
				cb.flip();
				sb.append(cb.toString());
				cb.clear();
			}
		}
		return sb.toString();
	}
}
