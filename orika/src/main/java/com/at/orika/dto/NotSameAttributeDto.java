package com.at.orika.dto;

import java.io.Serializable;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class NotSameAttributeDto implements Serializable {
    private String birthDate;
    private String dateOnBoard;
    private String name;
    private String id;
    private String userId;
    private String noMatchedNameFromA;
}
