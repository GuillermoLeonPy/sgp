package py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceCreditNoteDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceDTO;
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
public class PurchaseInvoiceCreditNoteDTOTableLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(PurchaseInvoiceCreditNoteDTOTableLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private final PurchaseInvoiceRegisterFormViewFunctions purchaseInvoiceRegisterFormViewFunctions;
	private final PurchaseInvoiceDTO purchaseInvoiceDTO;
	private List<PurchaseInvoiceCreditNoteDTO> listPurchaseInvoiceCreditNoteDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Table listPurchaseInvoiceCreditNoteDTOTable;
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_registration_date","g_amount","g_credit_note_balance","bussines_name"};
    private BussinesSessionUtils bussinesSessionUtils;
    private CashMovementsManagementService cashMovementsManagementService;
	
	public PurchaseInvoiceCreditNoteDTOTableLayout(
			final PurchaseInvoiceRegisterFormViewFunctions purchaseInvoiceRegisterFormViewFunctions,
			final String VIEW_NAME,
			final PurchaseInvoiceDTO purchaseInvoiceDTO) {
		// TODO Auto-generated constructor stub
		this.purchaseInvoiceRegisterFormViewFunctions = purchaseInvoiceRegisterFormViewFunctions;
		this.VIEW_NAME = VIEW_NAME;
		this.purchaseInvoiceDTO = purchaseInvoiceDTO;
		try{
			logger.info("\n CreditNoteDTOTableTabLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        if(this.purchaseInvoiceDTO!=null && this.purchaseInvoiceDTO.getId()!=null)this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public PurchaseInvoiceCreditNoteDTOTableLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	private void initServices() throws Exception{
		this.cashMovementsManagementService = (CashMovementsManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.CASH_MOVEMENTS_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	private void setUpLayoutContent() throws PmsServiceException{
		this.buildPurchaseInvoiceCreditNoteDTOTable();
		addComponent(this.buildToolbar());
        addComponent(listPurchaseInvoiceCreditNoteDTOTable);
        setExpandRatio(listPurchaseInvoiceCreditNoteDTOTable, 1);
	}
	
	private void buildPurchaseInvoiceCreditNoteDTOTable() throws PmsServiceException{
    	this.listPurchaseInvoiceCreditNoteDTOTable = new Table();
    	BeanItemContainer<PurchaseInvoiceCreditNoteDTO> PurchaseInvoiceCreditNoteDTOBeanItemContainer	= new BeanItemContainer<PurchaseInvoiceCreditNoteDTO>(PurchaseInvoiceCreditNoteDTO.class);
    	this.updatelistPurchaseInvoiceCreditNoteDTO();
    	PurchaseInvoiceCreditNoteDTOBeanItemContainer.addAll(this.listPurchaseInvoiceCreditNoteDTO);    	
    	this.listPurchaseInvoiceCreditNoteDTOTable.setContainerDataSource(PurchaseInvoiceCreditNoteDTOBeanItemContainer); 
    	
       	this.listPurchaseInvoiceCreditNoteDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
    			@Override
    			public Object generateCell(Table source, Object itemId, Object columnId) {
    				// TODO Auto-generated method stub
    				final PurchaseInvoiceCreditNoteDTO vPurchaseInvoiceCreditNoteDTO = (PurchaseInvoiceCreditNoteDTO)itemId;
    				return buildOperationsButtonPanel(vPurchaseInvoiceCreditNoteDTO);
    			}//public Object generateCell(Table source, Object itemId, Object columnId)
    		});//creditNoteDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
        	this.listPurchaseInvoiceCreditNoteDTOTable.addGeneratedColumn("g_identifier_number", new Table.ColumnGenerator() {				
    			@Override
    			public Object generateCell(Table source, Object itemId, Object columnId) {
    				// TODO Auto-generated method stub
    				final PurchaseInvoiceCreditNoteDTO vPurchaseInvoiceCreditNoteDTO = (PurchaseInvoiceCreditNoteDTO)itemId;
    				final Label identifierNumberLabel = new Label(vPurchaseInvoiceCreditNoteDTO.getIdentifier_number());
    				identifierNumberLabel.addStyleName("colored");
    				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(identifierNumberLabel);
    				vHorizontalLayout.setSizeFull();
    				vHorizontalLayout.setComponentAlignment(identifierNumberLabel, Alignment.MIDDLE_RIGHT);
    				return vHorizontalLayout;
    			}//public Object generateCell(Table source, Object itemId, Object columnId)
    		});//creditNoteDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {

        	this.listPurchaseInvoiceCreditNoteDTOTable.addGeneratedColumn("g_amount", new Table.ColumnGenerator() {				
    			@Override
    			public Object generateCell(Table source, Object itemId, Object columnId) {
    				// TODO Auto-generated method stub
    				final PurchaseInvoiceCreditNoteDTO vPurchaseInvoiceCreditNoteDTO = (PurchaseInvoiceCreditNoteDTO)itemId;
    				final Label currencyLabel = new Label(vPurchaseInvoiceCreditNoteDTO.getCurrencyDTO().getId_code());
        			currencyLabel.addStyleName("colored");
        			final Label amountLabel = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vPurchaseInvoiceCreditNoteDTO.getTotal_amount());
    				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(currencyLabel,amountLabel);
    				vHorizontalLayout.setSpacing(true);
    				return vHorizontalLayout;
    			}//public Object generateCell(Table source, Object itemId, Object columnId)
    		});//creditNoteDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
        	this.listPurchaseInvoiceCreditNoteDTOTable.addGeneratedColumn("g_credit_note_balance", new Table.ColumnGenerator() {				
    			@Override
    			public Object generateCell(Table source, Object itemId, Object columnId) {
    				// TODO Auto-generated method stub
    				final PurchaseInvoiceCreditNoteDTO vPurchaseInvoiceCreditNoteDTO = (PurchaseInvoiceCreditNoteDTO)itemId;
    				final Label currencyLabel = new Label(vPurchaseInvoiceCreditNoteDTO.getCurrencyDTO().getId_code());
        			currencyLabel.addStyleName("colored");
        			final Label amountLabel = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vPurchaseInvoiceCreditNoteDTO.getCredit_note_balance());
    				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(currencyLabel,amountLabel);
    				vHorizontalLayout.setSpacing(true);
    				return vHorizontalLayout;
    			}//public Object generateCell(Table source, Object itemId, Object columnId)
    		});//creditNoteDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
        	this.listPurchaseInvoiceCreditNoteDTOTable.addGeneratedColumn("g_registration_date", new Table.ColumnGenerator() {				
    			@Override
    			public Object generateCell(Table source, Object itemId, Object columnId) {
    				// TODO Auto-generated method stub
    				final PurchaseInvoiceCreditNoteDTO vPurchaseInvoiceCreditNoteDTO = (PurchaseInvoiceCreditNoteDTO)itemId;
    				return SgpUtils.dateFormater.format(vPurchaseInvoiceCreditNoteDTO.getEmission_date());
    			}//public Object generateCell(Table source, Object itemId, Object columnId)
    		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
        	this.listPurchaseInvoiceCreditNoteDTOTable.addGeneratedColumn("g_status", new Table.ColumnGenerator() {				
    			@Override
    			public Object generateCell(Table source, Object itemId, Object columnId) {
    				// TODO Auto-generated method stub
    				final PurchaseInvoiceCreditNoteDTO vPurchaseInvoiceCreditNoteDTO = (PurchaseInvoiceCreditNoteDTO)itemId;	
    				final Label statusLabel = new Label(messages.get(vPurchaseInvoiceCreditNoteDTO.getStatus()));
    				statusLabel.addStyleName("colored");
    				return statusLabel;
    			}//public Object generateCell(Table source, Object itemId, Object columnId)
    		});//creditNoteDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
        	this.listPurchaseInvoiceCreditNoteDTOTable.setVisibleColumns(new Object[] 
        			{"operations","g_identifier_number","bussines_name","g_registration_date","g_amount","g_credit_note_balance","g_status"});
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnAlignment("operations", Table.Align.LEFT);
        	
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnHeader("g_identifier_number", this.messages.get("application.common.number.indicator.label"));
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnAlignment("g_identifier_number", Table.Align.RIGHT);
        	
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnHeader("bussines_name", this.messages.get("application.common.supplier.label"));
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnAlignment("bussines_name", Table.Align.LEFT);
        	
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnHeader("g_registration_date", this.messages.get("application.common.table.column.registration.date.label"));
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
      	
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnHeader("g_amount", this.messages.get("application.common.table.column.total.amount.label"));
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnAlignment("g_amount", Table.Align.RIGHT);

        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnHeader("g_credit_note_balance", this.messages.get("application.common.balance.label"));
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnAlignment("g_credit_note_balance", Table.Align.RIGHT);
        	
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnHeader("g_status", this.messages.get("application.common.status.label"));
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnAlignment("g_status", Table.Align.LEFT);
        	
        	this.listPurchaseInvoiceCreditNoteDTOTable.setSizeFull();
        	this.listPurchaseInvoiceCreditNoteDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        	this.listPurchaseInvoiceCreditNoteDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
        	this.listPurchaseInvoiceCreditNoteDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnExpandRatio("operations", 0.0039f); 
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnExpandRatio("g_identifier_number", 0.0052f);
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnExpandRatio("bussines_name", 0.019f);
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnExpandRatio("g_registration_date", 0.0075f);
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnExpandRatio("g_amount", 0.005f);
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnExpandRatio("g_credit_note_balance", 0.005f);
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnExpandRatio("g_status", 0.004f);
        	this.listPurchaseInvoiceCreditNoteDTOTable.setSelectable(true);
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnCollapsingAllowed(true);
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnCollapsible("operations", false);
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnCollapsible("g_identifier_number", true);
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnCollapsible("g_status", true);
        	//this.rolesTable.setColumnCollapsible("role_description", false);
        	//this.listPurchaseInvoiceCreditNoteDTOTable.setSortContainerPropertyId("measurment_unit_id");
        	//this.listPurchaseInvoiceCreditNoteDTOTable.setSortAscending(false);
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnReorderingAllowed(true);
        	this.listPurchaseInvoiceCreditNoteDTOTable.setFooterVisible(true);
        	this.listPurchaseInvoiceCreditNoteDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
	private void updatelistPurchaseInvoiceCreditNoteDTO() throws PmsServiceException{
		this.listPurchaseInvoiceCreditNoteDTO = this.cashMovementsManagementService.listPurchaseInvoiceCreditNoteDTO(new PurchaseInvoiceCreditNoteDTO(null, this.purchaseInvoiceDTO.getId()));
		if(this.listPurchaseInvoiceCreditNoteDTO == null)this.listPurchaseInvoiceCreditNoteDTO = new ArrayList<PurchaseInvoiceCreditNoteDTO>();
	}
	
	private HorizontalLayout buildOperationsButtonPanel(final PurchaseInvoiceCreditNoteDTO vPurchaseInvoiceCreditNoteDTO){
		final Button viewDetailsButton = new Button();
		viewDetailsButton.setDescription(this.messages.get("application.common.query.label"));
		viewDetailsButton.setIcon(FontAwesome.SEARCH);
		viewDetailsButton.addStyleName("borderless");
		viewDetailsButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n query vPurchaseInvoiceCreditNoteDTO...\n" + vPurchaseInvoiceCreditNoteDTO.toString());
                try{		                	
                	logger.info("\n query credit note button clicked");
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		viewDetailsButton.setEnabled(true);
		
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(viewDetailsButton);
		//operationsButtonPanel.addStyleName("wrapping");
		return operationsButtonPanel;
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
        
        /*HorizontalLayout tools = this.createGenerateCreditNoteButton();	        	        
        tools.setSpacing(true);
        //tools.addStyleName("toolbar");
        header.addComponent(tools);
        header.setComponentAlignment(tools, Alignment.MIDDLE_RIGHT);*/
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
	    	   this.listPurchaseInvoiceCreditNoteDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.listPurchaseInvoiceCreditNoteDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
	
	@Override
	public void detach() {
	    super.detach();
	    // A new instance of TransactionsView is created every time it's
	    // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n PurchaseInvoiceCreditNoteDTOTableLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
	    DashboardEventBus.unregister(this);
	}//public void detach()

}
