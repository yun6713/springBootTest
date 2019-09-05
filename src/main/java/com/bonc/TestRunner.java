package com.bonc;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

import com.bonc.integrate.bean.ConfigurationPropertiesTest;
/**
 * 初始化h2数据
 * @author litianlin
 * @date   2019年7月5日上午11:12:40
 * @Description TODO
 */
@Component
public class TestRunner implements ApplicationRunner{
	@Value("${l}")
	String test;
	ConfigurationProperties a;
	EnableConfigurationProperties a1;
	PostConstruct b;
	Validation c;
	Valid c1;//嵌套验证
	Validated c2;//标记类，验证bean；分组
	Validator d;//手动验证
	ValidationUtils d1;
	BeanPropertyBindingResult d2;
	@Autowired
	ConfigurationPropertiesTest cpt;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println(args);
	}
}
