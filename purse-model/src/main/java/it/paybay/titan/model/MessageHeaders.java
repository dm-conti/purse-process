package it.paybay.titan.model;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The headers for a {@link TitanMessage}.<br>
 * IMPORTANT: MessageHeaders are immutable. Any mutating operation (e.g., put(..), putAll(..) etc.)
 * will result in {@link UnsupportedOperationException}
 * To create TytnMessage instance use fluent TytnMessageBuilder API
 * <pre>
 * TytnMessageBuilder.withPayload("foo").setHeader("key1", "value1").setHeader("key2", "value2");
 * </pre>
 */
public final class MessageHeaders implements Map<String, Object>, Serializable {

	private static final long serialVersionUID = 6901029029524535147L;

	/** The key that maps the value of the reply channel */
	public static final String REPLY_CHANNEL = "replyChannel";
	
	/** The key that maps the value of the business operation */
	public static final String BUSINESS_OPERATION = "businessOperation";
	
	/** The key that maps the value of the operation for Command Message */
	public static final String OPERATION_NAME = "operationName";

	/** The key that maps the value of the next actions */
	public static final String NEXT_ACTIONS = "nextActions";
	
	/** The key that maps the value of the status of operation */
	public static final String STATUS = "status";
	
	/** The key that maps the errorMessage  */
	public static final String ERROR_MESSAGE = "errorMessage";
	
	private static final String ERROR_SET_HEADERS = "MessageHeaders is immutable.";
	
	/** The headers object */
	private final Map<String, Object> headers;

	/**
	 * Create a new MessageHeaders with the given headers. 
	 *  
	 * @param headers message headers
	 */
	public MessageHeaders(Map<String, Object> headers) {
		this.headers = (headers != null) ? new HashMap<String, Object>(headers) : new HashMap<String, Object>();
	}

	/**
	 * @return the reply channel value
	 */
	public Object getReplyChannel() {
		return this.get(REPLY_CHANNEL);
	}
	
	/**
	 * @return the business operation value
	 */
	public Object getBusinessOperation() {
		return this.get(BUSINESS_OPERATION);
	}
	
	/**
	 * @param key the key of header
	 * @param type the desired type of the value
	 * @return the value mapped with key
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Object key, Class<T> type) {
		Object value = this.headers.get(key);
		if (value == null) {
			return null;
		}
		if (!type.isAssignableFrom(value.getClass())) {
			throw new IllegalArgumentException("Incorrect type specified for header '" + key + "'. Expected [" + type
					+ "] but actual type is [" + value.getClass() + "]");
		}
		return (T) value;
	}

	/*
	 * Map implementation
	 */

	public boolean containsKey(Object key) {
		return this.headers.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return this.headers.containsValue(value);
	}

	@Override
	public Set<Map.Entry<String, Object>> entrySet() {
		return Collections.unmodifiableSet(this.headers.entrySet());
	}

	public Object get(Object key) {
		return this.headers.get(key);
	}

	public boolean isEmpty() {
		return this.headers.isEmpty();
	}
	
	@Override
	public Set<String> keySet() {
		return Collections.unmodifiableSet(this.headers.keySet());
	}

	public int size() {
		return this.headers.size();
	}

	@Override
	public Collection<Object> values() {
		return Collections.unmodifiableCollection(this.headers.values());
	}

	/*
	 * Unsupported operations
	 */
	/**
	 * Since MessageHeaders are immutable the call to this method will result in {@link UnsupportedOperationException}
	 */
	@Override
	public Object put(String key, Object value) {
		throw new UnsupportedOperationException(ERROR_SET_HEADERS);
	}
	/**
	 * Since MessageHeaders are immutable the call to this method will result in {@link UnsupportedOperationException}
	 */
	@Override
	public void putAll(Map<? extends String, ? extends Object> t) {
		throw new UnsupportedOperationException(ERROR_SET_HEADERS);
	}
	/**
	 * Since MessageHeaders are immutable the call to this method will result in {@link UnsupportedOperationException}
	 */
	@Override
	public Object remove(Object key) {
		throw new UnsupportedOperationException(ERROR_SET_HEADERS);
	}
	/**
	 * Since MessageHeaders are immutable the call to this method will result in {@link UnsupportedOperationException}
	 */
	@Override
	public void clear() {
		throw new UnsupportedOperationException(ERROR_SET_HEADERS);
	}

	/*
	 * Serialization methods
	 */

	private void writeObject(ObjectOutputStream out) throws IOException {
		List<String> keysToRemove = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : this.headers.entrySet()) {
			if (!(entry.getValue() instanceof Serializable)) {
				keysToRemove.add(entry.getKey());
			}
		}
		for (String key : keysToRemove) {
			this.headers.remove(key);
		}
		out.defaultWriteObject();
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.headers.hashCode();
	}

	/**
	 * The equals method for MessageHeaders object
	 * 
	 * @param object the object
	 * 
	 * @return true, if equals
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object instanceof MessageHeaders) {
			MessageHeaders other = (MessageHeaders) object;
			return this.headers.equals(other.headers);
		}
		return false;
	}

	@Override
	public String toString() {
		return this.headers.toString();
	}
}