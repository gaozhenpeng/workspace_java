package com.at.springboot.mybatis.dto;

import lombok.Data;

@Data
public class GeoipDto {
    private String ip;
    private String city;
    private String countryName;
    private String countryIsoCode;
    private String latitude;
    private String longitude;
}