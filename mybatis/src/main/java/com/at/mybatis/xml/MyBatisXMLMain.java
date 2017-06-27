package com.at.mybatis.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.at.mybatis.xml.vo.Blog;


public class MyBatisXMLMain {
    private static final Logger logger = LoggerFactory.getLogger(MyBatisXMLMain.class);

    public static void main(String[] args) throws IOException {
        logger.debug("Enterring Main.");

        String resource = "com/at/mybatis/xml/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    
        boolean isAutoCommit = false;
        SqlSession session = sqlSessionFactory.openSession(isAutoCommit);
        try {
            Blog blog = session.selectOne("com.at.mybatis.xml.mapper.BlogMapper.selectBlog", 3);
            logger.info(blog == null ? "blog is null" : "blog: " + blog.toString());
            blog.setContent("Current Time: " + new Date().getTime());
            blog.setName(null);
            session.update("com.at.mybatis.xml.mapper.BlogMapper.updateBlog", blog);
//          if(true){throw new RuntimeException("Broken the transaction.");}
            session.commit();
        }catch(Exception e){
            logger.error("Shit happened!", e);
            session.rollback();
        } finally {
            session.close();
        }

        logger.debug("Exiting Main.");
    }
}