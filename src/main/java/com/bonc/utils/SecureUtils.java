package com.bonc.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 加密解密工具类，MD5、BCryptPasswordEncoder、BASE64/BASE64URL、DES
 * spring DigestUtils工具类
 * @author Administrator
 *
 */
public class SecureUtils {
	private static final PasswordEncoder PE = new BCryptPasswordEncoder();
	private static MessageDigest MD5;
	private static final Charset DEFAULT_CHARSET=Charset.defaultCharset();
	private static final String DES="DES";
	private static final Logger LOG = LoggerFactory.getLogger(SecureUtils.class);
	static {
		try {
			MD5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	//加密测试
	public static void main(String[] args) throws Exception{
		String key ="helloworld?";
		String salt = "helloworld1";
		byte[] encrypt = desEncrypt(key.getBytes(), salt.getBytes());
		LOG.info(bytes2HexString(encrypt));
		LOG.info(base64Encode(encrypt, false));
		String str = new String(Base64.getEncoder().encode(encrypt),DEFAULT_CHARSET);
		LOG.info(str);
		LOG.info(str.length()+"");
		byte[] b=Base64.getDecoder().decode(str.getBytes(DEFAULT_CHARSET));
		LOG.info(new String(desDecrypt(b,salt.getBytes())));
	}
	/**
	 * 密码加密
	 * @param pwd
	 * @return
	 */
	public static String peEncode(String pwd){
		return PE.encode(pwd);
	}
	public static boolean peMatches(String rawPwd,String encodedPwd){
		return PE.matches(rawPwd, encodedPwd);
	}
	/**
	 * 使用UTF-8字符集
	 * @param str
	 * @param isUrl
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String base64Encode(String str,boolean isUrl){
		return base64Encode(str.getBytes(DEFAULT_CHARSET),isUrl);
	}
	public static String base64Encode(byte[] data,boolean isUrl){
		return isUrl?Base64.getUrlEncoder().encodeToString(data)
				:Base64.getEncoder().encodeToString(data);
	}
	public static String base64Decode(String str,boolean isUrl){
		return base64Decode(str.getBytes(DEFAULT_CHARSET),isUrl);
	}
	public static String base64Decode(byte[] data,boolean isUrl){
		return isUrl?new String(Base64.getUrlDecoder().decode(data),DEFAULT_CHARSET)
				:new String(Base64.getDecoder().decode(data),DEFAULT_CHARSET);
	}
	/**
	 * 信息摘要，MD5
	 * @param str
	 * @param salt
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] md5Encode2Bytes(String str,String salt){
		str=salt!=null?str+salt:str;
		MD5.reset();
		MD5.update(str.getBytes(DEFAULT_CHARSET));		
		return MD5.digest();
	}
	public static String md5Encode2HexStr(String str,String salt){
		return bytes2HexString(md5Encode2Bytes(str,salt));
	}
	public static boolean md5Martch(String raw,String pwd,String salt) {
		Objects.requireNonNull(raw, "raw can't null");
		Objects.requireNonNull(pwd, "pwd can't null");
		return raw.equals(bytes2HexString(md5Encode2Bytes(pwd,salt)));
	}
	/**
	 * byte[]转为16进制字符串
	 * @param bytes
	 * @return
	 */
	public static String bytes2HexString(byte[] bytes){
		StringBuffer builder = new StringBuffer();
		//把数组每一字节换成16进制连成字符串
		for (byte b:bytes) {
			builder.append(Integer.toHexString((0x000000FF & b) | 0xFFFFFF00).substring(6));
		}
		return builder.toString().toUpperCase();
	}
	//DES加密解密；data--key，key--salt
	public static byte[] desEncrypt(byte[] data,byte[] key) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		return desEncrypt(data,key,true);
	}
	public static byte[] desDecrypt(byte[] data,byte[] key) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		return desEncrypt(data,key,false);
	}
	private static byte[] desEncrypt(byte[] data,byte[] key,boolean isEncrypt) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		//get securekey
		SecretKey securekey = SecretKeyFactory.getInstance(DES).generateSecret(new DESKeySpec(key));
		Cipher cipher = Cipher.getInstance(DES);
        //根据isEncrypt初始化
        cipher.init(isEncrypt?Cipher.ENCRYPT_MODE:Cipher.DECRYPT_MODE, securekey, new SecureRandom());
        return cipher.doFinal(data);
	}
}
