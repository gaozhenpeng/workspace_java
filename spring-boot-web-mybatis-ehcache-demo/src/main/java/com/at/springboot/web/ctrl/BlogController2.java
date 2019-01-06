package com.at.springboot.web.ctrl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.at.springboot.mybatis.dto.BlogDto;
import com.at.springboot.web.svr2.BlogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/blog2")
public class BlogController2 {
    @Autowired
    @Qualifier("blogService2")
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
        
        List<BlogDto> blogDtos = new ArrayList<>();
        if(pageNo == null || pageSize == null) {
            if(blogId == null) {
                blogDtos.addAll(blogService.list());
            }else{
                blogDtos.add(blogService.list(blogId));
            }
        }else {
            blogDtos = blogService.list(blogId, pageNo, pageSize);
        }
        return blogDtos;
    }
}
