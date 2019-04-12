package com.at.springboot.mybatis.po;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "`author`")
public class Author {
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
     * firstName
     */
    @Column(name = "`first_name`")
    private String firstName;

    /**
     * lastName
     */
    @Column(name = "`last_name`")
    private String lastName;

    public static final String ID = "id";

    public static final String DB_ID = "id";

    public static final String CREATED_TIME = "createdTime";

    public static final String DB_CREATED_TIME = "created_time";

    public static final String UPDATED_TIME = "updatedTime";

    public static final String DB_UPDATED_TIME = "updated_time";

    public static final String FIRST_NAME = "firstName";

    public static final String DB_FIRST_NAME = "first_name";

    public static final String LAST_NAME = "lastName";

    public static final String DB_LAST_NAME = "last_name";
}