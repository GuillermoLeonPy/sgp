package py.com.kyron.sgp.gui.utils;


import java.util.Map;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;

public class ViewMessagesHelper {

		
	public ViewMessagesHelper() {
		// TODO Auto-generated constructor stub
	}

    public static Map<String,String> prepareViewMessagesUsingBussinesSessionUtilsLocale(String viewName) throws Exception{    	
    	BussinesSessionUtils bussinesSessionUtils =	(BussinesSessionUtils)SgpApplicationContextAware.getXmlWebApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
    	return bussinesSessionUtils.getApplicationUtils().loadMessagesByViewAndKeysAndUserSessionLocale(viewName, bussinesSessionUtils.getRawSessionData().getUserSessionLocale());
    }

	
}
