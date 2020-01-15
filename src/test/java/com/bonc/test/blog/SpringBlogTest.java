package com.bonc.test.blog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBlogTest {
	@Autowired
	ApplicationContext ac;
	@Test
//	手动装配bean
	public void springCore(){
		DefaultListableBeanFactory acbf=(DefaultListableBeanFactory) ac.getAutowireCapableBeanFactory();
		acbf.autowire(TestBean1.class, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
		acbf.autowire(TestBean2.class, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
		System.out.println(acbf.getBean(TestBean2.class));
	}



	
	
}