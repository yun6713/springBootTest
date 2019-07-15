package com.bonc.utils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于slf4j；增加日志级别判定
 * @author litianlin
 * @date   2019年7月15日下午2:51:53
 * @Description TODO
 */
public class Slf4jHelper {
	private static final Map<Class<?>,Logger> LOG_MAP=new ConcurrentHashMap<>();
	static {
		LOG_MAP.put(Slf4jHelper.class, LoggerFactory.getLogger(Slf4jHelper.class));
	}
	private static Logger getLogger(Class<?> clazz) {
		return getLogger(clazz,null);
	}
	private static Logger getLogger(Class<?> clazz,Logger defaultLog) {
		Objects.requireNonNull(clazz, "clazz can't be null");
		if(!LOG_MAP.containsKey(clazz)) {
			LOG_MAP.put(clazz, defaultLog==null?LoggerFactory.getLogger(clazz):defaultLog);
		}
		return LOG_MAP.get(clazz);
	}
	public static void trace(Class<?> clazz,String msg,Object... args) {
		trace(getLogger(clazz),msg,args);
	}
	public static void trace(Logger log,String msg,Object... args) {
		if(log.isTraceEnabled()) {
			log.trace(msg,args);
		}
	}
	public static void debug(Class<?> clazz,String msg,Object... args) {
		debug(getLogger(clazz),msg,args);
	}
	public static void debug(Logger log,String msg,Object... args) {
		if(log.isDebugEnabled()) {
			log.debug(msg,args);
		}
	}
	public static void info(Class<?> clazz,String msg,Object... args) {
		info(getLogger(clazz),msg,args);
	}
	public static void info(Logger log,String msg,Object... args) {
		if(log.isInfoEnabled()) {
			log.info(msg,args);
		}
	}
	public static void warn(Class<?> clazz,String msg,Object... args) {
		warn(getLogger(clazz),msg,args);
	}
	public static void warn(Logger log,String msg,Object... args) {
		if(log.isWarnEnabled()) {
			log.warn(msg,args);
		}
	}
	public static void error(Class<?> clazz,String msg,Object... args) {
		error(getLogger(clazz),msg,args);
	}
	public static void error(Logger log,String msg,Object... args) {
		if(log.isErrorEnabled()) {
			log.error(msg,args);
		}
	}
	
	
}
