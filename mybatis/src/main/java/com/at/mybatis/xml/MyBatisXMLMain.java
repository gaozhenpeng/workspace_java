package com.at.mybatis.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
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

        String resource = "mybatis/mybatis-config.xml";
        InputStream inputStream = MyBatisXMLMain.class.getClassLoader().getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    
        boolean isAutoCommit = false;
        SqlSession session = sqlSessionFactory.openSession(isAutoCommit);
        try {
            List<Integer> ids = new ArrayList<Integer>();
            ids.add(1);
            ids.add(2);
            ids.add(3);
            ids.add(4);
            ids.add(5);
            List<Integer> not_ids = new ArrayList<Integer>();
            not_ids.add(5);
            not_ids.add(6);
            not_ids.add(7);
            not_ids.add(8);
            
            Map params = new HashMap();
            params.put("not_name", "notmyname");
            params.put("ids", ids);
            params.put("not_ids", not_ids);

            List<Blog> blogs = session.selectList("com.at.mybatis.xml.mapper.BlogMapper.selectBlogs", params, new RowBounds(3, 3));
            logger.info(blogs == null ? "blogs is null" : "blog: " + blogs.toString());
            for(Blog blog : blogs){
	            blog.setContent("Current Time: " + System.currentTimeMillis());
	            blog.setName(null);
	            blog.setUpdated_datetime(new Date());
	            session.update("com.at.mybatis.xml.mapper.BlogMapper.updateBlog", blog);
	//          if(true){throw new RuntimeException("Broken the transaction.");}
	            session.commit();
            }
        }catch(Exception e){
            logger.error("Shit happened!", e);
            session.rollback();
        } finally {
            session.close();
        }

        logger.debug("Exiting Main.");
    }
}