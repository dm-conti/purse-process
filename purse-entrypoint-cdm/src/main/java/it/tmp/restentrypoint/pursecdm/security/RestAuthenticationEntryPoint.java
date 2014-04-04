package it.tmp.restentrypoint.pursecdm.security;

import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;

/**
 * CXF Interceptor that provides HTTP Basic Authentication validation.
 * 
 * @see cxf.xml
 *
 * @author Salvatore Esposito [salvatore.esposito@quigroup.it]
 * 
 */
public class RestAuthenticationEntryPoint extends SoapHeaderInterceptor {

    @Override 
    public void handleMessage(Message message) throws Fault {
        
    	/**
         * Add here the logic needed to invoke 
         * the authentication system (e.g. the IAM system). 
         */
    }
    
}
