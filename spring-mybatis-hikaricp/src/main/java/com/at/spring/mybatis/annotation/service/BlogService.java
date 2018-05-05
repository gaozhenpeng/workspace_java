package com.at.spring.mybatis.annotation.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.at.spring.mybatis.annotation.mapper.BlogMapper;
import com.at.spring.mybatis.annotation.vo.Blog;

@Service
public class BlogService {
	@Autowired
	BlogMapper blogMapper = null;

	@Transactional
	public void updateBatch(Long toUpdateBlogID) {
		int i = 1;
		Blog toUpdateBlog = new Blog();
		toUpdateBlog.setBlogId(toUpdateBlogID);
		toUpdateBlog.setContent("Update: " + (i++) + "; Current Time: " + new Date().getTime());
		blogMapper.updateBlog(toUpdateBlog);

		// if(true) throw new RuntimeException("Broken the transaction.");

		Blog toUpdateBlog2 = new Blog();
		toUpdateBlog2.setBlogId(toUpdateBlogID);
		toUpdateBlog2.setContent("Update: " + (i++) + "; Current Time: " + new Date().getTime());
		blogMapper.updateBlog(toUpdateBlog2);

		Blog toUpdateBlog3 = new Blog();
		toUpdateBlog3.setBlogId(toUpdateBlogID);
		toUpdateBlog3.setContent("Update: " + (i++) + "; Current Time: " + new Date().getTime());
		blogMapper.updateBlog(toUpdateBlog3);

	}

	@Transactional
	public void insertBatch(List<Blog> toInsertBlogs) {
		if (toInsertBlogs == null || toInsertBlogs.size() <= 0) {
			return;
		}
		for (Blog toInsertBlog : toInsertBlogs) {
			blogMapper.insertBlog(toInsertBlog);
		}
	}

}
