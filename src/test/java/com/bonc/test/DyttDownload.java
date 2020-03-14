package com.bonc.test;

import java.io.File;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.util.StringUtils;

public class DyttDownload {
	String PAGE_URL="https://www.dytt8.net/html/tv/hytv/20191016/59267.html",
			SELECTOR="#Zoom a",//元素选择器
			ATTR_NAME="href",//属性名
			PATTERN="";//属性值匹配，空串替换为[\\w\\W]*为匹配所有
//	url前缀，无时添加protocal
	final String urlHeader="^(http|https)://[\\w\\W]*",
			protocal="http://";
	@Test
//	通过jsoup获取页面节点
	public void getUrls() throws Exception {
		PAGE_URL=PAGE_URL.trim();
		PATTERN=StringUtils.hasLength(PATTERN)?PATTERN:"[\\w\\W]*";
//		判定本地文件，还是url；若为url，添加http://前缀
		File file=new File(PAGE_URL);
		Document doc;
		if(file.exists()) {
			System.out.println("Local File");
			doc=Jsoup.parse(file, null);
		}else {
			String pageUrl=Pattern.matches(urlHeader, PAGE_URL)?PAGE_URL
					:protocal+PAGE_URL;
			doc = Jsoup.connect(pageUrl).get();
		}
//		获取匹配元素
		Elements links = doc.select(SELECTOR);
		for(Element link:links) {
			getDownloadUrl(link);
		}
	}
	//下载链接字段名随机，遍历所有属性，获取下载链接thunder://开头
	private void getDownloadUrl(Element link) {
		if(StringUtils.hasLength(ATTR_NAME)) {
			String url=link.attr(ATTR_NAME);
			if(url!=null && Pattern.matches(PATTERN, url)) {
				System.out.println(url);
			}
		}else {
			traverse(link);
		}
	}
//	无法确定属性名时，遍历匹配
	private void traverse(Element link) {
		Attributes attrs=link.attributes();
		for(Attribute attr:attrs) {
			String url=attr.getValue();
			if(Pattern.matches(PATTERN, url)) {
				System.out.println(url);
			}
		}
	}
	@Test
	public void test() {
		String urlHeader="^(http|https)://[\\w\\W]*",
				PAGE_URL="https://www.dytt8.net/html/tv/hytv/20191016/59267.html";
		PAGE_URL=PAGE_URL.trim();
		System.out.println(Pattern.matches(urlHeader, PAGE_URL));
		System.out.println(Pattern.matches("^lk+","lkk"));
		System.out.println(Pattern.matches("^lk","lkk"));
		System.out.println(Pattern.matches("","lkk"));
	}
}
