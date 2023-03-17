package py.com.kyron.sgp.bussines.application.utils;

import java.util.Locale;
import java.util.Map;

public interface ApplicationUtils {
	public void init();
	public void cleanUp();
	public Map<String,String> loadMessagesByViewAndKeysAndUserSessionLocale(String viewName, Locale locale) throws Exception;
	public Object[] getApplicationMessagesKeys();
	public String getMessageByKey(String key, Locale locale);
}
