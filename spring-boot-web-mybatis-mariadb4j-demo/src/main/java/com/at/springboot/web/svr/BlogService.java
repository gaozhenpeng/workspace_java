package com.at.springboot.web.svr;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.at.springboot.mybatis.dto.BlogDto;
import com.at.springboot.mybatis.mapper.BlogMapper;
import com.at.springboot.mybatis.po.Blog;
import com.at.springboot.mybatis.po.BlogExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;

@Slf4j
@Service
public class BlogService {
    @Autowired
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
        // for testing
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
        
        if(blogId != null) {
            blogExample
                .or()
                    .andBlogIdEqualTo(blogId);
        }

        List<Blog> blogs = blogMapper.selectByExample(blogExample);

        return mapperFacade.mapAsList(blogs, BlogDto.class);
    }

    /**
     * list blogs pagingly, if blogId is not null, show the specific blog
     * @param blogId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<BlogDto> list(Long blogId, Integer pageNo, Integer pageSize) {
        log.info("list, blogId: '{}', pageNo: '{}', pageSize: '{}' ", blogId, pageNo, pageSize);
        
        BlogExample blogExample = new BlogExample();
        blogExample.setOrderByClause("name asc");
        
        if(blogId != null) {
            blogExample
                .or()
                    .andBlogIdEqualTo(blogId);
        }

        PageHelper.startPage(pageNo, pageSize);
        List<Blog> blogs = blogMapper.selectByExample(blogExample);
        log.info(blogs == null ? "blogs is null" : "blog: " + blogs.toString());
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        log.info("pageInfo: '{}'", pageInfo);

        return mapperFacade.mapAsList(blogs, BlogDto.class);
    }

}
