package com.bonc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bonc.service.SearchService;

@Controller
@RequestMapping("/search")
public class SearchController {
	@Autowired
	SearchService ss;
	@RequestMapping("/page")
	public String addDoc(String path) {
		return "search/page";
	}
//	获取所有文档类型
	@RequestMapping("/getTypes")
	@ResponseBody
	public Object getTypes() {	
		return ss.getTypes();
	}
//	检索文档
	@RequestMapping("/searchDoc")
	@ResponseBody
	public Object searchDoc(String content,String types) {		
		return ss.searchDoc(content,types);
	}
	
	
}
