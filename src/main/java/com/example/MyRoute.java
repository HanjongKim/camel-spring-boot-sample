package com.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class MyRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		//------------
		// ActiveMQ
		//------------
		
		// from File to ActiveMQ
		from("file://inbox/order")
			.to("activemq:queue:in");

		// from ActiveMQ to ActiveMQ 
		from("activemq:queue:in")
			.to("activemq://queue:out");
		

		//------------
		// REST
		//------------
		
		// rest configuration
		restConfiguration().component("restlet").host("localhost").port(8081).bindingMode(RestBindingMode.auto);
		
		from("rest:get:/access")
			.transform().simple("access");	// return a simple text
		

		//-----------
		// direct from REST controller
		//-----------
		
        from("direct:rest").transform(simple("direct:rest"));		

		
		// TEST
//		from("timer:logMessageTimer?period=1s")
//			.log("Evnet triggerd by ${property.CamelTimerName} at ${header.CamelTimerFiredTime}");
		
	}

}
