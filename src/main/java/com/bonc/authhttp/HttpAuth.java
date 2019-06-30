package com.bonc.authhttp;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Optional;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * 基于sessionId的验证
 * @author Administrator
 *
 */
public class HttpAuth {
	//sessionId
	private static String sessionId;
	//获取到sessionId的时间
	private static Long authTime=0L;
	//sessionId有效时间
	private static long expireDuration=2*60*60L;
	private static AuthInfoFetcher fetcher;
	private static String getAuthInfo() throws Exception{
		if(System.currentTimeMillis()-authTime>expireDuration){
			synchronized (authTime) {
				if(System.currentTimeMillis()-authTime>expireDuration){
					//TODO 通过rest方式获取新的验证sessionId
					sessionId=fetcher.getAuthInfo();
					authTime=System.currentTimeMillis();
				}
			}
		}
		if(sessionId==null){
			throw new RuntimeException("sessionId is null.");
		}
		return sessionId;
	}
	public static HttpRequestBase addAuthInfo(HttpRequestBase req){
		try {
//			req.addHeader("Cookie", getAuthInfo(new HttpAuth.SpringSecurityAuthInfoFetcher("http://localhost:8080/login", "user", "user")));
			req.addHeader("Cookie", getAuthInfo());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("addAuthInfo occurs exception");
		}
		return req;
	}
	public static void setFetcher(AuthInfoFetcher fetcher){
		HttpAuth.fetcher=fetcher;
	}
	/**
	 * 通过rest方式获取新的验证sessionId
	 * @author Administrator
	 *
	 */
	public static interface AuthInfoFetcher{
		String getAuthInfo() throws Exception;
	}
	public static class SpringSecurityAuthInfoFetcher implements AuthInfoFetcher{
		private String authUrl;
		private String username;
		private String password;
		
		public SpringSecurityAuthInfoFetcher(String authUrl, String username, String password) {
			super();
			Objects.requireNonNull(authUrl, "authUrl can't be null");
			Objects.requireNonNull(username, "username can't be null");
			Objects.requireNonNull(password, "password can't be null");
			this.authUrl = authUrl;
			this.username = username;
			this.password = password;
		}

		@Override
		public String getAuthInfo() throws UnsupportedEncodingException {
			HttpPost post = new HttpPost(authUrl);
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			post.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			post.setEntity(new StringEntity(String.format("username=%1s&password=%2s", username,password)));
			try(
					CloseableHttpClient client = HttpClientBuilder.create().build();
					CloseableHttpResponse resp = client.execute(post);
				) {
//				HttpEntity entity = resp.getEntity();
//				String result = EntityUtils.toString(entity);
//				System.out.println(result);
				if(resp!=null&&resp.getStatusLine().getStatusCode()==302)
					return Optional.ofNullable(resp.getFirstHeader("Set-Cookie"))
							.map(Header::getValue)
							.map(o->o.split(";")[0])
							.orElse(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
