package com.stt.conf;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * MyBatis 基础配置
 * Created by stt on 2017/9/30.
 */

@Configuration
@EnableTransactionManagement // 开启注解式事物的支持，等同于xml配置方式的 <tx:annotation-driven />
@MapperScan("com.stt.mapper") // 指定要扫描的mapper接口的路径
public class MyBatisConfig implements TransactionManagementConfigurer {

	private final Logger log = LoggerFactory.getLogger(MyBatisConfig.class);

	 //配置类型别名
	@Value("${mybatis.typeAliasesPackage}")
	private String typeAliasesPackage;

    //配置mapper的扫描，找到所有的mapper.xml映射文件
	@Value("${mybatis.mapperLocations}")
	private String mapperLocations;

	// 加载全局的配置文件
	@Value("${mybatis.configLocation}")
	private String configLocation;

	/*数据源*/
	// 关于多数据源问题，使用@Primary注解，表示默认使用哪个
	@Primary
	// 可以给bean设定名称
	@Bean(name = "defaultDatasource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	/*sqlSessionFactory*/
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws IOException {
		try {
			SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
			bean.setDataSource(dataSource());
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			// 配置解析xml的路径
			bean.setMapperLocations(resolver.getResources(mapperLocations));
			// 添加别名
			bean.setTypeAliasesPackage(typeAliasesPackage);
			// 添加全局配置文件
			bean.setConfigLocation(new DefaultResourceLoader().getResource(configLocation));
			// 添加分页插件
			bean.setPlugins(new Interceptor[] {pageHelper()});
			return bean.getObject();
		}catch(Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	/*事务管理*/
	// 配置事务01,默认事务 mybatis 使用
	// 也可以直接入参使用datasource，是直接注入的
	@Bean(name = "defaultTxManager")
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

//	// 创建事务管理器2 EntityManagerFactory 是直接注入的
//	@Bean(name = "txManager2")
//	public PlatformTransactionManager txManager2(EntityManagerFactory factory) {
//		return new JpaTransactionManager(factory);
//	}

	// 实现接口 TransactionManagementConfigurer 方法，
	// 其返回值代表在拥有多个事务管理器的情况下默认使用的事务管理器
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return transactionManager();
	}

	// 配置分页的插件
	@Bean
	public PageHelper pageHelper() {
		log.info("--config MyBatis plugin PageHelper");
		PageHelper pageHelper = new PageHelper();
		Properties p = new Properties();
		p.setProperty("offsetAsPageNum", "true");
		p.setProperty("rowBoundsWithCount", "true");
		p.setProperty("reasonable", "true");
		pageHelper.setProperties(p);
		return pageHelper;
	}

}

/*
	传统applicationContext.xml配置mybatis
	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
	 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	 xmlns:aop="http://www.springframework.org/schema/aop"
	 xmlns:context="http://www.springframework.org/schema/context"
	 xmlns:tx="http://www.springframework.org/schema/tx"
	 xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
	   http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.0.xsd
	   http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd
	   http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.0.xsd">

		 <context:component-scan base-package="com.stt.mybatis"/>

		 <context:property-placeholder location="classpath:db.properties"/>

		 <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		   <property name="user"     value="${jdbc.user}"/>
		   <property name="password"    value="${jdbc.password}"/>
		   <property name="jdbcUrl"   value="${jdbc.jdbcUrl}" />
		   <property name="driverClass"  value="${jdbc.driverClass}" />
		 </bean>

		 <!-- 配置mybatis的sqlSessionFactory -->
		 <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		   <property name="dataSource" ref="dataSource"/>
		   <property name="typeAliasesPackage" value="com.stt.mybatis.entities"/>
		   <property name="configLocation" value="classpath:mybatis-config.xml"/>
		 </bean>

		 <!-- 配置事务管理 -->
		 <bean id="transactionManager"
			class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		   <property name="dataSource" ref="dataSource"/>
		 </bean>

		 <tx:annotation-driven transaction-manager="transactionManager"/>

		 <!-- 配置Mapper所在的package，将mapper中的对象加入到ioc中 -->
		 <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
			 <property name="basePackage" value="com.stt.mybatis.mapper"/>
		 </bean>

	</beans>
*/