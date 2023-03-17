package py.com.kyron.sgp.gui.view.application.report.management;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.PurchaseInvoiceRegisterFormViewEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.ReportExcecutionManagementViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.application.report.management.report.list.management.view.components.ReportProgramListLayout;
import py.com.kyron.sgp.gui.view.application.report.management.report.list.management.view.components.ReportProgramListLayout.ReportProgram;
import py.com.kyron.sgp.gui.view.application.report.management.report.list.management.view.components.ReportProgramListLayoutFunctions;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;

@SuppressWarnings("serial")
public class ReportListManagementView extends VerticalLayout implements View,ReportProgramListLayoutFunctions {

	private final Logger logger = LoggerFactory.getLogger(ReportListManagementView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "report.list.management.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private BussinesSessionUtils bussinesSessionUtils;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private TabSheet tabs;
	private Tab reportProgramListLayoutTab;
	private VerticalLayout mainViewLayout;
	private String selectedTabContentComponentId;
	
	public ReportListManagementView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n ReportListManagementView()...");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();			
	        this.addStyleName("v-scrollable");
	        this.setHeight("100%");
	        Responsive.makeResponsive(this);
			DashboardEventBus.register(this);
			this.setUpLayout();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	public ReportListManagementView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
	
	private void initServices() throws Exception{
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}

	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void setUpLayout(){
		this.initMainViewLayout();
		this.initTitles();
		this.initTabSheet();
		this.setUpReportProgramListLayoutTab();
	    this.mainViewLayout.addComponent(this.tabs);
	    this.removeAndReAddMainViewLayout();
	    this.refreshTabSelection();
	}
	
	private void initMainViewLayout(){
		this.mainViewLayout = new VerticalLayout();
		this.mainViewLayout.setSpacing(true);
		this.mainViewLayout.setMargin(true);
	}
	
	private void initTitles(){
		try{
			Label title = new Label(this.messages.get(this.VIEW_NAME + "main.title"));
        	title.addStyleName("h1");
        	this.mainViewLayout.addComponent(title);
		}catch(Exception e){
			commonExceptionErrorNotification.showErrorMessageNotification(e);
		}
	}
	
	private void initTabSheet(){
    	this.tabs = new TabSheet();
    	this.tabs.setId("tabs");
    	//this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
    	this.tabs.addStyleName("framed");	
	}
	
	private void refreshTabSelection(){
		logger.info("\n##############################"
					+"\n refreshing tab selection"
					+"\n##############################");
		//this.tabs.setSelectedTab(1);
		//this.tabs.markAsDirty();
		int compomentCount = this.tabs.getComponentCount();
		for(int counter = (compomentCount - 1); counter >= 0 ; counter--){
			this.tabs.setSelectedTab(counter);
			logger.info("\n tab in position: "+ counter + " has been selected...");
		}
		this.tabs.markAsDirty();
		this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
	}
	
	private void removeAndReAddMainViewLayout(){
		this.removeAllComponents();
		this.addComponent(this.mainViewLayout);
	}
	
    private SelectedTabChangeListener setUpTabsSelectionListener(){
    	return new SelectedTabChangeListener(){
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				// TODO Auto-generated method stub				
				selectedTabContentComponentId =  event.getTabSheet().getSelectedTab().getId();
				logger.info("\ntab selection listener"
						+"\n selectedTabContentComponentId : " + selectedTabContentComponentId);				

			}    		
    	};
    }
    
    private void setUpReportProgramListLayoutTab(){
    	ReportProgramListLayout vReportProgramListLayout = new ReportProgramListLayout(this.VIEW_NAME, this);
    	vReportProgramListLayout.setId("vReportProgramListLayout");
    	if(this.reportProgramListLayoutTab != null)this.tabs.removeTab(this.reportProgramListLayoutTab);
    	this.reportProgramListLayoutTab = this.tabs.addTab(
    			vReportProgramListLayout, 
    			this.messages.get(this.VIEW_NAME + "tab.report.program.list"),
    			FontAwesome.LIST_OL, 
    			0);
    }

	@Override
	public void queryReportProgram(ReportProgram vReportProgram) {
		// TODO Auto-generated method stub
		try{			
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.REPORT_EXCECUTION_MANAGEMENT.getViewName());
			DashboardEventBus.post(new ReportExcecutionManagementViewEvent(
					vReportProgram,
					DashboardViewType.REPORT_LIST_MANAGEMENT.getViewName()));
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}
}
