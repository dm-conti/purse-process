package it.paybay.titan.workflows;

import it.paybay.titan.model.DocumentMessage.Keys;
import it.paybay.titan.model.Queues;
import it.paybay.titan.model.TitanMessage;
import it.paybay.titan.model.support.ServiceCommandBuilder;

import java.util.concurrent.ExecutorService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ThreadPoolBuilder;

/**
 *	This class creates a route for the business process about the 
 *	creation of a new customer profile.
 */
public class NewCustomerWorkflow extends RouteBuilder {

	public static final String ROUTE_ID = "newCustomerWorkflowRoute";
	
	@Override
	public void configure() throws Exception {
		
		ThreadPoolBuilder builder = new ThreadPoolBuilder(getContext());
		ExecutorService myPool = builder.poolSize(40).maxPoolSize(50).build("MyPool");

		from("direct:newCustomerWorkflowRoute")
		.routeId(ROUTE_ID)
		.threads().executorService(myPool)
		.to("log:" + ROUTE_ID + ".input?level=INFO&showAll=true&multiline=true")
		.setHeader("replyChannel", simple("${body.headers[replyChannel]}"))
		.to("direct:" + ROUTE_ID + ".step1")
		.to("direct:" + ROUTE_ID + ".step2")
		.to("log:" + ROUTE_ID + ".output?level=INFO&showAll=true&multiline=true");
		
		from("direct:" + ROUTE_ID + ".step1")
		.routeId(ROUTE_ID + ".step1")
		.convertBodyTo(TitanMessage.class)
		.process(ServiceCommandBuilder.operation(constant("proxy"), constant("createCustomerProfile")).withParameters(new Keys[]{ Keys.CUSTOMER_NAME, Keys.CUSTOMER_SURNAME, Keys.CUSTOMER_CITY, Keys.CUSTOMER_ADDRESS }))
		.setHeader("JMSReplyTo", constant("queue:" + Queues.CUSTOMER_SERVICE_ADAPTER_QUEUE.outputQueue()))
		.inOut("activemq:queue:" + Queues.CUSTOMER_SERVICE_ADAPTER_QUEUE.inputQueue())
		.to("log:" + ROUTE_ID + ".step1.output?level=INFO&showAll=true&multiline=true");
		
		from("direct:" + ROUTE_ID + ".step2")
		.routeId(ROUTE_ID + ".step2")
		.convertBodyTo(TitanMessage.class)
		.process(ServiceCommandBuilder.operation(constant("self"), simple("${body.headers[nextActions][self]}")))
		.setHeader("JMSReplyTo", constant("queue:" + Queues.CUSTOMER_SERVICE_ADAPTER_QUEUE.outputQueue()))
		.inOut("activemq:queue:" + Queues.CUSTOMER_SERVICE_ADAPTER_QUEUE.inputQueue())
		.to("log:" + ROUTE_ID + ".step2.output?level=INFO&showAll=true&multiline=true");
	}
}
