package py.com.kyron.sgp.gui.view;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.personmanagement.PersonManagementView;

import com.google.common.eventbus.Subscribe;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class WelcomeView extends VerticalLayout implements View {
	private final Logger logger = LoggerFactory.getLogger(WelcomeView.class);
	private Map<String,String> messages;
	
	public WelcomeView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n WelcomeView()...");
	        //setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        this.setSpacing(true);
	        this.setMargin(true);
	        this.initMessagesServices();
	        this.setUpWelcome(this.getSessionUserName());
		}catch(Exception e){
			logger.error("\nerror: ",e);
			this.setUpWelcome(null);
		}
	}
	
	private void setUpWelcome(String sessionUserName){
        Label title = new Label(this.messages.get("application.common.welcome.message"));
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        this.addComponent(title);
        Label user = new Label(sessionUserName!=null ? sessionUserName : "no user credentials loaded");
        user.addStyleName(ValoTheme.LABEL_H3);
        user.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        this.addComponent(user);		
	}
	
	private void initMessagesServices() throws Exception{
		logger.info("\n*************************\nremember: you can get the common application messages by\n"
				+ "ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(xxx)"
				+ "\n*************************\n");
		this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale("xxx");
	}
	
	private String getSessionUserName() throws Exception{
		BussinesSessionUtils bussinesSessionUtils = (BussinesSessionUtils)
				SgpApplicationContextAware.getXmlWebApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		return 
		bussinesSessionUtils.getRawSessionData().getPersonDTO().getPersonal_name()
		+ " " + 
		bussinesSessionUtils.getRawSessionData().getPersonDTO().getPersonal_last_name();		
	}

	public WelcomeView(Component... children) {
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

	 }//public void browserResized(final BrowserResizeEvent event)

}
