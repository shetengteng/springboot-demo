package com.stt.ch04_myBatis.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * MyBatis 基础配置
 * Created by stt on 2017/9/30.
 */

@Configuration

// 开启注解式事物的支持，等同于xml配置方式的 <tx:annotation-driven />
@EnableTransactionManagement
@MapperScan("com.stt.ch04_mybatis.mapper")
@ComponentScan(basePackageClasses = {DruidConfig.class})
public class MyBatisConfig implements TransactionManagementConfigurer {

	@Autowired
	private DataSource dataSource;

	// 配置事务01,默认事务 mybatis 使用
	@Bean(name = "defaultTxManager")
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

	// 实现接口 TransactionManagementConfigurer 方法，
	// 其返回值代表在拥有多个事务管理器的情况下默认使用的事务管理器
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return null;
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