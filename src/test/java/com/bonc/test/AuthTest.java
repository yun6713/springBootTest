package com.bonc.test;

import java.util.List;

import org.junit.Test;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;

import com.alibaba.fastjson.JSON;

public class AuthTest {
	@Test
	public void tsetOauth() {
		ClientCredentialsTokenEndpointFilter cctf;
		TokenEndpoint te;
		TokenGranter tg;
		ClientDetailsService cds;
	}
	@Test
	public void tsetJSON() {
		List<String> codes = JSON.parseArray("[51a,52]", String.class);
		System.out.println(codes);
	}
}
