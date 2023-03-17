package py.com.kyron.sgp.bussines.application.utils.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import py.com.kyron.sgp.bussines.application.utils.ApplicationUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;

public class ApplicationUtilsImpl implements ApplicationUtils {
	private final Logger logger = LoggerFactory.getLogger(ApplicationUtilsImpl.class);
	private Object[] applicationMessagesKeys;
	
	public ApplicationUtilsImpl() {
		// TODO Auto-generated constructor stub
	}
	
	public void init(){
		try{
			this.loadApplicationMessagesKeys();
	    	
			if(this.applicationMessagesKeys!=null)
				logger.info("\n applicationUtils has been set, applicationMessagesKeys is loaded... keys count: "+ this.applicationMessagesKeys.length);
	    	else
	    		logger.info("\n************************\nWARNING : applicationMessagesKeys HAS NOT BEEN LOADED\n************************");
			
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}


	private void loadApplicationMessagesKeys() throws IOException{
    	Properties props = new Properties();    	
    	Resource resource = SgpApplicationContextAware.getResourceByClassPath("messages.properties");
    	props.load(resource.getInputStream());
    	this.applicationMessagesKeys = props.keySet().toArray();
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		logger.info("\n singleton bean ApplicationUtilsImpl destroy method (cleanUp()) is being executed");
	}

	public Map<String,String> loadMessagesByViewAndKeysAndUserSessionLocale(String viewName, Locale locale) throws Exception{
		Map<String,String> messages = new HashMap<String,String>();
		for(Object key: this.applicationMessagesKeys){
			if (key.toString().startsWith(viewName) || key.toString().startsWith("application.")){
				String message = SgpApplicationContextAware.getMessage
								(key.toString(), null, key + ": key was not found",locale);
				messages.put(key.toString(), message);
			}//if (key.toString().startsWith(viewName) || key.toString().startsWith("application."))
		}//for(Object key: this.applicationUtils.getApplicationMessagesKeys())
		return messages;
	}//private void loadMessagesByViewAndKeysAndUserSessionLocale(String viewName) throws Exception

	public Object[] getApplicationMessagesKeys() {
		return applicationMessagesKeys;
	}

	public void setApplicationMessagesKeys(Object[] applicationMessagesKeys) {
		this.applicationMessagesKeys = applicationMessagesKeys;
	}

	@Override
	public String getMessageByKey(String key, Locale locale) {
		// TODO Auto-generated method stub
		return SgpApplicationContextAware.getMessage(key.toString(), null, key + ": key was not found",locale);
	}
}
