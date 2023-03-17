package py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.register.form.view.components;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CashReceiptDocumentDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteItemDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoicePaymentDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.service.CashMovementsManagementService;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SIItemPDMProductInstanceInvolvedDTO;
import py.com.kyron.sgp.bussines.stockmanagement.service.StockManagementService;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.cash.movements.management.SaleInvoiceRegisterFormView;
import py.com.kyron.sgp.gui.view.utils.commponents.editquantitywindow.EditQuantityWindow;

import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

@SuppressWarnings("serial")
public class CreditNoteDTOTabLayout extends VerticalLayout implements CreditNoteItemDTOTableFunctions{

	private final Logger logger = LoggerFactory.getLogger(CreditNoteDTOTabLayout.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME = "sale.invoice.management.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private final SaleInvoiceRegisterFormView saleInvoiceRegisterFormView;
	private CreditNoteDTO creditNoteDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private CashMovementsManagementService cashMovementsManagementService;
	private BussinesSessionUtils bussinesSessionUtils;
	private CreditNoteHeaderLayout creditNoteHeaderLayout;
	private CreditNoteItemDTOTableLayout creditNoteItemDTOTableLayout;
	private CreditNoteItemDTOTableTotalsLayout creditNoteItemDTOTableTotalsLayout;
	private EditQuantityWindow editQuantityWindow;
	private final boolean editFormMode;
	private HorizontalLayout okCancelButtonsPanel;
	private Button okButton;
	private final OrderDTO orderDTO;
	private StockManagementService stockManagementService;
	private CreditNoteItemReturnableProductListWindow creditNoteItemReturnableProductListWindow;
	//private ComercialManagementService comercialManagementService;
	
	public CreditNoteDTOTabLayout(
			final SaleInvoiceRegisterFormView saleInvoiceRegisterFormView,
			final CreditNoteDTO creditNoteDTO,
			final OrderDTO orderDTO) {
		// TODO Auto-generated constructor stub
		this.saleInvoiceRegisterFormView = saleInvoiceRegisterFormView;
		this.creditNoteDTO = creditNoteDTO;
		this.orderDTO = orderDTO;
		this.editFormMode = creditNoteDTO.getId()!=null;
		try{
			logger.info("\n CreditNoteDTOTabLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();			
	        this.setSizeFull();
	        //this.addStyleName("v-scrollable");
	        //this.setHeight("100%");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public CreditNoteDTOTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/

	private void initServices() throws Exception{
		this.cashMovementsManagementService = (CashMovementsManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.CASH_MOVEMENTS_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		this.stockManagementService = (StockManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.STOCK_MANAGEMENT_SERVICE);
		//this.comercialManagementService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.STOCK_MANAGEMENT_SERVICE);
	}

	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n CreditNoteDTOTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void setUpLayoutContent(){
		this.removeAllComponents();

		if(this.creditNoteDTO!=null){
			this.creditNoteHeaderLayout = new CreditNoteHeaderLayout(this.VIEW_NAME, this.creditNoteDTO);
			this.addComponent(this.creditNoteHeaderLayout);
			this.creditNoteItemDTOTableLayout = new CreditNoteItemDTOTableLayout(this.VIEW_NAME,this,this.creditNoteDTO.getListCreditNoteItemDTO());
			this.addComponent(this.creditNoteItemDTOTableLayout);
			this.creditNoteItemDTOTableTotalsLayout = 
					new CreditNoteItemDTOTableTotalsLayout(
							this.VIEW_NAME,
							this.creditNoteDTO.getListCreditNoteItemDTO(), 
							this.orderDTO.getCredit_order_payment_condition_surcharge_percentage() != null ?
							this.orderDTO.getCredit_order_payment_condition_surcharge_percentage() :
							BigDecimal.ZERO);
			this.addComponent(this.creditNoteItemDTOTableTotalsLayout);
			this.okCancelButtonsPanel = this.setUpOkCancelButtons();
			this.addComponent(this.okCancelButtonsPanel);
		}
	}
	
    private HorizontalLayout setUpOkCancelButtons(){
		this.okButton = new Button(this.messages.get("application.common.button.save.label"));/*"Cancelar"*/
		this.okButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		creditNoteDTO.setValue_added_tax_10_amount(creditNoteItemDTOTableTotalsLayout.getTotalValueAdded_10_Tax());
		            		creditNoteDTO.setValue_added_tax_5_amount(creditNoteItemDTOTableTotalsLayout.getTotalValueAdded_5_Tax());
		            		creditNoteDTO.setExempt_amount(creditNoteItemDTOTableTotalsLayout.getExcemptTotal());		            		
		            		saleInvoiceRegisterFormView.insertCreditNoteDTO(creditNoteDTO);
		            		commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		
		this.okButton.setEnabled(
				this.creditNoteDTO.getListCreditNoteItemDTO()!=null 
				&& !this.creditNoteDTO.getListCreditNoteItemDTO().isEmpty()
				&& (!this.creditNoteDTO.getStatus().equals("application.common.status.annulled") || !editFormMode));
		/*okButton.setVisible(
				bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
				.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "edit.revision.status.sale.invoice")
				&& this.creditNoteDTO.getStatus().equals("application.common.status.revision"));*/
    	
		final Button cancelButton = new Button(this.messages.get("application.common.button.cancel.label"));/*"Cancelar"*/
		cancelButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		saleInvoiceRegisterFormView.cancelButtonClickCreditNoteDTOTabLayout(editFormMode);
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);

		final HorizontalLayout okCancelHorizontalLayout = new HorizontalLayout();
		okCancelHorizontalLayout.setMargin(true);
		okCancelHorizontalLayout.setSpacing(true);
		okCancelHorizontalLayout.setSizeFull();
		okCancelHorizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		final HorizontalLayout wrapperHorizontalLayout = new HorizontalLayout();
		wrapperHorizontalLayout.setSpacing(true);
		wrapperHorizontalLayout.addComponent(okButton);
		wrapperHorizontalLayout.addComponent(cancelButton);
		okCancelHorizontalLayout.addComponent(wrapperHorizontalLayout);
		return okCancelHorizontalLayout;
    }
	@Override
	public String getCreditNoteDTOStatus() {
		// TODO Auto-generated method stub
		return this.creditNoteDTO.getStatus();
	}

	@Override
	public void editCancellationWithdrawalQuantity(CreditNoteItemDTO vCreditNoteItemDTO) {
		// TODO Auto-generated method stub		
		this.editQuantityWindow = 
				new EditQuantityWindow(						
						this.messages.get(this.VIEW_NAME + "tab.credit.note.form.table.credit.note.item.column.operations.button.edit.cancellation.withdrawal.quantity.description"),
						vCreditNoteItemDTO.getProductDTO().getProduct_id() + "; " 
						+ this.messages.get("application.common.quantity.label") + ": " + vCreditNoteItemDTO.getSaleInvoiceItemDTO().getQuantity(), 
						null,
						1L,
						vCreditNoteItemDTO.getSaleInvoiceItemDTO().getQuantity());
		this.editQuantityWindow.addCloseListener(this.setUpEditQuantityWindowCloseListener(vCreditNoteItemDTO));
		this.editQuantityWindow.adjuntWindowSizeAccordingToCientDisplay();
	}

	@Override
	public void reBuildTableAndTotalsPanel() {
		// TODO Auto-generated method stub
		this.setUpLayoutContent();
	}
	
	private CloseListener setUpEditQuantityWindowCloseListener(final CreditNoteItemDTO vCreditNoteItemDTO){
		return new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{					
					logger.info("\n******************************"
								+"\n the sale invoice item quantity editor window has been closed"
								+"\n******************************");
					logger.info( "\n =================================================="
							+"\n" + vCreditNoteItemDTO.toString()
							+"\n ==================================================");
			    	//to recalculate table totals
					vCreditNoteItemDTO.setCancellation_withdrawal_quantity(editQuantityWindow.getQuantityEditor().getQuantity());
					calculateCreditNoteItemDTOTotal(vCreditNoteItemDTO);
					setUpLayoutContent();					
				}catch(Exception ex){
					logger.error("\n error",e);
				}
			}
		};
	}

	@Override
	public void deleteCreditNoteItemDTOFromPreliminaryList(
			CreditNoteItemDTO vCreditNoteItemDTO) {
		// TODO Auto-generated method stub
		this.creditNoteDTO.getListCreditNoteItemDTO().remove(vCreditNoteItemDTO);
		this.reSetLayoutAfterAnItemHasBeenAdded();
		this.refreshOkButtonStatus();
	}
	
    public void reSetLayoutAfterAnItemHasBeenAdded(){
		this.removeComponent(this.creditNoteItemDTOTableLayout);
		this.removeComponent(this.creditNoteItemDTOTableTotalsLayout);
		this.removeComponent(this.okCancelButtonsPanel);
		
		
		this.creditNoteItemDTOTableLayout = new CreditNoteItemDTOTableLayout(this.VIEW_NAME,this,this.creditNoteDTO.getListCreditNoteItemDTO());
		this.creditNoteItemDTOTableTotalsLayout = 
				new CreditNoteItemDTOTableTotalsLayout(
						this.VIEW_NAME,
						this.creditNoteDTO.getListCreditNoteItemDTO(), 
						BigDecimal.ZERO);
		//estimated finalize date panel date
		this.okCancelButtonsPanel = this.setUpOkCancelButtons();
		
		this.addComponent(this.creditNoteItemDTOTableLayout);
		this.addComponent(this.creditNoteItemDTOTableTotalsLayout);
		this.addComponent(this.okCancelButtonsPanel);   	
    }
    
    private void refreshOkButtonStatus(){
		this.okButton.setEnabled(
				this.creditNoteDTO.getListCreditNoteItemDTO()!=null 
				&& !this.creditNoteDTO.getListCreditNoteItemDTO().isEmpty()
				&& (!this.creditNoteDTO.getStatus().equals("application.common.status.annulled") || !editFormMode));
    }

	@Override
	public Long getCreditNoteDTOId() {
		// TODO Auto-generated method stub
		return this.creditNoteDTO.getId();
	}

	@Override
	public void showDeliveredUnitsListToSelectDevolutionUnits(
			CreditNoteItemDTO vCreditNoteItemDTO, boolean query) throws PmsServiceException {
		// TODO Auto-generated method stub
		if(!query){
			vCreditNoteItemDTO.setId_sale_invoice(this.creditNoteDTO.getId_sale_invoice());
			vCreditNoteItemDTO.setListProductInstances(this.stockManagementService.listProductInstancesReturnableByCreditNoteItemDTO(vCreditNoteItemDTO));
		}
		if(vCreditNoteItemDTO.getListProductInstances() == null || vCreditNoteItemDTO.getListProductInstances().isEmpty())
			throw new PmsServiceException("py.com.kyron.sgp.persistence.common.search.no.results.found.error", null, null);
		this.creditNoteItemReturnableProductListWindow = 
				new CreditNoteItemReturnableProductListWindow(
						vCreditNoteItemDTO.getListProductInstances(), 
						this.VIEW_NAME, 
						query);
		this.creditNoteItemReturnableProductListWindow.adjuntWindowSizeAccordingToCientDisplay();
		this.creditNoteItemReturnableProductListWindow.addCloseListener(this.setUpCreditNoteItemReturnableProductListWindowCloseListener(vCreditNoteItemDTO));
		
	}
	
	private CloseListener setUpCreditNoteItemReturnableProductListWindowCloseListener(final CreditNoteItemDTO vCreditNoteItemDTO){
		return new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{						
					if(!creditNoteItemReturnableProductListWindow.isQueryWindowMode()
					&& creditNoteItemReturnableProductListWindow.isCancellButtoActionflag()){
						for(SIItemPDMProductInstanceInvolvedDTO vSIItemPDMProductInstanceInvolvedDTO : 
							vCreditNoteItemDTO.getListProductInstances()){
							vSIItemPDMProductInstanceInvolvedDTO.setReturnUnit(false);
						}
						vCreditNoteItemDTO.setDevolution_quantity(null);						
					}else if(!creditNoteItemReturnableProductListWindow.isQueryWindowMode()
					&& !creditNoteItemReturnableProductListWindow.isCancellButtoActionflag()){
						long counter = 0;
						for(SIItemPDMProductInstanceInvolvedDTO vSIItemPDMProductInstanceInvolvedDTO : 
							vCreditNoteItemDTO.getListProductInstances()){
							if(vSIItemPDMProductInstanceInvolvedDTO.getReturnUnit()!=null && vSIItemPDMProductInstanceInvolvedDTO.getReturnUnit())
								counter++;
						}
						if(counter > 0)vCreditNoteItemDTO.setDevolution_quantity(counter);
						else vCreditNoteItemDTO.setDevolution_quantity(null);					
					}
					
					calculateCreditNoteItemDTOTotal(vCreditNoteItemDTO);
					
					setUpLayoutContent();
				}catch(Exception ex){
					commonExceptionErrorNotification.showErrorMessageNotification(ex);
				}
			}			
		};
	}
	
