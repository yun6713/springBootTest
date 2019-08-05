package com.bonc.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.TemporalType;

import org.springframework.core.env.PropertySource;
/**
 * 自动构建jpa entity类；关联关系、主键策略需自行修改
 * 必填项：tableName，colField
 * 通过String.format格式化模板实现。
 * 生成的类位于当前包，刷新展示
 * @author litianlin
 * @date   2019年7月12日下午3:28:22
 * @Description TODO
 */
public class JpaClassBuilder {
	private static final String JAVA=FileUtils.getJavaPath(JpaClassBuilder.class,false)+"%1$s.java";
	//默认当前包
	String packageName="";
//	String tableName="oauth_client_details";
	String tableName="user";
	//默认转换表名
	String className="";
	List<String> idCol=Arrays.asList("");//id列，默认第一个属性
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
	
	public static void main(String[] args) throws Exception {
		JpaClassBuilder jcb=new JpaClassBuilder();
		try{//默认读取表结构构建entity
			jcb.getTableInfo();
		}finally{
			jcb.generateClass();
		}
	}
	//通过sql读取表结构，构建entity
	public void getTableInfo() throws Exception{
		tableName=tableName==null||"".equals(tableName.trim())?null:tableName;
		Objects.requireNonNull(tableName, "tableName 不可为null或空串");
		PropertySource<?> ps=CommonUtils.loadYml("spring", "classpath:application.yml").get(0);
		//按需更改
		String prefix="spring.datasource.first.";
	    String driver=ps.getProperty(prefix+"driver-class-name").toString();
		String url=ps.getProperty(prefix+"url").toString();
		String username=ps.getProperty(prefix+"username").toString();
		String password=ps.getProperty(prefix+"password").toString();
		String sql = null;
		int type;
		if(driver.contains("h2")||driver.contains("mysql")){
			sql="show columns from "+tableName;
			type=0;
		}else if(driver.contains("oracle")){
			sql=null;
			type=1;
		}else{
			throw new RuntimeException("不支持的数据库，驱动器："+driver);
		}
		if(sql==null) return;
		Class.forName(driver);
		try(
				Connection conn = DriverManager.getConnection(url,username,password);
				ResultSet rs = conn.createStatement().executeQuery("show columns from user");
	    ){
			handleRs(rs,type);
		}
	}
	//处理结果集，设置类构建参数
	private void handleRs(ResultSet rs,int flag) throws Exception{
		idCol=new ArrayList<String>();
		colField=new HashMap<String,String>();
		switch(flag){
		//h2、mysql；field、type、key、null、default。暂时只处理前三个。
		case 0:
			while(rs.next()){
				String field=rs.getString("FIELD");
				String type=rs.getString("TYPE");
				String key=rs.getString("KEY");
				colField.put(field, db2Java(type));
				if(key!=null&&"".equals(key)){
					idCol.add(key);
				}
			}
			break;
		default:
			break;
		}
	}
	//数据库类型to java类型
	private String db2Java(String dbType){
		String javaType="String";
		if(dbType==null) return javaType;
		if(dbType.indexOf('(')!=-1){
			dbType=dbType.substring(0, dbType.indexOf('('));
		}
		dbType=dbType.trim().toUpperCase();
		switch(dbType){
		case "CHAR":
			return ",char";
		case "BIT":
			return ",boolean";
		case "TINYINT":
			return ",Byte";
		case "SMALLINT":
			return ",Short";
		case "INTEGER":
			return ",Integer";
		case "BIGINT":
			return ",Long";
		case "FLOAT":
			return ",Float";
		case "DOUBLE":
			return ",Double";
		case "NUMERIC":
			return ",BigDecimal";
		case "DATE":
			return ",DATE";
		case "TIME":
			return ",TIME";//后期转Date
		case "TIMESTAMP":
			return ",TIMESTAMP";//后期转Date
		case "BLOB":
			return ",BLOB";//后期转byte[]
		case "CLOB":
			return ",CLOB";//后期转String
		default:
			return ","+javaType;
		}
	}
	//构建类
	public void generateClass() throws IOException {
		if(isBlank(tableName))
			throw new RuntimeException("tableName不能为空");
		if(isBlank(className)) {
			className=toCamelName(tableName.trim().toLowerCase(), "(_[a-z])([a-z]*)").replaceAll("_", "");
			className=toCamelName(className, "(\\w)(\\w*)");
		}
		if(isBlank(packageName)) {
			packageName=JpaClassBuilder.class.getPackage().getName();
		}
		StringBuilder varBuilder=new StringBuilder();
		StringBuilder mtdBuilder=new StringBuilder();
		Set<String> idSet=new HashSet<>();
		if(idCol==null||idCol.isEmpty()||isBlank(idCol.get(0))) {
			varBuilder.append(idInfo);
		}else{
			idSet.addAll(idCol);
		}
		colField.forEach((k,v)->{
			if(isBlank(k)) return;
			if(idSet.contains(k)) {
				varBuilder.append(idInfo);
			}
			v=v.trim();
			if(isBlank(v)||v.indexOf(',')==0) {
				v=toCamelName(k.trim().toLowerCase(), "(_[a-z])([a-z]*)").replaceAll("_", "")+v;
			}
			varBuilder.append(varStr(k,v));
			mtdBuilder.append(methodStr(v));
		});
		String content=String.format(classTemplate, 
				packageName,tableName,className,varBuilder.toString()+mtdBuilder.toString());
		System.out.println(content);
		FileUtils.string2File(content, String.format(JAVA, className));
	}
	//判定字符串是否为null，或者为空串、空白串
	private boolean isBlank(String str) {
		return str==null||str.trim().isEmpty();
	}
	/**
	 * 解析、声明变量
	 * @param col
	 * @param field
	 * @return
	 */
	private String varStr(String col,String field) {
		//拼接特殊注解
		String temp="";
		String[] strs=parse(field);
		switch(strs[0]){
		case"DATE":
			temp="@Temporal((TemporalType.DATE))\n";
			break;
		case"TIME":
			temp="@Temporal((TemporalType.TIME))\n";
			break;
		case"TIMESTAMP":
			temp="@Temporal((TemporalType.TIMESTAMP))\n";
			break;
		case"BLOB":
		case"CLOB":
			temp="@Lob\n";
			break;
		}
		return temp+String.format(varTemplate,col,getActualType(strs[0]),strs[1]);
	}
	private String getActualType(String type){
		switch(type){
		case"DATE":
		case"TIME":
		case"TIMESTAMP":
			return "Date";
		case"BLOB":
			return "byte[]";
		case"CLOB":
			return "String";
		default:
			return type;
		}
	}
	/**
	 * 按变量名、变量类型拼接getter/setter
	 * @param str
	 * @return
	 */
	private String methodStr(String str) {
		String[] strs=parse(str);
		//首字母大写
		String upperVarName=toCamelName(strs[1], "(\\w)(\\w*)");
		return String.format(methodTemplate,getActualType(strs[0]),strs[1],upperVarName);
	}
	/**
	 * 按逗号分隔，解析出变量名、变量类型
	 * @param str
	 * @return [type,varName]
	 */
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
	"import javax.persistence.Temporal;\n"+
	"import javax.persistence.TemporalType;\n"+
	"import java.util.Date;\n"+
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
