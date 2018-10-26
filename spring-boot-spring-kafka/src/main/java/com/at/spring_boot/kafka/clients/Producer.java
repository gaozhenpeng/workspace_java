package com.at.spring_boot.kafka.clients;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.at.spring_boot.kafka.config.KafkaCommProperties;
import com.at.spring_boot.kafka.vo.CommMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Producer implements CommandLineRunner{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    private KafkaTemplate<String, CommMessage> kafkaTemplate;
    @Autowired
    private KafkaCommProperties kafkaCommProperties;


    @Override
    public void run(String... args) throws Exception {
        try {
            for(int i = 0 ; i < 100 ; i++) {
                CommMessage commMessage = new CommMessage();
                commMessage.setId(System.currentTimeMillis());
                commMessage.setMsg(UUID.randomUUID().toString());
                commMessage.setSendTime(new Date());
                String stringMsg = OBJECT_MAPPER.writeValueAsString(commMessage);
                log.info("sending messsage: '{}'", stringMsg);
                // asynchronous
                kafkaTemplate.send(kafkaCommProperties.getTopic(), commMessage);
                // synchronous
                //kafkaTemplate.send(kafkaCommProperties.getTopic(), commMessage).get(1, TimeUnit.SECONDS);
            }
        }finally {
            kafkaTemplate.flush();
        }
    }
}
