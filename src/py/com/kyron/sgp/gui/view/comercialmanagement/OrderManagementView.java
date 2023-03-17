package py.com.kyron.sgp.gui.view.comercialmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.OrderRegisterFormViewEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.SaleInvoiceRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.comercialmanagement.ordermanagementview.components.SearchOrderToolComponent;
import py.com.kyron.sgp.gui.view.comercialmanagement.ordermanagementview.components.SearchOrderToolComponentHostView;
import py.com.kyron.sgp.gui.view.utils.TableNumericColumnCellLabelHelper;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
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
public class OrderManagementView extends VerticalLayout implements View,SearchOrderToolComponentHostView {

	private final Logger logger = LoggerFactory.getLogger(OrderManagementView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "order.management.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private static final String[] DEFAULT_COLLAPSIBLE = {"g_customer","g_payment_condition","g_registration_date","g_amount"};
	private SearchOrderToolComponent searchOrderToolComponent;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Table orderDTOTable;
	private List<OrderDTO> listOrderDTO;
	private BussinesSessionUtils bussinesSessionUtils;
	
	public OrderManagementView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n OrderManagementView()...");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        this.searchOrderToolComponent = new SearchOrderToolComponent(this,this.VIEW_NAME, true);
	        this.addComponent(this.searchOrderToolComponent);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}
	
	private void initServices() throws Exception{
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}

