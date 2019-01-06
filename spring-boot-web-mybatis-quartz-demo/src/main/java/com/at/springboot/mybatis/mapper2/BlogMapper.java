package com.at.springboot.mybatis.mapper2;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import com.at.springboot.mybatis.po.Blog;
import com.at.springboot.mybatis.po.BlogExample;

@Component("blogMapper2")
public interface BlogMapper {
    @SelectProvider(type=BlogSqlProvider.class, method="countByExample")
    long countByExample(BlogExample example);

    @DeleteProvider(type=BlogSqlProvider.class, method="deleteByExample")
    int deleteByExample(BlogExample example);

    @Delete({
        "delete from blog",
        "where blog_id = #{blogId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long blogId);

    @Insert({
        "insert into blog (blog_id, name, ",
        "content, created_datetime, ",
        "updated_datetime)",
        "values (#{blogId,jdbcType=BIGINT}, #{name,jdbcType=VARCHAR}, ",
        "#{content,jdbcType=VARCHAR}, #{createdDatetime,jdbcType=TIMESTAMP}, ",
        "#{updatedDatetime,jdbcType=TIMESTAMP})"
    })
    @Options(keyColumn="blog_id", keyProperty="blogId", useGeneratedKeys=true) // useGeneratedKeys=true, after successful inserting, the keyProperty will be updated
    int insert(Blog record);

    @InsertProvider(type=BlogSqlProvider.class, method="insertSelective")
    @Options(keyColumn="blog_id", keyProperty="blogId", useGeneratedKeys=true) // useGeneratedKeys=true, after successful inserting, the keyProperty will be updated
    int insertSelective(Blog record);

    @SelectProvider(type=BlogSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="blog_id", property="blogId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_datetime", property="createdDatetime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="updated_datetime", property="updatedDatetime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<Blog> selectByExample(BlogExample example);

    @Select({
        "select",
        "blog_id, name, content, created_datetime, updated_datetime",
        "from blog",
        "where blog_id = #{blogId,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="blog_id", property="blogId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_datetime", property="createdDatetime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="updated_datetime", property="updatedDatetime", jdbcType=JdbcType.TIMESTAMP)
    })
    Blog selectByPrimaryKey(Long blogId);

    @UpdateProvider(type=BlogSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") Blog record, @Param("example") BlogExample example);

    @UpdateProvider(type=BlogSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") Blog record, @Param("example") BlogExample example);

    @UpdateProvider(type=BlogSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Blog record);

    @Update({
        "update blog",
        "set name = #{name,jdbcType=VARCHAR},",
          "content = #{content,jdbcType=VARCHAR},",
          "created_datetime = #{createdDatetime,jdbcType=TIMESTAMP},",
          "updated_datetime = #{updatedDatetime,jdbcType=TIMESTAMP}",
        "where blog_id = #{blogId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Blog record);
}