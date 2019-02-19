package com.at.mq.queue;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class QProducer {
    public static void main(final String[] args) throws JMSException {
        String username = ActiveMQConnection.DEFAULT_USER; // admin
        String password = ActiveMQConnection.DEFAULT_PASSWORD; // activemq
        String mqurl = ActiveMQConnection.DEFAULT_BROKER_URL; // tcp://localhost:61616
        ActiveMQConnectionFactory factory = null;
        Connection conn = null;
        Session session = null;
        Queue q = null;
        MessageProducer producer = null;
        try {
            factory = new ActiveMQConnectionFactory(mqurl);
            conn = factory.createConnection(username, password);
            conn.start();

            boolean isUsingTrans = false;
            int ackMode = Session.AUTO_ACKNOWLEDGE;

            session = conn.createSession(isUsingTrans, ackMode);
            q = session.createQueue("com.at.mq.queue");
            producer = session.createProducer(q);
            for (int i = 0; i < 1000; i++) {
                String txt = "Msg id: " + i;
                System.out.println("Sending msg: '" + txt + "'");
                Message msg = session.createTextMessage(txt);
                producer.send(msg);
            }

        } finally {
            if (producer != null) {
                producer.close();
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