package it.tmp.businessprocess.pursecdm.route;

import it.tmp.businessprocess.pursecdm.converter.*;
import org.apache.camel.builder.RouteBuilder;

/**
 * 
 *  **************************************************************************************************************
 *  - DESIGN ABSTRACT - 
 *  
 *  REQUEST IN:  
 *  |ServiceCommand|     |Translation into Target| 	   |Creation of Target| 
 *  |Reception     | --> |Service parameters     | --> |Service request   |
 *  
 *  REPLY OUT:
 *  |Target Service | 	  |Translation into    | 	 |Wrapping into|  
 *  |Reply Reception| --> |Canonical Data Model| --> |Titan Message|
 *  																											
 *  ***************************************************************************************************************
 *  
 *  ***************************************************************************************************************
 *  - DESCRIPTION -
 * 
 *  This class creates an integration route used to call a generic Entity Service.
 *  A Service Command message is received from the point-to-point input channel; this message contains
 *  the operation name that must be invoked on the target service and its related parameters, represented
 *  in the form of a canonical data format. The Service Adapter is responsible for translating these parameters
 *  into the format expected by the target service, and must create the service request using the
 *  technology and communication protocol used by the service.
 *  The service reply is then translated into a canonical form using a document message and then wrapped into
 *  a Titan Message, which is sent back to the Business Process.
 * 
 *  @author Carmine Sasso [consulenti.csasso@quigroup.it]
 */
public class EntityServiceAdapterRoute extends RouteBuilder {

	public static final String ROUTE_ID = "entityServiceAdapterRoute";
	public static final String ENTITY_ADAPTER_QUEUE = "entityServiceAdapterQueue.in";
	
	@Override
	public void configure() throws Exception {
		
		/*
		 *  Point-To-Point Channel of the Service Adapter, where ServiceCommands are received
		 */
//		from("activemq:queue:" + ENTITY_ADAPTER_QUEUE + "?concurrentConsumers=10")
		from("direct:start")
		.routeId(ROUTE_ID)
		.to("log:" + ROUTE_ID + ".input?level=INFO&showAll=true&multiline=true")
		/*
		 *  The Service Adapter converts the parameters of the ServiceCommand and creates the request into the specific format of the target service.
		 */
		.beanRef("canonicalMapper", "canonical2ParameterMapper")
		
		.doTry()
			.to("log:" + ROUTE_ID + ".service.input?level=INFO&showAll=true&multiline=true")
			/*
			 * Use the above translated parameters to build the request to the target service,
			 * using a service-specific endpoint (made available by Camel, like for instance a CXFrs endpoint) 
			 */
			.to("log:" + ROUTE_ID + ".service").id("callingService")
			.to("log:" + ROUTE_ID + ".service.output?level=INFO&showAll=true&multiline=true")
		.doCatch(Exception.class)
		/*
		 * In this route we use a generic Exception. Replace it with the correct
		 * exceptions and manage them in the class
		 */
			.bean(ExceptionMapper.class, "handleException")
		.end()
		/*
		 * The Service Adapter transforms the reply data into a TitanMessage and sends them on the reply queue of the business process.
		 */
		.beanRef("canonicalMapper", "parameter2CanonicalMapper")
		.to("log:" + ROUTE_ID + ".output?level=INFO&showAll=true&multiline=true");
	}
}