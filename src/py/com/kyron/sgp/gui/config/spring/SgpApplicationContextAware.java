package py.com.kyron.sgp.gui.config.spring;

import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SgpApplicationContextAware implements ApplicationContextAware {

	private final Logger log = LoggerFactory.getLogger(SgpApplicationContextAware.class);
	private static ApplicationContext applicationContext;
	private static XmlWebApplicationContext xmlWebApplicationContext;
	
	public SgpApplicationContextAware() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
		// TODO Auto-generated method stub
		log.info("\n========================================================"
				+"\nsetting the common application context by an instance of:"
				+"\n"+applicationContext.getClass()
				+"\n========================================================");
		SgpApplicationContextAware.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public static XmlWebApplicationContext getXmlWebApplicationContext() {
		return xmlWebApplicationContext;
	}

	public static void setXmlWebApplicationContext(XmlWebApplicationContext xmlWebApplicationContext) {
		SgpApplicationContextAware.xmlWebApplicationContext = xmlWebApplicationContext;
	}

	public static String getMessage(String key, Object[] messageParams, String defaultMessage, Locale locale){
		return SgpApplicationContextAware.applicationContext.getMessage(key, messageParams, defaultMessage, locale);
	}
	
	public static Resource getResourceByClassPath(String resource){
		return SgpApplicationContextAware.applicationContext.getResource("classpath:" + resource);
	}

}
