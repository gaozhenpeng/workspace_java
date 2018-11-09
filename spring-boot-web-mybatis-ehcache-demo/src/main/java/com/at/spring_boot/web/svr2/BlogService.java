package com.at.spring_boot.web.svr2;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.at.spring_boot.mybatis.dto.BlogDto;
import com.at.spring_boot.mybatis.mapper2.BlogMapper;
import com.at.spring_boot.mybatis.po.Blog;
import com.at.spring_boot.mybatis.po.BlogExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;

@Slf4j
@Service("blogService2")
public class BlogService {
    @Autowired
    @Qualifier("blogMapper2")
    private BlogMapper blogMapper;
    @Autowired
    private MapperFacade mapperFacade;

    /** 
     * create a new blog
     * @param name
     * @param content
     * @return
     */
    @Caching(
        put = {@CachePut(cacheNames = {"blogdto"}, key = "#result.blogId")}
        ,evict = {@CacheEvict(cacheNames = {"blogdto_all"}, allEntries = true)}
    )
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
    @Caching(
            cacheable = {
                    @Cacheable(cacheNames = {"blogdto_all"})
                    }
            )
    public List<BlogDto> list() {
        
        BlogExample blogExample = new BlogExample();
        blogExample.setOrderByClause("name asc");
        
        List<Blog> blogs = blogMapper.selectByExample(blogExample);

        return mapperFacade.mapAsList(blogs, BlogDto.class);
    }

    /**
     * list blogs, if blogId is not null, show the specific blog
     * @param blogId
     * @return
     */
    @Caching(
            cacheable = {
                    @Cacheable(cacheNames = {"blogdto"})
                    }
            )
    public BlogDto list(Long blogId) {
        log.info("list, blogId: '{}' ", blogId);
        Assert.notNull(blogId, "blogId should not be null");
        
        Blog blog = blogMapper.selectByPrimaryKey(blogId);

        return mapperFacade.map(blog, BlogDto.class);
    }
    
    /**
     * remove a blog by id
     * @param blogId
     * @return
     */
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = {"blogdto_all"}, allEntries = true)
                    ,@CacheEvict(cacheNames = {"blogdto"}, key = "#blogId")
                    }
            )
    public int remove(Long blogId) {
        log.info("remove, blogId: '{}' ", blogId);
        Assert.notNull(blogId, "blogId should not be null");
        
        return blogMapper.deleteByPrimaryKey(blogId);
    }

    /**
     * remove a blog by id
     * @param blogId
     * @return
     */
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = {"blogdto_all"}, allEntries = true)
                    }
            ,put = {
                    @CachePut(cacheNames = {"blogdto"}, key = "#blogId")
                    }
            )
    public BlogDto update(Long blogId, String name, String content) {
        log.info("update, blogId: '{}' ", blogId);
        Assert.notNull(blogId, "blogId should not be null");
        Assert.isTrue(name != null || content != null, "both of name and content are null");
        
        Blog blog = new Blog();
        blog.setBlogId(blogId);
        blog.setName(name);
        blog.setContent(content);
        
        int affectedRows = blogMapper.updateByPrimaryKeySelective(blog);
        if(affectedRows <= 0) {
            return mapperFacade.map(null, BlogDto.class);
        }else {
            return mapperFacade.map(blogMapper.selectByPrimaryKey(blogId), BlogDto.class);
        }
    }

    /**
     * list blogs pagingly, if blogId is not null, show the specific blog
     * @param blogId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Cacheable(cacheNames = {"blogdto_all"})
    public List<BlogDto> list(Long blogId, Integer pageNo, Integer pageSize) {
        log.info("list, blogId: '{}', pageNo: '{}', pageSize: '{}' ", blogId, pageNo, pageSize);
        
        BlogExample blogExample = new BlogExample();
        blogExample.setOrderByClause("name asc");
        
        BlogExample.Criteria blogExampleCriteria = blogExample.or();
        if(blogId != null) {
            blogExampleCriteria.andBlogIdEqualTo(blogId);
        }

        PageHelper.startPage(pageNo, pageSize);
        List<Blog> blogs = blogMapper.selectByExample(blogExample);
        log.info(blogs == null ? "blogs is null" : "blog: " + blogs.toString());
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        log.info("pageInfo: '{}'", pageInfo);

        return mapperFacade.mapAsList(blogs, BlogDto.class);
    }

}
