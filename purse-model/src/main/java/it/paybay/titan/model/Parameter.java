package it.paybay.titan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Parameter {
	
	@XmlAttribute(name="keyIn")
	private String keyIn;
	
	@XmlAttribute(name="keyOut")
	private String keyOut;
	
	@XmlAttribute(name="type")
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeyIn() {
		return keyIn;
	}

	public void setKeyIn(String keyIn) {
		this.keyIn = keyIn;
	}

	public String getKeyOut() {
		return keyOut;
	}

	public void setKeyOut(String keyOut) {
		this.keyOut = keyOut;
	}
}
