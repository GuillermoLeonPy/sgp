package py.com.kyron.sgp.gui.view.stockmanagement;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.PersonRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.comercialmanagement.CustomerManagementView;
import py.com.kyron.sgp.gui.view.personmanagement.components.SearchPersonToolComponent;

import com.google.common.eventbus.Subscribe;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class SupplierManagementView extends VerticalLayout implements View {

	private final Logger logger = LoggerFactory.getLogger(SupplierManagementView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "supplier.management.";
	private SearchPersonToolComponent searchPersonToolComponent;
	
	public SupplierManagementView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n SupplierManagementView()...");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        this.searchPersonToolComponent = 
	        		new SearchPersonToolComponent(this.messages.get(this.VIEW_NAME + "main.title"),
	        		DashboardViewType.SUPPLIER_MANAGEMENT.getViewName(), true, true, false, false);
	        addComponent(this.searchPersonToolComponent);
		}catch(Exception e){
			logger.error(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null));
			logger.error(e.getMessage(), e);
			addComponent(new Label(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null)));
		}
	}

	public SupplierManagementView(Component... children) {
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
	@Subscribe
	public void backNavigationFromRegisterCurrencyFormView(final PersonRegisterFormViewEvent personRegisterFormViewEvent){
		logger.info("\nSupplierManagementView.backNavigationFromRegisterCurrencyFormView"
		+ "\npersonRegisterFormViewEvent.getCallerView() : " + personRegisterFormViewEvent.getCallerView());
	}
}
