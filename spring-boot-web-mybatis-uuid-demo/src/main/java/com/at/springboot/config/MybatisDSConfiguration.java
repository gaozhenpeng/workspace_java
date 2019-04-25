package com.at.springboot.config;

import java.sql.Driver;
import java.time.LocalDateTime;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
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
@MapperScan(basePackages = "com.at.springboot.mybatis.mapper")
@EnableTransactionManagement
public class MybatisDSConfiguration {
    @Value("#{'${app.datasource.driverClassName}'.trim()}")
    private String driverClassName;
    @Value("#{'${app.datasource.url}'.trim()}")
    private String url;
    @Value("#{'${app.datasource.username}'.trim()}")
    private String username;
    @Value("#{'${app.datasource.password}'.trim()}")
    private String password;
    
    @Bean
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


    @Bean
    public SqlSessionFactory sqlSessionFactory(ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());

        // Mybatis Config
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCallSettersOnNulls(true);
        // LocalDateTimeTypeHandler
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        typeHandlerRegistry.register(LocalDateTime.class, LocalDateTimeTypeHandler.class);
        
        sessionFactory.setConfiguration(configuration);
        
        // Mybatis Mapper XML
        Resource[] resourceArray = applicationContext.getResources("classpath:/mybatis/**/*Mapper.xml");
        sessionFactory.setMapperLocations(resourceArray);

        return sessionFactory.getObject();
    }
    
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {  
        return new DataSourceTransactionManager(dataSource());
    }  
    
    @Bean
    public TransactionInterceptor myTransactionInterceptor() {
        Properties props = new Properties();
        props.setProperty("get*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        props.setProperty("find*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        props.setProperty("query*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        props.setProperty("count*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        props.setProperty("list*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        props.setProperty("*", "PROPAGATION_REQUIRED,-Throwable");
        return new TransactionInterceptor(dataSourceTransactionManager(), props);
    }

    @Bean
    public AspectJExpressionPointcutAdvisor transactionAdvisor() {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setAdvice(myTransactionInterceptor());
        advisor.setExpression("(execution(* com.at.springboot.web.svr.*Service.*(..)))");
        return advisor;
    }
}
