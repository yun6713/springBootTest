package com.bonc.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.ResourceUtils;

/**
 * 常用工具方法
 * @author litianlin
 * @date   2019年7月17日上午9:05:44
 * @Description TODO
 */
public class CommonUtils {
	/**
	 * 加载yml文件
	 * @param rootName OriginTrackedMapPropertySource的key，不可为空串
	 * @param loc
	 * @return yml文件使用"---"隔开为多个yml时，返回多个元素的List
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<PropertySource<?>> loadYml(String rootName,String loc) throws FileNotFoundException, IOException {
		return new YamlPropertySourceLoader()
				.load(rootName,FileUtils.getResource(loc));
	}
	/**
	 * 判定obj是否为null，或空串、空集合
	 * @param obj
	 * @return
	 */
//	public static boolean isNullOrEmpty(Object obj) {
//		
//		return false;
//	}
}
