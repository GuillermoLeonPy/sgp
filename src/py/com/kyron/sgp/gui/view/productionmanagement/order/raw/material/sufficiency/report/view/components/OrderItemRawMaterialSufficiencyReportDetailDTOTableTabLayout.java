package py.com.kyron.sgp.gui.view.productionmanagement.order.raw.material.sufficiency.report.view.components;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.productionmanagement.domain.OrderItemRawMaterialSufficiencyReportDetailDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.OrderRawMaterialSufficiencyReportDTO;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
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
public class OrderItemRawMaterialSufficiencyReportDetailDTOTableTabLayout extends
		VerticalLayout {
	
	private final Logger logger = LoggerFactory.getLogger(OrderItemRawMaterialSufficiencyReportDetailDTOTableTabLayout.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private final OrderRawMaterialSufficiencyReportViewFunctions orderRawMaterialSufficiencyReportViewFunctions;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final OrderRawMaterialSufficiencyReportDTO orderRawMaterialSufficiencyReportDTO;
	private Table orderItemRawMaterialSufficiencyReportDetailDTOTable;
	private static final String[] DEFAULT_COLLAPSIBLE = {"sum_required_quantity","available_quantity"};
	
	public OrderItemRawMaterialSufficiencyReportDetailDTOTableTabLayout(
			final OrderRawMaterialSufficiencyReportViewFunctions orderRawMaterialSufficiencyReportViewFunctions,
			final String VIEW_NAME,
			final OrderRawMaterialSufficiencyReportDTO orderRawMaterialSufficiencyReportDTO) {
		// TODO Auto-generated constructor stub
		this.orderRawMaterialSufficiencyReportViewFunctions = orderRawMaterialSufficiencyReportViewFunctions;
		this.VIEW_NAME = VIEW_NAME;
		this.orderRawMaterialSufficiencyReportDTO = orderRawMaterialSufficiencyReportDTO;
		try{
			logger.info("\n OrderItemRawMaterialSufficiencyReportDetailDTOTableTabLayout..");
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

	/*public OrderItemRawMaterialSufficiencyReportDetailDTOTableTabLayout(
			Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n OrderItemRawMaterialSufficiencyReportDetailDTOTableTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void setUpLayoutContent(){
		this.buildOrderItemRawMaterialSufficiencyReportDetailDTOTable(this.orderRawMaterialSufficiencyReportDTO.getListOrderItemRawMaterialSufficiencyReportDetailDTOByIdOrder());
		this.addComponent(this.buildToolbar());
		this.addComponent(this.orderItemRawMaterialSufficiencyReportDetailDTOTable);
		this.setExpandRatio(this.orderItemRawMaterialSufficiencyReportDetailDTOTable, 1);
	}
	
	 private void buildOrderItemRawMaterialSufficiencyReportDetailDTOTable(
			 List<OrderItemRawMaterialSufficiencyReportDetailDTO> listOrderItemRawMaterialSufficiencyReportDetailDTO) { 
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable = new Table();
	    	BeanItemContainer<OrderItemRawMaterialSufficiencyReportDetailDTO> machineDTOBeanItemContainer	= new BeanItemContainer<OrderItemRawMaterialSufficiencyReportDetailDTO>(OrderItemRawMaterialSufficiencyReportDetailDTO.class);
	    	machineDTOBeanItemContainer.addAll(listOrderItemRawMaterialSufficiencyReportDetailDTO);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setContainerDataSource(machineDTOBeanItemContainer);
	    	
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setVisibleColumns(new Object[] 
	    			{"raw_material_id","measurment_unit_id","available_quantity",
	    			"sum_required_quantity","missing_quantity"});
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnHeader("raw_material_id", this.messages.get("application.common.rawmaterialid.label"));
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnAlignment("raw_material_id", Table.Align.LEFT);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnHeader("measurment_unit_id", this.messages.get("application.common.measurmentunitid.label"));
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnAlignment("measurment_unit_id", Table.Align.LEFT);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnHeader("available_quantity", this.messages.get(this.VIEW_NAME + "tab.table.order.item.raw.material.sufficiency.report.detail.column.available.quantity"));
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnAlignment("available_quantity", Table.Align.RIGHT);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnHeader("sum_required_quantity", this.messages.get(this.VIEW_NAME + "tab.table.order.item.raw.material.sufficiency.report.detail.column.required.quantity"));
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnAlignment("sum_required_quantity", Table.Align.RIGHT);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnHeader("missing_quantity", this.messages.get(this.VIEW_NAME + "tab.table.order.item.raw.material.sufficiency.report.detail.column.missing.quantity"));
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnAlignment("missing_quantity", Table.Align.RIGHT);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setSizeFull();
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
	    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.addStyleName(ValoTheme.TABLE_SMALL);	    	    	
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnExpandRatio("raw_material_id", 0.008f);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnExpandRatio("measurment_unit_id", 0.0045f);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnExpandRatio("available_quantity", 0.0035f);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnExpandRatio("sum_required_quantity", 0.0035f);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnExpandRatio("missing_quantity", 0.0035f);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setSelectable(true);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnCollapsingAllowed(true);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnCollapsible("raw_material_id", false);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnCollapsible("measurment_unit_id", false);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnCollapsible("missing_quantity", false);
	    	//this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setSortContainerPropertyId("product_description");
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setSortAscending(false);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnReorderingAllowed(true);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setFooterVisible(true);
	    	this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
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
	    	   this.orderItemRawMaterialSufficiencyReportDetailDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
		 
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.orderItemRawMaterialSufficiencyReportDetailDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
	
    
	 private Component buildToolbar() {
	        HorizontalLayout header = new HorizontalLayout();
	        header.addStyleName("viewheader");
	        header.setSpacing(true);
	        Responsive.makeResponsive(header);
	        logger.info("\n*********************remember: an HorizontalLayout is set with the instrucction: Responsive.makeResponsive(header); !!! "
	        		+"\n*********************");
	        Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.table.order.item.raw.material.sufficiency.report.detail.main.title"));
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
