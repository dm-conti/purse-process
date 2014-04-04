package it.tmp.businessprocess.pursecdm;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * 
 *  **************************************************************************************************************
 *  - DESIGN ABSTRACT - 
 *  
 *  REQUEST IN:  
 *  |Read the Titan Message |     |Execute one or more workflows  | 	|Execute some Integration Logic    | 
 *  |from a DataType Channel| --> |from the Titan Workflow Library| --> |between workflow calls (if needed)|
 *  
 *  REPLY OUT:
 *  |Gather results from| 	  |Wrap them into | 	|Send the Titan Message            |  
 *  |workflows execution| --> |a Titan Message| --> |to the Entry-Point's reply channel|
 *  																											
 *  ***************************************************************************************************************
 *  
 *  ***************************************************************************************************************
 *  - DESCRIPTION -
 * 
 *  This class contains the business process logic composed of:
 *  
 *   - multiple workflows injected from the Titan Workflow Library
 *   - the EIPs used to integrate the workflows (if needed) and implement the business process custom logic 
 *  
 *  The Business Process receives a Titan Message from its DataType Channel, and executes multiple workflows
 *  injected from the Titan Workflow Library. Each workflow is used to implement a specific set of activities 
 *  within the business process and will process only Titan Messages containing the right Document Messages
 *  (basically the messages containing data specific to a workflow). Between multiple workflow calls the Business
 *  Process may need to add some integration logic so that (for instance) the output of a workflow A can be
 *  translated as the input to the workflow B.
 * 
 *  @author Salvatore Esposito [salvatore.esposito@quigroup.it]
 *  @author Raffaele Carotenuto [raffaele.carotenuto@quigroup.it]
 */
public class PurseBP extends RouteBuilder{
	
	/*
	 * Define your business process routes using one or more workflows.
	 * The Titan workflows can be injected in the camel context by properly
	 * configuring the camel-context.xml file and using the Titan Workflow Library. 
	 */
	@Override
	public void configure() throws Exception {		
		
		/* Define the input queue representing the DataType Channel for this Business Process */
		from("activemq:queue:input")
		.routeId("purseBP")
		
		/*
		 * At this point one of the workflows injected from the Titan Workflow Library 
		 * can be invoked using the Camel DSL.
		 * Example:
		 * 
		 * .to("direct:myWorkflow")
		 */		
		
		/*
		 * Add the necessary integration logic for configuring your business process,
		 * so that it can invoke new workflows or perform some other EIP actions.
		 */
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				/*
				 * This processor can be used to transform the message body so that 
				 * it can be consumed by another worklow or EIP component.
				 */				
				String payload = exchange.getIn().getBody(String.class);
		        // Do something with the payload and/or exchange here
		        exchange.getIn().setBody("Changed body..." + payload);
			}
			
		})
		.log(LoggingLevel.DEBUG , "purseBP", "Sending message...")
		.to("file://target/activemq/output"); 
		/**
		 * Usare direct:nome_endpoint per invocare le rotte nel workflow
		 * 
		 * es. .to("direct:newPurseWorkflowRoute")
		 * attiva il workflow newCustomerWorkflow che inizia proprio con un
		 * from("direct:newPurseWorkflowRoute")
		 * 
		 * **/
		
		
		// The message produced so far can be given in input to more workflows or EIP components.

	}

}
