package it.tmp.restentrypoint.pursecdm.processor;

import static org.apache.camel.component.cxf.common.message.CxfConstants.CAMEL_CXF_RS_OPERATION_RESOURCE_INFO_STACK;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.cxf.jaxrs.model.MethodInvocationInfo;
import org.apache.cxf.jaxrs.model.OperationResourceInfoStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible of aggregating the parameters received in input
 * from the client in a single Parameters Map. It calls a specific service that
 * read methos's annotation parameter and return a parameters map.
 * 
 * @author Domenico Conti [domenico.conti@quigroup.it]
 * 
 */
public class ParametersAggregatorProcessor implements Processor {

	private static final Logger LOG = LoggerFactory.getLogger(ParametersAggregatorProcessor.class);
	private List<String> parametersNames = new LinkedList<String>();

	@Override
	public void process(Exchange exchange) throws Exception {
		if (parametersNames.isEmpty()) {
			Endpoint endpoint = exchange.getFromEndpoint();
			Map<String, Object> headers = exchange.getIn().getHeaders();

			OperationResourceInfoStack infoStack = (OperationResourceInfoStack) headers.get(CAMEL_CXF_RS_OPERATION_RESOURCE_INFO_STACK);
			MethodInvocationInfo methodInvoked = infoStack.firstElement();
			Method method = methodInvoked.getMethodInfo().getMethodToInvoke();

			readParameters(method.getParameterAnnotations());
		}

		try {
			String operationName = exchange.getIn().getHeader("operationName",String.class);
			LOG.info("Aggregating parameters for operation:" + operationName+ ".");

			Map<String, Object> parameters = new LinkedHashMap<String, Object>();
			Map<String, Object> camelHeaders = exchange.getIn().getHeaders();

			for (String parameterName : parametersNames) {
				parameters.put(parameterName, camelHeaders.get(parameterName));
			}
			
			parameters.putAll(exchange.getIn().getBody(Map.class));
			exchange.getOut().setBody(parameters);
			exchange.getOut().setHeader("operationName", operationName);
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}

	}
	
	
	private void readParameters(Annotation[][] annotations) {

		parametersNames = new LinkedList<String>();
		for (int i = 0; i < annotations.length; i++) {
			for (int j = 0; j < annotations[i].length; j++) {
				Annotation current = annotations[i][j];
				if (current instanceof PathParam) {
					PathParam param = (PathParam) current;
					parametersNames.add(param.value());
				} else if (current instanceof QueryParam) {
					QueryParam param = (QueryParam) current;
					parametersNames.add(param.value());
				} else if (current instanceof HeaderParam) {
					HeaderParam param = (HeaderParam) current;
					parametersNames.add(param.value());
				} else if (current instanceof FormParam) {
					FormParam param = (FormParam) current;
					parametersNames.add(param.value());
				}
			}
		}
	}
}