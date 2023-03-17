package py.com.kyron.sgp.gui.view.applicationutilities;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.applicationsecurity.service.ApplicationSecurityService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.service.PersonManagementService;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.google.common.eventbus.Subscribe;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ApplicationUtilitiesView extends VerticalLayout implements View {

	private final Logger logger = LoggerFactory.getLogger(ApplicationUtilitiesView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "applicationtools.";
	private ApplicationSecurityService applicationSecurityService;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	
	public ApplicationUtilitiesView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n ApplicationUtilitiesView()...");
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        
	        addComponent(buildToolbar());
		}catch(Exception e){
			logger.error(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null));
			logger.error(e.getMessage(), e);
			addComponent(new Label(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null)));
		}
	}
	
	private void initServices() throws Exception{
		this.applicationSecurityService = (ApplicationSecurityService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.APPLICATION_SECURITY_SERVICE);
	}//private void initServices() throws Exception
	
	 private Component buildToolbar() {
	        HorizontalLayout header = new HorizontalLayout();
	        header.addStyleName("viewheader");
	        header.setSpacing(true);
	        Responsive.makeResponsive(header);

	        Label title = new Label(this.messages.get(this.VIEW_NAME + "main.title"));/*"Registrar moneda"*/
	        title.setSizeUndefined();
	        title.addStyleName(ValoTheme.LABEL_H1);
	        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	        header.addComponent(title);
	        
	        HorizontalLayout tools = new HorizontalLayout(createRegisterCurrencyButton());	        	        
	        tools.setSpacing(true);
	        tools.addStyleName("toolbar");
	        header.addComponent(tools);
	        return header;
	 }//private Component buildToolbar()

	 private Button createRegisterCurrencyButton(){
	        final Button registerCurrency = new Button(this.messages.get(this.VIEW_NAME + "add.program.keys"));
	        registerCurrency.addClickListener(new ClickListener() {
	            @Override
	            public void buttonClick(final ClickEvent event) {
	                try{	
	            		addProgramKeys();
	            		commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
            		}catch(Exception e){		
            			commonExceptionErrorNotification.showErrorMessageNotification(e);
            		}
	            }
	        });
	        registerCurrency.setEnabled(true);
	        return registerCurrency;
	 }//private Button createRegisterCurrencyButton()
	 
	private void addProgramKeys() throws PmsServiceException{
		
		this.applicationSecurityService.insertApplicationSecurityProgramDTO(null);
	}
	 
	public ApplicationUtilitiesView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }//public void detach()

	 @Subscribe
	 public void browserResized(final BrowserResizeEvent event) {
	     // Some columns are collapsed when browser window width gets small
	     // enough to make the table fit better.
		 logger.info("\nbrowserResized");
	 }//public void browserResized(final BrowserResizeEvent event)

}
