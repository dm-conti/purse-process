package it.paybay.titan.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso(value=Parameter.class)
@XmlAccessorType(XmlAccessType.FIELD)
public class Operation {
	
	@XmlAttribute(name="name")
	private String name;
	
	@XmlElementWrapper(name="parameters")
	@XmlElement(name="param")
	private List<Parameter> parameters;
	
	@XmlElementWrapper(name="payload")
	@XmlElement(name="param")
	private List<Parameter> payload;
	
	@XmlElementWrapper(name="output")
	@XmlElement(name="param")
	private List<Parameter> output;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Parameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<Parameter> params) {
		this.parameters = params;
	}
	
	public List<Parameter> getPayload() {
		return payload;
	}
	
	public void setPayload(List<Parameter> payload) {
		this.payload = payload;
	}
	
	public List<Parameter> getOutput() {
		return output;
	}
	
	public void setOutput(List<Parameter> output) {
		this.output = output;
	}
}
