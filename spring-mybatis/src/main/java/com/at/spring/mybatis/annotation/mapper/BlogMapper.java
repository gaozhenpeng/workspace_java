package com.at.spring.mybatis.annotation.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.at.spring.mybatis.annotation.vo.Blog;

public interface BlogMapper {
//    @ResultType(Blog.class) // the column name should be exactly the same as the db definition
    @Results({
      @Result(property="blogId", column="blog_id")
      ,@Result(property="name", column="name")
      ,@Result(property="content", column="content")
      ,@Result(property="updatedDatetime", column="updated_datetime")
      ,@Result(property="createdDatetime", column="created_datetime")
    })
    @Select("SELECT * FROM blog WHERE blog_id = #{blogId}")
    Blog selectBlog(int id);
    //@Update("UPDATE blog SET name = #{name}, content = #{content}, updated_datetime = #{updated_datetime}, created_datetime = #{created_datetime} where id = #{id}")
    @Update("<script> "
    + " update blog "
    + " <set> "
    + "     <if test=\"name != null\">name = #{name},</if> "
    + "     <if test=\"content != null\">content = #{content},</if> "
    + "     <if test=\"updatedDatetime != null\">updated_datetime = #{updatedDatetime},</if> "
    + " </set> "
    + " where blog_id = #{blogId}"
    + "</script>")
    long updateBlog(Blog blog);
    // Note: when using <set>, the tailing "," will be removed automatically.
    //    Ref: http://www.mybatis.org/mybatis-3/dynamic-sql.html
    //    Here, the set element will dynamically prepend the SET
    //    keyword, and also eliminate any extraneous commas that
    //    might trail the value assignments after the conditions
    //    are applied.
    @Insert("insert into blog (name, content, updated_datetime, created_datetime) values (#{name}, #{content}, now(), now()) ")
    @Options(keyColumn="blog_id", keyProperty="blogId", useGeneratedKeys=true) // useGeneratedKeys=true, after successful inserting, the keyProperty will be updated
    long insertBlog(Blog blog);
}