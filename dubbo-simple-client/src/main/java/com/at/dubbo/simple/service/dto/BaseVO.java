package com.at.dubbo.simple.service.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BaseVO implements Serializable {
    private String name="baseName";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
