package py.com.kyron.sgp.gui.view.comercialmanagement;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.service.CashMovementsManagementService;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CreditOrderChargeConditionDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.OrderRegisterFormViewEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.SaleInvoiceRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.comercialmanagement.orderregisterformview.components.CreditOrderChargeConditionDTOTabLayout;
import py.com.kyron.sgp.gui.view.comercialmanagement.orderregisterformview.components.CreditOrderChargeConditionDTOTableTabLayout;
import py.com.kyron.sgp.gui.view.comercialmanagement.orderregisterformview.components.OrderDTOTabLayout;

import com.google.common.eventbus.Subscribe;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;

@SuppressWarnings("serial")
public class OrderRegisterFormView extends VerticalLayout implements View {

	private final Logger logger = LoggerFactory.getLogger(OrderRegisterFormView.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME = "order.management.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private boolean editFormMode;
	private OrderDTO orderDTO;
	private TabSheet tabs;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private VerticalLayout mainViewLayout;
	private String CALLER_VIEW;
	private Tab orderDTOTabLayoutTab;
	private Tab creditOrderChargeConditionDTOTableTabLayoutTab;
	private Tab creditOrderChargeConditionDTOTabLayoutTab;
	private String selectedTabContentComponentId;
	private boolean massiveInsertMode;
	private ComercialManagementService comercialManagementService;
	private boolean creditOrderChargeConditionDTOTabLayoutTabFlag;
	private boolean creditOrderChargeConditionDTOTableTabLayoutIsFresh;
	private CashMovementsManagementService cashMovementsManagementService;
	private boolean orderInRevisionStatusCheck;
	
	public OrderRegisterFormView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n OrderRegisterFormView..");
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
		this.comercialManagementService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
		this.cashMovementsManagementService = (CashMovementsManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.CASH_MOVEMENTS_MANAGEMENT_SERVICE);
	}
	/*public OrderRegisterFormView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/

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
	public void setOrderDTOToEdit(final OrderRegisterFormViewEvent orderRegisterFormViewEvent){
		try{
			this.CALLER_VIEW = orderRegisterFormViewEvent.getCallerView();
			this.editFormMode = orderRegisterFormViewEvent.isEditFormMode();
			this.massiveInsertMode = orderRegisterFormViewEvent.isMassiveInsertMode();
			this.orderDTO = orderRegisterFormViewEvent.getOrderDTO();
			this.initMainViewLayout();
			this.initTitles();
			this.initTabSheet();
			this.setUpOrderDTOTabLayout();
			this.setUpCreditOrderChargeConditionDTOTableTabLayoutTab();
			this.setUpCreditOrderChargeConditionDTOTabLayoutTab(null);
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
					if(selectedTabContentComponentId.equals("vCreditOrderChargeConditionDTOTableTabLayout") 
					&& !creditOrderChargeConditionDTOTableTabLayoutIsFresh){					
						setUpCreditOrderChargeConditionDTOTableTabLayoutTab();					
						tabs.setSelectedTab(creditOrderChargeConditionDTOTableTabLayoutTab);
						tabs.markAsDirty();
						creditOrderChargeConditionDTOTableTabLayoutIsFresh = false;
						updateEnableDisableStatusCreditOrderChargeConditionDTOTabLayoutTab();
					}else if(!selectedTabContentComponentId.equals("vCreditOrderChargeConditionDTOTableTabLayout"))
						creditOrderChargeConditionDTOTableTabLayoutIsFresh = false;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("\nerror", e);
				}
			}    		
    	};
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
			Label title = new Label(
					!this.editFormMode ? 
							this.messages.get(this.VIEW_NAME + "main.title.register") : 
							this.messages.get(this.VIEW_NAME + "main.title.edit"));
        	title.addStyleName("h1");
        	this.mainViewLayout.addComponent(title);
		}catch(Exception e){
			commonExceptionErrorNotification.showErrorMessageNotification(e);
		}
	}
	
	private void setUpOrderDTOTabLayout(){
		OrderDTOTabLayout vOrderDTOTabLayout = new OrderDTOTabLayout(this,this.orderDTO);
		vOrderDTOTabLayout.setId("vOrderDTOTabLayout");
		this.orderDTOTabLayoutTab = this.tabs.addTab(vOrderDTOTabLayout, 
													this.messages.get(this.VIEW_NAME + "tab.order.form"),
													FontAwesome.BOOK, 
													0);
		
	}
	
	public void saveButtonActionOrderDTOTabLayout(OrderDTO vOrderDTO) throws PmsServiceException{
		
    	if(!this.editFormMode)
    		this.orderDTO = this.comercialManagementService.insertOrderDTO(vOrderDTO);
    	else{
    		logger.info("\nhere call update method, then return to caller view, because its a fast edit");
    		this.orderInRevisionStatusCheck = vOrderDTO.getStatus().equals("application.common.status.revision") ? true : false;
    		this.orderDTO = this.comercialManagementService.updateOrderDTO(vOrderDTO);
    		if(this.orderInRevisionStatusCheck){
    			final SaleInvoiceDTO vSaleInvoiceDTO = this.cashMovementsManagementService.listSaleInvoiceDTO(
    					new SaleInvoiceDTO(
    							null/*id*/, 
    							null/*id_person*/, 
    							vOrderDTO.getId(), 
    							null/*id_branch_office_sale_station*/)).get(0);
    			this.goToSaleInvoiceRegisterFormView(vSaleInvoiceDTO);
    			commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
    			return;
    		}
    	}    	
		commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();		
		if(!this.editFormMode && this.massiveInsertMode) 
			this.restartFormAfterSuccesfulInsertOperation();
		else
			this.navigateToCallerView();
	}
	
	public void cancelButtonActionOrderDTOTabLayout(){
		this.navigateToCallerView();
	}
	
	private void initTabSheet(){
    	this.tabs = new TabSheet();
    	this.tabs.setId("tabs");
    	this.tabs.addStyleName("framed");
	}
	
	public void editCreditOrderChargeConditionDTO(CreditOrderChargeConditionDTO vCreditOrderChargeConditionDTO) throws PmsServiceException{
		this.setUpCreditOrderChargeConditionDTOTabLayoutTab(vCreditOrderChargeConditionDTO);
		this.tabs.setSelectedTab(this.creditOrderChargeConditionDTOTabLayoutTab);
	}
	
	public void saveButtonActionCreditOrderChargeConditionDTOTabLayout(
			final CreditOrderChargeConditionDTO vCreditOrderChargeConditionDTO, final boolean editFormMode) throws PmsServiceException{
		if(!editFormMode)this.comercialManagementService.insertCreditOrderChargeConditionDTO(vCreditOrderChargeConditionDTO);
		else this.comercialManagementService.updateCreditOrderChargeConditionDTO(vCreditOrderChargeConditionDTO);
		
		this.selectCreditOrderChargeConditionDTOTableTabLayoutTab();
		this.setUpCreditOrderChargeConditionDTOTabLayoutTab(null);
		this.updateEnableDisableStatusCreditOrderChargeConditionDTOTabLayoutTab();
	}
	
	public void cancelButtonActionCreditOrderChargeConditionDTOTabLayout() throws PmsServiceException{
		this.selectCreditOrderChargeConditionDTOTableTabLayoutTab();
		this.setUpCreditOrderChargeConditionDTOTabLayoutTab(null);
		this.updateEnableDisableStatusCreditOrderChargeConditionDTOTabLayoutTab();
	}
	
	private void selectCreditOrderChargeConditionDTOTableTabLayoutTab(){
		this.creditOrderChargeConditionDTOTableTabLayoutIsFresh = false;
		this.tabs.setSelectedTab(this.creditOrderChargeConditionDTOTableTabLayoutTab);		
	}
	
	private void setUpCreditOrderChargeConditionDTOTableTabLayoutTab(){
		if(this.creditOrderChargeConditionDTOTableTabLayoutTab!=null){
			this.tabs.removeTab(this.creditOrderChargeConditionDTOTableTabLayoutTab);
		}
		CreditOrderChargeConditionDTOTableTabLayout vCreditOrderChargeConditionDTOTableTabLayout =
				new CreditOrderChargeConditionDTOTableTabLayout(this);
		vCreditOrderChargeConditionDTOTableTabLayout.setId("vCreditOrderChargeConditionDTOTableTabLayout");
		this.creditOrderChargeConditionDTOTableTabLayoutTab = 
				this.tabs.addTab(vCreditOrderChargeConditionDTOTableTabLayout,
						this.messages.get(this.VIEW_NAME + "tab.credit.order.charge.condition"),
						FontAwesome.CREDIT_CARD,
						1);
		this.creditOrderChargeConditionDTOTableTabLayoutTab.setClosable(false);
		this.creditOrderChargeConditionDTOTableTabLayoutTab.setEnabled(true);
		this.creditOrderChargeConditionDTOTableTabLayoutIsFresh = true;
		
	}
	
	private void setUpCreditOrderChargeConditionDTOTabLayoutTab(CreditOrderChargeConditionDTO vCreditOrderChargeConditionDTO) throws PmsServiceException{
		if(vCreditOrderChargeConditionDTO == null)vCreditOrderChargeConditionDTO = new CreditOrderChargeConditionDTO();
		CreditOrderChargeConditionDTOTabLayout vCreditOrderChargeConditionDTOTabLayout = 
				new CreditOrderChargeConditionDTOTabLayout(this,vCreditOrderChargeConditionDTO,vCreditOrderChargeConditionDTO.getId()!=null);
		vCreditOrderChargeConditionDTOTabLayout.setId("vCreditOrderChargeConditionDTOTabLayout");
		if(this.creditOrderChargeConditionDTOTabLayoutTab!=null)this.tabs.removeTab(this.creditOrderChargeConditionDTOTabLayoutTab);
		
		this.creditOrderChargeConditionDTOTabLayoutTab = 
    			this.tabs.addTab(vCreditOrderChargeConditionDTOTabLayout, 
    					this.messages.get(this.VIEW_NAME + "tab.credit.order.charge.condition.form"),
    					FontAwesome.CREDIT_CARD_ALT,
    					2);
    	
    	this.creditOrderChargeConditionDTOTabLayoutTab.setClosable(false);
    	if(vCreditOrderChargeConditionDTO.getId()==null)this.setEnableCreditOrderChargeConditionDTOTabLayoutTabFlag();
    	this.creditOrderChargeConditionDTOTabLayoutTab.setEnabled(vCreditOrderChargeConditionDTO.getId()!=null || this.creditOrderChargeConditionDTOTabLayoutTabFlag);    	
	}
	
	 private void setEnableCreditOrderChargeConditionDTOTabLayoutTabFlag() throws PmsServiceException{
		 Long id = this.comercialManagementService.getCreditOrderChargeConditionValidRowId();
		 if(id != null)this.creditOrderChargeConditionDTOTabLayoutTabFlag = false; else this.creditOrderChargeConditionDTOTabLayoutTabFlag = true;
	 }
	 
	 private void updateEnableDisableStatusCreditOrderChargeConditionDTOTabLayoutTab() throws PmsServiceException{
		 if(this.creditOrderChargeConditionDTOTabLayoutTab != null){
			this.setEnableCreditOrderChargeConditionDTOTabLayoutTabFlag();
		 	this.creditOrderChargeConditionDTOTabLayoutTab.setEnabled(this.creditOrderChargeConditionDTOTabLayoutTabFlag);
		 	this.tabs.markAsDirty();
		 }
	 }
	 
	private void navigateToCallerView(){
		logger.info("\n OrderRegisterFormView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new OrderRegisterFormViewEvent(
					this.orderDTO, 
					DashboardViewType.REGISTER_ORDER_FORM.getViewName(),
					this.orderDTO!=null && this.orderDTO.getId()!=null,
					true));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	private void restartFormAfterSuccesfulInsertOperation() throws PmsServiceException{
		this.orderDTO = new OrderDTO();
		this.editFormMode = false;
		this.initMainViewLayout();
		this.initTitles();
		this.initTabSheet();
		this.setUpOrderDTOTabLayout();
		this.setUpCreditOrderChargeConditionDTOTableTabLayoutTab();
		this.setUpCreditOrderChargeConditionDTOTabLayoutTab(null);
	    this.mainViewLayout.addComponent(this.tabs);
	    this.removeAndReAddMainViewLayout();
	    this.refreshTabSelection();
	}
	
    private void goToSaleInvoiceRegisterFormView(final SaleInvoiceDTO vSaleInvoiceDTO){
		try{						
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.SALE_INVOICE_REGISTER_FORM.getViewName());
			DashboardEventBus.post(new SaleInvoiceRegisterFormViewEvent(
					null, 
					vSaleInvoiceDTO,
					DashboardViewType.REGISTER_ORDER_FORM.getViewName(),
					vSaleInvoiceDTO!=null && vSaleInvoiceDTO.getId()!=null && vSaleInvoiceDTO.getStatus().equals("application.common.status.revision")));
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
    }
}