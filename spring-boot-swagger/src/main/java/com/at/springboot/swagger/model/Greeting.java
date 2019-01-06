package com.at.springboot.swagger.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Greeting {

    @ApiModelProperty(notes = "Provided user name", required = true)
    private String name;

    @ApiModelProperty(notes = "The system generated greeting message" , readOnly = true)
    private String message;
}
