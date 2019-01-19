package com.at.spring.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.at.spring.mybatis.po.Blog;
import com.at.spring.mybatis.po.BlogExample;


public interface BlogMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sat Jan 19 18:53:52 CST 2019
     */
    long countByExample(BlogExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sat Jan 19 18:53:52 CST 2019
     */
    int deleteByExample(BlogExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sat Jan 19 18:53:52 CST 2019
     */
    int deleteByPrimaryKey(Long blogId);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sat Jan 19 18:53:52 CST 2019
     */
    int insert(Blog record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sat Jan 19 18:53:52 CST 2019
     */
    int insertSelective(Blog record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sat Jan 19 18:53:52 CST 2019
     */
    List<Blog> selectByExample(BlogExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sat Jan 19 18:53:52 CST 2019
     */
    Blog selectByPrimaryKey(Long blogId);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sat Jan 19 18:53:52 CST 2019
     */
    int updateByExampleSelective(@Param("record") Blog record, @Param("example") BlogExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sat Jan 19 18:53:52 CST 2019
     */
    int updateByExample(@Param("record") Blog record, @Param("example") BlogExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sat Jan 19 18:53:52 CST 2019
     */
    int updateByPrimaryKeySelective(Blog record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sat Jan 19 18:53:52 CST 2019
     */
    int updateByPrimaryKey(Blog record);
}