package com.at.kafka_simple;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleTransaction {
    /**
     * kafka-topics --zookeeper localhost:2181/kafka --create --topic my-transaction-topic --replication-factor 1 --partitions 3
     * @return
     */
    public static String getTransactionTopicName() {
        return "my-transaction-topic";
    }
    public static void main(String[] args) {

        log.info("program starting...");
        

        log.info("initializing kafka consumer...");
        Properties consumerProps = SimpleConsumer.getConsumerProperties();
        consumerProps.put(ConsumerConfig.CLIENT_ID_CONFIG, "my-client-id-trans-consumer");
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(consumerProps);
        
        log.info("subscribing '{}'...", SimpleProducer.getTopicName());
        //kafkaConsumer.subscribe("my-.*");
        kafkaConsumer.subscribe(Collections.singletonList(SimpleProducer.getTopicName()));
        log.info("subscribed");

        log.info("initializing kafka producer...");
        Properties producerProps = SimpleProducer.getProducerProperties();
        producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, "my-client-id-trans-producer");
        producerProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transaction-id");
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(producerProps);

        // init transaction, prepare a clean environment for the transaction
        kafkaProducer.initTransactions();
        
        Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
        try {
            int emptyCount = 0;
            while(emptyCount < SimpleConsumer.stopAfterCountEmpty()) {
                
                currentOffsets.clear();
                
                log.info("polling 1s...");
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(1000);
                if(consumerRecords != null && !consumerRecords.isEmpty()) {
                    emptyCount = 0;
                    try {
                        // begin transaction
                        kafkaProducer.beginTransaction();
                        
                        for(ConsumerRecord<String, String> cr : consumerRecords) {
                            log.info("dealing consumer record: '{}'", cr);
                            
                            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(SimpleTransaction.getTransactionTopicName(), cr.value());
                            log.info("sending producer record, '{}'...", producerRecord);
                            kafkaProducer.send(producerRecord);
                            log.info("sent producer record");
                            
                            currentOffsets.put(
                                    new TopicPartition(cr.topic(), cr.partition())
                                    , new OffsetAndMetadata(cr.offset()+1, "no metadata"));
    
                            log.info("sending offsets to transaction...");
                            kafkaProducer.sendOffsetsToTransaction(currentOffsets, SimpleConsumer.getConsumerGroupId());
                            log.info("sent offsets to transaction");
                        }
                        log.info("committing transaction...");
                        kafkaProducer.commitTransaction();
                        log.info("committed transaction");
                    }catch(ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
                        log.error("Shit happened.", e);
                        kafkaProducer.abortTransaction();
                    }
                }else {
                    emptyCount++;
                    log.info("empty for '{}' times", emptyCount);
                }
            }
        }finally {
            kafkaProducer.close(60, TimeUnit.SECONDS);
            kafkaConsumer.close(60, TimeUnit.SECONDS);
        }
        
    }
}
