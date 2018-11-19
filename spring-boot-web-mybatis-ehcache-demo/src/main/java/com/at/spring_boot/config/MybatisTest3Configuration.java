package com.at.spring_boot.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

/**
 * <p>It's not required to define the datasource explicitly if a single one is presented.
 * </p>
 * <p>Use <code>@Transactional</code> to mark transactional methods.
 * </p>
 * <p>
 * </p>
 */
@Configuration
@MapperScan(basePackages = "com.at.spring_boot.mybatis.mapper3", sqlSessionFactoryRef="sqlSessionFactory3")
@EnableTransactionManagement
public class MybatisTest3Configuration {
    @Primary
    @Bean("dataSource3")
    @ConfigurationProperties(prefix = "app.datasource.test3") // spring-boot-configuration-processor
    public DataSource dataSource() {
        return new HikariDataSource();
    }

    @Primary
    @Bean("sqlSessionFactory3")
    public SqlSessionFactory sqlSessionFactory(ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());

        // Mybatis Config
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCallSettersOnNulls(true);
        sessionFactory.setConfiguration(configuration);

        // Mybatis Mapper XML
        Resource[] resourceArray = applicationContext.getResources("classpath:/mybatis/**/*Mapper.xml");
        sessionFactory.setMapperLocations(resourceArray);

        return sessionFactory.getObject();
    }

    @Primary
    @Bean("dataSourceTransactionManager3")
    public DataSourceTransactionManager dataSourceTransactionManager() {  
        return new DataSourceTransactionManager(dataSource());
    }  
    
}
