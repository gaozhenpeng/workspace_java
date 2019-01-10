package com.at.springboot.shiro.controller.jwt;

import org.apache.shiro.authc.AuthenticationToken;

import lombok.Getter;

@SuppressWarnings("serial")
public class JwtToken implements AuthenticationToken {
    @Getter
    private String jws;

    public JwtToken(String jws) {
        this.jws = jws;
    }
    
    @Override
    public Object getPrincipal() {
        return jws;
    }

    @Override
    public Object getCredentials() {
        return jws;
    }

}
