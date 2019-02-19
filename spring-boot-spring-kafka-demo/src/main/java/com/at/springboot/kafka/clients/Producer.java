package com.at.springboot.kafka.clients;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.at.springboot.kafka.config.KafkaCommProperties;
import com.at.springboot.kafka.vo.CommMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Producer implements CommandLineRunner{
    private static final boolean IS_SEND_SYNC = true;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    private KafkaTemplate<String, CommMessage> kafkaTemplate;
    @Autowired
    private KafkaCommProperties kafkaCommProperties;
    @Override
    public void run(String... args) throws Exception {
        for(int i = 0 ; i < 100 ; i++) {
            CommMessage commMessage = new CommMessage();
            commMessage.setId(System.currentTimeMillis());
            commMessage.setMsg(UUID.randomUUID().toString());
            commMessage.setSendTime(new Date());
            String stringMsg = OBJECT_MAPPER.writeValueAsString(commMessage);
            log.info("sending messsage: '{}'", stringMsg);
            sendMessageWithTransWise(commMessage);
        }
    }
    private void sendMessageWithTransWise(CommMessage commMessage) {
        if(kafkaTemplate.isTransactional()) {
            kafkaTemplate.executeInTransaction(kt -> {
                sendMessage(kt, commMessage);
                boolean returnObj = true;
                return returnObj;
            });
        }else {
            sendMessage(kafkaTemplate, commMessage);
        }
    }
    private void sendMessage(KafkaOperations<String, CommMessage> kt, CommMessage commMessage) {
        if(IS_SEND_SYNC) {
            // asynchronous
            kt.send(kafkaCommProperties.getS2p().getTopic(), commMessage);
            kt.send(kafkaCommProperties.getP2s().getTopic(), commMessage);
        }else {
            // synchronous
            try {
                kt.send(kafkaCommProperties.getS2p().getTopic(), commMessage).get(1, TimeUnit.SECONDS);
                kt.send(kafkaCommProperties.getP2s().getTopic(), commMessage).get(1, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                log.error("Shit happened.", e);
                throw new RuntimeException(e);
            }
        }
    }
}
