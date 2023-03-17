package py.com.kyron.sgp.gui.view.cash.movements.management;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.print.PrintException;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CashReceiptDocumentDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteItemDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceItemDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoicePaymentDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.service.CashMovementsManagementService;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.OrderRegisterFormViewEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.SaleInvoiceRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.OnDemandFileDownloader;
import py.com.kyron.sgp.gui.utils.SgpPrintService;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.utils.OnDemandFileDownloader.OnDemandStreamResource;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.register.form.view.components.CreditNoteDTOTabLayout;
import py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.register.form.view.components.CreditNoteDTOTableTabLayout;
import py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.register.form.view.components.SaleInvoiceDTOTabLayout;
import py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.register.form.view.components.SaleInvoicePaymentDTOTableTabLayout;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.ConfirmOptionsWindow;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.ConfirmWindow;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.ConfirmWindowDecision;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.Decision;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.ConfirmOptionsWindow.SelectOneOption;

import com.google.common.eventbus.Subscribe;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedHttpSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

@SuppressWarnings("serial")
public class SaleInvoiceRegisterFormView extends VerticalLayout implements View {

	private final Logger logger = LoggerFactory.getLogger(SaleInvoiceRegisterFormView.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME = "sale.invoice.management.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private boolean editFormMode;
	private OrderDTO orderDTO;
	private SaleInvoiceDTO saleInvoiceDTO;
	private TabSheet tabs;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private VerticalLayout mainViewLayout;
	private String CALLER_VIEW;
	private Tab saleInvoiceDTOTabLayoutTab;
	private Tab saleInvoicePaymentDTOTableTabLayoutTab;
	private Tab creditNoteDTOTableTabLayoutTab;
	private Tab creditNoteDTOTabLayoutTab;
	private String selectedTabContentComponentId;
	private CashMovementsManagementService cashMovementsManagementService;
	private ConfirmOptionsWindow vConfirmOptionsWindow;
	private SelectOneOption vSelectOneOptionSelectedOption;
	private SgpUtils sgpUtils;
	private BussinesSessionUtils bussinesSessionUtils;
	private List<CreditNoteDTO> saleInvoiceListCreditNoteDTO;
	private SgpPrintService sgpPrintService;
	private byte[] downloadData;
	private String downloadFileName;
	private ComercialManagementService comercialManagementService;
	private boolean saleInvoiceInRevisionStatusCheck;
	
	public SaleInvoiceRegisterFormView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n SaleInvoiceRegisterFormView..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.sgpUtils = new SgpUtils();
			this.sgpPrintService = new SgpPrintService();
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
		this.cashMovementsManagementService = (CashMovementsManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.CASH_MOVEMENTS_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		this.comercialManagementService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
	}
	/*public SaleInvoiceRegisterFormView(Component... children) {
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
	public void setOrderDTOsaleInvoiceDTO(final SaleInvoiceRegisterFormViewEvent vSaleInvoiceRegisterFormViewEvent){
		try{
			this.CALLER_VIEW = vSaleInvoiceRegisterFormViewEvent.getCallerView();
			this.editFormMode = vSaleInvoiceRegisterFormViewEvent.isEditFormMode();
			this.orderDTO = vSaleInvoiceRegisterFormViewEvent.getOrderDTO();
			this.saleInvoiceDTO = vSaleInvoiceRegisterFormViewEvent.getSaleInvoiceDTO();
			this.initMainViewLayout();
			this.initTitles();
			this.initTabSheet();
			this.setUpSaleInvoiceDTOTabLayout();
			if(this.saleInvoiceDTO != null){
				this.setUpSaleInvoicePaymentDTOTableTabLayout();
				this.setUpCreditNoteDTOTableTabLayout();
				this.setUpCreditNoteDTOTabLayout(null);
			}
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
			Label title = null;
			if(!this.editFormMode && this.saleInvoiceDTO!=null)
				title = new Label(this.messages.get(this.VIEW_NAME + "main.title.query"));
			else if(this.editFormMode && this.saleInvoiceDTO!=null)
				title = new Label(this.messages.get(this.VIEW_NAME + "main.title.edit"));
			else
				title = new Label(this.messages.get(this.VIEW_NAME + "main.title.generate"));
			
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
	
	private void setUpSaleInvoiceDTOTabLayout(){
		SaleInvoiceDTOTabLayout vSaleInvoiceDTOTabLayout = new SaleInvoiceDTOTabLayout(this, this.saleInvoiceDTO, this.orderDTO);
		vSaleInvoiceDTOTabLayout.setId("vSaleInvoiceDTOTabLayout");
		this.saleInvoiceDTOTabLayoutTab = this.tabs.addTab(vSaleInvoiceDTOTabLayout, 
				this.messages.get(this.VIEW_NAME + "tab.sale.invoice.form"),
				FontAwesome.SELLSY, 
				0);
	}
	
	private void setUpSaleInvoicePaymentDTOTableTabLayout(){
		SaleInvoicePaymentDTOTableTabLayout vSaleInvoicePaymentDTOTableTabLayout = 
				new SaleInvoicePaymentDTOTableTabLayout(this,this.saleInvoiceDTO);
		vSaleInvoicePaymentDTOTableTabLayout.setId("vSaleInvoicePaymentDTOTableTabLayout");
		this.saleInvoicePaymentDTOTableTabLayoutTab = this.tabs.addTab(vSaleInvoicePaymentDTOTableTabLayout, 
				this.messages.get(this.VIEW_NAME + "tab.sale.invoice.payments"),
				FontAwesome.PAYPAL, 
				1);
		this.saleInvoicePaymentDTOTableTabLayoutTab.setEnabled(this.saleInvoiceDTO!=null);
		
	}
	
	public void navigateToCallerView(){
		logger.info("\n SaleInvoiceRegisterFormView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new SaleInvoiceRegisterFormViewEvent(
					this.orderDTO, 
					this.saleInvoiceDTO,
					DashboardViewType.SALE_INVOICE_REGISTER_FORM.getViewName(),
					this.editFormMode));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	public void preparePaymentEffectuation(final SaleInvoicePaymentDTO vSaleInvoicePaymentDTO) throws PmsServiceException{
		//
		this.setUpPaymentEffectuationConfirmWindow(vSaleInvoicePaymentDTO);
	}
	
	public void generateSaleInvoicePayments(final SaleInvoiceDTO saleInvoiceDTO) throws PmsServiceException{		
		this.cashMovementsManagementService.insertSaleInvoicePaymentDTO(new SaleInvoicePaymentDTO(null, saleInvoiceDTO.getId()));		
		this.reStartLayout(saleInvoiceDTO);
		this.tabs.setSelectedTab(this.saleInvoicePaymentDTOTableTabLayoutTab);
	}
	
	public void reGenerateSaleInvoicePaymentDTO(final SaleInvoiceDTO saleInvoiceDTO) throws PmsServiceException{		
		this.cashMovementsManagementService.reGenerateSaleInvoicePaymentDTO(new SaleInvoicePaymentDTO(null, saleInvoiceDTO.getId()));		
		this.reStartLayout(saleInvoiceDTO);
		this.tabs.setSelectedTab(this.saleInvoicePaymentDTOTableTabLayoutTab);
	}
	
	public void reStartLayout(final SaleInvoiceDTO saleInvoiceDTO) throws PmsServiceException{
		this.saleInvoiceDTO = this.cashMovementsManagementService.listSaleInvoiceDTO(new SaleInvoiceDTO(saleInvoiceDTO.getId())).get(0);
		this.saleInvoiceDTO.setListSaleInvoicePaymentDTO(this.cashMovementsManagementService.listSaleInvoicePaymentDTO(new SaleInvoicePaymentDTO(null, this.saleInvoiceDTO.getId())));
		
		this.initMainViewLayout();
		this.initTitles();
		this.initTabSheet();
		this.setUpSaleInvoiceDTOTabLayout();
		this.setUpSaleInvoicePaymentDTOTableTabLayout();
		this.setUpCreditNoteDTOTableTabLayout();
		this.setUpCreditNoteDTOTabLayout(null);
	    this.mainViewLayout.addComponent(this.tabs);
	    this.removeAndReAddMainViewLayout();
	    this.refreshTabSelection();
	}
	
	public void updateSaleInvoiceDTO(final SaleInvoiceDTO vSaleInvoiceDTO) throws PmsServiceException{
		this.saleInvoiceInRevisionStatusCheck = vSaleInvoiceDTO.getStatus().equals("application.common.status.revision") ? true : false;
		this.cashMovementsManagementService.updateSaleInvoiceDTO(vSaleInvoiceDTO);		
		if(this.saleInvoiceInRevisionStatusCheck){
			final OrderDTO vOrderDTO = this.comercialManagementService.listOrderDTO(new OrderDTO(vSaleInvoiceDTO.getId_order())).get(0);
			this.goToRegisterOrderView(vOrderDTO);
			this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		}
		this.reStartLayout(vSaleInvoiceDTO);
		
	}
	
	public void reGenerateSaleInvoiceFromOrder(final SaleInvoiceDTO vSaleInvoiceDTO) throws PmsServiceException{
		this.cashMovementsManagementService.reGenerateSaleInvoiceDTO(vSaleInvoiceDTO);
		this.reStartLayout(vSaleInvoiceDTO);
	}
	
	public void annulSaleInvoiceDTO(final SaleInvoiceDTO vSaleInvoiceDTO) throws PmsServiceException{
		this.cashMovementsManagementService.annulSaleInvoiceDTO(vSaleInvoiceDTO);
		this.reStartLayout(vSaleInvoiceDTO);
	}
	
	public void prepareCreditNoteDTOGeneration() throws PmsServiceException{
		//build and set visible the CreditNoteDTOTabLayout
	}
	
	public void insertCreditNoteDTO(CreditNoteDTO vCcreditNoteDTO) throws PmsServiceException{
		this.cashMovementsManagementService.insertCreditNoteDTO(vCcreditNoteDTO);
		//re start the credit note table tab and re init the credit note tab
		//switch to credit note table tab
		this.reInitCreditNoteDTOTableTabLayout();
		this.reInitCreditNoteDTOTabLayout();
		this.tabs.setSelectedTab(2);
	}
	
	public void cancelButtonClickCreditNoteDTOTabLayout(final boolean editFormMode){
		if(!editFormMode){
			//re init the credit note tab
			this.reInitCreditNoteDTOTabLayout();
			this.tabs.setSelectedTab(3);
		}else{
			//switch to credit note table tab
			this.tabs.setSelectedTab(2);
		}
	}
	
	private void setUpCreditNoteDTOTableTabLayout(){
		CreditNoteDTOTableTabLayout vCreditNoteDTOTableTabLayout = new CreditNoteDTOTableTabLayout(this,this.saleInvoiceDTO);
		vCreditNoteDTOTableTabLayout.setId("vCreditNoteDTOTableTabLayout");
		this.creditNoteDTOTableTabLayoutTab = this.tabs.addTab
				(vCreditNoteDTOTableTabLayout, 
						this.messages.get(this.VIEW_NAME + "tab.credit.notes"), 
						FontAwesome.CREDIT_CARD_ALT, 
						2);		
	}
	
	private void reInitCreditNoteDTOTabLayout(){
		this.tabs.removeTab(this.creditNoteDTOTabLayoutTab);
		this.setUpCreditNoteDTOTabLayout(null);
	}
	
	private void reInitCreditNoteDTOTableTabLayout(){
		this.tabs.removeTab(this.creditNoteDTOTableTabLayoutTab);
		this.setUpCreditNoteDTOTableTabLayout();
	}
	
	private void setUpCreditNoteDTOTabLayout(final CreditNoteDTO vCreditNoteDTO){
		CreditNoteDTOTabLayout vCreditNoteDTOTabLayout = 
				new CreditNoteDTOTabLayout(this,
						(vCreditNoteDTO == null ? this.prepareCreditNoteFromSaleInvoiceDTO() : vCreditNoteDTO), this.saleInvoiceDTO.getOrderDTO());
		vCreditNoteDTOTabLayout.setId("vCreditNoteDTOTabLayout");
		this.creditNoteDTOTabLayoutTab = this.tabs.addTab
				(vCreditNoteDTOTabLayout, 
						this.messages.get(this.VIEW_NAME + "tab.credit.note.form"), 
						FontAwesome.CREDIT_CARD,
						3);
		this.creditNoteDTOTabLayoutTab.setEnabled(
				!this.saleInvoiceDTO.getStatus().equals("application.common.status.pending")
				&& !this.saleInvoiceDTO.getStatus().equals("application.common.status.payment.in.progress")
				&& !this.saleInvoiceDTO.getStatus().equals("application.common.status.annulled")
				&& !this.saleInvoiceDTO.getStatus().equals("application.common.status.revision"));
	}
	
	private CreditNoteDTO prepareCreditNoteFromSaleInvoiceDTO(){
		CreditNoteDTO vCreditNoteDTO = new CreditNoteDTO();
		vCreditNoteDTO.setId_sale_invoice(this.saleInvoiceDTO.getId());
		vCreditNoteDTO.setBussines_ci_ruc(this.saleInvoiceDTO.getBussines_ci_ruc());
		vCreditNoteDTO.setBussines_name(this.saleInvoiceDTO.getBussines_name());
		vCreditNoteDTO.setStatus("application.common.status.pending");
		vCreditNoteDTO.setIdentifier_number("Xxx-Xxx-Xxxxxxx");
		vCreditNoteDTO.setModified_documens_identifier_numbers(this.saleInvoiceDTO.getIdentifier_number());
		vCreditNoteDTO.setEmission_date( new Date());
		vCreditNoteDTO.setCurrencyDTO(this.saleInvoiceDTO.getOrderDTO().getCurrencyDTO());
		//prepare credit note details
		vCreditNoteDTO.setListCreditNoteItemDTO(new ArrayList<CreditNoteItemDTO>());
		for(SaleInvoiceItemDTO vSaleInvoiceItemDTO : this.saleInvoiceDTO.getListSaleInvoiceItemDTO()){
			if(!vSaleInvoiceItemDTO.getStatus().equals("application.common.status.discarded")){
				CreditNoteItemDTO vCreditNoteItemDTO = new CreditNoteItemDTO(vSaleInvoiceItemDTO);
				vCreditNoteItemDTO.setId_sale_invoice_item(vSaleInvoiceItemDTO.getId());
				
				vCreditNoteItemDTO.setUnit_price_amount(vSaleInvoiceItemDTO.getUnit_price_amount());
				vCreditNoteItemDTO.setExempt_unit_price_amount(BigDecimal.ZERO);
				vCreditNoteItemDTO.setValue_added_tax_5_unit_price_amount(BigDecimal.ZERO);
				vCreditNoteItemDTO.setValue_added_tax_10_unit_price_amount(BigDecimal.ZERO);
				vCreditNoteItemDTO.setProductDTO(vSaleInvoiceItemDTO.getProductDTO());
				/* temporal id */
				vCreditNoteItemDTO.setTemporal_id(Math.round(Math.random() * 1000000L));
				vCreditNoteDTO.getListCreditNoteItemDTO().add(vCreditNoteItemDTO);
			}
		}
		return vCreditNoteDTO;
	}
	
	private void setUpPaymentEffectuationConfirmWindow(final SaleInvoicePaymentDTO vSaleInvoicePaymentDTO) throws PmsServiceException{
		this.retrieveUsableCreditNotesByCustomer();
		if(this.saleInvoiceListCreditNoteDTO!=null && !this.saleInvoiceListCreditNoteDTO.isEmpty())
			this.preparePaymentConfirmationWithCreditNoteListOptions(vSaleInvoicePaymentDTO);
		else
			this.prepareSaleInvoicePaymentConfirmationWindow(vSaleInvoicePaymentDTO);
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
	
	private class ConfirmOptionsWindowOkCancelHandler implements ConfirmWindowDecision{

		private final SaleInvoicePaymentDTO saleInvoicePaymentDTO;
		public ConfirmOptionsWindowOkCancelHandler(final SaleInvoicePaymentDTO vSaleInvoicePaymentDTO){
			this.saleInvoicePaymentDTO = vSaleInvoicePaymentDTO;
		}
		
		@Override
		public void yes(ClickEvent event) {
			// TODO Auto-generated method stub
			try{
				vSelectOneOptionSelectedOption = (SelectOneOption)vConfirmOptionsWindow.getSelectOptionGroup().getValue();		
				effectuateSaleInvoicePaymentWithCreditNote(this.saleInvoicePaymentDTO);
				commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
        	}catch(Exception e){
        		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
        	}
		}

		@Override
		public void no(ClickEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private void retrieveUsableCreditNotesByCustomer()throws PmsServiceException{
		this.saleInvoiceListCreditNoteDTO = this.cashMovementsManagementService.listCreditNoteDTO(new CreditNoteDTO(this.saleInvoiceDTO.getId_person(),true));
	}
	
	private List<SelectOneOption> prepareOptionBySaleInvoiceCreditNotes(final SaleInvoicePaymentDTO vSaleInvoicePaymentDTO) throws PmsServiceException {
		List<SelectOneOption> listSelectOneOptionCreditNotes = new ArrayList<SelectOneOption>();
		BigDecimal creditNoteBalance = null;
		for(CreditNoteDTO vCreditNoteDTO : saleInvoiceListCreditNoteDTO){
			creditNoteBalance = this.comercialManagementService.convertCurrencyAmount(
					new CurrencyDTO(
							vCreditNoteDTO.getId_currency(), 
							vSaleInvoicePaymentDTO.getId_currency(), 
							vCreditNoteDTO.getCredit_note_balance()));
			listSelectOneOptionCreditNotes.add(
					this.vConfirmOptionsWindow.new SelectOneOption(
					vCreditNoteDTO.getId().toString(),
					vCreditNoteDTO.getIdentifier_number() + "; " 
					+ this.messages.get("application.common.balance.label") + ": "
					+ " " + vSaleInvoicePaymentDTO.getCurrencyDTO().getId_code() 
					+ " - " 
					+ this.sgpUtils.decodeNumericContentFromParam(creditNoteBalance.toString(), 
					bussinesSessionUtils.getRawSessionData().getUserSessionLocale()))
					);
		}		
		return listSelectOneOptionCreditNotes;
	}
	
	private void effectuateSaleInvoicePaymentWithCreditNote(final SaleInvoicePaymentDTO vSaleInvoicePaymentDTO) throws PmsServiceException{
		int index = this.saleInvoiceListCreditNoteDTO.indexOf(new CreditNoteDTO(Long.valueOf(vSelectOneOptionSelectedOption.getKey())));
		CreditNoteDTO vCreditNoteDTO = this.saleInvoiceListCreditNoteDTO.get(index);
		logger.info( "\n ============================================="
					+"\n " + vCreditNoteDTO.toString()
					+"\n =============================================");
		vSaleInvoicePaymentDTO.setId_credit_note(vCreditNoteDTO.getId());
		this.effectuateSaleInvoicePaymen(vSaleInvoicePaymentDTO);		
	}
	
	private void effectuateSaleInvoicePaymen(final SaleInvoicePaymentDTO vSaleInvoicePaymentDTO) throws PmsServiceException{
		this.cashMovementsManagementService.effectuateSaleInvoicePayment(vSaleInvoicePaymentDTO);
		//this.reInitSaleInvoicePaymentDTOTableTabLayout();
		this.reStartLayout(new SaleInvoiceDTO(vSaleInvoicePaymentDTO.getId_sale_invoice()));
		this.tabs.setSelectedTab(this.saleInvoicePaymentDTOTableTabLayoutTab);
	}
	
	private void preparePaymentConfirmationWithCreditNoteListOptions(final SaleInvoicePaymentDTO vSaleInvoicePaymentDTO) throws PmsServiceException{
		this.vConfirmOptionsWindow = new ConfirmOptionsWindow();
		this.vConfirmOptionsWindow = new ConfirmOptionsWindow(
				this.messages.get(this.VIEW_NAME + "tab.sale.invoice.payments.table.payments.column.operations.button.effectuate.payment.description"), 
				this.messages.get("application.common.number.indicator.label") 
				+ ": " + vSaleInvoicePaymentDTO.getPayment_number() + "\n"
				+ this.messages.get("application.common.amount.label")
				+ ": " + vSaleInvoicePaymentDTO.getCurrencyDTO().getId_code()
				+ " " + this.sgpUtils.decodeNumericContentFromParam(vSaleInvoicePaymentDTO.getAmount().toString(), 
						bussinesSessionUtils.getRawSessionData().getUserSessionLocale()), 
				this.messages.get("application.common.button.save.label"), 
				this.messages.get("application.common.button.cancel.label"), 
				this.prepareOptionBySaleInvoiceCreditNotes(vSaleInvoicePaymentDTO),
				this.messages.get(this.VIEW_NAME + "tab.credit.notes"));
		this.vConfirmOptionsWindow.addCloseListener(this.setUpConfirmOptionsWindowCloseListener());
		this.vConfirmOptionsWindow.setDecision(new ConfirmOptionsWindowOkCancelHandler(vSaleInvoicePaymentDTO));
		this.vConfirmOptionsWindow.adjuntWindowSizeAccordingToCientDisplay();
	}
	
	private void prepareSaleInvoicePaymentConfirmationWindow(final SaleInvoicePaymentDTO vSaleInvoicePaymentDTO){
    	final ConfirmWindow window = new ConfirmWindow(
    			this.messages.get(this.VIEW_NAME + "tab.sale.invoice.payments.table.payments.column.operations.button.effectuate.payment.description"),
				this.messages.get("application.common.number.indicator.label") 
				+ ": " + vSaleInvoicePaymentDTO.getPayment_number() + "\n"
				+ this.messages.get("application.common.amount.label")
				+ ": " + vSaleInvoicePaymentDTO.getCurrencyDTO().getId_code()
				+ " " + this.sgpUtils.decodeNumericContentFromParam(vSaleInvoicePaymentDTO.getAmount().toString(), 
						bussinesSessionUtils.getRawSessionData().getUserSessionLocale()),  
    			this.messages.get("application.common.confirmation.view.buttonlabel.yes"),
    			this.messages.get("application.common.confirmation.view.buttonlabel.no"),
    			true);
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
    	window.addShortcutListener(new ShortcutListener(this.messages.get("application.common.confirmation.view.shortcut.close"),ShortcutAction.KeyCode.ESCAPE, null) {
			@Override
			public void handleAction(Object sender, Object target) {
					window.close();
				}
			});
    	window.addCloseListener(new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				logger.info("\nwindow.addCloseListener\nwindow.isYesAccionRealized() : " + window.isYesAccionRealized()
						+"\nwindow.isNoAccionRealized() : " + window.isNoAccionRealized());
				try{
					if(window.isYesAccionRealized()){
						effectuateSaleInvoicePaymen(vSaleInvoicePaymentDTO);
						commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
					}				
				}catch(Exception exception){
					commonExceptionErrorNotification.showErrorMessageNotification(exception);
					window.resetYesNoAccionFlags();
				}
			}
    		
    	});
	}
	
	private void reInitSaleInvoicePaymentDTOTableTabLayout(){
		this.tabs.removeTab(this.saleInvoicePaymentDTOTableTabLayoutTab);
		this.setUpSaleInvoicePaymentDTOTableTabLayout();
		this.tabs.setSelectedTab(this.saleInvoicePaymentDTOTableTabLayoutTab);
	}
	
	public void showQueryCashReceiptDocumentWindow(final CashReceiptDocumentDTO vCashReceiptDocumentDTO){
    	final ConfirmWindow window = new ConfirmWindow(
    			this.messages.get(this.VIEW_NAME + "tab.sale.invoice.payments.table.payments.column.operations.button.query.cash.receipt.document.description"),
				this.messages.get("application.common.number.indicator.label") 
				+ ": " + vCashReceiptDocumentDTO.getIdentifier_number() + "\n"
				+ this.messages.get("application.common.commercial.name.label") 
				+ ": " +vCashReceiptDocumentDTO.getBussines_ci_ruc() +  "; " + vCashReceiptDocumentDTO.getBussines_name() + "\n"
				+ this.messages.get("application.common.amount.label")
				+ ": " + vCashReceiptDocumentDTO.getCurrencyDTO().getId_code()
				+ " " + this.sgpUtils.decodeNumericContentFromParam(vCashReceiptDocumentDTO.getAmount().toString(), 
						bussinesSessionUtils.getRawSessionData().getUserSessionLocale()),  
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
    	window.addShortcutListener(new ShortcutListener(this.messages.get("application.common.confirmation.view.shortcut.close"),ShortcutAction.KeyCode.ESCAPE, null) {
			@Override
			public void handleAction(Object sender, Object target) {
					window.close();
				}
			});
    	window.addCloseListener(new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{
					logger.info("\nwindow.addCloseListener\nwindow.isYesAccionRealized() : " + window.isYesAccionRealized()
							+"\nwindow.isNoAccionRealized() : " + window.isNoAccionRealized());
				}catch(Exception exception){
					commonExceptionErrorNotification.showErrorMessageNotification(exception);
					window.resetYesNoAccionFlags();
				}
			}
    		
    	});
	}
	
	/*public void printSaleInvoiceDTO(final SaleInvoiceDTO vSaleInvoiceDTO) throws PmsServiceException, PrintException, IOException{
		final WrappedHttpSession vWrappedHttpSession = (WrappedHttpSession)VaadinSession.getCurrent().getSession();
		final HttpSession vHttpSession = vWrappedHttpSession.getHttpSession();
		byte[] saleInvoiceDTOReport = this.cashMovementsManagementService.getSaleInvoiceDTOReport(vSaleInvoiceDTO,vHttpSession);
		this.sgpPrintService.printPdfFileByByteArray(
				saleInvoiceDTOReport, 
				this.messages.get("application.common.print.service.pdf.creator.printer.windows"));
		this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
	}*/

	public void printSaleInvoiceDTO(final SaleInvoiceDTO vSaleInvoiceDTO) throws PmsServiceException, PrintException, IOException{
		final WrappedHttpSession vWrappedHttpSession = (WrappedHttpSession)VaadinSession.getCurrent().getSession();
		final HttpSession vHttpSession = vWrappedHttpSession.getHttpSession();
		this.downloadData = this.cashMovementsManagementService.getSaleInvoiceDTOReport(vSaleInvoiceDTO,vHttpSession);
		this.downloadFileName = SgpUtils.formatDate_yyyyMMddHHmmss_ByLocale(
				new Date(), 
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()) + ".pdf";
		Page.getCurrent().getJavaScript().execute("document.getElementById('downloadButtonId" + "vSaleInvoiceDTO" + "').click();");
	}
	
	
	/*public void printCreditNoteDTO(final CreditNoteDTO vCreditNoteDTO) throws PmsServiceException, PrintException, IOException{
		final WrappedHttpSession vWrappedHttpSession = (WrappedHttpSession)VaadinSession.getCurrent().getSession();
		final HttpSession vHttpSession = vWrappedHttpSession.getHttpSession();
		byte[] vCreditNoteDTOReport = this.cashMovementsManagementService.getCreditNoteDTOReport(vCreditNoteDTO, this.saleInvoiceDTO, vHttpSession);
		this.sgpPrintService.printPdfFileByByteArray(
				vCreditNoteDTOReport, 
				this.messages.get("application.common.print.service.pdf.creator.printer.windows"));
		this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
	}*/
	
	public void printCreditNoteDTO(final CreditNoteDTO vCreditNoteDTO) throws PmsServiceException, PrintException, IOException{
		final WrappedHttpSession vWrappedHttpSession = (WrappedHttpSession)VaadinSession.getCurrent().getSession();
		final HttpSession vHttpSession = vWrappedHttpSession.getHttpSession();
		this.downloadData = this.cashMovementsManagementService.getCreditNoteDTOReport(vCreditNoteDTO, this.saleInvoiceDTO, vHttpSession);
		this.downloadFileName = SgpUtils.formatDate_yyyyMMddHHmmss_ByLocale(
				new Date(), 
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()) + ".pdf";
		Page.getCurrent().getJavaScript().execute("document.getElementById('downloadButtonId" + "vCreditNoteDTO" + vCreditNoteDTO.getId() + "').click();");
	}
	
	/*public void printCashReceiptDocumentDTO(final CashReceiptDocumentDTO vCashReceiptDocumentDTO) throws PmsServiceException, PrintException, IOException{
		final WrappedHttpSession vWrappedHttpSession = (WrappedHttpSession)VaadinSession.getCurrent().getSession();
		final HttpSession vHttpSession = vWrappedHttpSession.getHttpSession();
		byte[] vCashReceiptDocumentDTOReport = this.cashMovementsManagementService.getCashReceiptDocumentDTOReport(vCashReceiptDocumentDTO, vHttpSession);
		this.sgpPrintService.printPdfFileByByteArray(
				vCashReceiptDocumentDTOReport, 
				this.messages.get("application.common.print.service.pdf.creator.printer.windows"));
		this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();		
	}*/
	
	public void printCashReceiptDocumentDTO(final CashReceiptDocumentDTO vCashReceiptDocumentDTO) throws PmsServiceException, PrintException, IOException{
		final WrappedHttpSession vWrappedHttpSession = (WrappedHttpSession)VaadinSession.getCurrent().getSession();
		final HttpSession vHttpSession = vWrappedHttpSession.getHttpSession();
		this.downloadData = this.cashMovementsManagementService.getCashReceiptDocumentDTOReport(vCashReceiptDocumentDTO, vHttpSession);
		this.downloadFileName = SgpUtils.formatDate_yyyyMMddHHmmss_ByLocale(
				new Date(), 
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()) + ".pdf";
		logger.info( "\n ============================================= "
					+"\n download button id searched : downloadButtonIdvCashReceiptDocumentDTO" + vCashReceiptDocumentDTO.getId()
					+"\n ============================================= ");
		Page.getCurrent().getJavaScript().execute("document.getElementById('downloadButtonId" + "vCashReceiptDocumentDTO" + vCashReceiptDocumentDTO.getId() +  "').click();");
	}
	
	public void setUpDownloadButton(Button downloadButton) {
		// TODO Auto-generated method stub
		downloadButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	logger.info( "\n ======================================== "
		            				+"\n downloadButton clicked !"
		            				+"\n ======================================== ");
		            }
		        }
			);
		OnDemandStreamResource myResource = createOnDemandResource();
		OnDemandFileDownloader fileDownloader = new OnDemandFileDownloader(myResource);
		fileDownloader.extend(downloadButton);
	}
	

	private OnDemandStreamResource createOnDemandResource() {
		return new OnDemandStreamResource() {
			@Override
			public InputStream getStream() {
				// TODO Auto-generated method stub
				logger.info( "\n ======================================== "
							+"\n OnDemandStreamResource.getStream() method executed"
							+"\n ======================================== ");
				return new ByteArrayInputStream(downloadData);
			}
			@Override
			public String getFilename() {
				// TODO Auto-generated method stub
				return downloadFileName;
			}			
		};
	}
	
    private void goToRegisterOrderView(final OrderDTO vOrderDTO){
		try{
			
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.REGISTER_ORDER_FORM.getViewName());
			DashboardEventBus.post(new OrderRegisterFormViewEvent(
					vOrderDTO, 
					DashboardViewType.SALE_INVOICE_REGISTER_FORM.getViewName(),
					vOrderDTO!=null && vOrderDTO.getId()!=null,
					false));
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
    }
}
