package py.com.kyron.sgp.gui.view.applicationutilities;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.Page;
import com.vaadin.ui.Label;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.personmanagement.service.PersonManagementService;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;

public class AuthenticatorHelper {

	private final Logger logger = LoggerFactory.getLogger(AuthenticatorHelper.class);
	private PersonManagementService personManagementService;
	private Locale locale;
	
	public AuthenticatorHelper() {
		// TODO Auto-generated constructor stub
		try{
			this.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("\nerror", e);
		}
	}
	
	private void init(){
		this.personManagementService = (PersonManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PERSON_MANAGEMENT_SERVICE);
		this.locale = Page.getCurrent().getWebBrowser().getLocale();
		logger.info("\n********\nAuthenticatorHelper.init\nlocale : " + this.locale + "\n********");
	}
	
	public PersonDTO checkAuthentication(final String username, final String password) throws PmsServiceException{
		if(username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty())
			throw new PmsServiceException("application.common.authentication.username.password.required.exception", null, null,this.locale);
		PersonDTO vPersonDTO = this.personManagementService.personDTOByUsernameAndPassword(username, password,this.locale);
		if(vPersonDTO == null )
			throw new PmsServiceException("application.common.authentication.exception", null, null,this.locale);
		else
			return vPersonDTO;
	}


}
