package com.bonc.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
}
class D<T>{
	
}
class A{
	
}
class B extends A{
	
}
class C extends B{
	
}
