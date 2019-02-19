package com.at.spring.mybatis.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.at.spring.mybatis.vo.Blog;


@Repository
public class BlogDao extends SqlSessionDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger(BlogDao.class);

	@Override
	@Autowired
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate){
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	
	public List<Blog> listPage(Map<String, Object> paramMap, String sqlId) {
		logger.debug("paramMap: '" + paramMap +"'");
		if (paramMap == null)
			paramMap = new HashMap<String, Object>();

		List<Blog> list = getSqlSession().selectList(this.getClass().getName() + "." + sqlId, paramMap, new RowBounds(3,3));

		return list;
	}
	
	public Blog listOne(Map<String, Object> paramMap, String sqlId) {
		logger.debug("paramMap: '" + paramMap +"'");
		if (paramMap == null)
			paramMap = new HashMap<String, Object>();

		Blog blog = getSqlSession().selectOne(this.getClass().getName() + "." + sqlId, paramMap);

		return blog;
	}
}
