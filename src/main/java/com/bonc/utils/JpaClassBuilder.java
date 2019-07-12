package com.bonc.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;
/**
 * 自动构建jpa entity类；关联关系、主键策略需自行修改
 * 必填项：tableName，colField
 * 通过String.format格式化模板实现。
 * @author litianlin
 * @date   2019年7月12日下午3:28:22
 * @Description TODO
 */
public class JpaClassBuilder {
	String packageName="";
	String tableName="oauth_client_details";
	//默认转换表名
	String className="";
	String idCol="";//id列，默认第一个属性
	/*
	 * col为key，field为value；
	 * 默认类型为String，自定义类型：id,Integer
	 */
	Map<String,String> colField=MapUtils
			.builder("client_id", "")
			.put("resource_ids", "")
			.put("client_secret", "")
			.put("scope", "")
			.put("authorized_grant_types", "")
			.put("web_server_redirect_uri", "")
			.put("authorities", "")
			.put("access_token_validity", ",Integer")
			.put("refresh_token_validity", ",Integer")
			.put("additional_information", "")
			.put("autoapprove", "")
			.put("", "")
			.build();
	@Test
	public void generateClass() {
		if(isBlank(tableName))
			throw new RuntimeException("tableName不能为空");
		if(isBlank(className)) {
			className=toCamelName(tableName.trim().toLowerCase(), "(_[a-z])([a-z]*)").replaceAll("_", "");
			className=toCamelName(className, "(\\w)(\\w*)");
		}
		StringBuilder varBuilder=new StringBuilder();
		StringBuilder mtdBuilder=new StringBuilder();
		if(isBlank(idCol)) {
			varBuilder.append(idInfo);
		}
		colField.forEach((k,v)->{
			if(isBlank(k)) return;
			if(idCol.equals(k)) {
				varBuilder.append(idInfo);
			}
			v=v.trim();
			if(isBlank(v)||v.indexOf(',')==0) {
				v=toCamelName(k.trim().toLowerCase(), "(_[a-z])([a-z]*)").replaceAll("_", "")+v;
			}
			varBuilder.append(varStr(k,v));
			mtdBuilder.append(methodStr(v));
		});
		System.out.println(String.format(classTemplate, 
				packageName,tableName,className,varBuilder.toString()+mtdBuilder.toString()));
	}
	private boolean isBlank(String str) {
		return str==null||str.trim().isEmpty();
	}
	private String varStr(String col,String field) {
		String[] strs=parse(field);
		return String.format(varTemplate,col,strs[0],strs[1]);
	}
	private String methodStr(String str) {
		String[] strs=parse(str);
		//首字母大写
		String upperVarName=toCamelName(strs[1], "(\\w)(\\w*)");
		return String.format(methodTemplate,strs[0],strs[1],upperVarName);
	}
	private String[] parse(String str) {
		int loc = str.indexOf(",");
		String type,varName;
		if(loc!=-1) {
			type=str.substring(loc+1);
			varName=str.substring(0, loc);
		}else {
			type="String";
			varName=str;
		}
		return new String[] {type,varName};
	}
	/**
	 * @param str
	 * @param reg 转大写的正则表达式，第一个捕获组转大写，默认([a-z])([a-z]*)
	 * @return
	 */
	private String toCamelName(String str,String reg) {
		if(reg==null||isBlank(reg)) {
			reg="([a-z])([a-z]*)";
		}
		//转为驼峰命名法
		StringBuffer stringbf = new StringBuffer();
		Matcher m = Pattern.compile(reg, Pattern.CASE_INSENSITIVE).matcher(str);
		while (m.find()) {
            m.appendReplacement(stringbf, m.group(1).toUpperCase() + m.group(2));
        }
		return m.appendTail(stringbf).toString();
	}
	String classTemplate=
	"package %1$s;\n" + 
	"\n" + 
	"import java.io.Serializable;\r\n" + 
	"import javax.persistence.Column;\r\n" + 
	"import javax.persistence.Entity;\r\n" + 
	"import javax.persistence.FetchType;\r\n" + 
	"import javax.persistence.GeneratedValue;\r\n" + 
	"import javax.persistence.GenerationType;\r\n" + 
	"import javax.persistence.Id;\r\n" + 
	"import javax.persistence.JoinColumn;\r\n" + 
	"import javax.persistence.JoinTable;\r\n" + 
	"import javax.persistence.ManyToMany;\r\n" + 
	"import javax.persistence.Table;\n"+
	"//建议重写hashCode，equals方法\n"+
	"@Entity\n" + 
	"@Table(name=\"%2$s\")\n"+
	"public class %3$s implements Serializable{\n" + 
	"	private static final long serialVersionUID = 1L;\n" + 
	"	%4$s\n"
	+ "    }\n";
	String idInfo="@Id\n"
			+ "//@GeneratedValue(strategy=GenerationType.SEQUENCE)\n";
	String varTemplate="@Column(name=\"%1$s\")\n"
			+ "private %2$s %3$s;\n";
	String methodTemplate=
			"   public %1$s get%3$s() {\n" + 
			"		return %2$s;\n" + 
			"	}\n" + 
			"	public void set%3$s(%1$s %2$s) {\n" + 
			"		this.%2$s = %2$s;\n" + 
			"	}\n";
}
