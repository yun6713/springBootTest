package com.bonc.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.pool.DruidDataSource;
import com.bonc.constant.DocType;
import com.bonc.service.DocService;

@RestController
@RequestMapping("/doc")
public class DocController {
	@Autowired
	DataSource ds1;
	@Autowired
	DocService ds;
	@RequestMapping("/add")
	public String addDoc(String path) {
		ds.add(path);
		return "ok";
	}
//	获取所有文档类型
	@RequestMapping("/getTypes")
	public Object getTypes() {	
		System.out.println(((DruidDataSource)ds1).getConnectProperties());
		DocType[] types=DocType.values();
		List l=Arrays.asList(types)
				.stream()
				.filter(type->!DocType.DIR.equals(type))
				.collect(Collectors.toList());
		return Arrays.asList(types)
				.stream()
				.filter(type->!DocType.DIR.equals(type))
				.collect(Collectors.toList());
	}
//	检索文档
	@RequestMapping("/searchByContent")
	public Object searchByContent(String content) {		
		return ds.searchByContent(content);
	}
	
	
}
