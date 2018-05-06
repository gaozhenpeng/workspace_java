package com.at.spring_boot.web.svr1;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.at.spring_boot.mybatis.dto.BlogDto;
import com.at.spring_boot.mybatis.mapper1.BlogMapper;
import com.at.spring_boot.mybatis.po.Blog;
import com.at.spring_boot.mybatis.po.BlogExample;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;

@Slf4j
@Service("blogService1")
public class BlogService {
    @Autowired
    @Qualifier("blogMapper1")
    private BlogMapper blogMapper;
    @Autowired
    private MapperFacade mapperFacade;

    /** 
     * create a new blog
     * @param name
     * @param content
     * @return
     */
//    @Transactional
    public BlogDto create(String name, String content) {
        log.info("create, name: '{}', content: '{}' ", name, content);
        
        Date now = new Date();
        
        Blog blog = new Blog();
        blog.setName(name);
        blog.setContent(content);
        blog.setCreatedDatetime(now);
        blog.setUpdatedDatetime(now);
        
        
        int affectedRows = blogMapper.insertSelective(blog);
        if(affectedRows <= 0) {
            String msg = String.format("Not able to insert record, blog: '%s'", blog);
            throw new RuntimeException(msg);
        }
        if(!StringUtils.isEmpty(content) && content.contains("throw")) {
            throw new RuntimeException(content);
        }
        
        return mapperFacade.map(blog, BlogDto.class);
    }

    /**
     * list blogs, if blogId is not null, show the specific blog
     * @param blogId
     * @return
     */
    public List<BlogDto> list(Long blogId) {
        log.info("list, blogId: '{}' ", blogId);
        
        BlogExample blogExample = new BlogExample();
        blogExample.setOrderByClause("name asc");
        
        BlogExample.Criteria blogExampleCriteria = blogExample.or();
        if(blogId != null) {
            blogExampleCriteria.andBlogIdEqualTo(blogId);
        }

        List<Blog> blogs = blogMapper.selectByExample(blogExample);

        return mapperFacade.mapAsList(blogs, BlogDto.class);
    }

}
