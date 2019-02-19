package com.at.rabbitmq.prod;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class BaseConnector {
    protected Channel channel;
    protected Connection connection;
    protected String queueName;
    /**
     * add user and vhost
     *   rabbitmqctl add_user test test123
     *   rabbitmqctl add_vhost /mq_test
     *   rabbitmqctl set_permissions -p /mq_test test ".*" ".*" ".*"
     *   
     * @param queueName
     * @throws IOException
     * @throws TimeoutException
     */
    public BaseConnector(String queueName) throws IOException, TimeoutException{
        this.queueName = queueName;
        //打开连接和创建频道
        ConnectionFactory factory = new ConnectionFactory();
        //设置MabbitMQ所在主机ip或者主机名  127.0.0.1即localhost
        factory.setHost("127.0.0.1");
//      factory.setPort(5672);
//      factory.setUsername("test");
//      factory.setPassword("test123");
//      factory.setVirtualHost("/mq_test");
        //创建连接  
        connection = factory.newConnection();
        //创建频道  
        channel = connection.createChannel();
        //声明创建队列
        channel.queueDeclare(queueName, false, false, false, null);
    }
}
