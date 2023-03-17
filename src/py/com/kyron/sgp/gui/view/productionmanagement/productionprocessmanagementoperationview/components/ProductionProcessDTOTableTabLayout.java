package py.com.kyron.sgp.gui.view.productionmanagement.productionprocessmanagementoperationview.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.ProductDTO;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineRequirementDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.productionmanagement.ProductionProcessManagementOperationView;

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
public class ProductionProcessDTOTableTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(ProductionProcessDTOTableTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.management.operation.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private final ProductDTO productDTO;
	private List<ProductionProcessDTO> listProductionProcessDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final ProductionProcessManagementOperationView productionProcessManagementOperationView;
	private Table productionProcessDTOTable;
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_registration_date","g_validity_end_date"};
    private BussinesSessionUtils bussinesSessionUtils;
    private ProductionManagementService productionManagementService;
    private Button registerProcessButton;
    
	public ProductionProcessDTOTableTabLayout(ProductionProcessManagementOperationView productionProcessManagementOperationView, ProductDTO productDTO) {
		// TODO Auto-generated constructor stub
		this.productionProcessManagementOperationView = productionProcessManagementOperationView;
		this.productDTO = productDTO;
		try{
			logger.info("\n ProductionProcessDTOTableTabLayout..");
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

	/*public ProductionProcessDTOTableTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/

	private void initServices(){
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	private void setUpLayoutContent() throws PmsServiceException{
		this.buildproductionProcessDTOTable();
		addComponent(this.buildToolbar());
        addComponent(productionProcessDTOTable);
        setExpandRatio(productionProcessDTOTable, 1);
	}
	
	private void buildproductionProcessDTOTable() throws PmsServiceException{
    	this.productionProcessDTOTable = new Table();
    	BeanItemContainer<ProductionProcessDTO> ProductionProcessDTOBeanItemContainer	= new BeanItemContainer<ProductionProcessDTO>(ProductionProcessDTO.class);
    	this.updatelistProductionProcessDTO();
    	ProductionProcessDTOBeanItemContainer.addAll(this.listProductionProcessDTO);    	
    	this.productionProcessDTOTable.setContainerDataSource(ProductionProcessDTOBeanItemContainer); 
    	
    	this.productionProcessDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessDTO vProductionProcessDTO = (ProductionProcessDTO)itemId;
				return buildOperationsButtonPanel(vProductionProcessDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//productionProcessDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessDTOTable.addGeneratedColumn("g_registration_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessDTO vProductionProcessDTO = (ProductionProcessDTO)itemId;
				return SgpUtils.dateFormater.format(vProductionProcessDTO.getRegistration_date());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessDTOTable.addGeneratedColumn("g_validity_end_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessDTO vProductionProcessDTO = (ProductionProcessDTO)itemId;
				return (vProductionProcessDTO.getValidity_end_date()!=null ? 
						SgpUtils.dateFormater.format(vProductionProcessDTO.getValidity_end_date()):buildIsValidLabel());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessDTOTable.setVisibleColumns(new Object[] {"operations","process_id","g_registration_date","g_validity_end_date"});
    	this.productionProcessDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.productionProcessDTOTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.productionProcessDTOTable.setColumnHeader("process_id", this.messages.get("application.common.process.label"));
    	this.productionProcessDTOTable.setColumnAlignment("process_id", Table.Align.LEFT);
    	
    	this.productionProcessDTOTable.setColumnHeader("g_registration_date", this.messages.get("application.common.table.column.registration.date.label"));
    	this.productionProcessDTOTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
  	
    	this.productionProcessDTOTable.setColumnHeader("g_validity_end_date", this.messages.get("application.common.table.column.validity.end.date.label"));
    	this.productionProcessDTOTable.setColumnAlignment("g_validity_end_date", Table.Align.LEFT);
    	
    	this.productionProcessDTOTable.setSizeFull();
    	this.productionProcessDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.productionProcessDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.productionProcessDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.productionProcessDTOTable.setColumnExpandRatio("operations", 0.012f); 
    	this.productionProcessDTOTable.setColumnExpandRatio("process_id", 0.019f);
    	this.productionProcessDTOTable.setColumnExpandRatio("g_registration_date", 0.016f);
    	this.productionProcessDTOTable.setColumnExpandRatio("g_validity_end_date", 0.016f);
    	this.productionProcessDTOTable.setSelectable(true);
    	this.productionProcessDTOTable.setColumnCollapsingAllowed(true);
    	this.productionProcessDTOTable.setColumnCollapsible("operations", false);
    	this.productionProcessDTOTable.setColumnCollapsible("g_supplier_data", true);
    	this.productionProcessDTOTable.setColumnCollapsible("g_validity_end_date", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.productionProcessDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.productionProcessDTOTable.setSortAscending(false);
    	this.productionProcessDTOTable.setColumnReorderingAllowed(true);
    	this.productionProcessDTOTable.setFooterVisible(true);
    	this.productionProcessDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
    private void updatelistProductionProcessDTO() throws PmsServiceException{
    	this.listProductionProcessDTO = this.productionManagementService.listProductionProcessDTO(new ProductionProcessDTO(null,this.productDTO.getId()));
    	if(this.listProductionProcessDTO == null) this.listProductionProcessDTO = new ArrayList<ProductionProcessDTO>();
    }
    
	private Component buildToolbar() {
	        HorizontalLayout header = new HorizontalLayout();
	        header.addStyleName("viewheader");
	        header.setSpacing(true);
	        Responsive.makeResponsive(header);
	        logger.info("\n*********************remember: an HorizontalLayout is set with the instrucction: Responsive.makeResponsive(header); !!! "
	        		+"\n*********************");
	        Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.production.process.main.title"));
	        title.setSizeUndefined();
	        title.addStyleName(ValoTheme.LABEL_H1);
	        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	        header.addComponent(title);
	        
	        HorizontalLayout tools = new HorizontalLayout(this.createRegisterProcessButton());	        	        
	        tools.setSpacing(true);
	        tools.addStyleName("toolbar");
	        header.addComponent(tools);
	        return header;
	 }//private Component buildToolbar()
	
	 private Button createRegisterProcessButton(){
	        this.registerProcessButton = new Button(this.messages.get(this.VIEW_NAME + "tab.production.process.button.register.process"));
	        this.registerProcessButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.production.process.button.register.process.description"));
	        this.registerProcessButton.addClickListener(new ClickListener() {
	            @Override
	            public void buttonClick(final ClickEvent event) {
	            	
	            	productionProcessManagementOperationView.editProductionProcessDTO(null);
	            }
	        });
	        //this.registerProcessButton.setEnabled(true);
	        this.updateStatusRegisterProcessButton();
	        return this.registerProcessButton;
	 }//private Button createRegisterMachineButton()
	 
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    // Some columns are collapsed when browser window width gets small
	    // enough to make the table fit better.
	    if (defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.productionProcessDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.productionProcessDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
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
				+"\n ProductionProcessDTOTableTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
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
	
	private HorizontalLayout buildOperationsButtonPanel(final ProductionProcessDTO vProductionProcessDTO){
		/*final Button process_disabledButton = new Button();
		process_disabledButton.addStyleName("borderless");
		process_disabledButton.setIcon(FontAwesome.SAVE);		
		process_disabledButton.setVisible(vProductionProcessDTO.getIs_enable()!=null && !vProductionProcessDTO.getIs_enable());		
		process_disabledButton.setDescription(messages.get(VIEW_NAME + "tab.production.process.activities.table.activities.column.operations.button.activity.pending.to.be.saved.description"));
		process_disabledButton.setResponsive(false);*/
		
		final Button editButton = new Button();
		editButton.setDescription(messages.get("application.common.table.column.operations.edit"));
		editButton.setIcon(FontAwesome.EDIT);
		editButton.addStyleName("borderless");
		editButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar ProductionProcessDTO...\n" + vProductionProcessDTO.toString());
                try{		                	
                	productionProcessManagementOperationView.editProductionProcessDTO(vProductionProcessDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		
		try {
			editButton.setEnabled(vProductionProcessDTO.getValidity_end_date() == null
			/*&& bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
					.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "tab.rawmaterial.purchase.request.form")*/);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("\nerror",e);
		}
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(editButton/*,process_disabledButton*/);
		//operationsButtonPanel.addStyleName("wrapping");
		return operationsButtonPanel;
	}
	
	private void updateStatusRegisterProcessButton(){
		if(this.listProductionProcessDTO == null || this.listProductionProcessDTO.isEmpty())
			this.registerProcessButton.setEnabled(true);
		else{
			boolean loopBreaked = false;
			for(ProductionProcessDTO vProductionProcessDTO:this.listProductionProcessDTO){
				if(vProductionProcessDTO.getValidity_end_date()==null){
					this.registerProcessButton.setEnabled(false);
					loopBreaked=true;
					break;
				}
			}
			if(!loopBreaked)this.registerProcessButton.setEnabled(true);
		}
	}
}
