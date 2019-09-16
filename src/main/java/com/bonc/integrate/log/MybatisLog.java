package com.bonc.integrate.log;

import org.apache.ibatis.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * mybatis Log实现类不可为内部类。
 * @author litianlin
 * @date   2019年9月6日上午10:29:32
 * @Description TODO
 */
public class MybatisLog implements Log{
	private static final Logger logger=LoggerFactory.getLogger(MybatisLog.class);
//	必须要有String类型构造器
	public MybatisLog(String clazz) {
	    // Do Nothing
	  }
	@Override
	public boolean isDebugEnabled() {
		return true;
	}

	@Override
	public boolean isTraceEnabled() {
		return true;
	}

	@Override
	public void error(String s, Throwable e) {
		logger.error(s,e);
		e.printStackTrace();
	}

	@Override
	public void error(String s) {
		logger.error(s);
		
	}

	@Override
	public void debug(String s) {
		logger.debug(s);
	}

	@Override
	public void trace(String s) {
		logger.trace(s);
	}

	@Override
	public void warn(String s) {
		logger.warn(s);
	}
	
}

