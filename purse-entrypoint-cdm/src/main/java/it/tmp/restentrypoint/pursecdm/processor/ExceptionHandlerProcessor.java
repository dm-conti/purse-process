package it.tmp.restentrypoint.pursecdm.processor;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.jms.UncategorizedJmsException;

/**
 * The ExceptionHandlerProcessor is responsible for handling the exception and producing
 * a custom message in output to the client.
 * 
 * @author Raffaele Carotenuto [raffaele.carotenuto@quigroup.it]
 * @author Mario Gallo [consulenti.mgallo@quigroup.it]
 * 
 */
public class ExceptionHandlerProcessor implements Processor {

	private ObjectMapper objectMapper;
	
	public ExceptionHandlerProcessor() {
		objectMapper = new ObjectMapper();
	}
	
	@Override
	public void process(Exchange exchange) throws Exception {
		if (exchange.getProperty(Exchange.EXCEPTION_CAUGHT) == null)
			throw new IllegalArgumentException("exception not found");
		Exception ex = (Exception) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
		ObjectNode obj = objectMapper.createObjectNode();
		if (ex instanceof BeanValidationException) {
			ArrayNode array = objectMapper.createArrayNode();
			BeanValidationException exception = (BeanValidationException) ex;
			Set<ConstraintViolation<Object>> constraints = exception.getConstraintViolations();
			for (ConstraintViolation<Object> constraint : constraints) {
				ObjectNode error = objectMapper.createObjectNode();
				error.put("property", constraint.getPropertyPath().toString());
				error.put("value", constraint.getInvalidValue().toString());
				error.put("message", constraint.getMessage());
				array.add(error);
			}
			obj.put("errors", array);
			String jsonString = objectMapper.writeValueAsString(obj);
			Response response = Response.status(Status.BAD_REQUEST).entity(jsonString).build();
			throw new WebApplicationException(response);
		}
		else if (ex instanceof UncategorizedJmsException) {
			obj.put("message", ex.getCause().getMessage());
			obj.put("reason", ex.getCause().getCause().getMessage());
			String jsonString = objectMapper.writeValueAsString(obj);
			Response response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(jsonString).build();
			throw new WebApplicationException(response);
		}
		
	}

}
