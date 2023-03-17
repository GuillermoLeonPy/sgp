package py.com.kyron.sgp.gui.view.comercialmanagement.orderregisterformview.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderItemDTO;
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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class OrderItemDTOTableLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(OrderItemDTOTableLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "order.management.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_unit_price_amount","g_total_amount"};
    private CommonExceptionErrorNotification commonExceptionErrorNotification;
    private final List<OrderItemDTO> listOrderItemDTO;
    private final OrderItemDTOTableFunctions orderItemDTOTableFunctions;
    private Table listOrderItemDTOTable;
    private BussinesSessionUtils bussinesSessionUtils;
    private int discardedOrderItemsCount;
    
	public OrderItemDTOTableLayout(final OrderItemDTOTableFunctions orderItemDTOTableFunctions,final List<OrderItemDTO> listOrderItemDTO) {
		// TODO Auto-generated constructor stub
		this.orderItemDTOTableFunctions = orderItemDTOTableFunctions;
		this.listOrderItemDTO = listOrderItemDTO;		
		try{
			logger.info("\n OrderItemDTOTableLayout()...");
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.initServices();
			this.setSizeFull();
	        //this.addStyleName("transactions");
	        this.setId("OrderItemDTOTableLayout");
			this.setSpacing(true);
			this.setMargin(true);
	        Responsive.makeResponsive(this);
	        DashboardEventBus.register(this);
	    	int listSize = (this.listOrderItemDTO!=null ? this.listOrderItemDTO.size() : 0);
	    	this.setHeight(150f + (listSize*45), Unit.PIXELS);
	        this.buildListOrderItemDTOTable();
	        this.addComponent(this.listOrderItemDTOTable);
	        this.setExpandRatio(this.listOrderItemDTOTable, 1);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public OrderItemDTOTableLayout(Component... children) {
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
	
	private void buildListOrderItemDTOTable() throws PmsServiceException{
    	this.listOrderItemDTOTable = new Table();
    	BeanItemContainer<OrderItemDTO> OrderItemDTOBeanItemContainer	= new BeanItemContainer<OrderItemDTO>(OrderItemDTO.class);
    	if(this.listOrderItemDTO == null)OrderItemDTOBeanItemContainer.addAll(new ArrayList<OrderItemDTO>());
    	else OrderItemDTOBeanItemContainer.addAll(this.listOrderItemDTO);    	
    	this.listOrderItemDTOTable.setContainerDataSource(OrderItemDTOBeanItemContainer);     	
    	this.listOrderItemDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final OrderItemDTO vOrderItemDTO = (OrderItemDTO)itemId;
				return buildOperationsButtonPanel(vOrderItemDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listOrderItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listOrderItemDTOTable.addGeneratedColumn("g_product", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final OrderItemDTO vOrderItemDTO = (OrderItemDTO)itemId;					
				return vOrderItemDTO.getProductDTO().getProduct_id();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listOrderItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listOrderItemDTOTable.addGeneratedColumn("g_unit_price_amount", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final OrderItemDTO vOrderItemDTO = (OrderItemDTO)itemId;
				HorizontalLayout vHorizontalLayout = 
				new HorizontalLayout(TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vOrderItemDTO.getUnit_price_amount()));
				vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(0), Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listOrderItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listOrderItemDTOTable.addGeneratedColumn("g_total_amount", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final OrderItemDTO vOrderItemDTO = (OrderItemDTO)itemId;
				return TableNumericColumnCellLabelHelper.buildValueAddedTaxAmountLabel(vOrderItemDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listOrderItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listOrderItemDTOTable.setVisibleColumns(new Object[] 
    			{"operations",
    			"g_product",
    			"quantity",
    			"g_unit_price_amount",
    			"g_total_amount"});
    	this.listOrderItemDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.listOrderItemDTOTable.setColumnAlignment("operations", Table.Align.LEFT);

    	this.listOrderItemDTOTable.setColumnHeader("g_product", this.messages.get("application.common.table.column.article.label"));
    	this.listOrderItemDTOTable.setColumnAlignment("g_product", Table.Align.LEFT);
    	
    	this.listOrderItemDTOTable.setColumnHeader("quantity", this.messages.get("application.common.table.column.any.amount.label"));
    	this.listOrderItemDTOTable.setColumnAlignment("quantity", Table.Align.RIGHT);
  	
    	this.listOrderItemDTOTable.setColumnHeader("g_unit_price_amount", this.messages.get("application.common.table.column.unit.price.amount.label"));
    	this.listOrderItemDTOTable.setColumnAlignment("g_unit_price_amount", Table.Align.RIGHT);
    	
    	this.listOrderItemDTOTable.setColumnHeader("g_total_amount", this.messages.get("application.common.table.column.total.amount.label"));
    	this.listOrderItemDTOTable.setColumnAlignment("g_total_amount", Table.Align.RIGHT);
    	
    	this.listOrderItemDTOTable.setSizeFull();    	
    	this.listOrderItemDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.listOrderItemDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.listOrderItemDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.listOrderItemDTOTable.setColumnExpandRatio("operations", 0.0058f); 
    	this.listOrderItemDTOTable.setColumnExpandRatio("g_product", 0.025f);    	
    	this.listOrderItemDTOTable.setColumnExpandRatio("quantity", 0.0038f);
    	this.listOrderItemDTOTable.setColumnExpandRatio("g_unit_price_amount", 0.0055f);
    	this.listOrderItemDTOTable.setColumnExpandRatio("g_total_amount", 0.0098f);
    	this.listOrderItemDTOTable.setSelectable(true);
    	this.listOrderItemDTOTable.setColumnCollapsingAllowed(true);
    	this.listOrderItemDTOTable.setColumnCollapsible("operations", false);
    	this.listOrderItemDTOTable.setColumnCollapsible("g_unit_price_amount", true);
    	this.listOrderItemDTOTable.setColumnCollapsible("g_total_amount", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.listOrderItemDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.listOrderItemDTOTable.setSortAscending(false);
    	this.listOrderItemDTOTable.setColumnReorderingAllowed(true);
    	this.listOrderItemDTOTable.setFooterVisible(true);
    	this.listOrderItemDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");   	
	}

	private HorizontalLayout buildOperationsButtonPanel(final OrderItemDTO vOrderItemDTO){
		final Button itemIsDiscardedIndicatorButton = new Button();
		itemIsDiscardedIndicatorButton.addStyleName("borderless");
		itemIsDiscardedIndicatorButton.setIcon(FontAwesome.WARNING);
		itemIsDiscardedIndicatorButton.setResponsive(false);
		itemIsDiscardedIndicatorButton.setDescription(messages.get("application.common.status.discarded"));
		itemIsDiscardedIndicatorButton.setVisible(vOrderItemDTO.getStatus()!=null && vOrderItemDTO.getStatus().equals("application.common.status.discarded"));	
		
		
		final Button deleteButton = new Button();
		deleteButton.addStyleName("borderless");
		deleteButton.setIcon(FontAwesome.REMOVE);		
		deleteButton.setVisible(true);		
		deleteButton.setDescription(messages.get("application.common.operation.delete.label"));
		deleteButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {                
                try{		                	
                	logger.info("\n delete vOrderItemDTO...\n" + vOrderItemDTO.toString());
                	orderItemDTOTableFunctions.deleteOrderItemFromPreliminaryList(vOrderItemDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		deleteButton.setEnabled(
				this.orderItemDTOTableFunctions.getOrderDTOStatus() == null ||
				(this.orderItemDTOTableFunctions.getOrderDTOStatus()!=null && 
				this.orderItemDTOTableFunctions.getOrderDTOStatus().equalsIgnoreCase("application.common.status.pending")));

		Boolean vBoolean = new Boolean(vOrderItemDTO.getStatus()!=null && vOrderItemDTO.getStatus().equals("application.common.status.discarded") ? true : false);
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
					vOrderItemDTO.setPrevious_status(vOrderItemDTO.getStatus());
					vOrderItemDTO.setStatus("application.common.status.discarded");
				}
				else {
					vOrderItemDTO.setPrevious_status(null);
					vOrderItemDTO.setStatus(vOrderItemDTO.getPrevious_status());
				}
				orderItemDTOTableFunctions.reSetLayoutAfterAnItemHasBeenAdded();//to recalculate table totals
			}			
		});
		
		vCheckBox.setEnabled(vOrderItemDTO.getStatus()!=null && !vOrderItemDTO.getStatus().equals("application.common.status.discarded") /*&& vOrderItemDTO.getPrevious_status() == null*/);
		vCheckBox.setVisible(bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
				.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "edit.revision.status.order")
				&& this.orderItemDTOTableFunctions.getOrderDTOStatus()!=null
				&& this.orderItemDTOTableFunctions.getOrderDTOStatus().equalsIgnoreCase("application.common.status.revision")
				&& this.checkOrderItemDTOForChanceToBeDiscarded(vOrderItemDTO));
			
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(itemIsDiscardedIndicatorButton,deleteButton,vCheckBox);
		return operationsButtonPanel;
	}
	
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    if (defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.listOrderItemDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.listOrderItemDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
	
	private boolean checkOrderItemDTOForChanceToBeDiscarded(final OrderItemDTO vOrderItemDTO){
		if(vOrderItemDTO.getStatus()!=null
			&& (vOrderItemDTO.getStatus().equals("application.common.status.confirmed") || 
				vOrderItemDTO.getStatus().equals("application.common.status.invoiced") || 
				vOrderItemDTO.getStatus().equals("application.common.status.in.progress"))
				&& this.checkForEnableDiscardOrderItemDTO())
			return true;
		else return false;
	}
	
	private boolean checkForEnableDiscardOrderItemDTO(){
		if(this.listOrderItemDTO.size() == 1)	return false;
		this.discardedOrderItemsCount = 0;
		for(OrderItemDTO vOrderItemDTO: this.listOrderItemDTO){
			if(vOrderItemDTO.getStatus()!=null && vOrderItemDTO.getStatus().equals("application.common.status.discarded"))this.discardedOrderItemsCount++;
		}
		if((this.listOrderItemDTO.size() - this.discardedOrderItemsCount) < 2)return false;
		
		return true;
	}
}
