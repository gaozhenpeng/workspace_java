package com.at.rabbitmq.prod;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.SerializationUtils;

public class Sender extends BaseConnector {
    public Sender(String queueName) throws IOException, TimeoutException {
        super(queueName);
    }

    public void sendMessage(Serializable object) throws IOException {
        channel.basicPublish("",queueName, null, SerializationUtils.serialize(object));
    }   
}
