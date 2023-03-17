package py.com.kyron.sgp.gui.view.cash.movements.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceDTO;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.SaleInvoiceRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.management.view.components.SearchSaleInvoiceToolComponent;
import py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.management.view.components.SearchSaleInvoiceToolComponentHostView;
import py.com.kyron.sgp.gui.view.utils.TableNumericColumnCellLabelHelper;

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
public class SaleInvoiceManagementView extends VerticalLayout implements View,SearchSaleInvoiceToolComponentHostView {

	private final Logger logger = LoggerFactory.getLogger(SaleInvoiceManagementView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "sale.invoice.management.";
	private static final String[] DEFAULT_COLLAPSIBLE = {"g_customer","g_payment_condition","g_registration_date","g_amount"};
	private SearchSaleInvoiceToolComponent searchSaleInvoiceToolComponent;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Table saleInvoiceDTOTable;
	private List<SaleInvoiceDTO> listSaleInvoiceDTO;
	
	public SaleInvoiceManagementView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n SaleInvoiceManagementView()...");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        this.searchSaleInvoiceToolComponent = new SearchSaleInvoiceToolComponent(this,this.VIEW_NAME, true);
	        this.addComponent(this.searchSaleInvoiceToolComponent);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public SaleInvoiceManagementView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/

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
	public void buildSaleInvoiceDTOTable(List<SaleInvoiceDTO> listSaleInvoiceDTO)
			throws Exception {
		// TODO Auto-generated method stub
		if(this.saleInvoiceDTOTable!=null)this.removeComponent(this.saleInvoiceDTOTable);
		if(listSaleInvoiceDTO!=null)this.listSaleInvoiceDTO = listSaleInvoiceDTO; else this.listSaleInvoiceDTO = new ArrayList<SaleInvoiceDTO>();
    	this.saleInvoiceDTOTable = new Table();
    	BeanItemContainer<SaleInvoiceDTO> SaleInvoiceDTOBeanItemContainer	= new BeanItemContainer<SaleInvoiceDTO>(SaleInvoiceDTO.class);
    	SaleInvoiceDTOBeanItemContainer.addAll(this.listSaleInvoiceDTO);    	
    	this.saleInvoiceDTOTable.setContainerDataSource(SaleInvoiceDTOBeanItemContainer); 
    	
    	this.saleInvoiceDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceDTO vSaleInvoiceDTO = (SaleInvoiceDTO)itemId;
				return buildOperationsButtonPanel(vSaleInvoiceDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//saleInvoiceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoiceDTOTable.addGeneratedColumn("g_identifier_number", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceDTO vSaleInvoiceDTO = (SaleInvoiceDTO)itemId;
				final Label identifierNumberLabel = new Label(vSaleInvoiceDTO.getIdentifier_number());
				identifierNumberLabel.addStyleName("colored");
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(identifierNumberLabel);
				vHorizontalLayout.setSizeFull();
				vHorizontalLayout.setComponentAlignment(identifierNumberLabel, Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//saleInvoiceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoiceDTOTable.addGeneratedColumn("g_order_identifier_number", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceDTO vSaleInvoiceDTO = (SaleInvoiceDTO)itemId;
				final Label identifierNumberLabel = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vSaleInvoiceDTO.getOrderDTO().getIdentifier_number());
				identifierNumberLabel.addStyleName("colored");
				/*final Label spaceSeparatorLabel = new Label("");
				spaceSeparatorLabel.addStyleName("colored");
				spaceSeparatorLabel.setSizeFull();*/
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(/*spaceSeparatorLabel,*/identifierNumberLabel);
				//vHorizontalLayout.setSizeFull();
				//vHorizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
				//vHorizontalLayout.setComponentAlignment(spaceSeparatorLabel, Alignment.MIDDLE_LEFT);
				vHorizontalLayout.setComponentAlignment(identifierNumberLabel, Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//saleInvoiceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoiceDTOTable.addGeneratedColumn("g_customer", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceDTO vSaleInvoiceDTO = (SaleInvoiceDTO)itemId;					
				return (vSaleInvoiceDTO.getOrderDTO().getPersonDTO().getCommercial_name()!=null ? 
						vSaleInvoiceDTO.getOrderDTO().getPersonDTO().getCommercial_name() : 
						vSaleInvoiceDTO.getOrderDTO().getPersonDTO().getPersonal_name() + "," + 
						vSaleInvoiceDTO.getOrderDTO().getPersonDTO().getPersonal_last_name());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//saleInvoiceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoiceDTOTable.addGeneratedColumn("g_amount", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceDTO vSaleInvoiceDTO = (SaleInvoiceDTO)itemId;
				final Label currencyLabel = new Label(vSaleInvoiceDTO.getOrderDTO().getCurrencyDTO().getId_code());
    			currencyLabel.addStyleName("colored");
    			final Label amountLabel = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vSaleInvoiceDTO.getTotal_amount());
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(currencyLabel,amountLabel);
				vHorizontalLayout.setComponentAlignment(currencyLabel, Alignment.MIDDLE_LEFT);
				vHorizontalLayout.setComponentAlignment(amountLabel, Alignment.MIDDLE_RIGHT);				
				vHorizontalLayout.setSizeFull();
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//saleInvoiceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoiceDTOTable.addGeneratedColumn("g_registration_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceDTO vSaleInvoiceDTO = (SaleInvoiceDTO)itemId;
				return SgpUtils.dateFormater.format(vSaleInvoiceDTO.getEmission_date());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoiceDTOTable.addGeneratedColumn("g_payment_condition", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceDTO vSaleInvoiceDTO = (SaleInvoiceDTO)itemId;	
				final Label statusLabel = new Label(messages.get(vSaleInvoiceDTO.getPayment_condition()));
				statusLabel.addStyleName("colored");
				return statusLabel;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//saleInvoiceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoiceDTOTable.addGeneratedColumn("g_status", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SaleInvoiceDTO vSaleInvoiceDTO = (SaleInvoiceDTO)itemId;	
				final Label statusLabel = new Label(messages.get(vSaleInvoiceDTO.getStatus()));
				statusLabel.addStyleName("colored");
				return statusLabel;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//saleInvoiceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.saleInvoiceDTOTable.setVisibleColumns(new Object[] 
    			{"operations","g_identifier_number","g_order_identifier_number","g_registration_date",
    			 "g_customer","g_payment_condition","g_amount","g_status"});
    	this.saleInvoiceDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.saleInvoiceDTOTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.saleInvoiceDTOTable.setColumnHeader("g_identifier_number", this.messages.get("application.common.sale.invoice.number.indicator.label"));
    	this.saleInvoiceDTOTable.setColumnAlignment("g_identifier_number", Table.Align.RIGHT);

    	this.saleInvoiceDTOTable.setColumnHeader("g_order_identifier_number", this.messages.get("application.common.order.number.indicator.label"));
    	this.saleInvoiceDTOTable.setColumnAlignment("g_order_identifier_number", Table.Align.RIGHT);
    	
    	this.saleInvoiceDTOTable.setColumnHeader("g_customer", this.messages.get("application.common.customer.label"));
    	this.saleInvoiceDTOTable.setColumnAlignment("g_customer", Table.Align.LEFT);
    	
    	this.saleInvoiceDTOTable.setColumnHeader("g_payment_condition", this.messages.get("application.common.payment.condition.selector.description"));
    	this.saleInvoiceDTOTable.setColumnAlignment("g_payment_condition", Table.Align.LEFT);
    	
    	this.saleInvoiceDTOTable.setColumnHeader("g_registration_date", this.messages.get("application.common.table.column.registration.date.label"));
    	this.saleInvoiceDTOTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
  	
    	this.saleInvoiceDTOTable.setColumnHeader("g_amount", this.messages.get("application.common.table.column.total.amount.label"));
    	this.saleInvoiceDTOTable.setColumnAlignment("g_amount", Table.Align.RIGHT);
    	
    	this.saleInvoiceDTOTable.setColumnHeader("g_status", this.messages.get("application.common.status.label"));
    	this.saleInvoiceDTOTable.setColumnAlignment("g_status", Table.Align.LEFT);
    	
    	this.saleInvoiceDTOTable.setSizeFull();
    	this.saleInvoiceDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.saleInvoiceDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.saleInvoiceDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.saleInvoiceDTOTable.setColumnExpandRatio("operations", 0.0039f); 
    	this.saleInvoiceDTOTable.setColumnExpandRatio("g_identifier_number", 0.0052f);
    	this.saleInvoiceDTOTable.setColumnExpandRatio("g_order_identifier_number", 0.004f);
    	this.saleInvoiceDTOTable.setColumnExpandRatio("g_customer", 0.019f);
    	this.saleInvoiceDTOTable.setColumnExpandRatio("g_payment_condition", 0.0045f);
    	this.saleInvoiceDTOTable.setColumnExpandRatio("g_registration_date", 0.0075f);
    	this.saleInvoiceDTOTable.setColumnExpandRatio("g_amount", 0.005f);
    	this.saleInvoiceDTOTable.setColumnExpandRatio("g_status", 0.004f);
    	this.saleInvoiceDTOTable.setSelectable(true);
    	this.saleInvoiceDTOTable.setColumnCollapsingAllowed(true);
    	this.saleInvoiceDTOTable.setColumnCollapsible("operations", false);
    	this.saleInvoiceDTOTable.setColumnCollapsible("g_identifier_number", true);
    	this.saleInvoiceDTOTable.setColumnCollapsible("g_status", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.saleInvoiceDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.saleInvoiceDTOTable.setSortAscending(false);
    	this.saleInvoiceDTOTable.setColumnReorderingAllowed(true);
    	this.saleInvoiceDTOTable.setFooterVisible(true);
    	this.saleInvoiceDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	
        addComponent(this.saleInvoiceDTOTable);
        setExpandRatio(this.saleInvoiceDTOTable, 1);
	}

	private HorizontalLayout buildOperationsButtonPanel(final SaleInvoiceDTO vSaleInvoiceDTO){	
		final Button editButton = new Button();
		editButton.setIcon(vSaleInvoiceDTO.getStatus().equals("application.common.status.revision") ? FontAwesome.EDIT : FontAwesome.SEARCH);
		editButton.setDescription(vSaleInvoiceDTO.getStatus().equals("application.common.status.revision") ? 
				this.messages.get("application.common.table.column.operations.edit") : this.messages.get("application.common.query.label"));
		editButton.addStyleName("borderless");
		editButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar vSaleInvoiceDTO...\n" + vSaleInvoiceDTO.toString());
                try{		                	
                	goToSaleInvoiceRegisterFormView(vSaleInvoiceDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		editButton.setVisible(vSaleInvoiceDTO.getId()!=null);
		editButton.setEnabled(true);
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(editButton);
		//operationsButtonPanel.addStyleName("wrapping");
		return operationsButtonPanel;
	}
	
    private void goToSaleInvoiceRegisterFormView(final SaleInvoiceDTO vSaleInvoiceDTO){
		try{						
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.SALE_INVOICE_REGISTER_FORM.getViewName());
			DashboardEventBus.post(new SaleInvoiceRegisterFormViewEvent(
					null, 
					vSaleInvoiceDTO,
					DashboardViewType.SALE_INVOICE_MANAGEMENT.getViewName(),
					vSaleInvoiceDTO!=null && vSaleInvoiceDTO.getId()!=null && vSaleInvoiceDTO.getStatus().equals("application.common.status.revision")));
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
	    if (this.saleInvoiceDTOTable!=null && defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.saleInvoiceDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.saleInvoiceDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
}
