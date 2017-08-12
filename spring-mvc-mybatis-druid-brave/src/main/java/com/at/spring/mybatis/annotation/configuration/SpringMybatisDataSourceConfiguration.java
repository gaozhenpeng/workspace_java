package com.at.spring.mybatis.annotation.configuration;

import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@EnableTransactionManagement // <tx:annotation-driven />, @Transactional
@MapperScan("com.at.spring.mybatis.annotation.mapper")
public class SpringMybatisDataSourceConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SpringMybatisDataSourceConfiguration.class);

    @Resource(name="DATABASE_DRIVERCLASS")
    private String database_driverclass = null;
    @Resource(name="DATABASE_URL")
    private String database_url = null;
    @Resource(name="DATABASE_USERNAME")
    private String database_username = null;
    @Resource(name="DATABASE_PASSWORD")
    private String database_password = null;
    
    
    @Bean
    public DataSource dataSource() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
//        logger.info("Preparing simple driver datasource:");
//
//        SimpleDriverDataSource simpleDriverDataSource  = new SimpleDriverDataSource();
//        Driver driver = (Driver)Class.forName(database_driverclass).newInstance();
//        simpleDriverDataSource.setDriver(driver);
//        simpleDriverDataSource.setUrl(database_url);
//        simpleDriverDataSource.setUsername(database_username);
//        simpleDriverDataSource.setPassword(database_password);

        logger.info("Preparing druid datasource:");
        DruidDataSource druidDS = new DruidDataSource();
        druidDS.setDriverClassName(database_driverclass);
        druidDS.setUrl(database_url);
        druidDS.setUsername(database_username);
        druidDS.setPassword(database_password);
        
        // druid specific configurations
        //   配置初始化大小、最小、最大
        druidDS.setInitialSize(5);
        druidDS.setMinIdle(3);
        druidDS.setMaxActive(100);
        //   配置获取连接等待超时的时间
        druidDS.setMaxWait(60000);
        //   配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        druidDS.setTimeBetweenEvictionRunsMillis(60000);
        //   配置一个连接在池中最小生存的时间，单位是毫秒
        druidDS.setMinEvictableIdleTimeMillis(300000);
        //   连接测试
        druidDS.setValidationQuery("SELECT 'x' FROM DUAL");
        druidDS.setTestWhileIdle(true);
        druidDS.setTestOnBorrow(false);
        druidDS.setTestOnReturn(false);
        //   打开PSCache，并且指定每个连接上PSCache的大小
        druidDS.setPoolPreparedStatements(true);
        druidDS.setMaxPoolPreparedStatementPerConnectionSize(20);
        //   关闭长时间不使用的连接
        druidDS.setRemoveAbandoned(true);
        druidDS.setRemoveAbandonedTimeout(1200); // 1200s
        druidDS.setLogAbandoned(true);
        //   filter
        druidDS.setFilters("stat");
        
//      logger.info("\tchecking database:");
//      JdbcTemplate jdbcTemplate = new JdbcTemplate(simpleDriverDataSource);
//      jdbcTemplate.execute("DESCRIBE blog");
//      logger.info("\t\tdatabase ok");
        
        logger.debug("initializing database");;
        JdbcTemplate jdbcTemplate = new JdbcTemplate(druidDS);
        jdbcTemplate.execute("DROP TABLE IF EXISTS `blog`;");
        jdbcTemplate.execute("CREATE TABLE `blog` ("
                             + "  `blog_id` bigint unsigned NOT NULL AUTO_INCREMENT,"
                             + "  `name` varchar(64) DEFAULT NULL,"
                             + "  `content` varchar(512) DEFAULT NULL,"
                             + "  `created_datetime` datetime DEFAULT null,"
                             + "  `updated_datetime` datetime DEFAULT null,"
                             + "  PRIMARY KEY (`blog_id`)"
                             + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;"
                             + "");
        jdbcTemplate.update("insert into `blog` (`name`, `content`, `created_datetime`, `updated_datetime`) values"
                            + " ('name1', 'content1', now(), now())"
                            + ",('name2', 'content2', now(), now())"
                            + ",('name3', 'content3', now(), now())"
                            + ",('name4', 'content4', now(), now())"
                            + ",('name5', 'content5', now(), now())"
                            + "");

        return druidDS;
    }
    
    
    @Bean
    public DataSourceTransactionManager transactionManager() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        logger.debug("Preparing DataSourceTransactionManager.");
        return new DataSourceTransactionManager(dataSource());
    }
    
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        logger.debug("Preparing SqlSessionFactory.");
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        return sessionFactoryBean.getObject();
    }
    
    @Bean
    public SqlSessionTemplate sqlSessionTemplate() throws Exception{
        logger.debug("Preparing SqlSessionTemplate.");
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory(), ExecutorType.BATCH);
        return sqlSessionTemplate;
    }
    
}