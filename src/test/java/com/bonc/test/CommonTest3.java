package com.bonc.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.bonc.utils.FileUtils;
import com.bonc.utils.StringUtils;


public class CommonTest3 {
	@Test
	public void findList() throws Exception {
		List<String> list = new ArrayList<>();
		list.add("aaa");
		list.add("aaav");
		list.add("aaag");
		System.out.println(list.toString());
		Object a="1";
		System.out.println(a.equals("1"));
	}
	@Test
	public void findArgs() throws Exception {
		String content=FileUtils.file2String("classpath:test2");
		String pattern="params\\.get\\(\\s*\"\\s*([\\w]+)";
		Set<String> result=StringUtils.getAllStrs(content,pattern,1);
		System.out.println(result);
	}
	public static void main(String[] args) throws Exception {
		new CommonTest3().findArgs();
		System.out.println("abc".indexOf("c",1));
		System.out.println("abc".substring(0, 3));
	}
	@Test
	public void test01() throws Exception {
		String path="E:\\项目资料\\陕西经分\\sql4es使用说明文档.docx";
//		String content=FileUtils.file2String("E:\\项目资料\\陕西经分\\sql4es使用说明文档.docx");
//		content = content.replaceAll("\r\n", "<p>\r\n")+"<p>";
		System.out.println(FileUtils.readPoi(new File(path)));
	}
	@Test
	public void testProcess() throws Exception {
		String command="netstat -ano|findstr /r /c:\"9998 *\\[\"";
		Process process=Runtime.getRuntime().exec(new String[]{"cmd.exe","/c",command});
		byte[] b=new byte[8*1024];
		process.getInputStream().read(b);
		String str=new String(b);
		String[] strs=str.split("\r\n");
		String result="";
		for (int i = 0; i < strs.length; i++) {
			String[] temp=strs[i].split("\\s+");
			if(!temp[temp.length-1].equals("0")){
				result=temp[temp.length-1];
				break;
			}
		}
		System.out.println(result);
	}
	@Test
	public void testClass() {
		System.out.println(Object.class.isInstance(CommonTest3.class));
		System.out.println(Arrays.toString(new Class[] {CommonTest3.class,CommonTest3.class}));
		
	}
}
