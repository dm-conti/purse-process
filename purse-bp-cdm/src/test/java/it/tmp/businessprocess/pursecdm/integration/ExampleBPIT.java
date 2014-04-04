package it.tmp.businessprocess.pursecdm.integration;

import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Integration Test Suite - ExampleBP
 * 
 * The integration testing of one or more routes created using a route
 * builder requires the use of NotifyBuider in order to allow the system
 * to process the request. The integration testing involves real live 
 * components, so we don't need mocks.
 * 
 * The configuration used for the test can be found in the file
 * @see configuration-test.properties. In that file, you have to change the parameters
 * with those of your testing environment.
 * 
 * @see PurseBP
 * @see camel-test-context.xml
 * @author Salvatore Esposito [salvatore.esposito@quigroup.it]
 * 
 */
public class ExampleBPIT extends CamelSpringTestSupport{
	
	@Override
	protected AbstractApplicationContext createApplicationContext() {
		// Loading camel context for integration testing
		return new ClassPathXmlApplicationContext("IT-context.xml");
	}
	
	/**
	 * Test Case 1 - Tests the business process route of ExampleBP.
	 * 
	 */
	@Test
	public void validExampleBP(){
		// Using the NotifyBuilder we can build an expression which expresses when that condition occurred.
		NotifyBuilder notify = new NotifyBuilder(context).whenExactlyDone(1).create();
		
		String message = "msg";
		
		// Starting test...
		Object result = template.requestBody("activemq:queue:input", message);

		boolean matches = notify.matches(5, TimeUnit.SECONDS);
		
		// Assertions.
		assertTrue(matches);
		assertTrue(result instanceof String);
		// Needed to test the processor into the business process.
		assertEquals("Changed body..." + message, result);
	}
}
