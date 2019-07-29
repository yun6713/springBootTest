package com.bonc.config;

import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bonc.entity.jpa.User;
import com.bonc.repository.jpa.UserRepository;
import com.bonc.security.springSecurity.UserDetailsImpl;

/**
 * 根据DataSource、TransactionManager配置jpa EntityManagerFactory、EntityManager
 * @EnableJpaRepositories，区分多个jpa。
 * @EnableJpaAuditing；开启审计。auditorAwareRef提供用户，dateTimeProviderRef提供时间,有默认
 * 
 * @author litianlin
 * @date 2019年7月5日上午11:14:36
 * @Description TODO
 */
@Configuration
@EnableJpaRepositories(
		entityManagerFactoryRef="entityManagerFactoryPrimary",
		transactionManagerRef="primaryTransactionManager",
		basePackageClasses = { UserRepository.class },//默认本注解所在包
		repositoryImplementationPostfix="Impl" //自实现类后缀，默认Impl；接口、实现类必须放在扫描包下
	)
@EnableJpaAuditing//开启审计
@EnableTransactionManagement//开启注解驱动的transaction
public class JpaConfig {
	@Autowired
	DataSource dataSource;

	@Bean
	@Primary
	public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
		return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
	}

	@Autowired
	private JpaProperties jpaProperties;
//	springboot 2.x配置
	@Autowired
    private HibernateProperties hibernateProperties;
	private Map<String, Object> getVendorProperties() {
        return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
    }
//	springboot 1.5配置
//	private Map<String, String> getVendorProperties(DataSource dataSource) {	        
//		return jpaProperties.getHibernateProperties(dataSource);	    
//	}
	@Primary
	@Bean(name = "entityManagerFactoryPrimary")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(dataSource)
				.properties(getVendorProperties())
				.packages(User.class) // 设置实体类所在位置
				.persistenceUnit("primaryPersistenceUnit")
				.build();
	}

	// 事务配置
	@Primary
	@Bean(name = "primaryTransactionManager")
	public PlatformTransactionManager primaryTransactionManager(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(entityManagerFactoryPrimary(builder).getObject());
	}
	/**
	 * 返回获取用户的实例类；泛型类型与@CreatedBy、@LastModifiedBy一致
	 * 本例基于spring security
	 * @return
	 */
	@Bean
	public AuditorAware<String> auditorAware(){
		return new AuditorAware<String>(){

			@Override
			public Optional<String> getCurrentAuditor() {
				return Optional.of(((UserDetailsImpl)SecurityContextHolder.getContext()
						.getAuthentication()
						.getPrincipal())
						.getUsername());
			}
			
		};
	}
}
