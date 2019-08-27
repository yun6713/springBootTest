package com.bonc.integrate.bean;

import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class SpringExtInterface implements InitializingBean{
	@Autowired
	ResourceLoader rl;
	Logger log=LoggerFactory.getLogger(SpringExtInterface.class);
	@Override
	public void afterPropertiesSet() throws Exception {
		try(
				//classpath资源
				InputStreamReader c=new InputStreamReader(rl.getResource("classpath:rsaPri").getInputStream());
				//url资源，有前缀且非classpath，均以url resource解析
				InputStreamReader f=new InputStreamReader(rl.getResource("file:/D:\\eclipse-workspace\\springbootTest\\src\\main\\java\\com\\bonc\\config\\DruidViewConfig.java").getInputStream());
				){
			char[] cs=new char[8*1024];
			c.read(cs);
			log.info(new String(cs));
			f.read(cs);
			log.info(new String(cs));
			log.info("SpringExtInterface inited.");
			
		}
	}
	
	
}
