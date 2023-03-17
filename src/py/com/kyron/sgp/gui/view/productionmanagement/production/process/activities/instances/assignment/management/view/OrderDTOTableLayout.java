package py.com.kyron.sgp.gui.view.productionmanagement.production.process.activities.instances.assignment.management.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.utils.TableNumericColumnCellLabelHelper;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
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
public class OrderDTOTableLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(OrderDTOTableLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.activities.instances.assignment.management.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Table orderDTOTable;
	private List<OrderDTO> listOrderDTO;
	private static final String[] DEFAULT_COLLAPSIBLE = {"g_customer","g_payment_condition","g_registration_date","g_amount"};
    private BussinesSessionUtils bussinesSessionUtils;
    private ComercialManagementService comercialManagementService;
    private final OrderDTOTableLayoutFunctions orderDTOTableLayoutFunctions;
	
	public OrderDTOTableLayout(final OrderDTOTableLayoutFunctions orderDTOTableLayoutFunctions) {
		// TODO Auto-generated constructor stub
		this.orderDTOTableLayoutFunctions = orderDTOTableLayoutFunctions;
		try{
			logger.info("\n OrderDTOTableLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public OrderDTOTableLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
    
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void initServices(){
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		this.comercialManagementService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
	}
	
	private void setUpLayoutContent() throws PmsServiceException{
		this.buildOrderDTOTable();
		addComponent(this.buildToolbar());
        addComponent(orderDTOTable);
        setExpandRatio(orderDTOTable, 1);
	}
	
	private void buildOrderDTOTable() throws PmsServiceException{
		if(this.orderDTOTable!=null)this.removeComponent(this.orderDTOTable);
		this.retrievePreProductionOrderDTOList();
		if(listOrderDTO==null)this.listOrderDTO = new ArrayList<OrderDTO>();
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
				final Label label = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vOrderDTO.getIdentifier_number());
				label.addStyleName("colored");
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(label);
				vHorizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
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
    	this.orderDTOTable.setSortContainerPropertyId("identifier_number");
    	this.orderDTOTable.setSortAscending(true);
    	this.orderDTOTable.setSortEnabled(true);
    	this.orderDTOTable.setColumnReorderingAllowed(true);
    	this.orderDTOTable.setFooterVisible(true);
    	this.orderDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
	private void retrievePreProductionOrderDTOList() throws PmsServiceException{
		this.listOrderDTO = this.comercialManagementService.listOrderDTO(new OrderDTO("application.common.status.pre.production", true, true));
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
                	orderDTOTableLayoutFunctions.goToRegisterOrderView(vOrderDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		editButton.setVisible(true);
		editButton.setEnabled(true);
		
		final Button instantiateProductionProcessActivitiesButton = new Button();
		instantiateProductionProcessActivitiesButton.setIcon(FontAwesome.CUBES);
		instantiateProductionProcessActivitiesButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.order.table.table.order.operations.column.button.instantiate.production.process.activities.description"));
		instantiateProductionProcessActivitiesButton.addStyleName("borderless");
		instantiateProductionProcessActivitiesButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n instantiateProductionProcessActivities...\n" + vOrderDTO.toString());
                try{	
                	orderDTOTableLayoutFunctions.instantiateProductionProcessActivities(vOrderDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		instantiateProductionProcessActivitiesButton.setVisible(bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
				.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "instantiate.production.process.activities"));
		instantiateProductionProcessActivitiesButton.setEnabled(
				vOrderDTO.getStatus().equals("application.common.status.pre.production")
				|| 
				(vOrderDTO.getStatus().equals("application.common.status.in.progress")
				&& vOrderDTO.getHavingAnyOrderItemWithPendingToInstanciateQuantity()));		
		
		
		final Button queryOrderRawMaterialSufficiencyReportDTOButton = new Button();
		queryOrderRawMaterialSufficiencyReportDTOButton.setIcon(FontAwesome.WARNING);
		queryOrderRawMaterialSufficiencyReportDTOButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.order.table.table.order.operations.column.button.query.order.raw.material.sufficiency.report.description"));
		queryOrderRawMaterialSufficiencyReportDTOButton.addStyleName("borderless");
		queryOrderRawMaterialSufficiencyReportDTOButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n queryOrderRawMaterialSufficiencyReportDTOButton...\n" + vOrderDTO.toString());
                try{	
                	orderDTOTableLayoutFunctions.queryOrderRawMaterialSufficiencyReportDTO(vOrderDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		
		queryOrderRawMaterialSufficiencyReportDTOButton.setVisible(
				(vOrderDTO.getStatus().equals("application.common.status.pre.production")
				|| 
				vOrderDTO.getStatus().equals("application.common.status.in.progress"))
				 && vOrderDTO.getHavingAnyOrderItemWithPendingToInstanciateQuantity());
		
		
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(editButton,instantiateProductionProcessActivitiesButton,queryOrderRawMaterialSufficiencyReportDTOButton);
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
	
	private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);
        logger.info("\n*********************remember: an HorizontalLayout is set with the instrucction: Responsive.makeResponsive(header); !!! "
        		+"\n*********************");
        Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.order.table.main.title"));
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);
        
        /*HorizontalLayout tools = new HorizontalLayout(this.createRegisterProcessButton());	        	        
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);*/
        return header;
 }//private Component buildToolbar()
}
