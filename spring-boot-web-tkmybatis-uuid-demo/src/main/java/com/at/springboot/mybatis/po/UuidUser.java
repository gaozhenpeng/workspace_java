package com.at.springboot.mybatis.po;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "`uuid_user`")
public class UuidUser {
    @Id
    @Column(name = "`bid`")
    private byte[] bid;

    @Column(name = "`cid`")
    private String cid;

    @Column(name = "`name`")
    private String name;

    @Column(name = "`created_datetime`")
    private LocalDateTime createdDatetime;

    @Column(name = "`updated_datetime`")
    private LocalDateTime updatedDatetime;

    public static final String BID = "bid";

    public static final String DB_BID = "bid";

    public static final String CID = "cid";

    public static final String DB_CID = "cid";

    public static final String NAME = "name";

    public static final String DB_NAME = "name";

    public static final String CREATED_DATETIME = "createdDatetime";

    public static final String DB_CREATED_DATETIME = "created_datetime";

    public static final String UPDATED_DATETIME = "updatedDatetime";

    public static final String DB_UPDATED_DATETIME = "updated_datetime";
}