package com.bonc.test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bonc.utils.FileUtils;

public class JsoupTest2 {
	public static final String ADDR="http://code.dongnaoedu.com:9999/?non_archived=true&page=%1$s&sort=latest_activity_desc";
	public static final String TMP="F:/KwDownload/test/%1$s.txt";
	public static final String TMP1="F:/KwDownload/test1/%1$s.txt";
	public static final String TMP2="http://e1.zxhgqgp.xyz/pw/";
	public static final String TMP3="http://e1.zxhgqgp.xyz/pw/thread.php?fid=17&page=";
	public static final Logger LOG=LoggerFactory.getLogger("");
	public static final int size=60,num=5;
	public static final ExecutorService ES=Executors.newFixedThreadPool(size);
	public static Semaphore sph1=new Semaphore(size-num),
			sph2=new Semaphore(num);
	public static void main(String[] args) throws Exception {
		int start=1;
		int size=20;
		for(int i=start;i<start+size;i++){
			int page=i;
			sph2.acquire();
			ES.execute(()->{
				try {
					test02(String.format(ADDR, page));
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					sph2.release();
				}
			});
		}
		ES.shutdown();
		ES.awaitTermination(120, TimeUnit.SECONDS);
		System.out.println("end");
//		testCopy();
	}
	public static void test02(String page) throws Exception{
		try {
			LOG.info("page:{}",page);
			Document doc=Jsoup.connect(TMP3+page).get();
			List<Element>eles=doc.select("span project-name");
			for(Element ele:eles){
				System.out.println(ele.text());
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	@Test
	public void testHtml() throws Exception{
		try {
			String content=FileUtils.file2String("classpath:html");
			Document doc=Jsoup.parse(content);
			List<Element>eles=doc.select("span .project-name");
			for(Element ele:eles){
				System.out.print("\""+ele.text()+"\",");
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	public static void test01(String path) throws Exception{
		try{
			System.out.println(path);
			String dic1="F:\\KwDownload\\test1\\";
			String dic="F:\\KwDownload\\test\\";
			Document doc=Jsoup.connect(TMP2+path).get();
			String title=doc.title();
			title=title.split("\\|")[0];
			if(title.contains("爸爸")||title.contains("妈妈")||title.contains("母"))
				return;
			LOG.info("title:{}",title);
			String src=String.format("%1$s%2$s.txt", dic1,"a"),
					dst=String.format("%1$s%2$s.txt", dic,title);
			if(new File(dst).exists()){
				return;
			}
			Files.copy(Paths.get(src), Paths.get(dst));
			String content=doc.getElementById("read_tpc").text();
			content=content.replaceAll("。\\s+", "  \n").replaceAll("\\s{2,}", "  \n");
			content=title+"\n"+content;
			FileUtils.string2File(content, String.format(TMP, title),true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Test
	public void test() throws Exception{
		String a="　　";
		test01("html_data/17/1909/4312488.html");
	}
}
