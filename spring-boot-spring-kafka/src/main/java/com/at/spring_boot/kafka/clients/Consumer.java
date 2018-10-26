package com.at.spring_boot.kafka.clients;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.at.spring_boot.kafka.vo.CommMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Consumer{
    
    /**
     * <p>required to set <strong>spring.kafka.listener.type = BATCH</strong>
     * </p>
     * <p>when <strong>spring.kafka.listener.type = SINGLE</strong>,
     * this method will also be called, while it will complain the message body for ack is empty
     * </p>
     * @param records List of CusumerRecord
     * @param ack Acknowledgment
     */
    @KafkaListener(topics = {"${kafka.comm.topic}"}, clientIdPrefix = "my-client-id-batchmsg-prefix")
    public void listenBatchMessages(List<ConsumerRecord<String, CommMessage>> records, Acknowledgment ack) {
        if(records != null && records.size() > 0) {
            log.info("records.size(): '{}'", records.size());
            for(ConsumerRecord<String, CommMessage> cr : records) {
                    CommMessage value = cr.value();
                    log.info("batch ConsumerRecord = '{}'", cr);
                    log.info("batch value = '{}'", value);
            }
            ack.acknowledge();
        }
    }

    /**
     * <p>required to set <strong>spring.kafka.listener.type = SINGLE</strong>
     * </p>
     * <p>when <strong>spring.kafka.listener.type = BATCH</strong>, this method will be ignored
     * </p>
     * 
     * @param cr one ConsumerRecord
     * @param ack Acknowledgment
     */
    @KafkaListener(topics = {"${kafka.comm.topic}"}, clientIdPrefix = "my-client-id-onemsg-prefix")
    public void listenOneMessage(ConsumerRecord<String, String> cr,  Acknowledgment ack) {
        if(cr != null) {
            String value = cr.value();
            log.info("one ConsumerRecord = '{}'", cr);
            log.info("one value = '{}'", value);
        }
        ack.acknowledge();
    }
}
