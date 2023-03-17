package py.com.kyron.sgp.gui.view.cash.movements.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceDTO;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.management.view.components.PurchaseInvoiceManagementViewFunctions;
import py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.management.view.components.SearchPurchaseInvoiceToolComponent;
import py.com.kyron.sgp.gui.view.utils.TableNumericColumnCellLabelHelper;
import py.com.kyron.sgp.gui.event.SgpEvent.PurchaseInvoiceRegisterFormViewEvent;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
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
public class PurchaseInvoiceManagementView extends VerticalLayout implements
		View,PurchaseInvoiceManagementViewFunctions {

	private final Logger logger = LoggerFactory.getLogger(PurchaseInvoiceManagementView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "purchase.invoice.management.";
	private static final String[] DEFAULT_COLLAPSIBLE = {"g_customer","g_payment_condition","g_emission_date","g_amount"};
	private SearchPurchaseInvoiceToolComponent searchPurchaseInvoiceToolComponent;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Table purchaseInvoiceDTOTable;
	private List<PurchaseInvoiceDTO> listPurchaseInvoiceDTO;
	
	public PurchaseInvoiceManagementView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n PurchaseInvoiceManagementView()...");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        this.searchPurchaseInvoiceToolComponent = new SearchPurchaseInvoiceToolComponent(this.VIEW_NAME,this, true);
	        this.addComponent(this.searchPurchaseInvoiceToolComponent);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	public PurchaseInvoiceManagementView(Component... children) {
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
	public void buildPurchaseInvoiceDTOTable(
			List<PurchaseInvoiceDTO> listPurchaseInvoiceDTO) {
		// TODO Auto-generated method stub
		if(this.purchaseInvoiceDTOTable!=null)this.removeComponent(this.purchaseInvoiceDTOTable);
		if(listPurchaseInvoiceDTO!=null)this.listPurchaseInvoiceDTO = listPurchaseInvoiceDTO; else this.listPurchaseInvoiceDTO = new ArrayList<PurchaseInvoiceDTO>();
    	this.purchaseInvoiceDTOTable = new Table();
    	BeanItemContainer<PurchaseInvoiceDTO> PurchaseInvoiceDTOBeanItemContainer	= new BeanItemContainer<PurchaseInvoiceDTO>(PurchaseInvoiceDTO.class);
    	PurchaseInvoiceDTOBeanItemContainer.addAll(this.listPurchaseInvoiceDTO);    	
    	this.purchaseInvoiceDTOTable.setContainerDataSource(PurchaseInvoiceDTOBeanItemContainer); 
    	
    	this.purchaseInvoiceDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceDTO vPurchaseInvoiceDTO = (PurchaseInvoiceDTO)itemId;
				return buildOperationsButtonPanel(vPurchaseInvoiceDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//purchaseInvoiceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.purchaseInvoiceDTOTable.addGeneratedColumn("g_identifier_number", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceDTO vPurchaseInvoiceDTO = (PurchaseInvoiceDTO)itemId;
				final Label identifierNumberLabel = new Label(vPurchaseInvoiceDTO.getIdentifier_number());
				identifierNumberLabel.addStyleName("colored");
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(identifierNumberLabel);
				vHorizontalLayout.setSizeFull();
				vHorizontalLayout.setComponentAlignment(identifierNumberLabel, Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//purchaseInvoiceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.purchaseInvoiceDTOTable.addGeneratedColumn("g_supplier", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceDTO vPurchaseInvoiceDTO = (PurchaseInvoiceDTO)itemId;					
				return (vPurchaseInvoiceDTO.getPersonDTO().getCommercial_name()!=null ? 
						vPurchaseInvoiceDTO.getPersonDTO().getCommercial_name() : 
						vPurchaseInvoiceDTO.getPersonDTO().getPersonal_name() + "," + 
						vPurchaseInvoiceDTO.getPersonDTO().getPersonal_last_name());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//purchaseInvoiceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.purchaseInvoiceDTOTable.addGeneratedColumn("g_amount", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceDTO vPurchaseInvoiceDTO = (PurchaseInvoiceDTO)itemId;
				final Label currencyLabel = new Label(vPurchaseInvoiceDTO.getCurrencyDTO().getId_code());
    			currencyLabel.addStyleName("colored");
    			final Label amountLabel = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vPurchaseInvoiceDTO.getTotal_amount());
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(currencyLabel,amountLabel);
				vHorizontalLayout.setComponentAlignment(currencyLabel, Alignment.MIDDLE_LEFT);
				vHorizontalLayout.setComponentAlignment(amountLabel, Alignment.MIDDLE_RIGHT);				
				vHorizontalLayout.setSizeFull();
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//purchaseInvoiceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.purchaseInvoiceDTOTable.addGeneratedColumn("g_emission_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceDTO vPurchaseInvoiceDTO = (PurchaseInvoiceDTO)itemId;
				return SgpUtils.dateFormaterWithOutHour.format(vPurchaseInvoiceDTO.getEmission_date());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.purchaseInvoiceDTOTable.addGeneratedColumn("g_payment_condition", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceDTO vPurchaseInvoiceDTO = (PurchaseInvoiceDTO)itemId;	
				final Label statusLabel = new Label(messages.get(vPurchaseInvoiceDTO.getPayment_condition()));
				statusLabel.addStyleName("colored");
				return statusLabel;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//purchaseInvoiceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.purchaseInvoiceDTOTable.addGeneratedColumn("g_status", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final PurchaseInvoiceDTO vPurchaseInvoiceDTO = (PurchaseInvoiceDTO)itemId;	
				final Label statusLabel = new Label(messages.get(vPurchaseInvoiceDTO.getStatus()));
				statusLabel.addStyleName("colored");
				return statusLabel;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//purchaseInvoiceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.purchaseInvoiceDTOTable.setVisibleColumns(new Object[] 
    			{"operations","g_identifier_number","g_emission_date",
    			 "g_supplier","g_payment_condition","g_amount","g_status"});
    	this.purchaseInvoiceDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.purchaseInvoiceDTOTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.purchaseInvoiceDTOTable.setColumnHeader("g_identifier_number", this.messages.get(this.VIEW_NAME + "table.purchase.invoice.column.identifier.number.label"));
    	this.purchaseInvoiceDTOTable.setColumnAlignment("g_identifier_number", Table.Align.RIGHT);
    	
    	this.purchaseInvoiceDTOTable.setColumnHeader("g_supplier", this.messages.get(this.VIEW_NAME + "table.purchase.invoice.column.supplier.label"));
    	this.purchaseInvoiceDTOTable.setColumnAlignment("g_supplier", Table.Align.LEFT);
    	
    	this.purchaseInvoiceDTOTable.setColumnHeader("g_payment_condition", this.messages.get("application.common.payment.condition.selector.description"));
    	this.purchaseInvoiceDTOTable.setColumnAlignment("g_payment_condition", Table.Align.LEFT);
    	
    	this.purchaseInvoiceDTOTable.setColumnHeader("g_emission_date", this.messages.get("application.common.emission.date.indicator.label"));
    	this.purchaseInvoiceDTOTable.setColumnAlignment("g_emission_date", Table.Align.LEFT);
  	
    	this.purchaseInvoiceDTOTable.setColumnHeader("g_amount", this.messages.get("application.common.table.column.total.amount.label"));
    	this.purchaseInvoiceDTOTable.setColumnAlignment("g_amount", Table.Align.RIGHT);
    	
    	this.purchaseInvoiceDTOTable.setColumnHeader("g_status", this.messages.get("application.common.status.label"));
    	this.purchaseInvoiceDTOTable.setColumnAlignment("g_status", Table.Align.LEFT);
    	
    	this.purchaseInvoiceDTOTable.setSizeFull();
    	this.purchaseInvoiceDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.purchaseInvoiceDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.purchaseInvoiceDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.purchaseInvoiceDTOTable.setColumnExpandRatio("operations", 0.0039f); 
    	this.purchaseInvoiceDTOTable.setColumnExpandRatio("g_identifier_number", 0.0052f);
    	this.purchaseInvoiceDTOTable.setColumnExpandRatio("g_supplier", 0.019f);
    	this.purchaseInvoiceDTOTable.setColumnExpandRatio("g_payment_condition", 0.0045f);
    	this.purchaseInvoiceDTOTable.setColumnExpandRatio("g_emission_date", 0.0075f);
    	this.purchaseInvoiceDTOTable.setColumnExpandRatio("g_amount", 0.005f);
    	this.purchaseInvoiceDTOTable.setColumnExpandRatio("g_status", 0.004f);
    	this.purchaseInvoiceDTOTable.setSelectable(true);
    	this.purchaseInvoiceDTOTable.setColumnCollapsingAllowed(true);
    	this.purchaseInvoiceDTOTable.setColumnCollapsible("operations", false);
    	this.purchaseInvoiceDTOTable.setColumnCollapsible("g_identifier_number", true);
    	this.purchaseInvoiceDTOTable.setColumnCollapsible("g_status", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.purchaseInvoiceDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.purchaseInvoiceDTOTable.setSortAscending(false);
    	this.purchaseInvoiceDTOTable.setColumnReorderingAllowed(true);
    	this.purchaseInvoiceDTOTable.setFooterVisible(true);
    	this.purchaseInvoiceDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	
        addComponent(this.purchaseInvoiceDTOTable);
        setExpandRatio(this.purchaseInvoiceDTOTable, 1);
	}

	private HorizontalLayout buildOperationsButtonPanel(final PurchaseInvoiceDTO vPurchaseInvoiceDTO){	
		final Button editButton = new Button();
		editButton.setIcon(vPurchaseInvoiceDTO.getStatus().equals("application.common.status.pending") ? FontAwesome.EDIT : FontAwesome.SEARCH);
		editButton.setDescription(vPurchaseInvoiceDTO.getStatus().equals("application.common.status.pending") ? 
				this.messages.get("application.common.table.column.operations.edit") : this.messages.get("application.common.query.label"));
		editButton.addStyleName("borderless");
		editButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar PurchaseInvoiceDTO...\n" + vPurchaseInvoiceDTO.toString());
                try{		                	
                	goToPurchaseInvoiceRegisterFormView(vPurchaseInvoiceDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		editButton.setVisible(true);
		editButton.setEnabled(true);
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(editButton);
		//operationsButtonPanel.addStyleName("wrapping");
		return operationsButtonPanel;
	}
	
	@Override
	public void goToPurchaseInvoiceRegisterFormView(PurchaseInvoiceDTO vPurchaseInvoiceDTO) {
		// TODO Auto-generated method stub
		try{
			if(vPurchaseInvoiceDTO == null)vPurchaseInvoiceDTO = new PurchaseInvoiceDTO();
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PURCHASE_INVOICE_REGISTER_FORM.getViewName());
			DashboardEventBus.post(new PurchaseInvoiceRegisterFormViewEvent(
					vPurchaseInvoiceDTO, 
					DashboardViewType.PURCHASE_INVOICE_MANAGEMENT.getViewName(),
					vPurchaseInvoiceDTO!=null && vPurchaseInvoiceDTO.getId()!=null));
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
	    if (this.purchaseInvoiceDTOTable!=null && defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.purchaseInvoiceDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.purchaseInvoiceDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
}
