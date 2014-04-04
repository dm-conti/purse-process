package it.paybay.titan.model.support;

import it.paybay.titan.model.DocumentMessage;
import it.paybay.titan.model.MessageHeaders;
import it.paybay.titan.model.TitanMessage;
import it.paybay.titan.model.DocumentMessage.Keys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseBuilder implements Processor {

	private static final Logger LOG = LoggerFactory.getLogger(ResponseBuilder.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		LOG.info("Entering Response Builder");

		DocumentMessage documentMessage = new DocumentMessage();
		Map<String, Object> headers = new HashMap<String, Object>();
		TitanMessage<DocumentMessage> tytnMessage;
		if (exchange.getProperty(Exchange.GROUPED_EXCHANGE, List.class) != null) {
			List<Exchange> exchanges = (List<Exchange>) exchange.getProperty(Exchange.GROUPED_EXCHANGE, List.class);
			LOG.debug("The input message contains " + exchanges.size() + " grouped exchanges.");
			List<Map<String, Object>> nextActions = new ArrayList<Map<String, Object>>();
			int index = 0;
			Map<String, Object> exchangeHeaders = new HashMap<String, Object>();
			for (Exchange ex : exchanges) {
				TitanMessage<DocumentMessage> receivedTytnMessage = ex.getIn().getBody(TitanMessage.class);
				Map<String,Object> exHeaders = ex.getIn().getHeaders();
				for (String key : exHeaders.keySet()) {
					if (key.startsWith("titan")) {
						exchangeHeaders.put(key, exHeaders.get(key));
					}
				}
				
				if (receivedTytnMessage.getHeaders().containsKey(MessageHeaders.NEXT_ACTIONS)) {
					Map<String, Object> nextActionHeader = (Map<String, Object>) receivedTytnMessage.getHeaders().get(MessageHeaders.NEXT_ACTIONS);
					LOG.debug("The exchange with index=" + index + " contains a next_actions header=" + nextActionHeader.toString());
					nextActions.add(index, nextActionHeader);
				}
				else {
					LOG.debug("The exchange with index=" + index + " does not contain a next_actions header.");
					nextActions.add(index, new HashMap<String, Object>());
				}
				DocumentMessage payload = receivedTytnMessage.getPayload();
				for (Keys key : payload.keyset()) {
					Object value = payload.get(key);
					LOG.info("Document Message from Exchange with index=" + index + "- Parameter Name:[" + key + "] + Parameter Value:[" + (value != null ? value.toString() : "null") + "]");
					documentMessage.put(key, value);
				}
				index++;
			}
			headers.put(MessageHeaders.NEXT_ACTIONS, nextActions);
			tytnMessage = TitanMessageBuilder.withPayload(documentMessage).setHeaders(headers).build();
			exchange.getIn().setHeaders(exchangeHeaders);
		}
		else {
			TitanMessage<DocumentMessage> receivedTytnMessage = exchange.getIn().getBody(TitanMessage.class);
			DocumentMessage payload = receivedTytnMessage.getPayload();
			for (Keys key : payload.keyset()) {
				Object value = payload.get(key);
				LOG.info("Document Message - Parameter Name:[" + key + "] + Parameter Value:[" + (value != null ? value.toString() : "null") + "]");
				documentMessage.put(key, value);
			}
			tytnMessage = TitanMessageBuilder.withPayload(documentMessage).setHeaders(receivedTytnMessage.getHeaders()).build();
		}
		exchange.getOut().setHeaders(exchange.getIn().getHeaders());
		exchange.getOut().setBody(tytnMessage);
	}
}
