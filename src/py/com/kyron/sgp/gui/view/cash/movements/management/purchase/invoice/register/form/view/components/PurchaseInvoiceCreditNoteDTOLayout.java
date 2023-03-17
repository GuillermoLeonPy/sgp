package py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceCreditNoteDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceCreditNoteItemDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.utils.commponents.editquantitywindow.EditBigDecimalQuantityWindow;

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
public class PurchaseInvoiceCreditNoteDTOLayout extends VerticalLayout implements PurchaseInvoiceCreditNoteItemDTOTableLayoutFunctions{

	private final Logger logger = LoggerFactory.getLogger(PurchaseInvoiceCreditNoteDTOLayout.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final PurchaseInvoiceCreditNoteDTO purchaseInvoiceCreditNoteDTO;
	private BussinesSessionUtils bussinesSessionUtils;
	private PurchaseInvoiceCreditNoteDTOHeaderLayout purchaseInvoiceCreditNoteDTOHeaderLayout;
	private PurchaseInvoiceCreditNoteItemDTOTableLayout purchaseInvoiceCreditNoteItemDTOTableLayout;
	private PurchaseInvoiceCreditNoteItemDTOTableTotalsLayout purchaseInvoiceCreditNoteItemDTOTableTotalsLayout;
	private final PurchaseInvoiceRegisterFormViewFunctions purchaseInvoiceRegisterFormViewFunctions;
	private final PurchaseInvoiceDTO purchaseInvoiceDTO;
	private EditBigDecimalQuantityWindow editBigDecimalQuantityWindow;
	private Button okButton;
	private final boolean editFormMode;
	
	public PurchaseInvoiceCreditNoteDTOLayout(
			final PurchaseInvoiceRegisterFormViewFunctions purchaseInvoiceRegisterFormViewFunctions,
			final String VIEW_NAME,
			final PurchaseInvoiceCreditNoteDTO purchaseInvoiceCreditNoteDTO,
			final PurchaseInvoiceDTO purchaseInvoiceDTO) {
		// TODO Auto-generated constructor stub
		this.purchaseInvoiceRegisterFormViewFunctions = purchaseInvoiceRegisterFormViewFunctions;
		this.VIEW_NAME = VIEW_NAME;
		this.purchaseInvoiceCreditNoteDTO = purchaseInvoiceCreditNoteDTO;
		this.purchaseInvoiceDTO = purchaseInvoiceDTO;
		this.editFormMode = this.purchaseInvoiceCreditNoteDTO!=null && this.purchaseInvoiceCreditNoteDTO.getId()!=null; 
		try{
			logger.info("\n PurchaseInvoiceCreditNoteDTOLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
	        this.setSizeFull();
	        //this.addStyleName("v-scrollable");
	        //this.setHeight("100%");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        if(this.purchaseInvoiceDTO!=null && this.purchaseInvoiceDTO.getId()!=null)this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public PurchaseInvoiceCreditNoteDTOLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/

	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n PurchaseInvoiceCreditNoteDTOLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//
	
	private void initServices() throws Exception{
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	
	private void setUpLayoutContent(){
		this.setUpHeaderLayout();
		this.setUpTableAndTotalsLayout();
		this.addComponent(this.purchaseInvoiceCreditNoteDTOHeaderLayout);
		this.addComponent(this.purchaseInvoiceCreditNoteItemDTOTableLayout);
		this.addComponent(this.purchaseInvoiceCreditNoteItemDTOTableTotalsLayout);
		this.addComponent(this.setUpOkCancelButtons());
	}
	
	private void setUpHeaderLayout(){
		this.purchaseInvoiceCreditNoteDTOHeaderLayout = new PurchaseInvoiceCreditNoteDTOHeaderLayout(
				this.VIEW_NAME, this.purchaseInvoiceCreditNoteDTO, this.purchaseInvoiceDTO);		
	}
	
	private void setUpTableAndTotalsLayout(){
		this.purchaseInvoiceCreditNoteItemDTOTableLayout = new PurchaseInvoiceCreditNoteItemDTOTableLayout(
				this, 
				this.VIEW_NAME, 
				this.purchaseInvoiceCreditNoteDTO.getListPurchaseInvoiceCreditNoteItemDTO()); 
		this.purchaseInvoiceCreditNoteItemDTOTableTotalsLayout = new PurchaseInvoiceCreditNoteItemDTOTableTotalsLayout(
				this.VIEW_NAME, this.purchaseInvoiceCreditNoteDTO.getListPurchaseInvoiceCreditNoteItemDTO());
	}
	
	private void reFreshLayout(){
		this.removeAllComponents();
		this.setUpTableAndTotalsLayout();
		this.addComponent(this.purchaseInvoiceCreditNoteDTOHeaderLayout);
		this.addComponent(this.purchaseInvoiceCreditNoteItemDTOTableLayout);
		this.addComponent(this.purchaseInvoiceCreditNoteItemDTOTableTotalsLayout);
		this.addComponent(this.setUpOkCancelButtons());
	}

	@Override
	public void deletePurchaseInvoiceCreditNoteItemDTOFromPreliminaryList(
			PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO) {
		// TODO Auto-generated method stub
		this.purchaseInvoiceCreditNoteDTO.getListPurchaseInvoiceCreditNoteItemDTO().remove(vPurchaseInvoiceCreditNoteItemDTO);
		this.reFreshLayout();
		this.refreshOkButtonStatus();
	}

	@Override
	public Long getPurchaseInvoiceCreditNoteDTOId() {
		// TODO Auto-generated method stub
		return this.purchaseInvoiceCreditNoteDTO.getId();
	}

	@Override
	public String getPurchaseInvoiceCreditNoteDTOStatus() {
		// TODO Auto-generated method stub
		return this.purchaseInvoiceCreditNoteDTO.getStatus();
	}

	@Override
	public void editQuantity(
			PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO) {
		// TODO Auto-generated method stub
		final double vdouble = 0.1;
		this.editBigDecimalQuantityWindow = 
				new EditBigDecimalQuantityWindow(						
						this.messages.get("application.common.table.column.operations.edit") + " " + this.messages.get("application.common.table.column.any.amount.label"),
						vPurchaseInvoiceCreditNoteItemDTO.getRawMaterialDTO().getRaw_material_id()
						+ " / " + vPurchaseInvoiceCreditNoteItemDTO.getMeasurmentUnitDTO().getMeasurment_unit_id() + " ; " 
						+ "Max " + this.messages.get("application.common.quantity.label") 
						+ " : " + vPurchaseInvoiceCreditNoteItemDTO.getPurchaseInvoiceItemDTO().getQuantity(), 
						null,
						BigDecimal.valueOf(vdouble),
						vPurchaseInvoiceCreditNoteItemDTO.getPurchaseInvoiceItemDTO().getQuantity());
		this.editBigDecimalQuantityWindow.addCloseListener(this.setUpEditQuantityWindowCloseListener(vPurchaseInvoiceCreditNoteItemDTO));
		this.editBigDecimalQuantityWindow.adjuntWindowSizeAccordingToCientDisplay();
		
	}
	
	private CloseListener setUpEditQuantityWindowCloseListener(final PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO){
		return new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{					
					logger.info("\n******************************"
								+"\n the purchase invoice credit note item quantity editor window has been closed"
								+"\n******************************");
					logger.info( "\n =================================================="
							+"\n" + vPurchaseInvoiceCreditNoteItemDTO.toString()
							+"\n ==================================================");
			    	//to recalculate table totals					
					vPurchaseInvoiceCreditNoteItemDTO.setQuantity(editBigDecimalQuantityWindow.getQuantityEditor().getQuantity());
					calculatePurchaseInvoiceCreditNoteItemDTOTotal(vPurchaseInvoiceCreditNoteItemDTO);
					reFreshLayout();					
				}catch(Exception ex){
					logger.error("\n error",e);
				}
			}
		};
	}
	
	
	private void calculatePurchaseInvoiceCreditNoteItemDTOTotal(final PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO){
		final BigDecimal unit_price_amount = vPurchaseInvoiceCreditNoteItemDTO.getUnit_price_amount();
		final BigDecimal quantity = 
				vPurchaseInvoiceCreditNoteItemDTO.getQuantity()!=null ? 
						vPurchaseInvoiceCreditNoteItemDTO.getQuantity():BigDecimal.ZERO;
		logger.info("\n =============================================== "
				+	"\n unit_price_amount : " + unit_price_amount				
				+	"\n quantity : " + quantity
				+	"\n unit_price_amount * quantity = " + unit_price_amount.multiply(quantity)
				+   "\n =============================================== ");
		vPurchaseInvoiceCreditNoteItemDTO.setValue_added_tax_10_amount(unit_price_amount.multiply(quantity));
		vPurchaseInvoiceCreditNoteItemDTO.setExempt_amount(BigDecimal.ZERO);
		vPurchaseInvoiceCreditNoteItemDTO.setValue_added_tax_5_amount(BigDecimal.ZERO);	
	}
	
    private HorizontalLayout setUpOkCancelButtons(){
		this.okButton = new Button(this.messages.get("application.common.button.save.label"));/*"Cancelar"*/
		this.okButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		purchaseInvoiceCreditNoteDTOHeaderLayout.commitFormsValues();
		            		purchaseInvoiceCreditNoteDTO.setValue_added_tax_10_amount(purchaseInvoiceCreditNoteItemDTOTableTotalsLayout.getTotalValueAdded_10_Tax());
		            		purchaseInvoiceCreditNoteDTO.setValue_added_tax_5_amount(purchaseInvoiceCreditNoteItemDTOTableTotalsLayout.getTotalValueAdded_5_Tax());
		            		purchaseInvoiceCreditNoteDTO.setExempt_amount(purchaseInvoiceCreditNoteItemDTOTableTotalsLayout.getExcemptTotal());		            		
		            		purchaseInvoiceRegisterFormViewFunctions.insertPurchaseInvoiceCreditNoteDTO(purchaseInvoiceCreditNoteDTO);
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		
		this.okButton.setEnabled(
				this.purchaseInvoiceCreditNoteDTO.getListPurchaseInvoiceCreditNoteItemDTO()!=null 
				&& !this.purchaseInvoiceCreditNoteDTO.getListPurchaseInvoiceCreditNoteItemDTO().isEmpty()
				&& (!this.editFormMode));
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
		            		purchaseInvoiceRegisterFormViewFunctions.cancelButtonPurchaseInvoiceCreditNoteDTOLayout(editFormMode);
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
    
    private void refreshOkButtonStatus(){
		this.okButton.setEnabled(
				this.purchaseInvoiceCreditNoteDTO.getListPurchaseInvoiceCreditNoteItemDTO()!=null 
				&& !this.purchaseInvoiceCreditNoteDTO.getListPurchaseInvoiceCreditNoteItemDTO().isEmpty()
				&& (!this.editFormMode));
    }
}
