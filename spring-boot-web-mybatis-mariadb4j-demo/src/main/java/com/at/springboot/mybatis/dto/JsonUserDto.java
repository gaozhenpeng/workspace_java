package com.at.springboot.mybatis.dto;

import java.io.Serializable;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class JsonUserDto implements Serializable {
    private String id;
    private String userName;
    private String lastLoginInfo;
}
