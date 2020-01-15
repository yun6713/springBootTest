package com.bonc.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;

public class SqlConcat {
	
	public static void main(String[] args) {
		printInfo();
//		formatSql();
	}
/**
 * 打印规律重复内容
 */
	public static void printInfo(){
//		<if test="kpis.size()>3">nvl(kpi.RANK4,0) RANK6,nvl(kpi.SCORE4,0) VAL6,</if>
//变量为AAA，BBB，CCC；便于后续替换。
//		String template="<if test=\"kpis.size()>AAA\">nvl(kpi.RANKBBB,0) RANKCCC,nvl(kpi.SCOREBBB,0) VALCCC,</if>";
		String template="<when test=\"order=='RANKAAA'\"> RANKAAA </when> ";
		int start=2,
			end =14;
		System.out.println();
		for (int i = start; i <= end; i++) {			
			System.out.println(template.replaceAll("AAA", ""+i)
					.replaceAll("BBB", ""+(i+1))
					.replaceAll("CCC", ""+(i+2)));
		}
	}
/**
 * 获取Http，发送请求；获取解析后SQL
 * https://blog.csdn.net/a360316515/article/details/77272128
 * @return
 */ 
	private static final HttpClient HTTP=HttpClientBuilder.create().build();
	private static final String PATH="https://www.w3cschool.cn/statics/demosource/tools/toolsAjax.php?action=sql_formatter";
	private static final String SQL_REG="^\\w{4,6}\\s[\\w\\W]+$";
	public static void formatSql(){
		try {
			File src=new File("sqlLog"),
				tgt=new File("formatSql");
			FileUtils.string2File("", tgt);//清空
//			读取文件，判定是否为SQL
			String[] infos=FileUtils.file2String(src).split("\n");
			for (int i = 0; i < infos.length; i++) {
				Object result;
				String info=infos[i].trim();
//				是SQL
				if(info.matches(SQL_REG)){
					HttpPost request=getPostRequest(info);
					HttpResponse response=HTTP.execute(request);
					String data=getResponseData(response);
//					System.out.println(data);
					result=JSON.parseObject(data, Map.class).get("result");
					System.out.println(i);
				}else{//非SQL直接输出
					result=info;
				}
//				System.out.println(result);
				FileUtils.string2File(result.toString(), tgt, true);
				FileUtils.string2File("\n", tgt, true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private static HttpPost getPostRequest(String sql) throws UnsupportedEncodingException{
		HttpPost post=new HttpPost(PATH);
		//创建一个提交数据的容器
        List<BasicNameValuePair> parames = new ArrayList<>();

        parames.add(new BasicNameValuePair("query", sql));

        //封装容器到请求参数中
        HttpEntity entity = new UrlEncodedFormEntity(parames);
        //设置请求参数到post请求中
        post.setEntity(entity);
		return post;
	}
	private static String getResponseData(HttpResponse response) throws UnsupportedOperationException, IOException{
		StringBuilder builder=new StringBuilder();
		if(response.getStatusLine().getStatusCode()==200){
			HttpEntity entity=response.getEntity();
			InputStreamReader isr=new InputStreamReader(entity.getContent());
			char[] chars=new char[1024*4];
			int len;
			while((len=isr.read(chars))!=-1){
				builder.append(chars, 0, len);
			}
			isr.close();
		}
		return builder.toString();
	}
}
