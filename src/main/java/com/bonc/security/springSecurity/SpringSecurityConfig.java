package com.bonc.security.springSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
/**
 * 配置安全策略HttpSecurity
 * 配置验证服务，内存、LDAP、UserDetailsService
 * @author litianlin
 * @date   2019年7月9日上午8:50:29
 * @Description TODO
 */
@EnableWebSecurity//启用安全配置
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${spring.security.enabled:false}")
	String enable;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//禁用csrf
//		http.csrf().disable();
//		http.headers().frameOptions().disable();
		//开启验证
		if(Boolean.valueOf(enable)) {
			http.authorizeRequests()
			.antMatchers("/test","/h2-console/*").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin().permitAll().defaultSuccessUrl("/test1") //loginPage()用于指定自定义的多路页面路径
			.and()
			.logout().permitAll();
		}else {
			http.authorizeRequests().anyRequest().permitAll();
		}
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
		auth.userDetailsService(uds).passwordEncoder(new BCryptPasswordEncoder());
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