	public OrderManagementView(Component... children) {
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
	public void buildOrderDTOTable(List<OrderDTO> listOrderDTO) throws Exception {
		// TODO Auto-generated method stub
		if(this.orderDTOTable!=null)this.removeComponent(this.orderDTOTable);
		if(listOrderDTO!=null)this.listOrderDTO = listOrderDTO; else this.listOrderDTO = new ArrayList<OrderDTO>();
    	this.orderDTOTable = new Table();
    	BeanItemContainer<OrderDTO> OrderDTOBeanItemContainer	= new BeanItemContainer<OrderDTO>(OrderDTO.class);
    	OrderDTOBeanItemContainer.addAll(this.listOrderDTO);    	
    	this.orderDTOTable.setContainerDataSource(OrderDTOBeanItemContainer); 
    	
    	this.orderDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final OrderDTO vOrderDTO = (OrderDTO)itemId;
				return buildOperationsButtonPanel(vOrderDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//orderDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.orderDTOTable.addGeneratedColumn("g_identifier_number", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final OrderDTO vOrderDTO = (OrderDTO)itemId;
				final HorizontalLayout vHorizontalLayout = 
				new HorizontalLayout(TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vOrderDTO.getIdentifier_number()));
				vHorizontalLayout.setSizeFull();
				vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(0), Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//orderDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.orderDTOTable.addGeneratedColumn("g_customer", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final OrderDTO vOrderDTO = (OrderDTO)itemId;					
				return (vOrderDTO.getPersonDTO().getCommercial_name()!=null ? 
						vOrderDTO.getPersonDTO().getCommercial_name() : 
						vOrderDTO.getPersonDTO().getPersonal_name() + "," + vOrderDTO.getPersonDTO().getPersonal_last_name());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//orderDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.orderDTOTable.addGeneratedColumn("g_amount", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final OrderDTO vOrderDTO = (OrderDTO)itemId;
				final Label currencyLabel = new Label(vOrderDTO.getCurrencyDTO().getId_code());
    			currencyLabel.addStyleName("colored");
    			final Label amountLabel = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vOrderDTO.getAmount());
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(currencyLabel,amountLabel);
				vHorizontalLayout.setComponentAlignment(currencyLabel, Alignment.MIDDLE_LEFT);
				vHorizontalLayout.setComponentAlignment(amountLabel, Alignment.MIDDLE_RIGHT);				
				vHorizontalLayout.setSizeFull();
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//orderDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.orderDTOTable.addGeneratedColumn("g_registration_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final OrderDTO vOrderDTO = (OrderDTO)itemId;
				return SgpUtils.dateFormater.format(vOrderDTO.getRegistration_date());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.orderDTOTable.addGeneratedColumn("g_payment_condition", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final OrderDTO vOrderDTO = (OrderDTO)itemId;	
				final Label statusLabel = new Label(messages.get(vOrderDTO.getPayment_condition()));
				statusLabel.addStyleName("colored");
				return statusLabel;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//orderDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.orderDTOTable.addGeneratedColumn("g_status", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final OrderDTO vOrderDTO = (OrderDTO)itemId;	
				final Label statusLabel = new Label(messages.get(vOrderDTO.getStatus()));
				statusLabel.addStyleName("colored");
				return statusLabel;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//orderDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.orderDTOTable.setVisibleColumns(new Object[] 
    			{"operations","g_identifier_number","g_registration_date",
    			 "g_customer","g_payment_condition","g_amount","g_status"});
    	this.orderDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.orderDTOTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.orderDTOTable.setColumnHeader("g_identifier_number", this.messages.get("application.common.order.number.indicator.label"));
    	this.orderDTOTable.setColumnAlignment("g_identifier_number", Table.Align.RIGHT);
    	
    	this.orderDTOTable.setColumnHeader("g_customer", this.messages.get("application.common.customer.label"));
    	this.orderDTOTable.setColumnAlignment("g_customer", Table.Align.LEFT);
    	
    	this.orderDTOTable.setColumnHeader("g_payment_condition", this.messages.get("application.common.payment.condition.selector.description"));
    	this.orderDTOTable.setColumnAlignment("g_payment_condition", Table.Align.LEFT);
    	
    	this.orderDTOTable.setColumnHeader("g_registration_date", this.messages.get("application.common.table.column.registration.date.label"));
    	this.orderDTOTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
  	
    	this.orderDTOTable.setColumnHeader("g_amount", this.messages.get("application.common.table.column.total.amount.label"));
    	this.orderDTOTable.setColumnAlignment("g_amount", Table.Align.RIGHT);
    	
    	this.orderDTOTable.setColumnHeader("g_status", this.messages.get("application.common.status.label"));
    	this.orderDTOTable.setColumnAlignment("g_status", Table.Align.LEFT);
    	
    	this.orderDTOTable.setSizeFull();
    	this.orderDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.orderDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.orderDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.orderDTOTable.setColumnExpandRatio("operations", 0.0059f); 
    	this.orderDTOTable.setColumnExpandRatio("g_identifier_number", 0.0052f);    	
    	this.orderDTOTable.setColumnExpandRatio("g_customer", 0.019f);
    	this.orderDTOTable.setColumnExpandRatio("g_payment_condition", 0.0085f);
    	this.orderDTOTable.setColumnExpandRatio("g_registration_date", 0.0086f);
    	this.orderDTOTable.setColumnExpandRatio("g_amount", 0.009f);
    	this.orderDTOTable.setColumnExpandRatio("g_status", 0.009f);
    	this.orderDTOTable.setSelectable(true);
    	this.orderDTOTable.setColumnCollapsingAllowed(true);
    	this.orderDTOTable.setColumnCollapsible("operations", false);
    	this.orderDTOTable.setColumnCollapsible("g_identifier_number", true);
    	this.orderDTOTable.setColumnCollapsible("g_status", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.orderDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.orderDTOTable.setSortAscending(false);
    	this.orderDTOTable.setColumnReorderingAllowed(true);
    	this.orderDTOTable.setFooterVisible(true);
    	this.orderDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	
        addComponent(this.orderDTOTable);
        setExpandRatio(this.orderDTOTable, 1);
	}

	private HorizontalLayout buildOperationsButtonPanel(final OrderDTO vOrderDTO){	
		final Button editButton = new Button();
		editButton.setIcon(vOrderDTO.getStatus().equals("application.common.status.pending") ? FontAwesome.EDIT : FontAwesome.SEARCH);
		editButton.setDescription(vOrderDTO.getStatus().equals("application.common.status.pending") ? 
				this.messages.get("application.common.table.column.operations.edit") : this.messages.get("application.common.query.label"));
		editButton.addStyleName("borderless");
		editButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar OrderDTO...\n" + vOrderDTO.toString());
                try{		                	
                	goToRegisterOrderView(vOrderDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		editButton.setVisible(true);
		editButton.setEnabled(true);
		
		
		final Button generateSaleInvoiceButton = new Button();
		generateSaleInvoiceButton.setIcon(FontAwesome.SELLSY);
		generateSaleInvoiceButton.setDescription(this.messages.get(this.VIEW_NAME + "table.order.operations.column.button.generate.sale.invoice.description"));
		generateSaleInvoiceButton.addStyleName("borderless");
		generateSaleInvoiceButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n generate sale invoice from OrderDTO...\n" + vOrderDTO.toString());
                try{		                	
                	goToSaleInvoiceRegisterFormView(vOrderDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		generateSaleInvoiceButton.setVisible(bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
				.contains("main.menu.module.cash.movements.management.sale.invoice.management"));
		generateSaleInvoiceButton.setEnabled(vOrderDTO.getStatus().equals("application.common.status.confirmed"));
		
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(editButton,generateSaleInvoiceButton);
		//operationsButtonPanel.addStyleName("wrapping");
		return operationsButtonPanel;
	}
	
    private void goToRegisterOrderView(OrderDTO vOrderDTO){
		try{
			if(vOrderDTO == null)vOrderDTO = new OrderDTO();
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.REGISTER_ORDER_FORM.getViewName());
			DashboardEventBus.post(new OrderRegisterFormViewEvent(
					vOrderDTO, 
					DashboardViewType.ORDER_MANAGEMENT.getViewName(),
					vOrderDTO!=null && vOrderDTO.getId()!=null,
					false));
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
    }
    
    private void goToSaleInvoiceRegisterFormView(final OrderDTO vOrderDTO){
		try{						
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.SALE_INVOICE_REGISTER_FORM.getViewName());
			DashboardEventBus.post(new SaleInvoiceRegisterFormViewEvent(
					vOrderDTO, 
					null,
					DashboardViewType.SALE_INVOICE_MANAGEMENT.getViewName(),
					false));
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
    }
    
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    // Some columns are collapsed when browser window width gets small
	    // enough to make the table fit better.
	    if (this.orderDTOTable!=null && defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.orderDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.orderDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
	
	private void instantiateProductionProcessActivities(final OrderDTO vOrderDTO){
		
	}
}
