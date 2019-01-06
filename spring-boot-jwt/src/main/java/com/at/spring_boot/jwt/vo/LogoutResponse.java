package com.at.spring_boot.jwt.vo;

import lombok.Data;

@Data
public class LogoutResponse {
    private boolean ok;
    private String msg;
}
