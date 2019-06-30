package com.bonc.authhttp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
/**
 * 基于org.apatche.httpcomponents#httpclient
 * @author Administrator
 *
 */
public class HttpUtils {
	static{
		HttpAuth.setFetcher(new HttpAuth.SpringSecurityAuthInfoFetcher("http://localhost:8080/login", "user", "user"));
	}
	public static void get(String url){
		//builder方式构建
		HttpGet get = new HttpGet(url);
		HttpAuth.addAuthInfo(get);
		try(
				CloseableHttpClient client = HttpClientBuilder.create().build();
				CloseableHttpResponse resp = client.execute(get);
					
			) {
			HttpEntity entity = resp.getEntity();
			String result = EntityUtils.toString(entity);
			System.out.println(result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static class UrlBuilder{
		private final StringBuffer buffer;
		private boolean isFirstParam=true;
		public UrlBuilder(String loc){
			buffer = new StringBuffer(loc);
		}
		public UrlBuilder addParam(String key,String value){
			if(isFirstParam){
				buffer.append("?");
			}else{
				buffer.append("&");
			}
			buffer.append(key).append("=").append(value);
			return this;
		}
		public UrlBuilder addParam(NameValuePair param){
			return param==null?this:addParam(param.getName(),param.getValue());
		}
		public UrlBuilder addParamMap(Map<String,String> params){
			params.forEach(this::addParam);
			return this;
		}
		public UrlBuilder addParamList(List<NameValuePair> list){
			list.forEach(this::addParam);
			return this;
		}
		public UrlBuilder addParamString(String paramStr){
			paramStr=paramStr.trim();
			paramStr=paramStr.replaceFirst("^[\\?\\&]", "");
			if(isFirstParam){
				paramStr="?"+paramStr;
				isFirstParam=false;
			}else{
				paramStr="&"+paramStr;
			}
			buffer.append(paramStr);			
			return this;
		}
		public String build(){
			return buffer.toString();
		}
	}

	public static class ParamListBuilder{
		List<NameValuePair> params = new ArrayList<>();
		public ParamListBuilder addParam(String key,String value){
			params.add(new BasicNameValuePair(key,value));
			return this;
		}
		public ParamListBuilder addParamList(List<NameValuePair> list){
			params.addAll(list);
			return this;
		}
		public ParamListBuilder addParamMap(Map<String,String> params){
			params.forEach(this::addParam);
			return this;
		}
		public List<NameValuePair> build(){
			return params;
		}
	}
}
