package it.tmp.businessprocess.pursecdm.converter;

import it.paybay.titan.model.Operation;
import it.paybay.titan.model.Parameter;
import it.paybay.titan.model.ServiceCommandMessage;
import it.paybay.titan.model.ServiceDefinition;
import it.paybay.titan.model.TitanMessage;
import it.paybay.titan.model.support.TitanMessageBuilder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.camel.Body;
import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * This class is responsible for translating the parameters
 * available in the ServiceCommandMessage from/to the Canonical Data Model.
 */

public class CanonicalMapper {

	private static final Logger LOG = LoggerFactory.getLogger(CanonicalMapper.class);

	private String configurationFile;
	
	private ServiceDefinition serviceDefinition;
	
	private Map<String, Operation> operationName2Operation;
	
	public void init() throws JAXBException, IOException{
		JAXBContext context = JAXBContext.newInstance(ServiceDefinition.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		serviceDefinition = (ServiceDefinition) unmarshaller.unmarshal(new InputStreamReader(new ClassPathResource(
					configurationFile).getInputStream()));
		operationName2Operation = new HashMap<String, Operation>();
		for (Operation operation : serviceDefinition.getOperations()) {
			operationName2Operation.put(operation.getName(), operation);
		}
	}
	
	public void setConfigurationFile(String configurationFile) {
		this.configurationFile = configurationFile;
	}
	
	public ServiceCommandMessage canonical2ParameterMapper(@Body ServiceCommandMessage command) throws IllegalArgumentException{
	
		Map<String, Object> parameters = command.getParameters();
		
		if (command.getOperationName() == null)
			throw new IllegalArgumentException("Operation Name not found!");

		String operationName = command.getOperationName();
		String operationType = (String) command.getHeaders().get(ServiceCommandMessage.OPERATION_TYPE);
		
		if (!operationType.equalsIgnoreCase("proxy")) {
			operationName = command.getHeaders().get(ServiceCommandMessage.OPERATION_TYPE).toString();
		}

		LOG.info("Translating from Canonical Model to ServiceSpecificParameters:{operationName=" + operationName + ", operationType=" + operationType + ", configFile=" + configurationFile + ", parameters=" + parameters.toString() + "}");
	
		Operation current = operationName2Operation.get(operationName);
		if (current == null) {
			LOG.error("Operation not found:" + operationName);
			throw new IllegalArgumentException("Operation not Found:" + operationName);
		}	
		command.setParameters(getSpecificMap(parameters, current));		
		return command;
	}

	public TitanMessage<?> parameter2CanonicalMapper(@Header("operationName") String operationName, @Body Object value) {
		
	return TitanMessageBuilder.withPayload(value).build();	
	}

	private Map<String, Object> getSpecificMap(Map<String, Object> parameters, Operation current) throws IllegalArgumentException{
		Map<String, Object> newMapParameters = new LinkedHashMap<String, Object>();
		if (current.getParameters() == null || current.getParameters().size() == 0) {
			LOG.debug("No Parameters provided for the required operation.");
		}
		else {
			for (Parameter param : current.getParameters()) {
				LOG.debug("Converting Parameter from " + param.getKeyIn() + " to " + param.getKeyOut() + ".");
				Object value = parameters.get(param.getKeyIn());
				if (value == null)
					throw new IllegalArgumentException("Parameter not found!");
				Object newValue = null;
				if (param.getType().equalsIgnoreCase("long")) {
					if (!(value instanceof Long))
						newValue = ((Integer) value).longValue();
					else
						newValue = value;
				}
				else if (param.getType().equalsIgnoreCase("string")) {
					newValue = (String) value;
				}
				else {
					newValue = value;
				}
				newMapParameters.put(param.getKeyOut(), newValue);
			}
		}

		if (current.getPayload() == null) {
			LOG.debug("No Payload provided for the required operation.");
			if (current.getParameters() == null || current.getParameters().size() == 0) {
				return null;
			}
		}
		else if (current.getPayload().size() == 0) {
			LOG.debug("Empty Payload provided for the required operation.");
			newMapParameters.put("payload",new LinkedHashMap<String, Object>());
		}
		else {
			Map<String, Object> newMapPayload = new LinkedHashMap<String, Object>();
			for (Parameter param : current.getPayload()) {
				LOG.debug("Converting Payload parameter from " + param.getKeyIn() + " to " + param.getKeyOut() + ".");
				Object value = parameters.get(param.getKeyIn());
				if (value == null)
					throw new IllegalArgumentException("PayloadParameter not found!");

				newMapPayload.put(param.getKeyOut(), value);
			}
			newMapParameters.put("payload",newMapPayload);
		}
		return newMapParameters;
	}
}