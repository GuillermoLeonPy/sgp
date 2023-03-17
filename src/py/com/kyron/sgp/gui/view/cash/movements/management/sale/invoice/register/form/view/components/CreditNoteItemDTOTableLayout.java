package py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.register.form.view.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteItemDTO;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class CreditNoteItemDTOTableLayout extends VerticalLayout {
	
	private final Logger logger = LoggerFactory.getLogger(CreditNoteItemDTOTableLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_unit_price_amount","g_total_amount"};
    private CommonExceptionErrorNotification commonExceptionErrorNotification;
    private final List<CreditNoteItemDTO> listCreditNoteItemDTO;
    private Table listCreditNoteItemDTOTable;
    private BussinesSessionUtils bussinesSessionUtils;
    private final CreditNoteItemDTOTableFunctions creditNoteItemDTOTableFunctions;
    private int discardedSaleInvoiceItemsCount;

	public CreditNoteItemDTOTableLayout(			
			final String VIEW_NAME,
			CreditNoteItemDTOTableFunctions creditNoteItemDTOTableFunctions,
			final List<CreditNoteItemDTO> listCreditNoteItemDTO) {
		// TODO Auto-generated constructor stub
		this.VIEW_NAME = VIEW_NAME;
		this.creditNoteItemDTOTableFunctions = creditNoteItemDTOTableFunctions;
		this.listCreditNoteItemDTO = listCreditNoteItemDTO;
		try{
			logger.info("\n CreditNoteItemDTOTableLayout()...");
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.initServices();
			this.setSizeFull();
	        //this.addStyleName("transactions");
	        this.setId("CreditNoteItemDTOTableLayout");
			this.setSpacing(true);
			this.setMargin(true);
	        Responsive.makeResponsive(this);
	        DashboardEventBus.register(this);
	    	int listSize = (this.listCreditNoteItemDTO!=null ? this.listCreditNoteItemDTO.size() : 0);
	    	this.setHeight(150f + (listSize*75), Unit.PIXELS);
	        this.buildlistCreditNoteItemDTOTable();
	        this.addComponent(this.listCreditNoteItemDTOTable);
	        this.setExpandRatio(this.listCreditNoteItemDTOTable, 1);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}
	/*public CreditNoteItemDTOTableLayout(Component... children) {
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
	
	private void buildlistCreditNoteItemDTOTable() throws PmsServiceException{
    	this.listCreditNoteItemDTOTable = new Table();
    	BeanItemContainer<CreditNoteItemDTO> CreditNoteItemDTOBeanItemContainer	= new BeanItemContainer<CreditNoteItemDTO>(CreditNoteItemDTO.class);
    	if(this.listCreditNoteItemDTO == null)CreditNoteItemDTOBeanItemContainer.addAll(new ArrayList<CreditNoteItemDTO>());
    	else CreditNoteItemDTOBeanItemContainer.addAll(this.listCreditNoteItemDTO);    	
    	this.listCreditNoteItemDTOTable.setContainerDataSource(CreditNoteItemDTOBeanItemContainer);     	
    	this.listCreditNoteItemDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final CreditNoteItemDTO vCreditNoteItemDTO = (CreditNoteItemDTO)itemId;
				return buildOperationsButtonPanel(vCreditNoteItemDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listCreditNoteItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listCreditNoteItemDTOTable.addGeneratedColumn("g_product", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final CreditNoteItemDTO vCreditNoteItemDTO = (CreditNoteItemDTO)itemId;					
				return vCreditNoteItemDTO.getProductDTO().getProduct_id();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listCreditNoteItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listCreditNoteItemDTOTable.addGeneratedColumn("g_cancellation_withdrawal_quantity", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final CreditNoteItemDTO vCreditNoteItemDTO = (CreditNoteItemDTO)itemId;					
				if(vCreditNoteItemDTO.getCancellation_withdrawal_quantity() == null){
					final Label label = new Label(messages.get(VIEW_NAME + "tab.credit.note.form.table.credit.note.item.column.label.not.established"));				
					label.addStyleName("colored");
					return label;
				}else{
					final ObjectProperty<Long> property = new ObjectProperty<Long>(vCreditNoteItemDTO.getCancellation_withdrawal_quantity());
					final Label label = new Label(property);				
					label.addStyleName("colored");
					final HorizontalLayout vHorizontalLayout = new HorizontalLayout(label);
					vHorizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
					return vHorizontalLayout;
				}
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listCreditNoteItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listCreditNoteItemDTOTable.addGeneratedColumn("g_devolution_quantity", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final CreditNoteItemDTO vCreditNoteItemDTO = (CreditNoteItemDTO)itemId;					
				if(vCreditNoteItemDTO.getDevolution_quantity() == null){
					final Label label = new Label(messages.get(VIEW_NAME + "tab.credit.note.form.table.credit.note.item.column.label.not.established"));				
					label.addStyleName("colored");
					return label;
				}else{
					final ObjectProperty<Long> property = new ObjectProperty<Long>(vCreditNoteItemDTO.getDevolution_quantity());
					final Label label = new Label(property);				
					label.addStyleName("colored");
					final HorizontalLayout vHorizontalLayout = new HorizontalLayout(label);
					vHorizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
					return vHorizontalLayout;
				}
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listCreditNoteItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listCreditNoteItemDTOTable.addGeneratedColumn("g_unit_price_amount", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final CreditNoteItemDTO vCreditNoteItemDTO = (CreditNoteItemDTO)itemId;
				HorizontalLayout vHorizontalLayout = 
				new HorizontalLayout(TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vCreditNoteItemDTO.getUnit_price_amount()));
				vHorizontalLayout.setComponentAlignment(vHorizontalLayout.getComponent(0), Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listCreditNoteItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listCreditNoteItemDTOTable.addGeneratedColumn("g_total_amount", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final CreditNoteItemDTO vCreditNoteItemDTO = (CreditNoteItemDTO)itemId;
				return TableNumericColumnCellLabelHelper.buildValueAddedTaxAmountLabel(vCreditNoteItemDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listCreditNoteItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listCreditNoteItemDTOTable.setVisibleColumns(new Object[] 
    			{"operations",
    			"g_product",
    			"g_cancellation_withdrawal_quantity",
    			"g_devolution_quantity",
    			"g_unit_price_amount",
    			"g_total_amount"});
    	this.listCreditNoteItemDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.listCreditNoteItemDTOTable.setColumnAlignment("operations", Table.Align.LEFT);

    	this.listCreditNoteItemDTOTable.setColumnHeader("g_product", this.messages.get("application.common.table.column.article.label"));
    	this.listCreditNoteItemDTOTable.setColumnAlignment("g_product", Table.Align.LEFT);
    	
    	this.listCreditNoteItemDTOTable.setColumnHeader("g_cancellation_withdrawal_quantity", this.messages.get(this.VIEW_NAME + "tab.credit.note.form.table.credit.note.item.column.cancellation.withdrawal.quantity"));
    	this.listCreditNoteItemDTOTable.setColumnAlignment("g_cancellation_withdrawal_quantity", Table.Align.RIGHT);

    	this.listCreditNoteItemDTOTable.setColumnHeader("g_devolution_quantity", this.messages.get(this.VIEW_NAME + "tab.credit.note.form.table.credit.note.item.column.devolution.quantity"));
    	this.listCreditNoteItemDTOTable.setColumnAlignment("g_devolution_quantity", Table.Align.RIGHT);
    	
    	this.listCreditNoteItemDTOTable.setColumnHeader("g_unit_price_amount", this.messages.get("application.common.table.column.unit.price.amount.label"));
    	this.listCreditNoteItemDTOTable.setColumnAlignment("g_unit_price_amount", Table.Align.RIGHT);
    	
    	this.listCreditNoteItemDTOTable.setColumnHeader("g_total_amount", this.messages.get("application.common.table.column.total.amount.label"));
    	this.listCreditNoteItemDTOTable.setColumnAlignment("g_total_amount", Table.Align.RIGHT);
    	
    	this.listCreditNoteItemDTOTable.setSizeFull();    	
    	this.listCreditNoteItemDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.listCreditNoteItemDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.listCreditNoteItemDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.listCreditNoteItemDTOTable.setColumnExpandRatio("operations", 0.0058f); 
    	this.listCreditNoteItemDTOTable.setColumnExpandRatio("g_product", 0.025f);    	
    	this.listCreditNoteItemDTOTable.setColumnExpandRatio("g_cancellation_withdrawal_quantity", 0.0049f);
    	this.listCreditNoteItemDTOTable.setColumnExpandRatio("g_devolution_quantity", 0.0038f);
    	this.listCreditNoteItemDTOTable.setColumnExpandRatio("g_unit_price_amount", 0.0055f);
    	this.listCreditNoteItemDTOTable.setColumnExpandRatio("g_total_amount", 0.0098f);
    	this.listCreditNoteItemDTOTable.setSelectable(true);
    	this.listCreditNoteItemDTOTable.setColumnCollapsingAllowed(true);
    	this.listCreditNoteItemDTOTable.setColumnCollapsible("operations", false);
    	this.listCreditNoteItemDTOTable.setColumnCollapsible("g_unit_price_amount", true);
    	this.listCreditNoteItemDTOTable.setColumnCollapsible("g_total_amount", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.listCreditNoteItemDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.listCreditNoteItemDTOTable.setSortAscending(false);
    	this.listCreditNoteItemDTOTable.setColumnReorderingAllowed(true);
    	this.listCreditNoteItemDTOTable.setFooterVisible(true);
    	this.listCreditNoteItemDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");   	
	}

	private HorizontalLayout buildOperationsButtonPanel(final CreditNoteItemDTO vCreditNoteItemDTO){
		
		final Button itemIsDiscardedIndicatorButton = new Button();
		itemIsDiscardedIndicatorButton.addStyleName("borderless");
		itemIsDiscardedIndicatorButton.setIcon(FontAwesome.WARNING);
		itemIsDiscardedIndicatorButton.setResponsive(false);
		itemIsDiscardedIndicatorButton.setDescription(messages.get("application.common.status.discarded"));
		itemIsDiscardedIndicatorButton.setVisible(false);
		
		final Button deleteButton = new Button();
		deleteButton.addStyleName("borderless");
		deleteButton.setIcon(FontAwesome.REMOVE);		
		deleteButton.setVisible(true);		
		deleteButton.setDescription(messages.get("application.common.operation.delete.label"));
		deleteButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {                
                try{		                	
                	logger.info("\n delete vCreditNoteItemDTO...\n" + vCreditNoteItemDTO.toString());
                	creditNoteItemDTOTableFunctions.deleteCreditNoteItemDTOFromPreliminaryList(vCreditNoteItemDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		deleteButton.setEnabled( 
				/* as long as update credit note not implemented*/
				this.creditNoteItemDTOTableFunctions.getCreditNoteDTOId() == null
				&& this.listCreditNoteItemDTO.size() > 1
				&& this.creditNoteItemDTOTableFunctions.getCreditNoteDTOStatus().equalsIgnoreCase("application.common.status.pending"));
		
		
		final Button editCancellationWithdrawalQuantityButton = new Button();
		editCancellationWithdrawalQuantityButton.addStyleName("borderless");
		editCancellationWithdrawalQuantityButton.setIcon(FontAwesome.REPLY_ALL);		
		editCancellationWithdrawalQuantityButton.setVisible(true);		
		editCancellationWithdrawalQuantityButton.setDescription(messages.get(this.VIEW_NAME + "tab.credit.note.form.table.credit.note.item.column.operations.button.edit.cancellation.withdrawal.quantity.description"));
		editCancellationWithdrawalQuantityButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {                
                try{		                	
                	logger.info("\n edit editCancellationWithdrawalQuantityButton vCreditNoteItemDTO...\n" + vCreditNoteItemDTO.toString());
                	creditNoteItemDTOTableFunctions.editCancellationWithdrawalQuantity(vCreditNoteItemDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		editCancellationWithdrawalQuantityButton.setEnabled(vCreditNoteItemDTO.getId() == null);
		
		final Button ediDevolutionQuantityButton = new Button();
		ediDevolutionQuantityButton.addStyleName("borderless");
		ediDevolutionQuantityButton.setIcon(FontAwesome.HISTORY);		
		ediDevolutionQuantityButton.setVisible(true);		
		ediDevolutionQuantityButton.setDescription(
				vCreditNoteItemDTO.getId() == null ?
				this.messages.get(this.VIEW_NAME + "tab.credit.note.form.table.credit.note.item.column.operations.button.enter.return.units.description")
				: this.messages.get(this.VIEW_NAME + "tab.credit.note.form.table.credit.note.item.column.operations.button.query.returned.units.description"));
		ediDevolutionQuantityButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {                
                try{		                	
                	logger.info("\n edit ediDevolutionQuantityButton vCreditNoteItemDTO...\n" + vCreditNoteItemDTO.toString());
                	creditNoteItemDTOTableFunctions.showDeliveredUnitsListToSelectDevolutionUnits(vCreditNoteItemDTO,vCreditNoteItemDTO.getId() != null);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		/*editQuantityButton.setEnabled(creditNoteItemDTOTableFunctions.getCreditNoteDTOStatus().equals("application.common.status.revision")
				&& !vCreditNoteItemDTO.getStatus().equals("application.common.status.discarded"));*/
		/*editQuantityButton.setVisible(
				bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
				.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "edit.revision.status.sale.invoice"));*/
		

		Boolean vBoolean = new Boolean(false);
		final ObjectProperty<Boolean> property =	new ObjectProperty<Boolean>(vBoolean);
		CheckBox vCheckBox = new CheckBox(this.messages.get("application.common.operation.discard.label"), property);
		vCheckBox.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				Boolean vBoolean = (Boolean)event.getProperty().getValue();
				logger.info( "\n=========================="
							+"\n discard credit note item check : " + vBoolean	
							+"\n==========================");
				
				creditNoteItemDTOTableFunctions.reBuildTableAndTotalsPanel();//to recalculate table totals
			}			
		});
		
		//vCheckBox.setEnabled(!vCreditNoteItemDTO.getStatus().equals("application.common.status.discarded") && vCreditNoteItemDTO.getPrevious_status() == null);
		vCheckBox.setEnabled(this.checkForEnableDiscardSaleInvoiceItem());
		vCheckBox.setVisible(false
				/*bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
				.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "edit.revision.status.sale.invoice")
				&& this.creditNoteItemDTOTableFunctions.getSaleInvoiceDTOStatus().equalsIgnoreCase("application.common.status.revision")*/);
			
		final HorizontalLayout operationsButtonPanel = 
				new HorizontalLayout(
						itemIsDiscardedIndicatorButton,deleteButton,editCancellationWithdrawalQuantityButton,ediDevolutionQuantityButton,vCheckBox);
		return operationsButtonPanel;
	}
	
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    if (defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.listCreditNoteItemDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.listCreditNoteItemDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
	
	private boolean checkForEnableDiscardSaleInvoiceItem(){
		if(this.listCreditNoteItemDTO.size() == 1)return false;
		this.discardedSaleInvoiceItemsCount = 0;	
		return true;
	}
}
