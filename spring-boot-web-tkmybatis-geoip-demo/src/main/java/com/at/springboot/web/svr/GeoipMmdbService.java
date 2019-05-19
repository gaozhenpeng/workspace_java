package com.at.springboot.web.svr;

import java.io.IOException;
import java.net.InetAddress;
import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.at.springboot.mybatis.dto.GeoipDto;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GeoipMmdbService {
    @Autowired
    private DatabaseReader mmdbReader = null;
    
    public GeoipDto queryLocationByIp(String ip) throws IOException, GeoIp2Exception {
        log.info("ip: '{}'", ip);
        Assert.notNull(ip, "ip should not be null");
        
        
        InetAddress ipAddress = InetAddress.getByName(ip);

        CityResponse response = mmdbReader.city(ipAddress);
        Location location = response.getLocation();
        Country country = response.getCountry();
        City city = response.getCity();
        
        GeoipDto geoipDto = new GeoipDto();

        // assemble geoip dto
//        geoipDto.setCity(city.getName()); // Guangzhou
        geoipDto.setCity(city.getNames().get("zh-CN")); // 广州
        geoipDto.setCountryIsoCode(country.getIsoCode());
//        geoipDto.setCountryName(country.getName()); // China
        geoipDto.setCountryName(country.getNames().get("zh-CN")); // 中国
        geoipDto.setIp(ip);
        geoipDto.setLatitude(MessageFormat.format("{0,number,0.000000}", location.getLatitude()));
        geoipDto.setLongitude(MessageFormat.format("{0,number,0.000000}", location.getLongitude()));
        
        
        log.info("geoipDto: '{}'", geoipDto);
        return geoipDto;
    }
}
