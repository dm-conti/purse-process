package it.paybay.titan.model.support.unit;

import it.paybay.titan.model.purse.CDMMapUtils;
import it.paybay.titan.model.purse.Customer;
import it.paybay.titan.model.purse.Ticketing;

import org.junit.Ignore;
import org.junit.Test;

public class CDMTest {
	private CDMMapUtils classUnderTest;
	
	@Test @Ignore
	public void test() throws NoSuchFieldException, SecurityException {
		classUnderTest = new CDMMapUtils(Ticketing.class);		
		classUnderTest.put(Customer.Keys.CUSTOMER_ID); //Accetta solo chiavi dell'Enum Customer.Keys
		classUnderTest.put(Ticketing.Keys.ID_TICKET);
	}
	
	public void unionTest() {
		
	}
}