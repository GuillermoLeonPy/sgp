package py.com.kyron.sgp.gui.utils;

import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;

import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.ui.Notification;

public class CommonExceptionErrorNotification {

	private final Logger logger = LoggerFactory.getLogger(CommonExceptionErrorNotification.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "xxx.";//doesnt matter
	private Locale locale;
	
	public CommonExceptionErrorNotification() {
		// TODO Auto-generated constructor stub
		try {
			this.prepareMessages();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("\nerror in CommonExceptionErrorNotification", e);
		}
	}

    public void showErrorMessageNotification(Exception e){
		logger.error("\nerror", e);		            		
		//Notification.show("Error: ",buildExceptionMessage("button.save.error.message", e), Notification.Type.ERROR_MESSAGE);/*"Favor re ingrese campos indicados"*/
		Notification.show("! ",buildExceptionMessage(e), Notification.Type.ERROR_MESSAGE);/*"Favor re ingrese campos indicados"*/
    }
    
    public void showCommonApplicationNotificactionByKey(String messageKey){
    	Notification.show("",this.messages.get(messageKey), Notification.Type.WARNING_MESSAGE);
    }
    
    public void showCommonApplicationSuccesfulOperationNotificaction(){
    	this.showCommonApplicationNotificactionByKey("application.common.operation.succesful");
    }
    
    private String buildExceptionMessage(Exception e){    	
    	String message = "";
    	if (e instanceof PmsServiceException)
    		message+=e.getMessage();
    	else if(e instanceof CommitException)
    		message+=this.messages.get("application.common.form.error.message");
    	else if(e instanceof com.vaadin.data.Validator.EmptyValueException)
    		message+=e.getMessage();
    	else if(e instanceof com.vaadin.data.Validator.InvalidValueException)
    		message+=e.getMessage();
    	else
    		message+=this.messages.get("application.common.gui.exception.unexpected");
    	return message;
    }
    
    public String helperBuildExceptionMessage(Exception e){
    	String message = "";
    	if (e instanceof PmsServiceException)
    		message+=e.getMessage();
    	else if(e instanceof CommitException)
    		message+=this.messages.get("application.common.form.error.message");
    	else
    		message+=this.messages.get("application.common.gui.exception.unexpected");
    	return message;    	
    }
    
    private void prepareMessages() throws Exception{    	
    	BussinesSessionUtils bussinesSessionUtils =	(BussinesSessionUtils)SgpApplicationContextAware.getXmlWebApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
    	this.setLocale(bussinesSessionUtils.getRawSessionData().getUserSessionLocale());
    	this.messages = bussinesSessionUtils.getApplicationUtils().loadMessagesByViewAndKeysAndUserSessionLocale(this.VIEW_NAME, this.getLocale());
    }

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
