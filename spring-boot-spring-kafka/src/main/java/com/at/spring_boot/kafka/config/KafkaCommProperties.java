package com.at.spring_boot.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix="kafka.comm")
@Data
public class KafkaCommProperties {
    private String topic;
}
