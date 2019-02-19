package com.at.dubbo.simple.service.dto;

@SuppressWarnings("serial")
public class Request  extends BaseVO{
    private String name="RequestName";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
