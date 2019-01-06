package com.at.springboot.jwt.vo;

import lombok.Data;

@Data
public class LoginResponse {
    private boolean ok;
    private String msg;
    private String jws;
}
