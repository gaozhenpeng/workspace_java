package com.at.spring.mybatis.annotation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.at.spring.mybatis.annotation.mapper.BlogMapper;
import com.at.spring.mybatis.annotation.service.BlogService;
import com.at.spring.mybatis.annotation.vo.Blog;

@Component
public class SpringMybatisMain {
    private static final Logger logger = LoggerFactory.getLogger(SpringMybatisMain.class);
    @Autowired
    ApplicationContext context;
    public void main() throws IOException {
        logger.debug("Enterring Main.");

        BlogMapper blogMapper = context.getBean(BlogMapper.class);
        Blog blog2 = blogMapper.selectBlog(3);
        logger.info(blog2 == null ? "blog is null" : "blog: " + blog2.toString());
        Long blog2_id = blog2.getBlog_id();
        
        
        DataSourceTransactionManager txManager = context.getBean(DataSourceTransactionManager.class);
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);
        
        try{
            int i = 1;
            Blog toUpdateBlog = new Blog();
            toUpdateBlog.setBlog_id(blog2_id);
            toUpdateBlog.setContent("Update: "+(i++)+"; Current Time: " + new Date().getTime());
            blogMapper.updateBlog(toUpdateBlog);
            
//          if(true) throw new RuntimeException("Broken the transaction.");
            
            Blog toUpdateBlog2 = new Blog();
            toUpdateBlog2.setBlog_id(blog2_id);
            toUpdateBlog2.setContent("Update: "+(i++)+"; Current Time: " + new Date().getTime());
            blogMapper.updateBlog(toUpdateBlog2);
            
            Blog toUpdateBlog3 = new Blog();
            toUpdateBlog3.setBlog_id(blog2_id);
            toUpdateBlog3.setContent("Update: "+(i++)+"; Current Time: " + new Date().getTime());
            blogMapper.updateBlog(toUpdateBlog3);
            
            txManager.commit(txStatus);
        }catch(Exception e){
            logger.error("Shit happened!", e);
            txManager.rollback(txStatus);
        }
        
        // blog service
        logger.debug("Getting BlogService.");
        BlogService blogService = context.getBean(BlogService.class);
        logger.debug("Batch updating.");
        blogService.updateBatch(blog2_id); // update batch
        
        logger.debug("Batch inserting.");
        List<Blog> toInsertBlogs = new ArrayList<Blog>();
        for(int i = 0; i < 10 ; i++){
            Blog toInsertBlog = new Blog();
            toInsertBlog.setName("newInsertedBlogName" + i);
            toInsertBlog.setContent("newInsertedBlogContent" + i);
            toInsertBlogs.add(toInsertBlog);
        }
        blogService.insertBatch(toInsertBlogs);// insert batch
        
//      context.close(); // java.lang.UnsupportedOperationException: Manual close is not allowed over a Spring managed SqlSession
        logger.debug("Exiting Main.");
    }
}