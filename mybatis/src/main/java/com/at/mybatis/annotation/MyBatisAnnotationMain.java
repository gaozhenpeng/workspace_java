package com.at.mybatis.annotation;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.at.mybatis.annotation.mapper.BlogMapper;
import com.at.mybatis.annotation.vo.Blog;

public class MyBatisAnnotationMain {
    private static final Logger logger = LoggerFactory.getLogger(MyBatisAnnotationMain.class);


    public static void main(String[] args) throws IOException {
        logger.debug("Enterring Main.");
        
        logger.debug("setting datasource factory");
        Properties dataSourceProperties = new Properties();
        dataSourceProperties.setProperty("driver", "com.mysql.jdbc.Driver");
        dataSourceProperties.setProperty("url", "jdbc:mysql://127.0.0.1:3306/test");
        dataSourceProperties.setProperty("username", "root");
        dataSourceProperties.setProperty("password", "");
        
        UnpooledDataSourceFactory unpooledDataSourceFactory = new UnpooledDataSourceFactory();
        unpooledDataSourceFactory.setProperties(dataSourceProperties);
        
        logger.debug("getting datasource");
        DataSource dataSource = unpooledDataSourceFactory.getDataSource();
        
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        
        logger.debug("initalizing environment");
        Environment environment = new Environment("development", transactionFactory, dataSource);
        
        logger.debug("initalizing configuration");
        Configuration configuration = new Configuration(environment);
        
        logger.debug("adding mappers to configuration");
        configuration.addMappers("com.at.mybatis.annotation.mapper");
        
        
        logger.debug("building SqlSessionFactoryBuilder");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        
        
        logger.debug("SqlSessionFactoryBuilder.openSession()");
        boolean isAutoCommit = false;
        ExecutorType executorType = ExecutorType.REUSE; // ExecutorType.BATCH; ExecutorType.REUSE; ExecutorType.SIMPLE;
        SqlSession session = sqlSessionFactory.openSession(executorType, isAutoCommit);
        try {
            logger.debug("session.getMapper(BlogMapper.class)");
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            logger.debug("mapper.selectBlog(3)");
            Blog blog = mapper.selectBlog(3);
            logger.info(blog == null ? "blog is null" : "blog: " + blog.toString());
            Blog toUpdateBlog = new Blog();
            toUpdateBlog.setBlog_id(blog.getBlog_id());
            toUpdateBlog.setContent("Current Time: " + new Date().getTime());
            mapper.updateBlog(toUpdateBlog);
//          if(true){throw new RuntimeException("Broken the updatetransaction.");}
            logger.debug("blog updated.");
            for(int i = 0 ; i < 10 ; i++){
                Blog toInsertBlog = new Blog();
                toInsertBlog.setName("new_inserted_name_"+ i);
                toInsertBlog.setContent("new inserted blog content, " + i + ", " + new Date().getTime());
                Long retIns = mapper.insertBlog(toInsertBlog);
                logger.debug("retIns: " + retIns + "; toInsertBlog.getBlogId(): " + toInsertBlog.getBlog_id()); // Not able to get the blog_id for the ExecutorType.BATCH;
//              if(i == 5){throw new RuntimeException("Broken the insert transaction.");}
            }
            logger.debug("blog inserted.");
            session.commit();
        } catch(Exception e){
            logger.error("Shit happened!", e);
            session.rollback();
        } finally {
            logger.debug("session.close()");
            session.close();
        }

        logger.debug("Exiting Main.");
    }
}