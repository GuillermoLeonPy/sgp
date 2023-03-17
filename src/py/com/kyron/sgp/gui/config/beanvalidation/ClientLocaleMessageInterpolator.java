package py.com.kyron.sgp.gui.config.beanvalidation;

import java.util.Locale;

import javax.validation.MessageInterpolator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.VaadinSession;

public class ClientLocaleMessageInterpolator implements MessageInterpolator {

	static final Logger logger = LoggerFactory.getLogger(ClientLocaleMessageInterpolator.class);
	private final MessageInterpolator delegate;
	//private final Locale locale;
	  
//	public ClientLocaleMessageInterpolator(MessageInterpolator delegate,Locale locale) {  
//		//logger.info("\nClientLocaleMessageInterpolator(MessageInterpolator delegate); delegate class: " + delegate.getClass());
//		System.out.println("\nClientLocaleMessageInterpolator(MessageInterpolator delegate); delegate class: " + delegate.getClass()
//				+"\nlocale : "+locale);
//		this.delegate = delegate;
//		//this.locale = locale;
//	} 
	
	public ClientLocaleMessageInterpolator(){
		this.delegate = new org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator();
		//this.locale = new Locale("es_py");
		//this.locale = ClientLocaleThreadLocal.get();
		//System.out.println("\npublic ClientLocaleMessageInterpolator()\nthis.locale : " + this.locale);
	}
	  
//	public ClientLocaleMessageInterpolator() {
//		// TODO Auto-generated constructor stub
//	}

	@Override
	public String interpolate(String message, Context context) {
		// TODO Auto-generated method stub
		//return this.interpolate(message, context, ClientLocaleThreadLocal.get());
		return this.interpolate(message, context, (Locale)VaadinSession.getCurrent().getAttribute("userSessionLocale"));
		//return this.interpolate(message, context, this.locale);
	}

	@Override
	public String interpolate(String message, Context context, Locale locale) {
		// TODO Auto-generated method stub
		//logger.info("\nClientLocaleMessageInterpolator.interpolate\nlocale : "+ locale);
//		logger.info("\nClientLocaleMessageInterpolator.interpolate(String message, Context context, Locale locale)"
//		+"\nlocale : "+ locale
//		+"\nClientLocaleThreadLocal.get() : "+ ClientLocaleThreadLocal.get() 
//		+  "\nthis.hashCode() : " + this.hashCode());
		logger.info("\nClientLocaleMessageInterpolator.interpolate(String message, Context context, Locale locale)"
		+"\nlocale : "+ locale
		+"\nVaadinSession.getCurrent().getAttribute(userSessionLocale) : "+ VaadinSession.getCurrent().getAttribute("userSessionLocale") 
		+  "\nthis.hashCode() : " + this.hashCode());
		return delegate.interpolate(message, context, (Locale)VaadinSession.getCurrent().getAttribute("userSessionLocale"));
	}

}
