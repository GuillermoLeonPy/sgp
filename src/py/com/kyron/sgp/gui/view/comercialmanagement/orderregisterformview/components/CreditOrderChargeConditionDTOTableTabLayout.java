package py.com.kyron.sgp.gui.view.comercialmanagement.orderregisterformview.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CreditOrderChargeConditionDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.comercialmanagement.OrderRegisterFormView;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
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
public class CreditOrderChargeConditionDTOTableTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(CreditOrderChargeConditionDTOTableTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "order.management.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private List<CreditOrderChargeConditionDTO> listCreditOrderChargeConditionDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final OrderRegisterFormView orderRegisterFormView;
	private Table creditOrderChargeConditionDTOTable;
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_registration_date","g_validity_end_date"};
    private BussinesSessionUtils bussinesSessionUtils;
    private ComercialManagementService comercialManagementService;
    
	public CreditOrderChargeConditionDTOTableTabLayout(final OrderRegisterFormView orderRegisterFormView) {
		// TODO Auto-generated constructor stub
		this.orderRegisterFormView = orderRegisterFormView;
		try{
			logger.info("\n CreditOrderChargeConditionDTOTableTabLayout..");
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

	/*public CreditOrderChargeConditionDTOTableTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	private void initServices() throws Exception{
		this.comercialManagementService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
	}
	
	private void setUpLayoutContent() throws PmsServiceException{
		this.buildcreditOrderChargeConditionDTOTable();
        addComponent(creditOrderChargeConditionDTOTable);
        setExpandRatio(creditOrderChargeConditionDTOTable, 1);
	}
	
	private void buildcreditOrderChargeConditionDTOTable() throws PmsServiceException{
    	this.creditOrderChargeConditionDTOTable = new Table();
    	BeanItemContainer<CreditOrderChargeConditionDTO> CreditOrderChargeConditionDTOBeanItemContainer	= new BeanItemContainer<CreditOrderChargeConditionDTO>(CreditOrderChargeConditionDTO.class);
    	this.updatelistCreditOrderChargeConditionDTO();
    	CreditOrderChargeConditionDTOBeanItemContainer.addAll(this.listCreditOrderChargeConditionDTO);    	
    	this.creditOrderChargeConditionDTOTable.setContainerDataSource(CreditOrderChargeConditionDTOBeanItemContainer); 
    	
    	this.creditOrderChargeConditionDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final CreditOrderChargeConditionDTO vCreditOrderChargeConditionDTO = (CreditOrderChargeConditionDTO)itemId;
				return buildOperationsButtonPanel(vCreditOrderChargeConditionDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//creditOrderChargeConditionDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.creditOrderChargeConditionDTOTable.addGeneratedColumn("g_registration_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final CreditOrderChargeConditionDTO vCreditOrderChargeConditionDTO = (CreditOrderChargeConditionDTO)itemId;
				if(vCreditOrderChargeConditionDTO.getId()!=null && vCreditOrderChargeConditionDTO.getRegistration_date() != null )
					return SgpUtils.dateFormater.format(vCreditOrderChargeConditionDTO.getRegistration_date());
				else if(vCreditOrderChargeConditionDTO.getId()!=null && vCreditOrderChargeConditionDTO.getRegistration_date() == null )
					return SgpUtils.dateFormater.format(new Date());
				else
					return buildIsInPreliminarySaveStatusLabel();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.creditOrderChargeConditionDTOTable.addGeneratedColumn("g_validity_end_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final CreditOrderChargeConditionDTO vCreditOrderChargeConditionDTO = (CreditOrderChargeConditionDTO)itemId;
				if(vCreditOrderChargeConditionDTO.getId()!=null && vCreditOrderChargeConditionDTO.getValidity_end_date()!=null)
					return SgpUtils.dateFormater.format(vCreditOrderChargeConditionDTO.getValidity_end_date());
				else if (vCreditOrderChargeConditionDTO.getId()!=null && vCreditOrderChargeConditionDTO.getValidity_end_date()==null)
					return buildIsValidLabel();
				else
					return buildIsInPreliminarySaveStatusLabel();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.creditOrderChargeConditionDTOTable.setVisibleColumns(new Object[] {"operations","days_interval","days_interval_percent_increment","g_registration_date","g_validity_end_date"});
    	this.creditOrderChargeConditionDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.creditOrderChargeConditionDTOTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.creditOrderChargeConditionDTOTable.setColumnHeader("days_interval", this.messages.get(this.VIEW_NAME + "tab.credit.order.charge.condition.table.column.days.interval.label"));
    	this.creditOrderChargeConditionDTOTable.setColumnAlignment("days_interval", Table.Align.RIGHT);

    	this.creditOrderChargeConditionDTOTable.setColumnHeader("days_interval_percent_increment", this.messages.get(this.VIEW_NAME + "tab.credit.order.charge.condition.table.column.days.interval.percent.increment.label"));
    	this.creditOrderChargeConditionDTOTable.setColumnAlignment("days_interval_percent_increment", Table.Align.RIGHT);
    	
    	this.creditOrderChargeConditionDTOTable.setColumnHeader("g_registration_date", this.messages.get("application.common.table.column.registration.date.label"));
    	this.creditOrderChargeConditionDTOTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
  	
    	this.creditOrderChargeConditionDTOTable.setColumnHeader("g_validity_end_date", this.messages.get("application.common.table.column.validity.end.date.label"));
    	this.creditOrderChargeConditionDTOTable.setColumnAlignment("g_validity_end_date", Table.Align.LEFT);
    	
    	this.creditOrderChargeConditionDTOTable.setSizeFull();
    	this.creditOrderChargeConditionDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.creditOrderChargeConditionDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.creditOrderChargeConditionDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.creditOrderChargeConditionDTOTable.setColumnExpandRatio("operations", 0.009f); 
    	this.creditOrderChargeConditionDTOTable.setColumnExpandRatio("days_interval", 0.019f);
    	this.creditOrderChargeConditionDTOTable.setColumnExpandRatio("days_interval_percent_increment", 0.019f);
    	this.creditOrderChargeConditionDTOTable.setColumnExpandRatio("g_registration_date", 0.011f);
    	this.creditOrderChargeConditionDTOTable.setColumnExpandRatio("g_validity_end_date", 0.011f);
    	this.creditOrderChargeConditionDTOTable.setSelectable(true);
    	this.creditOrderChargeConditionDTOTable.setColumnCollapsingAllowed(true);
    	this.creditOrderChargeConditionDTOTable.setColumnCollapsible("operations", false);
    	this.creditOrderChargeConditionDTOTable.setColumnCollapsible("g_registration_date", true);
    	this.creditOrderChargeConditionDTOTable.setColumnCollapsible("g_validity_end_date", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.creditOrderChargeConditionDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.creditOrderChargeConditionDTOTable.setSortAscending(false);
    	this.creditOrderChargeConditionDTOTable.setColumnReorderingAllowed(true);
    	this.creditOrderChargeConditionDTOTable.setFooterVisible(true);
    	this.creditOrderChargeConditionDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
    private void updatelistCreditOrderChargeConditionDTO() throws PmsServiceException{

    	this.listCreditOrderChargeConditionDTO = this.comercialManagementService.listCreditOrderChargeConditionDTO(null);
    	if(this.listCreditOrderChargeConditionDTO == null) this.listCreditOrderChargeConditionDTO = new ArrayList<CreditOrderChargeConditionDTO>();
    }
    
	 
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    // Some columns are collapsed when browser window width gets small
	    // enough to make the table fit better.
	    if (defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.creditOrderChargeConditionDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.creditOrderChargeConditionDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n CreditOrderChargeConditionDTOTableTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private HorizontalLayout buildOperationsButtonPanel(final CreditOrderChargeConditionDTO vCreditOrderChargeConditionDTO){
	       
		final Button editButton = new Button();
		editButton.setDescription(messages.get("application.common.set.end.validity.date.button.label"));
		editButton.setIcon(FontAwesome.EDIT);
		editButton.addStyleName("borderless");
		editButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar CreditOrderChargeConditionDTO...\n" + vCreditOrderChargeConditionDTO.toString());
                try{		                	
                	orderRegisterFormView.editCreditOrderChargeConditionDTO(vCreditOrderChargeConditionDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		
		try {
			editButton.setEnabled(vCreditOrderChargeConditionDTO.getValidity_end_date() == null
			/*&& bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
					.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "tab.rawmaterial.purchase.request.form")*/);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("\nerror",e);
		}
		
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(editButton);
		//operationsButtonPanel.addStyleName("wrapping");
		return operationsButtonPanel;
	}
	
	private Label buildIsValidLabel(){
	    final Label isValidLabel = new Label(this.messages.get("application.common.validity.end.date.is.valid.value"));
	    isValidLabel.addStyleName(ValoTheme.LABEL_COLORED);
	    return isValidLabel;
	}

	private Label buildIsInPreliminarySaveStatusLabel(){
		final Label isInPreliminarySaveStatusLabel = new Label(this.messages.get("application.common.preliminary.save.status"));
	    isInPreliminarySaveStatusLabel.addStyleName(ValoTheme.LABEL_COLORED);
	    return isInPreliminarySaveStatusLabel;
	}
}
