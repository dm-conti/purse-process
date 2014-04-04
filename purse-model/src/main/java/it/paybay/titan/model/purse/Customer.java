package it.paybay.titan.model.purse;

public abstract class Customer  implements CDM{
	public enum Keys implements CDM.Keys {
		CUSTOMER_ID,
		CUSTOMER_NAME,
		CUSTOMER_SURNAME;
	}
}