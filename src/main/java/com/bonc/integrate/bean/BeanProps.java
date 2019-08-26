package com.bonc.integrate.bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Priority;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bonc.service.H2Service;
@Component("beanTest")
@Lazy(false)//非懒加载，默认true
@Scope(value="singleton")
@Order(Ordered.LOWEST_PRECEDENCE)
@Priority(value = 0)
public class BeanProps implements InitializingBean,DisposableBean{
	@Autowired(required=false)//非必需，无时不报错
	H2Service h2Service;
	private static final Logger LOG=LoggerFactory.getLogger(BeanProps.class);
	@Override
	public void destroy() throws Exception {
		LOG.info("BeanProps destroy");
	}

	@PreDestroy
	public void destroy2() throws Exception {
		LOG.info("BeanProps destroy2");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LOG.info("BeanProps afterPropertiesSet");
	}

	@PostConstruct
	public void afterPropertiesSet2() throws Exception {
		LOG.info("BeanProps afterPropertiesSet2");
	}
	
}
