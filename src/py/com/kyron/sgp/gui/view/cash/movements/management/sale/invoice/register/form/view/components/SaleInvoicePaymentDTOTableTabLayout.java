/**
 * 
 */
package py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.register.form.view.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoicePaymentDTO;
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

/**
 * @author testuser
 *
 */
@SuppressWarnings("serial")
public class SaleInvoicePaymentDTOTableTabLayout extends VerticalLayout {

	/**
	 * 
	 */
	private final Logger logger = LoggerFactory.getLogger(SaleInvoicePaymentDTOTableTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "sale.invoice.management.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private final SaleInvoiceRegisterFormView saleInvoiceRegisterFormView;
	private final SaleInvoiceDTO saleInvoiceDTO;
	private List<SaleInvoicePaymentDTO> listSaleInvoicePaymentDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Table saleInvoicePaymentDTOTable;
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_registration_date","g_payment_due_date","g_cancellation_date","g_annulment_date"};
    private BussinesSessionUtils bussinesSessionUtils;
    private CashMovementsManagementService cashMovementsManagementService;
	
	public SaleInvoicePaymentDTOTableTabLayout(
			final SaleInvoiceRegisterFormView saleInvoiceRegisterFormView,
			final SaleInvoiceDTO saleInvoiceDTO) {
		// TODO Auto-generated constructor stub
		this.saleInvoiceRegisterFormView = saleInvoiceRegisterFormView;
		this.saleInvoiceDTO = saleInvoiceDTO;
		try{
			logger.info("\n saleInvoicePaymentDTOTableTabLayout..");
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

	/**
	 * @param children
	 */
	/*public SaleInvoicePaymentDTOTableTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/

	private void initServices() throws Exception{
		this.cashMovementsManagementService = (CashMovementsManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.CASH_MOVEMENTS_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	private void setUpLayoutContent() throws PmsServiceException{
		this.buildsaleInvoicePaymentDTOTable();
		addComponent(this.buildToolbar());
        addComponent(saleInvoicePaymentDTOTable);
        setExpandRatio(saleInvoicePaymentDTOTable, 1);
	}
	
	private void buildsaleInvoicePaymentDTOTable() throws PmsServiceException{
    	this.saleInvoicePaymentDTOTable = new Table();
    	BeanItemContainer<SaleInvoicePaymentDTO> SaleInvoicePaymentDTOBeanItemContainer	= new BeanItemContainer<SaleInvoicePaymentDTO>(SaleInvoicePaymentDTO.class);
    	this.updatelistSaleInvoicePaymentDTO();
    	SaleInvoicePaymentDTOBeanItemContainer.addAll(this.listSaleInvoicePaymentDTO);    	
    	this.saleInvoicePaymentDTOTable.setContainerDataSource(SaleInvoicePaymentDTOBeanItemContainer); 
    	
    	this.saleInvoicePaymentDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoicePaymentDTO vSaleInvoicePaymentDTO = (SaleInvoicePaymentDTO)itemId;
				return buildOperationsButtonPanel(vSaleInvoicePaymentDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//saleInvoicePaymentDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoicePaymentDTOTable.addGeneratedColumn("g_amount", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoicePaymentDTO vSaleInvoicePaymentDTO = (SaleInvoicePaymentDTO)itemId;
				final Label currencyLabel = new Label(vSaleInvoicePaymentDTO.getCurrencyDTO().getId_code());
    			currencyLabel.addStyleName("colored");
    			final Label amountLabel = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vSaleInvoicePaymentDTO.getAmount());
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(currencyLabel,amountLabel);
				vHorizontalLayout.setComponentAlignment(currencyLabel, Alignment.MIDDLE_LEFT);
				vHorizontalLayout.setComponentAlignment(amountLabel, Alignment.MIDDLE_RIGHT);				
				vHorizontalLayout.setSizeFull();
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {    	
    	this.saleInvoicePaymentDTOTable.addGeneratedColumn("g_sale_invoice_balance", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoicePaymentDTO vSaleInvoicePaymentDTO = (SaleInvoicePaymentDTO)itemId;
				final Label currencyLabel = new Label(vSaleInvoicePaymentDTO.getCurrencyDTO().getId_code());
    			currencyLabel.addStyleName("colored");
    			final Label amountLabel = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vSaleInvoicePaymentDTO.getSale_invoice_balance());
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(currencyLabel,amountLabel);
				vHorizontalLayout.setComponentAlignment(currencyLabel, Alignment.MIDDLE_LEFT);
				vHorizontalLayout.setComponentAlignment(amountLabel, Alignment.MIDDLE_RIGHT);				
				vHorizontalLayout.setSizeFull();
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoicePaymentDTOTable.addGeneratedColumn("g_status", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoicePaymentDTO vSaleInvoicePaymentDTO = (SaleInvoicePaymentDTO)itemId;	
				final Label statusLabel = new Label(messages.get(vSaleInvoicePaymentDTO.getStatus()));
				statusLabel.addStyleName("colored");
				return statusLabel;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//orderDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoicePaymentDTOTable.addGeneratedColumn("g_registration_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoicePaymentDTO vSaleInvoicePaymentDTO = (SaleInvoicePaymentDTO)itemId;
				return SgpUtils.dateFormater.format(vSaleInvoicePaymentDTO.getRegistration_date());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoicePaymentDTOTable.addGeneratedColumn("g_payment_due_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoicePaymentDTO vSaleInvoicePaymentDTO = (SaleInvoicePaymentDTO)itemId;
				if(!vSaleInvoicePaymentDTO.getStatus().equals("application.common.status.annulled") 
						&& !vSaleInvoicePaymentDTO.getStatus().equals("application.common.status.canceled")
						&& vSaleInvoicePaymentDTO.getPayment_due_date().before(new Date())
					){
					final Label dueDateLabel = new Label(SgpUtils.dateFormater.format(vSaleInvoicePaymentDTO.getPayment_due_date()));
					dueDateLabel.addStyleName("failure");
					return dueDateLabel;
				}else
					return SgpUtils.dateFormater.format(vSaleInvoicePaymentDTO.getPayment_due_date());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoicePaymentDTOTable.addGeneratedColumn("g_cancellation_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoicePaymentDTO vSaleInvoicePaymentDTO = (SaleInvoicePaymentDTO)itemId;
				if(vSaleInvoicePaymentDTO.getCancellation_date()!=null)
					return SgpUtils.dateFormater.format(vSaleInvoicePaymentDTO.getCancellation_date());
				else
					return null;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoicePaymentDTOTable.addGeneratedColumn("g_annulment_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoicePaymentDTO vSaleInvoicePaymentDTO = (SaleInvoicePaymentDTO)itemId;
				if(vSaleInvoicePaymentDTO.getAnnulment_date()!=null)
					return SgpUtils.dateFormater.format(vSaleInvoicePaymentDTO.getAnnulment_date());
				else
					return null;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoicePaymentDTOTable.setVisibleColumns(new Object[] 
    			{"operations","payment_number","g_amount","g_sale_invoice_balance","g_status","g_registration_date"
    			,"g_payment_due_date","g_cancellation_date","g_annulment_date"});
    	this.saleInvoicePaymentDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.saleInvoicePaymentDTOTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.saleInvoicePaymentDTOTable.setColumnHeader("payment_number", this.messages.get("application.common.number.indicator.label"));
    	this.saleInvoicePaymentDTOTable.setColumnAlignment("payment_number", Table.Align.RIGHT);

    	this.saleInvoicePaymentDTOTable.setColumnHeader("g_amount", this.messages.get("application.common.amount.label"));
    	this.saleInvoicePaymentDTOTable.setColumnAlignment("g_amount", Table.Align.RIGHT);
    	
    	this.saleInvoicePaymentDTOTable.setColumnHeader("g_sale_invoice_balance", this.messages.get("application.common.balance.label"));
    	this.saleInvoicePaymentDTOTable.setColumnAlignment("g_sale_invoice_balance", Table.Align.RIGHT);
  	
    	this.saleInvoicePaymentDTOTable.setColumnHeader("g_status", this.messages.get("application.common.status.label"));
    	this.saleInvoicePaymentDTOTable.setColumnAlignment("g_status", Table.Align.LEFT);

    	this.saleInvoicePaymentDTOTable.setColumnHeader("g_registration_date", this.messages.get("application.common.table.column.registration.date.label"));
    	this.saleInvoicePaymentDTOTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);

    	this.saleInvoicePaymentDTOTable.setColumnHeader("g_payment_due_date", this.messages.get("application.common.table.column.validity.end.date.label"));
    	this.saleInvoicePaymentDTOTable.setColumnAlignment("g_payment_due_date", Table.Align.LEFT);
    	
    	this.saleInvoicePaymentDTOTable.setColumnHeader("g_cancellation_date", this.messages.get("application.common.cancelation.date.label"));
    	this.saleInvoicePaymentDTOTable.setColumnAlignment("g_cancellation_date", Table.Align.LEFT);
  	
    	this.saleInvoicePaymentDTOTable.setColumnHeader("g_annulment_date", this.messages.get("application.common.annulment.date.label"));
    	this.saleInvoicePaymentDTOTable.setColumnAlignment("g_annulment_date", Table.Align.LEFT);
    	this.saleInvoicePaymentDTOTable.setSizeFull();
    	this.saleInvoicePaymentDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.saleInvoicePaymentDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.saleInvoicePaymentDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.saleInvoicePaymentDTOTable.setColumnExpandRatio("operations", 0.0042f); 
    	this.saleInvoicePaymentDTOTable.setColumnExpandRatio("payment_number", 0.0016f);
    	this.saleInvoicePaymentDTOTable.setColumnExpandRatio("g_amount", 0.0051f);
    	this.saleInvoicePaymentDTOTable.setColumnExpandRatio("g_sale_invoice_balance", 0.0051f);
    	this.saleInvoicePaymentDTOTable.setColumnExpandRatio("g_status", 0.0035f);
    	this.saleInvoicePaymentDTOTable.setColumnExpandRatio("g_registration_date", 0.0056f);
    	this.saleInvoicePaymentDTOTable.setColumnExpandRatio("g_payment_due_date", 0.0055f);
    	this.saleInvoicePaymentDTOTable.setColumnExpandRatio("g_cancellation_date", 0.0054f);
    	this.saleInvoicePaymentDTOTable.setColumnExpandRatio("g_annulment_date", 0.0053f);
    	this.saleInvoicePaymentDTOTable.setSelectable(true);
    	this.saleInvoicePaymentDTOTable.setColumnCollapsingAllowed(true);
    	this.saleInvoicePaymentDTOTable.setColumnCollapsible("operations", false);
    	this.saleInvoicePaymentDTOTable.setColumnCollapsible("g_registration_date", true);
    	this.saleInvoicePaymentDTOTable.setColumnCollapsible("g_cancellation_date", true);
    	this.saleInvoicePaymentDTOTable.setColumnCollapsible("g_annulment_date", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.saleInvoicePaymentDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.saleInvoicePaymentDTOTable.setSortAscending(false);
    	this.saleInvoicePaymentDTOTable.setColumnReorderingAllowed(true);
    	this.saleInvoicePaymentDTOTable.setFooterVisible(true);
    	this.saleInvoicePaymentDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
    private void updatelistSaleInvoicePaymentDTO() throws PmsServiceException{
    	this.listSaleInvoicePaymentDTO = this.cashMovementsManagementService.listSaleInvoicePaymentDTO(new SaleInvoicePaymentDTO(null, this.saleInvoiceDTO.getId()));
    	if(this.listSaleInvoicePaymentDTO == null) this.listSaleInvoicePaymentDTO = new ArrayList<SaleInvoicePaymentDTO>();
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
	        
	        HorizontalLayout tools = this.createGenerateSaleInvoicePaymentsButton();	        	        
	        tools.setSpacing(true);
	        //tools.addStyleName("toolbar");
	        header.addComponent(tools);
	        header.setComponentAlignment(tools, Alignment.MIDDLE_RIGHT);
	        return header;
	 }//private Component buildToolbar()
	
	 private HorizontalLayout createGenerateSaleInvoicePaymentsButton(){
	        final Button generateSaleInvoicePayments = new Button(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.payments.button.generate.sale.invoice.payments.label"));
	        generateSaleInvoicePayments.setDescription(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.payments.button.generate.sale.invoice.payments.description"));
	        generateSaleInvoicePayments.addClickListener(new ClickListener() {
	            @Override
	            public void buttonClick(final ClickEvent event) {
	            	try {
						saleInvoiceRegisterFormView.generateSaleInvoicePayments(saleInvoiceDTO);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						commonExceptionErrorNotification.showErrorMessageNotification(e);
					}
	            }
	        });
	        generateSaleInvoicePayments.setEnabled(this.saleInvoiceDTO.getStatus().equals("application.common.status.pending"));
	        generateSaleInvoicePayments.setVisible(false);
	        
	        final Button reGenerateSaleInvoicePayments = new Button(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.payments.button.re.generate.sale.invoice.payments.label"));
	        reGenerateSaleInvoicePayments.setDescription(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.payments.button.re.generate.sale.invoice.payments.description"));
	        reGenerateSaleInvoicePayments.addClickListener(new ClickListener() {
	            @Override
	            public void buttonClick(final ClickEvent event) {
	            	try {
						saleInvoiceRegisterFormView.reGenerateSaleInvoicePaymentDTO(saleInvoiceDTO);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						commonExceptionErrorNotification.showErrorMessageNotification(e);
					}
	            }
	        });
	        reGenerateSaleInvoicePayments.setVisible(false);
	        /*reGenerateSaleInvoicePayments.setVisible(bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
				.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "edit.revision.status.sale.invoice"));
	        reGenerateSaleInvoicePayments.setEnabled(
	        		this.listSaleInvoicePaymentDTO!=null && !this.listSaleInvoicePaymentDTO.isEmpty()
	        		&& this.saleInvoiceDTO.getStatus().equals("application.common.status.payment.in.progress")
	        		&& this.saleInvoiceDTO.getPrevious_status() != null 
	        		&& this.saleInvoiceDTO.getPrevious_status().equals("application.common.status.revision"));*/
	        return new HorizontalLayout(reGenerateSaleInvoicePayments,generateSaleInvoicePayments);
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
	    	   this.saleInvoicePaymentDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.saleInvoicePaymentDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
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
				+"\n saleInvoicePaymentDTOTableTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private HorizontalLayout buildOperationsButtonPanel(final SaleInvoicePaymentDTO vSaleInvoicePaymentDTO){	
		final Button effectuatePaymentButton = new Button();
		effectuatePaymentButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.payments.table.payments.column.operations.button.effectuate.payment.description"));
		effectuatePaymentButton.setIcon(FontAwesome.PAYPAL);
		effectuatePaymentButton.addStyleName("borderless");
		effectuatePaymentButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar SaleInvoicePaymentDTO...\n" + vSaleInvoicePaymentDTO.toString());
                try{		                	
                	saleInvoiceRegisterFormView.preparePaymentEffectuation(vSaleInvoicePaymentDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		effectuatePaymentButton.setEnabled(vSaleInvoicePaymentDTO.getStatus().equals("application.common.status.in.progress"));

		final Button viewCashReceiptDocumentButton = new Button();
		viewCashReceiptDocumentButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.payments.table.payments.column.operations.button.query.cash.receipt.document.description"));
		viewCashReceiptDocumentButton.setIcon(FontAwesome.PAPERCLIP);
		viewCashReceiptDocumentButton.addStyleName("borderless");
		viewCashReceiptDocumentButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar SaleInvoicePaymentDTO...\n" + vSaleInvoicePaymentDTO.toString());
                try{		                	
                	saleInvoiceRegisterFormView.showQueryCashReceiptDocumentWindow(vSaleInvoicePaymentDTO.getCashReceiptDocumentDTO());
                	//asdf
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		viewCashReceiptDocumentButton.setVisible(vSaleInvoicePaymentDTO.getCashReceiptDocumentDTO() != null);

    	final Button downloadButton = new Button();
    	final String vDownloadButtonId = vSaleInvoicePaymentDTO.getCashReceiptDocumentDTO() != null
    			&& vSaleInvoicePaymentDTO.getCashReceiptDocumentDTO().getId() != null ? 
    			"" + vSaleInvoicePaymentDTO.getCashReceiptDocumentDTO().getId() : "" + Math.round(Math.random() * 1000000L);
    			
    	downloadButton.setId("downloadButtonId" + "vCashReceiptDocumentDTO" + vDownloadButtonId);
    	downloadButton.setPrimaryStyleName(".dashboard .myCustomyInvisibleButtonCssRule");
    	//downloadButton.setVisible(false);
    	this.saleInvoiceRegisterFormView.setUpDownloadButton(downloadButton);
    	
		final Button printCashReceiptDocumentButton = new Button();
		printCashReceiptDocumentButton.setDescription(this.messages.get("application.common.button.print.label") + " " + this.messages.get("application.common.document.type.cash.receipt.document").toLowerCase());
		printCashReceiptDocumentButton.setIcon(FontAwesome.PRINT);
		printCashReceiptDocumentButton.addStyleName("borderless");
		printCashReceiptDocumentButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n printCashReceiptDocumentButton...\n" + vSaleInvoicePaymentDTO.toString());
                try{		                	
                	saleInvoiceRegisterFormView.printCashReceiptDocumentDTO(vSaleInvoicePaymentDTO.getCashReceiptDocumentDTO());
                	//asdf
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		printCashReceiptDocumentButton.setVisible(vSaleInvoicePaymentDTO.getCashReceiptDocumentDTO() != null);
		
		
		final Button dueDateReachedButton = new Button();
		dueDateReachedButton.setDescription(this.messages.get("application.common.due.date.reached.label"));
		dueDateReachedButton.setIcon(FontAwesome.WARNING);
		dueDateReachedButton.addStyleName("borderless");
		dueDateReachedButton.setResponsive(false);
		dueDateReachedButton.setVisible(
				!vSaleInvoicePaymentDTO.getStatus().equals("application.common.status.annulled") 
				&& !vSaleInvoicePaymentDTO.getStatus().equals("application.common.status.canceled")
				&& vSaleInvoicePaymentDTO.getPayment_due_date().before(new Date())
				||
				(vSaleInvoicePaymentDTO.getStatus().equals("application.common.status.canceled")
						&& vSaleInvoicePaymentDTO.getPayment_due_date().before(vSaleInvoicePaymentDTO.getCancellation_date()))
				);
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(effectuatePaymentButton,viewCashReceiptDocumentButton,printCashReceiptDocumentButton,dueDateReachedButton,downloadButton);
		//operationsButtonPanel.addStyleName("wrapping");
		return operationsButtonPanel;
	}
	

	

}
