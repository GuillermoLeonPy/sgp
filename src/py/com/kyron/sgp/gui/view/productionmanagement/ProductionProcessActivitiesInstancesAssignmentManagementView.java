package py.com.kyron.sgp.gui.view.productionmanagement;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderItemDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityInstanceDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.OrderRawMaterialSufficiencyReportViewEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.OrderRegisterFormViewEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.ProductionProcessActivityInstanceOperationsViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.activities.instances.assignment.management.view.OrderDTOTableLayout;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.activities.instances.assignment.management.view.OrderDTOTableLayoutFunctions;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.activities.instances.assignment.management.view.ProductionProcessActivityInstanceDTOTableFunctions;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.activities.instances.assignment.management.view.ProductionProcessActivityInstanceDTOTableLayout;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.ConfirmWindow;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.Decision;
import py.com.kyron.sgp.gui.view.utils.commponents.personmanagement.PersonFinderWindow;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

@SuppressWarnings("serial")
public class ProductionProcessActivitiesInstancesAssignmentManagementView
		extends VerticalLayout implements View,OrderDTOTableLayoutFunctions,ProductionProcessActivityInstanceDTOTableFunctions{

	private final Logger logger = LoggerFactory.getLogger(ProductionProcessActivitiesInstancesAssignmentManagementView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.activities.instances.assignment.management.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private BussinesSessionUtils bussinesSessionUtils;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private TabSheet tabs;
	private Tab orderDTOTableTabLayoutTab;
	private Tab productionProcessActivityInstanceDTOTableTabLayoutTab;
	private VerticalLayout mainViewLayout;
	private String selectedTabContentComponentId;
	private ProductionManagementService productionManagementService;
	private PersonFinderWindow personFinderWindow;
	private OrderDTO queryOrderDTO;
	private ComercialManagementService comercialManagementService;
	private String queryOrderDTODetails;
	private int orderItemPosition;
	private OrderItemDTO queryOrderItemDTO;
	
	public ProductionProcessActivitiesInstancesAssignmentManagementView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n ProductionProcessActivitiesInstancesAssignmentManagementView()...");
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

	public ProductionProcessActivitiesInstancesAssignmentManagementView(
			Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	private void initServices() throws Exception{
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
		this.comercialManagementService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
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
	
	private void setUpLayout(){
		this.initMainViewLayout();
		this.initTitles();
		this.initTabSheet();
		this.setUpOrderDTOTableTabLayoutTab();
	    this.setUpProductionProcessDTOTableTabLayoutTab();
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
	
	private void setUpOrderDTOTableTabLayoutTab(){
		OrderDTOTableLayout vOrderDTOTableLayout = new OrderDTOTableLayout(this);
		vOrderDTOTableLayout.setId("vOrderDTOTableLayout");
		if(this.orderDTOTableTabLayoutTab!=null)this.tabs.removeTab(this.orderDTOTableTabLayoutTab);
		this.orderDTOTableTabLayoutTab =     			
				this.tabs.addTab(vOrderDTOTableLayout, 
				this.messages.get(this.VIEW_NAME + "tab.order.table"),
				FontAwesome.MORTAR_BOARD,
				0);		
	}

	@Override
	public void goToRegisterOrderView(OrderDTO vOrderDTO) {
		// TODO Auto-generated method stub
		try{
			if(vOrderDTO == null)vOrderDTO = new OrderDTO();
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.REGISTER_ORDER_FORM.getViewName());
			DashboardEventBus.post(new OrderRegisterFormViewEvent(
					vOrderDTO, 
					DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_INSTANCES_MANAGEMENT.getViewName(),
					vOrderDTO!=null && vOrderDTO.getId()!=null,
					false));
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	@Override
	public void instantiateProductionProcessActivities(OrderDTO vOrderDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		this.productionManagementService.instantiateProductionProcessActivityInstanceDTO(new ProductionProcessActivityInstanceDTO(null, vOrderDTO.getId()));
		this.setUpLayout();
		this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
	}
	
	private void setUpProductionProcessDTOTableTabLayoutTab(){
		ProductionProcessActivityInstanceDTOTableLayout vProductionProcessActivityInstanceDTOTableLayout = new ProductionProcessActivityInstanceDTOTableLayout(this);
		vProductionProcessActivityInstanceDTOTableLayout.setId("vProductionProcessActivityInstanceDTOTableLayout");
		if(this.productionProcessActivityInstanceDTOTableTabLayoutTab!=null)this.tabs.removeTab(this.productionProcessActivityInstanceDTOTableTabLayoutTab);
		this.productionProcessActivityInstanceDTOTableTabLayoutTab =      			
				this.tabs.addTab(vProductionProcessActivityInstanceDTOTableLayout, 
				this.messages.get(this.VIEW_NAME + "tab.activities.instances.table"),
				FontAwesome.WECHAT,
				1);
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

	@Override
	public void assignProductionProcessActivityInstanceDTO(
			ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO) {
		// TODO Auto-generated method stub
    	personFinderWindow = new PersonFinderWindow(false,false,true);
    	personFinderWindow.addCloseListener(this.setUpPersonFinderWindowCloseListener(productionProcessActivityInstanceDTO));
    	personFinderWindow.adjuntWindowSizeAccordingToCientDisplay();
	}

	@Override
	public void doParcialProductRecall(
			ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO) {
		// TODO Auto-generated method stub
		try{			
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_INSTANCES_OPERATIONS_VIEW.getViewName());
			DashboardEventBus.post(new ProductionProcessActivityInstanceOperationsViewEvent(
					DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_INSTANCES_MANAGEMENT.getViewName(),
					productionProcessActivityInstanceDTO,
					true,
					false,
					false,
					false,
					false));
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}				
	}

	@Override
	public void finalizeProductionProcessActivityInstanceDTO(
			ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO) {
		// TODO Auto-generated method stub
		try{			
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_INSTANCES_OPERATIONS_VIEW.getViewName());
			DashboardEventBus.post(new ProductionProcessActivityInstanceOperationsViewEvent(
					DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_INSTANCES_MANAGEMENT.getViewName(),
					productionProcessActivityInstanceDTO,
					false,
					false,
					true,
					false,
					false));
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}		
	}

	@Override
	public void deliverPartialResult(
			ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO) {
		// TODO Auto-generated method stub
		try{			
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_INSTANCES_OPERATIONS_VIEW.getViewName());
			DashboardEventBus.post(new ProductionProcessActivityInstanceOperationsViewEvent(
					DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_INSTANCES_MANAGEMENT.getViewName(),
					productionProcessActivityInstanceDTO,
					false,
					false,
					false,
					true,
					false));
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}				
	}

	@Override
	public void deliverFinalProduct(
			ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO) {
		// TODO Auto-generated method stub
		try{			
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_INSTANCES_OPERATIONS_VIEW.getViewName());
			DashboardEventBus.post(new ProductionProcessActivityInstanceOperationsViewEvent(
					DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_INSTANCES_MANAGEMENT.getViewName(),
					productionProcessActivityInstanceDTO,
					false,
					false,
					false,
					false,
					true));
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	@Override
	public void effectuateRawMaterialSupply(
			ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO) {
		// TODO Auto-generated method stub
		try{			
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_INSTANCES_OPERATIONS_VIEW.getViewName());
			DashboardEventBus.post(new ProductionProcessActivityInstanceOperationsViewEvent(
					DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_INSTANCES_MANAGEMENT.getViewName(),
					productionProcessActivityInstanceDTO,
					false,
					true,
					false,
					false,
					false));
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}		
	}
	
	private CloseListener setUpPersonFinderWindowCloseListener(final ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO){
		return new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{
					PersonDTO vPersonDTO = personFinderWindow.getPersonDTOTableRowSelected();
					logger.info("\n******************************"
								+"\nthe person finder window has been closed"
								+"\nperson found: \n" + vPersonDTO
								+"\n******************************");
					if(vPersonDTO!=null){
						productionProcessActivityInstanceDTO.setId_person(vPersonDTO.getId());
						doAssignProductionProcessActivityInstanceDTO(productionProcessActivityInstanceDTO);
					}
				}catch(Exception ex){
					commonExceptionErrorNotification.showErrorMessageNotification(ex);
				}
			}			
		};
	}
	
	private void doAssignProductionProcessActivityInstanceDTO(ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO) throws PmsServiceException{
		this.productionManagementService.assignProductionProcessActivityInstanceDTO(productionProcessActivityInstanceDTO);
		this.setUpLayout();
		this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
	}

	@Override
	public void queryOrderRawMaterialSufficiencyReportDTO(OrderDTO vOrderDTO) {
		// TODO Auto-generated method stub
		try{			
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.ORDER_RAW_MATERIAL_SUFFICIENCY_REPORT_VIEW.getViewName());
			DashboardEventBus.post(new OrderRawMaterialSufficiencyReportViewEvent(
					vOrderDTO,
					DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_INSTANCES_MANAGEMENT.getViewName()));
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	@Override
	public void queryOrderNumber(
			ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		this.queryOrderDTO = this.comercialManagementService.listOrderDTO(new OrderDTO(productionProcessActivityInstanceDTO.getId_order())).get(0);
		this.orderItemPosition = this.queryOrderDTO.getListOrderItemDTO().indexOf(new OrderItemDTO(productionProcessActivityInstanceDTO.getId_order_item()));
		this.queryOrderItemDTO = this.queryOrderDTO.getListOrderItemDTO().get(this.orderItemPosition);
		this.queryOrderDTODetails = 
				this.messages.get("application.common.order.number.indicator.label") 
				+ " " + this.queryOrderDTO.getIdentifier_number() + " ; "
				+ "\n" + this.messages.get("application.common.table.column.article.label")
				+ " : " + this.queryOrderItemDTO.getProductDTO().getProduct_id() + " ; "
				+"\n" + this.messages.get("application.common.quantity.label") 
				+ " : " + this.queryOrderItemDTO.getQuantity();
		
		
    	final ConfirmWindow window = new ConfirmWindow(
    			this.messages.get("application.common.query.label") + " " + this.messages.get("application.common.order.number.indicator.label").toLowerCase(),
    			this.queryOrderDTODetails,  
    			this.messages.get("application.common.confirmation.view.buttonlabel.yes"),
    			this.messages.get("application.common.confirmation.view.shortcut.close"),
    			false);
    	window.setDecision(new Decision() {
				@Override
				public void yes(ClickEvent event) {
					try{		
						window.close();
					}catch(Exception e){
						commonExceptionErrorNotification.showErrorMessageNotification(e);
						window.resetYesNoAccionFlags();
					}					
				}
				@Override
				public void no(ClickEvent event) {
		    				// do nothing?
					try{					
						window.close();
					}catch(Exception e){
						commonExceptionErrorNotification.showErrorMessageNotification(e);
						window.resetYesNoAccionFlags();
					}
				}
    	});
	}
}
