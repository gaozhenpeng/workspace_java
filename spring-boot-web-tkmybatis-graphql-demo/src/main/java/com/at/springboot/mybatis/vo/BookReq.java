package com.at.springboot.mybatis.vo;

import lombok.Data;

@Data
public class BookReq {
    private String title;

    private String isbn;

    private int pageCount;

    private long authorId;
}
