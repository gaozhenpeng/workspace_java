package com.at.springboot.web.svr;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.at.springboot.mybatis.dto.GeoipDto;
import com.at.springboot.mybatis.mapper.GeoipLocationMapper;
import com.at.springboot.mybatis.mapper.GeoipNetworkMapper;
import com.at.springboot.mybatis.mapper.GeoipNetworkSpatialMapper;
import com.at.springboot.mybatis.po.GeoipLocation;
import com.at.springboot.mybatis.po.GeoipNetwork;
import com.at.springboot.mybatis.po.GeoipNetworkSpatial;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

@Slf4j
@Service
public class GeoipRmdbService {

    @Autowired
    private GeoipNetworkMapper geoipNetworkMapper;
    @Autowired
    private GeoipNetworkSpatialMapper geoipNetworkSpatialMapper;
    @Autowired
    private GeoipLocationMapper geoipLocationMapper;
    
    public GeoipDto queryLocationByIp(String ip) throws UnknownHostException {
        log.info("ip: '{}'", ip);
        Assert.notNull(ip, "ip should not be null");
        
        InetAddress inetAddress = InetAddress.getByName(ip);
        
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.SIZE);
        byteBuffer.put(inetAddress.getAddress());
        byteBuffer.position(0);
        
        long ipLong = byteBuffer.getInt() & 0xFFFFFFFFL;
        
        GeoipDto geoipDto = new GeoipDto();
        
        // get network geoname_id
        Example networkExample = new Example(GeoipNetwork.class);
        networkExample
            .or()
                // NOTE: GeoipNetwork.NETWORK_LAST_INTEGER, not GeoipNetwork.DB_NETWORK_LAST_INTEGER
                .andLessThanOrEqualTo(GeoipNetwork.NETWORK_START_INTEGER, ipLong)
                .andGreaterThanOrEqualTo(GeoipNetwork.NETWORK_LAST_INTEGER, ipLong)
                ;
        GeoipNetwork geoipNetwork = geoipNetworkMapper.selectOneByExample(networkExample);
        if(geoipNetwork == null) {
            return geoipDto;
        }
        long networkGeonameId = geoipNetwork.getGeonameId();
        
        // query location info
        Example locationExample = new Example(GeoipLocation.class);
        locationExample
            .or()
                .andEqualTo(GeoipLocation.GEONAME_ID, networkGeonameId)
                ;
        GeoipLocation geoipLocation = geoipLocationMapper.selectOneByExample(locationExample);
        if(geoipLocation == null) {
            return geoipDto;
        }
        
        // assemble geoip dto
        geoipDto.setCity(geoipLocation.getCityName());
        geoipDto.setCountryIsoCode(geoipLocation.getCountryIsoCode());
        geoipDto.setCountryName(geoipLocation.getCountryName());
        geoipDto.setIp(ip);
        geoipDto.setLatitude(geoipNetwork.getLatitude().toPlainString());
        geoipDto.setLongitude(geoipNetwork.getLongitude().toPlainString());
        
        log.info("geoipDto: '{}'", geoipDto);
        return geoipDto;
    }

    public GeoipDto queryLocationByIpSpatial(String ip) throws UnknownHostException {
        log.info("ip: '{}'", ip);
        Assert.notNull(ip, "ip should not be null");
        
        InetAddress inetAddress = InetAddress.getByName(ip);
        
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.SIZE);
        byteBuffer.put(inetAddress.getAddress());
        byteBuffer.position(0);
        
        long ipLong = byteBuffer.getInt() & 0xFFFFFFFFL;
        
        GeoipDto geoipDto = new GeoipDto();
        
        // get network geoname_id
        GeoipNetworkSpatial geoipNetworkSpatial = geoipNetworkSpatialMapper.selectByUnsignedIntegerIP(Long.valueOf(ipLong));
        if(geoipNetworkSpatial == null) {
            return geoipDto;
        }
        long networkGeonameId = geoipNetworkSpatial.getGeonameId();
        
        // query location info
        Example locationExample = new Example(GeoipLocation.class);
        locationExample
            .or()
                .andEqualTo(GeoipLocation.GEONAME_ID, networkGeonameId)
                ;
        GeoipLocation geoipLocation = geoipLocationMapper.selectOneByExample(locationExample);
        if(geoipLocation == null) {
            return geoipDto;
        }
        
        // assemble geoip dto
        geoipDto.setCity(geoipLocation.getCityName());
        geoipDto.setCountryIsoCode(geoipLocation.getCountryIsoCode());
        geoipDto.setCountryName(geoipLocation.getCountryName());
        geoipDto.setIp(ip);
        geoipDto.setLatitude(geoipNetworkSpatial.getLatitude().toPlainString());
        geoipDto.setLongitude(geoipNetworkSpatial.getLongitude().toPlainString());
        
        log.info("geoipDto: '{}'", geoipDto);
        return geoipDto;
    }
}
