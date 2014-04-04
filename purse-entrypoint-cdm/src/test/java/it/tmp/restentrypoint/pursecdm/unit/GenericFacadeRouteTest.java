package it.tmp.restentrypoint.pursecdm.unit;

import static org.junit.Assert.*;
import it.paybay.titan.model.TitanMessage;
import it.paybay.titan.model.support.TitanMessageBuilder;
import it.tmp.restentrypoint.pursecdm.dto.SomethingDTO;
import it.tmp.restentrypoint.pursecdm.route.GenericFacadeRoute;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.DisableJmx;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Test Suite - Unit Testing GenericFacadeRoute
 * 
 * The unit testing of one or more routes created using a route builder
 * requires the use of the adviceWith, which permits to replace the 
 * external system with mock or stub endpoints.
 * 
 * The configuration used for the test can be found in the file
 * @see test.properties.
 * 
 * @see GenericFacadeRoute
 * @see test-context.xml
 * @author Mario Gallo [consulenti.mgallo@quigroup.it]
 * 
 */
@Ignore
@RunWith(CamelSpringJUnit4ClassRunner.class)
@DisableJmx(true)
@UseAdviceWith(true)
@ContextConfiguration(locations = { "classpath:unit/test-context.xml" })
public class GenericFacadeRouteTest {

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
	 * Test Case 1 - Tests the operation getSomething.
	 * In this test case, the reply generated by the middleware is
	 * simulated using a processor in place of a logging endpoint.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetSomething() throws Exception {
		RouteDefinition route = context.getRouteDefinitions().get(0);
		route.adviceWith(context, new AdviceWithRouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				interceptSendToEndpoint("log:input*")
				.process(new Processor()
				{
					@Override
					public void process(Exchange exchange) throws Exception {
						Map<String, Object> results = new LinkedHashMap<String, Object>();
						results.put("result", "this is an example");
						results.put("id", 1);
						results.put("entity", "something");						
						TitanMessage<Map<String,Object>> titanMessage = TitanMessageBuilder.withPayload(results).build();
						exchange.getIn().setBody(titanMessage);
					}
				});
				
			}
		});

		/*
		 * Using AdviceWith, the starting of the context is delayed. So, we need to
		 * start it manually.
		 */
		context.start();
		
		/* Starting the test */
		Map<String, Object> urlParameters = new LinkedHashMap<String, Object>();
		urlParameters.put("content-type", "application/json");
		String url = PROTOCOL + restEndpointAddress + ":" + restEndpointPort + "/somethings/1";
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class, urlParameters);
		String actualString = responseEntity.getBody();
		
		/* Expectations */
		String expectedString = 
				"{\"result\" : \"this is an example\",\"id\" : 1,\"entity\" : \"something\"}";
		
		/* Assertions */
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		JSONAssert.assertEquals(expectedString, actualString, true);
	}
	
	/**
	 * Test Case 2 - Tests the operation postSomething.
	 * In this test case, the answer generated from the middleware is
	 * simulated using a processor in place of a logging endpoint.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostSomething() throws Exception {
		RouteDefinition route = context.getRouteDefinitions().get(0);
		route.adviceWith(context, new AdviceWithRouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				interceptSendToEndpoint("log:input*")
				.process(new Processor()
				{
					@Override
					public void process(Exchange exchange) throws Exception {
						Map<String, Object> results = new LinkedHashMap<String, Object>();
						results.put("result", "this is an example");
						results.put("id", 1);
						results.put("entity", "something");						
						TitanMessage<Map<String,Object>> titanMessage = TitanMessageBuilder.withPayload(results).build();
						exchange.getIn().setBody(titanMessage);
					}
				});
				
			}
		});

		/*
		 * Using AdviceWith, the starting of the context is delayed. So, we need to
		 * start it manually.
		 */
		context.start();
		
		/* Starting the test */
		Map<String, Object> urlParameters = new LinkedHashMap<String, Object>();
		urlParameters.put("content-type", "application/json");
		SomethingDTO payload = new SomethingDTO();
		payload.setAmount(1);
		payload.setCode("ABCDE");
		String url = PROTOCOL + restEndpointAddress + ":" + restEndpointPort + "/somethings/";
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, payload, String.class, urlParameters);
		String actualString = responseEntity.getBody();
		
		/* Expectations */
		String expectedString = 
				"{\"result\" : \"this is an example\",\"id\" : 1,\"entity\" : \"something\"}";
		
		/* Assertions */
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		JSONAssert.assertEquals(expectedString, actualString, true);
	}
	
	/**
	 * Test Case 3 - Tests the operation postSomething with wrong parameters.
	 * In this test case, the answer generated from the middleware is
	 * simulated using a processor in place of a logging endpoint.
	 * 
	 * @throws Exception
	 */
	@Test(expected=HttpClientErrorException.class)
	public void testPostSomethingWithWrongParameters() throws Exception {
		/*
		 * Using AdviceWith, the starting of the context is delayed. So, we need to
		 * start it manually.
		 */
		context.start();
		
		/* Starting the test */
		Map<String, Object> urlParameters = new LinkedHashMap<String, Object>();
		urlParameters.put("content-type", "application/json");
		SomethingDTO payload = new SomethingDTO();
		payload.setAmount(1);
		payload.setCode("E"); // The length of the code must be exactly five characters.
		String url = PROTOCOL + restEndpointAddress + ":" + restEndpointPort + "/somethings/";
		restTemplate.postForEntity(url, payload, String.class, urlParameters);
	}
}
