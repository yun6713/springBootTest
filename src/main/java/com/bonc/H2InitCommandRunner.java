package com.bonc;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
/**
 * 初始化h2数据
 * @author litianlin
 * @date   2019年7月5日上午11:12:40
 * @Description TODO
 */
//@Component
public class H2InitCommandRunner implements CommandLineRunner{

	@Autowired
	ConversionService cs;
	private final static Logger LOG = LoggerFactory.getLogger(H2InitCommandRunner.class);
	private static String sqlTemplate = "insert into %1$s select %2$s from csvread('%3$s')";
	//获取表格列名
	private static String sqlTemplate2 = "select COLUMN_NAME from information_schema.COLUMNS where table_name='%1$s'";
	@Value("${h2.data-path:}")
	String dataPath;
	@Value("${h2.relation-path:}")
	String relationPath;
	@Value("${spring.jpa.hibernate.ddl-auto:}")
	String ddlAuto;
	@Autowired
	DataSource datasource;
	@Override
	public void run(String... args) throws Exception {
		//初始化数据
		ddlAuto=ddlAuto.trim();
		if("create".equals(ddlAuto)||"create-drop".equals(ddlAuto)) {
			initData();
		}
	}
	//根据配置路径，插入csv中数据
	private void initData() throws SQLException, FileNotFoundException {
		try(
				Statement stat = datasource.getConnection().createStatement();
				){
			initDataFromFile(ResourceUtils.getFile(dataPath),stat);
			initDataFromFile(ResourceUtils.getFile(relationPath),stat);
		}
		
	}
	//.csv文件插入，目录时迭代
	private void initDataFromFile(File file,Statement stat) throws SQLException {
		if(!file.exists()){
			throw new RuntimeException(file.getName()+" doesn't exist.");
		}
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f:files) {
				if(f.isDirectory()) {
					initDataFromFile(f,stat);
				}else{
					loadDataFromFile(f,stat);
				}
			}
		}else {
			loadDataFromFile(file,stat);
		}
		
	}
	private void loadDataFromFile(File file,Statement stat) throws SQLException {
		String fileName=file.getName();
		if(fileName.endsWith(".csv")){
			String tableName = fileName.substring(0, fileName.lastIndexOf('.'));
			//查询表列，防止csv、数据库列顺序不一致导致数据错乱
			String sql = String.format(sqlTemplate2, tableName.toUpperCase());
			if(LOG.isInfoEnabled())
				LOG.info(sql);
			try(
					ResultSet rs = stat.executeQuery(sql);
				){
				if(rs.next()) {
					StringBuffer columns=new StringBuffer(rs.getString(1));
					while(rs.next()) {
						columns.append(",").append(rs.getString(1));
					}
					//h2语法，插入csv中数据
					sql = String.format(sqlTemplate, tableName,columns.toString(),file.getAbsolutePath());
					if(LOG.isInfoEnabled())
						LOG.info(sql);
					stat.execute(sql);
				}
			}
		
		}
	}
}
