package it.tmp.restentrypoint.pursecdm.integration;

import static org.junit.Assert.*;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.DisableJmx;
import org.apache.camel.test.spring.ShutdownTimeout;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

/**
 * Integration Test Suite - GenericFaçadeRoute
 * 
 * The integration testing of one or more routes created using a route
 * builder requires the use of NotifyBuider in order to allow the system
 * to process the request. The integration testing involves real live 
 * components, so we don't need mocks.
 * 
 * The configuration used for the test can be found in the file
 * @see test.properties. In that file, you have to change the parameters
 * with those of your testing environment.
 * 
 * @see GenericFaçadeRoute
 * @see IT-context.xml
 * @author Mario Gallo [consulenti.mgallo@quigroup.it]
 * 
 */

@Ignore
@RunWith(CamelSpringJUnit4ClassRunner.class)
@DisableJmx(true)
@ShutdownTimeout(value=5, timeUnit=TimeUnit.SECONDS)
@ContextConfiguration(locations = { "classpath:integration/IT-context.xml" })
public class GenericFacadeRouteIT {

	/** If SSL is enabled use HTTPS protocol */
	private static final String PROTOCOL = "http://";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value(value="${rest.endpoint.address}")
	private String restEndpointAddress;
	
	@Value(value="${rest.endpoint.port}")
	private int restEndpointPort;
	
	@Autowired
	private ModelCamelContext context;


	/**
	 * Test Case 1 - Tests the operation getSomething in the testing environment.
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
		
		/* Starting the test */
		Map<String, Object> urlParameters = new LinkedHashMap<String, Object>();
		urlParameters.put("content-type", "application/json");
		String url = PROTOCOL + restEndpointAddress + ":" + restEndpointPort + "/somethings/1";
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class, urlParameters);
		String actualString = responseEntity.getBody();
		
		/* Expectations */
		String expectedString = "{\"id\" : 1}";
		
		/* Waiting for the Camel Application to complete the routing. 
		 * The parameters represents the time limit within which the 
		 * conditions must be met.
		*/
		boolean matches = notify.matches(5, TimeUnit.SECONDS);
		assertTrue(matches);

		/* Assertions */
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		JSONAssert.assertEquals(expectedString, actualString, true);
	}
}
