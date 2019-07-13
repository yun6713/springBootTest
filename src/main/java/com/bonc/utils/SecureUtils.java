package com.bonc.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 加密解密工具类，MD5、BCryptPasswordEncoder、BASE64/BASE64URL
 * spring DigestUtils工具类
 * @author Administrator
 *
 */
public class SecureUtils {
	private static final PasswordEncoder PE = new BCryptPasswordEncoder();
	private static MessageDigest MD5;
	private static final String DEFAULT_CHARSET="UTF-8";
	static {
		try {
			MD5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
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
	public static String base64Encode(String str,boolean isUrl) throws UnsupportedEncodingException{
		return isUrl?Base64.getUrlEncoder().encodeToString(str.getBytes(DEFAULT_CHARSET))
				:Base64.getEncoder().encodeToString(str.getBytes(DEFAULT_CHARSET));
	}
	public String base64Decode(String str,boolean isUrl) throws UnsupportedEncodingException{
		return isUrl?new String(Base64.getUrlDecoder().decode(str),DEFAULT_CHARSET)
				:new String(Base64.getDecoder().decode(str),DEFAULT_CHARSET);
	}
	/**
	 * 信息摘要，MD5
	 * @param str
	 * @param salt
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] md5Encode2Bytes(String str,String salt) throws UnsupportedEncodingException{
		str=salt!=null?str+salt:str;
		MD5.reset();
		MD5.update(str.getBytes(DEFAULT_CHARSET));		
		return MD5.digest();
	}
	public static String md5Encode2HexStr(String str,String salt) throws UnsupportedEncodingException{
		return bytes2String(md5Encode2Bytes(str,salt));
	}
	/**
	 * byte[]转为16进制字符串
	 * @param bytes
	 * @return
	 */
	public static String bytes2String(byte[] bytes){
		StringBuffer builder = new StringBuffer();
		//把数组每一字节换成16进制连成字符串
		for (byte b:bytes) {
			builder.append(Integer.toHexString((0x000000FF & b) | 0xFFFFFF00).substring(6));
		}
		return builder.toString().toUpperCase();
	}
}
