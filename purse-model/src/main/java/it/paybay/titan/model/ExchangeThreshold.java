package it.paybay.titan.model;

public class ExchangeThreshold {
	private String thresholdMin;
    private String thresholdMax;
    private Integer quantity;
	
    public String getThresholdMin() {
		return thresholdMin;
	}
	public void setThresholdMin(String thresholdMin) {
		this.thresholdMin = thresholdMin;
	}
	public String getThresholdMax() {
		return thresholdMax;
	}
	public void setThresholdMax(String thresholdMax) {
		this.thresholdMax = thresholdMax;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}