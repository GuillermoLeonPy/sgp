package py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceCreditNoteItemDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.utils.TableNumericColumnCellLabelHelper;

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
public class PurchaseInvoiceCreditNoteItemDTOTableLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(PurchaseInvoiceCreditNoteItemDTOTableLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_unit_price_amount","g_total_amount"};
    private CommonExceptionErrorNotification commonExceptionErrorNotification;
    private final List<PurchaseInvoiceCreditNoteItemDTO> listPurchaseInvoiceCreditNoteItemDTO;
    private final PurchaseInvoiceCreditNoteItemDTOTableLayoutFunctions purchaseInvoiceCreditNoteItemDTOTableLayoutFunctions;
    private Table listPurchaseInvoiceCreditNoteItemDTOTable;
    private BussinesSessionUtils bussinesSessionUtils;
    private int discardedOrderItemsCount;
    
	public PurchaseInvoiceCreditNoteItemDTOTableLayout(
			final PurchaseInvoiceCreditNoteItemDTOTableLayoutFunctions purchaseInvoiceCreditNoteItemDTOTableLayoutFunctions,
			final String VIEW_NAME,
			final List<PurchaseInvoiceCreditNoteItemDTO> listPurchaseInvoiceCreditNoteItemDTO) {
		// TODO Auto-generated constructor stub
		this.purchaseInvoiceCreditNoteItemDTOTableLayoutFunctions = purchaseInvoiceCreditNoteItemDTOTableLayoutFunctions;
		this.VIEW_NAME = VIEW_NAME;
		this.listPurchaseInvoiceCreditNoteItemDTO = listPurchaseInvoiceCreditNoteItemDTO;
		try{
			logger.info("\n PurchaseInvoiceCreditNoteItemDTOTableLayout()...");
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.initServices();
			this.setSizeFull();
	        //this.addStyleName("transactions");
	        this.setId("vPurchaseInvoiceCreditNoteItemDTOTableLayout");
			this.setSpacing(true);
			this.setMargin(true);
	        Responsive.makeResponsive(this);
	        DashboardEventBus.register(this);
	    	int listSize = (this.listPurchaseInvoiceCreditNoteItemDTO!=null ? this.listPurchaseInvoiceCreditNoteItemDTO.size() : 0);
	    	this.setHeight(150f + (listSize*45), Unit.PIXELS);
	        this.buildListPurchaseInvoiceCreditNoteItemDTOTable();
	        this.addComponent(this.listPurchaseInvoiceCreditNoteItemDTOTable);
	        this.setExpandRatio(this.listPurchaseInvoiceCreditNoteItemDTOTable, 1);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public PurchaseInvoiceCreditNoteItemDTOTableLayout(Component... children) {
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
	
	private void buildListPurchaseInvoiceCreditNoteItemDTOTable(){
    	this.listPurchaseInvoiceCreditNoteItemDTOTable = new Table();
    	BeanItemContainer<PurchaseInvoiceCreditNoteItemDTO> PurchaseInvoiceCreditNoteItemDTOBeanItemContainer	= new BeanItemContainer<PurchaseInvoiceCreditNoteItemDTO>(PurchaseInvoiceCreditNoteItemDTO.class);
    	if(this.listPurchaseInvoiceCreditNoteItemDTO == null)PurchaseInvoiceCreditNoteItemDTOBeanItemContainer.addAll(new ArrayList<PurchaseInvoiceCreditNoteItemDTO>());
    	else PurchaseInvoiceCreditNoteItemDTOBeanItemContainer.addAll(this.listPurchaseInvoiceCreditNoteItemDTO);    	
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setContainerDataSource(PurchaseInvoiceCreditNoteItemDTOBeanItemContainer);     	
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO = (PurchaseInvoiceCreditNoteItemDTO)itemId;
				return buildOperationsButtonPanel(vPurchaseInvoiceCreditNoteItemDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listPurchaseInvoiceCreditNoteItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.addGeneratedColumn("g_raw_material_id", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO = (PurchaseInvoiceCreditNoteItemDTO)itemId;		
				final Label title = new Label(vPurchaseInvoiceCreditNoteItemDTO.getRawMaterialDTO().getRaw_material_id());
				title.addStyleName(ValoTheme.LABEL_COLORED);
				return title;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listPurchaseInvoiceCreditNoteItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.addGeneratedColumn("g_measurment_unit_id", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO = (PurchaseInvoiceCreditNoteItemDTO)itemId;
				final Label title = new Label(vPurchaseInvoiceCreditNoteItemDTO.getMeasurmentUnitDTO().getMeasurment_unit_id());
				title.addStyleName(ValoTheme.LABEL_COLORED);
				return title;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listPurchaseInvoiceCreditNoteItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.addGeneratedColumn("g_quantity", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO = (PurchaseInvoiceCreditNoteItemDTO)itemId;
				if(vPurchaseInvoiceCreditNoteItemDTO.getQuantity() == null){
					final Label label = new Label(messages.get(VIEW_NAME + "tab.purchase.invoice.credit.note.form.table.purchase.invoice.credit.note.item.column.quantity.not.established"));				
					label.addStyleName("colored");
					return label;
				}else{
					final ObjectProperty<BigDecimal> property = new ObjectProperty<BigDecimal>(vPurchaseInvoiceCreditNoteItemDTO.getQuantity());
					final Label label = new Label(property);				
					label.addStyleName("colored");
					final HorizontalLayout vHorizontalLayout = new HorizontalLayout(label);
					vHorizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
					return vHorizontalLayout;
				}
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listPurchaseInvoiceCreditNoteItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.addGeneratedColumn("g_unit_price_amount", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO = (PurchaseInvoiceCreditNoteItemDTO)itemId;
				HorizontalLayout vHorizontalLayout = 
				new HorizontalLayout(TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vPurchaseInvoiceCreditNoteItemDTO.getUnit_price_amount()));
				vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(0), Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listPurchaseInvoiceCreditNoteItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.addGeneratedColumn("g_total_amount", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO = (PurchaseInvoiceCreditNoteItemDTO)itemId;
				return TableNumericColumnCellLabelHelper.buildValueAddedTaxAmountLabel(vPurchaseInvoiceCreditNoteItemDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listPurchaseInvoiceCreditNoteItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setVisibleColumns(new Object[] 
    			{"operations",
    			"g_raw_material_id",
    			"g_measurment_unit_id",
    			"g_quantity",
    			"g_unit_price_amount",
    			"g_total_amount"});
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnAlignment("operations", Table.Align.LEFT);

    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnHeader("g_raw_material_id", this.messages.get("application.common.rawmaterialid.label"));
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnAlignment("g_raw_material_id", Table.Align.LEFT);

    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnHeader("g_measurment_unit_id", this.messages.get("application.common.measurmentunitid.label"));
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnAlignment("g_measurment_unit_id", Table.Align.LEFT);
    	
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnHeader("g_quantity", this.messages.get("application.common.table.column.any.amount.label"));
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnAlignment("g_quantity", Table.Align.RIGHT);
  	
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnHeader("g_unit_price_amount", this.messages.get("application.common.table.column.unit.price.amount.label"));
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnAlignment("g_unit_price_amount", Table.Align.RIGHT);
    	
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnHeader("g_total_amount", this.messages.get("application.common.table.column.total.amount.label"));
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnAlignment("g_total_amount", Table.Align.RIGHT);
    	
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setSizeFull();    	
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnExpandRatio("operations", 0.0058f); 
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnExpandRatio("g_raw_material_id", 0.025f);
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnExpandRatio("g_measurment_unit_id", 0.011f);
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnExpandRatio("g_quantity", 0.0038f);
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnExpandRatio("g_unit_price_amount", 0.0085f);
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnExpandRatio("g_total_amount", 0.0098f);
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setSelectable(true);
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnCollapsingAllowed(true);
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnCollapsible("operations", false);
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnCollapsible("g_unit_price_amount", true);
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnCollapsible("g_total_amount", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.listPurchaseInvoiceCreditNoteItemDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.listPurchaseInvoiceCreditNoteItemDTOTable.setSortAscending(false);
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnReorderingAllowed(true);
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setFooterVisible(true);
    	this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}

	private HorizontalLayout buildOperationsButtonPanel(final PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO){
		final Button deleteButton = new Button();
		deleteButton.addStyleName("borderless");
		deleteButton.setIcon(FontAwesome.REMOVE);		
		deleteButton.setVisible(true);		
		deleteButton.setDescription(messages.get("application.common.operation.delete.label"));
		deleteButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {                
                try{		                	
                	logger.info("\n delete vPurchaseInvoiceCreditNoteItemDTO...\n" + vPurchaseInvoiceCreditNoteItemDTO.toString());
                	purchaseInvoiceCreditNoteItemDTOTableLayoutFunctions.deletePurchaseInvoiceCreditNoteItemDTOFromPreliminaryList(vPurchaseInvoiceCreditNoteItemDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		deleteButton.setEnabled( 
				/* as long as update credit note not implemented*/
				this.purchaseInvoiceCreditNoteItemDTOTableLayoutFunctions.getPurchaseInvoiceCreditNoteDTOId() == null
				&& this.listPurchaseInvoiceCreditNoteItemDTO.size() > 1
				/*&& this.purchaseInvoiceCreditNoteItemDTOTableLayoutFunctions.getPurchaseInvoiceCreditNoteDTOStatus().equalsIgnoreCase("application.common.status.pending")*/);
		
		final Button editQuantityButton = new Button();
		editQuantityButton.addStyleName("borderless");
		editQuantityButton.setIcon(FontAwesome.REPLY_ALL);		
		editQuantityButton.setVisible(true);		
		editQuantityButton.setDescription(messages.get("application.common.table.column.operations.edit") + " " + messages.get("application.common.quantity.label"));
		editQuantityButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {                
                try{		                	
                	logger.info("\n edit editQuantityButton vPurchaseInvoiceCreditNoteItemDTO...\n" + vPurchaseInvoiceCreditNoteItemDTO.toString());
                	purchaseInvoiceCreditNoteItemDTOTableLayoutFunctions.editQuantity(vPurchaseInvoiceCreditNoteItemDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		editQuantityButton.setEnabled(true);
		
		final HorizontalLayout operationsButtonPanel = 
				new HorizontalLayout(deleteButton,editQuantityButton);
		return operationsButtonPanel;
	}
	
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    if (defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.listPurchaseInvoiceCreditNoteItemDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.listPurchaseInvoiceCreditNoteItemDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
}
