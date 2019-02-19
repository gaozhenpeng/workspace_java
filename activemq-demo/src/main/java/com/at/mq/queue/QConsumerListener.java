package com.at.mq.queue;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class QConsumerListener implements MessageListener {
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
        Queue q = null;
        MessageConsumer consumer = null;
        try {
            factory = new ActiveMQConnectionFactory(mqurl);
            conn = factory.createConnection(username, password);
            conn.start();

            boolean isUsingTrans = false;
            int ackMode = Session.AUTO_ACKNOWLEDGE;

            session = conn.createSession(isUsingTrans, ackMode);
            q = session.createQueue("com.at.mq.queue");
            consumer = session.createConsumer(q);
            consumer.setMessageListener(new QConsumerListener());
            Object o = new Object();
            synchronized (o) {
                o.wait(100000);
            }
        } finally {
            if (consumer != null) {
                consumer.close();
            }
            if (q != null) {
                q = null;
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