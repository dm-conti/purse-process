package it.paybay.titan.model;

import it.paybay.titan.model.support.CurrencySymbols;

public class ConversionFactor {
	private CurrencySymbols symbol;
	private Long factor;
	
	public CurrencySymbols getSymbol() {
		return symbol;
	}
	public void setSymbol(CurrencySymbols symbol) {
		this.symbol = symbol;
	}
	public Long getFactor() {
		return factor;
	}
	public void setFactor(Long factor) {
		this.factor = factor;
	}
}