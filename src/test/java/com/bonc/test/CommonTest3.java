package com.bonc.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.bonc.entity.jpa.Permission;
import com.bonc.utils.FileUtils;
import com.bonc.utils.StringUtils;

import cn.hutool.core.io.resource.ClassPathResource;


public class CommonTest3 {
	@Test
	/**
	 * 上海项目提取变量，
	 * @throws IOException
	 */
	public void shArgs() throws IOException {
		Set<String> args=new HashSet<>();
		String content = FileUtils.file2String("classpath:test2");
		
		System.out.println(content);
	}
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
		new CommonTest3().testGen("VisitShopPositionsSelector");
//		System.out.println("abc".indexOf("c",1));
//		System.out.println("abc".substring(0, 3));
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
	@Test
	public void testxml() throws DocumentException {
		Document d=new SAXReader().read(new ClassPathResource("MobileVisitShopMapper.xml").getFile());
		List<Element> list=d.getRootElement().elements();
		list.stream().map(e->{
			String info="select".equals(e.getName())?e.attributeValue("resultClass","HashMap"):"void";
			info= info.substring(info.lastIndexOf(".")+1)+" "+e.attributeValue("id");
			return String.format("%1$s (Map<String, Object> params);", info);
		}).forEach(System.out::println);
	}
//	@Test
	public void testGen(String name) {
		String a=(String) new HashMap().get("a");
//		System.out.println(a);
//		String name="getRenwuTree";
		String tmp="	@ApiOperation(value = \"\", notes = \"{'token':'','productId':'产品Id','isLocal':'0--本地，1---跨域'}\")\r\n" + 
				"	@ApiImplicitParam(name = \"jsonParam\", value = \"JSON\", required = true, dataType = \"String\")\r\n" + 
				"	@RequestMapping(value = \"/%1$s\", method = RequestMethod.POST)\r\n" + 
				"	public String  %1$s(@RequestParam String jsonParam){\r\n" + 
				"		return vs.%1$s(jsonParam);\r\n" + 
				"	}";
		System.out.println(String.format(tmp, name));
	}
	@Test
	public void cloneTest() throws CloneNotSupportedException {
		Permission p=new Permission();
		p.setpId("1");
		p.setpName("add");
		Permission p1=p.clone();
		System.out.println(p==p1);
		String str=JSON.toJSONString(p);
		Permission p2=JSON.parseObject(str, Permission.class);
		System.out.println(p2.getpName()==p.getpName());
	}
}
