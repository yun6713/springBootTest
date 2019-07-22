package com.bonc.test;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.List;

import org.junit.Test;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;

import com.alibaba.fastjson.JSON;
import com.bonc.utils.RsaUtils;

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
	@Test
	public void tsetRsa() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		String str = "hello world";
		RsaUtils rs=new RsaUtils();
		rs.setPUB_KEY_FILE("classpath:rsaPub");
		rs.setPRI_KEY_FILE("classpath:rsaPri");
		String enc=RsaUtils.encode(str);
		System.out.println(enc);
		System.out.println(RsaUtils.decode(enc));
	}
	@Test
	public void generateRsaKey() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(512);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
		System.out.println(new String(Base64.getEncoder().encode(rsaPublicKey.getEncoded()))+"$$$");
		System.out.println(new String(Base64.getEncoder().encode(rsaPrivateKey.getEncoded()))+"$$$");
	}
}
