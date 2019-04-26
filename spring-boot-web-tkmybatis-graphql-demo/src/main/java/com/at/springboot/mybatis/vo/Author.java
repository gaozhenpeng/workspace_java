package com.at.springboot.mybatis.vo;

import lombok.Data;

@Data
public class Author {

    private Long id;

    private String createdTime;

    private String updatedTime;

    /**
     * firstName
     */
    private String firstName;

    /**
     * lastName
     */
    private String lastName;
    
}