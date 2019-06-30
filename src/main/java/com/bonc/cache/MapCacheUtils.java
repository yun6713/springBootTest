package com.bonc.cache;

import java.util.concurrent.ConcurrentHashMap;
/**
 * 基于ConcurrentHashMap的缓存类。
 * @author Administrator
 *
 */
public class MapCacheUtils {
	private static final ConcurrentHashMap<Object,Object> CACHE_MAP = new ConcurrentHashMap<>(100);

	public static void cache(Object key,Object value){
		CACHE_MAP.put(key, value);
	}
	public static Object fetch(Object key){
		return fetch(key,Object.class);
	}
	@SuppressWarnings("unchecked")
	public static <T> T fetch(Object key, T clazz) {
		return (T)CACHE_MAP.get(key);
	}
	public static void remove(Object key){
		CACHE_MAP.remove(key);
	}
	public static void clear(){
		CACHE_MAP.clear();
	}
	public static boolean exist(Object key){
		return CACHE_MAP.containsKey(key);
	}
}
