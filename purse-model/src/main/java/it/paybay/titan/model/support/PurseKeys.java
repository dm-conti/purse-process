package it.paybay.titan.model.support;

public enum PurseKeys {
	PURSE_DESC("purseDesc"),
	PRODUCT_START_DATE("productStartDate"),
	ID_SHOP_NETWORK("idShopNetwork"),
	ID_EMITTER("idEmitter");
	
	private String key;
	 
	private PurseKeys(String key) {
		this.key = key;
	}
 
	public String key() {
		return key;
	}
}