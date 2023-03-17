package py.com.kyron.sgp.bussines.session.utils.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ApplicationUtils;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.bussines.session.utils.RawSessionData;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;

public class BussinesSessionUtilsImpl implements BussinesSessionUtils {
	private final Logger logger = LoggerFactory.getLogger(BussinesSessionUtilsImpl.class);
	private RawSessionData rawSessionData;
	private Map<String, Map<String, String>> messagesPerViewAndUserSessionLocale;
	private ApplicationUtils applicationUtils; 
	
	public BussinesSessionUtilsImpl() {
		// TODO Auto-generated constructor stub
		logger.info("\n************************\nBussinesSessionUtilsImpl()...\n************************\n");
	}

	@Override
	public RawSessionData getRawSessionData() {
		// TODO Auto-generated method stub
		return this.rawSessionData;
	}

	@Override
	public void setRawSessionData(RawSessionData rawSessionData) {
		// TODO Auto-generated method stub
		this.rawSessionData = rawSessionData;
	}


	@Override
	public Map<String,String> getApplicationMessagesPerViewAndUserSessionLocale(String viewName) throws Exception{
		// TODO Auto-generated method stub
		if(this.messagesPerViewAndUserSessionLocale.get(viewName) == null){
			Map<String,String> applicationMessagesPerViewAndUserSessionLocale = 
			this.applicationUtils.loadMessagesByViewAndKeysAndUserSessionLocale(viewName, this.getRawSessionData().getUserSessionLocale());
			this.messagesPerViewAndUserSessionLocale.put(viewName,applicationMessagesPerViewAndUserSessionLocale);
			return applicationMessagesPerViewAndUserSessionLocale;
		}
		return this.messagesPerViewAndUserSessionLocale.get(viewName);
	}

	@Override
	public ApplicationUtils getApplicationUtils() throws Exception {
		// TODO Auto-generated method stub
		return this.applicationUtils;
	}

	@Override
	public void setApplicationUtils(ApplicationUtils applicationUtils)throws Exception {
		// TODO Auto-generated method stub
		this.applicationUtils = applicationUtils;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		logger.info("\nBussinesSessionUtilsImpl.init()...this.hashCode() : " + this.hashCode());
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		logger.info("\nBussinesSessionUtilsImpl.cleanUp()...this.hashCode() : " + this.hashCode());
	}
	


}
