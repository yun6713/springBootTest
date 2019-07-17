package com.bonc.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

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

import com.mysql.cj.util.Base64Decoder;

/**
 * 加密解密工具类，MD5、BCryptPasswordEncoder、BASE64/BASE64URL、DES
 * spring DigestUtils工具类
 * @author Administrator
 *
 */
public class SecureUtils {
	private static final PasswordEncoder PE = new BCryptPasswordEncoder();
	private static MessageDigest MD5;
	private static final String DEFAULT_CHARSET="UTF-8";
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
		String str = base64Encode(encrypt,false);
		LOG.info(str);
		LOG.info(new String(desDecrypt(encrypt,salt.getBytes())));
		byte[] b=base64Decode(str,false).getBytes();
		LOG.info(new String(desDecrypt(base64Decode(str,false).getBytes(),salt.getBytes())));
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
	public static String base64Encode(byte[] data,boolean isUrl) throws UnsupportedEncodingException{
		return isUrl?Base64.getUrlEncoder().encodeToString(data)
				:Base64.getEncoder().encodeToString(data);
	}
	public static String base64Decode(String str,boolean isUrl) throws UnsupportedEncodingException{
		return isUrl?new String(Base64.getUrlDecoder().decode(str),DEFAULT_CHARSET)
				:new String(Base64.getDecoder().decode(str),DEFAULT_CHARSET);
	}
	public static String base64Decode(byte[] data,boolean isUrl) throws UnsupportedEncodingException{
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
	public static byte[] md5Encode2Bytes(String str,String salt) throws UnsupportedEncodingException{
		str=salt!=null?str+salt:str;
		MD5.reset();
		MD5.update(str.getBytes(DEFAULT_CHARSET));		
		return MD5.digest();
	}
	public static String md5Encode2HexStr(String str,String salt) throws UnsupportedEncodingException{
		return bytes2HexString(md5Encode2Bytes(str,salt));
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
