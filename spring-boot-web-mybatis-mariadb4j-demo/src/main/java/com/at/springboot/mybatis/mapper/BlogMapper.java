package com.at.springboot.mybatis.mapper;

import com.at.springboot.mybatis.po.Blog;
import com.at.springboot.mybatis.po.BlogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BlogMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sun Jan 20 12:49:45 CST 2019
     */
    long countByExample(BlogExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sun Jan 20 12:49:45 CST 2019
     */
    int deleteByExample(BlogExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sun Jan 20 12:49:45 CST 2019
     */
    int deleteByPrimaryKey(Long blogId);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sun Jan 20 12:49:45 CST 2019
     */
    int insert(Blog record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sun Jan 20 12:49:45 CST 2019
     */
    int insertSelective(Blog record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sun Jan 20 12:49:45 CST 2019
     */
    List<Blog> selectByExample(BlogExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sun Jan 20 12:49:45 CST 2019
     */
    Blog selectByPrimaryKey(Long blogId);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sun Jan 20 12:49:45 CST 2019
     */
    int updateByExampleSelective(@Param("record") Blog record, @Param("example") BlogExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sun Jan 20 12:49:45 CST 2019
     */
    int updateByExample(@Param("record") Blog record, @Param("example") BlogExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sun Jan 20 12:49:45 CST 2019
     */
    int updateByPrimaryKeySelective(Blog record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table blog
     * @mbg.generated  Sun Jan 20 12:49:45 CST 2019
     */
    int updateByPrimaryKey(Blog record);
}