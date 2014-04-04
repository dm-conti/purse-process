package it.paybay.titan.model.purse;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CDMMapUtils {
	private final List<Class<?>> usedClasses;
	private Map<CDM,Object> properties;
	
	@SafeVarargs
	public <T> CDMMapUtils( Class<? extends T>... classes ){
		usedClasses = new LinkedList<>();
		
		for(Class<?> clazz : classes)
			usedClasses.add(clazz);
	}
	
	public <T> void readInteranlKeys() throws NoSuchFieldException, SecurityException{
		for(Class<?> clazz : usedClasses){
			System.out.println(clazz.getSimpleName());
			
			for(Field field : clazz.getDeclaredFields()){
				System.out.println(field.getType());
				System.out.println(field.getName());
			}
		}
	}
	
	public void put(CDM.Keys enumKey){
		System.out.println("La chiave inserita e' del tipo "+enumKey.getClass());
		System.out.println(enumKey);
		/*Returns the immediately enclosing class of the underlying class.*/
		if(!usedClasses.contains(enumKey.getClass().getEnclosingClass()))
			System.out.println("La mappa non puo contenere chiavi del tipo "+enumKey.getClass().getEnclosingClass().getSimpleName());
	}
}