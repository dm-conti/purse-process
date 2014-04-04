package it.tmp.restentrypoint.pursecdm.dto;

import it.paybay.titan.model.ConversionFactor;
import it.paybay.titan.model.TemporalThreshold;
import it.paybay.titan.model.TimeSlot;
import it.paybay.titan.model.support.DaysOfWeek;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is an example of DTO for service {@link SomethingFaçadeService}.
 * DTOs do not have any behaviour except for storage and retrieval of its own data.
 * 
 * @author Domenico Conti [domenico.conti@quigroup.it]
 * 
 */
@XmlRootElement
public class CreatePurseDTO {
	
	@XmlElement @NotNull @Size(min=5, max=5)
	private String idEmitter;
	
	@XmlElement @NotNull @Size(min=5)
	private String purseDesc;
	
	@XmlElement @NotNull
	private String productStartDate;
	
	@XmlElement
	private String productEndDate; //(optional) 
	
	@XmlElement
	private List<String> idsShopNetwork;
	
	@XmlElement
	private DaysOfWeek daysOfWeek;
	
	@XmlElement
	private List<ConversionFactor> conversionFactors;
	
	@XmlElement
	private TemporalThreshold dailyThreshold;
	
	@XmlElement
	private TemporalThreshold montlyThreshold;
	
	@XmlList
	private List<TimeSlot> hourlyThresholds;
	
	public String getIdEmitter() {
		return idEmitter;
	}

	public void setIdEmitter(String idEmitter) {
		this.idEmitter = idEmitter;
	}

	public String getPurseDesc() {
		return purseDesc;
	}

	public void setPurseDesc(String purseDesc) {
		this.purseDesc = purseDesc;
	}

	public String getProductStartDate() {
		return productStartDate;
	}

	public void setProductStartDate(String productStartDate) {
		this.productStartDate = productStartDate;
	}

	public String getProductEndDate() {
		return productEndDate;
	}

	public void setProductEndDate(String productEndDate) {
		this.productEndDate = productEndDate;
	}

	public List<String> getIdsShopNetwork() {
		return idsShopNetwork;
	}

	public void setIdsShopNetwork(List<String> idsShopNetwork) {
		this.idsShopNetwork = idsShopNetwork;
	}

	public DaysOfWeek getDaysOfWeek() {
		return daysOfWeek;
	}

	public void setDaysOfWeek(DaysOfWeek daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}

	public List<ConversionFactor> getConversionFactors() {
		return conversionFactors;
	}

	public void setConversionFactors(List<ConversionFactor> conversionFactors) {
		this.conversionFactors = conversionFactors;
	}

	public TemporalThreshold getDailyThreshold() {
		return dailyThreshold;
	}

	public void setDailyThreshold(TemporalThreshold dailyThreshold) {
		this.dailyThreshold = dailyThreshold;
	}

	public TemporalThreshold getMontlyThreshold() {
		return montlyThreshold;
	}
	
	public void setMontlyThreshold(TemporalThreshold montlyThreshold) {
		this.montlyThreshold = montlyThreshold;
	}

	public List<TimeSlot> getHourlyThresholds() {
		return hourlyThresholds;
	}

	public void setHourlyThresholds(List<TimeSlot> hourlyThresholds) {
		this.hourlyThresholds = hourlyThresholds;
	}
}