	private void calculateCreditNoteItemDTOTotal(final CreditNoteItemDTO vCreditNoteItemDTO){
		final BigDecimal unit_price_amount = vCreditNoteItemDTO.getSaleInvoiceItemDTO().getUnit_price_amount();
		final BigDecimal cancellation_withdrawal_quantity = 
				vCreditNoteItemDTO.getCancellation_withdrawal_quantity()!=null ? 
						BigDecimal.valueOf(vCreditNoteItemDTO.getCancellation_withdrawal_quantity()):BigDecimal.ZERO;
		final BigDecimal devolution_quantity = vCreditNoteItemDTO.getDevolution_quantity()!=null ?	BigDecimal.valueOf(vCreditNoteItemDTO.getDevolution_quantity()) : BigDecimal.ZERO;
		logger.info("\n =============================================== "
				+	"\n unit_price_amount : " + unit_price_amount
				+	"\n cancellation_withdrawal_quantity : " + cancellation_withdrawal_quantity
				+	"\n devolution_quantity : " + devolution_quantity
				+	"\n unit_price_amount * (cancellation_withdrawal_quantity + devolution_quantity) = " + unit_price_amount.multiply((cancellation_withdrawal_quantity.add(devolution_quantity)))
				+   "\n =============================================== ");
		vCreditNoteItemDTO.setValue_added_tax_10_unit_price_amount(unit_price_amount.multiply((cancellation_withdrawal_quantity.add(devolution_quantity))));
		vCreditNoteItemDTO.setExempt_unit_price_amount(BigDecimal.ZERO);
		vCreditNoteItemDTO.setValue_added_tax_5_unit_price_amount(BigDecimal.ZERO);	
	}
		
}
