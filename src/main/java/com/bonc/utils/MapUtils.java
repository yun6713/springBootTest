package com.bonc.utils;

import java.util.HashMap;
import java.util.Map;


public class MapUtils {

	public static <K,V> MapBuilder<K,V> builder(K key,V value){
		return new MapBuilder<K,V>().put(key, value);
	}
	

	public static class  MapBuilder<K,V>{
		private Map<K,V> map=new HashMap<>();
		public MapBuilder<K,V> put(K key,V value){
			map.put(key, value);
			return this;
		}
		public Map<K,V> build(){
			return map;
		}
	}
}
