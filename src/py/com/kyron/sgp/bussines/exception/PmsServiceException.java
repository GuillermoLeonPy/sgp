package py.com.kyron.sgp.bussines.exception;

import java.util.Locale;

import com.vaadin.server.VaadinSession;

import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;

@SuppressWarnings("serial")
public class PmsServiceException extends Exception {

	public PmsServiceException() {
		// TODO Auto-generated constructor stub
	}

	public PmsServiceException(String messageKey) {		
		super(SgpApplicationContextAware.getMessage(messageKey, null, messageKey + ": key was not found",(Locale)VaadinSession.getCurrent().getAttribute("userSessionLocale")));
		// TODO Auto-generated constructor stub
	}

	public PmsServiceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public PmsServiceException(String messageKey, Throwable cause) {
		super(SgpApplicationContextAware.getMessage(messageKey, null, messageKey + ": key was not found",(Locale)VaadinSession.getCurrent().getAttribute("userSessionLocale")), 
			cause);
		// TODO Auto-generated constructor stub
	}

	public PmsServiceException(String messageKey, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(SgpApplicationContextAware.getMessage(messageKey, null, messageKey + ": key was not found",(Locale)VaadinSession.getCurrent().getAttribute("userSessionLocale")), 
		cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public PmsServiceException(String messageKey, Throwable cause, Object[] messageParams) {
		super(SgpApplicationContextAware.getMessage(messageKey, messageParams, messageKey + ": key was not found",(Locale)VaadinSession.getCurrent().getAttribute("userSessionLocale")), 
			cause);
		// TODO Auto-generated constructor stub
	}
	
	public PmsServiceException(String messageKey, Throwable cause, Object[] messageParams, Locale locale) {
		super(SgpApplicationContextAware.getMessage(messageKey, messageParams, messageKey + ": key was not found",locale), 
			cause);
		// TODO Auto-generated constructor stub
	}
}
