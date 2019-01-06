package com.at.springboot.config;

import java.sql.Driver;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * <p>It's not required to define the datasource explicitly if a single one is presented.
 * </p>
 * <p>Use <code>@Transactional</code> to mark transactional methods.
 * </p>
 * <p>
 * </p>
 */
@Configuration
@MapperScan(basePackages = "com.at.springboot.mybatis.mapper2", sqlSessionFactoryRef="sqlSessionFactory2")
@EnableTransactionManagement
public class MybatisTest2Configuration {
    @Value("#{'${app.datasource.test2.driverClassName}'.trim()}")
    private String driverClassName;
    @Value("#{'${app.datasource.test2.url}'.trim()}")
    private String url;
    @Value("#{'${app.datasource.test2.username}'.trim()}")
    private String username;
    @Value("#{'${app.datasource.test2.password}'.trim()}")
    private String password;
    
    @Bean("dataSource2")
    public DataSource dataSource(){
        try {
            SimpleDriverDataSource simpleDriverDataSource  = new SimpleDriverDataSource();
            Driver driver = (Driver)Class.forName(driverClassName).getDeclaredConstructor().newInstance();
            simpleDriverDataSource.setDriver(driver);
            simpleDriverDataSource.setUrl(url);
            simpleDriverDataSource.setUsername(username);
            simpleDriverDataSource.setPassword(password);
            return simpleDriverDataSource;
        } catch (Exception e) {
            throw new RuntimeException("Not able to initilaize the data source.", e);
        }
    }


    @Bean("sqlSessionFactory2")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());

        // Mybatis Config
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCallSettersOnNulls(true);
        sessionFactory.setConfiguration(configuration);
        
        return sessionFactory.getObject();
    }
    
    @Bean("dataSourceTransactionManager2")
    public DataSourceTransactionManager dataSourceTransactionManager() {  
        return new DataSourceTransactionManager(dataSource());
    }  
    
    @Bean("transactionInterceptor2")
    public TransactionInterceptor transactionInterceptor() {
        Properties props = new Properties();
        props.setProperty("get*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        props.setProperty("find*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        props.setProperty("query*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        props.setProperty("count*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        props.setProperty("list*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        props.setProperty("*", "PROPAGATION_REQUIRED,-Throwable");
        return new TransactionInterceptor(dataSourceTransactionManager(), props);
    }

    @Bean("transactionAdvisor2")
    public AspectJExpressionPointcutAdvisor transactionAdvisor() {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setAdvice(transactionInterceptor());
        advisor.setExpression("(execution(* com.at.springboot.web.svr2.*Service.*(..)))");
        return advisor;
    }
}
