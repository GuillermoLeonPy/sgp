package py.com.kyron.sgp.gui.config.beanvalidation;

import java.util.Locale;

public class ClientLocaleThreadLocal {

	private static ThreadLocal<Locale> tLocal = new ThreadLocal<Locale>(); 
	
	public ClientLocaleThreadLocal() {
		// TODO Auto-generated constructor stub
	}
	 
	public static void set(Locale locale) {  
	    tLocal.set(locale);  
	}
	
	public static Locale get() {  
		return tLocal.get();  
	}  
		  
	public static void remove() {  
	    tLocal.remove();  
	}
}
