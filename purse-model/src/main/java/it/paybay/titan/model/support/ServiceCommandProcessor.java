package it.paybay.titan.model.support;

import it.paybay.titan.model.DocumentMessage;
import it.paybay.titan.model.ServiceCommand;
import it.paybay.titan.model.TitanMessage;
import it.paybay.titan.model.DocumentMessage.Keys;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceCommandProcessor implements Processor {

	private static final Logger LOG = LoggerFactory.getLogger(ServiceCommandProcessor.class);

	private Keys[] parameterKeys;
	private Map<String, Object> headers;
	private Expression operationNameExpression, operationTypeExpression;
	
	private ServiceCommandProcessor(Expression operationType, Expression operationName) {
		this.operationNameExpression = operationName;
		this.operationTypeExpression = operationType;
		headers = new LinkedHashMap<String,Object>();
	}
	
	public static ServiceCommandProcessor operation(Expression operationType, Expression operationName) {
		return new ServiceCommandProcessor(operationType, operationName);
	}
	
	public ServiceCommandProcessor withParameters(Keys[] keys) {
		if (keys != null) {
			this.parameterKeys = keys.clone();
		}else {
			this.parameterKeys = null;
		}
		return this;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		
		Object body = exchange.getIn().getBody();
		if (!(body instanceof TitanMessage<?>)) {
			throw new IllegalArgumentException("the body received is not of the excepted type");
		}
		
		TitanMessage<?> tytnMessage = exchange.getIn().getBody(TitanMessage.class);
		Object payload = tytnMessage.getPayload();
		if (!(payload instanceof DocumentMessage)) {
			throw new IllegalArgumentException("the payload of the received TytnMessage is not of the expected type");
		}
		if (operationNameExpression == null) {
			throw new IllegalArgumentException("The operation name cannot be null");
		}
		if (operationTypeExpression == null) {
			throw new IllegalArgumentException("The operation type cannot be null");
		}
		
		DocumentMessage documentMessage = (DocumentMessage) tytnMessage.getPayload();
		
		LOG.info("Creating a service command:{Headers:" + headers.toString() + " Selected Parameters:" + ArrayUtils.toString(parameterKeys) + "} from TytnMessage:" + tytnMessage.getPayload().toString() + ".");

		headers.put(ServiceCommand.OPERATION_TYPE, operationTypeExpression.evaluate(exchange, String.class));
		headers.put(ServiceCommand.OPERATION_NAME, operationNameExpression.evaluate(exchange, String.class));
		
		ServiceCommand command = new ServiceCommand();

		Map<String, Object> commandPayload = new HashMap<String, Object>();
		if (parameterKeys != null) {
			for (Keys key : parameterKeys) {
				Object value = documentMessage.get(key);
				commandPayload.put(key.name(), value);
				LOG.info("Parameter Name:[" + key + "] + Parameter Value:[" + (value != null ? value.toString() : "null") + "]");
			}
		}
		command.setHeaders(headers);
		command.setParameters(commandPayload);
		
		LOG.info("Created a service command with Headers=" + headers.toString() + " and CommandPayload:" + commandPayload.toString() + ".");
		exchange.getIn().setBody(command);
	}

}
