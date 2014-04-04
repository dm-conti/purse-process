package it.tmp.restentrypoint.pursecdm.route;

import it.paybay.titan.model.MessageHeaders;
import it.paybay.titan.model.TitanMessage;
import it.tmp.restentrypoint.pursecdm.processor.ExceptionHandlerProcessor;
import it.tmp.restentrypoint.pursecdm.processor.ParametersAggregatorProcessor;
import it.tmp.restentrypoint.pursecdm.processor.TitanMessageBuildProcessor;
import it.tmp.restentrypoint.pursecdm.service.GenericFacadeService;
import it.tmp.restentrypoint.pursecdm.translator.RestTranslator;

import javax.ws.rs.core.MediaType;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.springframework.jms.UncategorizedJmsException;

/**
 * 
 *  **************************************************************************************************************
 *  - DESIGN ABSTRACT - 
 *  
 *  REQUEST IN:  
 *  |Restful Inbound  | 	|Input     | 	 |Translation To      | 	|Wrapping into| 	|Decision Logic and         | 
 *  |Interface        | --> |Validation| --> |Canonical Data Model| --> |Titan Message| --> |Routing to DataType Channel|
 *  
 *  REPLY OUT:
 *  |Unwrap from  | 	|Translation To   | 	|Preparation of   |  
 *  |Titan Message| --> |Client Data Model| --> |the Reply Message|
 *  																											
 *  ***************************************************************************************************************
 * 
 *  ***************************************************************************************************************
 *  - DESCRIPTION -
 * 
 *  The RESTFul Entry-Point is a façade composed of various JAX-RS annotated interfaces, each one
 *  representing the available operations on a specific resource (e.g. GenericFacadeService). 
 *  For each of these interfaces we need a route which will perform different actions depending on 
 *  the selected method of the input interface. For this reason, we can analyse the operationName parameter, 
 *  which is set by Cxfrs and matches exactly the method name.
 * 
 *  The configuration of each service, together with the context path, must be positioned in the cxf.xml file.
 * 
 * @see GenericFacadeService
 * @see cxf.xml
 * 
 * ***************************************************************************************************************
 * 
 * @author Mario Gallo [consulenti.mgallo@quigroup.it]
 * 
 */
public class PurseFacadeRoute extends RouteBuilder {

	private static final String FACADE_CLASS = "it.tmp.restentrypoint.pursecdm.service.CreatePurseFacadeService";
	private static final String ROUTE_ID = "CreatePurseFacadeRoute";
	private static final String REPLY_CHANNEL = "restChannel.out";
	private static final LoggingLevel ROUTE_LOGGING_LEVEL = LoggingLevel.INFO;
	
	@Override
	public void configure() throws Exception {
		from("cxfrs:bean:purseServer?bindingStyle=SimpleConsumer&resourceClasses=" + FACADE_CLASS)
		.routeId(ROUTE_ID)
		.log(ROUTE_LOGGING_LEVEL, "RestEntrypoint - Validating the request.")
		.doTry()
			.to("bean-validator://x")
		
		/**
		 * If one or more constraints are not met, a BeanValidationException is thrown by
		 * the bean-validator component.
		 */
		.doCatch(BeanValidationException.class)
			.removeHeaders("*")
			.bean(ExceptionHandlerProcessor.class)
			.setHeader("Content-Type", constant(MediaType.APPLICATION_JSON))
			.to("log:output?level=" + ROUTE_LOGGING_LEVEL.name() + "&showAll=true&multiline=true")
			.stop()
		.end()
		
		/**
		 * The component Cxfrs places HeaderParams, QueryParams, etc. in the headers
		 * of Camel Exchange. For this reason, we need to group them together with the other
		 * parameters contained in the payload.
		 */
		.bean(ParametersAggregatorProcessor.class)
		.log(ROUTE_LOGGING_LEVEL, "RestEntrypoint - Routing of the request.")
		
		/**
		 * The Queue REPLY_CHANNEL represents the channel where the reply from
		 * the middleware core is expected to be.
		 */
		.setHeader("JMSReplyTo", constant("queue:" + REPLY_CHANNEL))
		.doTry()
			.choice()
				.when(header("operationName").isEqualTo("getSomething"))
					.process(TitanMessageBuildProcessor.getInstance().setHeader(MessageHeaders.REPLY_CHANNEL, REPLY_CHANNEL))
					.to("log:input?level=" + ROUTE_LOGGING_LEVEL.name() + "&showAll=true&multiline=true")
					/**
					 * The Titan message has to be sent to the correct queue of the 
					 * Middleware core.
					 * 
					 * .inOut("activemq:queue:<QueueName>")
					 */
//					.inOut("mock:activemq:queue:" + Queues.PURSE_QUEUE.inputQueue() + "?replyTo=restChannel.out")
				.endChoice()
				.when(header("operationName").isEqualTo("postSomething"))
					.process(TitanMessageBuildProcessor.getInstance().setHeader(MessageHeaders.REPLY_CHANNEL, REPLY_CHANNEL))
					.to("log:input?level=" + ROUTE_LOGGING_LEVEL.name() + "&showAll=true&multiline=true")
					/**
					 * The Titan message has to be sent to the correct queue of the 
					 * Middleware core.
					 * 
					 * .inOut("activemq:queue:<QueueName>")
					 */
//					.inOut("mock:activemq:queue:" + Queues.PURSE_QUEUE.inputQueue() + "?replyTo=restChannel.out")
				.endChoice()
		.endDoTry()
		
		/**
		 * The UncategorizedJmsException maps the communication errors caused by the broker.
		 */
		.doCatch(UncategorizedJmsException.class)
			.removeHeaders("*")
			.bean(ExceptionHandlerProcessor.class)
			.setHeader("Content-Type", constant(MediaType.APPLICATION_JSON))
			.to("log:output?level=" + ROUTE_LOGGING_LEVEL.name() + "&showAll=true&multiline=true")
			.stop()
		.end()
		.to("log:output?level=" + ROUTE_LOGGING_LEVEL.name() + "&showAll=true&multiline=true")
		.convertBodyTo(TitanMessage.class)
		/**
		 * The response to the client has to be customized with a generic processor, or with
		 * a specialized one.
		 */
		.bean(RestTranslator.class)
		.removeHeaders("*")
		.setHeader("Content-Type", constant("application/json"));
	}
}