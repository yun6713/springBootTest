package com.bonc.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
	private static final String DEFAULT_JAVA=FileUtils.getJavaPath(JpaClassBuilder.class,false)+"%1$s.java";
	//默认当前包
	String packageName="com.bonc";
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
		SqlProvider sp=null;
		if(driver.contains(".h2.")){
			sp=new H2Provider();
		}else if(driver.contains(".mysql.")){
			sp=new MysqlProvider();
		}else if(driver.contains(".oracle.")){
			sp=new OracleProvider();
		}else{
			throw new RuntimeException("不支持的数据库，驱动器："+driver);
		}
		Class.forName(driver);
		try(
				Connection conn = DriverManager.getConnection(url,username,password);
//				ResultSet rs = conn.createStatement().executeQuery("show columns from user");
				ResultSet cols = conn.createStatement().executeQuery(sp.getColumnInfoSql("user"));
				ResultSet ids = conn.createStatement().executeQuery(sp.getIdsSql("user"));
	    ){
			colField=sp.handleCols(cols);
			idCol=sp.handleIds(ids);
		}
	}
	//构建类
	public void generateClass() throws IOException {
		if(isBlank(tableName))
			throw new RuntimeException("tableName不能为空");
		if(isBlank(className)) {
			className=StringUtils.toUpperCaseByReg(tableName.trim().toLowerCase(), "(_[a-z])([a-z]*)").replaceAll("_", "");
			className=StringUtils.toUpperCaseByReg(className, "(\\w)(\\w*)");
		}
		if(isBlank(packageName)) {
			packageName=JpaClassBuilder.class.getPackage().getName();
		}
		StringBuilder varBuilder=new StringBuilder();
		StringBuilder mtdBuilder=new StringBuilder();
		Set<String> idSet=new HashSet<>();
		if(idCol==null||idCol.isEmpty()||isBlank(idCol.get(0))) {
			varBuilder.append(Constant.idInfo);
		}else{
			idSet.addAll(idCol);
		}
		colField.forEach((k,v)->{
			if(isBlank(k)) return;
			if(idSet.contains(k)) {
				varBuilder.append(Constant.idInfo);
			}
			v=v.trim();
			if(isBlank(v)||v.indexOf(',')==0) {
				v=StringUtils.toUpperCaseByReg(k.trim().toLowerCase(), "(_[a-z])([a-z]*)").replaceAll("_", "")+v;
			}
			varBuilder.append(varStr(k,v));
			mtdBuilder.append(methodStr(v));
		});
		String content=String.format(Constant.classTemplate, 
				packageName,tableName,className,varBuilder.toString()+mtdBuilder.toString());
		System.out.println(content);
		String path=FileUtils.getJavaPath(packageName, false);
		if(new File(path).exists()){
			FileUtils.string2File(content, path.endsWith(File.separator)?path+className+".java":path+File.separator+className+".java");
		}else{
			FileUtils.string2File(content, String.format(DEFAULT_JAVA, className));
		}
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
		String[] strs=parse(field);
		return getTypeAnnotation(strs[0])+String.format(Constant.varTemplate,col,getActualType(strs[0]),strs[1],strs[2]);
	}
	/**
	 * 按逗号分隔，解析出变量类型、变量名、变量描述
	 * @param str
	 * @return [type,varName]
	 */
	private String[] parse(String str) {
		String[] strs = str.split(",");
		String type,varName,comment="";
		if(strs.length>1) {	
			varName=strs[0];		
			type=strs[1];
			comment=strs.length>2?strs[2]:"";
		}else {
			type="String";
			varName=str;
		}
		return new String[] {type,varName,comment};
	}
	//拼接特殊注解
	private String getTypeAnnotation(String type){
		String temp="";
		switch(type){
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
		default:
			return temp;
		}
		return Constant.indent+temp;
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
		String upperVarName=StringUtils.toUpperCaseByReg(strs[1], "(\\w)(\\w*)");
		return String.format(Constant.methodTemplate,getActualType(strs[0]),strs[1],upperVarName);
	}
	public static class Constant{
		public static final String indent="\t";
		public static final String classTemplate=
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
				indent+"private static final long serialVersionUID = 1L;\n" + 
				"%4$s\n"
				+ "}\n";
		public static final String idInfo=indent+"@Id\n"
						+ indent+"//@GeneratedValue(strategy=GenerationType.SEQUENCE)\n";
		public static final String varTemplate=indent+"@Column(name=\"%1$s\")//%4$s\n"
						+ indent+"private %2$s %3$s;\n";
		public static final String methodTemplate=
				indent+"public %1$s get%3$s() {\n" + 
						indent+indent+"return %2$s;\n" + 
						indent+"}\n" + 
						indent+"public void set%3$s(%1$s %2$s) {\n" + 
						indent+indent+"this.%2$s = %2$s;\n" + 
						indent+"}\n";
	}
	/**
	 * 提供查询主键、表列的sql
	 * @author Administrator
	 *
	 */
	public static interface SqlProvider{
		/**
		 * sql返回字段COL、TYPE、COMMENT
		 * @param tableName
		 * @return
		 */
		String getIdsSql(String tableName);
		/**
		 * sql返回字段COL、ISPRI(0,1)
		 * @param tableName
		 * @return
		 */
		String getColumnInfoSql(String tableName);
		/**
		 * 处理ids结果集，返回主键列表
		 * @param cols
		 * @return
		 * @throws Exception
		 */
		default List<String> handleIds(ResultSet ids) throws SQLException {
			List<String> idCol=new ArrayList<String>();
			while(ids.next()){
				String isPri=ids.getString("ISPRI");
				if(isPri!=null&&Integer.valueOf(isPri)==1){
					idCol.add(ids.getString("COL"));
				}
			}
			return idCol;
		}
		/**
		 * 处理cols结果集，返回列类构建参数
		 * @param cols
		 * @return
		 * @throws Exception
		 */
		default Map<String, String> handleCols(ResultSet cols) throws Exception{
			Map<String,String> colField=new HashMap<String,String>();
			while(cols.next()){
				String colName=cols.getString("COL");
				String type=cols.getString("TYPE");
				String comment=Optional.ofNullable(cols.getString("COMMENT")).orElse("");
				colField.put(colName, ","+db2Java(type)+","+comment);
			}
			return colField;
		}

		//数据库类型to java类型
		default String db2Java(String dbType){
			String javaType="String";
			if(dbType==null) return javaType;
			dbType=dbType.trim().toUpperCase().split("[\\s\\(]")[0];
			switch(dbType){
			case "CHAR":
				return "char";
			case "BIT":
				return "boolean";
			case "TINYINT":
				return "Byte";
			case "SMALLINT":
				return "Short";
			case "INTEGER":
				return "Integer";
			case "BIGINT":
				return ",Long";
			case "FLOAT":
				return "Float";
			case "DOUBLE":
				return "Double";
			case "NUMERIC":
				return "BigDecimal";
			case "DATE":
				return "DATE";
			case "TIME":
				return "TIME";//后期转Date
			case "TIMESTAMP":
				return "TIMESTAMP";//后期转Date
			case "BLOB":
				return "BLOB";//后期转byte[]
			case "CLOB":
				return "CLOB";//后期转String
			default:
				return javaType;
			}
		}
	}
	public static class MysqlProvider implements SqlProvider{
		private static final String IDS_TMP="select column_name COL,case column_key when 'PRI' then 1 else 0 end ISPRI "+
				"from information_schema.columns where table_name='%1$s'";
		private static final String COL_TMP="select  column_name COL,column_type TYPE,column_comment COMMENT "+
				"from information_schema.columns where table_name='%1$s'";
		
		@Override
		public String getIdsSql(String tableName) {
			return String.format(IDS_TMP, tableName);
		}
		@Override
		public String getColumnInfoSql(String tableName) {
			return String.format(COL_TMP, tableName);
		}
		
	}
	public static class H2Provider implements SqlProvider{
		private static final String IDS_TMP="select column_name COL,1 ISPRI "+
				"from  INFORMATION_SCHEMA.KEY_COLUMN_USAGE where table_name='%1$s'";
		private static final String COL_TMP="select  column_name COL,type_name TYPE,'' COMMENT "+
				"from information_schema.columns where table_name='%1$s'";
		
		@Override
		public String getIdsSql(String tableName) {
			return String.format(IDS_TMP, tableName.toUpperCase());
		}
		@Override
		public String getColumnInfoSql(String tableName) {
			return String.format(COL_TMP, tableName.toUpperCase());
		}
		
	}
	public static class OracleProvider implements SqlProvider{
		private static final String IDS_TMP="select cu.column_name COL ,case cu.constraint_name when 'PK_%1$s' then 1 else 0 end ISPRI from user_cons_columns cu, "
				+ "user_constraints au where cu.constraint_name = au.constraint_name and au.constraint_type = 'P' and au.table_name = '%1$s' ";
		private static final String COL_TMP="select t.COLUMN_NAME COL,t.data_type TYPE,c.COMMENTS COMMENT from user_tab_columns t,user_col_comments c "
				+ "where t.table_name = c.table_name and t.column_name = c.column_name and t.table_name = '%1$s'";
		
		@Override
		public String getIdsSql(String tableName) {
			return String.format(IDS_TMP, tableName.toUpperCase());
		}
		@Override
		public String getColumnInfoSql(String tableName) {
			return String.format(COL_TMP, tableName.toUpperCase());
		}
		
	}
}
