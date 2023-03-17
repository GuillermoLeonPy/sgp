package py.com.kyron.sgp.gui.view.stockmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.stockmanagement.domain.SaleInvoiceProductDeliverablesDTO;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.SaleInvoiceProductDeliverablesFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.stockmanagement.sale.invoice.product.deliverables.management.view.components.SearchSaleInvoiceToolComponent;
import py.com.kyron.sgp.gui.view.stockmanagement.sale.invoice.product.deliverables.management.view.components.SearchSaleInvoiceToolComponentFunctions;
import py.com.kyron.sgp.gui.view.utils.TableNumericColumnCellLabelHelper;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SaleInvoiceProductDeliverablesManagementView extends
		VerticalLayout implements View,SearchSaleInvoiceToolComponentFunctions {

	private final Logger logger = LoggerFactory.getLogger(SaleInvoiceProductDeliverablesManagementView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "sale.invoice.product.deliverables.management.";
	private static final String[] DEFAULT_COLLAPSIBLE = {"g_invoice_status",
														"g_customer",
														"invoice_product_exigible_quantity",
														"invoice_product_physical_quantity_in_stock",
														"invoice_product_delivered_quantity",
														"g_order_status",
														"order_product_quantity_pending_to_production",
														"order_product_quantity_in_progress",
														"order_product_finished_quantity"};
	private SearchSaleInvoiceToolComponent searchSaleInvoiceToolComponent;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Table saleInvoiceProductDeliverablesDTOTable;
	private List<SaleInvoiceProductDeliverablesDTO> listSaleInvoiceProductDeliverablesDTO;
	
	public SaleInvoiceProductDeliverablesManagementView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n SaleInvoiceProductDeliverablesManagementView()...");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        this.searchSaleInvoiceToolComponent = new SearchSaleInvoiceToolComponent(this.VIEW_NAME,this);
	        this.addComponent(this.searchSaleInvoiceToolComponent);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	public SaleInvoiceProductDeliverablesManagementView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }//public void detach()

	@Override
	public void buildListTableSaleInvoiceProductDeliverablesDTO(
			List<SaleInvoiceProductDeliverablesDTO> listSaleInvoiceProductDeliverablesDTO) {
		// TODO Auto-generated method stub
		if(this.saleInvoiceProductDeliverablesDTOTable!=null)this.removeComponent(this.saleInvoiceProductDeliverablesDTOTable);
		if(listSaleInvoiceProductDeliverablesDTO!=null)this.listSaleInvoiceProductDeliverablesDTO = listSaleInvoiceProductDeliverablesDTO; else this.listSaleInvoiceProductDeliverablesDTO = new ArrayList<SaleInvoiceProductDeliverablesDTO>();
    	this.saleInvoiceProductDeliverablesDTOTable = new Table();
    	BeanItemContainer<SaleInvoiceProductDeliverablesDTO> SaleInvoiceProductDeliverablesDTOBeanItemContainer	= new BeanItemContainer<SaleInvoiceProductDeliverablesDTO>(SaleInvoiceProductDeliverablesDTO.class);
    	SaleInvoiceProductDeliverablesDTOBeanItemContainer.addAll(this.listSaleInvoiceProductDeliverablesDTO);    	
    	this.saleInvoiceProductDeliverablesDTOTable.setContainerDataSource(SaleInvoiceProductDeliverablesDTOBeanItemContainer); 
    	
    	this.saleInvoiceProductDeliverablesDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceProductDeliverablesDTO vSaleInvoiceProductDeliverablesDTO = (SaleInvoiceProductDeliverablesDTO)itemId;
				return buildOperationsButtonPanel(vSaleInvoiceProductDeliverablesDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//saleInvoiceProductDeliverablesDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoiceProductDeliverablesDTOTable.addGeneratedColumn("g_invoice_identifier_number", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceProductDeliverablesDTO vSaleInvoiceProductDeliverablesDTO = (SaleInvoiceProductDeliverablesDTO)itemId;
				final Label identifierNumberLabel = new Label(vSaleInvoiceProductDeliverablesDTO.getInvoice_identifier_number());
				identifierNumberLabel.addStyleName("colored");
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(identifierNumberLabel);
				vHorizontalLayout.setSizeFull();
				vHorizontalLayout.setComponentAlignment(identifierNumberLabel, Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//saleInvoiceProductDeliverablesDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoiceProductDeliverablesDTOTable.addGeneratedColumn("g_invoice_status", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceProductDeliverablesDTO vSaleInvoiceProductDeliverablesDTO = (SaleInvoiceProductDeliverablesDTO)itemId;	
				final Label statusLabel = new Label(messages.get(vSaleInvoiceProductDeliverablesDTO.getInvoice_status()));
				statusLabel.addStyleName("colored");
				return statusLabel;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//saleInvoiceProductDeliverablesDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoiceProductDeliverablesDTOTable.addGeneratedColumn("g_order_identifier_number", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceProductDeliverablesDTO vSaleInvoiceProductDeliverablesDTO = (SaleInvoiceProductDeliverablesDTO)itemId;
				final Label identifierNumberLabel = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vSaleInvoiceProductDeliverablesDTO.getOrder_identifier_number());
				identifierNumberLabel.addStyleName("colored");
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(identifierNumberLabel);
				vHorizontalLayout.setComponentAlignment(identifierNumberLabel, Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//saleInvoiceProductDeliverablesDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoiceProductDeliverablesDTOTable.addGeneratedColumn("g_order_status", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceProductDeliverablesDTO vSaleInvoiceProductDeliverablesDTO = (SaleInvoiceProductDeliverablesDTO)itemId;	
				final Label statusLabel = new Label(messages.get(vSaleInvoiceProductDeliverablesDTO.getOrder_status()));
				statusLabel.addStyleName("colored");
				return statusLabel;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//saleInvoiceProductDeliverablesDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoiceProductDeliverablesDTOTable.addGeneratedColumn("g_customer", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceProductDeliverablesDTO vSaleInvoiceProductDeliverablesDTO = (SaleInvoiceProductDeliverablesDTO)itemId;					
				return (vSaleInvoiceProductDeliverablesDTO.getPersonDTO().getCommercial_name()!=null ? 
						vSaleInvoiceProductDeliverablesDTO.getPersonDTO().getCommercial_name() : 
						vSaleInvoiceProductDeliverablesDTO.getPersonDTO().getPersonal_name() + "," + 
						vSaleInvoiceProductDeliverablesDTO.getPersonDTO().getPersonal_last_name());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//saleInvoiceProductDeliverablesDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoiceProductDeliverablesDTOTable.addGeneratedColumn("g_invoice_product_exigible_quantity", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceProductDeliverablesDTO vSaleInvoiceProductDeliverablesDTO = (SaleInvoiceProductDeliverablesDTO)itemId;
				final ObjectProperty<Long> property = new ObjectProperty<Long>(vSaleInvoiceProductDeliverablesDTO.getInvoice_product_exigible_quantity());
				final Label label = new Label(property);				
				label.addStyleName("colored");
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(label);
				vHorizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//tableListSaleInvoiceItemProductDeliverablesDTO.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoiceProductDeliverablesDTOTable.addGeneratedColumn("g_invoice_product_delivered_quantity", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceProductDeliverablesDTO vSaleInvoiceProductDeliverablesDTO = (SaleInvoiceProductDeliverablesDTO)itemId;
				final ObjectProperty<Long> property = new ObjectProperty<Long>(vSaleInvoiceProductDeliverablesDTO.getInvoice_product_delivered_quantity());
				final Label label = new Label(property);				
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(label);
				vHorizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//tableListSaleInvoiceItemProductDeliverablesDTO.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoiceProductDeliverablesDTOTable.setVisibleColumns(new Object[] 
    			{"operations","g_invoice_identifier_number","g_invoice_status","g_customer",
    			"g_invoice_product_exigible_quantity","invoice_product_physical_quantity_in_stock","g_invoice_product_delivered_quantity",
    			"invoice_product_returned_quantity_stock",
    			"g_order_identifier_number","g_order_status",
    			"order_product_canceled_entering_production",
    			 "order_product_quantity_pending_to_production","order_product_quantity_in_progress","order_product_finished_quantity"});
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnHeader("g_invoice_identifier_number", this.messages.get("application.common.sale.invoice.number.indicator.label"));
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnAlignment("g_invoice_identifier_number", Table.Align.LEFT);

    	this.saleInvoiceProductDeliverablesDTOTable.setColumnHeader("g_invoice_status", this.messages.get("application.common.status.label"));
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnAlignment("g_invoice_status", Table.Align.LEFT);
    	
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnHeader("g_customer", this.messages.get("application.common.customer.label"));
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnAlignment("g_customer", Table.Align.LEFT);
    	
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnHeader("g_invoice_product_exigible_quantity", this.messages.get(this.VIEW_NAME + "table.sale.invoice.product.deliverables.column.invoice.product.exigible.quantity.label.invoice.product.exigible.quantity"));
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnAlignment("g_invoice_product_exigible_quantity", Table.Align.RIGHT);
    	
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnHeader("invoice_product_physical_quantity_in_stock", this.messages.get(this.VIEW_NAME + "table.sale.invoice.product.deliverables.column.invoice.product.physical.quantity.in.stock.label.invoice.product.physical.quantity.in.stock"));
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnAlignment("invoice_product_physical_quantity_in_stock", Table.Align.RIGHT);

    	this.saleInvoiceProductDeliverablesDTOTable.setColumnHeader("g_invoice_product_delivered_quantity", this.messages.get(this.VIEW_NAME + "table.sale.invoice.product.deliverables.column.invoice.product.delivered.quantity.label.invoice.product.delivered.quantity"));
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnAlignment("g_invoice_product_delivered_quantity", Table.Align.RIGHT);
    	
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnHeader("invoice_product_returned_quantity_stock", this.messages.get("application.common.storage.returned.to.stock"));
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnAlignment("invoice_product_returned_quantity_stock", Table.Align.RIGHT);
    	
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnHeader("g_order_identifier_number", this.messages.get("application.common.order.number.indicator.label"));
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnAlignment("g_order_identifier_number", Table.Align.RIGHT);
    	
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnHeader("g_order_status", this.messages.get("application.common.status.label"));
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnAlignment("g_order_status", Table.Align.LEFT);
    	
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnHeader("order_product_canceled_entering_production", this.messages.get("application.common.status.canceled"));
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnAlignment("order_product_canceled_entering_production", Table.Align.RIGHT);

    	this.saleInvoiceProductDeliverablesDTOTable.setColumnHeader("order_product_quantity_pending_to_production", this.messages.get("application.common.status.pending"));
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnAlignment("order_product_quantity_pending_to_production", Table.Align.RIGHT);
  	
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnHeader("order_product_quantity_in_progress", this.messages.get("application.common.status.in.progress"));
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnAlignment("order_product_quantity_in_progress", Table.Align.RIGHT);
    	
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnHeader("order_product_finished_quantity", this.messages.get("application.common.status.finalized"));
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnAlignment("order_product_finished_quantity", Table.Align.RIGHT);
    	
    	this.saleInvoiceProductDeliverablesDTOTable.setSizeFull();
    	this.saleInvoiceProductDeliverablesDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.saleInvoiceProductDeliverablesDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.saleInvoiceProductDeliverablesDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnExpandRatio("operations", 0.0004f); 
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnExpandRatio("g_invoice_identifier_number", 0.001f);
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnExpandRatio("g_invoice_status", 0.00059f);
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnExpandRatio("g_customer", 0.0026f);
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnExpandRatio("g_invoice_product_exigible_quantity", 0.00069f);
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnExpandRatio("invoice_product_physical_quantity_in_stock", 0.0005f);
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnExpandRatio("g_invoice_product_delivered_quantity", 0.00069f);
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnExpandRatio("g_order_identifier_number", 0.0007f);
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnExpandRatio("g_order_status", 0.0006f);
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnExpandRatio("order_product_canceled_entering_production", 0.0007f);
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnExpandRatio("order_product_quantity_pending_to_production", 0.0007f);
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnExpandRatio("order_product_quantity_in_progress", 0.0007f);
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnExpandRatio("order_product_finished_quantity", 0.0011f);
    	this.saleInvoiceProductDeliverablesDTOTable.setSelectable(true);
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnCollapsingAllowed(true);
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnCollapsible("operations", false);
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnCollapsible("g_invoice_identifier_number", false);
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnCollapsible("g_order_identifier_number", false);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.saleInvoiceProductDeliverablesDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.saleInvoiceProductDeliverablesDTOTable.setSortAscending(false);
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnReorderingAllowed(true);
    	this.saleInvoiceProductDeliverablesDTOTable.setFooterVisible(true);
    	this.saleInvoiceProductDeliverablesDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	
        addComponent(this.saleInvoiceProductDeliverablesDTOTable);
        setExpandRatio(this.saleInvoiceProductDeliverablesDTOTable, 1);
	}

	private HorizontalLayout buildOperationsButtonPanel(final SaleInvoiceProductDeliverablesDTO vSaleInvoiceProductDeliverablesDTO){	
		final Button deliverProductsBySaleInvoiceItemDeliverablesButton = new Button();
		deliverProductsBySaleInvoiceItemDeliverablesButton.setIcon(FontAwesome.EDIT);
		deliverProductsBySaleInvoiceItemDeliverablesButton.setDescription(this.messages.get(this.VIEW_NAME + "table.sale.invoice.product.deliverables.column.operations.button.deliver.producs.by.sale.invoice.item.deliverables.description"));
		deliverProductsBySaleInvoiceItemDeliverablesButton.addStyleName("borderless");
		deliverProductsBySaleInvoiceItemDeliverablesButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar vSaleInvoiceProductDeliverablesDTO...\n" + vSaleInvoiceProductDeliverablesDTO.toString());
                try{		                	
                	goToSaleInvoiceProductDeliverablesFormView(vSaleInvoiceProductDeliverablesDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		//editButton.setVisible(vSaleInvoiceProductDeliverablesDTO.getId()!=null);
		deliverProductsBySaleInvoiceItemDeliverablesButton.setEnabled(true);
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(deliverProductsBySaleInvoiceItemDeliverablesButton);
		//operationsButtonPanel.addStyleName("wrapping");
		return operationsButtonPanel;
	}
	
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    // Some columns are collapsed when browser window width gets small
	    // enough to make the table fit better.
	    if (this.saleInvoiceProductDeliverablesDTOTable!=null && defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.saleInvoiceProductDeliverablesDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.saleInvoiceProductDeliverablesDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
	
    private void goToSaleInvoiceProductDeliverablesFormView(final SaleInvoiceProductDeliverablesDTO vSaleInvoiceProductDeliverablesDTO){
		try{						
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.SALE_INVOICE_PRODUCT_DELIVERABLES_FORM.getViewName());
			DashboardEventBus.post(new SaleInvoiceProductDeliverablesFormViewEvent(
					vSaleInvoiceProductDeliverablesDTO,
					DashboardViewType.SALE_INVOICE_PRODUCT_DELIVERABLES_MANAGEMENT.getViewName(),
					true,
					false));
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
    }
}
