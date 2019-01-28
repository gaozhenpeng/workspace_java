package com.at.springboot.shiro.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class JwtToken implements AuthenticationToken {
    private String jwt;

    public JwtToken(String jwt) {
        this.jwt = jwt;
    }
    
    @Override
    public Object getPrincipal() {
        return jwt;
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }

}
