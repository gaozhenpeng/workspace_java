package com.at.lombok.vo;

import java.util.Date;

import lombok.Data;
import lombok.NonNull;

@Data
public class User {
    /** auto required in constructor */
    @NonNull
    private String name;
    /** auto required in constructor */
    @NonNull
    private Gender gender;
    private Date birthday;
}
