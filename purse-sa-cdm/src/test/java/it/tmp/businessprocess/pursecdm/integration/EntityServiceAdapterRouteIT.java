package it.tmp.businessprocess.pursecdm.integration;

import static org.junit.Assert.assertTrue;
import it.paybay.titan.model.ServiceCommandMessage;
import it.paybay.titan.model.TitanMessage;
import it.tmp.businessprocess.pursecdm.route.EntityServiceAdapterRoute;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.DisableJmx;
import org.apache.camel.test.spring.ShutdownTimeout;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 *  Integration Test Suite - EntityServiceAdapterRoute
 * 
 *  The integration testing of one or more routes created using a route
 *  builder requires the use of NotifyBuider in order to allow the system
 *  to process the request. The integration testing involves real live 
 *  components, so we don't need mocks.
 * 
 *  @author Carmine Sasso [consulenti.csasso@quigroup.it]
 *  
 */

@RunWith(CamelSpringJUnit4ClassRunner.class)
@DisableJmx(true)
@ShutdownTimeout(value=5, timeUnit=TimeUnit.SECONDS)
@ContextConfiguration(locations = { "classpath:IT-context.xml" })
public class EntityServiceAdapterRouteIT {

	@Autowired
	private ModelCamelContext context;
	
	@Autowired
	private ProducerTemplate template;

	/*
	 * Test Case 1 - Tests the operation getSomething in the testing
	 * environment.
	 * 
	 * This represents just a guideline for how to write an integration test.
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetSomething() throws Exception {
		/*
		 * The NotifyBuilder allows you to define the conditions, through DSL,
		 * which must occur before continuing the test.
		 */
		NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();

		/*
		 * Starting the test 
		 */
		ServiceCommandMessage command = new ServiceCommandMessage();
		Map<String, Object> headers = new LinkedHashMap<String, Object>();
		headers.put(ServiceCommandMessage.OPERATION_NAME, "createEntityProfile");
		headers.put(ServiceCommandMessage.OPERATION_TYPE, "proxy");
		Map<String, Object> parameters = new LinkedHashMap<String, Object>();
		parameters.put("ENTITY_NAME", "Gianni Ometti");
		command.setHeaders(headers);
		command.setParameters(parameters);
		
		Object result = template.sendBody("activemq:queue:" + EntityServiceAdapterRoute.ENTITY_ADAPTER_QUEUE, ExchangePattern.InOut, command);

		/*
		 * Waiting for the Camel Application to complete the routing. The
		 * parameters represents the time limit within which the conditions must
		 * be met.
		 */
		
		boolean matches = notify.matches(5, TimeUnit.SECONDS);
		
		/*
		 *  Assertions 
		 */
		assertTrue(matches);
		assertTrue(result instanceof TitanMessage<?>);
	}
}