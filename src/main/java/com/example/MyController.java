package com.example;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.util.LoadPropertiesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

	@Autowired
	CamelContext camel;
	
	@RequestMapping(value = "/camel", method = RequestMethod.GET)
	public @ResponseBody String camel(){
		
		System.out.println("Use camel library");

		String result = "NONE";

		try {
			result = camel.findComponents().toString();
			System.out.println("Componets:"+camel.findComponents().toString());
			
		} catch (LoadPropertiesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	@RequestMapping(value = "/direct", method = RequestMethod.GET)
	public @ResponseBody String direct(){
		
		System.out.println("use camel producer");

		String result = "NONE";

		ProducerTemplate producer = camel.createProducerTemplate();
        producer.sendBody("direct:rest", "Message from REST/full");
        
		return result;
	}

	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public @ResponseBody String send(){
		System.out.println("Send a message to ActiveMQ:queue:in");
		
		String result = "success";
		
		
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("in");

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a messages
            String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
            TextMessage message = session.createTextMessage(text);

            // Tell the producer to send the message
            System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
            producer.send(message);

            // Clean up
            session.close();
            connection.close();
        }
        catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
            
            result = e.toString();
        }
		return result;
	}
	
}
