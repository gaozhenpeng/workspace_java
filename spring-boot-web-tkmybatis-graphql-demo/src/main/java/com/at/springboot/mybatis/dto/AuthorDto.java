package com.at.springboot.mybatis.dto;

import java.util.Date;

import lombok.Data;

@Data
public class AuthorDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * firstName
     */
    private String firstName;

    /**
     * lastName
     */
    private String lastName;
    
}