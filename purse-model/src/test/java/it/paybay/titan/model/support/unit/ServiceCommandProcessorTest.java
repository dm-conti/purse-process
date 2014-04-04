package it.paybay.titan.model.support.unit;

import it.paybay.titan.model.DocumentMessage;
import it.paybay.titan.model.ServiceCommand;
import it.paybay.titan.model.TitanMessage;
import it.paybay.titan.model.DocumentMessage.Keys;
import it.paybay.titan.model.support.ServiceCommandProcessor;
import it.paybay.titan.model.support.TitanMessageBuilder;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * ServiceCommandProcessor Test Suite
 * 
 * 
 * @author Mario Gallo
 * 
 */
public class ServiceCommandProcessorTest extends CamelTestSupport {

	@EndpointInject(uri = "direct:start")
	private ProducerTemplate producer;

	@EndpointInject(uri = "mock:result")
	protected MockEndpoint resultEndpoint;

	/**
	 * ServiceCommandProcessor Test Suite - Test Case 1
	 * The builder receives an unexpected body.
	 * 
	 * @throws InterruptedException
	 */
	@Test(expected = CamelExecutionException.class)
	public void testWithWrongExpectedBody() throws InterruptedException {
		producer.sendBody("<name>Giorgio</name>");

		// Assertions
		resultEndpoint.expectedMessageCount(0);
		resultEndpoint.assertIsSatisfied();
	}

	/**
	 * ServiceCommandProcessor Test Suite - Test Case 2
	 * The builder receives a TytnMessage with a wrong payload.
	 * 
	 * @throws InterruptedException
	 */
	@Test(expected = CamelExecutionException.class)
	public void testWithWrongExpectedPayload() throws InterruptedException {
		TitanMessage<?> tytnMessage = TitanMessageBuilder.withPayload("ciao").build();
		producer.sendBody(tytnMessage);

		// Assertions
		resultEndpoint.expectedMessageCount(0);
		resultEndpoint.assertIsSatisfied();
	}

	/**
	 * ServiceCommandProcessor Test Suite - Test Case 3
	 * The builder is created with a null operationName.
	 * 
	 * @throws InterruptedException
	 */
	@Test(expected = CamelExecutionException.class)
	public void testWithNullExpressionForOperationName() throws InterruptedException {
		TitanMessage<?> tytnMessage = TitanMessageBuilder.withPayload(new DocumentMessage()).build();
		producer.sendBody("direct:nullOperationName", tytnMessage);

		// Assertions
		resultEndpoint.expectedMessageCount(0);
		resultEndpoint.assertIsSatisfied();
	}

	/**
	 * ServiceCommandProcessor Test Suite - Test Case 4
	 * The builder is created with a null operationType.
	 * 
	 * @throws InterruptedException
	 */
	@Test(expected = CamelExecutionException.class)
	public void testWithNullExpressionForOperationType() throws InterruptedException {
		TitanMessage<?> tytnMessage = TitanMessageBuilder.withPayload(new DocumentMessage()).build();
		producer.sendBody("direct:nullOperationType", tytnMessage);

		// Assertions
		resultEndpoint.expectedMessageCount(0);
		resultEndpoint.assertIsSatisfied();
	}

	/**
	 * ServiceCommandProcessor Test Suite - Test Case 5
	 * The builder receives a TytnMessage with a Document Message which has no
	 * parameters.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testWithNullParameters() throws InterruptedException {
		TitanMessage<?> tytnMessage = TitanMessageBuilder.withPayload(new DocumentMessage()).build();
		producer.sendBody("direct:nullParameters", tytnMessage);
		
		// Assertions MockEndpoint
		resultEndpoint.expectedMessageCount(1);
		resultEndpoint.message(0).body().isInstanceOf(ServiceCommand.class);
		ServiceCommand received = resultEndpoint.getReceivedExchanges().get(0).getIn().getBody(ServiceCommand.class);
		
		// Assertions ServiceCommand generated
		assertEquals(received.getOperationName(), "operationName");
		assertEquals(received.getOperationType(), "operationType");
		assertEquals(received.getParameters().size(), 0);

		resultEndpoint.assertIsSatisfied();
	}

	/**
	 * ServiceCommandProcessor Test Suite - Test Case 6
	 * The builder receives a TytnMessage with a Document Message which has two
	 * parameters and selects one of them.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testWithParameters() throws InterruptedException {
		DocumentMessage documentMessage = new DocumentMessage();
		documentMessage.put(Keys.CUSTOMER_NAME, "Gianni Ometti");
		documentMessage.put(Keys.CUSTOMER_ID, "113");
		TitanMessage<?> tytnMessage = TitanMessageBuilder.withPayload(documentMessage).build();
		producer.sendBody(tytnMessage);

		// Assertions MockEndpoint
		resultEndpoint.expectedMessageCount(1);
		resultEndpoint.message(0).body().isInstanceOf(ServiceCommand.class);
		ServiceCommand received = resultEndpoint.getReceivedExchanges().get(0).getIn().getBody(ServiceCommand.class);

		// Assertions ServiceCommand generated
		assertEquals(received.getOperationName(), "operationName");
		assertEquals(received.getOperationType(), "operationType");
		assertEquals(received.getParameters().size(), 1);

		resultEndpoint.assertIsSatisfied();
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				from("direct:start").process(
						ServiceCommandProcessor.operation(constant("operationType"), constant("operationName")).withParameters(
								new Keys[] { Keys.CUSTOMER_NAME })).to("mock:result");

				from("direct:nullParameters").process(
						ServiceCommandProcessor.operation(constant("operationType"), constant("operationName")).withParameters(null))
						.to("mock:result");

				from("direct:nullOperationName").process(ServiceCommandProcessor.operation(constant("proxy"), null)).to("mock:result");

				from("direct:nullOperationType").process(ServiceCommandProcessor.operation(null, constant("create"))).to("mock:result");
			}
		};
	}
}
