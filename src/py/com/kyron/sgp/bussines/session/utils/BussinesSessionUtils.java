package py.com.kyron.sgp.bussines.session.utils;

import java.util.Map;

import py.com.kyron.sgp.bussines.application.utils.ApplicationUtils;

public interface BussinesSessionUtils {
	public void init();
	public void cleanUp();
	public RawSessionData getRawSessionData() /*throws Exception*/;
	public ApplicationUtils getApplicationUtils() throws Exception;;
	public void setApplicationUtils(ApplicationUtils applicationUtils) throws Exception;;
	public void setRawSessionData(RawSessionData rawSessionData) throws Exception;
	public Map<String,String> getApplicationMessagesPerViewAndUserSessionLocale(String viewName) throws Exception;
}
