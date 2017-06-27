package com.at.mq.topic;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TConsumer {
    public static void main(final String[] args) throws JMSException {
        String username = "admin"; // ActiveMQConnection.DEFAULT_USER; // admin
        String password = "activemq"; // ActiveMQConnection.DEFAULT_PASSWORD;
                                      // // activemq
        String mqurl = "tcp://localhost:61616"; // ActiveMQConnection.DEFAULT_BROKER_URL;
                                                // //
                                                // failover://tcp://localhost:61616
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

            conn.start();

            Message msg = null;
            int msgIdx = 1;
            while ((msg = consumer.receive(1000)) != null) {
                TextMessage txtMsg = (TextMessage) msg; // waiting in
                                                        // miliseconds
                String txt = txtMsg.getText();
                System.out.println("Text '" + msgIdx + "' : " + txt);
                msgIdx++;
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