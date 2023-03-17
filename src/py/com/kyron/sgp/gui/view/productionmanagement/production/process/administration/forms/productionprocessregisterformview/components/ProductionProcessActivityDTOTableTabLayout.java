package py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessregisterformview.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.ProductDTO;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.ProductionProcessRegisterFormView;

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
public class ProductionProcessActivityDTOTableTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(ProductionProcessActivityDTOTableTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.register.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private final ProductionProcessDTO productionProcessDTO;
	private List<ProductionProcessActivityDTO> listProductionProcessActivityDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final ProductionProcessRegisterFormView productionProcessRegisterFormView;
	private Table productionProcessActivityDTOTable;
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_registration_date","g_validity_end_date"};
    private BussinesSessionUtils bussinesSessionUtils;
    private ProductionManagementService productionManagementService;
    
	public ProductionProcessActivityDTOTableTabLayout(ProductionProcessRegisterFormView productionProcessRegisterFormView, ProductionProcessDTO productionProcessDTO) {
		// TODO Auto-generated constructor stub
		this.productionProcessRegisterFormView = productionProcessRegisterFormView;
		this.productionProcessDTO = productionProcessDTO;
		try{
			logger.info("\n productionProcessActivityDTOTableTabLayout..");
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

	/*public ProductionProcessActivityDTOTableTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/

	private void initServices(){
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	private void setUpLayoutContent() throws PmsServiceException{
		this.buildproductionProcessActivityDTOTable();
		addComponent(this.buildToolbar());
        addComponent(productionProcessActivityDTOTable);
        setExpandRatio(productionProcessActivityDTOTable, 1);
	}
	
	private void buildproductionProcessActivityDTOTable() throws PmsServiceException{
    	this.productionProcessActivityDTOTable = new Table();
    	BeanItemContainer<ProductionProcessActivityDTO> productionProcessActivityDTOBeanItemContainer	= new BeanItemContainer<ProductionProcessActivityDTO>(ProductionProcessActivityDTO.class);
    	this.updatelistProductionProcessActivityDTO();
    	productionProcessActivityDTOBeanItemContainer.addAll(this.listProductionProcessActivityDTO);    	
    	this.productionProcessActivityDTOTable.setContainerDataSource(productionProcessActivityDTOBeanItemContainer); 
    	
    	this.productionProcessActivityDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessActivityDTO vProductionProcessActivityDTO = (ProductionProcessActivityDTO)itemId;
				return buildOperationsButtonPanel(vProductionProcessActivityDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//productionProcessActivityDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessActivityDTOTable.addGeneratedColumn("g_registration_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessActivityDTO vProductionProcessActivityDTO = (ProductionProcessActivityDTO)itemId;
				if(vProductionProcessActivityDTO.getId()!=null && vProductionProcessActivityDTO.getRegistration_date() != null )
					return SgpUtils.dateFormater.format(vProductionProcessActivityDTO.getRegistration_date());
				else if(vProductionProcessActivityDTO.getId()!=null && vProductionProcessActivityDTO.getRegistration_date() == null )
					return SgpUtils.dateFormater.format(new Date());
				else
					return buildIsInPreliminarySaveStatusLabel();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessActivityDTOTable.addGeneratedColumn("g_validity_end_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessActivityDTO vProductionProcessActivityDTO = (ProductionProcessActivityDTO)itemId;
				if(vProductionProcessActivityDTO.getId()!=null && vProductionProcessActivityDTO.getValidity_end_date()!=null)
					return SgpUtils.dateFormater.format(vProductionProcessActivityDTO.getValidity_end_date());
				else if (vProductionProcessActivityDTO.getId()!=null && vProductionProcessActivityDTO.getValidity_end_date()==null)
					return buildIsValidLabel();
				else
					return buildIsInPreliminarySaveStatusLabel();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessActivityDTOTable.setVisibleColumns(new Object[] {"operations","activity_id","minutes_quantity","g_registration_date","g_validity_end_date"});
    	this.productionProcessActivityDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.productionProcessActivityDTOTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.productionProcessActivityDTOTable.setColumnHeader("activity_id", this.messages.get("application.common.activity.label"));
    	this.productionProcessActivityDTOTable.setColumnAlignment("activity_id", Table.Align.LEFT);

    	this.productionProcessActivityDTOTable.setColumnHeader("minutes_quantity", this.messages.get("application.common.time.space.minutes.label"));
    	this.productionProcessActivityDTOTable.setColumnAlignment("minutes_quantity", Table.Align.RIGHT);
    	
    	this.productionProcessActivityDTOTable.setColumnHeader("g_registration_date", this.messages.get("application.common.table.column.registration.date.label"));
    	this.productionProcessActivityDTOTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
  	
    	this.productionProcessActivityDTOTable.setColumnHeader("g_validity_end_date", this.messages.get("application.common.table.column.validity.end.date.label"));
    	this.productionProcessActivityDTOTable.setColumnAlignment("g_validity_end_date", Table.Align.LEFT);
    	
    	this.productionProcessActivityDTOTable.setSizeFull();
    	this.productionProcessActivityDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.productionProcessActivityDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.productionProcessActivityDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.productionProcessActivityDTOTable.setColumnExpandRatio("operations", 0.009f); 
    	this.productionProcessActivityDTOTable.setColumnExpandRatio("activity_id", 0.019f);
    	this.productionProcessActivityDTOTable.setColumnExpandRatio("minutes_quantity", 0.0089f);
    	this.productionProcessActivityDTOTable.setColumnExpandRatio("g_registration_date", 0.011f);
    	this.productionProcessActivityDTOTable.setColumnExpandRatio("g_validity_end_date", 0.011f);
    	this.productionProcessActivityDTOTable.setSelectable(true);
    	this.productionProcessActivityDTOTable.setColumnCollapsingAllowed(true);
    	this.productionProcessActivityDTOTable.setColumnCollapsible("operations", false);
    	this.productionProcessActivityDTOTable.setColumnCollapsible("g_registration_date", true);
    	this.productionProcessActivityDTOTable.setColumnCollapsible("g_validity_end_date", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.productionProcessActivityDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.productionProcessActivityDTOTable.setSortAscending(false);
    	this.productionProcessActivityDTOTable.setColumnReorderingAllowed(true);
    	this.productionProcessActivityDTOTable.setFooterVisible(true);
    	this.productionProcessActivityDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
    private void updatelistProductionProcessActivityDTO() throws PmsServiceException{
    	/*if(this.productionProcessDTO.getId()!=null)
    		this.listProductionProcessActivityDTO = this.productionManagementService.listProductionProcessActivityDTOByIdProductionProcess(this.productionProcessDTO.getId());
    		*/
    	this.checkActivitiesOrderInTheList();
    	this.listProductionProcessActivityDTO = this.productionProcessDTO.getListProductionProcessActivityDTO();
    	if(this.listProductionProcessActivityDTO == null) this.listProductionProcessActivityDTO = new ArrayList<ProductionProcessActivityDTO>();
    }
    
	private Component buildToolbar() {
	        HorizontalLayout header = new HorizontalLayout();
	        header.addStyleName("viewheader");
	        header.setSpacing(true);
	        Responsive.makeResponsive(header);
	        logger.info("\n*********************remember: an HorizontalLayout is set with the instrucction: Responsive.makeResponsive(header); !!! "
	        		+"\n*********************");
	        Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.production.process.activities.main.title"));
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
	        final Button registerRawMaterial = new Button(this.messages.get(this.VIEW_NAME + "tab.production.process.activities.button.register.activity"));
	        registerRawMaterial.setDescription(this.messages.get(this.VIEW_NAME + "tab.production.process.activities.button.register.activity.description"));
	        registerRawMaterial.addClickListener(new ClickListener() {
	            @Override
	            public void buttonClick(final ClickEvent event) {
	            	/*editRawMaterial(null, true);*/

	            	productionProcessRegisterFormView.editProductionProcessActivityDTO(null);
	            }
	        });
	        registerRawMaterial.setEnabled(true);
	        return registerRawMaterial;
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
	    	   this.productionProcessActivityDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.productionProcessActivityDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
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
				+"\n ProductionProcessActivityDTOTableTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private HorizontalLayout buildOperationsButtonPanel(final ProductionProcessActivityDTO vProductionProcessActivityDTO){
	       
		final Button activity_pending_to_be_savedButton = new Button();
		activity_pending_to_be_savedButton.addStyleName("borderless");
		activity_pending_to_be_savedButton.setIcon(FontAwesome.SAVE);		
		activity_pending_to_be_savedButton.setVisible(vProductionProcessActivityDTO.getId() == null);		
		activity_pending_to_be_savedButton.setDescription(messages.get(VIEW_NAME + "tab.production.process.activities.table.activities.column.operations.button.activity.pending.to.be.saved.description"));
		activity_pending_to_be_savedButton.setResponsive(false);
		
		final Button editButton = new Button();
		editButton.setDescription(messages.get("application.common.table.column.operations.edit"));
		editButton.setIcon(FontAwesome.EDIT);
		editButton.addStyleName("borderless");
		editButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar ProductionProcessActivityDTO...\n" + vProductionProcessActivityDTO.toString());
                try{		                	
                	productionProcessRegisterFormView.editProductionProcessActivityDTO(vProductionProcessActivityDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		
		try {
			editButton.setEnabled(vProductionProcessActivityDTO.getValidity_end_date() == null
			/*&& bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
					.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "tab.rawmaterial.purchase.request.form")*/);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("\nerror",e);
		}
		
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(editButton,activity_pending_to_be_savedButton);
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
	
	private void checkActivitiesOrderInTheList(){		
		if(this.productionProcessDTO.getListProductionProcessActivityDTO()!=null
		&& this.productionProcessDTO.getListProductionProcessActivityDTO().size() > 2
		&& this.productionProcessDTO.getListProductionProcessActivityDTO().get(
		this.productionProcessDTO.getListProductionProcessActivityDTO().size() - 1).
		getRegistration_date() == null)
		{
			int index = 
					this.productionProcessDTO.getListProductionProcessActivityDTO().indexOf(
					new ProductionProcessActivityDTO(
							this.productionProcessDTO.getListProductionProcessActivityDTO().get(
									this.productionProcessDTO.getListProductionProcessActivityDTO().size() - 1).getId_previous_activity()	
							)
					);
			if(index == -1)return;
			
			ProductionProcessActivityDTO vProductionProcessActivityDTO = 
					this.productionProcessDTO.getListProductionProcessActivityDTO().get(
					this.productionProcessDTO.getListProductionProcessActivityDTO().size() - 1);
			this.productionProcessDTO.getListProductionProcessActivityDTO().remove(vProductionProcessActivityDTO);
			this.productionProcessDTO.getListProductionProcessActivityDTO().add(index + 1, vProductionProcessActivityDTO);
		}
	}
}
