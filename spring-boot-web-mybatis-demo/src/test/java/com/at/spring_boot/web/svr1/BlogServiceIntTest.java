package com.at.spring_boot.web.svr1;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.at.spring_boot.mybatis.dto.BlogDto;
import com.at.spring_boot.mybatis.mapper1.BlogMapper;
import com.at.spring_boot.mybatis.po.Blog;
import com.at.spring_boot.mybatis.po.BlogExample;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Slf4j
public class BlogServiceIntTest {
    @Autowired
    @Qualifier("blogService1")
    private BlogService blogService;
    @MockBean(name="blogMapper1")
    private BlogMapper blogMapper;
    
    
    
    private List<Blog> blogMockList = null;
    @Before
    public void setUp() {
        blogMockList = new ArrayList<>();
        for(int i = 0 ; i < 4 ; i++) {
            Blog blog = new Blog();
            blog.setBlogId(new Long(i+1));
            blog.setContent("content" +i);
            blog.setCreatedDatetime(new Date());
            blog.setName("name" +i);
            blog.setUpdatedDatetime(new Date());
            blogMockList.add(blog);
        }
    }
    
    @Test
    public void testListAll() {
        when(blogMapper.selectByExample(Mockito.any(BlogExample.class))).thenReturn(blogMockList);
        List<BlogDto> blogDtos = blogService.list(null);
        log.info("blogDtos: '{}'", blogDtos);
        assertNotNull("blogService.list(null) return null", blogDtos);
        for(int i = 0 ; i < blogDtos.size() ; i++) {
            BlogDto blogDto = blogDtos.get(i);
            assertNotNull("blogDto.getBlogId() is null", blogDto.getBlogId());
            long blogId = Long.parseLong(blogDto.getBlogId(), 10);
            assertTrue("blogId <= 0 || blogId > 4", 0 < blogId && blogId <= 4);
        }
    }
}
