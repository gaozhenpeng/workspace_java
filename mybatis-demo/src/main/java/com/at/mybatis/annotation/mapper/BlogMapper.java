package com.at.mybatis.annotation.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.at.mybatis.annotation.vo.Blog;

public interface BlogMapper {
    @ResultType(Blog.class) // the column name should be consistent with the db definition
//  @Results({
//      @Result(property="blog_id", column="blog_id")
//      ,@Result(property="name", column="name")
//      ,@Result(property="content", column="content")
//      ,@Result(property="updated_datetime", column="updated_datetime")
//      ,@Result(property="created_datetime", column="created_datetime")
//  })
    @Select("SELECT * FROM blog WHERE blog_id = #{blog_id}")
    Blog selectBlog(int id);
    //@Update("UPDATE blog SET name = #{name}, content = #{content}, updated_datetime = #{updated_datetime}, created_datetime = #{created_datetime} where id = #{id}")
    @Update("<script> "
    + " update blog "
    + " <set> "
    + "     <if test=\"name != null\">name = #{name},</if> "
    + "     <if test=\"content != null\">content = #{content},</if> "
    + "     <if test=\"updated_datetime != null\">updated_datetime = #{updated_datetime},</if> "
    + "     <if test=\"created_datetime != null\">created_datetime = #{created_datetime}</if> "
    + " </set> "
    + " where blog_id = #{blog_id}"
    + "</script>")
    Long updateBlog(Blog blog);
    // Note: when using <set>, the tailing "," will be removed automatically.
    //    Ref: http://www.mybatis.org/mybatis-3/dynamic-sql.html
    //    Here, the set element will dynamically prepend the SET
    //    keyword, and also eliminate any extraneous commas that
    //    might trail the value assignments after the conditions
    //    are applied.
    @Insert("insert into blog (name, content, updated_datetime, created_datetime) values (#{name}, #{content}, now(), now()) ")
    @Options(keyProperty="blog_id", useGeneratedKeys=true) // useGeneratedKeys=true, after successful inserting, the keyProperty will be updated
    Long insertBlog(Blog blog);
}
