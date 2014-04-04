package it.paybay.titan.workflows;

import it.paybay.titan.model.DocumentMessage.Keys;
import it.paybay.titan.model.Queues;
import it.paybay.titan.model.support.ResponseBuilder;
import it.paybay.titan.model.support.ServiceCommandBuilder;

import java.util.concurrent.ExecutorService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ThreadPoolBuilder;
import org.apache.camel.processor.aggregate.GroupedExchangeAggregationStrategy;

/**
 *	This class creates a route for the business process about posting
 *	a new ticket order.
 */
public class OrderTicketWorkflow extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		ThreadPoolBuilder builder = new ThreadPoolBuilder(getContext());
		ExecutorService pool = builder.poolSize(30).maxPoolSize(50).build("Pool");
		
		from("direct:orderTicketWorkflowRoute")
		.routeId("orderTicketWorkflowRoute")
		.threads().executorService(pool)
		.to("log:orderTicketWorkflowRoute.input?level=INFO&showAll=true&multiline=true")
		.to("direct:orderTicketWorkflowRoute.processing")
		.to("log:orderTicketWorkflowRoute.output?level=INFO&showAll=true&multiline=true");		
		
		from("direct:orderTicketWorkflowRoute.processing")
		.routeId("orderTicketWorkflowRoute.processing")
		.multicast(new GroupedExchangeAggregationStrategy())
			.parallelProcessing()
				.to("direct:orderTicket.ticketing")
				.to("direct:orderTicket.customer")
		.end()
		.bean(ResponseBuilder.class);
			
		from("direct:orderTicket.ticketing")
		.routeId("orderTicketWorkflowRoute.orderTicket.ticketing")
		.process(ServiceCommandBuilder.operation(constant("proxy"), constant("createTicket"))
				.withParameters(new Keys[]{ Keys.CUSTOMER_ID, Keys.TICKET_TYPE }))
		.setHeader("JMSReplyTo", constant("queue:" +  Queues.TICKETING_SERVICE_ADAPTER_QUEUE.outputQueue()))
		.inOut("activemq:queue:" + Queues.TICKETING_SERVICE_ADAPTER_QUEUE.inputQueue())
		.to("log:orderTicketWorkflowRoute.output.ticketing?level=INFO&showAll=true&multiline=true");
		
		from("direct:orderTicket.customer")
		.routeId("orderTicketWorkflowRoute.orderTicket.customer")
		.process(ServiceCommandBuilder.operation(constant("proxy"), constant("updateCustomerProfile"))
				.withParameters(new Keys[]{ Keys.CUSTOMER_ID, Keys.TICKET_TYPE }))
		.setHeader("JMSReplyTo", constant("queue:" +  Queues.CUSTOMER_SERVICE_ADAPTER_QUEUE.outputQueue()))
		.inOut("activemq:queue:" + Queues.CUSTOMER_SERVICE_ADAPTER_QUEUE.inputQueue())
		.to("log:orderTicketWorkflowRoute.output.customer?level=INFO&showAll=true&multiline=true");
	}
}
