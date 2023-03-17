package py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceItemDTO;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.utils.TableNumericColumnCellLabelHelper;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
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
public class PurchaseInvoiceItemDTOTableLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(PurchaseInvoiceItemDTOTableLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_unit_price_amount","g_total_amount"};
    private CommonExceptionErrorNotification commonExceptionErrorNotification;
    private final List<PurchaseInvoiceItemDTO> listPurchaseInvoiceItemDTO;
    private final PurchaseInvoiceItemDTOTableFunctions purchaseInvoiceItemDTOTableFunctions;
    private Table listPurchaseInvoiceItemDTOTable;
    private BussinesSessionUtils bussinesSessionUtils;
    private int discardedOrderItemsCount;
    
    
	public PurchaseInvoiceItemDTOTableLayout(
			final String VIEW_NAME,
			final PurchaseInvoiceItemDTOTableFunctions purchaseInvoiceItemDTOTableFunctions,
			final List<PurchaseInvoiceItemDTO> listPurchaseInvoiceItemDTO) {
		// TODO Auto-generated constructor stub
		this.VIEW_NAME = VIEW_NAME;
		this.purchaseInvoiceItemDTOTableFunctions = purchaseInvoiceItemDTOTableFunctions;
		this.listPurchaseInvoiceItemDTO = listPurchaseInvoiceItemDTO;
		try{
			logger.info("\n PurchaseInvoiceItemDTOTableLayout()...");
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.initServices();
			this.setSizeFull();
	        //this.addStyleName("transactions");
	        this.setId("PurchaseInvoiceItemDTOTableLayout");
			this.setSpacing(true);
			this.setMargin(true);
	        Responsive.makeResponsive(this);
	        DashboardEventBus.register(this);
	    	int listSize = (this.listPurchaseInvoiceItemDTO!=null ? this.listPurchaseInvoiceItemDTO.size() : 0);
	    	this.setHeight(150f + (listSize*45), Unit.PIXELS);
	        this.buildListPurchaseInvoiceItemDTOTable();
	        this.addComponent(this.listPurchaseInvoiceItemDTOTable);
	        this.setExpandRatio(this.listPurchaseInvoiceItemDTOTable, 1);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public PurchaseInvoiceItemDTOTableLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	
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
	
	private void buildListPurchaseInvoiceItemDTOTable() throws PmsServiceException{
    	this.listPurchaseInvoiceItemDTOTable = new Table();
    	BeanItemContainer<PurchaseInvoiceItemDTO> PurchaseInvoiceItemDTOBeanItemContainer	= new BeanItemContainer<PurchaseInvoiceItemDTO>(PurchaseInvoiceItemDTO.class);
    	if(this.listPurchaseInvoiceItemDTO == null)PurchaseInvoiceItemDTOBeanItemContainer.addAll(new ArrayList<PurchaseInvoiceItemDTO>());
    	else PurchaseInvoiceItemDTOBeanItemContainer.addAll(this.listPurchaseInvoiceItemDTO);    	
    	this.listPurchaseInvoiceItemDTOTable.setContainerDataSource(PurchaseInvoiceItemDTOBeanItemContainer);     	
    	this.listPurchaseInvoiceItemDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceItemDTO vPurchaseInvoiceItemDTO = (PurchaseInvoiceItemDTO)itemId;
				return buildOperationsButtonPanel(vPurchaseInvoiceItemDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listPurchaseInvoiceItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listPurchaseInvoiceItemDTOTable.addGeneratedColumn("g_raw_material_id", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceItemDTO vPurchaseInvoiceItemDTO = (PurchaseInvoiceItemDTO)itemId;		
				final Label title = new Label(vPurchaseInvoiceItemDTO.getRawMaterialDTO().getRaw_material_id());
				title.addStyleName(ValoTheme.LABEL_COLORED);
				return title;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listPurchaseInvoiceItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listPurchaseInvoiceItemDTOTable.addGeneratedColumn("g_measurment_unit_id", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceItemDTO vPurchaseInvoiceItemDTO = (PurchaseInvoiceItemDTO)itemId;
				final Label title = new Label(vPurchaseInvoiceItemDTO.getMeasurmentUnitDTO().getMeasurment_unit_id());
				title.addStyleName(ValoTheme.LABEL_COLORED);
				return title;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listPurchaseInvoiceItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listPurchaseInvoiceItemDTOTable.addGeneratedColumn("g_unit_price_amount", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceItemDTO vPurchaseInvoiceItemDTO = (PurchaseInvoiceItemDTO)itemId;
				HorizontalLayout vHorizontalLayout = 
				new HorizontalLayout(TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vPurchaseInvoiceItemDTO.getUnit_price_amount()));
				vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(0), Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listPurchaseInvoiceItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listPurchaseInvoiceItemDTOTable.addGeneratedColumn("g_total_amount", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceItemDTO vPurchaseInvoiceItemDTO = (PurchaseInvoiceItemDTO)itemId;
				return TableNumericColumnCellLabelHelper.buildValueAddedTaxAmountLabel(vPurchaseInvoiceItemDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listPurchaseInvoiceItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listPurchaseInvoiceItemDTOTable.setVisibleColumns(new Object[] 
    			{"operations",
    			"g_raw_material_id",
    			"g_measurment_unit_id",
    			"quantity",
    			"g_unit_price_amount",
    			"g_total_amount"});
    	this.listPurchaseInvoiceItemDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.listPurchaseInvoiceItemDTOTable.setColumnAlignment("operations", Table.Align.LEFT);

    	this.listPurchaseInvoiceItemDTOTable.setColumnHeader("g_raw_material_id", this.messages.get("application.common.rawmaterialid.label"));
    	this.listPurchaseInvoiceItemDTOTable.setColumnAlignment("g_raw_material_id", Table.Align.LEFT);

    	this.listPurchaseInvoiceItemDTOTable.setColumnHeader("g_measurment_unit_id", this.messages.get("application.common.measurmentunitid.label"));
    	this.listPurchaseInvoiceItemDTOTable.setColumnAlignment("g_measurment_unit_id", Table.Align.LEFT);
    	
    	this.listPurchaseInvoiceItemDTOTable.setColumnHeader("quantity", this.messages.get("application.common.table.column.any.amount.label"));
    	this.listPurchaseInvoiceItemDTOTable.setColumnAlignment("quantity", Table.Align.RIGHT);
  	
    	this.listPurchaseInvoiceItemDTOTable.setColumnHeader("g_unit_price_amount", this.messages.get("application.common.table.column.unit.price.amount.label"));
    	this.listPurchaseInvoiceItemDTOTable.setColumnAlignment("g_unit_price_amount", Table.Align.RIGHT);
    	
    	this.listPurchaseInvoiceItemDTOTable.setColumnHeader("g_total_amount", this.messages.get("application.common.table.column.total.amount.label"));
    	this.listPurchaseInvoiceItemDTOTable.setColumnAlignment("g_total_amount", Table.Align.RIGHT);
    	
    	this.listPurchaseInvoiceItemDTOTable.setSizeFull();    	
    	this.listPurchaseInvoiceItemDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.listPurchaseInvoiceItemDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.listPurchaseInvoiceItemDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.listPurchaseInvoiceItemDTOTable.setColumnExpandRatio("operations", 0.0058f); 
    	this.listPurchaseInvoiceItemDTOTable.setColumnExpandRatio("g_raw_material_id", 0.025f);
    	this.listPurchaseInvoiceItemDTOTable.setColumnExpandRatio("g_measurment_unit_id", 0.011f);
    	this.listPurchaseInvoiceItemDTOTable.setColumnExpandRatio("quantity", 0.0038f);
    	this.listPurchaseInvoiceItemDTOTable.setColumnExpandRatio("g_unit_price_amount", 0.0085f);
    	this.listPurchaseInvoiceItemDTOTable.setColumnExpandRatio("g_total_amount", 0.0098f);
    	this.listPurchaseInvoiceItemDTOTable.setSelectable(true);
    	this.listPurchaseInvoiceItemDTOTable.setColumnCollapsingAllowed(true);
    	this.listPurchaseInvoiceItemDTOTable.setColumnCollapsible("operations", false);
    	this.listPurchaseInvoiceItemDTOTable.setColumnCollapsible("g_unit_price_amount", true);
    	this.listPurchaseInvoiceItemDTOTable.setColumnCollapsible("g_total_amount", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.listPurchaseInvoiceItemDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.listPurchaseInvoiceItemDTOTable.setSortAscending(false);
    	this.listPurchaseInvoiceItemDTOTable.setColumnReorderingAllowed(true);
    	this.listPurchaseInvoiceItemDTOTable.setFooterVisible(true);
    	this.listPurchaseInvoiceItemDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");   	
	}
	
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    if (defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.listPurchaseInvoiceItemDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.listPurchaseInvoiceItemDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible(
	
	private HorizontalLayout buildOperationsButtonPanel(final PurchaseInvoiceItemDTO vPurchaseInvoiceItemDTO){
		final Button deleteButton = new Button();
		deleteButton.addStyleName("borderless");
		deleteButton.setIcon(FontAwesome.REMOVE);		
		deleteButton.setVisible(true);		
		deleteButton.setDescription(messages.get("application.common.operation.delete.label"));
		deleteButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {                
                try{		                	
                	logger.info("\n delete vPurchaseInvoiceItemDTO...\n" + vPurchaseInvoiceItemDTO.toString());
                	purchaseInvoiceItemDTOTableFunctions.deletePurchaseInvoiceItemDTOFromPreliminaryList(vPurchaseInvoiceItemDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		deleteButton.setEnabled(
				this.purchaseInvoiceItemDTOTableFunctions.getPurchaseInvoiceDTOStatus() == null ||
				(this.purchaseInvoiceItemDTOTableFunctions.getPurchaseInvoiceDTOStatus()!=null && 
				this.purchaseInvoiceItemDTOTableFunctions.getPurchaseInvoiceDTOStatus().equalsIgnoreCase("application.common.status.pending")));
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(deleteButton);
		return operationsButtonPanel;
	}
}
