package com.bonc.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.bonc.utils.CommonUtils;
import com.bonc.utils.FactoryBeanWrapper;
import com.bonc.utils.MapUtils;

public class CommonTest {
	@Test
	public void test01(){
		UsernamePasswordAuthenticationFilter u;
		System.out.println(Boolean.valueOf(""));
		Collection<? super B> list=new ArrayList<>();
		list.add(new C());
		D<? super B> d=new D<A>();
		FactoryBeanWrapper.Builder<? super B> b=new FactoryBeanWrapper.Builder<A>();
	}
	@Test
	public void testRestTemplate(){
		RestTemplate rt = new RestTemplate();
		Map<String,Object> map = new HashMap<>();
		map.put("a", "a");
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(MapUtils.toLinkHashMap(map),
				headers);
		String str = rt.postForObject("http://localhost:8080/test?b=b",httpEntity, String.class);
		System.out.println(str);
	}
	@Test
	public void testPasswordEncoder(){
		String pwd="123456";
		System.out.println(new BCryptPasswordEncoder().encode(pwd));
		System.out.println(new BCryptPasswordEncoder().matches(pwd, "$2a$10$LycPFgZnpC1PgRCnBJ/okOMZOOh4nv/J8yR6rM.qvULlSD9EkmvLm"));
	}
	@Test
	public void testStringFormart(){
		System.out.println(String.format("%1$s,%2$s,%1$s", "a","b"));
		Map<String,String> map = MapUtils.builder("a", "b").build();
		map.forEach((k,v)->v="c");
		System.out.println(map);
	}
	@Test
	public void testIntegerHex(){
		byte b=5;
		int a=b;
		System.out.println(Integer.toHexString((0x000000FF & b) | 0xFFFFFF00));
		System.out.println(a);
		System.out.println(Integer.toHexString(a));
		System.out.println("a"+null);
	}
	@Test
	public void testLoadYml() throws FileNotFoundException, IOException{
		Object obj = CommonUtils.loadYml("", "classpath:application.yml");
		System.out.println(obj);
	}
	
		
}
class D<T>{
	
}
class A{
	
}
class B extends A{
	
}
class C extends B{
	
}
