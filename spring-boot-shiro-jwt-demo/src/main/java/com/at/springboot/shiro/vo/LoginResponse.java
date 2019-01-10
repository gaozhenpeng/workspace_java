package com.at.springboot.shiro.vo;

import lombok.Data;

@Data
public class LoginResponse {
    private boolean ok;
    private String msg;
    private String jws;
}
