package py.com.kyron.sgp.gui;

import java.util.Date;
import java.util.Locale;

import javax.validation.Configuration;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.bussines.session.utils.RawSessionData;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.beanvalidation.ClientLocaleMessageInterpolator;
import py.com.kyron.sgp.gui.config.beanvalidation.MyHibernateConfig;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.data.DataProvider;
import py.com.kyron.sgp.gui.data.dummy.DummyDataProvider;
import py.com.kyron.sgp.gui.domain.User;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.event.DashboardEvent.CloseOpenWindowsEvent;
import py.com.kyron.sgp.gui.event.DashboardEvent.UserLoggedOutEvent;
import py.com.kyron.sgp.gui.event.DashboardEvent.UserLoginRequestedEvent;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.LoginView;
import py.com.kyron.sgp.gui.view.MainView;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@Theme("dashboard")
@Widgetset("py.com.kyron.sgp.gui.DashboardWidgetSet")
@Title("Sistema de gestión de producción")
@SuppressWarnings("serial")
public final class DashboardUI extends UI {
	private final Logger logger = LoggerFactory.getLogger(DashboardUI.class);
    /*
     * This field stores an access to the dummy backend layer. In real
     * applications you most likely gain access to your beans trough lookup or
     * injection; and not in the UI but somewhere closer to where they're
     * actually accessed.
     */
    private final DataProvider dataProvider = new DummyDataProvider();
    private final DashboardEventBus dashboardEventbus = new DashboardEventBus();

    @Override
    protected void init(final VaadinRequest request) {
        //setLocale(Locale.US);   
    	setLocale(Page.getCurrent().getWebBrowser().getLocale());
    	getSession().setLocale(getLocale());    	
        DashboardEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);
        updateContent();
        // Some views need to be aware of browser resize events so a
        // BrowserResizeEvent gets fired to the event bus on every occasion.
        Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
                    @Override
                    public void browserWindowResized(final BrowserWindowResizeEvent event) {
                        DashboardEventBus.post(new BrowserResizeEvent());
                    }
                });
    }//protected void init(final VaadinRequest request) 

    /**
     * Updates the correct content for this UI based on the current user status.
     * If the user is logged in with appropriate privileges, main view is shown.
     * Otherwise login view is shown.
     */
  
//    private void configureHibernateMessageInterpolator(){
////    	logger.info("\nconfigureHibernateMessageInterpolator()"
////    			+"\nValidation.byDefaultProvider().getClass(): " + Validation.byDefaultProvider().getClass());
//    	Configuration<HibernateValidatorConfiguration> config = Validation.byProvider(HibernateValidator.class).configure();
//    	//Configuration<?> config = Validation.byDefaultProvider().configure();
//    	ClientLocaleMessageInterpolator myInterpolator = 
//    	new ClientLocaleMessageInterpolator(config.getDefaultMessageInterpolator(), this.getLocale()/*new Locale("es")*/);
//    	config = config.messageInterpolator(myInterpolator);
//    	ValidatorFactory validatorFactory = config.buildValidatorFactory();
//    	MyHibernateConfig.setValidatorFactory(validatorFactory);
//    	Validator validator = validatorFactory.getValidator(); 
//    }
    
    private void updateContent() {
        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        if (user != null && "admin".equals(user.getRole())) {
            // Authenticated user
            setContent(new MainView());
            removeStyleName("loginview");
            /*getNavigator().navigateTo(getNavigator().getState());*/
            getNavigator().navigateTo(DashboardViewType.WELCOME_VIEW.getViewName());
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    	logger.info("\n==================================\n current user: "
    	+ (VaadinSession.getCurrent() != null && 
    		VaadinSession.getCurrent().getAttribute("app_user_name") != null ? 
    		VaadinSession.getCurrent().getAttribute("app_user_name"): "no current user session")
    	+ "\n==================================\n");
    }

    @Subscribe
    public void userLoginRequested(final UserLoginRequestedEvent event) {
        User user = getDataProvider().authenticate(event.getUserName(), event.getPassword());        
        this.setSpringsSessionScopeRawSessionData(user, event.getPersonDTO());
        user.setFirstName(SgpUtils.getFirstWordFromString(event.getPersonDTO().getPersonal_name()));
        user.setLastName(SgpUtils.getFirstWordFromString(event.getPersonDTO().getPersonal_last_name()));
        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
        VaadinSession.getCurrent().setAttribute("app_user_name",event.getPersonDTO().getApplication_user_name());
        updateContent();
    }

    private void setSpringsSessionScopeRawSessionData(final User user, final PersonDTO personDTO){
    	try{
    		logger.info("\nDashboardUI.setSpringsSessionScopeRawSessionData(User user)");
    		VaadinSession.getCurrent().setAttribute("userSessionLocale", Page.getCurrent().getWebBrowser().getLocale());
    		
    		BussinesSessionUtils bussinesSessionUtils = (BussinesSessionUtils)
            SgpApplicationContextAware.getXmlWebApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
    		
    		bussinesSessionUtils.setRawSessionData(new RawSessionData(user.getFirstName() + user.getLastName(), new Date(), Page.getCurrent().getWebBrowser().getLocale(),personDTO));
    		
    	}catch(Exception e){
    		logger.error("\nerror", e);
    	}
    }
    
    @Subscribe
    public void userLoggedOut(final UserLoggedOutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Subscribe
    public void closeOpenWindows(final CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }

    /**
     * @return An instance for accessing the (dummy) services layer.
     */
    public static DataProvider getDataProvider() {
        return ((DashboardUI) getCurrent()).dataProvider;
    }

    public static DashboardEventBus getDashboardEventbus() {
        return ((DashboardUI) getCurrent()).dashboardEventbus;
    }
}
