package com.at.kafka_simple;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleProducer {
    public static String getTopicName() {
        return "my-topic";
    }
    public static void main(String[] args) throws InterruptedException {
        log.info("program starting...");
        
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "my-client-id-producer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");


        log.info("connecting to kafka...");
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(props);
        log.info("connected");
        
        
        try {
            for(long i = 0 ; i < 1000000L ; i++) {
                log.info("producing...");
                String msgKey = "my-key-" + i;
                String msgValue = "my-value-" + i;
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(getTopicName(), msgKey, msgValue);
                log.info("sending record asynchronously. topic: '{}', key: '{}', value: '{}'", getTopicName(), msgKey, msgValue);
                kafkaProducer.send(producerRecord);
                
                if(i % 1000 == 0) {
                    log.info("flushing synchronously...");
                    kafkaProducer.flush();
                    log.info("flushed");
                }
            }
        }finally {
            kafkaProducer.close(60, TimeUnit.SECONDS);
        }
        log.info("done");
    }
}
