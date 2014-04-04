package it.tmp.restentrypoint.pursecdm.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.jaxrs.CxfRsEndpoint;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.cxf.jaxrs.model.MethodInvocationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class is responsible of aggregating the parameters received in input from the client
 * in a single Parameters Map.
 * 
 * @author Mario Gallo [consulenti.mgallo@quigroup.it]
 * 
 */
public class ParametersAggregatorProcessor_OLD implements Processor {

	private static final Logger LOG = LoggerFactory.getLogger(ParametersAggregatorProcessor.class);
	private Map<String, List<String>> operationName2Parameters;
	
	@Autowired private MethodInvocationInfo methoInfo;
	@Autowired private CamelContext context;
	
	@Override
	public void process(Exchange exchange) throws Exception {
		if (operationName2Parameters == null) {
			Endpoint endpoint = exchange.getFromEndpoint();
			
			CxfRsEndpoint cxfEndpoint = (CxfRsEndpoint) endpoint;
			Class<?> clazz = cxfEndpoint.getResourceClasses().get(0);
			LOG.info("Reading parameters from class:" + clazz.getName() + "." );
			readParameters(clazz);
		}
		
		try {
			String operationName = exchange.getIn().getHeader("operationName", String.class);
			LOG.info("Aggregating parameters for operation:" + operationName + ".");
			List<String> parametersName = operationName2Parameters.get(operationName);
			
			Map<String, Object> parameters = new LinkedHashMap<String, Object>();
			Map<String, Object> camelHeaders = exchange.getIn().getHeaders();
			
			for (String parameterName : parametersName) {
				parameters.put(parameterName, camelHeaders.get(parameterName));
			}
			
			//Qui avviene la conversion del DTO in MAP
			Object body = exchange.getIn().getBody(); 
			if (body != null) {
				Class<?> clazz = body.getClass();
				Field[] fields = clazz.getDeclaredFields();
				for (Field f : fields) {
						f.setAccessible(true);
						parameters.put(f.getName(), f.get(body));
				}
			}
			exchange.getOut().setHeader("operationName", operationName);
			exchange.getOut().setBody(parameters);
		}
		catch (Exception ex) {
			LOG.error(ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}
		
	}
	
	/**
	 * 
	 * **/
	private void readParameters(Class<?> clazz) {
		operationName2Parameters = new LinkedHashMap<String, List<String>>();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			Annotation[][] annotations = method.getParameterAnnotations();
			List<String> parametersNames = new LinkedList<String>();
			for (int i = 0; i < annotations.length; i++) {
				for (int j = 0; j < annotations[i].length; j++) {
					Annotation current = annotations[i][j];
					if (current instanceof PathParam) {
						PathParam param = (PathParam) current;
						parametersNames.add(param.value());
					}
					else if (current instanceof QueryParam) {
						QueryParam param = (QueryParam) current;
						parametersNames.add(param.value());
					}
					else if (current instanceof HeaderParam) {
						HeaderParam param = (HeaderParam) current;
						parametersNames.add(param.value());
					}
					else if (current instanceof FormParam) {
						FormParam param = (FormParam) current;
						parametersNames.add(param.value());
					}
				}
			}
			operationName2Parameters.put(method.getName(), parametersNames);
		}
	}
}
