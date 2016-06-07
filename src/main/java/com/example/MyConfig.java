package com.example;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {
	
	  @Bean
	  CamelContextConfiguration contextConfiguration() {
	    return new CamelContextConfiguration() {
	    	@Override
			public void afterApplicationStart(CamelContext arg0) {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void beforeApplicationStart(CamelContext context) {
				
				// config to ActiviMQ
				ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://127.0.0.1:61616");
				context.addComponent("activemq", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
				
			}
	    };
	  }	

}
