package py.com.kyron.sgp.gui.view.stockmanagement.sale.invoice.product.deliverables.form.view.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SaleInvoiceItemProductDeliverablesDTO;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable.Unit;
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
public class SaleInvoiceItemProductDeliverablesDTOTableLayout extends
		VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(SaleInvoiceItemProductDeliverablesDTOTableLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
    private static final String[] DEFAULT_COLLAPSIBLE = {"ord_item_pending_to_production",
    	"ord_item_in_progress_quantity",
    	"invoice_item_total_exigible_quantity",
    	"invoice_item_delivered_quantity",
    	"invoice_item_product_stock_quantity"};
    private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final SaleInvoiceItemProductDeliverablesDTOTableLayoutFunctions saleInvoiceItemProductDeliverablesDTOTableLayoutFunctions;
	private final List<SaleInvoiceItemProductDeliverablesDTO> listSaleInvoiceItemProductDeliverablesDTO;
	private Table tableListSaleInvoiceItemProductDeliverablesDTO;
	private BussinesSessionUtils bussinesSessionUtils;
	
	public SaleInvoiceItemProductDeliverablesDTOTableLayout(
			final SaleInvoiceItemProductDeliverablesDTOTableLayoutFunctions saleInvoiceItemProductDeliverablesDTOTableLayoutFunctions,
			final String VIEW_NAME,
			final List<SaleInvoiceItemProductDeliverablesDTO> listSaleInvoiceItemProductDeliverablesDTO) {
		// TODO Auto-generated constructor stub
		this.saleInvoiceItemProductDeliverablesDTOTableLayoutFunctions = saleInvoiceItemProductDeliverablesDTOTableLayoutFunctions;
		this.VIEW_NAME = VIEW_NAME;
		this.listSaleInvoiceItemProductDeliverablesDTO = listSaleInvoiceItemProductDeliverablesDTO;
		try{
			logger.info("\n SaleInvoiceItemProductDeliverablesDTOTableLayout()...");
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.initServices();
			this.setSizeFull();
	        //this.addStyleName("transactions");
	        this.setId("SaleInvoiceItemProductDeliverablesDTOTableLayout");
			this.setSpacing(true);
			this.setMargin(true);
	        Responsive.makeResponsive(this);
	        DashboardEventBus.register(this);
	    	int listSize = (this.listSaleInvoiceItemProductDeliverablesDTO!=null ? this.listSaleInvoiceItemProductDeliverablesDTO.size() : 0);
	    	this.setHeight(150f + (listSize*45), Unit.PIXELS);
	        this.buildtableListSaleInvoiceItemProductDeliverablesDTO();
	        this.addComponent(this.tableListSaleInvoiceItemProductDeliverablesDTO);
	        this.setExpandRatio(this.tableListSaleInvoiceItemProductDeliverablesDTO, 1);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public SaleInvoiceItemProductDeliverablesDTOTableLayout(
			Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}
	 */
	private void initServices() throws Exception{
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void buildtableListSaleInvoiceItemProductDeliverablesDTO() throws PmsServiceException{
    	this.tableListSaleInvoiceItemProductDeliverablesDTO = new Table();
    	BeanItemContainer<SaleInvoiceItemProductDeliverablesDTO> SaleInvoiceItemProductDeliverablesDTOBeanItemContainer	= new BeanItemContainer<SaleInvoiceItemProductDeliverablesDTO>(SaleInvoiceItemProductDeliverablesDTO.class);
    	if(this.listSaleInvoiceItemProductDeliverablesDTO == null)SaleInvoiceItemProductDeliverablesDTOBeanItemContainer.addAll(new ArrayList<SaleInvoiceItemProductDeliverablesDTO>());
    	else SaleInvoiceItemProductDeliverablesDTOBeanItemContainer.addAll(this.listSaleInvoiceItemProductDeliverablesDTO);    	
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setContainerDataSource(SaleInvoiceItemProductDeliverablesDTOBeanItemContainer);     	
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceItemProductDeliverablesDTO vSaleInvoiceItemProductDeliverablesDTO = (SaleInvoiceItemProductDeliverablesDTO)itemId;
				return buildOperationsButtonPanel(vSaleInvoiceItemProductDeliverablesDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//tableListSaleInvoiceItemProductDeliverablesDTO.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.addGeneratedColumn("g_product", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceItemProductDeliverablesDTO vSaleInvoiceItemProductDeliverablesDTO = (SaleInvoiceItemProductDeliverablesDTO)itemId;
				final Label productIdLabel = new Label(vSaleInvoiceItemProductDeliverablesDTO.getProduct_id());
				productIdLabel.addStyleName("colored");
				return productIdLabel;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//tableListSaleInvoiceItemProductDeliverablesDTO.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.addGeneratedColumn("g_invoice_item_delivered_quantity", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceItemProductDeliverablesDTO vSaleInvoiceItemProductDeliverablesDTO = (SaleInvoiceItemProductDeliverablesDTO)itemId;
				final ObjectProperty<Long> property = new ObjectProperty<Long>(vSaleInvoiceItemProductDeliverablesDTO.getInvoice_item_delivered_quantity());
				final Label label = new Label(property);				
				label.addStyleName("colored");
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(label);
				vHorizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//tableListSaleInvoiceItemProductDeliverablesDTO.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.addGeneratedColumn("g_invoice_item_remain_exigible_quantity", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceItemProductDeliverablesDTO vSaleInvoiceItemProductDeliverablesDTO = (SaleInvoiceItemProductDeliverablesDTO)itemId;
				final ObjectProperty<Long> property = new ObjectProperty<Long>(vSaleInvoiceItemProductDeliverablesDTO.getInvoice_item_remain_exigible_quantity());
				final Label label = new Label(property);				
				label.addStyleName("colored");
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(label);
				vHorizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//tableListSaleInvoiceItemProductDeliverablesDTO.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	
    	if(this.saleInvoiceItemProductDeliverablesDTOTableLayoutFunctions.
    			getSaleInvoiceProductDeliverablesDTO().getTransactionRealized() == null
    			&& this.saleInvoiceItemProductDeliverablesDTOTableLayoutFunctions.getEditFormMode())
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.addGeneratedColumn("g_deliver_quantity", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceItemProductDeliverablesDTO vSaleInvoiceItemProductDeliverablesDTO = (SaleInvoiceItemProductDeliverablesDTO)itemId;
				if(vSaleInvoiceItemProductDeliverablesDTO.getInvoice_item_remain_exigible_quantity() > 0
					&& vSaleInvoiceItemProductDeliverablesDTO.getDeliver_quantity()!=null){
					final ObjectProperty<Long> property = new ObjectProperty<Long>(vSaleInvoiceItemProductDeliverablesDTO.getDeliver_quantity());
					final Label label = new Label(property);				
					label.addStyleName("colored");
					final HorizontalLayout vHorizontalLayout = new HorizontalLayout(label);
					vHorizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
					return vHorizontalLayout;
				}else if(vSaleInvoiceItemProductDeliverablesDTO.getInvoice_item_remain_exigible_quantity() == 0){
					final Label label = new Label(messages.get(VIEW_NAME + "tab.sale.invoice.product.deliverables.table.sale.invoice.item.product.deliverables.column.deliver.quantity.label.full.output"));				
					label.addStyleName("colored");
					return label;					
				}else{
					final Label label = new Label(messages.get(VIEW_NAME + "tab.sale.invoice.product.deliverables.table.sale.invoice.item.product.deliverables.column.deliver.quantity.label.deliver.quantity.required"));				
					label.addStyleName("colored");
					return label;					
				}
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//tableListSaleInvoiceItemProductDeliverablesDTO.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	
    	
    	if(this.saleInvoiceItemProductDeliverablesDTOTableLayoutFunctions.
    			getSaleInvoiceProductDeliverablesDTO().getTransactionRealized() == null
    			&& this.saleInvoiceItemProductDeliverablesDTOTableLayoutFunctions.getEditFormMode())
		    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setVisibleColumns(new Object[] 
		    			{"operations",
		    			"g_product",
		    			"ord_item_canceled_entering_production",
		    			"ord_item_pending_to_production",
		    			"ord_item_in_progress_quantity",
		    			"invoice_item_total_exigible_quantity",
		    			"invoice_item_product_stock_quantity",
		    			"g_invoice_item_delivered_quantity",
		    			"invoice_item_returned_quantity_stock",
		    			"g_invoice_item_remain_exigible_quantity",		    			
		    			"g_deliver_quantity"});
    	else
	    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setVisibleColumns(new Object[] 
	    			{"operations",
	    			"g_product",
	    			"ord_item_canceled_entering_production",
	    			"ord_item_pending_to_production",
	    			"ord_item_in_progress_quantity",
	    			"invoice_item_total_exigible_quantity",
	    			"invoice_item_product_stock_quantity",
	    			"g_invoice_item_delivered_quantity",
	    			"invoice_item_returned_quantity_stock",
	    			"g_invoice_item_remain_exigible_quantity"});
    	
    	
    	
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnAlignment("operations", Table.Align.LEFT);

    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnHeader("g_product", this.messages.get("application.common.table.column.article.label"));
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnAlignment("g_product", Table.Align.LEFT);

    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnHeader("ord_item_canceled_entering_production", this.messages.get("application.common.status.canceled"));
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnAlignment("ord_item_canceled_entering_production", Table.Align.RIGHT);
    	
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnHeader("ord_item_pending_to_production", this.messages.get("application.common.status.pending"));
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnAlignment("ord_item_pending_to_production", Table.Align.RIGHT);
  	
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnHeader("ord_item_in_progress_quantity", this.messages.get("application.common.status.in.progress"));
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnAlignment("ord_item_in_progress_quantity", Table.Align.RIGHT);
    	
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnHeader("invoice_item_total_exigible_quantity", this.messages.get(this.VIEW_NAME + "tab.sale.invoice.product.deliverables.table.sale.invoice.item.product.deliverables.column.invoice.item.total.exigible.quantity"));
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnAlignment("invoice_item_total_exigible_quantity", Table.Align.RIGHT);
    	
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnHeader("invoice_item_product_stock_quantity", this.messages.get(this.VIEW_NAME + "tab.sale.invoice.product.deliverables.table.sale.invoice.item.product.deliverables.column.invoice.item.stock.quantity"));
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnAlignment("invoice_item_product_stock_quantity", Table.Align.RIGHT);
    	
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnHeader("g_invoice_item_delivered_quantity", this.messages.get(this.VIEW_NAME + "tab.sale.invoice.product.deliverables.table.sale.invoice.item.product.deliverables.column.invoice.item.delivered.quantity"));
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnAlignment("g_invoice_item_delivered_quantity", Table.Align.RIGHT);

    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnHeader("invoice_item_returned_quantity_stock", this.messages.get("application.common.storage.returned.to.stock"));
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnAlignment("invoice_item_returned_quantity_stock", Table.Align.RIGHT);
    	
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnHeader("g_invoice_item_remain_exigible_quantity", this.messages.get(this.VIEW_NAME + "tab.sale.invoice.product.deliverables.table.sale.invoice.item.product.deliverables.column.invoice.item.remain.exigible.quantity"));
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnAlignment("g_invoice_item_remain_exigible_quantity", Table.Align.RIGHT);
    	
    	if(this.saleInvoiceItemProductDeliverablesDTOTableLayoutFunctions.
    			getSaleInvoiceProductDeliverablesDTO().getTransactionRealized() == null
    			&& this.saleInvoiceItemProductDeliverablesDTOTableLayoutFunctions.getEditFormMode()){
    		this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnHeader("g_deliver_quantity", this.messages.get(this.VIEW_NAME + "tab.sale.invoice.product.deliverables.table.sale.invoice.item.product.deliverables.column.deliver.quantity"));
    		this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnAlignment("g_deliver_quantity", Table.Align.RIGHT);
    	}
    	
    	
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setSizeFull();    	
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnExpandRatio("operations", 0.001f); 
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnExpandRatio("g_product", 0.0038f);
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnExpandRatio("ord_item_canceled_entering_production", 0.0009f);
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnExpandRatio("ord_item_pending_to_production", 0.0009f);
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnExpandRatio("ord_item_in_progress_quantity", 0.00089f);
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnExpandRatio("invoice_item_total_exigible_quantity", 0.001f);
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnExpandRatio("invoice_item_delivered_quantity", 0.0044f);
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnExpandRatio("invoice_item_product_stock_quantity", 0.00065f);
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnExpandRatio("g_invoice_item_remain_exigible_quantity", 0.00089f);
    	
    	if(this.saleInvoiceItemProductDeliverablesDTOTableLayoutFunctions.
    			getSaleInvoiceProductDeliverablesDTO().getTransactionRealized() == null
    			&& this.saleInvoiceItemProductDeliverablesDTOTableLayoutFunctions.getEditFormMode())
    		this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnExpandRatio("g_deliver_quantity", 0.0021f);
    	
    	
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setSelectable(true);
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnCollapsingAllowed(true);
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnCollapsible("operations", false);
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnCollapsible("g_product", true);
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnCollapsible("g_invoice_item_remain_exigible_quantity", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.tableListSaleInvoiceItemProductDeliverablesDTO.setSortContainerPropertyId("measurment_unit_id");
    	//this.tableListSaleInvoiceItemProductDeliverablesDTO.setSortAscending(false);
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnReorderingAllowed(true);
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setFooterVisible(true);
    	this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");   	
	}
	
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    if (defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.tableListSaleInvoiceItemProductDeliverablesDTO.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.tableListSaleInvoiceItemProductDeliverablesDTO.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
	
	private HorizontalLayout buildOperationsButtonPanel(final SaleInvoiceItemProductDeliverablesDTO vSaleInvoiceItemProductDeliverablesDTO){
		
		final Button editQuantityButton = new Button();
		editQuantityButton.addStyleName("borderless");
		editQuantityButton.setIcon(FontAwesome.EDIT);		
		editQuantityButton.setVisible(true);		
		editQuantityButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.product.deliverables.table.sale.invoice.item.product.deliverables.column.operations.button.set.deliver.quantity.description"));
		editQuantityButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {                
                try{		                	
                	logger.info("\n edit quantity vSaleInvoiceItemProductDeliverablesDTO...\n" + vSaleInvoiceItemProductDeliverablesDTO.toString());
                	saleInvoiceItemProductDeliverablesDTOTableLayoutFunctions.setRequestDeliverQuantity(vSaleInvoiceItemProductDeliverablesDTO);                	
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });	
		editQuantityButton.setEnabled(
				vSaleInvoiceItemProductDeliverablesDTO.getInvoice_item_product_stock_quantity() > 0
				&& vSaleInvoiceItemProductDeliverablesDTO.getInvoice_item_remain_exigible_quantity() > 0
				&& vSaleInvoiceItemProductDeliverablesDTO.getListSIItemPDMProductInstanceInvolvedDTO() == null);
		
		final Button queryListSIItemPDMProductInstanceInvolvedDTOButton = new Button();
		queryListSIItemPDMProductInstanceInvolvedDTOButton.addStyleName("borderless");
		queryListSIItemPDMProductInstanceInvolvedDTOButton.setIcon(FontAwesome.SEARCH);		
		queryListSIItemPDMProductInstanceInvolvedDTOButton.setVisible(true);		
		queryListSIItemPDMProductInstanceInvolvedDTOButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.product.deliverables.table.sale.invoice.item.product.deliverables.column.operations.button.query.product.unique.instance.number.list.description"));
		queryListSIItemPDMProductInstanceInvolvedDTOButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {                
                try{		                	
                	logger.info("\n queryListSIItemPDMProductInstanceInvolvedDTOButton vSaleInvoiceItemProductDeliverablesDTO...\n" + vSaleInvoiceItemProductDeliverablesDTO.toString());
                	saleInvoiceItemProductDeliverablesDTOTableLayoutFunctions.queryListSIItemPDMProductInstanceInvolvedDTO(vSaleInvoiceItemProductDeliverablesDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });	
		queryListSIItemPDMProductInstanceInvolvedDTOButton.setVisible(vSaleInvoiceItemProductDeliverablesDTO.getListSIItemPDMProductInstanceInvolvedDTO()!=null
				&& !vSaleInvoiceItemProductDeliverablesDTO.getListSIItemPDMProductInstanceInvolvedDTO().isEmpty());
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(editQuantityButton,queryListSIItemPDMProductInstanceInvolvedDTOButton);
		return operationsButtonPanel;
	}
}
