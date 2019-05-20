package com.at.springboot.web.ctrl;

import java.io.IOException;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.at.springboot.mybatis.dto.GeoipDto;
import com.at.springboot.mybatis.vo.Geoip;
import com.at.springboot.web.svr.GeoipMmdbService;
import com.at.springboot.web.svr.GeoipRmdbService;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;

@Slf4j
@RestController
@RequestMapping("/geoip")
public class GeoipController {
    @Autowired
    private MapperFacade orikaMapper;
    @Autowired
    private GeoipRmdbService geoipRmdbService;
    @Autowired
    private GeoipMmdbService geoipMmdbService;

    @RequestMapping("/rmdb/q")
    public Geoip rmdbq(@RequestParam(required=true) String ip) throws UnknownHostException {
        log.info("ip: '{}'", ip);
        
        GeoipDto geoipDto = geoipRmdbService.queryLocationByIp(ip);
        return orikaMapper.map(geoipDto, Geoip.class);
    }

    @RequestMapping("/rmdb/qs")
    public Geoip rmdbqs(@RequestParam(required=true) String ip) throws UnknownHostException {
        log.info("ip: '{}'", ip);
        
        GeoipDto geoipDto = geoipRmdbService.queryLocationByIpSpatial(ip);
        return orikaMapper.map(geoipDto, Geoip.class);
    }

    @RequestMapping("/mmdb/q")
    public Geoip mmdbq(@RequestParam(required=true) String ip) throws IOException, GeoIp2Exception {
        log.info("ip: '{}'", ip);
        
        GeoipDto geoipDto = geoipMmdbService.queryLocationByIp(ip);
        return orikaMapper.map(geoipDto, Geoip.class);
    }
}
