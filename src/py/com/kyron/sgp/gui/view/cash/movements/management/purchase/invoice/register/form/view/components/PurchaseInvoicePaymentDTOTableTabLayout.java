package py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoicePaymentDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.service.CashMovementsManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.utils.TableNumericColumnCellLabelHelper;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class PurchaseInvoicePaymentDTOTableTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(PurchaseInvoicePaymentDTOTableTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private final PurchaseInvoiceRegisterFormViewFunctions purchaseInvoiceRegisterFormViewFunctions;
	private final PurchaseInvoiceDTO purchaseInvoiceDTO;
	//private List<PurchaseInvoicePaymentDTO> listPurchaseInvoicePaymentDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Table listPurchaseInvoicePaymentDTOTable;
    private static final String[] DEFAULT_COLLAPSIBLE = 
    	{"g_overdue_amount",
    	"g_purchase_invoice_balance",
    	"g_registration_date",
    	"g_payment_due_date",
    	"g_cancellation_date"};
    
    private BussinesSessionUtils bussinesSessionUtils;
    private CashMovementsManagementService cashMovementsManagementService;
	
	public PurchaseInvoicePaymentDTOTableTabLayout(
			final PurchaseInvoiceRegisterFormViewFunctions purchaseInvoiceRegisterFormViewFunctions,
			final String VIEW_NAME,
			final PurchaseInvoiceDTO purchaseInvoiceDTO) {
		// TODO Auto-generated constructor stub
		this.purchaseInvoiceRegisterFormViewFunctions = purchaseInvoiceRegisterFormViewFunctions;
		this.VIEW_NAME = VIEW_NAME;
		this.purchaseInvoiceDTO = purchaseInvoiceDTO;
		try{
			logger.info("\n PurchaseInvoicePaymentDTOTableTabLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        if(this.purchaseInvoiceDTO!=null)this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public PurchaseInvoicePaymentDTOTableTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/

	private void initServices() throws Exception{
		this.cashMovementsManagementService = (CashMovementsManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.CASH_MOVEMENTS_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n PurchaseInvoicePaymentDTOTableTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void setUpLayoutContent() throws PmsServiceException{
		this.buildlistPurchaseInvoicePaymentDTOTable();
		addComponent(this.buildToolbar());
        addComponent(listPurchaseInvoicePaymentDTOTable);
        setExpandRatio(listPurchaseInvoicePaymentDTOTable, 1);
	}
	
	
	private void buildlistPurchaseInvoicePaymentDTOTable() throws PmsServiceException{
    	this.listPurchaseInvoicePaymentDTOTable = new Table();
    	BeanItemContainer<PurchaseInvoicePaymentDTO> PurchaseInvoicePaymentDTOBeanItemContainer	= new BeanItemContainer<PurchaseInvoicePaymentDTO>(PurchaseInvoicePaymentDTO.class);
    	if(this.purchaseInvoiceDTO.getListPurchaseInvoicePaymentDTO()!=null && !this.purchaseInvoiceDTO.getListPurchaseInvoicePaymentDTO().isEmpty())
    		PurchaseInvoicePaymentDTOBeanItemContainer.addAll(this.purchaseInvoiceDTO.getListPurchaseInvoicePaymentDTO());
    	else
    		PurchaseInvoicePaymentDTOBeanItemContainer.addAll(new ArrayList<PurchaseInvoicePaymentDTO>());
    	this.listPurchaseInvoicePaymentDTOTable.setContainerDataSource(PurchaseInvoicePaymentDTOBeanItemContainer); 
    	
    	this.listPurchaseInvoicePaymentDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoicePaymentDTO vPurchaseInvoicePaymentDTO = (PurchaseInvoicePaymentDTO)itemId;
				return buildOperationsButtonPanel(vPurchaseInvoicePaymentDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listPurchaseInvoicePaymentDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listPurchaseInvoicePaymentDTOTable.addGeneratedColumn("g_amount", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoicePaymentDTO vPurchaseInvoicePaymentDTO = (PurchaseInvoicePaymentDTO)itemId;
				final Label currencyLabel = new Label(vPurchaseInvoicePaymentDTO.getCurrencyDTO().getId_code());
    			currencyLabel.addStyleName("colored");
    			final Label amountLabel = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vPurchaseInvoicePaymentDTO.getAmount());
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(currencyLabel,amountLabel);
				vHorizontalLayout.setComponentAlignment(currencyLabel, Alignment.MIDDLE_LEFT);
				vHorizontalLayout.setComponentAlignment(amountLabel, Alignment.MIDDLE_RIGHT);				
				vHorizontalLayout.setSizeFull();
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {    	
    	this.listPurchaseInvoicePaymentDTOTable.addGeneratedColumn("g_overdue_amount", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoicePaymentDTO vPurchaseInvoicePaymentDTO = (PurchaseInvoicePaymentDTO)itemId;
				if(vPurchaseInvoicePaymentDTO.getOverdue_amount() != null){
						final Label currencyLabel = new Label(vPurchaseInvoicePaymentDTO.getCurrencyDTO().getId_code());
		    			currencyLabel.addStyleName("colored");
		    			final Label amountLabel = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vPurchaseInvoicePaymentDTO.getOverdue_amount());
						final HorizontalLayout vHorizontalLayout = new HorizontalLayout(currencyLabel,amountLabel);
						vHorizontalLayout.setComponentAlignment(currencyLabel, Alignment.MIDDLE_LEFT);
						vHorizontalLayout.setComponentAlignment(amountLabel, Alignment.MIDDLE_RIGHT);				
						vHorizontalLayout.setSizeFull();				
						return vHorizontalLayout;
				}else{
					final Label currencyLabel = new Label(messages.get("application.common.do.not.apply.label"));
					currencyLabel.addStyleName("colored");
					return currencyLabel;
				}
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() { 
    	this.listPurchaseInvoicePaymentDTOTable.addGeneratedColumn("g_purchase_invoice_balance", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoicePaymentDTO vPurchaseInvoicePaymentDTO = (PurchaseInvoicePaymentDTO)itemId;
				final Label currencyLabel = new Label(vPurchaseInvoicePaymentDTO.getCurrencyDTO().getId_code());
    			currencyLabel.addStyleName("colored");
    			final Label amountLabel = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vPurchaseInvoicePaymentDTO.getPurchase_invoice_balance());
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(currencyLabel,amountLabel);
				vHorizontalLayout.setComponentAlignment(currencyLabel, Alignment.MIDDLE_LEFT);
				vHorizontalLayout.setComponentAlignment(amountLabel, Alignment.MIDDLE_RIGHT);				
				vHorizontalLayout.setSizeFull();
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listPurchaseInvoicePaymentDTOTable.addGeneratedColumn("g_status", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoicePaymentDTO vPurchaseInvoicePaymentDTO = (PurchaseInvoicePaymentDTO)itemId;	
				final Label statusLabel = new Label(messages.get(vPurchaseInvoicePaymentDTO.getStatus()));
				statusLabel.addStyleName("colored");
				return statusLabel;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//orderDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listPurchaseInvoicePaymentDTOTable.addGeneratedColumn("g_registration_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoicePaymentDTO vPurchaseInvoicePaymentDTO = (PurchaseInvoicePaymentDTO)itemId;
				return SgpUtils.dateFormater.format(vPurchaseInvoicePaymentDTO.getRegistration_date());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listPurchaseInvoicePaymentDTOTable.addGeneratedColumn("g_payment_due_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoicePaymentDTO vPurchaseInvoicePaymentDTO = (PurchaseInvoicePaymentDTO)itemId;
				if(!vPurchaseInvoicePaymentDTO.getStatus().equals("application.common.status.annulled") 
						&& !vPurchaseInvoicePaymentDTO.getStatus().equals("application.common.status.canceled")
						&& vPurchaseInvoicePaymentDTO.getPayment_due_date().before(new Date())){
					final Label dueDateLabel = new Label(SgpUtils.dateFormater.format(vPurchaseInvoicePaymentDTO.getPayment_due_date()));
					dueDateLabel.addStyleName("failure");
					return dueDateLabel;
				}else
					return SgpUtils.dateFormater.format(vPurchaseInvoicePaymentDTO.getPayment_due_date());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listPurchaseInvoicePaymentDTOTable.addGeneratedColumn("g_cancellation_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoicePaymentDTO vPurchaseInvoicePaymentDTO = (PurchaseInvoicePaymentDTO)itemId;
				if(vPurchaseInvoicePaymentDTO.getCancellation_date()!=null)
					return SgpUtils.dateFormater.format(vPurchaseInvoicePaymentDTO.getCancellation_date());
				else
					return null;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {

    	this.listPurchaseInvoicePaymentDTOTable.setVisibleColumns(new Object[] 
    			{"operations",
    			"payment_number",
    			"g_amount",
    			"g_overdue_amount",
    			"g_purchase_invoice_balance",
    			"g_status",
    			"g_registration_date"
    			,"g_payment_due_date",
    			"g_cancellation_date"});
    	this.listPurchaseInvoicePaymentDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.listPurchaseInvoicePaymentDTOTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.listPurchaseInvoicePaymentDTOTable.setColumnHeader("payment_number", this.messages.get("application.common.number.indicator.label"));
    	this.listPurchaseInvoicePaymentDTOTable.setColumnAlignment("payment_number", Table.Align.RIGHT);

    	this.listPurchaseInvoicePaymentDTOTable.setColumnHeader("g_amount", this.messages.get("application.common.amount.label"));
    	this.listPurchaseInvoicePaymentDTOTable.setColumnAlignment("g_amount", Table.Align.RIGHT);

    	this.listPurchaseInvoicePaymentDTOTable.setColumnHeader("g_overdue_amount", this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.payments.table.purchase.invoice.payments.column.overdue.amount"));
    	this.listPurchaseInvoicePaymentDTOTable.setColumnAlignment("g_overdue_amount", Table.Align.RIGHT);
    	
    	this.listPurchaseInvoicePaymentDTOTable.setColumnHeader("g_purchase_invoice_balance", this.messages.get("application.common.balance.label"));
    	this.listPurchaseInvoicePaymentDTOTable.setColumnAlignment("g_purchase_invoice_balance", Table.Align.RIGHT);
  	
    	this.listPurchaseInvoicePaymentDTOTable.setColumnHeader("g_status", this.messages.get("application.common.status.label"));
    	this.listPurchaseInvoicePaymentDTOTable.setColumnAlignment("g_status", Table.Align.LEFT);

    	this.listPurchaseInvoicePaymentDTOTable.setColumnHeader("g_registration_date", this.messages.get("application.common.table.column.registration.date.label"));
    	this.listPurchaseInvoicePaymentDTOTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);

    	this.listPurchaseInvoicePaymentDTOTable.setColumnHeader("g_payment_due_date", this.messages.get("application.common.table.column.validity.end.date.label"));
    	this.listPurchaseInvoicePaymentDTOTable.setColumnAlignment("g_payment_due_date", Table.Align.LEFT);
    	
    	this.listPurchaseInvoicePaymentDTOTable.setColumnHeader("g_cancellation_date", this.messages.get("application.common.cancelation.date.label"));
    	this.listPurchaseInvoicePaymentDTOTable.setColumnAlignment("g_cancellation_date", Table.Align.LEFT);
    	this.listPurchaseInvoicePaymentDTOTable.setSizeFull();
    	this.listPurchaseInvoicePaymentDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.listPurchaseInvoicePaymentDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.listPurchaseInvoicePaymentDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.listPurchaseInvoicePaymentDTOTable.setColumnExpandRatio("operations", 0.0032f); 
    	this.listPurchaseInvoicePaymentDTOTable.setColumnExpandRatio("payment_number", 0.0016f);
    	this.listPurchaseInvoicePaymentDTOTable.setColumnExpandRatio("g_amount", 0.0051f);
    	this.listPurchaseInvoicePaymentDTOTable.setColumnExpandRatio("g_overdue_amount", 0.0051f);
    	this.listPurchaseInvoicePaymentDTOTable.setColumnExpandRatio("g_purchase_invoice_balance", 0.0051f);
    	this.listPurchaseInvoicePaymentDTOTable.setColumnExpandRatio("g_status", 0.0035f);
    	this.listPurchaseInvoicePaymentDTOTable.setColumnExpandRatio("g_registration_date", 0.0056f);
    	this.listPurchaseInvoicePaymentDTOTable.setColumnExpandRatio("g_payment_due_date", 0.0055f);
    	this.listPurchaseInvoicePaymentDTOTable.setColumnExpandRatio("g_cancellation_date", 0.0054f);
    	this.listPurchaseInvoicePaymentDTOTable.setSelectable(true);
    	this.listPurchaseInvoicePaymentDTOTable.setColumnCollapsingAllowed(true);
    	this.listPurchaseInvoicePaymentDTOTable.setColumnCollapsible("operations", false);
    	this.listPurchaseInvoicePaymentDTOTable.setColumnCollapsible("g_amount", false);
    	this.listPurchaseInvoicePaymentDTOTable.setColumnCollapsible("g_status", false);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.listPurchaseInvoicePaymentDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.listPurchaseInvoicePaymentDTOTable.setSortAscending(false);
    	this.listPurchaseInvoicePaymentDTOTable.setColumnReorderingAllowed(true);
    	this.listPurchaseInvoicePaymentDTOTable.setFooterVisible(true);
    	this.listPurchaseInvoicePaymentDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
	private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        //header.addStyleName("viewheader");
        header.setMargin(new MarginInfo(true, true, true, true));
        header.setSizeFull();
        //header.setSpacing(true);
        Responsive.makeResponsive(header);
        logger.info("\n*********************remember: an HorizontalLayout is set with the instrucction: Responsive.makeResponsive(header); !!! "
        		+"\n*********************");
        Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.payments.label.purchase.invoice.number"));
        title.addStyleName(ValoTheme.LABEL_H2);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        Label saleInvoiceNumber = new Label(this.purchaseInvoiceDTO.getIdentifier_number());
        saleInvoiceNumber.addStyleName(ValoTheme.LABEL_H2);
        saleInvoiceNumber.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        saleInvoiceNumber.addStyleName("colored");
        HorizontalLayout vHorizontalLayout = new HorizontalLayout(title,saleInvoiceNumber);
        vHorizontalLayout.setSpacing(true);
        header.addComponent(vHorizontalLayout);
        header.setComponentAlignment(vHorizontalLayout, Alignment.MIDDLE_LEFT);
        return header;
	}//private Component buildToolbar()
	
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    // Some columns are collapsed when browser window width gets small
	    // enough to make the table fit better.
	    if (defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.listPurchaseInvoicePaymentDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.listPurchaseInvoicePaymentDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()

	private HorizontalLayout buildOperationsButtonPanel(final PurchaseInvoicePaymentDTO vPurchaseInvoicePaymentDTO){	
		final Button effectuatePaymentButton = new Button();
		effectuatePaymentButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.payments.table.purchase.invoice.payments.column.operations.button.cash.receipt.document.form.description"));
		effectuatePaymentButton.setIcon(FontAwesome.PAYPAL);
		effectuatePaymentButton.addStyleName("borderless");
		effectuatePaymentButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar vPurchaseInvoicePaymentDTO...\n" + vPurchaseInvoicePaymentDTO.toString());
                try{		                	
                	//purchaseInvoiceRegisterFormViewFunctions.initAndShowCashReceiptDocumentDTOWindow(vPurchaseInvoicePaymentDTO);
                	purchaseInvoiceRegisterFormViewFunctions.preparePurchaseInvoicePaymentDTOPaymentProcedure(vPurchaseInvoicePaymentDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		effectuatePaymentButton.setEnabled(vPurchaseInvoicePaymentDTO.getStatus().equals("application.common.status.in.progress"));

		final Button viewCashReceiptDocumentButton = new Button();
		viewCashReceiptDocumentButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.payments.table.purchase.invoice.payments.column.operations.button.query.cash.receipt.document.description"));
		viewCashReceiptDocumentButton.setIcon(FontAwesome.PAPERCLIP);
		viewCashReceiptDocumentButton.addStyleName("borderless");
		viewCashReceiptDocumentButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar vPurchaseInvoicePaymentDTO...\n" + vPurchaseInvoicePaymentDTO.toString());
                try{		                	
                	purchaseInvoiceRegisterFormViewFunctions.queryCashReceiptDocumentDTOWindow(vPurchaseInvoicePaymentDTO);
                	//asdf
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		viewCashReceiptDocumentButton.setVisible(vPurchaseInvoicePaymentDTO.getCashReceiptDocumentDTO() != null);
		
		
		final Button dueDateReachedButton = new Button();
		dueDateReachedButton.setDescription(this.messages.get("application.common.due.date.reached.label"));
		dueDateReachedButton.setIcon(FontAwesome.WARNING);
		dueDateReachedButton.addStyleName("borderless");
		dueDateReachedButton.setResponsive(false);
		dueDateReachedButton.setVisible(
				!vPurchaseInvoicePaymentDTO.getStatus().equals("application.common.status.annulled") 
				&& !vPurchaseInvoicePaymentDTO.getStatus().equals("application.common.status.canceled")
				&& vPurchaseInvoicePaymentDTO.getPayment_due_date().before(new Date())
				||
				(vPurchaseInvoicePaymentDTO.getStatus().equals("application.common.status.canceled")
						&& vPurchaseInvoicePaymentDTO.getPayment_due_date().before(vPurchaseInvoicePaymentDTO.getCancellation_date())));
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(effectuatePaymentButton,viewCashReceiptDocumentButton,dueDateReachedButton);
		//operationsButtonPanel.addStyleName("wrapping");
		return operationsButtonPanel;
	}
}
