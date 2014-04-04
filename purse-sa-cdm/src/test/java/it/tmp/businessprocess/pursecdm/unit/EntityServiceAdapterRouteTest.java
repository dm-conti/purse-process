package it.tmp.businessprocess.pursecdm.unit;

import java.util.LinkedHashMap;
import java.util.Map;
import it.paybay.titan.model.ServiceCommandMessage;
import it.paybay.titan.model.TitanMessage;
import it.tmp.businessprocess.pursecdm.route.EntityServiceAdapterRoute;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 *  Unit Test Suite - EntityServiceAdapterRoute
 * 
 *  The unit testing of one or more routes requires the use of the adviceWith,
 *  which permits to replace the external system with mock or stub endpoints.
 *  
 *  @author Carmine Sasso [consulenti.csasso@quigroup.it]
 * 
 */

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
public class EntityServiceAdapterRouteTest extends CamelTestSupport {

	@Override
	protected CamelContext createCamelContext() throws Exception {
		/*
		 *  Load the camel context
		 */
		CamelContext context =  super.createCamelContext();
		context.addComponent("activemq", context.getComponent("seda"));
		return context;
	}
	
	@Override
	public boolean isUseAdviceWith() {
		/*
		 *  This is required to delay the starting of the context.
		 */
		return true;
	}
	
	@Override
	protected RouteBuilder[] createRouteBuilders() throws Exception {
		/*
		 *  Create the routes for testing.
		 */
		return  new RouteBuilder[] { new EntityServiceAdapterRoute() };
	}
	
	@Autowired
	private ModelCamelContext context;
	
	@Autowired
	private ProducerTemplate template;
		
	/*
	 * Test Case 1 - Tests the operation getSomething.
	 * 
	 * This represents just a guideline for how to write a unit test.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getSomething() throws Exception {
		
		final String ENTITY_ADAPTER_QUEUE = "entityServiceAdapterQueue.in";
		
		/*
		 * Using AdviceWith, it is possible to do unit testing without
		 * modifying the route builder.
		 * The external systems (like ActiveMQ) are replaced with in-memory components.
		 */
		RouteDefinition route = context.getRouteDefinitions().get(0);
		
		route.adviceWith(context, new AdviceWithRouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				weaveById("callingService")
				.replace()
				.process(new Processor() {
	
						@Override
						public void process(Exchange exchange) throws Exception {
							ServiceCommandMessage command = exchange.getIn().getBody(ServiceCommandMessage.class);
							Map<String, Object> parameters = new LinkedHashMap<String,Object>();
							parameters.put("id", 1);
							Map<String,Object> payload = (Map<String, Object>) command.getParameters().get("payload");
							parameters.put("name", payload.get("name"));
							exchange.getIn().setBody(parameters);
						}
					})
				.end();
			}
		});

		/*
		 * Using AdviceWith, the starting of the context is delayed. So, we need to
		 * start it manually.
		 */
		context.start();
		
		/*
		 *  Starting the test 
		 */
		ServiceCommandMessage command = new ServiceCommandMessage();
		Map<String, Object> headers = new LinkedHashMap<String, Object>();
		headers.put(ServiceCommandMessage.OPERATION_NAME, "createEntityProfile");
		headers.put(ServiceCommandMessage.OPERATION_TYPE, "proxy");
		Map<String, Object> parameters = new LinkedHashMap<String, Object>();
		parameters.put("ENTITY_NAME", "Gianni Ometti");
		command.setHeaders(headers);
		command.setParameters(parameters);
		
		/*
		 *  Sending messages
		 */
		Object result = template.requestBody("activemq:queue:" + ENTITY_ADAPTER_QUEUE, command);

		/*
		 * Assertions 
		 */
		assertIsInstanceOf(TitanMessage.class, result);
	}
}
