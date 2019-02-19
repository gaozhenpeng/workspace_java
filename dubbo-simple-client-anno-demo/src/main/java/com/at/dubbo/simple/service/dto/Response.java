package com.at.dubbo.simple.service.dto;

@SuppressWarnings("serial")
public class Response extends BaseVO{
    private String name="ResponseName";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
