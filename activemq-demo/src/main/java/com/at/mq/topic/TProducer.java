package com.at.mq.topic;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class TProducer {
    public static void main(final String[] args) throws JMSException {
        String username = ActiveMQConnection.DEFAULT_USER; // admin
        String password = ActiveMQConnection.DEFAULT_PASSWORD; // activemq
        String mqurl = ActiveMQConnection.DEFAULT_BROKER_URL; // tcp://localhost:61616
        ActiveMQConnectionFactory factory = null;
        Connection conn = null;
        Session session = null;
        Topic t = null;
        MessageProducer producer = null;
        try {
            factory = new ActiveMQConnectionFactory(mqurl);
            conn = factory.createConnection(username, password);

            boolean isUsingTrans = false;
            int ackMode = Session.AUTO_ACKNOWLEDGE;

            session = conn.createSession(isUsingTrans, ackMode);
            t = session.createTopic("com.at.mq.topic");
            producer = session.createProducer(t);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            conn.start();

            for (int i = 0; i < 1000000; i++) {
                String txt = "Msg id: " + i;
                System.out.println("Sending topic msg: '" + txt + "'");
                Message msg = session.createTextMessage(txt);
                producer.send(msg);
            }

        } finally {
            if (producer != null) {
                producer.close();
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