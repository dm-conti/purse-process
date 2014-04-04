package it.paybay.titan.workflows;

import it.paybay.titan.model.DocumentMessage.Keys;
import it.paybay.titan.model.Queues;
import it.paybay.titan.model.support.ResponseBuilder;
import it.paybay.titan.model.support.ServiceCommandBuilder;
import java.util.concurrent.ExecutorService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ThreadPoolBuilder;

/**
 *	This class creates a route for the business process about buying
 *	a ticket.
 */
public class NewPurseWorkflow extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		ThreadPoolBuilder builder = new ThreadPoolBuilder(getContext());
		ExecutorService myPool = builder.poolSize(30).maxPoolSize(50).build("MyPool");
		
		from("direct:newPurseWorkflowRoute")
		.routeId("buyTicketWorkflowRoute")
		.threads().executorService(myPool)
		.to("log:buyTicketWorkflowRoute.input?level=INFO&showAll=true&multiline=true")
		.to("direct:buyTicket.ticketing")
		.bean(ResponseBuilder.class)
		.to("log:buyTicketWorkflowRoute.output?level=INFO&showAll=true&multiline=true");

		from("direct:buyTicket.ticketing")
		.routeId("buyTicketWorkflowRoute.buyTicket.ticketing")
		.process(ServiceCommandBuilder.operation(constant("proxy"), constant("getTicket"))
				.withParameters(new Keys[]{ Keys.TICKET_ID}))
				.setHeader("JMSReplyTo", constant("queue:" +  Queues.TICKETING_SERVICE_ADAPTER_QUEUE.outputQueue()))
				.inOut("activemq:queue:" + Queues.TICKETING_SERVICE_ADAPTER_QUEUE.inputQueue())
				.to("log:buyTicketWorkflowRoute.output.ticketing?level=INFO&showAll=true&multiline=true");
		
	}
}
