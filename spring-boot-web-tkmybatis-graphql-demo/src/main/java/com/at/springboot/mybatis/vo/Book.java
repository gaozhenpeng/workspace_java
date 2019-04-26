package com.at.springboot.mybatis.vo;

import lombok.Data;

@Data
public class Book {

    private Long id;

    private String createdTime;

    private String updatedTime;

    private String title;

    private Long authorId;

    private String isbn;

    private Integer pageCount;

}