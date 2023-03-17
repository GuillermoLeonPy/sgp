package py.com.kyron.sgp.gui.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import py.com.kyron.sgp.bussines.application.utils.ApplicationUtils;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.gui.config.beanvalidation.ClientLocaleThreadLocal;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.config.spring.SgpWebApplicationInitializer;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.UserLoginRequestedEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.view.applicationutilities.AuthenticatorHelper;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout {
	private final Logger logger = LoggerFactory.getLogger(LoginView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "login.";
	private Label errorLabel;
	
    public LoginView() {
    	try{
	    	//this.setLocale(ClientLocaleThreadLocal.get());
    		this.setLocale(Page.getCurrent().getWebBrowser().getLocale());
	    	this.prepareMessages();
	        setSizeFull();
	
	        Component loginForm = buildLoginForm();
	        addComponent(loginForm);
	        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
	
	        Notification notification = new Notification(this.messages.get(this.VIEW_NAME + "welcome.notification"));
	        notification.setDescription("<span>" + this.messages.get(this.VIEW_NAME + "welcome.notification.description") + "</span>");
	        notification.setHtmlContentAllowed(true);
	        notification.setStyleName("tray dark small closable login-help");
	        notification.setPosition(Position.BOTTOM_CENTER);
	        notification.setDelayMsec(20000);
	        notification.show(Page.getCurrent());
    	}catch(Exception e){
    		logger.error("\nerror", e);
        }
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        this.errorLabel = new Label(); 
        loginPanel.addComponent(this.errorLabel);
        //loginPanel.addComponent(new CheckBox(this.messages.get(this.VIEW_NAME + "rememberme.check"), true));
        return loginPanel;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        final TextField username = new TextField(this.messages.get(this.VIEW_NAME + "username.label"));
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final PasswordField password = new PasswordField(this.messages.get(this.VIEW_NAME + "username.password"));
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button(this.messages.get(this.VIEW_NAME + "init.session.button"));
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
            	try{            		
            		DashboardEventBus.post(new UserLoginRequestedEvent(username.getValue(), password.getValue(), (new AuthenticatorHelper()).checkAuthentication(username.getValue(), password.getValue())));
            	}catch(PmsServiceException e){		
            		//(new CommonExceptionErrorNotification()).showErrorMessageNotification(e);
            		//errorLabel.setValue((new CommonExceptionErrorNotification()).helperBuildExceptionMessage(e));
            		errorLabel.setValue(e.getMessage());
            		errorLabel.addStyleName("failure");
            		errorLabel.markAsDirty();
            		logger.error("\nerror : ", e);
            		
            	}
            }
        });
        return fields;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label(this.messages.get(this.VIEW_NAME + "welcome.label"));
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label(this.messages.get("application.name.label"));
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }

    
    /*
    private void prepareMessages() throws IOException{
    	this.prepareMessagesKeys();
    	Set<String> keySet = this.messages.keySet();
    	for(String key: keySet){
    		String message = SgpApplicationContextAware.getMessage(key, null, key + " key was not found", this.getLocale());
    		logger.info("\n" + key + ": " + message);
    		this.messages.replace(key,message);
    	}
    }
    
    private void prepareMessagesKeys() throws IOException{
    	this.messages = new HashMap<String, String>();
    	Properties props = new Properties();    	
    	Resource resource = SgpApplicationContextAware.getResourceByClassPath("messages.properties");
    	props.load(resource.getInputStream());
    	Set<Object> keySet = props.keySet();
    	for(Object key: keySet){
    		if (key.toString().startsWith(this.VIEW_NAME) || key.toString().startsWith("application."))
    			this.messages.put(key.toString(), null);
    	}
    }*/
    private void prepareMessages()throws Exception{
    	ApplicationUtils applicationUtils =	(ApplicationUtils)SgpApplicationContextAware.getXmlWebApplicationContext().getBean("applicationUtils");
    	this.messages = applicationUtils.loadMessagesByViewAndKeysAndUserSessionLocale(this.VIEW_NAME, this.getLocale());
    }
    
}
