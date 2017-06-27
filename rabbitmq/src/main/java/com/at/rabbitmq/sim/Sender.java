package com.at.rabbitmq.sim;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {

    private static final Logger logger = LoggerFactory.getLogger(Sender.class);
    
    //队列名称  
    private final static String QUEUE_NAME = "queue";  

    public static void main(String[] argv) throws java.io.IOException, TimeoutException  
    {  
        /** 
         * 创建连接连接到MabbitMQ 
         */  
        ConnectionFactory factory = new ConnectionFactory();  
        //设置MabbitMQ所在主机ip或者主机名   
        factory.setHost("127.0.0.1"); 
//        factory.setPort(5672);
//        factory.setUsername("test");
//        factory.setPassword("test123");
//        factory.setVirtualHost("/mq_test");
        //创建一个连接  
        Connection connection = factory.newConnection();  
        //创建一个频道  
        Channel channel = connection.createChannel();  
        //指定一个队列  
        //                   队列名称    持久化     独占   自动清除     额外属性
        channel.queueDeclare(QUEUE_NAME, false,     false, false,       null);  
        //发送的消息  
        String message = "hello world!";  
        //往队列中发出一条消息  
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());  
        logger.info("Sent '" + message + "'");  
        //关闭频道和连接  
        channel.close();  
        connection.close();  
     }  
}
