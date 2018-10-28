package com.at.spring_boot.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix="kafka.comm")
@Data
public class KafkaCommProperties {
    private P2s p2s;
    private S2p s2p;
    @Data
    public static class P2s {
        private String topic;
        private String producerTransIdPrefix;
    }
    @Data
    public static class S2p {
        private String topic;
        private String producerTransIdPrefix;
    }
}
