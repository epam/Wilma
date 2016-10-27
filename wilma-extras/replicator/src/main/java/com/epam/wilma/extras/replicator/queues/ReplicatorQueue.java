package com.epam.wilma.extras.replicator.queues;

/*==========================================================================
Copyright 2016 EPAM Systems

This file is part of Wilma.

Wilma is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Wilma is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wilma.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import com.epam.wilma.domain.http.WilmaHttpRequest;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 * This class is to create Replicator Queue.
 *
 * @author Tamas Kohegyi
 */
public class ReplicatorQueue {

    static final String AMQ_SERVICE_URL = "vm://localhost";
    static final String SUBJECT = "replicator"; //Queue Name

    private Session session;
    private MessageProducer producer;

    /**
     * Constructor of Replicator Queue - creates a new queue in ActiveMQ of Wilma.
     *
     * @throws JMSException if issue occurs
     */
    //CHECKSTYLE.OFF - to ignore stupid checkstyle problem of: Unable to get class information for JMSException
    public ReplicatorQueue() throws JMSException {
        //CHECKSTYLE.ON
        // Create a ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(AMQ_SERVICE_URL);

        // Create a Connection
        Connection connection = connectionFactory.createQueueConnection();
        connection.start();

        // Create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Queue)
        Destination destination = session.createQueue(SUBJECT);

        // Create a MessageProducer from the Session to the Topic or Queue
        producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        MessageConsumer consumer = session.createConsumer(destination);

        //create consumer of the queue
        ReplicatorQueueConsumer replicatorQueueConsumer = new ReplicatorQueueConsumer();
        consumer.setMessageListener(replicatorQueueConsumer);
    }

    /**
     * Method to send a request to the Replicator Queue.
     *
     * @param wilmaHttpRequest is the request to be sent
     * @throws JMSException if any issue occurs
     */
    //CHECKSTYLE.OFF - to ignore stupid checkstyle problem of: Unable to get class information for JMSException
    public void sendMessage(WilmaHttpRequest wilmaHttpRequest) throws JMSException {
        //CHECKSTYLE.ON
        ObjectMessage message = session.createObjectMessage(wilmaHttpRequest);
        producer.send(message);
    }

}
