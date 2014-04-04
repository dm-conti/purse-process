package it.tmp.restentrypoint.pursecdm.converter;

import it.tmp.restentrypoint.pursecdm.dto.CreatePurseDTO;

import java.util.Map;

import org.apache.camel.Converter;
import org.codehaus.jackson.map.ObjectMapper;

@Converter
public class DTOConverter {
	private static ObjectMapper mapper = new ObjectMapper();
    
	@Converter
    public static Map<String, Object> toMap(CreatePurseDTO dto) {
		System.out.println("Converter: convert DTO to MAP");
		Map<String,Object> props = mapper.convertValue(dto, Map.class);
//		MyBean anotherBean = m.convertValue(props, MyBean.class);
		return props;
    }
}