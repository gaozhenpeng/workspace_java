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

import com.at.mybatis.annotation.mapper.BlogMapper;
import com.at.mybatis.annotation.vo.Blog;
import com.github.pagehelper.PageInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyBatisAnnotationMain {


    public static void main(String[] args) throws IOException {
        log.debug("Enterring Main.");
        
        log.debug("setting datasource factory");
        Properties dataSourceProperties = new Properties();
        dataSourceProperties.setProperty("driver", "com.mysql.cj.jdbc.Driver");
        dataSourceProperties.setProperty("url", "jdbc:mysql://127.0.0.1:3306/test?serverTimezone=UTC&useSSL=false");
        dataSourceProperties.setProperty("username", "root");
        dataSourceProperties.setProperty("password", "");
        
        UnpooledDataSourceFactory unpooledDataSourceFactory = new UnpooledDataSourceFactory();
        unpooledDataSourceFactory.setProperties(dataSourceProperties);
        
        log.debug("getting datasource");
        DataSource dataSource = unpooledDataSourceFactory.getDataSource();
        
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        
        log.debug("initalizing environment");
        Environment environment = new Environment("development", transactionFactory, dataSource);
        
        log.debug("initalizing configuration");
        Configuration configuration = new Configuration(environment);
        
        log.debug("adding mappers to configuration");
        configuration.addMappers("com.at.mybatis.annotation.mapper");

        log.debug("adding plugin to configuration");
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties pageInterceptorProperties = new Properties();
        //// enable method arguments in Mapper @Param("pageNumKey"), @Param("PageSizeKey")
//        pageInterceptorProperties.setProperty("supportMethodsArguments", "true");
//        pageInterceptorProperties.setProperty("params", "pageNum=pageNumKey;pageSize=pageSizeKey;");
        pageInterceptor.setProperties(pageInterceptorProperties);
        configuration.addInterceptor(pageInterceptor);
        
        
        log.debug("building SqlSessionFactoryBuilder");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        
        
        log.debug("SqlSessionFactoryBuilder.openSession()");
        boolean isAutoCommit = false;
        ExecutorType executorType = ExecutorType.REUSE; // ExecutorType.BATCH; ExecutorType.REUSE; ExecutorType.SIMPLE;
        SqlSession session = sqlSessionFactory.openSession(executorType, isAutoCommit);
        try {
            log.debug("session.getMapper(BlogMapper.class)");
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            log.debug("mapper.selectBlog(3)");
            Blog blog = mapper.selectBlog(3);
            log.info(blog == null ? "blog is null" : "blog: " + blog.toString());
            Blog toUpdateBlog = new Blog();
            toUpdateBlog.setBlog_id(blog.getBlog_id());
            toUpdateBlog.setContent("Current Time: " + new Date().getTime());
            mapper.updateBlog(toUpdateBlog);
//          if(true){throw new RuntimeException("Broken the updatetransaction.");}
            log.debug("blog updated.");
            for(int i = 0 ; i < 10 ; i++){
                Blog toInsertBlog = new Blog();
                toInsertBlog.setName("new_inserted_name_"+ i);
                toInsertBlog.setContent("new inserted blog content, " + i + ", " + new Date().getTime());
                Long retIns = mapper.insertBlog(toInsertBlog);
                log.debug("retIns: " + retIns + "; toInsertBlog.getBlogId(): " + toInsertBlog.getBlog_id()); // Not able to get the blog_id for the ExecutorType.BATCH;
//              if(i == 5){throw new RuntimeException("Broken the insert transaction.");}
            }
            log.debug("blog inserted.");
            session.commit();
        } catch(Exception e){
            log.error("Shit happened!", e);
            session.rollback();
        } finally {
            log.debug("session.close()");
            session.close();
        }

        log.debug("Exiting Main.");
    }
}