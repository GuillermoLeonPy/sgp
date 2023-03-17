package py.com.kyron.sgp.gui.view.productionmanagement.order.raw.material.sufficiency.report.view.components;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.OrderItemRawMaterialSufficiencyReportDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.OrderRawMaterialSufficiencyReportDTO;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
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
public class OrderRawMaterialSufficiencyReportDTOTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(OrderRawMaterialSufficiencyReportDTOTabLayout.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private final OrderRawMaterialSufficiencyReportViewFunctions orderRawMaterialSufficiencyReportViewFunctions;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final OrderDTO orderDTO;
	private final OrderRawMaterialSufficiencyReportDTO orderRawMaterialSufficiencyReportDTO;
	private Table orderItemRawMaterialSufficiencyReportDTOTable;
	private static final String[] DEFAULT_COLLAPSIBLE = {"entered_into_producction_quantity","pending_to_instanciate_quantity","canceled_entering_production_by_document_quantity","in_progress_quantity"};
	
	public OrderRawMaterialSufficiencyReportDTOTabLayout(
			final OrderRawMaterialSufficiencyReportViewFunctions orderRawMaterialSufficiencyReportViewFunctions,
			final String VIEW_NAME,
			final OrderDTO orderDTO,
			final OrderRawMaterialSufficiencyReportDTO orderRawMaterialSufficiencyReportDTO
			) {
		// TODO Auto-generated constructor stub
		this.orderRawMaterialSufficiencyReportViewFunctions = orderRawMaterialSufficiencyReportViewFunctions;
		this.VIEW_NAME = VIEW_NAME;
		this.orderDTO = orderDTO;
		this.orderRawMaterialSufficiencyReportDTO = orderRawMaterialSufficiencyReportDTO;
		try{
			logger.info("\n OrderRawMaterialSufficiencyReportDTOTabLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();	
	        this.setSizeFull();
	        //this.addStyleName("v-scrollable");
	        //this.setHeight("100%");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public OrderRawMaterialSufficiencyReportDTOTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/

	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n OrderRawMaterialSufficiencyReportDTOTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void setUpLayoutContent(){
		//this.removeAllComponents();
		this.addComponent(this.buildToolbar());
		this.addComponent(new OrderRawMaterialSufficiencyReportHeaderLayout(this.orderRawMaterialSufficiencyReportDTO,this.VIEW_NAME));
		this.buildOrderItemRawMaterialSufficiencyReportDTOTable(this.orderRawMaterialSufficiencyReportDTO.getListOrderItemRawMaterialSufficiencyReportDTO());
		this.addComponent(this.orderItemRawMaterialSufficiencyReportDTOTable);
		this.setExpandRatio(this.orderItemRawMaterialSufficiencyReportDTOTable, 1);
		this.addComponent(this.setUpOkCancelButtons());
	}
	
	 private void buildOrderItemRawMaterialSufficiencyReportDTOTable(
			 List<OrderItemRawMaterialSufficiencyReportDTO> listOrderItemRawMaterialSufficiencyReportDTO) { 
	    	this.orderItemRawMaterialSufficiencyReportDTOTable = new Table();
	    	BeanItemContainer<OrderItemRawMaterialSufficiencyReportDTO> machineDTOBeanItemContainer	= new BeanItemContainer<OrderItemRawMaterialSufficiencyReportDTO>(OrderItemRawMaterialSufficiencyReportDTO.class);
	    	machineDTOBeanItemContainer.addAll(listOrderItemRawMaterialSufficiencyReportDTO);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setContainerDataSource(machineDTOBeanItemContainer);
	    	
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setVisibleColumns(new Object[] 
	    			{"product_description","item_quantity","entered_into_producction_quantity","in_progress_quantity",
	    			"pending_to_instanciate_quantity","canceled_entering_production_by_document_quantity"});
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnHeader("product_description", this.messages.get("application.common.product.label"));
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnAlignment("product_description", Table.Align.LEFT);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnHeader("item_quantity", this.messages.get("application.common.quantity.label") + " Item");
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnAlignment("item_quantity", Table.Align.RIGHT);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnHeader("entered_into_producction_quantity", this.messages.get(this.VIEW_NAME + "tab.order.raw.material.sufficiency.report.table.order.item.raw.material.sufficiency.report.column.entered.into.producction.quantity"));
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnAlignment("entered_into_producction_quantity", Table.Align.RIGHT);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnHeader("in_progress_quantity", this.messages.get("application.common.quantity.label") + " " + this.messages.get("application.common.status.in.progress").toLowerCase());
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnAlignment("in_progress_quantity", Table.Align.RIGHT);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnHeader("pending_to_instanciate_quantity", this.messages.get("application.common.quantity.label") + " " + this.messages.get("application.common.status.pending").toLowerCase());
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnAlignment("pending_to_instanciate_quantity", Table.Align.RIGHT);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnHeader("canceled_entering_production_by_document_quantity", this.messages.get("application.common.quantity.label") + " " + this.messages.get("application.common.status.canceled").toLowerCase());
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnAlignment("canceled_entering_production_by_document_quantity", Table.Align.RIGHT);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setSizeFull();
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
	    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.addStyleName(ValoTheme.TABLE_SMALL);	    	    	
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnExpandRatio("product_description", 0.03f);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnExpandRatio("item_quantity", 0.009f);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnExpandRatio("entered_into_producction_quantity", 0.009f);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnExpandRatio("in_progress_quantity", 0.009f);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnExpandRatio("pending_to_instanciate_quantity", 0.009f);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnExpandRatio("canceled_entering_production_by_document_quantity", 0.009f);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setSelectable(true);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnCollapsingAllowed(true);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnCollapsible("product_description", false);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnCollapsible("pending_to_instanciate_quantity", false);
	    	//this.orderItemRawMaterialSufficiencyReportDTOTable.setSortContainerPropertyId("product_description");
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setSortAscending(false);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnReorderingAllowed(true);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setFooterVisible(true);
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	    	this.orderItemRawMaterialSufficiencyReportDTOTable.setHeight(150f + (listOrderItemRawMaterialSufficiencyReportDTO.size() * 25), Unit.PIXELS);
	 }//private void buildMachinesTable(){
		
	 
	 @Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    // Some columns are collapsed when browser window width gets small
	    // enough to make the table fit better.
	    if (defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.orderItemRawMaterialSufficiencyReportDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
		 
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.orderItemRawMaterialSufficiencyReportDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
	
    private HorizontalLayout setUpOkCancelButtons(){
    	
		final Button cancelButton = new Button(this.messages.get("application.common.button.cancel.label"));/*"Cancelar"*/
		cancelButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		orderRawMaterialSufficiencyReportViewFunctions.navigateToCallerView();
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
		wrapperHorizontalLayout.addComponent(cancelButton);
		okCancelHorizontalLayout.addComponent(wrapperHorizontalLayout);
		return okCancelHorizontalLayout;
    }
    
	private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);
        logger.info("\n*********************remember: an HorizontalLayout is set with the instrucction: Responsive.makeResponsive(header); !!! "
        		+"\n*********************");
        Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.order.raw.material.sufficiency.report.main.title"));
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);
        
       /* HorizontalLayout tools = new HorizontalLayout(this.buildFilter(),this.createRegisterMachineButton());	        	        
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);*/
        header.setMargin(true);
        return header;
 }//private Component buildToolbar()
}
