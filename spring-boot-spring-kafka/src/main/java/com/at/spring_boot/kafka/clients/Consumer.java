package com.at.spring_boot.kafka.clients;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.at.spring_boot.kafka.config.KafkaCommProperties;
import com.at.spring_boot.kafka.vo.CommMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Consumer{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    private KafkaTemplate<String, CommMessage> kafkaTemplate;
    @Autowired
    private KafkaCommProperties kafkaCommProperties;
    @Autowired
    private KafkaProperties kafkaProperties;
    /**
     * <p>required to set <strong>spring.kafka.listener.type = BATCH</strong>
     * </p>
     * <p>when <strong>spring.kafka.listener.type = SINGLE</strong>,
     * this method will also be called, while it will complain the message body for ack is empty
     * </p>
     * @param records List of CusumerRecord
     * @param ack Acknowledgment
     */
    @KafkaListener(topics = {"${kafka.comm.p2s.topic}"}, clientIdPrefix = "my-client-id-batchmsg-comsumerrecord-prefix", groupId="my-group-id-batchmsg-comsumerrecord")
    public void listenBatchMessagesConsumerRecord(List<ConsumerRecord<String, CommMessage>> records, Acknowledgment ack) {
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
    @KafkaListener(topics = {"${kafka.comm.s2p.topic}"}, clientIdPrefix = "my-client-id-batchmsg-commmessage-prefix", groupId="my-group-id-batchmsg-commmessage")
    public void listenBatchMessagesCommMessage(
            @Payload List<CommMessage> commMessages
            ,@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys
            ,@Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions
            ,@Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics
            ,@Header(KafkaHeaders.RECEIVED_TIMESTAMP) List<Integer> tss
            ,@Header(KafkaHeaders.OFFSET) List<Integer> offsets
            ,Acknowledgment ack) {
        if(commMessages != null && commMessages.size() > 0) {
            for(int i = 0 ; i < commMessages.size() ; i++) {
                log.info("commMessage = '{}'", commMessages.get(i));
                log.info("key = '{}'", keys.get(i));
                log.info("partition = '{}'", partitions.get(i));
                log.info("topic = '{}'", topics.get(i));
                log.info("timestamp = '{}'", tss.get(i));
                log.info("offset = '{}'", offsets.get(i));
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
     * @throws TimeoutException 
     * @throws ExecutionException 
     * @throws InterruptedException 
     * @throws JsonProcessingException 
     */
    @KafkaListener(topics = {"${kafka.comm.s2p.topic}"}, clientIdPrefix = "my-client-id-onemsg-prefix", groupId="my-group-id-onemsg")
    public void listenOneMessage(
                @Payload CommMessage commMessage
                ,@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) Integer key
                ,@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition
                ,@Header(KafkaHeaders.RECEIVED_TOPIC) String topic
                ,@Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
                ,@Header(KafkaHeaders.OFFSET) long offset
                ,Acknowledgment ack
            ) throws InterruptedException, ExecutionException, TimeoutException, JsonProcessingException {
        if(commMessage != null) {
            log.info("commMessage = '{}'", commMessage);
            log.info("key = '{}'", key);
            log.info("partition = '{}'", partition);
            log.info("topic = '{}'", topic);
            log.info("timestamp = '{}'", ts);
            log.info("offset = '{}'", offset);
            
            CommMessage newCommMessage = new CommMessage();
            newCommMessage.setId(System.currentTimeMillis());
            newCommMessage.setMsg(OBJECT_MAPPER.writeValueAsString(commMessage));
            newCommMessage.setSendTime(new Date());
            
            ProducerRecord<String, CommMessage> producerRecord = new ProducerRecord<>(kafkaCommProperties.getP2s().getTopic(), newCommMessage);
            log.info("sending producer record, '{}'...", producerRecord);
            kafkaTemplate.send(producerRecord).get(1, TimeUnit.SECONDS);
            log.info("sent producer record");
              
            
            
            
            ack.acknowledge();
        }
    }
    
    
    
    
    
    
    
    

    
//    /**
//     * <p>enable transaction for producer
//     * </p>
//     * <p>Note: the topics has been changed to '<strong>${kafka.comm.p2s.topic}</strong>'</p>
//     * @param records List of CusumerRecord
//     */
//    @Transactional
//    @KafkaListener(topics = {"${kafka.comm.p2s.topic}"}, clientIdPrefix = "my-client-id-batchmsg-trans-prefix", groupId="my-group-id-batchmsg-trans")
//    public void listenBatchMessagesForTrans(List<ConsumerRecord<String, CommMessage>> records, Acknowledgment ack) {
//        if(records != null && records.size() > 0) {
//            log.info("records.size(): '{}'", records.size());
//            Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
//            for(ConsumerRecord<String, CommMessage> cr : records) {
//                log.info("dealing consumer record: '{}'", cr);
//                
//                ProducerRecord<String, CommMessage> producerRecord = new ProducerRecord<>(kafkaCommProperties.getS2p().getTopic(), cr.value());
//                log.info("sending producer record, '{}'...", producerRecord);
//                kafkaTemplate.send(producerRecord);
//                log.info("sent producer record");
//                
//                currentOffsets.put(
//                        new TopicPartition(cr.topic(), cr.partition())
//                        , new OffsetAndMetadata(cr.offset()+1, "no metadata"));
//            }
//            log.info("sending offsets to transaction...");
//            kafkaTemplate.sendOffsetsToTransaction(currentOffsets, "my-group-id-batchmsg-trans");
//            log.info("sent offsets to transaction");
//        }
//    }
//
//    
//    /**
//     * <p>manual transaction for producer
//     * </p>
//     * @param records List of CusumerRecord
//     */
//    @KafkaListener(topics = {"${kafka.comm.p2s.topic}"}, clientIdPrefix = "my-client-id-batchmsg-manual-trans-prefix", groupId="my-group-id-batchmsg-manual-trans")
//    public void listenBatchMessagesForManualTrans(List<ConsumerRecord<String, CommMessage>> records, Acknowledgment ack) {
//        if(records != null && records.size() > 0) {
//            log.info("records.size(): '{}'", records.size());
//            
//            Map<String, Object> producerProps = kafkaProperties.buildProducerProperties();
//            producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, "my-client-id-trans-producer-" + Thread.currentThread().getName());
//            producerProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transaction-id-" + Thread.currentThread().getName());
//            KafkaProducer<String, CommMessage> kafkaProducer = new KafkaProducer<String, CommMessage>(producerProps);
//            
//            try {
//                log.info("initializing transaction...");
//                kafkaProducer.initTransactions();
//                log.info("initialized transaction");
//
//                log.info("beginning transaction...");
//                kafkaProducer.beginTransaction();
//                log.info("began transaction");
//                
//                Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
//                for(ConsumerRecord<String, CommMessage> cr : records) {
//                    
//                    log.info("dealing consumer record: '{}'", cr);
//                    
//                    ProducerRecord<String, CommMessage> producerRecord = new ProducerRecord<>(kafkaCommProperties.getS2p().getTopic(), cr.value());
//                    log.info("sending producer record, '{}'...", producerRecord);
//                    kafkaProducer.send(producerRecord);
//                    log.info("sent producer record");
//                    
//                    currentOffsets.put(
//                            new TopicPartition(cr.topic(), cr.partition())
//                            , new OffsetAndMetadata(cr.offset()+1, "no metadata"));
//                }
//                log.info("sending offsets to transaction...");
//                kafkaProducer.sendOffsetsToTransaction(currentOffsets, "my-group-id-batchmsg-manual-trans");
//                log.info("sent offsets to transaction");
//                
//                log.info("committing transaction...");
//                kafkaProducer.commitTransaction();
//                log.info("committed transaction");
//            }catch(ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
//                log.error("Shit happened.", e);
//                kafkaProducer.abortTransaction();
//            }finally {
//                kafkaProducer.close(60, TimeUnit.SECONDS);
//            }
//        }
//    }

}
