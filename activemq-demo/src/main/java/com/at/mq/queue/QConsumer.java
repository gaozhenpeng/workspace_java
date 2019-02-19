package com.at.mq.queue;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class QConsumer {
    public static void main(final String[] args) throws JMSException {
        String username = "admin"; // ActiveMQConnection.DEFAULT_USER; // admin
        String password = "activemq"; // ActiveMQConnection.DEFAULT_PASSWORD;
                                      // activemq
        String mqurl = "tcp://localhost:61616"; // ActiveMQConnection.DEFAULT_BROKER_URL;
                                                // failover://tcp://localhost:61616
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
