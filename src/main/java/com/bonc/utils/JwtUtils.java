package com.bonc.utils;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * jwt构建、加密、解析
 * @author litianlin
 * @date   2019年7月11日下午4:41:24
 * @Description TODO
 */
public class JwtUtils {
	//Secret密钥，用于签名加密
    private final static String SECRET = "auth_chm";
    //token有效期（分钟）
    private final static long VALIDATE_MINUTE = 30;
    //加密算法
    private final static Algorithm algorithm;
    
    static {
    	//设置签名加密算法、密码
        algorithm = Algorithm.HMAC256(SECRET);
    }

    /**
     * 根据用户信息生成token
     * @param authentication
     * @return
     */
    public static String generateToken(Authentication authentication) {
        //权限字符串，逗号分隔
    	String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    	//有效期
        Date now = Date.from(Instant.now());
        Date expiration = Date.from(ZonedDateTime.now().plusMinutes(VALIDATE_MINUTE).toInstant());

        //create jwt
        String jwt = JWT.create()
                .withClaim("authorities", authorities)
                .withSubject(authentication.getName())
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .sign(algorithm);
        return jwt;
    }

    /**
     * 认证token有效性
     * @param token
     * @return
     */
    public static boolean validateToken(String token) {
        if(token==null)
            return false;
        try {
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * 从token中解析中用户信息
     * @param token
     * @return
     */
    public static Authentication getAuthentication(String token) {

        DecodedJWT decodedJWT = JWT.decode(token);
        String authorityString = decodedJWT.getClaim("authorities").asString();

        Collection<? extends GrantedAuthority> authorities = Collections.emptyList();

        if(!StringUtils.isEmpty(authorityString)){
            authorities = Arrays.asList(authorityString.split(","))
                    .stream()
                    .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(),"",authorities);
        return authToken;
    }
}
