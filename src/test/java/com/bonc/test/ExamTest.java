package com.bonc.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import com.bonc.utils.FileUtils;

public class ExamTest {
	@Test
	public void test() {
		Date date = new Date(100000L);
		System.out.println(date);
		System.out.println(date.getMonth());
		new SimpleDateFormat().format(date);
		Object obj;
		System.out.println(Math.round(-15.6));
		Comparator c;
		String s;
	}
	@Test
	public void utilTest() throws IOException, URISyntaxException {
		boolean b=FileUtils.existsFile(ResourceUtils.getFile("classpath:shFiles/"), 
				file->{
					System.out.println(file.getName());
					try {
						return FileUtils.file2String(file).contains("hello world");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return false;
				});
		System.out.println(b);
	}
	@Test
	public void testRaf() throws FileNotFoundException, IOException {
//		File file = ResourceUtils.getFile("classpath:shFiles\\拜访table.txt");
//		File file = ResourceUtils.getFile("D:\\eclipse-workspace\\springbootTest\\target\\classes\\shFiles\\拜访table.txt");
		File file2 = new File("D:\\eclipse-workspace\\springbootTest\\src\\main\\resources\\shFiles\\拜访table.txt");
//		System.out.println(file.getAbsolutePath());
		System.out.println(file2.getAbsolutePath());
		String str = FileUtils.file2String(file2);
		try(
//				RandomAccessFile raf = new RandomAccessFile(file.getAbsolutePath(),"rw");
				RandomAccessFile raf2 = new RandomAccessFile(file2.getAbsolutePath(),"rw");
			){
//			raf.seek(file.length());
//			raf.writeUTF("12345");
			raf2.write("天天好心情，12345".getBytes());
		}
	}
	@Test
	public void testWriter() throws FileNotFoundException, IOException {
		File file = ResourceUtils.getFile("D:\\eclipse-workspace\\springbootTest\\src\\main\\resources\\shFiles\\拜访table.txt");
		System.out.println(new ClassPathResource("").getFile().getPath());
		try(
			FileWriter fw = new FileWriter(file);	
			){
			fw.write("lalala654");
			fw.flush();
		}
	}
	@Test
	public void testStream() throws FileNotFoundException, IOException {
//		File file = ResourceUtils.getFile("D:\\eclipse-workspace\\springbootTest\\src\\main\\resources\\shFiles\\拜访table.txt");
//		File file = ResourceUtils.getFile("classpath:shFiles\\拜访table.txt");
		File file = new File("D:\\eclipse-workspace\\springbootTest\\src\\main\\resources\\shFiles\\拜访table.txt");
		File file2 = new File(file.getAbsolutePath());
		System.out.println(file.getAbsolutePath());
		System.out.println(file.canWrite());
		file.setWritable(true);
		try(
			FileOutputStream fw = new FileOutputStream(file2);	
			){
			fw.write("lalala2".getBytes(StandardCharsets.UTF_8));
			fw.flush();
		}
	}
	@Test
	//获取当前类绝对路径
	public void testPath() {
		//项目根路径;jar包运行时，为启动运行启动命令的位置
		System.out.println(new File("").getAbsolutePath());
		//相对路径--class文件路径；绝对路径--classpath路径
		System.out.println(ExamTest.class.getResource(""));
		System.out.println(ExamTest.class.getResource("/"));
		//ClassLoader为相对classpath路径
		System.out.println(ClassLoader.getSystemResource(""));
		System.out.println(ExamTest.class.getClassLoader().getResource(""));
		System.out.println(ExamTest.class.getPackage().getName());
		System.out.println(FileUtils.getJavaPath(ExamTest.class,true));
	}
	@Test
	public void testPath2() throws IOException {
		ClassLoader cl = ExamTest.class.getClassLoader();
		//读
		System.out.println(ResourceUtils.getFile("classpath:es").getAbsolutePath());
		System.out.println(ExamTest.class.getResource("es"));
		//写
		FileUtils.string2File("天天好心情", "classpath:test");
	}
	@Test
	public void testResource() throws IOException {
//		Resource r=new UrlResource("file:/F:\\test.txt");
//		Resource r=new FileSystemResource("file:/F:\\test.txt");
		File r=FileUtils.getFile("classpath:es");
		System.out.println(r.length());
	}
	@Test
	public void testMath() throws IOException {
		System.out.println(Math.ceil(0.001));
		System.out.println(Math.ceil(128d/8000));
		System.out.println(Math.floor(-128d/8000));
		System.out.println(Math.round(3.2));
		System.out.println(Math.round(3.8));
	}
	public static void main(String[] args) throws IOException {
		System.out.println(FileUtils.file2String("classpath:es"));
		Resource r = FileUtils.getResource("classpath:data/data");
		//		new ExamTest().testResource();
	}
}
