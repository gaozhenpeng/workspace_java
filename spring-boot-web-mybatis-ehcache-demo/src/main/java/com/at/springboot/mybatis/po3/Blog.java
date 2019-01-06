package com.at.springboot.mybatis.po3;

import java.util.Date;

/**
 * <pre>
 * <code>
 * USE test;
 * DROP TABLE IF EXISTS `blog`;
 * CREATE TABLE `blog` (
 *   `blog_id` bigint unsigned NOT NULL AUTO_INCREMENT,
 *   `name` varchar(64) DEFAULT NULL,
 *   `content` varchar(512) DEFAULT NULL,
 *   `created_datetime` datetime DEFAULT null,
 *   `updated_datetime` datetime DEFAULT null,
 *   PRIMARY KEY (`blog_id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
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
 * </code>
 * </pre>
 *
 */

public class Blog {
    private Long blogId;

    private String name;

    private String content;

    private Date createdDatetime;

    private Date updatedDatetime;

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Date createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public Date getUpdatedDatetime() {
        return updatedDatetime;
    }

    public void setUpdatedDatetime(Date updatedDatetime) {
        this.updatedDatetime = updatedDatetime;
    }
}