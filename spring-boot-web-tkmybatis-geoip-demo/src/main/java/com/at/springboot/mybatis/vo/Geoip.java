package com.at.springboot.mybatis.vo;

import lombok.Data;

@Data
public class Geoip {
    private String ip;
    private String city;
    private String countryName;
    private String countryIsoCode;
    private String latitude;
    private String longitude;
}