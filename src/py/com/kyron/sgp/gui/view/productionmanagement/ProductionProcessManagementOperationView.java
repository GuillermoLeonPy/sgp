package py.com.kyron.sgp.gui.view.productionmanagement;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.ProductDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.ProductionProcessManagementOperationViewEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.ProductionProcessRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.productionmanagement.productionprocessmanagementoperationview.components.ProductDTOTabLayout;
import py.com.kyron.sgp.gui.view.productionmanagement.productionprocessmanagementoperationview.components.ProductionProcessDTOTableTabLayout;

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
public class ProductionProcessManagementOperationView extends VerticalLayout implements
		View {
	
	private final Logger logger = LoggerFactory.getLogger(ProductionProcessManagementOperationView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.management.operation.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private ProductDTO productDTO;
	private BussinesSessionUtils bussinesSessionUtils;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private String CALLER_VIEW;
	private TabSheet tabs;
	private Tab productDTOTabLayoutTab;
	private Tab productionProcessDTOTableTabLayoutTab;
	private VerticalLayout mainViewLayout;
	private String selectedTabContentComponentId;

	public ProductionProcessManagementOperationView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n ProductionProcessManagementOperationView..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();			
	        this.addStyleName("v-scrollable");
	        this.setHeight("100%");
	        Responsive.makeResponsive(this);
			DashboardEventBus.register(this);
			
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	private void initServices() throws Exception{
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	public ProductionProcessManagementOperationView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		logger.info("\n==========================================="
					+"\n ProductionProcessManagementOperationView"
					+"\n a view change event has been registered"
					+"\n===========================================");
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n ProductionProcessManagementOperationView" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	@Subscribe
	public void editProductProductionProcess(final ProductionProcessManagementOperationViewEvent vProductionProcessManagementOperationViewEvent){
		try{
			this.CALLER_VIEW = vProductionProcessManagementOperationViewEvent.getCallerView();
			this.productDTO = vProductionProcessManagementOperationViewEvent.getProductDTO();
			this.initMainViewLayout();
			this.initTitles();
			this.initTabSheet();
			this.setUpProductDTOTabLayoutTab();
		    this.setUpProductionProcessDTOTableTabLayoutTab();
		    this.mainViewLayout.addComponent(this.tabs);
		    this.removeAndReAddMainViewLayout();
		    this.refreshTabSelection();
		}catch(Exception e){
			logger.error("\nerror", e);
		}		
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
    
    
	private void setUpProductDTOTabLayoutTab(){
		ProductDTOTabLayout vProductDTOTabLayout = new ProductDTOTabLayout(this,this.productDTO);
		vProductDTOTabLayout.setId("vProductDTOTabLayout");
    	this.productDTOTabLayoutTab = this.tabs.addTab(vProductDTOTabLayout, this.messages.get(this.VIEW_NAME + "tab.product.data"));        
    	this.productDTOTabLayoutTab.setClosable(false);
    	this.productDTOTabLayoutTab.setEnabled(true);
    	this.productDTOTabLayoutTab.setIcon(FontAwesome.BOOK);
	}
	
	public void navigateToCallerView(){
		logger.info("\n ProductionProcessManagementOperationView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new ProductionProcessManagementOperationViewEvent(this.productDTO, this.CALLER_VIEW));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}

	public void navigateToCallerView(String viewName){
		logger.info("\n ProductionProcessManagementOperationView.navigateToCallerView()\nviewName : " + viewName
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(viewName);
			DashboardEventBus.post(new ProductionProcessManagementOperationViewEvent(this.productDTO, viewName));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	public void editProductionProcessDTO(ProductionProcessDTO vProductionProcessDTO){
    	logger.info("\n**********************************************"
				+"\n navigate to register production process page"
				+"\n**********************************************");
		try{
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PRODUCTION_PROCESS_REGISTER_FORM.getViewName());
			DashboardEventBus.post(new ProductionProcessRegisterFormViewEvent
					(this.productDTO, 
					vProductionProcessDTO == null ? new ProductionProcessDTO(): vProductionProcessDTO,
					DashboardViewType.PRODUCTION_PROCESS_MANAGEMENT_OPERATION.getViewName(),
					vProductionProcessDTO == null || vProductionProcessDTO.getId() == null ? false : true));
		}catch(Exception e){
			logger.error("\nerror", e);
		}    	
	}
	
	private void setUpProductionProcessDTOTableTabLayoutTab(){
		if(this.productionProcessDTOTableTabLayoutTab!=null){
			this.tabs.removeTab(this.productionProcessDTOTableTabLayoutTab);
			//this.rawMaterialExistenceDTOTableTabLayoutReFreshed = true;
		}
		ProductionProcessDTOTableTabLayout vProductionProcessDTOTableTabLayout = new ProductionProcessDTOTableTabLayout(this, this.productDTO);
		vProductionProcessDTOTableTabLayout.setId("vProductionProcessDTOTableTabLayout");
		this.productionProcessDTOTableTabLayoutTab = 
				this.tabs.addTab(vProductionProcessDTOTableTabLayout,this.messages.get(this.VIEW_NAME + "tab.production.process"), 
						FontAwesome.STAR_HALF_EMPTY, 1);
		
		//if(this.rawMaterialExistenceDTOTableTabLayoutReFreshed)this.tabs.setSelectedTab(1);
	}

}
