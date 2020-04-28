package com.bonc.test.spring;

import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.beans.VetoableChangeListener;
import java.net.MalformedURLException;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.constraints.Size;

import org.aspectj.lang.annotation.DeclareParents;
import org.junit.Test;
import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.EventListenerMethodProcessor;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.ServletContextResource;
import org.yaml.snakeyaml.Yaml;

import com.bonc.test.spring.aop.test1.AopTest;
import com.bonc.test.spring.aop.test2.AopTestConfig.AopTest2;

/**
 * IoC、AOP、SpEL、事件、资源、参数处理
 * @author litianlin
 * @date   2020年3月24日上午10:38:36
 * @Description TODO
 */
public class SpringCore {
	@Test
	public void ioc() {
		ApplicationContext container;
		WebApplicationContext wac;
		BeanFactory bf;
		//spring默认实现，包含bean容器
		DefaultListableBeanFactory dbf;
		BeanFactoryPostProcessor bfpp;
		BeanDefinition bd;
		
		BeanPostProcessor bpp;
		Scope s;
		Import im;
		ImportResource ir;
		DeclareParents dp;
	}
	@Test
	public void env() {
		Environment e;
		Profile p;
		ActiveProfiles ap;
		PropertyPlaceholderConfigurer ppc;
		new SpringApplication().setAdditionalProfiles("");
		System.getenv();//操作系统
		System.getProperties();//JVM
		Yaml y;
		YamlPropertySourceLoader ypsl;
		PropertySource ps;
		PropertySources pss;
		
	}
	@Test
	public void spel() {
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression("'1' == '12'");		
		Object val=exp.getValue();
		System.out.println("result:"+val);
	}
	public void resource() {
		Resource r;
		UrlResource ur;
		ClassPathResource cpr;
		FileSystemResource fsr;
		ServletContextResource scr;
		
	}
	public void event() {
		ApplicationEventPublisher aep;
		ApplicationListener al;
		ApplicationEvent ae;
		EventListener el;
		EnableAsync ea;
		EventListenerMethodProcessor elmp;
		
	}
	public void argHandler() {
		Validator v;
		ValidationUtils vu;
		Constraint c;
		ConstraintValidator vc;
		Size size;
		javax.validation.Valid valid;
		Validated v2;
		DataBinder db;
		BeanWrapper bw;
		BeanWrapperImpl bwi;
		PropertyEditor pe;
		PropertyEditorSupport pes;
		PropertyValue pv;
		PropertyChangeListener pcl;
		VetoableChangeListener vcl;
		NumberFormat nf;
	}
	@Test
	//测试BeanMetadataElement

	public void beanElement() {
		AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(TestBean.class);
		DefaultListableBeanFactory bf=(DefaultListableBeanFactory)(ac.getAutowireCapableBeanFactory());
		//bean元数据，
		BeanDefinition bd=bf.getBeanDefinition("nana");
		System.out.println(bd);
		System.out.println(TestBean.class.getClassLoader());
		BeanMetadataElement bde;
		bf.destroySingleton("nana");
	}
	@Test
	public void order() {
		@SuppressWarnings("resource")
		ApplicationContext ac=new AnnotationConfigApplicationContext(TestBeanConfig.class);
		DefaultListableBeanFactory bf=(DefaultListableBeanFactory)(ac.getAutowireCapableBeanFactory());
		bf.destroySingleton("nana");
		
	}
	@Test
	public void order2() {
		ApplicationContext ac=new AnnotationConfigApplicationContext(this.getClass().getPackage().getName());
		
	}

	@Test
	public void aopTest() {
		ApplicationContext ac=new AnnotationConfigApplicationContext("com.bonc.test.spring.aop");
		DefaultListableBeanFactory bf=(DefaultListableBeanFactory)(ac.getAutowireCapableBeanFactory());
		AopTest at=(AopTest) bf.getBean("aopTest");
		at.test();
		at.test2();
		System.out.println("================");
		AopTest2 at2=(AopTest2) bf.getBean("aopTest2");
		at2.test();
		at2.test2();
		
	}
	@Test
	public void testResource() throws MalformedURLException {
		FileSystemResource fsr=new FileSystemResource("E:/new_epm.sql");
		System.out.println(fsr.getFile().exists());
		fsr=new FileSystemResource("E:/new_epm.sql2");
		System.out.println(fsr.getFile().exists());
		fsr=new FileSystemResource("readme");
		System.out.println(fsr.getFile().exists());
		System.out.println(fsr.getFile().getAbsolutePath());
		UrlResource ur=new UrlResource("file:readme");
		System.out.println(ur.exists());
		ur=new UrlResource("file:///readme");
		System.out.println(ur.exists());
		ur=new UrlResource("file:D:/eclipse-workspace/springbootTest/readme");
		System.out.println(ur.exists());
		
	}
}
