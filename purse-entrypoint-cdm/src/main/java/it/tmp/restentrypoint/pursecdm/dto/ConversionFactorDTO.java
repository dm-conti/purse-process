package it.tmp.restentrypoint.pursecdm.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is an example of DTO for service {@link SomethingFaçadeService}.
 * DTOs do not have any behaviour except for storage and retrieval of its own data.
 * 
 * @author Mario Gallo [consulenti.mgallo@quigroup.it]
 * 
 */
@XmlRootElement
public class ConversionFactorDTO {
	
	@XmlElement
	@NotNull
	@Size(min=5, max=5)
	private String code;
	
	@XmlElement
	@NotNull
	@DecimalMin(value="1")
	private double amount;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
