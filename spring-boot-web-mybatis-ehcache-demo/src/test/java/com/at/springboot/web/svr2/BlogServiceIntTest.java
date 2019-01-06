package com.at.springboot.web.svr2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.at.springboot.mybatis.dto.BlogDto;
import com.at.springboot.web.svr2.BlogService;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Slf4j
public class BlogServiceIntTest {
    @Autowired
    @Qualifier("blogService2")
    private BlogService blogService;
    
    
    @Test
    public void testCacheListAll() {
        List<BlogDto> blogDtos1 = blogService.list();
        log.info("blogDtos1: '{}'", blogDtos1);
        assertNotNull("blogService.list(null) return null", blogDtos1);
        assertFalse("blogDtos1.isEmpty() is true", blogDtos1.isEmpty());
        
        log.info("expect getting blogdto from cache.");
        List<BlogDto> blogDtos2 = blogService.list();
        log.info("blogDtos2: '{}'", blogDtos2);
        assertNotNull("blogService.list(null) return null", blogDtos2);
        assertFalse("blogDtos2.isEmpty() is true", blogDtos2.isEmpty());
        
        
        assertEquals("blogDtos1 ne blogDtos2", blogDtos1, blogDtos2);
        assertSame("blogDtos1 != blogDtos2", blogDtos1, blogDtos2);
    }

    @Test
    public void testCacheUpdate() {

        List<BlogDto> blogDtos1 = blogService.list();
        log.info("blogDtos1: '{}'", blogDtos1);
        assertNotNull("blogService.list(null) return null", blogDtos1);
        assertFalse("blogDtos1.isEmpty() is true", blogDtos1.isEmpty());
        
        BlogDto blogDto1 = blogDtos1.get(0);
        Long blogDto1Id = Long.parseLong(blogDto1.getBlogId(), 10);
        
        BlogDto queriedBlogDto1 = blogService.list(blogDto1Id);
        log.info("queriedBlogDto1: '{}'", queriedBlogDto1);
        
        BlogDto blogDtoUpdated = blogService.update(blogDto1Id, "update name", "update content");
        assertNotNull("blogService.update return null", blogDtoUpdated);
        

        BlogDto queriedBlogDto2 = blogService.list(blogDto1Id);
        log.info("queriedBlogDto2: '{}'", queriedBlogDto2);
        
        
        
        assertNotEquals("blogDtos1 eq blogDtos2", queriedBlogDto1, queriedBlogDto2);
        
        

        log.info("expect getting blogdto from database.");
        List<BlogDto> blogDtos2 = blogService.list();
        log.info("blogDtos2: '{}'", blogDtos2);
        assertNotNull("blogService.list(null) return null", blogDtos2);
        assertFalse("blogDtos2.isEmpty() is true", blogDtos2.isEmpty());
        
        
        assertNotEquals("blogDtos1 eq blogDtos2", blogDtos1, blogDtos2);
    }
}
