package com.bonc.test;

import java.io.File;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.lang.ref.WeakReference;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import com.bonc.utils.DbUtils;
import com.bonc.utils.FileUtils;


public class CommonTest4 {
	@Test
	public void shArgs() throws SQLException, Exception {
		DbUtils.executeQuery(DbUtils.getConnection("jdbc:mysql://192.168.1.123:3306/demo?useUnicode=true&characterEncoding=UTF-8",
				"root", "boncmysql"), 
				"select * from camel_rel.camel_user union select * from camel.camel_user", rs->{
					try {
						while(rs.next()) {
							System.out.println(rs.getString("user_name"));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return null;
				});
	}
	@Test
	public void test1() throws Exception {
		
		System.out.println(new DruidDataSource() instanceof DataSource);
	}
	@Test
	public void testPkg() throws IOException {
		System.out.println(Package.getPackage("com.bonc.test")
				.getName());
		System.out.println(new File("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt").getName());
		System.out.println(FileUtils.file2String("C:\\Users\\Administrator\\Desktop\\src\\b - 副本 - 副本 - 副本 - 副本 - 副本 - 副本.txt"));
	}
	@Test
	public void testCopy() throws IOException, InterruptedException{
		String dic="C:\\Users\\Administrator\\Desktop\\";
		String src=String.format("%1$s%2$s.txt", dic,"a"),
				dst=String.format("%1$s%2$s.txt", dic,"b");
		String copy=String.format("copy \"%1$s\" \"%2$s\"", src,dst);
		String[] cmds= {"cmd.exe","/c",copy};
		Runtime.getRuntime().exec(cmds);
		File file=new File(dst);
		if(!file.exists()) {
			Thread.sleep(1000);
		}
		
		FileUtils.string2File("卡卡坚实的愤怒", file, true);
		System.out.println("success");
	}
	public void copy(File rawFile) throws IOException, InterruptedException{
		String dic="C:\\Users\\Administrator\\Desktop\\dst\\";
		String rawFileName=rawFile.getName().split("\\.")[0];
		String src=String.format("%1$s%2$s.txt", dic,"a"),//空白UTF-8-BOM txt。
				dst=String.format("%1$s%2$s.txt", dic,rawFileName);
//		String copy=String.format("copy \"%1$s\" \"%2$s\"", src,dst);
//		String[] cmds= {"cmd.exe","/c",copy};
//		Runtime.getRuntime().exec(cmds);
//		File dstFile=new File(dst);
//		if(!dstFile.exists()) {
//			dstFile.createNewFile();
//		}
		Files.copy(Paths.get(src), Paths.get(dst));
		FileUtils.string2File(FileUtils.file2String(rawFile), dst, true);
		System.out.println("Copy success: "+rawFileName);
	}
	public void edit(File rawFile) throws IOException, InterruptedException{
		String fileName=rawFile.getName();
		if(!fileName.endsWith("md"))
			return;
		String newContent=new StringBuilder("{% raw %}\n ")
				.append(FileUtils.file2String(rawFile))
				.append("\n{% endraw %}").toString();
		
		FileUtils.string2File(newContent, rawFile, false);
		System.out.println("edit success: "+fileName);
	}
	public static int size=30;
	public static ExecutorService es=Executors.newFixedThreadPool(size);
//	public static CountDownLatch cdl=new CountDownLatch(size);
//	public static CyclicBarrier cb=new CyclicBarrier(size);
	public static Semaphore sph=new Semaphore(size);
	@Test
	public void testFileTree() throws IOException, InterruptedException {
		FileUtils.walkFileTree("E:\\学习\\docker_practice", (file)->{
			try {
				sph.acquire();
				es.execute(()->{
					try {
//						copy(file);
						edit(file);
						sph.release();
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				});
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return FileVisitResult.CONTINUE;
		});
		es.shutdown();
		es.awaitTermination(10, TimeUnit.SECONDS);
		System.out.println("end");
	}
	@Test
	public void testRedisSlots() {
		String start="cluster addslots ";
		int size=16384,s1=64;
		int s2=size/s1;
		for (int i = 0; i < s1; i++) {
			StringBuffer sb=new StringBuffer(start);
			for (int j = 0; j < s2; j++) {
				sb.append(i*s2+j).append(" ");
			}
			System.out.println(sb.toString());
		}
	}
	@Test
	public void createAsciidoc() throws IOException {
		String dir="E:/Github/elasticsearch-definitive-guide";
		StringBuffer sb=new StringBuffer();
//		System.out.println(Paths.get(dir).toFile().exists());
		//获取文件夹数组
		File[] files=new File(dir).listFiles((f,name)->f.isDirectory());
		//遍历数组
		for (int i = 0; i < files.length; i++) {
			String name=files[i].getName();
			//不以数字开头则跳过
			if(!name.matches("^\\d+\\w+")) continue;
			FileUtils.walkFileTree(files[i].toPath(), (f)->{
				String fName=f.getName();
				if(fName.endsWith(".asciidoc")) {
					String content="include::"+name+"/"+fName+"[]";
					//拼接格式
					System.out.println(content);
					//存储结果
					sb.append(content).append("\n");
				}
				return FileVisitResult.CONTINUE;
			});
		}
		//写入文件
		FileUtils.string2File(sb.toString(), "f:/asciidocFile");
	}
	@Test
	public void testCollection() {
		List<String> list=new ArrayList<>();
		List raw=new ArrayList();
		list.addAll(raw);
		JSONObject jobj=JSONObject.parseObject("{\"lkk\":[1]}");
//		list.addAll(jobj.getJSONArray("lkk"));
		Objects.hashCode(list);
		String[] ltl=new String[] {"lkk","lkk2"};
//		list=Arrays.asList(ltl);
		list=new ArrayList<>(Arrays.asList(ltl));
		System.out.println(list);
		ltl[1]="forget";
		System.out.println(list);
	}
	@Test
	public void testCollection2() {
		List<Integer> list=new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		List<Integer> list2=list.subList(1, 4);
//		list.remove(1);
//		for (int i = 0; i < list2.size(); i++) {
//			System.out.println(list2.get(i));
//		}
		for(Integer i:list) {
			if(i==4) {
				list.remove(i);
			}
		}
		System.out.println("OK");
	}
	@Test
	public void testMap() {
		Map<String,String> map=new HashMap<>();
		map.put("ltl", "lkk");
		Set<String> keys=map.keySet();
		System.out.println(keys);
		keys.remove("ltl");
		System.out.println(map);
		map.put("forget", "lkk");
		System.out.println(keys);
	}
	@Test
	public void testReference() {
		Map<String,String> map=new HashMap<>();
		WeakReference<Map> r=new WeakReference<>(map);
		PhantomReference p=new PhantomReference(map,null);
		WeakHashMap<String,String> wh;
		ThreadLocal<String> t;
		Thread t3h;
	}
}
