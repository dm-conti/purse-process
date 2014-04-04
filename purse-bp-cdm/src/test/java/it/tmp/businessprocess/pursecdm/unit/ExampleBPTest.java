package it.tmp.businessprocess.pursecdm.unit;

import it.tmp.businessprocess.pursecdm.PurseBP;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Test suite containing unit tests for ExampleBP.  
 *
 * @author Salvatore Esposito {salvatore.esposito@quigroup.it}
 */
public class ExampleBPTest extends CamelTestSupport {
	
	@Override
	protected CamelContext createCamelContext() throws Exception {
		// Load the camel context
		CamelContext context =  super.createCamelContext();
		context.addComponent("activemq", context.getComponent("seda"));
		return context;
	}
	
	@Override
	public boolean isUseAdviceWith() {
		// This is required in order to delay the starting of the context.
		return true;
	}
	
	@Override
	protected RouteBuilder[] createRouteBuilders() throws Exception {
		// Create the routes for testing.
		return  new RouteBuilder[] { new PurseBP() };
	}

	/**
	 * Test Case 1 - Tests the routing of the first step of the business
	 * process ExampleBP.
	 * 
	 * @throws Exception
	 */
	@Test
	public void validExampleBP() throws Exception {

		// Using AdviceWith, it is possible to do unit testing without
		// modifying the route builder.
		// The external systems (like ActiveMQ) are replaced with in-memory
		// components.
		
		RouteDefinition routeDefinition = context.getRouteDefinitions().get(0);
		
		// Using AdviceWith, the starting of the context is delayed.
		routeDefinition.adviceWith(context, new AdviceWithRouteBuilder(){

			@Override
			public void configure() throws Exception {
				interceptSendToEndpoint("activemq:queue:input")
				.skipSendToOriginalEndpoint()
				.to("mock:testInput");
				replaceFromWith("direct:testing");
			}
			
		});
		
		// So, we need to start it manually.
		context.start();
		
		// Start the testing.
		// Sending messages... 
		template.sendBody("activemq:queue:input","msg");
		template.sendBody("activemq:queue:input","msg2");
		
		// Get the mock endpoint.
		MockEndpoint mock = getMockEndpoint("mock:testInput");
		
		// Assertions.
		mock.expectedMessageCount(2);
		mock.message(0).body().isNotNull();
		mock.message(1).body().isInstanceOf(String.class);
		mock.assertIsSatisfied();
		
	}
}
