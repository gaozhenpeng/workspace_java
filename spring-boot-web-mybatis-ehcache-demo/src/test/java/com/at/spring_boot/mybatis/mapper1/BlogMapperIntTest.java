package com.at.spring_boot.mybatis.mapper1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.at.spring_boot.mybatis.po.Blog;
import com.at.spring_boot.mybatis.po.BlogExample;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Slf4j
public class BlogMapperIntTest {

    @Autowired
    @Qualifier("blogMapper1")
    BlogMapper blogMapper;
    
    
    @Test
    @Transactional // required to use the level 1 cache of mybatis
    public void testSelectByExample() {
        BlogExample blogExample = new BlogExample();
        List<Blog> blogs1 = blogMapper.selectByExample(blogExample);

        log.info("blogs1: '{}'", blogs1);
        assertNotNull("blogMapper.selectByExample(blogExample) return null", blogs1);
        assertFalse("blogs1.isEmpty() is true", blogs1.isEmpty());
        

        List<Blog> blogs2 = blogMapper.selectByExample(blogExample);

        log.info("blogs2: '{}'", blogs2);
        assertNotNull("blogMapper.selectByExample(blogExample) return null", blogs2);
        assertFalse("blogs2.isEmpty() is true", blogs2.isEmpty());
        
        
        assertEquals("! blogs1.equals(blogs2)", blogs1, blogs2);
        
    }
}
