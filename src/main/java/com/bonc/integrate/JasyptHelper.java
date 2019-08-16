package com.bonc.integrate;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class JasyptHelper {
	public static StringEncryptor stringEncryptor() {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword("helloworld");//盐
		config.setAlgorithm("PBEWithMD5AndDES");//加密方式
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setIvGeneratorClassName("org.jasypt.salt.NoOpIVGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);
		return encryptor;
	}

	public static void main(String[] args) {
		String info="b";
		StringEncryptor se=stringEncryptor();
		String str = se.encrypt(info);
		System.out.println("ENC("+str+")");
//		System.out.println(se.decrypt(str));
	}
}
