package it.tmp.restentrypoint.pursecdm.translator;

import it.paybay.titan.model.TitanMessage;
import java.io.IOException;
import java.util.Map;
import org.apache.camel.Body;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.type.TypeReference;

/**
 * This class is a processor for translating a document message from
 * the canonical data format to a JSON output.
 * 
 * @author Raffaele Carotenuto [raffaele.carotenuto@quigroup.it]
 * @author Mario Gallo [consulenti.mgallo@quigroup.it]
 * 
 */
public class RestTranslator {
	
	private ObjectMapper mapper;
	private ObjectWriter writer;
	
	public RestTranslator() {
		mapper = new ObjectMapper();
		writer = mapper.writerWithType(new TypeReference<Map<String,Object>>() {
		});
	}
	
	/**
	 * Simply translates a document message into a json format.
	 * 
	 * @param titanMessage - This is the TitanMessage generated by the middleware which contains the document message.
	 * @return String - the json output will become the new body of the exchange.
	 * 
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public String translate(@Body TitanMessage<Map<String,Object>> titanMessage) throws JsonGenerationException, JsonMappingException, IOException {
		Map<String,Object> results = titanMessage.getPayload();
		return writer.withDefaultPrettyPrinter().writeValueAsString(results);
	}
}
