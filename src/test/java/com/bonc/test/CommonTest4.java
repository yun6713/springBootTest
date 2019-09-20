package com.bonc.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.util.StringUtils;

import com.alibaba.druid.pool.DruidDataSource;
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
	public static int size=30;
	public static ExecutorService es=Executors.newFixedThreadPool(size);
//	public static CountDownLatch cdl=new CountDownLatch(size);
//	public static CyclicBarrier cb=new CyclicBarrier(size);
	public static Semaphore sph=new Semaphore(size);
	@Test
	public void testFileTree() throws IOException, InterruptedException {
		FileUtils.walkFileTree("C:\\Users\\Administrator\\Desktop\\src", (file)->{
			try {
				sph.acquire();
				es.execute(()->{
					try {
						copy(file);
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
	public void testFiles() {
	}
}
