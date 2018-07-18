package com.at.spring.mybatis.annotation.vo;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * <pre><code>
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
 * </code></pre>
 */
@SuppressWarnings("serial")
@Data
public class Blog implements Serializable{
    private Long blogId;
    private String name;
    private String content;
    private Date updatedDatetime;
    private Date createdDatetime;
}
