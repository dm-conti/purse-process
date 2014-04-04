package it.paybay.titan.model.support;

import it.paybay.titan.model.MessageHeaders;
import it.paybay.titan.model.TitanMessage;

import java.util.HashMap;
import java.util.Map;

public final class TitanMessageBuilder<T> {

	private final T payload;
	
	private final Map<String, Object> headers = new HashMap<String, Object>();


	/**
	 * Private constructor to be invoked from the static factory methods only.
	 */
	private TitanMessageBuilder(T payload) {
		this.payload = payload;
	}
	
	/**
	 * Create a builder for a new TytnMessage instance with the provided payload.
	 * 
	 * @param payload the payload for the new message
	 */
	public static <T> TitanMessageBuilder<T> withPayload(T payload) {
		if (payload == null) {
			throw new IllegalArgumentException("payload cannot be null");
		}
		return new TitanMessageBuilder<T>(payload);
	}
	
	/**
	 * Set the value for the given header name. If the provided value is <code>null</code>, the header will be removed.
	 */
	public TitanMessageBuilder<T> setHeader(String headerName, Object headerValue) {
		if (headerName == null || headerName.isEmpty()) {
			throw new IllegalArgumentException("key cannot be null or empty");
		}
		if (headerValue == null) {
			this.headers.remove(headerName);
			return this;
		}
		boolean check = this.verifyType(headerName, headerValue);
		if (!check){
			throw new IllegalArgumentException("the value of the header " + headerName + " is not of the expected type");
		}
		this.headers.put(headerName, headerValue);
		return this;
	}
	
	/**
	 * Set the value for the given headers name. If the provided value is <code>null</code>, the header will be removed.
	 */
	public TitanMessageBuilder<T> setHeaders(Map<String,Object> headers){
		if (headers == null) {
			throw new IllegalArgumentException("the input map cannot be null");
		}
		for (String key : headers.keySet()) {
			setHeader(key, headers.get(key));
		}
		return this;
	}
	
	public TitanMessage<T> build() {
		return new TitanMessage<T>(this.payload, this.headers);
	}

	private boolean verifyType(String headerName, Object headerValue) {
		if (MessageHeaders.REPLY_CHANNEL.equals(headerName) && !(headerValue instanceof String)) {
			return false;
		}
		else if (MessageHeaders.BUSINESS_OPERATION.equals(headerName) && !(headerValue instanceof String)) {
			return false;
		}
		return true;
	}

}
