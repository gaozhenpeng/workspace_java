package com.at.springboot.web.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.at.springboot.mybatis.dto.BlogDto;
import com.at.springboot.web.svr.BlogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @RequestMapping(method=RequestMethod.POST)
    public BlogDto create(
            @RequestParam(name="name", required=true) String name
            , @RequestParam(name="content", required=false, defaultValue="") String content
            ) {
        log.info("create blog, name: '{}', content: '{}' ", name, content);
        
        BlogDto blogDto = blogService.create(name, content);
        return blogDto;
    }


    @RequestMapping(method=RequestMethod.GET)
    public List<BlogDto> list(
            @RequestParam(name="blogId", required=false) Long blogId
            ,@RequestParam(name="pageNo", required=false) Integer pageNo
            ,@RequestParam(name="pageSize", required=false) Integer pageSize
            ) {
        log.info("list blog, blogId: '{}', pageNo: '{}', pageSize: '{}' ", blogId, pageNo, pageSize);
        
        List<BlogDto> blogDtos = null;
        if(pageNo == null || pageSize == null) {
            blogDtos = blogService.list(blogId);
        }else {
            blogDtos = blogService.list(blogId, pageNo, pageSize);
        }
        return blogDtos;
    }
}
