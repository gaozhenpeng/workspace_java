package com.at.springboot.jwt.vo;

import lombok.Data;

@Data
public class LogoutResponse {
    private boolean ok;
    private String msg;
}
