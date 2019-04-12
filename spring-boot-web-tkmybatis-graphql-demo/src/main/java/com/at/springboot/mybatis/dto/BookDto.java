package com.at.springboot.mybatis.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BookDto {
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
     * 标题
     */
    private String title;

    private Long authorId;

    private String isbn;

    private Integer pageCount;

}