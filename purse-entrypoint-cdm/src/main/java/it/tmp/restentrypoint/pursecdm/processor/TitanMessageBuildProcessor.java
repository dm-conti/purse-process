package it.tmp.restentrypoint.pursecdm.processor;

import it.paybay.titan.model.TitanMessage;
import it.paybay.titan.model.purse.BusinessProcess;
import it.paybay.titan.model.purse.NewPurseProduct;
import it.paybay.titan.model.purse.Purse;
import it.paybay.titan.model.purse.Purse.Properties;
import it.paybay.titan.model.support.TitanMessageBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 *  This class is responsible for generating the TitanMessage. Using the Fluent Builder API it is 
 *  possible to add headers to the message.
 * 
 *  @author Raffaele Carotenuto [raffaele.carotenuto@quigroup.it]
 *  @author Mario Gallo [consulenti.mgallo@quigroup.it]
 * 
 */
public class TitanMessageBuildProcessor implements Processor {

	private Map<String, Object> headers;
	
	/**
	 * This is the first method to call in order to access the other methods
	 * of the fluent builder API.
	 * 
	 * @return TitanMessageProcessor
	 */
	public static TitanMessageBuildProcessor getInstance() {
		return new TitanMessageBuildProcessor();
	}
	
	/**
	 * This methods allows the adding of a new header to the TitanMessage.
	 * 
	 * @param key
	 * @param value
	 */
	public TitanMessageBuildProcessor setHeader(String key, Object value) {
		headers.put(key, value);
		return this;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, Object> parameters = exchange.getIn().getBody(Map.class);
		
		TitanMessage<Map<NewPurseProduct,Object>> tytnMessage = TitanMessageBuilder.withPayload(translate(parameters)).setHeaders(headers).build();
		
		exchange.getIn().setBody(tytnMessage);
	}
	
	private TitanMessageBuildProcessor() {
		 headers = new LinkedHashMap<String,Object>();
	}
	
	private Map<NewPurseProduct,Object> translate(Map<String, Object> parameters){
		
		Map<NewPurseProduct, Object> purseProduct = new LinkedHashMap<NewPurseProduct, Object>();
		
		
		purseProduct.put(Properties.PURSE_DESC, parameters.get(Properties.PURSE_DESC.propertyName()));
		purseProduct.put(Properties.ID_EMITTER, parameters.get(Properties.ID_EMITTER.propertyName()));
		purseProduct.put(Properties.PRODUCT_START_DATE, parameters.get(Properties.PRODUCT_START_DATE.propertyName()));
		purseProduct.put(Properties.PRODUCT_END_DATE, parameters.get(Properties.PRODUCT_END_DATE.propertyName()));
		purseProduct.put(Properties.IDS_SHOP_NETWORK, parameters.get(Properties.IDS_SHOP_NETWORK.propertyName()));
		
		purseProduct.put(Properties.DAYS_OF_WEEK, parameters.get(Properties.DAYS_OF_WEEK.propertyName()));
		purseProduct.put(Properties.CONVERSION_FACTORS, parameters.get(Properties.CONVERSION_FACTORS.propertyName()));
		purseProduct.put(Properties.DAILY_THRESHOLD, parameters.get(Properties.DAILY_THRESHOLD.propertyName()));
		purseProduct.put(Properties.MONTLY_THRESHOLD, parameters.get(Properties.MONTLY_THRESHOLD.propertyName()));
		purseProduct.put(Properties.HOURLY_THRESHOLDS, parameters.get(Properties.HOURLY_THRESHOLDS.propertyName()));
		
		return purseProduct;
	}
}