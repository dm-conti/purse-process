package it.paybay.titan.model;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name="service")
@XmlSeeAlso(value=Operation.class)
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceDefinition {
	
	@XmlElement(name="operation")
	private List<Operation> operations;

	public  List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(
			List<Operation> operations) {
		this.operations = operations;
	}
}
