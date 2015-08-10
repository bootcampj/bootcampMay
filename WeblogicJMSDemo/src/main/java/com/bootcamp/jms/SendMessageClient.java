package com.bootcamp.jms;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SendMessageClient {
	
	 public void sendMessage() {
		 
	        Properties initialContextProperties = new Properties();
	        initialContextProperties.put("java.naming.factory.initial",
	                "org.apache.qpid.jndi.PropertiesFileInitialContextFactory");
	        String connectionString = "amqp://admin:admin@clientID/carbon?brokerlist='tcp://localhost:7001'";
	        initialContextProperties.put("connectionfactory.qpidConnectionfactory", connectionString);
	 
	        try {
	            InitialContext initialContext = new InitialContext(initialContextProperties);
	            QueueConnectionFactory queueConnectionFactory
	                    = (QueueConnectionFactory) initialContext.lookup("qpidConnectionfactory");
	 
	            QueueConnection queueConnection = queueConnectionFactory.createQueueConnection();
	            queueConnection.start();
	 
	            QueueSession queueSession = queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
	 
	            // Send message
	            Queue queue = queueSession.createQueue("myQueue;{create:always, node:{durable: true}}");
	            QueueSender queueSender = queueSession.createSender(queue);
	 
	            for(int count =0; count<10; count++)
	            {
	                queueSender.send(queueSession.createObjectMessage(new Integer(count)));
	            }
	 
	            // Housekeeping
	            queueSender.close();
	            queueSession.close();
	            queueConnection.stop();
	            queueConnection.close();
	 
	        } catch (NamingException e) {
	            e.printStackTrace();
	        } catch (JMSException e) {
	            e.printStackTrace();
	        }
	 
	    }
	 
	    public static void main(String[] args) {
	    	SendMessageClient sendConsumeClient = new SendMessageClient();
	        sendConsumeClient.sendMessage();
	 
	    }

}
