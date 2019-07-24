package com.bonc.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.springframework.util.ResourceUtils;

public class CommonTest2 {
	private static final String FILE_PATTERN="classpath:shFiles/%1$s.txt";
	private static final String JAVA_PRE="E:\\项目资料\\上海划小\\pms-wap-inner\\src\\com\\bonc\\";
	private static final String XML_PRE="E:\\项目资料\\上海划小\\pms-wap-inner\\src\\config\\";
	private static String schemas_pattern="";
	@Test
	/*
	 * 批量获取，去重
	 */
	public void getNamespaces() throws Exception {
//		String info = file2String(String.format(FILE_PATTERN, "巡楼java"));
		String info = file2String(String.format(FILE_PATTERN, "巡店java"));
		String[] files = info.split(",");
		Set<String> namespaceSet = new HashSet<>();
		for(String file:files) {
			namespaceSet.addAll(getNamespace(file));
		}
		namespaceSet.forEach(System.out::println);
	}
	@Test
	public void getNamespace() throws Exception {
		String path = "dss\\web\\mobile\\actionSh\\PaymentDetailAction.java";
		getNamespace(path).stream().forEach(System.out::println);
	}
	//传入地址后缀，获取namespaceSet
	public Set<String> getNamespace(String path) throws Exception {
		Set<String> namespaceSet = new HashSet<>();
		if(path==null||"".equals(path.trim()))
			return namespaceSet;
		path = JAVA_PRE+path.trim();
		//忽略大小写
		String pattern="daoHelper\\.[a-zA-Z]+\\(\\s*\"([\\w\\.]+)";
		String info = file2String(path);
		Matcher matcher = Pattern.compile(pattern).matcher(info);
		while(matcher.find()){
			String namespace = matcher.group();
			namespaceSet.add(namespace.substring(namespace.indexOf("\"")+1,namespace.lastIndexOf(".")));
		}
//		namespaceSet.stream().forEach(System.out::println);
		return namespaceSet;
	}
	@Test
	//根据xml文件获取表名，通过正则匹配schema实现
	public void getTableNames(String fileName) throws Exception {
		String xmls = file2String(String.format(FILE_PATTERN, "巡楼xml"));
//		String xmls = file2String(String.format(FILE_PATTERN, "巡店xml"));
		schemas_pattern=getSchemas();
		Set<String> namespaceSet = new HashSet<>();
		for(String xml:xmls.split(",")) {
			namespaceSet.addAll(getTableName(xml));
		}
		List<String> list = new ArrayList<>(namespaceSet);
		Collections.sort(list);
		list.forEach(System.out::println);;
	}
	@Test
	/*
	 * 读取文件内容到字符串
	 * 按空格拆分为数组
	 * 匹配字符串开头是否为模式名，是则加入Set
	 */
	public void getTableName() throws Exception {
		String path = "mobile\\sqlmap-mobileChannelTourShop.xml";
		getTableName(path).stream().forEach(System.out::println);
	}
	public Set<String> getTableName(String path) throws Exception {
		Set<String> tableSet = new HashSet<>();
		if(path==null||"".equals(path.trim()))
			return tableSet;
		path = XML_PRE+path.trim();
		String info = file2String(path);
		String[] strs = info.split("\\s+");
		Arrays.asList(strs).stream()
			.forEach(str->{
				str = str.trim();
				//筛选tableName
				if(str.matches(schemas_pattern)) {
					tableSet.add(str.trim().toUpperCase());
				}
			});
		return tableSet;
	}
	//拼接schema模式
	public String getSchemas() throws Exception {
		String schemas = file2String(String.format(FILE_PATTERN, "schemas"));
		return "(?i)("+schemas.replaceAll("\\s+", "")+")\\.\\w+";
	}
	@Test
	//去重、排序后输出
	public void handleTableNames() throws Exception {
		String tables = file2String(String.format(FILE_PATTERN, "tables"));
		tables=tables.replaceAll("\\s+", "");
		System.out.println(tables);
		Set<String> tableSet = new HashSet<>();
		List<String> list = Arrays.asList(tables.split(","));
		tableSet.addAll(list);
		System.out.println(tableSet.size());
		
	}
	@Test
	//获取数据字典中的表描述，若无则为：无表信息
	public void getTableDesc() throws Exception {
		String info = file2String(String.format(FILE_PATTERN, "数据字典"));
		String[] strs = info.split("\\$2\\$");
		Map<String,String> dic = new HashMap<>();
		for(String str:strs) {
			String[] temp=str.split("\\$1\\$");
			if(temp.length==2) {
				dic.put(temp[0].trim().toUpperCase(), temp[1].trim());
			}else {
				dic.put(temp[0].trim().toUpperCase(), "无表信息");
			}
		}
//		String tables = file2String(String.format(FILE_PATTERN, "巡楼table"));
		String tables = file2String(String.format(FILE_PATTERN, "巡店table"));
		List<String> list = Arrays.asList(tables.replaceAll("\\s+", "").split(","));
		list.stream().forEach(l->System.out.println(dic.containsKey(l)?dic.get(l):"无表信息"));
//		dic.forEach((k,v)->System.out.println(k+"$1$"+v+"$2$"));
//		System.out.println(dic.size());
	}
	//读取文件内容，存入字符串
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
		String info = sb.toString();
		//io读取时首位不可见
//		System.out.println(info.codePointAt(0));
		if(info.codePointAt(0)==65279)//UTF-8-BOM模式
			info=info.substring(1);
		
		return info;
	}
}
