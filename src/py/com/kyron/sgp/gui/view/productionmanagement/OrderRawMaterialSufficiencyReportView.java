package py.com.kyron.sgp.gui.view.productionmanagement;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.OrderRawMaterialSufficiencyReportDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.OrderRawMaterialSufficiencyReportViewEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.SaleInvoiceRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.cash.movements.management.SaleInvoiceRegisterFormView;
import py.com.kyron.sgp.gui.view.productionmanagement.order.raw.material.sufficiency.report.view.components.OrderItemRawMaterialSufficiencyReportDetailDTOTableTabLayout;
import py.com.kyron.sgp.gui.view.productionmanagement.order.raw.material.sufficiency.report.view.components.OrderRawMaterialSufficiencyReportDTOTabLayout;
import py.com.kyron.sgp.gui.view.productionmanagement.order.raw.material.sufficiency.report.view.components.OrderRawMaterialSufficiencyReportViewFunctions;

import com.google.common.eventbus.Subscribe;
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
public class OrderRawMaterialSufficiencyReportView extends VerticalLayout
		implements View,OrderRawMaterialSufficiencyReportViewFunctions {

	private final Logger logger = LoggerFactory.getLogger(OrderRawMaterialSufficiencyReportView.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME = "order.raw.material.sufficiency.report.view.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private OrderDTO orderDTO;
	private OrderRawMaterialSufficiencyReportDTO orderRawMaterialSufficiencyReportDTO;
	private TabSheet tabs;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private VerticalLayout mainViewLayout;
	private String CALLER_VIEW;
	private Tab orderRawMaterialSufficiencyReportDTOTabLayoutTab;
	private Tab orderItemRawMaterialSufficiencyReportDetailDTOTableTabLayoutTab;
	private String selectedTabContentComponentId;
	private SgpUtils sgpUtils;
	private BussinesSessionUtils bussinesSessionUtils;
	private ComercialManagementService comercialManagementService;
	
	
	public OrderRawMaterialSufficiencyReportView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n OrderRawMaterialSufficiencyReportView..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.sgpUtils = new SgpUtils();
			this.initServices();
	        this.addStyleName("v-scrollable");
	        this.setHeight("100%");	        
	        Responsive.makeResponsive(this);
			DashboardEventBus.register(this);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	public OrderRawMaterialSufficiencyReportView(Component... children) {
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
	
	private void initServices() throws Exception{
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		this.comercialManagementService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
	}
	
	@Subscribe
	public void handleCallingEvent(final OrderRawMaterialSufficiencyReportViewEvent vOrderRawMaterialSufficiencyReportViewEvent){
		try{
			this.CALLER_VIEW = vOrderRawMaterialSufficiencyReportViewEvent.getCallerView();
			this.orderDTO = vOrderRawMaterialSufficiencyReportViewEvent.getOrderDTO();
			this.retrieveOrderRawMaterialSufficiencyReportDTO();
			this.initMainViewLayout();
			this.initTitles();
			this.initTabSheet();
			this.setUpOrderRawMaterialSufficiencyReportDTOTabLayout();
			this.setUpOrderItemRawMaterialSufficiencyReportDetailDTOTableTabLayout();
		    this.mainViewLayout.addComponent(this.tabs);
		    this.removeAndReAddMainViewLayout();
		    this.refreshTabSelection();
		}catch(Exception e){
			logger.error("\nerror", e);
		}		
	}

	
	private void initMainViewLayout(){
		this.mainViewLayout = new VerticalLayout();
		this.mainViewLayout.setSpacing(true);
		this.mainViewLayout.setMargin(true);
	}
	
	private void removeAndReAddMainViewLayout(){
		this.removeAllComponents();
		this.addComponent(this.mainViewLayout);
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
    	this.tabs.addStyleName("framed");
	}

	@Override
	public void navigateToCallerView() {
		// TODO Auto-generated method stub
		logger.info("\n OrderRawMaterialSufficiencyReportView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new OrderRawMaterialSufficiencyReportViewEvent(
					this.orderDTO, 
					DashboardViewType.ORDER_RAW_MATERIAL_SUFFICIENCY_REPORT_VIEW.getViewName()));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	private void retrieveOrderRawMaterialSufficiencyReportDTO() throws PmsServiceException{
		this.orderRawMaterialSufficiencyReportDTO = this.comercialManagementService.getOrderRawMaterialSufficiencyReportDTO(this.orderDTO);
	}

	private void setUpOrderRawMaterialSufficiencyReportDTOTabLayout(){
		OrderRawMaterialSufficiencyReportDTOTabLayout vOrderRawMaterialSufficiencyReportDTOTabLayout = 
				new OrderRawMaterialSufficiencyReportDTOTabLayout(this,this.VIEW_NAME,this.orderDTO,this.orderRawMaterialSufficiencyReportDTO);
		vOrderRawMaterialSufficiencyReportDTOTabLayout.setId("vOrderRawMaterialSufficiencyReportDTOTabLayout");
		this.orderRawMaterialSufficiencyReportDTOTabLayoutTab = 
				this.tabs.addTab(vOrderRawMaterialSufficiencyReportDTOTabLayout, 
						this.messages.get(this.VIEW_NAME + "tab.order.raw.material.sufficiency.report"),
						FontAwesome.INDUSTRY, 
						0);
	}
	

	private void setUpOrderItemRawMaterialSufficiencyReportDetailDTOTableTabLayout(){
		OrderItemRawMaterialSufficiencyReportDetailDTOTableTabLayout vOrderItemRawMaterialSufficiencyReportDetailDTOTableTabLayout = 
				new OrderItemRawMaterialSufficiencyReportDetailDTOTableTabLayout(this,this.VIEW_NAME,this.orderRawMaterialSufficiencyReportDTO);
		vOrderItemRawMaterialSufficiencyReportDetailDTOTableTabLayout.setId("vOrderItemRawMaterialSufficiencyReportDetailDTOTableTabLayout");
		this.orderItemRawMaterialSufficiencyReportDetailDTOTableTabLayoutTab = 
				this.tabs.addTab(vOrderItemRawMaterialSufficiencyReportDetailDTOTableTabLayout, 
						this.messages.get(this.VIEW_NAME + "tab.table.order.item.raw.material.sufficiency.report.detail"),
						FontAwesome.BARS, 
						1);
	}
	private void refreshTabSelection(){
		logger.info("\n##############################"
					+"\n refreshing tab selection"
					+"\n##############################");
		int compomentCount = this.tabs.getComponentCount();
		for(int counter = (compomentCount - 1); counter >= 0 ; counter--){
			this.tabs.setSelectedTab(counter);
			logger.info("\n tab in position: "+ counter + " has been selected...");
		}

    	this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
		this.tabs.markAsDirty();
	}
	
    private SelectedTabChangeListener setUpTabsSelectionListener(){
    	return new SelectedTabChangeListener(){
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				// TODO Auto-generated method stub
				try{
					selectedTabContentComponentId =  event.getTabSheet().getSelectedTab().getId();
					logger.info("\ntab selection listener"
							+"\n selectedTabContentComponentId : " + selectedTabContentComponentId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("\nerror", e);
				}
			}    		
    	};
    }
}
