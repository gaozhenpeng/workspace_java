package com.at.springboot.mybatis.po;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "`book`")
public class Book {
    /**
     * 主键
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "`created_time`")
    private Date createdTime;

    /**
     * 更新时间
     */
    @Column(name = "`updated_time`")
    private Date updatedTime;

    /**
     * 标题
     */
    @Column(name = "`title`")
    private String title;

    @Column(name = "`author_id`")
    private Long authorId;

    @Column(name = "`isbn`")
    private String isbn;

    @Column(name = "`page_count`")
    private Integer pageCount;

    public static final String ID = "id";

    public static final String DB_ID = "id";

    public static final String CREATED_TIME = "createdTime";

    public static final String DB_CREATED_TIME = "created_time";

    public static final String UPDATED_TIME = "updatedTime";

    public static final String DB_UPDATED_TIME = "updated_time";

    public static final String TITLE = "title";

    public static final String DB_TITLE = "title";

    public static final String AUTHOR_ID = "authorId";

    public static final String DB_AUTHOR_ID = "author_id";

    public static final String ISBN = "isbn";

    public static final String DB_ISBN = "isbn";

    public static final String PAGE_COUNT = "pageCount";

    public static final String DB_PAGE_COUNT = "page_count";
}