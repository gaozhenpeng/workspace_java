package com.at.spring.dozer.po;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class NotSameAttribute implements Serializable {
    private Date birthDate;
    private Date dateOnBoard;
    private String name;
    private Integer id;
    private Long userId;
    private String noMatchedNameFromB;
}
