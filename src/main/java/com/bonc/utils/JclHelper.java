package com.bonc.utils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 基于jcl；增加日志级别判定
 * @author litianlin
 * @date   2019年7月15日下午2:51:53
 * @Description TODO
 */
public class JclHelper {
	private static final Map<Class<?>,Log> LOG_MAP=new ConcurrentHashMap<>();
	static {
		LOG_MAP.put(JclHelper.class, LogFactory.getLog(JclHelper.class));
	}
	private static Log getLog(Class<?> clazz) {
		return getLog(clazz,null);
	}
	private static Log getLog(Class<?> clazz,Log defaultLog) {
		Objects.requireNonNull(clazz, "clazz can't be null");
		if(!LOG_MAP.containsKey(clazz)) {
			LOG_MAP.put(clazz, defaultLog==null?LogFactory.getLog(clazz):defaultLog);
		}
		return LOG_MAP.get(clazz);
	}
	public static void trace(Class<?> clazz,String msg) {
		trace(getLog(clazz),msg);
	}
	public static void trace(Log log,String msg) {
		if(log.isTraceEnabled()) {
			log.trace(msg);
		}
	}
	public static void debug(Class<?> clazz,String msg) {
		debug(getLog(clazz),msg);
	}
	public static void debug(Log log,String msg) {
		if(log.isDebugEnabled()) {
			log.debug(msg);
		}
	}
	public static void info(Class<?> clazz,String msg) {
		info(getLog(clazz),msg);
	}
	public static void info(Log log,String msg) {
		if(log.isInfoEnabled()) {
			log.info(msg);
		}
	}
	public static void warn(Class<?> clazz,String msg) {
		warn(getLog(clazz),msg);
	}
	public static void warn(Log log,String msg) {
		if(log.isWarnEnabled()) {
			log.warn(msg);
		}
	}
	public static void error(Class<?> clazz,String msg) {
		error(getLog(clazz),msg);
	}
	public static void error(Log log,String msg) {
		if(log.isErrorEnabled()) {
			log.error(msg);
		}
	}
	public static void fatal(Class<?> clazz,String msg) {
		fatal(getLog(clazz),msg);
	}
	public static void fatal(Log log,String msg) {
		if(log.isFatalEnabled()) {
			log.fatal(msg);
		}
	}
	
	
}
