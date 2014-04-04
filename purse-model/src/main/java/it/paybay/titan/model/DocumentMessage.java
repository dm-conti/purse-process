package it.paybay.titan.model;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class DocumentMessage implements Serializable {
	
	private static final long serialVersionUID = 4909432822071886644L;
	private Map<Keys,Object> data;
	
	public DocumentMessage() {
		data = new EnumMap<Keys, Object>(Keys.class);
	}
	
	public void put(Keys key, Object value) {
		data.put(key, value);
	}
	
	public Object get(Keys key) {
		return data.get(key);
	}
	
	public Object remove(Keys key) {
		return data.remove(key);
	}
	
	public boolean containsKey(Keys key) {
		return data.containsKey(key);
	}
	
	public Set<Keys> keyset() {
		return data.keySet();
	}
	
	public int size() {
		return data.size();
	}
	
	public String toString() {
		return data.toString();
	}
	
	public enum Keys {
		CUSTOMER_ID,
		CUSTOMER_NAME,
		CUSTOMER_SURNAME,
		CUSTOMER_CITY,
		CUSTOMER_ADDRESS,
		CUSTOMER_TICKETS_CINEMA,
		CUSTOMER_TICKETS_SPORT,
		CUSTOMER_TICKETS_THEATRE,
		TICKET_ID,
		TICKET_STATUS,
		TICKET_TYPE,
		TICKET_AMOUNT,
		MERCHANT_CODE;
	}
}