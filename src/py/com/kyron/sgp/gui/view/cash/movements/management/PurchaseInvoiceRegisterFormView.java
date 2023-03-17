package py.com.kyron.sgp.gui.view.cash.movements.management;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CashReceiptDocumentDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceCreditNoteDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceCreditNoteItemDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceItemDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoicePaymentDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.service.CashMovementsManagementService;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.PurchaseInvoiceRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components.CashReceiptDocumentDTOWindow;
import py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components.PurchaseInvoiceCreditNoteDTOLayout;
import py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components.PurchaseInvoiceCreditNoteDTOTableLayout;
import py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components.PurchaseInvoiceDTOTabLayout;
import py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components.PurchaseInvoicePaymentDTOTableTabLayout;
import py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components.PurchaseInvoiceRegisterFormViewFunctions;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.ConfirmOptionsWindow;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.ConfirmWindowDecision;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.ConfirmOptionsWindow.SelectOneOption;
import py.com.kyron.sgp.gui.view.utils.commponents.editquantitywindow.EditBigDecimalQuantityWindow;

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
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

@SuppressWarnings("serial")
public class PurchaseInvoiceRegisterFormView extends VerticalLayout implements View,PurchaseInvoiceRegisterFormViewFunctions {

	private final Logger logger = LoggerFactory.getLogger(PurchaseInvoiceRegisterFormView.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME = "purchase.invoice.management.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private boolean editFormMode;
	private PurchaseInvoiceDTO purchaseInvoiceDTO;
	private TabSheet tabs;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private VerticalLayout mainViewLayout;
	private String CALLER_VIEW;
	private Tab purchaseInvoiceDTOTabLayoutTab;
	private Tab purchaseInvoicePaymentDTOTableTabLayoutTab;
	private Tab purchaseInvoiceCreditNoteDTOTableLayoutTab;
	private Tab purchaseInvoiceCreditNoteDTOLayoutTab;
	private String selectedTabContentComponentId;
	private SgpUtils sgpUtils;
	private BussinesSessionUtils bussinesSessionUtils;
	private CashMovementsManagementService cashMovementsManagementService;
	private CashReceiptDocumentDTOWindow cashReceiptDocumentDTOWindow;
	private List<PurchaseInvoiceCreditNoteDTO> listPurchaseInvoiceCreditNoteDTOForPaymentProcedure;
	private PurchaseInvoiceCreditNoteDTO selectedPurchaseInvoiceCreditNoteDTOForPaymentProcedure;
	private ConfirmOptionsWindow vConfirmOptionsWindow;
	private SelectOneOption vSelectOneOptionSelectedOption;
	private ComercialManagementService comercialManagementService;
	private EditBigDecimalQuantityWindow editBigDecimalQuantityWindow;
	
	public PurchaseInvoiceRegisterFormView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n PurchaseInvoiceRegisterFormView..");
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

	/*public PurchaseInvoiceRegisterFormView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	
	private void initServices() throws Exception{
		this.cashMovementsManagementService = (CashMovementsManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.CASH_MOVEMENTS_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
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
	
	@Subscribe
	public void handleCallingEvent(final PurchaseInvoiceRegisterFormViewEvent vPurchaseInvoiceRegisterFormViewEvent){
		try{
			this.CALLER_VIEW = vPurchaseInvoiceRegisterFormViewEvent.getCallerView();
			this.editFormMode = vPurchaseInvoiceRegisterFormViewEvent.isEditFormMode();
			this.purchaseInvoiceDTO = vPurchaseInvoiceRegisterFormViewEvent.getPurchaseInvoiceDTO();
			this.initMainViewLayout();
			this.initTitles();
			this.initTabSheet();
			this.setUpPurchaseInvoiceDTOTabLayout();
			this.setUpPurchaseInvoicePaymentDTOTableTabLayout();
			this.setUpPurchaseInvoiceCreditNoteDTOTableLayout();
			this.setUpPurchaseInvoiceCreditNoteDTOLayout();
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
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("\nerror", e);
				}
			}    		
    	};
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
	
	private void initTabSheet(){
    	this.tabs = new TabSheet();
    	this.tabs.setId("tabs");
    	this.tabs.addStyleName("framed");
	}

	private void setUpPurchaseInvoiceDTOTabLayout(){
		PurchaseInvoiceDTOTabLayout vPurchaseInvoiceDTOTabLayout = 
				new PurchaseInvoiceDTOTabLayout(this.VIEW_NAME,this,this.purchaseInvoiceDTO,true);
		vPurchaseInvoiceDTOTabLayout.setId("vPurchaseInvoiceDTOTabLayout");
		this.purchaseInvoiceDTOTabLayoutTab = this.tabs.addTab(
				vPurchaseInvoiceDTOTabLayout, 
				this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form"), 
				FontAwesome.BUYSELLADS, 
				0);
		
		this.purchaseInvoiceDTOTabLayoutTab.setVisible(true);		
	}
	
	private void setUpPurchaseInvoicePaymentDTOTableTabLayout(){
		PurchaseInvoicePaymentDTOTableTabLayout vPurchaseInvoicePaymentDTOTableTabLayout = 
				new PurchaseInvoicePaymentDTOTableTabLayout(this,this.VIEW_NAME,this.purchaseInvoiceDTO);
		vPurchaseInvoicePaymentDTOTableTabLayout.setId("vPurchaseInvoicePaymentDTOTableTabLayout");
		this.purchaseInvoicePaymentDTOTableTabLayoutTab = this.tabs.addTab(
				vPurchaseInvoicePaymentDTOTableTabLayout, 
				this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.payments"), 
				FontAwesome.CREDIT_CARD, 
				1);
		
		this.purchaseInvoicePaymentDTOTableTabLayoutTab.setVisible(true);		
	}
	
	@Override
	public void navigateToCallerView() {
		// TODO Auto-generated method stub
		logger.info("\n PurchaseInvoiceRegisterFormView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new PurchaseInvoiceRegisterFormViewEvent(
					this.purchaseInvoiceDTO,
					DashboardViewType.PURCHASE_INVOICE_REGISTER_FORM.getViewName(),
					this.editFormMode));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}

	@Override
	public void saveButtonActionPurchaseInvoiceDTOTabLayout(
			PurchaseInvoiceDTO purchaseInvoiceDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		if(!this.editFormMode){
			this.purchaseInvoiceDTO = this.cashMovementsManagementService.insertPurchaseInvoiceDTO(purchaseInvoiceDTO);
			this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
			this.restartFormAfterSuccesfulInsertOperation(new PurchaseInvoiceDTO(),false);
		}else{
			this.purchaseInvoiceDTO = this.cashMovementsManagementService.updatePurchaseInvoiceDTO(purchaseInvoiceDTO);
			this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
			this.navigateToCallerView();
		}
		
	}
	
	private void restartFormAfterSuccesfulInsertOperation(final PurchaseInvoiceDTO vPurchaseInvoiceDTO, final boolean editFormMode){
		this.purchaseInvoiceDTO = vPurchaseInvoiceDTO;
		this.editFormMode = editFormMode;
		this.initMainViewLayout();
		this.initTitles();
		this.initTabSheet();
		this.setUpPurchaseInvoiceDTOTabLayout();
		this.setUpPurchaseInvoicePaymentDTOTableTabLayout();
		this.setUpPurchaseInvoiceCreditNoteDTOTableLayout();
		this.setUpPurchaseInvoiceCreditNoteDTOLayout();
	    this.mainViewLayout.addComponent(this.tabs);
	    this.removeAndReAddMainViewLayout();
	    this.refreshTabSelection();
	}

	@Override
	public void initAndShowCashReceiptDocumentDTOWindow(
			PurchaseInvoicePaymentDTO purchaseInvoicePaymentDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		this.cashReceiptDocumentDTOWindow = 
				new CashReceiptDocumentDTOWindow(
						new CashReceiptDocumentDTO(
								purchaseInvoicePaymentDTO.getAmount(),
								purchaseInvoicePaymentDTO.getCurrencyDTO()
								),false/*queryWindowMode*/,true/*showOverdueOption*/
						);
		this.cashReceiptDocumentDTOWindow.addCloseListener(this.setUpCashReceiptDocumentDTOWindowCloseListener(purchaseInvoicePaymentDTO));
		this.cashReceiptDocumentDTOWindow.adjuntWindowSizeAccordingToCientDisplay();
		
	}
	


	private CloseListener setUpCashReceiptDocumentDTOWindowCloseListener(final PurchaseInvoicePaymentDTO purchaseInvoicePaymentDTO){
		return new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{
					if(!cashReceiptDocumentDTOWindow.isQueryWindowMode() && cashReceiptDocumentDTOWindow.isFormConfirmed()){
						final CashReceiptDocumentDTO vCashReceiptDocumentDTO =  cashReceiptDocumentDTOWindow.getCashReceiptDocumentDTO();
						vCashReceiptDocumentDTO.setId_currency(purchaseInvoicePaymentDTO.getId_currency());
						vCashReceiptDocumentDTO.setId_purchase_invoice_payment(purchaseInvoicePaymentDTO.getId());
						vCashReceiptDocumentDTO.setId_purchase_invoice(purchaseInvoiceDTO.getId());
						logger.info("\n******************************"
									+"\n the cash receipt document window has been closed"
									+"\n " + vCashReceiptDocumentDTO
									+"\n******************************");
						
						restartFormAfterSuccesfulInsertOperation(
						cashMovementsManagementService.effectuatePurchaseInvoicePaymentCashReceiptDocumentDTO
									(vCashReceiptDocumentDTO)
						,true);
					}
				}catch(Exception ex){
					commonExceptionErrorNotification.showErrorMessageNotification(ex);
				}
			}			
		};
	}

	@Override
	public void queryCashReceiptDocumentDTOWindow(
			PurchaseInvoicePaymentDTO purchaseInvoicePaymentDTO) {
		// TODO Auto-generated method stub
		purchaseInvoicePaymentDTO.getCashReceiptDocumentDTO().setCurrencyDTO(purchaseInvoicePaymentDTO.getCurrencyDTO());
		this.cashReceiptDocumentDTOWindow = 
				new CashReceiptDocumentDTOWindow(
						purchaseInvoicePaymentDTO.getCashReceiptDocumentDTO(),true/*queryWindowMode*/, true/*showOverdueOption*/
						);
		this.cashReceiptDocumentDTOWindow.addCloseListener(this.setUpCashReceiptDocumentDTOWindowCloseListener(purchaseInvoicePaymentDTO));
		this.cashReceiptDocumentDTOWindow.adjuntWindowSizeAccordingToCientDisplay();
	}

	@Override
	public void insertPurchaseInvoiceCreditNoteDTO(
			PurchaseInvoiceCreditNoteDTO purchaseInvoiceCreditNoteDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		this.cashMovementsManagementService.insertPurchaseInvoiceCreditNoteDTO(purchaseInvoiceCreditNoteDTO);
		this.restartFormAfterSuccesfulInsertOperation(this.purchaseInvoiceDTO, true);
		this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		this.tabs.setSelectedTab(2);
	}

	@Override
	public void cancelButtonPurchaseInvoiceCreditNoteDTOLayout(final boolean editFormMode) {
		// TODO Auto-generated method stub
		if(!editFormMode){
			//re init the credit note tab
			this.setUpPurchaseInvoiceCreditNoteDTOLayout();
			this.tabs.setSelectedTab(3);
		}else{
			//switch to credit note table tab
			this.tabs.setSelectedTab(2);
		}
	}
	
	private void setUpPurchaseInvoiceCreditNoteDTOTableLayout(){
		PurchaseInvoiceCreditNoteDTOTableLayout vPurchaseInvoiceCreditNoteDTOTableLayout = 
				new PurchaseInvoiceCreditNoteDTOTableLayout(this, this.VIEW_NAME, this.purchaseInvoiceDTO);
		vPurchaseInvoiceCreditNoteDTOTableLayout.setId("vPurchaseInvoiceCreditNoteDTOTableLayout");
		if(this.purchaseInvoiceCreditNoteDTOTableLayoutTab!=null) this.tabs.removeTab(this.purchaseInvoiceCreditNoteDTOTableLayoutTab);
		this.purchaseInvoiceCreditNoteDTOTableLayoutTab = this.tabs.addTab(
				vPurchaseInvoiceCreditNoteDTOTableLayout, 
				this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.credit.note.table"), 
				FontAwesome.BRIEFCASE, 
				2);
		this.purchaseInvoiceCreditNoteDTOTableLayoutTab.setEnabled(this.purchaseInvoiceDTO!=null && this.purchaseInvoiceDTO.getId()!=null);
	}
	
	private void setUpPurchaseInvoiceCreditNoteDTOLayout(){
		PurchaseInvoiceCreditNoteDTOLayout vPurchaseInvoiceCreditNoteDTOLayout = 
				new PurchaseInvoiceCreditNoteDTOLayout(
						this, 
						this.VIEW_NAME, 
						this.purchaseInvoiceDTO!=null && this.purchaseInvoiceDTO.getId()!=null ? 
						this.setUpPurchaseInvoiceCreditNoteDTOFromPurchaseInvoiceDTO() : null, 
						this.purchaseInvoiceDTO);
		vPurchaseInvoiceCreditNoteDTOLayout.setId("vPurchaseInvoiceCreditNoteDTOLayout");
		if(this.purchaseInvoiceCreditNoteDTOLayoutTab!=null)this.tabs.removeTab(this.purchaseInvoiceCreditNoteDTOLayoutTab);
		this.purchaseInvoiceCreditNoteDTOLayoutTab = this.tabs.addTab(
				vPurchaseInvoiceCreditNoteDTOLayout, 
				this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.credit.note.form"), 
				FontAwesome.NEWSPAPER_O, 
				3);
		this.purchaseInvoiceCreditNoteDTOLayoutTab.setEnabled(this.purchaseInvoiceDTO!=null && this.purchaseInvoiceDTO.getId()!=null);
	}
	
	private PurchaseInvoiceCreditNoteDTO setUpPurchaseInvoiceCreditNoteDTOFromPurchaseInvoiceDTO(){
		final PurchaseInvoiceCreditNoteDTO vPurchaseInvoiceCreditNoteDTO = new PurchaseInvoiceCreditNoteDTO();
		vPurchaseInvoiceCreditNoteDTO.setId_purchase_invoice(this.purchaseInvoiceDTO.getId());
		vPurchaseInvoiceCreditNoteDTO.setPersonDTO(this.purchaseInvoiceDTO.getPersonDTO());
		vPurchaseInvoiceCreditNoteDTO.setId_person(this.purchaseInvoiceDTO.getId_person());
		vPurchaseInvoiceCreditNoteDTO.setCurrencyDTO(this.purchaseInvoiceDTO.getCurrencyDTO());
		vPurchaseInvoiceCreditNoteDTO.setId_currency(this.purchaseInvoiceDTO.getId_currency());
		vPurchaseInvoiceCreditNoteDTO.setBussines_ci_ruc(this.purchaseInvoiceDTO.getBussines_ci_ruc());
		vPurchaseInvoiceCreditNoteDTO.setBussines_name(this.purchaseInvoiceDTO.getBussines_name());
		vPurchaseInvoiceCreditNoteDTO.setListPurchaseInvoiceCreditNoteItemDTO(new ArrayList<PurchaseInvoiceCreditNoteItemDTO>());
		PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO = null;
		for(PurchaseInvoiceItemDTO vPurchaseInvoiceItemDTO : this.purchaseInvoiceDTO.getListPurchaseInvoiceItemDTO()){
			vPurchaseInvoiceCreditNoteItemDTO = new PurchaseInvoiceCreditNoteItemDTO(
					vPurchaseInvoiceItemDTO,
					vPurchaseInvoiceItemDTO.getRawMaterialDTO(),
					vPurchaseInvoiceItemDTO.getMeasurmentUnitDTO(),
					vPurchaseInvoiceItemDTO.getId(),
					Math.round(Math.random() * 1000000L),
					vPurchaseInvoiceItemDTO.getUnit_price_amount());
			vPurchaseInvoiceCreditNoteItemDTO.setExempt_amount(BigDecimal.ZERO);
			vPurchaseInvoiceCreditNoteItemDTO.setValue_added_tax_5_amount(BigDecimal.ZERO);
			vPurchaseInvoiceCreditNoteItemDTO.setValue_added_tax_10_amount(BigDecimal.ZERO);
			vPurchaseInvoiceCreditNoteDTO.getListPurchaseInvoiceCreditNoteItemDTO().add(vPurchaseInvoiceCreditNoteItemDTO);
		}//for(PurchaseInvoiceItemDTO vPurchaseInvoiceItemDTO : this.purchaseInvoiceDTO.getListPurchaseInvoiceItemDTO()){		
		return vPurchaseInvoiceCreditNoteDTO;
	}

	@Override
	public void preparePurchaseInvoicePaymentDTOPaymentProcedure(
			PurchaseInvoicePaymentDTO purchaseInvoicePaymentDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		this.retrieveUsablePurchaseInvoiceCreditNoteDTOForPaymentProcedure(purchaseInvoicePaymentDTO);
		if(this.listPurchaseInvoiceCreditNoteDTOForPaymentProcedure!=null && !this.listPurchaseInvoiceCreditNoteDTOForPaymentProcedure.isEmpty())
			this.preparePurchaseInvoicePaymentDTOConfirmationWithPurchaseInvoiceCreditNoteDTOListOptions(purchaseInvoicePaymentDTO);
		else{
			/*standard payment without credit note*/
			this.initAndShowCashReceiptDocumentDTOWindow(purchaseInvoicePaymentDTO);
		}
	}
	
	private void retrieveUsablePurchaseInvoiceCreditNoteDTOForPaymentProcedure(final PurchaseInvoicePaymentDTO purchaseInvoicePaymentDTO) throws PmsServiceException{
		this.listPurchaseInvoiceCreditNoteDTOForPaymentProcedure = 
				this.cashMovementsManagementService.listPurchaseInvoiceCreditNoteDTO(
						new PurchaseInvoiceCreditNoteDTO(
								null, 
								this.purchaseInvoiceDTO.getId_person(),/*usable credit notes by supplier*/ 
								true));		
	}
	
	private void preparePurchaseInvoicePaymentDTOConfirmationWithPurchaseInvoiceCreditNoteDTOListOptions(final PurchaseInvoicePaymentDTO purchaseInvoicePaymentDTO) throws PmsServiceException{
		this.vConfirmOptionsWindow = new ConfirmOptionsWindow();
		this.vConfirmOptionsWindow = new ConfirmOptionsWindow(
				this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.payments.effectuate.payment.using.credit.note"), 
				this.messages.get("application.common.number.indicator.label") 
				+ ": " + purchaseInvoicePaymentDTO.getPayment_number() + "\n"
				+ this.messages.get("application.common.amount.label")
				+ ": " + purchaseInvoicePaymentDTO.getCurrencyDTO().getId_code()
				+ " " + this.sgpUtils.decodeNumericContentFromParam(purchaseInvoicePaymentDTO.getAmount().toString(), 
						bussinesSessionUtils.getRawSessionData().getUserSessionLocale()), 
				this.messages.get("application.common.button.save.label"), 
				this.messages.get("application.common.button.cancel.label"), 
				this.prepareOptionByPurchaseInvoiceCreditNoteDTO(purchaseInvoicePaymentDTO),
				this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.credit.note.table"));
		this.vConfirmOptionsWindow.addCloseListener(this.setUpConfirmOptionsWindowCloseListener());
		this.vConfirmOptionsWindow.setDecision(new ConfirmOptionsWindowOkCancelHandler(purchaseInvoicePaymentDTO));
		this.vConfirmOptionsWindow.adjuntWindowSizeAccordingToCientDisplay();
	}
	
	private CloseListener setUpConfirmOptionsWindowCloseListener(){
		return new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{
					//vSelectOneOptionSelectedOption = (SelectOneOption)vConfirmOptionsWindow.getSelectOptionGroup().getValue();
					logger.info( "\n******************************************************"
								+"\n the confirm option window has been closed"
								+"\n option selected: \n" + vSelectOneOptionSelectedOption
								+"\n******************************************************");
					
				}catch(Exception ex){
					commonExceptionErrorNotification.showErrorMessageNotification(ex);
				}
			}			
		};
	}
	
	private List<SelectOneOption> prepareOptionByPurchaseInvoiceCreditNoteDTO(final PurchaseInvoicePaymentDTO purchaseInvoicePaymentDTO) throws PmsServiceException {
		List<SelectOneOption> listSelectOneOptionCreditNotes = new ArrayList<SelectOneOption>();
		for(PurchaseInvoiceCreditNoteDTO vPurchaseInvoiceCreditNoteDTO : this.listPurchaseInvoiceCreditNoteDTOForPaymentProcedure){
			listSelectOneOptionCreditNotes.add(
					this.vConfirmOptionsWindow.new SelectOneOption(
					vPurchaseInvoiceCreditNoteDTO.getId().toString(),
					vPurchaseInvoiceCreditNoteDTO.getIdentifier_number() + " ; " 
					+ this.messages.get("application.common.balance.label") + ": "

					+ this.sgpUtils.decodeNumericContentFromParam(
							this.comercialManagementService.convertCurrencyAmount(
									new CurrencyDTO(
											vPurchaseInvoiceCreditNoteDTO.getId_currency(), 
											purchaseInvoicePaymentDTO.getId_currency(), 
											vPurchaseInvoiceCreditNoteDTO.getCredit_note_balance()))
											.toString(), 
					bussinesSessionUtils.getRawSessionData().getUserSessionLocale()))
					);
		}		
		return listSelectOneOptionCreditNotes;
	}
	
	private void checkSelectedPurchaseInvoiceCreditNoteDTOSufficiencyForPurchaseInvoicePaymentDTO(final PurchaseInvoicePaymentDTO vPurchaseInvoicePaymentDTO) throws PmsServiceException{
		int index = this.listPurchaseInvoiceCreditNoteDTOForPaymentProcedure.indexOf(
				new PurchaseInvoiceCreditNoteDTO(Long.valueOf(vSelectOneOptionSelectedOption.getKey())));
		this.selectedPurchaseInvoiceCreditNoteDTOForPaymentProcedure = this.listPurchaseInvoiceCreditNoteDTOForPaymentProcedure.get(index);
		logger.info( "\n ============================================="
					+"\n selected credit note : " + this.selectedPurchaseInvoiceCreditNoteDTOForPaymentProcedure.getIdentifier_number()
					+"\n balance : " + this.selectedPurchaseInvoiceCreditNoteDTOForPaymentProcedure.getCredit_note_balance() 
					+ " ; " +  this.selectedPurchaseInvoiceCreditNoteDTOForPaymentProcedure.getCurrencyDTO().getId_code()
					+ "\n prepare the cash receipt document dto"
					+"\n =============================================");
		final CashReceiptDocumentDTO vCashReceiptDocumentDTO = new CashReceiptDocumentDTO();
		vCashReceiptDocumentDTO.setCurrencyDTO(vPurchaseInvoicePaymentDTO.getCurrencyDTO());
		vCashReceiptDocumentDTO.setId_purchase_invoice_credit_note(this.selectedPurchaseInvoiceCreditNoteDTOForPaymentProcedure.getId());
		vCashReceiptDocumentDTO.setId_purchase_invoice_payment(vPurchaseInvoicePaymentDTO.getId());
		vCashReceiptDocumentDTO.setId_purchase_invoice(vPurchaseInvoicePaymentDTO.getId_purchase_invoice());
		final BigDecimal purchaseInvoiceCreditNoteBalance = this.comercialManagementService.convertCurrencyAmount(
				new CurrencyDTO(
						this.selectedPurchaseInvoiceCreditNoteDTOForPaymentProcedure.getId_currency(), 
						vPurchaseInvoicePaymentDTO.getId_currency(), 
						this.selectedPurchaseInvoiceCreditNoteDTOForPaymentProcedure.getCredit_note_balance()));
//		if(vPurchaseInvoicePaymentDTO.getAmount().compareTo(purchaseInvoiceCreditNoteBalance) == 1)// P > CN
//		{
//			vCashReceiptDocumentDTO.setAmount(vPurchaseInvoicePaymentDTO.getAmount().subtract(purchaseInvoiceCreditNoteBalance));
//			//show cash receipt document window
//			this.showCashReceiptDocumentWindowPreSetedCashReceiptDocumentDTO(vCashReceiptDocumentDTO, vPurchaseInvoicePaymentDTO, true/*overDueOption*/);
//		}else{//P <= CN
//			//show BigDecimalEditQuantityWindow: to ask if payment is with overdue amount
//			this.showOverdueAmountBigDecimalEditQuantityWindow(vCashReceiptDocumentDTO, vPurchaseInvoicePaymentDTO, purchaseInvoiceCreditNoteBalance);
//		}
		
		this.showOverdueAmountBigDecimalEditQuantityWindow(vCashReceiptDocumentDTO, vPurchaseInvoicePaymentDTO, purchaseInvoiceCreditNoteBalance);
	}
	
	
	private void showOverdueAmountBigDecimalEditQuantityWindow(final CashReceiptDocumentDTO vCashReceiptDocumentDTO, final PurchaseInvoicePaymentDTO vPurchaseInvoicePaymentDTO, final BigDecimal purchaseInvoiceCreditNoteBalance){
		final double vdouble = 0.1;
		this.editBigDecimalQuantityWindow = 
				new EditBigDecimalQuantityWindow(						
						this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.payments.overdue.amount.check.window.caption"),
						this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.payments.overdue.amount.check.window.payment.original.amount") + " : "
						+ vPurchaseInvoicePaymentDTO.getCurrencyDTO().getId_code() + " "+ vPurchaseInvoicePaymentDTO.getAmount()
						+" \n" + this.messages.get("application.common.status.partial.balance") 
						+ " " + this.messages.get("application.common.document.type.credit.note")
						+ " : " + purchaseInvoiceCreditNoteBalance,						
						null,
						BigDecimal.valueOf(vdouble).add(vPurchaseInvoicePaymentDTO.getAmount()),//minimal value = vPurchaseInvoicePaymentDTO.getAmount() + 0.1 
						vPurchaseInvoicePaymentDTO.getAmount().add(vPurchaseInvoicePaymentDTO.getAmount()));
		this.editBigDecimalQuantityWindow.addCloseListener(this.setUpEditQuantityWindowCloseListener(vCashReceiptDocumentDTO,vPurchaseInvoicePaymentDTO,purchaseInvoiceCreditNoteBalance));
		this.editBigDecimalQuantityWindow.adjuntWindowSizeAccordingToCientDisplay();
	}
	
	
	private CloseListener setUpEditQuantityWindowCloseListener(final CashReceiptDocumentDTO vCashReceiptDocumentDTO, final PurchaseInvoicePaymentDTO vPurchaseInvoicePaymentDTO, final BigDecimal purchaseInvoiceCreditNoteBalance){
		return new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{					
					logger.info("\n******************************"
								+"\n the OverdueAmount quantity editor window has been closed"
								+"\n overdue amount : " + editBigDecimalQuantityWindow.getQuantityEditor().getQuantity()
								+"\n ENTER KEY ACTION CLOSED THE WINDOW ? : " + editBigDecimalQuantityWindow.isEnterKeyActionWindowClosed()
								+"\n******************************");
					final BigDecimal overdueAmount = editBigDecimalQuantityWindow.getQuantityEditor().getQuantity();
					if(overdueAmount != null && editBigDecimalQuantityWindow.isEnterKeyActionWindowClosed())
						checkOverdueAmountEnteredWithSelectedPurchaseInvoiceCreditNoteForPaymentProcedure(overdueAmount,vCashReceiptDocumentDTO,vPurchaseInvoicePaymentDTO);
					else{
						//excecute the payment
						logger.info( "\n ================================================== "
									+"\n no overdue amount entered or window escaped with ESCAPE KEY"
									+"\n excecute the payment"
									+"\n ================================================== ");
						//check if the original payment amount is > CN
						if(vPurchaseInvoicePaymentDTO.getAmount().compareTo(purchaseInvoiceCreditNoteBalance) == 1){
							vCashReceiptDocumentDTO.setAmount(vPurchaseInvoicePaymentDTO.getAmount().subtract(purchaseInvoiceCreditNoteBalance));
							//show cash receipt document window
							vCashReceiptDocumentDTO.setAmount(vPurchaseInvoicePaymentDTO.getAmount().subtract(purchaseInvoiceCreditNoteBalance));
							showCashReceiptDocumentWindowPreSetedCashReceiptDocumentDTO(vCashReceiptDocumentDTO, vPurchaseInvoicePaymentDTO, false/*overDueOption*/);
						}else{
							restartFormAfterSuccesfulInsertOperation(
							cashMovementsManagementService.effectuatePurchaseInvoicePaymentCashReceiptDocumentDTO(vCashReceiptDocumentDTO)
							,true);
						}
					}
				}catch(Exception ex){					
					commonExceptionErrorNotification.showErrorMessageNotification(ex);
				}
			}
		};
	}
	
	private void checkOverdueAmountEnteredWithSelectedPurchaseInvoiceCreditNoteForPaymentProcedure(final BigDecimal overdueAmount,final CashReceiptDocumentDTO vCashReceiptDocumentDTO, final PurchaseInvoicePaymentDTO vPurchaseInvoicePaymentDTO) throws PmsServiceException{
		final BigDecimal purchaseInvoiceCreditNoteBalance = this.comercialManagementService.convertCurrencyAmount(
				new CurrencyDTO(
						this.selectedPurchaseInvoiceCreditNoteDTOForPaymentProcedure.getId_currency(), 
						vPurchaseInvoicePaymentDTO.getId_currency(), 
						this.selectedPurchaseInvoiceCreditNoteDTOForPaymentProcedure.getCredit_note_balance()));
		if(overdueAmount.compareTo(purchaseInvoiceCreditNoteBalance)==1){
			//show cash receipt document window form with amount = overdueAmount - CN balance
			//do not show the overdue amount input in the form
			vCashReceiptDocumentDTO.setAmount(overdueAmount.subtract(purchaseInvoiceCreditNoteBalance));//just for showing purpouse
			vCashReceiptDocumentDTO.setOverduePaymentamount(overdueAmount);//FOR CALCULATION PURPOUSE !
			this.showCashReceiptDocumentWindowPreSetedCashReceiptDocumentDTO(vCashReceiptDocumentDTO, vPurchaseInvoicePaymentDTO, false/*overDueOption*/);
			
		}else{//overDueAmount <= CN
			//excecute the payment with out showing the cash receipt document window
			vCashReceiptDocumentDTO.setOverduePaymentamount(overdueAmount);
			logger.info( "\n ================================================== "
						+"\n overdue amount equal or less than credit note balance"
						+"\n excecute the payment with out showing the cash receipt document window"
						+"\n ================================================== ");
			restartFormAfterSuccesfulInsertOperation(
			cashMovementsManagementService.effectuatePurchaseInvoicePaymentCashReceiptDocumentDTO(vCashReceiptDocumentDTO)
			,true);
		}
		
	}
	
	private class ConfirmOptionsWindowOkCancelHandler implements ConfirmWindowDecision{

		private final PurchaseInvoicePaymentDTO purchaseInvoicePaymentDTO;
		public ConfirmOptionsWindowOkCancelHandler(final PurchaseInvoicePaymentDTO purchaseInvoicePaymentDTO){
			this.purchaseInvoicePaymentDTO = purchaseInvoicePaymentDTO;
		}
		
		@Override
		public void yes(ClickEvent event) {
			// TODO Auto-generated method stub
			try{
				vSelectOneOptionSelectedOption = (SelectOneOption)vConfirmOptionsWindow.getSelectOptionGroup().getValue();		
				checkSelectedPurchaseInvoiceCreditNoteDTOSufficiencyForPurchaseInvoicePaymentDTO(this.purchaseInvoicePaymentDTO);
				
        	}catch(Exception e){
        		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
        	}
		}

		@Override
		public void no(ClickEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private void showCashReceiptDocumentWindowPreSetedCashReceiptDocumentDTO(
			final CashReceiptDocumentDTO vCashReceiptDocumentDTO, final PurchaseInvoicePaymentDTO vPurchaseInvoicePaymentDTO, final boolean overDueOption){
		this.cashReceiptDocumentDTOWindow = 
				new CashReceiptDocumentDTOWindow(
						vCashReceiptDocumentDTO,false/*queryWindowMode*/,overDueOption/*showOverdueOption*/);
		this.cashReceiptDocumentDTOWindow.addCloseListener(
				this.setUpCashReceiptDocumentWindowPreSetedCashReceiptDocumentDTOWindowCloseListener(vPurchaseInvoicePaymentDTO));
		this.cashReceiptDocumentDTOWindow.adjuntWindowSizeAccordingToCientDisplay();
	}
	
	private CloseListener setUpCashReceiptDocumentWindowPreSetedCashReceiptDocumentDTOWindowCloseListener(final PurchaseInvoicePaymentDTO vPurchaseInvoicePaymentDTO){
		return new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{
					if(!cashReceiptDocumentDTOWindow.isQueryWindowMode() && cashReceiptDocumentDTOWindow.isFormConfirmed()){
						final CashReceiptDocumentDTO vCashReceiptDocumentDTO =  cashReceiptDocumentDTOWindow.getCashReceiptDocumentDTO();
						logger.info("\n******************************"
									+"\n the cash receipt document window has been closed"
									+"\n " + vCashReceiptDocumentDTO
									+"\n******************************");
						
						restartFormAfterSuccesfulInsertOperation(
						cashMovementsManagementService.effectuatePurchaseInvoicePaymentCashReceiptDocumentDTO
									(vCashReceiptDocumentDTO)
						,true);
					}
				}catch(Exception ex){
					commonExceptionErrorNotification.showErrorMessageNotification(ex);
				}
			}			
		};
	}
}
