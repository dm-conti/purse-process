package it.paybay.titan.model;

import java.util.List;

public class ExchangeAgreement {
	private String idSourceEmitter;
	private String idSourceProgam;
	private String idTargetEmitter;
	private String idTargetProgram;
	private String fromDate;
	private String toDate;
	private String agreementFile;
	private ConversionFactor conversionType;
	private List<ExchangeThreshold> thresholds;
	
	public String getIdSourceEmitter() {
		return idSourceEmitter;
	}
	public void setIdSourceEmitter(String idSourceEmitter) {
		this.idSourceEmitter = idSourceEmitter;
	}
	public String getIdSourceProgam() {
		return idSourceProgam;
	}
	public void setIdSourceProgam(String idSourceProgam) {
		this.idSourceProgam = idSourceProgam;
	}
	public String getIdTargetEmitter() {
		return idTargetEmitter;
	}
	public void setIdTargetEmitter(String idTargetEmitter) {
		this.idTargetEmitter = idTargetEmitter;
	}
	public String getIdTargetProgram() {
		return idTargetProgram;
	}
	public void setIdTargetProgram(String idTargetProgram) {
		this.idTargetProgram = idTargetProgram;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getAgreementFile() {
		return agreementFile;
	}
	public void setAgreementFile(String agreementFile) {
		this.agreementFile = agreementFile;
	}
	public ConversionFactor getConversionType() {
		return conversionType;
	}
	public void setConversionType(ConversionFactor conversionType) {
		this.conversionType = conversionType;
	}
	public List<ExchangeThreshold> getThresholds() {
		return thresholds;
	}
	public void setThresholds(List<ExchangeThreshold> thresholds) {
		this.thresholds = thresholds;
	}
}