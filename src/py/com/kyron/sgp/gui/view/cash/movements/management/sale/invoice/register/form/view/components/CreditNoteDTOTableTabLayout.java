package py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.register.form.view.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.service.CashMovementsManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.cash.movements.management.SaleInvoiceRegisterFormView;
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
public class CreditNoteDTOTableTabLayout extends VerticalLayout {

	
	private final Logger logger = LoggerFactory.getLogger(CreditNoteDTOTableTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "sale.invoice.management.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private final SaleInvoiceRegisterFormView saleInvoiceRegisterFormView;
	private final SaleInvoiceDTO saleInvoiceDTO;
	private List<CreditNoteDTO> listCreditNoteDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Table creditNoteDTOTable;
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_registration_date","g_amount","g_credit_note_balance","bussines_name"};
    private BussinesSessionUtils bussinesSessionUtils;
    private CashMovementsManagementService cashMovementsManagementService;
    
	public CreditNoteDTOTableTabLayout(
			final SaleInvoiceRegisterFormView saleInvoiceRegisterFormView,
			final SaleInvoiceDTO saleInvoiceDTO) {
		// TODO Auto-generated constructor stub
		this.saleInvoiceRegisterFormView = saleInvoiceRegisterFormView;
		this.saleInvoiceDTO = saleInvoiceDTO;
		try{
			logger.info("\n CreditNoteDTOTableTabLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        if(this.saleInvoiceDTO!=null)this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public creditNoteDTOTableTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	private void initServices() throws Exception{
		this.cashMovementsManagementService = (CashMovementsManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.CASH_MOVEMENTS_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	private void setUpLayoutContent() throws PmsServiceException{
		this.buildcreditNoteDTOTable();
		addComponent(this.buildToolbar());
        addComponent(creditNoteDTOTable);
        setExpandRatio(creditNoteDTOTable, 1);
	}
	
	private void buildcreditNoteDTOTable() throws PmsServiceException{
    	this.creditNoteDTOTable = new Table();
    	BeanItemContainer<CreditNoteDTO> CreditNoteDTOBeanItemContainer	= new BeanItemContainer<CreditNoteDTO>(CreditNoteDTO.class);
    	this.updatelistCreditNoteDTO();
    	CreditNoteDTOBeanItemContainer.addAll(this.listCreditNoteDTO);    	
    	this.creditNoteDTOTable.setContainerDataSource(CreditNoteDTOBeanItemContainer); 
    	
       	this.creditNoteDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
    			@Override
    			public Object generateCell(Table source, Object itemId, Object columnId) {
    				// TODO Auto-generated method stub
    				final CreditNoteDTO vCreditNoteDTO = (CreditNoteDTO)itemId;
    				return buildOperationsButtonPanel(vCreditNoteDTO);
    			}//public Object generateCell(Table source, Object itemId, Object columnId)
    		});//creditNoteDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
        	this.creditNoteDTOTable.addGeneratedColumn("g_identifier_number", new Table.ColumnGenerator() {				
    			@Override
    			public Object generateCell(Table source, Object itemId, Object columnId) {
    				// TODO Auto-generated method stub
    				final CreditNoteDTO vCreditNoteDTO = (CreditNoteDTO)itemId;
    				final Label identifierNumberLabel = new Label(vCreditNoteDTO.getIdentifier_number());
    				identifierNumberLabel.addStyleName("colored");
    				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(identifierNumberLabel);
    				vHorizontalLayout.setSizeFull();
    				vHorizontalLayout.setComponentAlignment(identifierNumberLabel, Alignment.MIDDLE_RIGHT);
    				return vHorizontalLayout;
    			}//public Object generateCell(Table source, Object itemId, Object columnId)
    		});//creditNoteDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {

        	this.creditNoteDTOTable.addGeneratedColumn("g_amount", new Table.ColumnGenerator() {				
    			@Override
    			public Object generateCell(Table source, Object itemId, Object columnId) {
    				// TODO Auto-generated method stub
    				final CreditNoteDTO vCreditNoteDTO = (CreditNoteDTO)itemId;
    				final Label currencyLabel = new Label(vCreditNoteDTO.getCurrencyDTO().getId_code());
        			currencyLabel.addStyleName("colored");
        			final Label amountLabel = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vCreditNoteDTO.getTotal_amount());
    				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(currencyLabel,amountLabel);
    				vHorizontalLayout.setSpacing(true);
    				return vHorizontalLayout;
    			}//public Object generateCell(Table source, Object itemId, Object columnId)
    		});//creditNoteDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
        	this.creditNoteDTOTable.addGeneratedColumn("g_credit_note_balance", new Table.ColumnGenerator() {				
    			@Override
    			public Object generateCell(Table source, Object itemId, Object columnId) {
    				// TODO Auto-generated method stub
    				final CreditNoteDTO vCreditNoteDTO = (CreditNoteDTO)itemId;
    				final Label currencyLabel = new Label(vCreditNoteDTO.getCurrencyDTO().getId_code());
        			currencyLabel.addStyleName("colored");
        			final Label amountLabel = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vCreditNoteDTO.getCredit_note_balance());
    				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(currencyLabel,amountLabel);
    				vHorizontalLayout.setSpacing(true);
    				return vHorizontalLayout;
    			}//public Object generateCell(Table source, Object itemId, Object columnId)
    		});//creditNoteDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
        	this.creditNoteDTOTable.addGeneratedColumn("g_registration_date", new Table.ColumnGenerator() {				
    			@Override
    			public Object generateCell(Table source, Object itemId, Object columnId) {
    				// TODO Auto-generated method stub
    				final CreditNoteDTO vCreditNoteDTO = (CreditNoteDTO)itemId;
    				return SgpUtils.dateFormater.format(vCreditNoteDTO.getEmission_date());
    			}//public Object generateCell(Table source, Object itemId, Object columnId)
    		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
        	this.creditNoteDTOTable.addGeneratedColumn("g_status", new Table.ColumnGenerator() {				
    			@Override
    			public Object generateCell(Table source, Object itemId, Object columnId) {
    				// TODO Auto-generated method stub
    				final CreditNoteDTO vCreditNoteDTO = (CreditNoteDTO)itemId;	
    				final Label statusLabel = new Label(messages.get(vCreditNoteDTO.getStatus()));
    				statusLabel.addStyleName("colored");
    				return statusLabel;
    			}//public Object generateCell(Table source, Object itemId, Object columnId)
    		});//creditNoteDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
        	this.creditNoteDTOTable.setVisibleColumns(new Object[] 
        			{"operations","g_identifier_number","bussines_name","g_registration_date","g_amount","g_credit_note_balance","g_status"});
        	this.creditNoteDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
        	this.creditNoteDTOTable.setColumnAlignment("operations", Table.Align.LEFT);
        	
        	this.creditNoteDTOTable.setColumnHeader("g_identifier_number", this.messages.get("application.common.number.indicator.label"));
        	this.creditNoteDTOTable.setColumnAlignment("g_identifier_number", Table.Align.RIGHT);
        	
        	this.creditNoteDTOTable.setColumnHeader("bussines_name", this.messages.get("application.common.customer.label"));
        	this.creditNoteDTOTable.setColumnAlignment("bussines_name", Table.Align.LEFT);
        	
        	this.creditNoteDTOTable.setColumnHeader("g_registration_date", this.messages.get("application.common.table.column.registration.date.label"));
        	this.creditNoteDTOTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
      	
        	this.creditNoteDTOTable.setColumnHeader("g_amount", this.messages.get("application.common.table.column.total.amount.label"));
        	this.creditNoteDTOTable.setColumnAlignment("g_amount", Table.Align.RIGHT);

        	this.creditNoteDTOTable.setColumnHeader("g_credit_note_balance", this.messages.get("application.common.balance.label"));
        	this.creditNoteDTOTable.setColumnAlignment("g_credit_note_balance", Table.Align.RIGHT);
        	
        	this.creditNoteDTOTable.setColumnHeader("g_status", this.messages.get("application.common.status.label"));
        	this.creditNoteDTOTable.setColumnAlignment("g_status", Table.Align.LEFT);
        	
        	this.creditNoteDTOTable.setSizeFull();
        	this.creditNoteDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        	this.creditNoteDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
        	this.creditNoteDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
        	this.creditNoteDTOTable.setColumnExpandRatio("operations", 0.0039f); 
        	this.creditNoteDTOTable.setColumnExpandRatio("g_identifier_number", 0.0052f);
        	this.creditNoteDTOTable.setColumnExpandRatio("bussines_name", 0.019f);
        	this.creditNoteDTOTable.setColumnExpandRatio("g_registration_date", 0.0075f);
        	this.creditNoteDTOTable.setColumnExpandRatio("g_amount", 0.005f);
        	this.creditNoteDTOTable.setColumnExpandRatio("g_credit_note_balance", 0.005f);
        	this.creditNoteDTOTable.setColumnExpandRatio("g_status", 0.004f);
        	this.creditNoteDTOTable.setSelectable(true);
        	this.creditNoteDTOTable.setColumnCollapsingAllowed(true);
        	this.creditNoteDTOTable.setColumnCollapsible("operations", false);
        	this.creditNoteDTOTable.setColumnCollapsible("g_identifier_number", true);
        	this.creditNoteDTOTable.setColumnCollapsible("g_status", true);
        	//this.rolesTable.setColumnCollapsible("role_description", false);
        	//this.creditNoteDTOTable.setSortContainerPropertyId("measurment_unit_id");
        	//this.creditNoteDTOTable.setSortAscending(false);
        	this.creditNoteDTOTable.setColumnReorderingAllowed(true);
        	this.creditNoteDTOTable.setFooterVisible(true);
        	this.creditNoteDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
    private void updatelistCreditNoteDTO() throws PmsServiceException{
    	this.listCreditNoteDTO = this.cashMovementsManagementService.listCreditNoteDTO(new CreditNoteDTO(null, this.saleInvoiceDTO.getId()));
    	if(this.listCreditNoteDTO == null) this.listCreditNoteDTO = new ArrayList<CreditNoteDTO>();
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
	        Label title = new Label(this.messages.get("application.common.sale.invoice.number.indicator.label"));
	        title.addStyleName(ValoTheme.LABEL_H2);
	        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	        Label saleInvoiceNumber = new Label(this.saleInvoiceDTO.getIdentifier_number());
	        saleInvoiceNumber.addStyleName(ValoTheme.LABEL_H2);
	        saleInvoiceNumber.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	        saleInvoiceNumber.addStyleName("colored");
	        HorizontalLayout vHorizontalLayout = new HorizontalLayout(title,saleInvoiceNumber);
	        vHorizontalLayout.setSpacing(true);
	        header.addComponent(vHorizontalLayout);
	        header.setComponentAlignment(vHorizontalLayout, Alignment.MIDDLE_LEFT);
	        
	        HorizontalLayout tools = this.createGenerateCreditNoteButton();	        	        
	        tools.setSpacing(true);
	        //tools.addStyleName("toolbar");
	        header.addComponent(tools);
	        header.setComponentAlignment(tools, Alignment.MIDDLE_RIGHT);
	        return header;
	 }//private Component buildToolbar()
	
	 private HorizontalLayout createGenerateCreditNoteButton(){
	        final Button generateCreditNoteButton = new Button(this.messages.get(this.VIEW_NAME + "tab.credit.notes.button.generate.credit.note.label"));
	        generateCreditNoteButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.credit.notes.button.generate.credit.note.description"));
	        generateCreditNoteButton.addClickListener(new ClickListener() {
	            @Override
	            public void buttonClick(final ClickEvent event) {
	            	try {
						saleInvoiceRegisterFormView.prepareCreditNoteDTOGeneration();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						commonExceptionErrorNotification.showErrorMessageNotification(e);
					}
	            }
	        });
	        generateCreditNoteButton.setEnabled(!this.saleInvoiceDTO.getStatus().equals("application.common.status.annulled"));
	        generateCreditNoteButton.setVisible(false);
	        return new HorizontalLayout(generateCreditNoteButton);
	 }//private Button createRegisterMachineButton()
	 
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    // Some columns are collapsed when browser window width gets small
	    // enough to make the table fit better.
	    if (defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.creditNoteDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.creditNoteDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
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
				+"\n creditNoteDTOTableTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private HorizontalLayout buildOperationsButtonPanel(final CreditNoteDTO vCreditNoteDTO){	
		final Button viewDetailsButton = new Button();
		viewDetailsButton.setDescription(this.messages.get("application.common.query.label"));
		viewDetailsButton.setIcon(FontAwesome.SEARCH);
		viewDetailsButton.addStyleName("borderless");
		viewDetailsButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar CreditNoteDTO...\n" + vCreditNoteDTO.toString());
                try{		                	
                	logger.info("\n query credit note button clicked");
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		viewDetailsButton.setEnabled(true);

    	final Button downloadButton = new Button();
    	downloadButton.setId("downloadButtonId" + "vCreditNoteDTO" + vCreditNoteDTO.getId());
    	downloadButton.setPrimaryStyleName(".dashboard .myCustomyInvisibleButtonCssRule");
    	//downloadButton.setVisible(false);
    	this.saleInvoiceRegisterFormView.setUpDownloadButton(downloadButton);
    	
		final Button printButton = new Button();
		printButton.setDescription(this.messages.get("application.common.button.print.label"));
		printButton.setIcon(FontAwesome.PRINT);
		printButton.addStyleName("borderless");
		printButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar CreditNoteDTO...\n" + vCreditNoteDTO.toString());
                try{		                	
                	logger.info("\n print credit note button clicked");
                	saleInvoiceRegisterFormView.printCreditNoteDTO(vCreditNoteDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		printButton.setEnabled(true);
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(viewDetailsButton,printButton,downloadButton);
		//operationsButtonPanel.addStyleName("wrapping");
		return operationsButtonPanel;
	}
}
