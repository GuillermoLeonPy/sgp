package py.com.kyron.sgp.gui.view.productionmanagement;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityInstanceDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.bussines.stockmanagement.domain.PAIRawMaterialSupplyDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO;
import py.com.kyron.sgp.bussines.stockmanagement.service.StockManagementService;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.ProductionProcessActivityInstanceOperationsViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.activity.instance.operations.view.components.OperationDeliversFinalProductTabLayout;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.activity.instance.operations.view.components.OperationDeliversPartialResultTabLayout;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.activity.instance.operations.view.components.OperationFinalizeActivityTabLayout;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.activity.instance.operations.view.components.OperationPartialProductRecallTabLayout;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.activity.instance.operations.view.components.OperationRawMaterialSupplyTabLayout;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.activity.instance.operations.view.components.ProductionProcessActivityInstanceOperationsViewFunctions;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.ConfirmWindow;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.Decision;

import com.google.common.eventbus.Subscribe;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
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
public class ProductionProcessActivityInstanceOperationsView extends
		VerticalLayout implements View,ProductionProcessActivityInstanceOperationsViewFunctions {

	private final Logger logger = LoggerFactory.getLogger(ProductionProcessActivityInstanceOperationsView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.activity.instance.operations.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private BussinesSessionUtils bussinesSessionUtils;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private TabSheet tabs;
	private Tab operationRawMaterialSupplyTabLayoutTab;
	private Tab operationFinalizeActivityTabLayoutTab;
	private Tab operationDeliversPartialResultTabLayoutTab;
	private Tab operationPartialProductRecallTabLayoutTab;
	private Tab operationDeliversFinalProductTabLayoutTab;
	private VerticalLayout mainViewLayout;
	private String selectedTabContentComponentId;
	private String CALLER_VIEW;
	/**/
	private boolean operationPartialProductRecall;
	private boolean operationRawMaterialSupply;
	private boolean operationFinalizeActivity;
	private boolean operationDeliversPartialResult;
	private boolean operationDeliversFinalProduct;
	
	private ProductionManagementService productionManagementService;
	private StockManagementService stockManagementService;
	/**/
	private ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO;
	private String elementsPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO;
	
	
	public ProductionProcessActivityInstanceOperationsView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n ProductionProcessActivityInstanceOperationsView()...");
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
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
		this.stockManagementService = (StockManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.STOCK_MANAGEMENT_SERVICE);
	}

	public ProductionProcessActivityInstanceOperationsView(
			Component... children) {
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
	public void handleCallingEvent(final ProductionProcessActivityInstanceOperationsViewEvent vProductionProcessActivityInstanceOperationsViewEvent){
		try{
			this.CALLER_VIEW = vProductionProcessActivityInstanceOperationsViewEvent.getCallerView();
			this.productionProcessActivityInstanceDTO = vProductionProcessActivityInstanceOperationsViewEvent.getProductionProcessActivityInstanceDTO();
			this.operationPartialProductRecall = vProductionProcessActivityInstanceOperationsViewEvent.isOperationPartialProductRecall();
			this.operationRawMaterialSupply = vProductionProcessActivityInstanceOperationsViewEvent.isOperationRawMaterialSupply();
			this.operationFinalizeActivity = vProductionProcessActivityInstanceOperationsViewEvent.isOperationFinalizeActivity();
			this.operationDeliversPartialResult = vProductionProcessActivityInstanceOperationsViewEvent.isOperationDeliversPartialResult();
			this.operationDeliversFinalProduct = vProductionProcessActivityInstanceOperationsViewEvent.isOperationDeliversFinalProduct();

			this.initMainViewLayout();
			this.initTitles();
			this.initTabSheet();
			this.setUpOperationRawMaterialSupplyTabLayout();
			this.setUpOperationFinalizeActivityTabLayout();
			this.setUpOperationDeliversPartialResultTabLayout();
			this.setUpOperationPartialProductRecallTabLayout();
			this.setUpOperationDeliversFinalProductTabLayout();
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
    	//this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
    	this.tabs.addStyleName("framed");	
	}
	
	private void setUpOperationRawMaterialSupplyTabLayout(){
		OperationRawMaterialSupplyTabLayout vOperationRawMaterialSupplyTabLayout = 
				new OperationRawMaterialSupplyTabLayout(this,this.VIEW_NAME,this.productionProcessActivityInstanceDTO,this.operationRawMaterialSupply);
		vOperationRawMaterialSupplyTabLayout.setId("vOperationRawMaterialSupplyTabLayout");
		if(this.operationRawMaterialSupplyTabLayoutTab!=null)this.tabs.removeTab(this.operationRawMaterialSupplyTabLayoutTab);
		this.operationRawMaterialSupplyTabLayoutTab = this.tabs.addTab(vOperationRawMaterialSupplyTabLayout, 
				this.messages.get(this.VIEW_NAME + "tab.operation.raw.material.supply"),
				FontAwesome.DIAMOND,
				0);
		this.operationRawMaterialSupplyTabLayoutTab.setVisible(this.operationRawMaterialSupply);
	}
	
	private void setUpOperationFinalizeActivityTabLayout(){
		OperationFinalizeActivityTabLayout vOperationFinalizeActivityTabLayout = new OperationFinalizeActivityTabLayout(this,this.VIEW_NAME,this.productionProcessActivityInstanceDTO,this.operationFinalizeActivity);
		vOperationFinalizeActivityTabLayout.setId("vOperationFinalizeActivityTabLayout");
		if(this.operationFinalizeActivityTabLayoutTab!=null)this.tabs.removeTab(this.operationFinalizeActivityTabLayoutTab);
		this.operationFinalizeActivityTabLayoutTab = this.tabs.addTab
				(vOperationFinalizeActivityTabLayout,
						this.messages.get(this.VIEW_NAME + "tab.operation.finalize.activity"),
						FontAwesome.FLAG_CHECKERED,
						1);
		this.operationFinalizeActivityTabLayoutTab.setVisible(this.operationFinalizeActivity);
	}

	private void setUpOperationDeliversPartialResultTabLayout(){
		OperationDeliversPartialResultTabLayout vOperationDeliversPartialResultTabLayout = 
				new OperationDeliversPartialResultTabLayout(this,this.VIEW_NAME,this.productionProcessActivityInstanceDTO,this.operationDeliversPartialResult);
		vOperationDeliversPartialResultTabLayout.setId("vOperationDeliversPartialResultTabLayout");
		if(this.operationDeliversPartialResultTabLayoutTab!=null)this.tabs.removeTab(this.operationDeliversPartialResultTabLayoutTab);
		this.operationDeliversPartialResultTabLayoutTab = this.tabs.addTab
				(vOperationDeliversPartialResultTabLayout,
						this.messages.get(this.VIEW_NAME + "tab.operation.allocate.half.way.product"), 
						FontAwesome.CHECK_SQUARE_O, 
						2);
		this.operationDeliversPartialResultTabLayoutTab.setVisible(this.operationDeliversPartialResult);
	}
	
	private void setUpOperationPartialProductRecallTabLayout(){
		OperationPartialProductRecallTabLayout vOperationPartialProductRecallTabLayout = new OperationPartialProductRecallTabLayout(this,this.VIEW_NAME,this.productionProcessActivityInstanceDTO,this.operationPartialProductRecall);
		vOperationPartialProductRecallTabLayout.setId("vOperationPartialProductRecallTabLayout");
		if(this.operationPartialProductRecallTabLayoutTab!=null)this.tabs.removeTab(this.operationPartialProductRecallTabLayoutTab);
		this.operationPartialProductRecallTabLayoutTab = this.tabs.addTab(
				vOperationPartialProductRecallTabLayout, 
				this.messages.get(this.VIEW_NAME + "tab.operation.partial.product.recall"),
				FontAwesome.BITBUCKET, 
				3);
		this.operationPartialProductRecallTabLayoutTab.setVisible(this.operationPartialProductRecall);
	}
	
	private void setUpOperationDeliversFinalProductTabLayout(){
		OperationDeliversFinalProductTabLayout vOperationDeliversFinalProductTabLayout = new OperationDeliversFinalProductTabLayout(this,this.VIEW_NAME,this.productionProcessActivityInstanceDTO,this.operationDeliversFinalProduct);
		vOperationDeliversFinalProductTabLayout.setId("vOperationDeliversFinalProductTabLayout");
		if(this.operationDeliversFinalProductTabLayoutTab!=null)this.tabs.removeTab(this.operationDeliversFinalProductTabLayoutTab);
		this.operationDeliversFinalProductTabLayoutTab = this.tabs.addTab(
				vOperationDeliversFinalProductTabLayout, 
				this.messages.get(this.VIEW_NAME + "tab.operation.final.product.delivery"),
				FontAwesome.PRODUCT_HUNT, 
				4);
		this.operationDeliversFinalProductTabLayoutTab.setVisible(this.operationDeliversFinalProduct);
	}
	
	@Override
	public void navigateToCallerView() {
		// TODO Auto-generated method stub
		logger.info("\n ProductionProcessActivityInstanceOperationsView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new ProductionProcessActivityInstanceOperationsViewEvent(
					DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_INSTANCES_OPERATIONS_VIEW.getViewName(),
					this.productionProcessActivityInstanceDTO,
					this.operationPartialProductRecall,
					this.operationRawMaterialSupply,
					this.operationFinalizeActivity,
					this.operationDeliversPartialResult,
					this.operationDeliversFinalProduct));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}

	@Override
	public void effectuateRawMaterialDeparture() throws PmsServiceException {
		// TODO Auto-generated method stub
		this.stockManagementService.effectuatePAIRawMaterialSupplyDTO(new PAIRawMaterialSupplyDTO(null, this.productionProcessActivityInstanceDTO.getId()));
		this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		this.productionProcessActivityInstanceDTO = this.productionManagementService.listProductionProcessActivityInstanceDTO(new ProductionProcessActivityInstanceDTO(this.productionProcessActivityInstanceDTO.getId())).get(0);
		this.reStartLayout();
	}

	@Override
	public void finalizeActivity() throws PmsServiceException {
		// TODO Auto-generated method stub
		this.productionManagementService.finalizeProductionProcessActivityInstanceDTO(this.productionProcessActivityInstanceDTO);
		this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		this.navigateToCallerView();
	}

	@Override
	public void allocateHalfWayProductProductionProcessActivityInstanceDTO()
			throws PmsServiceException {
		// TODO Auto-generated method stub
		ProductionProcessActivityInstanceDTO vProductionProcessActivityInstanceDTO = 
		this.productionManagementService.allocateHalfWayProductProductionProcessActivityInstanceDTO(this.productionProcessActivityInstanceDTO);
		this.showAllocatedHalfWayProductLockerNumberWindow(vProductionProcessActivityInstanceDTO);
	}
	
	public void showAllocatedHalfWayProductLockerNumberWindow(final ProductionProcessActivityInstanceDTO vProductionProcessActivityInstanceDTO){
    	final ConfirmWindow window = new ConfirmWindow(
    			this.messages.get(this.VIEW_NAME + "tab.operation.allocate.half.way.product.confirmation.window.locker.number.window.title"),
				this.messages.get(this.VIEW_NAME + "tab.operation.allocate.half.way.product.confirmation.window.locker.number.window.label.locker.number") 
				+ ": " + vProductionProcessActivityInstanceDTO.getOccupied_locker_number(),  
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
    	/*window.addShortcutListener(new ShortcutListener(this.messages.get("application.common.confirmation.view.shortcut.close"),ShortcutAction.KeyCode.ESCAPE, null) {
			@Override
			public void handleAction(Object sender, Object target) {
					window.close();
				}
			});*/
    	window.addCloseListener(new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{
					logger.info("\nwindow.addCloseListener\nwindow.isYesAccionRealized() : " + window.isYesAccionRealized()
							+"\nwindow.isNoAccionRealized() : " + window.isNoAccionRealized());
					navigateToCallerView();
				}catch(Exception exception){
					commonExceptionErrorNotification.showErrorMessageNotification(exception);
					window.resetYesNoAccionFlags();
				}
			}
    		
    	});
	}

	@Override
	public void effectuatePartialProductRecall() throws PmsServiceException {
		// TODO Auto-generated method stub
		this.productionManagementService.effectuatePartialProductRecallProductionProcessActivityInstanceDTO(productionProcessActivityInstanceDTO);
		this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		this.navigateToCallerView();
	}

	@Override
	public void deliverFinalProduct() throws PmsServiceException {
		// TODO Auto-generated method stub
		this.productionManagementService.deliversFinalProductProductionProcessActivityInstanceDTO(this.productionProcessActivityInstanceDTO);
		this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		this.navigateToCallerView();	
	}

	@Override
	public void queryListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO(
			PAIRawMaterialSupplyDTO vPAIRawMaterialSupplyDTO) {
		// TODO Auto-generated method stub
		this.elementsPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO = "";
		/*int aux = vPAIRawMaterialSupplyDTO.getListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO().size();
		logger.info("\n vPAIRawMaterialSupplyDTO.getListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO().size() : " 
		+ aux);


		IF IN THE MyBatis config.xml, a bean is wrongly mapped to another bean, ej:
		<typeAlias type="py.com.kyron.sgp.bussines.stockmanagement.domain.PAIRawMaterialSupplyDTO" alias="PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO"/> 

		this wrong mapping is maintained in memory so an instance of PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO
		is considered like an instance of PAIRawMaterialSupplyDTO by the java virtual machine

		List<PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO> listPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO = vPAIRawMaterialSupplyDTO.getListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO();*/ 
		for(PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO vPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO : 
			vPAIRawMaterialSupplyDTO.getListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO()){
			this.elementsPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO += 
					this.messages.get("application.common.number.indicator.label")
					+ ": " +
					vPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO.getPurchase_invoice_identifier_number() + "; "
					+ this.messages.get("application.common.quantity.label") 
					+ ": " + vPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO.getQuantity()
					+ "\n";
		}
		/*int counter = 0;
		while(counter < aux){
			PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO vPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO = 
					listPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO.get(counter++);
			this.elementsPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO += 
					vPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO.getPurchase_invoice_identifier_number() + "; "
					+ this.messages.get("application.common.quantity.label") 
					+ ": " + vPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO.getQuantity()
					+ "\n";		
		}*/
		
		
    	final ConfirmWindow window = new ConfirmWindow(
    			this.messages.get(this.VIEW_NAME + "tab.operation.raw.material.supply.table.pai.raw.material.supply.column.operations.button.query.purchase.invoice.affected.description"),
    			this.elementsPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO,  
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
	
	private void reStartLayout(){
		this.initMainViewLayout();
		this.initTitles();
		this.initTabSheet();
		this.setUpOperationRawMaterialSupplyTabLayout();
		this.setUpOperationFinalizeActivityTabLayout();
		this.setUpOperationDeliversPartialResultTabLayout();
		this.setUpOperationPartialProductRecallTabLayout();
		this.setUpOperationDeliversFinalProductTabLayout();
	    this.mainViewLayout.addComponent(this.tabs);
	    this.removeAndReAddMainViewLayout();
	    this.refreshTabSelection();
	}
}
