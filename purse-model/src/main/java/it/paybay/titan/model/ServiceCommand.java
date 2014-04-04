package it.paybay.titan.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Business model representing a service command object.
 */

public class ServiceCommand implements Serializable {

	private static final long serialVersionUID = -2606510443072788521L;

	/** The key that maps the value of the operation to be executed */
	public static final String OPERATION_NAME = "operationName";

	/** The key that maps the value of the type of operation to be executed */
	public static final String OPERATION_TYPE = "operationType";

	/** The headers of a service command */
	private Map<String, Object> headers;

	/** The parameters of a service command */
	private Map<String, Object> parameters;

	// *** GETTERS & SETTERS *******************************************/

	/**
	 * @return the headers
	 */
	public Map<String, Object> getHeaders() {
		return new HashMap<String,Object>(headers);
	}

	/**
	 * @return the operation name value
	 */
	public String getOperationName() {
		return new String(headers.get(OPERATION_NAME).toString());
	}
	
	/**
	 * @return the operation type value
	 */
	public String getOperationType() {
		return new String(headers.get(OPERATION_TYPE).toString());
	}
	
	/**
	 * @param headers
	 *             the headers to set
	 */
	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}

	/**
	 * @return the parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *             the parameters to set
	 */
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
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
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
		return result;
	}

	/**
	 * The equals method for service command object
	 * 
	 * @param object the object
	 * 
	 * @return true, if equals
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		ServiceCommand other = null;
		return (this == obj)
	               || ((obj instanceof ServiceCommand)
	                   && ((other = (ServiceCommand)obj) != null)
	                   && checkEquals(this.headers, other.getHeaders())
	                   && checkEquals(this.parameters, other.getParameters()));
	}
	
	public String toString() {
		return "[Parameters=" + ((this.parameters != null)? this.parameters.toString():"null") + "][Headers=" + ((this.headers != null)? this.headers.toString():"null") + "]";
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
