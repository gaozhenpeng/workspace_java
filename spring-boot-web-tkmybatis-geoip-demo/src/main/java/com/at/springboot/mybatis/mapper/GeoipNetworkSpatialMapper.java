package com.at.springboot.mybatis.mapper;

import com.at.springboot.mybatis.po.GeoipNetworkSpatial;

import tk.mybatis.mapper.common.Mapper;

public interface GeoipNetworkSpatialMapper extends Mapper<GeoipNetworkSpatial> {
    public GeoipNetworkSpatial selectByUnsignedIntegerIP(Long ip);
}