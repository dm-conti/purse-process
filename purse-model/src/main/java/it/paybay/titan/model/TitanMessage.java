package it.paybay.titan.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * TytnMessage class defining properties such as payload and headers of a message.
 */
public class TitanMessage <T> implements Serializable {
 
 	private static final long serialVersionUID = 3649200745084232821L;

 	/** The payload of a TytnMessage */
 	private final T payload;
 	
 	/** The headers of a TytnMessage */
 	private final MessageHeaders headers;

 	/**
	 * Create a new TytnMessage with the given payload.
	 * 
	 * @param payload the message payload
	 */
	public TitanMessage(T payload) {
		this(payload, null);
	}

	/**
	 * Create a new TytnMessage with the given payload. The provided map
	 * will be used to populate the message headers
	 * 
	 * @param payload the message payload
	 * @param headers message headers
	 * @see MessageHeaders
	 */
	public TitanMessage(T payload, Map<String, Object> headers) {
		if (headers == null) {
			this.headers = new MessageHeaders(new HashMap<String, Object>());
		}
		else {
			this.headers = new MessageHeaders(new HashMap<String, Object>(headers));
		}
		this.payload = payload;
	}

	/**
	 * @return the headers
	 * @see MessageHeaders
	 */
	public MessageHeaders getHeaders() {
		return this.headers;
	}

	/**
	 * @return the payload
	 */
	public T getPayload() {
		return this.payload;
	}
	
	
	public String toString() {
		return "[Payload=" + this.payload + "][Headers=" + this.headers + "]";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + ((headers == null) ? 0 : headers.hashCode());
		result = prime * result + ((payload == null) ? 0 : payload.hashCode());
		return result;
	}

	/**
	 * The equals method for TytnMessage object
	 * 
	 * @param object the object
	 * 
	 * @return true, if equals
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		TitanMessage<T> other = null;
		return (this == obj)
	               || ((obj instanceof TitanMessage<?>)
	                   && ((other = (TitanMessage<T>)obj) != null)
	                   && checkEquals(this.headers, other.getHeaders())
	                   && checkEquals(this.payload, other.getPayload()));
	}
	
	/**
	 * Check equals.
	 * 
	 * @param thiis
	 *             the thiis
	 * @param that
	 *             the that
	 * 
	 * @return true, if check equals
	 */
	private boolean checkEquals(Object thiis, Object that) {
		return (thiis == that) || ((thiis != null) && thiis.equals(that));
	}
}
