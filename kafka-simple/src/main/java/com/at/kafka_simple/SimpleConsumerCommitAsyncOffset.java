package com.at.kafka_simple;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import lombok.extern.slf4j.Slf4j;

/**
 * WARNING: Async is NOT SAFE for business logic
 * 
 * WARNING: the async-finally-sync model should ONLY be applied for the sampling purpose
 */
@Slf4j
public class SimpleConsumerCommitAsyncOffset {
    public static void main(String[] args) {
        log.info("program starting...");
        
        Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
        
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "my-client-id-consumer-async-offset");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group-id");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
//      props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 5000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        log.info("connecting to kafka...");
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(props);
        log.info("connected");
        
        log.info("subscribing 'my-topic'...");
        //kafkaConsumer.subscribe("my-.*");
        kafkaConsumer.subscribe(Collections.singletonList(SimpleProducer.getTopicName()), new ConsumerRebalanceListener() {

            /** before REBALANCE starts, after CONSUMER stopped reading */
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            }

            /** after REBALANCE completed, before CONSUMER starts reading */
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                log.info("Lost partitions in rebalance. Commiting current offsets '{}'...", currentOffsets);
                kafkaConsumer.commitSync(currentOffsets);
                log.info("Lost partitions in rebalance. Commiting current offsets: Done. ");
            }
        });
        log.info("subscribed");
        
        
        try {
            while(true) {
                log.info("polling...");
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(1000);
                if(consumerRecords != null && !consumerRecords.isEmpty()) {
                    long i = 0;
                    for(ConsumerRecord<String, String> cr : consumerRecords) {
                        log.info("topic: '{}', partition: '{}', offset: '{}', key: '{}', value: '{}'"
                                ,cr.topic()
                                ,cr.partition()
                                ,cr.offset()
                                ,cr.key()
                                ,cr.value());

                        currentOffsets.put(
                                new TopicPartition(cr.topic(), cr.partition())
                                , new OffsetAndMetadata(cr.offset()+1, "no metadata"));

                        if(i % 10 == 0) {
                            // commit asynchronously, NO RETRY for the possibility of the bigger offset had commited 
                            log.info("commiting asynchronously...");
                            OffsetCommitCallback offsetCommitCallback = null;
                            kafkaConsumer.commitAsync(currentOffsets, offsetCommitCallback);
                        }
                    }
                }else {
                    log.info("empty");
                }
            }
        }catch(WakeupException e) {
            log.info("ignorable WakeupException happened");
        }catch(Exception e) {
            log.error("Shit happened.", e);
        }finally {
            try {
                // commit synchronously, retry unless unrecoverable error happened
                log.info("commiting synchronously with currentOffsets '{}'...", currentOffsets);
                kafkaConsumer.commitSync(currentOffsets);
                log.info("commited");
            }catch(CommitFailedException e) {
                log.error("Commit failed.", e);
            }finally {
                kafkaConsumer.close(60, TimeUnit.SECONDS);
            }
        }
    }
}
