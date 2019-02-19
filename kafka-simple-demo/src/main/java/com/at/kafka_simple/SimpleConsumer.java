package com.at.kafka_simple;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleConsumer {
    public static int stopAfterCountEmpty() {
        return 60;
    }
    public static String getConsumerGroupId() {
        return "my-group-id";
    }
    public static Properties getConsumerProperties() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, SimpleConsumer.getConsumerGroupId());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 3000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        return props;
    }
    public static void main(String[] args) {
        log.info("program starting...");

        Properties props = SimpleConsumer.getConsumerProperties();
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "my-client-id-consumer");
        
        log.info("connecting to kafka...");
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(props);
        log.info("connected");

        log.info("subscribing '{}'...", SimpleProducer.getTopicName());
        //kafkaConsumer.subscribe("my-.*");
        kafkaConsumer.subscribe(Collections.singletonList(SimpleProducer.getTopicName()));
        log.info("subscribed");
        
        
        try {
            int emptyCount = 0;
            while(emptyCount < SimpleConsumer.stopAfterCountEmpty()) {
                log.info("polling...");
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(1000);
                if(consumerRecords != null && !consumerRecords.isEmpty()) {
                    emptyCount = 0;
                    for(ConsumerRecord<String, String> cr : consumerRecords) {
                        log.info("topic: '{}', partition: '{}', offset: '{}', key: '{}', value: '{}'"
                                ,cr.topic()
                                ,cr.partition()
                                ,cr.offset()
                                ,cr.key()
                                ,cr.value());
                    }
                }else {
                    emptyCount++;
                    log.info("empty for '{}' times", emptyCount);
                }
            }
        }finally {
            kafkaConsumer.close(60, TimeUnit.SECONDS);
        }
    }
}
