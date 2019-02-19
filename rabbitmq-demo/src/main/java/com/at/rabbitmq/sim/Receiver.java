package com.at.rabbitmq.sim;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Receiver  {

	private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
	

    //队列名称  
    private final static String QUEUE_NAME = "queue";  
    public static void main(String[] argv) throws java.io.IOException, InterruptedException, TimeoutException  
    {  
        //打开连接和创建频道，与发送端一样  
        ConnectionFactory factory = new ConnectionFactory();
        //设置MabbitMQ所在主机ip或者主机名  
        factory.setHost("127.0.0.1");
//        factory.setPort(5672);
//        factory.setUsername("test");
//        factory.setPassword("test123");
//        factory.setVirtualHost("/mq_test");
        
        Connection connection = factory.newConnection();  
        Channel channel = connection.createChannel();  
        //声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
        //                   队列名称    持久化     独占   自动清除     额外属性
        channel.queueDeclare(QUEUE_NAME, false,     false, false,       null);  
        logger.info("Waiting for messages. To exit press CTRL+C");  

        //创建队列消费者  
        QueueingConsumer consumer = new QueueingConsumer(channel);  
        //指定消费队列  
        channel.basicConsume(QUEUE_NAME, true, consumer);  
        while (true)  
        {  
            //nextDelivery是一个阻塞方法（内部实现其实是阻塞队列的take方法）  
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();  
            String message = new String(delivery.getBody());  
            logger.info("Received '" + message + "'");
        }  

    }  
} 
