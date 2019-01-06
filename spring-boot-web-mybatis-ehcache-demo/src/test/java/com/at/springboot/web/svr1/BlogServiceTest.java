package com.at.springboot.web.svr1;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.at.springboot.mybatis.dto.BlogDto;
import com.at.springboot.mybatis.mapper1.BlogMapper;
import com.at.springboot.mybatis.po.Blog;
import com.at.springboot.mybatis.po.BlogExample;
import com.at.springboot.web.svr1.BlogService;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.DateToStringConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class BlogServiceTest {
    @InjectMocks
    private BlogService blogService;
    @Mock
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

        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        ConverterFactory converterFactory  = mapperFactory.getConverterFactory();
                
        // global default date to string converter
        converterFactory.registerConverter(new DateToStringConverter("yyyy-MM-dd HH:mm:ss"));
        // named converter
        converterFactory.registerConverter("dateFull", new DateToStringConverter("yyyy-MM-dd"));
        converterFactory.registerConverter("dateTimeFull", new DateToStringConverter("yyyy-MM-dd HH:mm:ss"));
        blogService.mapperFacade = mapperFactory.getMapperFacade();
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
