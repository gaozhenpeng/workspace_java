package com.at.mq.topic;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class TConsumerListener implements MessageListener {
    private static volatile int msgIdx = 1;

    @Override
    public void onMessage(final Message msg) {
        TextMessage txtMsg = (TextMessage) msg; // waiting in
                                                // miliseconds
        String txt = null;
        try {
            txt = txtMsg.getText();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        System.out.println("Text '" + msgIdx + "' : " + txt);
        msgIdx++;
    }

    public static void main(final String[] args) throws JMSException, InterruptedException {
        String username = ActiveMQConnection.DEFAULT_USER; // admin
        String password = ActiveMQConnection.DEFAULT_PASSWORD; // activemq
        String mqurl = ActiveMQConnection.DEFAULT_BROKER_URL; // tcp://localhost:61616
        ActiveMQConnectionFactory factory = null;
        Connection conn = null;
        Session session = null;
        Topic t = null;
        MessageConsumer consumer = null;
        try {
            factory = new ActiveMQConnectionFactory(mqurl);
            conn = factory.createConnection(username, password);

            boolean isUsingTrans = false;
            int ackMode = Session.AUTO_ACKNOWLEDGE;

            session = conn.createSession(isUsingTrans, ackMode);
            t = session.createTopic("com.at.mq.topic");
            consumer = session.createConsumer(t);
            consumer.setMessageListener(new TConsumerListener());
            conn.start();

            Object o = new Object();
            synchronized (o) {
                o.wait(100000);
            }
        } finally {
            if (consumer != null) {
                consumer.close();
            }
            if (t != null) {
                t = null;
            }
            if (session != null) {
                session.close();
            }
            if (conn != null) {
                conn.stop();
                conn.close();
            }
            if (factory != null) {
                factory = null;
            }
        }
    }
}