package com.bonc.utils;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
@Component
public class RsaUtils {
	private static PublicKey rsaPublicKey;
	private static PrivateKey rsaPrivateKey;
	private static final String RSA="RSA";
	@Value("${rsa.pub.file:classpath:rsaPub}")
	public void setPUB_KEY_FILE(String pUB_KEY_FILE) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		generate(pUB_KEY_FILE,true);
	}

	@Value("${rsa.pub.file:classpath:rsaPri}")
	public void setPRI_KEY_FILE(String pRI_KEY_FILE) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		generate(pRI_KEY_FILE,false);
	}
	/**
	 * 读取密钥信息
	 * @param fileName
	 * @param isPub
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static void generate(String fileName,boolean isPub) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException{
		byte[] bytes=FileUtils.file2String(fileName).getBytes(FileUtils.DEFAULT_CHARSET);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		if(isPub){
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(bytes));
			RsaUtils.rsaPublicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			
		}else{
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(bytes));
			RsaUtils.rsaPrivateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		}
		
	}
	public static String encode(String src) {
		try {
			// 初始化密钥
//			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//			keyPairGenerator.initialize(512);
//			KeyPair keyPair = keyPairGenerator.generateKeyPair();
//			rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
//			rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
//			// 私钥加密 公钥解密
//			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
//			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.ENCRYPT_MODE, rsaPrivateKey);
			byte[] resultBytes = cipher.doFinal(src.getBytes());

			// 私钥解密 公钥加密
			// X509EncodedKeySpec x509EncodedKeySpec =
			// new X509EncodedKeySpec(rsaPublicKey.getEncoded());
			// KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			// PublicKey publicKey =
			// keyFactory.generatePublic(x509EncodedKeySpec);
			// Cipher cipher = Cipher.getInstance("RSA");
			// cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// byte[] resultBytes = cipher.doFinal(src.getBytes());

			return Hex.encodeHexString(resultBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String decode(String src) {
		try {
			// 私钥加密 公钥解密
//			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
//			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.DECRYPT_MODE, RsaUtils.rsaPublicKey);
			byte[] resultBytes = cipher.doFinal(Hex.decodeHex(src.toCharArray()));

			// 私钥解密 公钥加密
			// PKCS8EncodedKeySpec pkcs8EncodedKeySpec
			// = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
			// KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			// PrivateKey privateKey =
			// keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			// Cipher cipher = Cipher.getInstance("RSA");
			// cipher.init(Cipher.DECRYPT_MODE, privateKey);
			// byte[] resultBytes =
			// cipher.doFinal(Hex.decodeHex(src.toCharArray()));

			return new String(resultBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
