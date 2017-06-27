package com.at.spring.mybatis.annotation.vo;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * USE test;
 * DROP TABLE IF EXISTS `blog`;
 * CREATE TABLE `blog` (
 *   `blog_id` bigint unsigned NOT NULL AUTO_INCREMENT,
 *   `name` varchar(64) DEFAULT NULL,
 *   `content` varchar(512) DEFAULT NULL,
 *   `created_datetime` datetime DEFAULT null,
 *   `updated_datetime` datetime DEFAULT null,
 *   PRIMARY KEY (`blog_id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 * 
 * insert into `blog` (`name`, `content`, `created_datetime`, `updated_datetime`) values
 *  ('name1', 'content1', now(), now())
 * ,('name2', 'content2', now(), now())
 * ,('name3', 'content3', now(), now())
 * ,('name4', 'content4', now(), now())
 * ,('name5', 'content5', now(), now())
 * ;
 * commit
 * ;
 *
 */
public class Blog implements Serializable{
    private static final long serialVersionUID = 5617011791638352165L;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
    private Long blog_id;
    private String name;
    private String content;
    private Date updated_datetime;
    private Date created_datetime;
    public Long getBlog_id() {
        return blog_id;
    }
    public void setBlog_id(Long blog_id) {
        this.blog_id = blog_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getUpdated_datetime() {
        return updated_datetime;
    }
    public void setUpdated_datetime(Date updated_datetime) {
        this.updated_datetime = updated_datetime;
    }
    public Date getCreated_datetime() {
        return created_datetime;
    }
    public void setCreated_datetime(Date created_datetime) {
        this.created_datetime = created_datetime;
    }
    @Override
    public String toString(){
//      return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("blog_id", blog_id)
                .append("name", name)
                .append("content", content)
                .append("updated_datetime", updated_datetime == null ? null : simpleDateFormat.format(updated_datetime))
                .append("created_datetime", created_datetime == null ? null : simpleDateFormat.format(created_datetime))
                .build();
    }
    
}