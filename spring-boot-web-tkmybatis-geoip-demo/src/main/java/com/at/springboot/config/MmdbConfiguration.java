package com.at.springboot.config;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.maxmind.geoip2.DatabaseReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MmdbConfiguration {

    @Value("#{'${app.mmdb.fileClassPath}'.trim()}")
    private String mmdbFileName;
    
    @Bean
    public DatabaseReader mmdbReader() throws IOException {
        log.info("initializing mmdb DatabaseReader from '{}'", mmdbFileName);
        
        File mmdbFile = new ClassPathResource(mmdbFileName).getFile();
        
        return new DatabaseReader.Builder(mmdbFile).build();
    }

}
