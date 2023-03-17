package py.com.kyron.sgp.gui.view.stockmanagement.sale.invoice.product.deliverables.form.view.components;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteItemDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.service.CashMovementsManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SIItemPDMProductInstanceInvolvedDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SaleInvoiceItemProductDeliverablesDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SaleInvoiceProductDeliverablesDTO;
import py.com.kyron.sgp.bussines.stockmanagement.service.StockManagementService;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.ConfirmWindow;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.Decision;
import py.com.kyron.sgp.gui.view.utils.commponents.editquantitywindow.EditQuantityWindow;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SaleInvoiceProductDeliverablesDTOTabLayout extends VerticalLayout 
implements SaleInvoiceItemProductDeliverablesDTOTableLayoutFunctions{

	private final Logger logger = LoggerFactory.getLogger(SaleInvoiceProductDeliverablesDTOTabLayout.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private final SaleInvoiceProductDeliverablesDTOTabLayoutFunctions saleInvoiceProductDeliverablesDTOTabLayoutFunctions;
	private final SaleInvoiceProductDeliverablesDTO saleInvoiceProductDeliverablesDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private BussinesSessionUtils bussinesSessionUtils;
	private StockManagementService stockManagementService;
	private HorizontalLayout headerLayout;
	private VerticalLayout headerLayoutColumn01;
	private VerticalLayout headerLayoutColumn02;
	private SaleInvoiceItemProductDeliverablesDTOTableLayout saleInvoiceItemProductDeliverablesDTOTableLayout;
	private final boolean editFormMode;
	private final boolean queryFormMode;
	private EditQuantityWindow editQuantityWindow;
	private HorizontalLayout setUpOkCancelButtonsHorizontalLayout;
	private String vListSIItemPDMProductInstanceInvolvedDTO; 
	private SgpUtils sgpUtils;
	
	public SaleInvoiceProductDeliverablesDTOTabLayout(
			final SaleInvoiceProductDeliverablesDTOTabLayoutFunctions saleInvoiceProductDeliverablesDTOTabLayoutFunctions,
			final String VIEW_NAME,
			final SaleInvoiceProductDeliverablesDTO saleInvoiceProductDeliverablesDTO,
			final boolean editFormMode,
			final boolean queryFormMode,
			final boolean setUpLayoutContent) {
		// TODO Auto-generated constructor stub
		this.saleInvoiceProductDeliverablesDTOTabLayoutFunctions = saleInvoiceProductDeliverablesDTOTabLayoutFunctions;
		this.VIEW_NAME = VIEW_NAME;
		this.saleInvoiceProductDeliverablesDTO = saleInvoiceProductDeliverablesDTO;
		this.editFormMode = editFormMode;
		this.queryFormMode = queryFormMode;
		try{
			logger.info("\n SaleInvoiceProductDeliverablesDTOTabLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.sgpUtils = new SgpUtils();
			this.initServices();			
	        this.setSizeFull();
	        //this.addStyleName("v-scrollable");
	        //this.setHeight("100%");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        if(setUpLayoutContent)this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public SaleInvoiceProductDeliverablesDTOTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	private void initServices() throws Exception{
		this.stockManagementService = (StockManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.STOCK_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n SaleInvoiceProductDeliverablesDTOTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()

	private void setUpLayoutContent() throws PmsServiceException{
		this.initTitles();
		this.setUpHeaderLayout();
		if(this.saleInvoiceProductDeliverablesDTO.getListSaleInvoiceItemProductDeliverablesDTO() == null)this.retrieveListSaleInvoiceItemProductDeliverablesDTO();
		if(this.saleInvoiceProductDeliverablesDTO.getProduct_deposit_movement_identifier_number() == null)this.setProduct_deposit_movement_identifier_number();
		this.saleInvoiceItemProductDeliverablesDTOTableLayout = 
				new SaleInvoiceItemProductDeliverablesDTOTableLayout(
						this, 
						this.VIEW_NAME,
						this.saleInvoiceProductDeliverablesDTO.getListSaleInvoiceItemProductDeliverablesDTO());
		this.addComponent(this.saleInvoiceItemProductDeliverablesDTOTableLayout);
		this.setUpOkCancelButtonsHorizontalLayout = this.setUpOkCancelButtons();
		this.addComponent(this.setUpOkCancelButtonsHorizontalLayout);
	}
	
	private void setUpHeaderLayout(){
		this.headerLayoutColumn01 = new VerticalLayout();
		this.headerLayoutColumn01.setMargin(new MarginInfo(false, true, false, false));
		this.headerLayoutColumn02 = new VerticalLayout();
		this.headerLayoutColumn02.setMargin(new MarginInfo(false, true, false, false));
		this.headerLayout = new HorizontalLayout();
		this.headerLayout.setMargin(new MarginInfo(true, true, false, true));
		Responsive.makeResponsive(this.headerLayout);
		
		final Label invoiceNumberLabel = new Label(this.messages.get("application.common.sale.invoice.number.indicator.label"));
		invoiceNumberLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> invoice_identifier_numberProperty =	new ObjectProperty<String>(this.saleInvoiceProductDeliverablesDTO.getInvoice_identifier_number());
		final Label invoiceNumberIndicatorLabel = new Label(invoice_identifier_numberProperty);	
		final HorizontalLayout invoiceNumberIndicatorHorizontalLayout = new HorizontalLayout(invoiceNumberLabel,invoiceNumberIndicatorLabel);
		invoiceNumberIndicatorHorizontalLayout.setSpacing(true);
		this.headerLayoutColumn01.addComponent(invoiceNumberIndicatorHorizontalLayout);
		
		final Label invoiceStatusLabel = new Label(this.messages.get("application.common.status.label"));
		invoiceStatusLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final Label invoiceStatusValue = new Label(this.messages.get(this.saleInvoiceProductDeliverablesDTO.getInvoice_status()));
		final HorizontalLayout invoiceStatusHorizontalLayout = new HorizontalLayout(invoiceStatusLabel,invoiceStatusValue);
		invoiceStatusHorizontalLayout.setSpacing(true);
		this.headerLayoutColumn01.addComponent(invoiceStatusHorizontalLayout);
		
		/* ************** */
		final Label orderNumberIndicatorLabel = new Label(this.messages.get("application.common.order.number.indicator.label"));
		orderNumberIndicatorLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<Long> orderNumberProperty = new ObjectProperty<Long>(this.saleInvoiceProductDeliverablesDTO.getOrder_identifier_number());
		final Label orderNumberLabel = new Label(orderNumberProperty);
		final HorizontalLayout orderNumberHorizontalLayout = new HorizontalLayout(orderNumberIndicatorLabel,orderNumberLabel);
		orderNumberHorizontalLayout.setSpacing(true);
		this.headerLayoutColumn02.addComponent(orderNumberHorizontalLayout);
		
		final Label orderStatusLabel = new Label(this.messages.get("application.common.status.label"));
		orderStatusLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final Label orderStatusValue = new Label(this.messages.get(this.saleInvoiceProductDeliverablesDTO.getOrder_status()));
		final HorizontalLayout orderStatusHorizontalLayout = new HorizontalLayout(orderStatusLabel,orderStatusValue);
		orderStatusHorizontalLayout.setSpacing(true);
		this.headerLayoutColumn02.addComponent(orderStatusHorizontalLayout);
		
		this.headerLayout.addComponents(this.headerLayoutColumn01,this.headerLayoutColumn02);
		this.addComponent(this.headerLayout);
	}
	
	private void initTitles(){
		Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.product.deliverables.main.title"));
        title.addStyleName("h1");
        HorizontalLayout vHorizontalLayout = new HorizontalLayout(title);
        vHorizontalLayout.setMargin(new MarginInfo(true, false, false, true));
        this.addComponent(vHorizontalLayout);
	}

	@Override
	public void setRequestDeliverQuantity(
			SaleInvoiceItemProductDeliverablesDTO saleInvoiceItemProductDeliverablesDTO) {
		// TODO Auto-generated method stub
		this.editQuantityWindow = 
				new EditQuantityWindow(
						this.messages.get(this.VIEW_NAME + "tab.sale.invoice.product.deliverables.window.request.deliver.quantity.caption"), 
						saleInvoiceItemProductDeliverablesDTO.getProduct_id(), 
						saleInvoiceItemProductDeliverablesDTO.getInvoice_item_remain_exigible_quantity(),
						1L,
						saleInvoiceItemProductDeliverablesDTO.getInvoice_item_remain_exigible_quantity());
		this.editQuantityWindow.addCloseListener(this.setUpEditQuantityWindowCloseListener(saleInvoiceItemProductDeliverablesDTO));
		this.editQuantityWindow.adjuntWindowSizeAccordingToCientDisplay();
		saleInvoiceItemProductDeliverablesDTO.setProduct_deposit_movement_identifier_number(
				this.saleInvoiceProductDeliverablesDTO.getProduct_deposit_movement_identifier_number());
	}
	
	private CloseListener setUpEditQuantityWindowCloseListener(final SaleInvoiceItemProductDeliverablesDTO saleInvoiceItemProductDeliverablesDTO){
		return new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{					
					logger.info("\n******************************"
								+"\n the sale invoice item quantity editor window has been closed"
								+"\n******************************");
			    	//to recalculate table totals
					saleInvoiceItemProductDeliverablesDTO.setDeliver_quantity(editQuantityWindow.getQuantityEditor().getQuantity());
					logger.info( "\n =================================================="
								+"\n" + saleInvoiceItemProductDeliverablesDTO.toString()
								+"\n ==================================================");
					refreshLayout();
				}catch(Exception ex){
					logger.error("\n error",e);
				}
			}
		};
	}
	
	private void refreshLayout() throws PmsServiceException{
		this.removeAllComponents();
		this.setUpLayoutContent();
	}
	private void retrieveListSaleInvoiceItemProductDeliverablesDTO() throws PmsServiceException{
		this.saleInvoiceProductDeliverablesDTO.setListSaleInvoiceItemProductDeliverablesDTO(
				this.stockManagementService.listSaleInvoiceItemProductDeliverablesDTO(
						new SaleInvoiceItemProductDeliverablesDTO(
								this.saleInvoiceProductDeliverablesDTO.getId_sale_invoice()
																)
						)
				);
	}
	
    private HorizontalLayout setUpOkCancelButtons(){
    	final Button downloadButton = new Button();
    	downloadButton.setId("downloadButtonId" + "vSaleInvoiceOrderStatusReport");
    	downloadButton.setPrimaryStyleName(".dashboard .myCustomyInvisibleButtonCssRule");
    	this.saleInvoiceProductDeliverablesDTOTabLayoutFunctions.setUpDownloadButton(downloadButton);
		final Button printButton = new Button(this.messages.get("application.common.button.print.label"));/*"Cancelar"*/
		printButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		saleInvoiceProductDeliverablesDTOTabLayoutFunctions.print(saleInvoiceProductDeliverablesDTO);
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		
		final Button okButton = new Button(this.messages.get("application.common.button.save.label"));/*"Cancelar"*/
		okButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		saleInvoiceProductDeliverablesDTOTabLayoutFunctions.effectuateProductDeliverSaleInvoiceProductDeliverablesDTO(saleInvoiceProductDeliverablesDTO);
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		okButton.setEnabled(
				this.saleInvoiceProductDeliverablesDTO.getTransactionRealized() == null
				&& this.editFormMode);
		final Button cancelButton = new Button(this.messages.get("application.common.button.cancel.label"));/*"Cancelar"*/
		cancelButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		saleInvoiceProductDeliverablesDTOTabLayoutFunctions.navigateToCallerView();
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
		wrapperHorizontalLayout.addComponent(downloadButton);
		wrapperHorizontalLayout.addComponent(printButton);
		wrapperHorizontalLayout.addComponent(okButton);
		wrapperHorizontalLayout.addComponent(cancelButton);
		okCancelHorizontalLayout.addComponent(wrapperHorizontalLayout);
		return okCancelHorizontalLayout;
    }

	@Override
	public void queryListSIItemPDMProductInstanceInvolvedDTO(
			SaleInvoiceItemProductDeliverablesDTO saleInvoiceItemProductDeliverablesDTO) {
		// TODO Auto-generated method stub
		this.vListSIItemPDMProductInstanceInvolvedDTO = "";
		for(SIItemPDMProductInstanceInvolvedDTO vSIItemPDMProductInstanceInvolvedDTO : saleInvoiceItemProductDeliverablesDTO.getListSIItemPDMProductInstanceInvolvedDTO()){
			this.vListSIItemPDMProductInstanceInvolvedDTO+= 
					this.sgpUtils.formatLongValueNumberByLocale(
							vSIItemPDMProductInstanceInvolvedDTO.getProduct_instance_unique_number(),
							this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()) 
							+ " - ";
				
		}
    	final ConfirmWindow window = new ConfirmWindow(
    			this.messages.get(this.VIEW_NAME + "tab.sale.invoice.product.deliverables.table.sale.invoice.item.product.deliverables.column.operations.button.query.product.unique.instance.number.list.description"),
    			this.vListSIItemPDMProductInstanceInvolvedDTO,  
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
	
	private void setProduct_deposit_movement_identifier_number() throws PmsServiceException{
		this.saleInvoiceProductDeliverablesDTO.setProduct_deposit_movement_identifier_number(
				this.stockManagementService.pmsProductDepositMovementIdentifierNumberBySequence());
	}

	@Override
	public SaleInvoiceProductDeliverablesDTO getSaleInvoiceProductDeliverablesDTO() {
		// TODO Auto-generated method stub
		return this.saleInvoiceProductDeliverablesDTO;
	}

	@Override
	public boolean getEditFormMode() {
		// TODO Auto-generated method stub
		return this.editFormMode;
	}

	@Override
	public boolean getQueryFormMode() {
		// TODO Auto-generated method stub
		return this.queryFormMode;
	}
}
