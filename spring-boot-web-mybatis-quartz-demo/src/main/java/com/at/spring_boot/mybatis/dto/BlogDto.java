package com.at.spring_boot.mybatis.dto;

import java.io.Serializable;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class BlogDto implements Serializable{
    private String blogId;
    private String name;
    private String content;
    private String updatedDatetime;
    private String createdDatetime;
}