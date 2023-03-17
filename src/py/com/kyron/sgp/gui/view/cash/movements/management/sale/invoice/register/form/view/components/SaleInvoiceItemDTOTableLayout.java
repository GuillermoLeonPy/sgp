package py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.register.form.view.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceItemDTO;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.utils.TableNumericColumnCellLabelHelper;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SaleInvoiceItemDTOTableLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(SaleInvoiceItemDTOTableLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_unit_price_amount","g_total_amount"};
    private CommonExceptionErrorNotification commonExceptionErrorNotification;
    private final List<SaleInvoiceItemDTO> listSaleInvoiceItemDTO;
    private Table listSaleInvoiceItemDTOTable;
    private BussinesSessionUtils bussinesSessionUtils;
    private final SaleInvoiceItemDTOTableFunctions saleInvoiceItemDTOTableFunctions;
    private int discardedSaleInvoiceItemsCount;
    
	public SaleInvoiceItemDTOTableLayout(
			final String VIEW_NAME,
			SaleInvoiceItemDTOTableFunctions saleInvoiceItemDTOTableFunctions,
			final List<SaleInvoiceItemDTO> listSaleInvoiceItemDTO) {
		// TODO Auto-generated constructor stub
		this.VIEW_NAME = VIEW_NAME;
		this.saleInvoiceItemDTOTableFunctions = saleInvoiceItemDTOTableFunctions;
		this.listSaleInvoiceItemDTO = listSaleInvoiceItemDTO;
		try{
			logger.info("\n SaleInvoiceItemDTOTableLayout()...");
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.initServices();
			this.setSizeFull();
	        //this.addStyleName("transactions");
	        this.setId("SaleInvoiceItemDTOTableLayout");
			this.setSpacing(true);
			this.setMargin(true);
	        Responsive.makeResponsive(this);
	        DashboardEventBus.register(this);
	    	int listSize = (this.listSaleInvoiceItemDTO!=null ? this.listSaleInvoiceItemDTO.size() : 0);
	    	this.setHeight(150f + (listSize*45), Unit.PIXELS);
	        this.buildlistSaleInvoiceItemDTOTable();
	        this.addComponent(this.listSaleInvoiceItemDTOTable);
	        this.setExpandRatio(this.listSaleInvoiceItemDTOTable, 1);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public SaleInvoiceItemDTOTableLayout(Component... children) {
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
	
	private void buildlistSaleInvoiceItemDTOTable() throws PmsServiceException{
    	this.listSaleInvoiceItemDTOTable = new Table();
    	BeanItemContainer<SaleInvoiceItemDTO> SaleInvoiceItemDTOBeanItemContainer	= new BeanItemContainer<SaleInvoiceItemDTO>(SaleInvoiceItemDTO.class);
    	if(this.listSaleInvoiceItemDTO == null)SaleInvoiceItemDTOBeanItemContainer.addAll(new ArrayList<SaleInvoiceItemDTO>());
    	else SaleInvoiceItemDTOBeanItemContainer.addAll(this.listSaleInvoiceItemDTO);    	
    	this.listSaleInvoiceItemDTOTable.setContainerDataSource(SaleInvoiceItemDTOBeanItemContainer);     	
    	this.listSaleInvoiceItemDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceItemDTO vSaleInvoiceItemDTO = (SaleInvoiceItemDTO)itemId;
				return buildOperationsButtonPanel(vSaleInvoiceItemDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listSaleInvoiceItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listSaleInvoiceItemDTOTable.addGeneratedColumn("g_product", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceItemDTO vSaleInvoiceItemDTO = (SaleInvoiceItemDTO)itemId;					
				return vSaleInvoiceItemDTO.getProductDTO().getProduct_id();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listSaleInvoiceItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listSaleInvoiceItemDTOTable.addGeneratedColumn("g_unit_price_amount", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceItemDTO vSaleInvoiceItemDTO = (SaleInvoiceItemDTO)itemId;
				HorizontalLayout vHorizontalLayout = 
				new HorizontalLayout(TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vSaleInvoiceItemDTO.getUnit_price_amount()));
				vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(0), Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listSaleInvoiceItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listSaleInvoiceItemDTOTable.addGeneratedColumn("g_total_amount", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceItemDTO vSaleInvoiceItemDTO = (SaleInvoiceItemDTO)itemId;
				return TableNumericColumnCellLabelHelper.buildValueAddedTaxAmountLabel(vSaleInvoiceItemDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listSaleInvoiceItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listSaleInvoiceItemDTOTable.setVisibleColumns(new Object[] 
    			{"operations",
    			"g_product",
    			"quantity",
    			"g_unit_price_amount",
    			"g_total_amount"});
    	this.listSaleInvoiceItemDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.listSaleInvoiceItemDTOTable.setColumnAlignment("operations", Table.Align.LEFT);

    	this.listSaleInvoiceItemDTOTable.setColumnHeader("g_product", this.messages.get("application.common.table.column.article.label"));
    	this.listSaleInvoiceItemDTOTable.setColumnAlignment("g_product", Table.Align.LEFT);
    	
    	this.listSaleInvoiceItemDTOTable.setColumnHeader("quantity", this.messages.get("application.common.table.column.any.amount.label"));
    	this.listSaleInvoiceItemDTOTable.setColumnAlignment("quantity", Table.Align.RIGHT);
  	
    	this.listSaleInvoiceItemDTOTable.setColumnHeader("g_unit_price_amount", this.messages.get("application.common.table.column.unit.price.amount.label"));
    	this.listSaleInvoiceItemDTOTable.setColumnAlignment("g_unit_price_amount", Table.Align.RIGHT);
    	
    	this.listSaleInvoiceItemDTOTable.setColumnHeader("g_total_amount", this.messages.get("application.common.table.column.total.amount.label"));
    	this.listSaleInvoiceItemDTOTable.setColumnAlignment("g_total_amount", Table.Align.RIGHT);
    	
    	this.listSaleInvoiceItemDTOTable.setSizeFull();    	
    	this.listSaleInvoiceItemDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.listSaleInvoiceItemDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.listSaleInvoiceItemDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.listSaleInvoiceItemDTOTable.setColumnExpandRatio("operations", 0.0058f); 
    	this.listSaleInvoiceItemDTOTable.setColumnExpandRatio("g_product", 0.025f);    	
    	this.listSaleInvoiceItemDTOTable.setColumnExpandRatio("quantity", 0.0038f);
    	this.listSaleInvoiceItemDTOTable.setColumnExpandRatio("g_unit_price_amount", 0.0055f);
    	this.listSaleInvoiceItemDTOTable.setColumnExpandRatio("g_total_amount", 0.0098f);
    	this.listSaleInvoiceItemDTOTable.setSelectable(true);
    	this.listSaleInvoiceItemDTOTable.setColumnCollapsingAllowed(true);
    	this.listSaleInvoiceItemDTOTable.setColumnCollapsible("operations", false);
    	this.listSaleInvoiceItemDTOTable.setColumnCollapsible("g_unit_price_amount", true);
    	this.listSaleInvoiceItemDTOTable.setColumnCollapsible("g_total_amount", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.listSaleInvoiceItemDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.listSaleInvoiceItemDTOTable.setSortAscending(false);
    	this.listSaleInvoiceItemDTOTable.setColumnReorderingAllowed(true);
    	this.listSaleInvoiceItemDTOTable.setFooterVisible(true);
    	this.listSaleInvoiceItemDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");   	
	}

	private HorizontalLayout buildOperationsButtonPanel(final SaleInvoiceItemDTO vSaleInvoiceItemDTO){
		
		final Button itemIsDiscardedIndicatorButton = new Button();
		itemIsDiscardedIndicatorButton.addStyleName("borderless");
		itemIsDiscardedIndicatorButton.setIcon(FontAwesome.WARNING);
		itemIsDiscardedIndicatorButton.setResponsive(false);
		itemIsDiscardedIndicatorButton.setDescription(messages.get("application.common.status.discarded"));
		itemIsDiscardedIndicatorButton.setVisible(vSaleInvoiceItemDTO.getStatus().equals("application.common.status.discarded"));		
		
		
		final Button editQuantityButton = new Button();
		editQuantityButton.addStyleName("borderless");
		editQuantityButton.setIcon(FontAwesome.EDIT);		
		editQuantityButton.setVisible(true);		
		editQuantityButton.setDescription(messages.get("application.common.table.column.operations.edit") + " " + messages.get("application.common.quantity.label").toLowerCase());
		editQuantityButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {                
                try{		                	
                	logger.info("\n edit quantity vSaleInvoiceItemDTO...\n" + vSaleInvoiceItemDTO.toString());
                	saleInvoiceItemDTOTableFunctions.editQuantity(vSaleInvoiceItemDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		editQuantityButton.setEnabled(saleInvoiceItemDTOTableFunctions.getSaleInvoiceDTOStatus().equals("application.common.status.revision")
				&& !vSaleInvoiceItemDTO.getStatus().equals("application.common.status.discarded"));
		editQuantityButton.setVisible(false
				/*bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
				.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "edit.revision.status.sale.invoice")*/);
		

		Boolean vBoolean = new Boolean(vSaleInvoiceItemDTO.getStatus().equals("application.common.status.discarded") ? true : false);
		final ObjectProperty<Boolean> property =	new ObjectProperty<Boolean>(vBoolean);
		CheckBox vCheckBox = new CheckBox(this.messages.get("application.common.operation.discard.label"), property);
		vCheckBox.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				Boolean vBoolean = (Boolean)event.getProperty().getValue();
				logger.info( "\n=========================="
							+"\n discard order item check : " + vBoolean	
							+"\n==========================");
				 
				if(vBoolean){
					vSaleInvoiceItemDTO.setPrevious_status(vSaleInvoiceItemDTO.getStatus());
					vSaleInvoiceItemDTO.setStatus("application.common.status.discarded");
				}
				else {
					vSaleInvoiceItemDTO.setPrevious_status(null);
					vSaleInvoiceItemDTO.setStatus(vSaleInvoiceItemDTO.getPrevious_status());
				}
				saleInvoiceItemDTOTableFunctions.reBuildTableAndTotalsPanel();//to recalculate table totals
			}			
		});
		
		//vCheckBox.setEnabled(!vSaleInvoiceItemDTO.getStatus().equals("application.common.status.discarded") && vSaleInvoiceItemDTO.getPrevious_status() == null);
		vCheckBox.setEnabled(this.checkForEnableDiscardSaleInvoiceItem());
		vCheckBox.setVisible(false
				/*bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
				.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "edit.revision.status.sale.invoice")
				&& this.saleInvoiceItemDTOTableFunctions.getSaleInvoiceDTOStatus().equalsIgnoreCase("application.common.status.revision")*/);
			
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(itemIsDiscardedIndicatorButton,editQuantityButton,vCheckBox);
		return operationsButtonPanel;
	}
	
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    if (defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.listSaleInvoiceItemDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.listSaleInvoiceItemDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
	
	private boolean checkForEnableDiscardSaleInvoiceItem(){
		if(this.listSaleInvoiceItemDTO.size() == 1)	return false;
		this.discardedSaleInvoiceItemsCount = 0;
		for(SaleInvoiceItemDTO vSaleInvoiceItemDTO: this.listSaleInvoiceItemDTO){
			if(vSaleInvoiceItemDTO.getStatus().equals("application.common.status.discarded"))this.discardedSaleInvoiceItemsCount++;
		}
		if((this.listSaleInvoiceItemDTO.size() - this.discardedSaleInvoiceItemsCount) < 2)return false;
		
		return true;
	}
}
