package com.bonc.security.springSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * 配置安全策略HttpSecurity
 * 配置验证服务，内存、LDAP、UserDetailsService
 * 配置其他登录方式，
 * @author litianlin
 * @date   2019年7月9日上午8:50:29
 * @Description TODO
 */
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@EnableWebSecurity//启用安全配置
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled=true)//启用权限
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${spring.security.enabled:false}")
	String enable;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//禁用csrf
		http.csrf().disable();
		//放行h2
		http.headers().frameOptions().disable();
		//开启验证
		if(Boolean.valueOf(enable)) {
 			http.httpBasic()//开启http basic登录
				.and()//endpoint安全配置，要求必须为admin角色
				.requestMatcher(EndpointRequest.toAnyEndpoint())
				.authorizeRequests()
				.anyRequest().permitAll()//.hasRole("admin")
				.and()//controller安全配置
				.addFilterAt(filter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
				.antMatchers("/test","/h2console/*","/oauth/*","/druid/*")
				.permitAll()
				.anyRequest().authenticated()
				.and()//自动构建登录界面，允许所有访问
				.formLogin().permitAll().defaultSuccessUrl("/test2") //loginPage()用于指定自定义的多路页面路径
				.and()//登出，清除JSESSIONID
				.logout().permitAll().deleteCookies("JSESSIONID");
		}else {
			http.authorizeRequests().anyRequest().permitAll()
				.and().csrf().disable();
		}
	}
	@Bean
	public GlobalMethodSecurityConfiguration  globalMethodSecurityConfiguration(PermissionEvaluatorImpl pei) {
		return new GlobalMethodSecurityConfiguration() {

    	    protected MethodSecurityExpressionHandler createExpressionHandler() {
    	        System.out.println("MethodSecurityConfig.createExpressionHandler");
    	        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
    	        expressionHandler.setPermissionEvaluator(pei);
    	        return expressionHandler;
    	    }

    	};
	}
	/**
	 * 配置本地验证服务，UserDetailsService
	 * UserDetailsService返回查询结果
	 * UserDetails接口，返回权限；SimpleGrantedAuthority
	 */
	@Autowired
	UserDetailsService uds;
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(uds).passwordEncoder(passwordEncoder());
	}
	/*
	 * 托管AuthenticationManager
	 * (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#authenticationManagerBean()
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	/**
	 * 加密策略，托管给spring，便于保存用户时加密密码
	 * encode()，加密密码
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder(){
		//不加密，已过时
//		return NoOpPasswordEncoder.getInstance();
		return new BCryptPasswordEncoder();
	}
	/**
	 * 托管自定义登录filter
	 * @return
	 * @throws Exception 
	 */
	@Bean("myFilter")
	public AbstractAuthenticationProcessingFilter filter() throws Exception {
		AbstractAuthenticationProcessingFilter filter = new MyAuthenticationFilter();
		//从父类获取AuthenticationManager
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
		return filter;
	}
	@Bean
	public JwtAuthFilter jwtAuthFilter() {
		return new JwtAuthFilter();
	}
	/**
	 * 配置本地验证服务，jdbc
	 */
//	@Autowired
//	DataSource dataSource;
//	@Override
//	  public configure(AuthenticationManagerBuilder builder) {
//	    builder.jdbcAuthentication().dataSource(dataSource).withUser("dave")
//	      .password("secret").roles("USER");
//	  }
	
}
