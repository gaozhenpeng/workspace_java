package com.at.kafka_simple;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import lombok.extern.slf4j.Slf4j;

/**
 * WARNING: Async is NOT SAFE for business logic
 * 
 * WARNING: the async-finally-sync model should ONLY be applied for the sampling purpose
 */
@Slf4j
public class SimpleConsumerCommitAsync {
    public static void main(String[] args) {
        log.info("program starting...");
        
        Properties props = SimpleConsumer.getConsumerProperties();
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "my-client-id-consumer-async");
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
                    // commit asynchronously, NO RETRY for the possibility of the bigger offset had commited 
                    log.debug("commiting asynchronously...");
                    kafkaConsumer.commitAsync();
                }else {
                    emptyCount ++;
                    log.info("empty for '{}' times", emptyCount);
                }
            }
        }finally {
            try {
                // commit synchronously, retry unless unrecoverable error happened
                log.info("commiting synchronously...");
                kafkaConsumer.commitSync();
                log.info("commited");
            }catch(CommitFailedException e) {
                log.error("Commit failed.", e);
            }finally {
                kafkaConsumer.close(60, TimeUnit.SECONDS);
            }
        }
    }
}
