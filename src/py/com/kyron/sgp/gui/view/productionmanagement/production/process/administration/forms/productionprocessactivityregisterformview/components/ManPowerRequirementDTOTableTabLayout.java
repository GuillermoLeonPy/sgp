package py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessactivityregisterformview.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineRequirementDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ManPowerRequirementDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.ProductionProcessActivityRegisterFormView;

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
public class ManPowerRequirementDTOTableTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(ManPowerRequirementDTOTableTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.activity.register.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private final ProductionProcessActivityDTO productionProcessActivityDTO;
	private List<ManPowerRequirementDTO> listManPowerRequirementDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final ProductionProcessActivityRegisterFormView productionProcessActivityRegisterFormView;
	private Table manPowerRequirementDTOTable;
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_registration_date","g_validity_end_date"};
    private BussinesSessionUtils bussinesSessionUtils;
    private ProductionManagementService productionManagementService;
    private Button registerManPowerRequirementButton;
    
	public ManPowerRequirementDTOTableTabLayout(ProductionProcessActivityRegisterFormView productionProcessActivityRegisterFormView, ProductionProcessActivityDTO productionProcessActivityDTO) {
		// TODO Auto-generated constructor stub
		this.productionProcessActivityRegisterFormView = productionProcessActivityRegisterFormView;
		this.productionProcessActivityDTO = productionProcessActivityDTO;
		try{
			logger.info("\n ManPowerRequirementDTOTableTabLayout..");
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

	/*public ManPowerRequirementDTOTableTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	private void initServices(){
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	private void setUpLayoutContent() throws PmsServiceException{
		this.buildmanPowerRequirementDTOTable();
		addComponent(this.buildToolbar());
        addComponent(manPowerRequirementDTOTable);
        setExpandRatio(manPowerRequirementDTOTable, 1);
	}
	
	private void buildmanPowerRequirementDTOTable() throws PmsServiceException{
    	this.manPowerRequirementDTOTable = new Table();
    	BeanItemContainer<ManPowerRequirementDTO> ManPowerRequirementDTOBeanItemContainer	= new BeanItemContainer<ManPowerRequirementDTO>(ManPowerRequirementDTO.class);
    	this.updatelistManPowerRequirementDTO();
    	ManPowerRequirementDTOBeanItemContainer.addAll(this.listManPowerRequirementDTO);    	
    	this.manPowerRequirementDTOTable.setContainerDataSource(ManPowerRequirementDTOBeanItemContainer); 
    	
    	this.manPowerRequirementDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ManPowerRequirementDTO vManPowerRequirementDTO = (ManPowerRequirementDTO)itemId;
				final Button editButton = new Button();
				editButton.setDescription(messages.get("application.common.table.column.operations.edit"));
				editButton.setIcon(FontAwesome.EDIT);
				editButton.addStyleName("borderless");
				editButton.addClickListener(new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		                logger.info("\n editar ManPowerRequirementDTO...\n" + vManPowerRequirementDTO.toString());
		                try{		                	
		                	productionProcessActivityRegisterFormView.editManPowerRequirementDTO(vManPowerRequirementDTO);
		                }catch(Exception e){
		                	commonExceptionErrorNotification.showErrorMessageNotification(e);
		                }			                
		            }
		        });
				
				try {
					editButton.setEnabled(vManPowerRequirementDTO.getValidity_end_date() == null
					/*&& bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
							.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "tab.rawmaterial.purchase.request.form")*/);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("\nerror",e);
				}
				final HorizontalLayout operationButtonPanel = new HorizontalLayout();
				operationButtonPanel.addComponents(editButton, buildSetEndValidityDateButton(vManPowerRequirementDTO));
				return operationButtonPanel;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//manPowerRequirementDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.manPowerRequirementDTOTable.addGeneratedColumn("g_registration_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ManPowerRequirementDTO vManPowerRequirementDTO = (ManPowerRequirementDTO)itemId;
				return (vManPowerRequirementDTO.getRegistration_date()!=null ? SgpUtils.dateFormater.format(vManPowerRequirementDTO.getRegistration_date()): buildIsInPreliminarySaveStatusLabel());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.manPowerRequirementDTOTable.addGeneratedColumn("g_validity_end_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ManPowerRequirementDTO vManPowerRequirementDTO = (ManPowerRequirementDTO)itemId;
				if(vManPowerRequirementDTO.getId()!=null && vManPowerRequirementDTO.getValidity_end_date()!=null)
					return SgpUtils.dateFormater.format(vManPowerRequirementDTO.getValidity_end_date());
				else if(vManPowerRequirementDTO.getId()!=null && vManPowerRequirementDTO.getValidity_end_date()==null)
					return buildIsValidLabel();
				else
					return buildIsInPreliminarySaveStatusLabel();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.manPowerRequirementDTOTable.setVisibleColumns(new Object[] {"operations","minutes_quantity","g_registration_date","g_validity_end_date"});
    	this.manPowerRequirementDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.manPowerRequirementDTOTable.setColumnAlignment("operations", Table.Align.LEFT);

    	this.manPowerRequirementDTOTable.setColumnHeader("minutes_quantity", this.messages.get("application.common.time.space.minutes.label"));
    	this.manPowerRequirementDTOTable.setColumnAlignment("minutes_quantity", Table.Align.RIGHT);
    	
    	this.manPowerRequirementDTOTable.setColumnHeader("g_registration_date", this.messages.get("application.common.table.column.registration.date.label"));
    	this.manPowerRequirementDTOTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
  	
    	this.manPowerRequirementDTOTable.setColumnHeader("g_validity_end_date", this.messages.get("application.common.table.column.validity.end.date.label"));
    	this.manPowerRequirementDTOTable.setColumnAlignment("g_validity_end_date", Table.Align.LEFT);
    	
    	this.manPowerRequirementDTOTable.setSizeFull();
    	this.manPowerRequirementDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.manPowerRequirementDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.manPowerRequirementDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.manPowerRequirementDTOTable.setColumnExpandRatio("operations", 0.012f); 
    	this.manPowerRequirementDTOTable.setColumnExpandRatio("minutes_quantity", 0.011f);
    	this.manPowerRequirementDTOTable.setColumnExpandRatio("g_registration_date", 0.013f);
    	this.manPowerRequirementDTOTable.setColumnExpandRatio("g_validity_end_date", 0.013f);
    	this.manPowerRequirementDTOTable.setSelectable(true);
    	this.manPowerRequirementDTOTable.setColumnCollapsingAllowed(true);
    	this.manPowerRequirementDTOTable.setColumnCollapsible("operations", false);
    	this.manPowerRequirementDTOTable.setColumnCollapsible("minutes_quantity", false);
    	this.manPowerRequirementDTOTable.setColumnCollapsible("g_supplier_data", true);
    	this.manPowerRequirementDTOTable.setColumnCollapsible("g_validity_end_date", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.manPowerRequirementDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.manPowerRequirementDTOTable.setSortAscending(false);
    	this.manPowerRequirementDTOTable.setColumnReorderingAllowed(true);
    	this.manPowerRequirementDTOTable.setFooterVisible(true);
    	this.manPowerRequirementDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
    private void updatelistManPowerRequirementDTO() throws PmsServiceException{
    	/*if(this.productionProcessActivityDTO.getId()!=null)
    		this.listManPowerRequirementDTO = this.productionManagementService.listManPowerRequirementDTO(new ManPowerRequirementDTO(null,this.productionProcessActivityDTO.getId()));
    	if(this.listManPowerRequirementDTO == null) this.listManPowerRequirementDTO = new ArrayList<ManPowerRequirementDTO>();*/
    	if(this.productionProcessActivityDTO.getListManPowerRequirementDTO() == null)
    		this.listManPowerRequirementDTO = new ArrayList<ManPowerRequirementDTO>();
    	else
    		this.listManPowerRequirementDTO = this.productionProcessActivityDTO.getListManPowerRequirementDTO();
    	
    }

    
	private Component buildToolbar() {
	        HorizontalLayout header = new HorizontalLayout();
	        header.addStyleName("viewheader");
	        header.setSpacing(true);
	        Responsive.makeResponsive(header);
	        logger.info("\n*********************remember: an HorizontalLayout is set with the instrucction: Responsive.makeResponsive(header); !!! "
	        		+"\n*********************");
	        Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.man.power.requirement.table.main.title"));
	        title.setSizeUndefined();
	        title.addStyleName(ValoTheme.LABEL_H1);
	        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	        header.addComponent(title);
	        
	        HorizontalLayout tools = new HorizontalLayout(this.createRegisterManPowerRequirementButton());	        	        
	        tools.setSpacing(true);
	        tools.addStyleName("toolbar");
	        header.addComponent(tools);
	        return header;
	 }//private Component buildToolbar()
	
	 private Button createRegisterManPowerRequirementButton(){
	        /*final Button*/ this.registerManPowerRequirementButton = new Button(this.messages.get("application.common.button.register.requirement"));
	        this.registerManPowerRequirementButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.man.power.requirement.table.button.register.man.power.requirement.description"));
	        this.registerManPowerRequirementButton.addClickListener(new ClickListener() {
	            @Override
	            public void buttonClick(final ClickEvent event) {
	            	/*editRawMaterial(null, true);*/

	            	productionProcessActivityRegisterFormView.editManPowerRequirementDTO(null);
	            }
	        });
	        //this.registerManPowerRequirementButton.setEnabled(true);
	        this.updateStatusRegisterManPowerRequirementButton();
	        return this.registerManPowerRequirementButton;
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
	    	   this.manPowerRequirementDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.manPowerRequirementDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
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
				+"\n ManPowerRequirementDTOTableTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void updateStatusRegisterManPowerRequirementButton(){
		if(this.productionProcessActivityDTO.getListManPowerRequirementDTO() == null || this.productionProcessActivityDTO.getListManPowerRequirementDTO().isEmpty())
			this.registerManPowerRequirementButton.setEnabled(true);
		else{
			boolean loopBreaked = false;
			for(ManPowerRequirementDTO vManPowerRequirementDTO:this.productionProcessActivityDTO.getListManPowerRequirementDTO()){
				if(vManPowerRequirementDTO.getValidity_end_date()==null){
					this.registerManPowerRequirementButton.setEnabled(false);
					loopBreaked=true;
					break;
				}
			}
			if(!loopBreaked)this.manPowerRequirementDTOTable.setEnabled(true);
		}
	}
	
	private Button buildSetEndValidityDateButton(final ManPowerRequirementDTO vManPowerRequirementDTO){
		final Button setEndValidityDateButton = new Button();
		setEndValidityDateButton.setDescription(messages.get("application.common.set.end.validity.date.button.label"));
		setEndValidityDateButton.setIcon(FontAwesome.CALENDAR_CHECK_O);
		setEndValidityDateButton.addStyleName("borderless");
		setEndValidityDateButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n end validity date button...\n");
                try{
                	updateTable(productionProcessActivityRegisterFormView.manPowerRequirementDTOSetEndValidityDate(vManPowerRequirementDTO));
                	updateStatusRegisterManPowerRequirementButton();
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		setEndValidityDateButton.setEnabled(vManPowerRequirementDTO.getId()!=null && vManPowerRequirementDTO.getValidity_end_date()==null);
		return setEndValidityDateButton;
	}
	
	
	@SuppressWarnings("unchecked")
	private void updateTable(ManPowerRequirementDTO vManPowerRequirementDTO){
		BeanItemContainer<ManPowerRequirementDTO> ManPowerRequirementDTOBeanItemContainer = 
				(BeanItemContainer<ManPowerRequirementDTO>)this.manPowerRequirementDTOTable.getContainerDataSource();
		int index = ManPowerRequirementDTOBeanItemContainer.indexOfId(vManPowerRequirementDTO);
		ManPowerRequirementDTOBeanItemContainer.removeItem(vManPowerRequirementDTO);
		ManPowerRequirementDTOBeanItemContainer.addItemAt(index, vManPowerRequirementDTO);
		this.manPowerRequirementDTOTable.commit();
		this.manPowerRequirementDTOTable.refreshRowCache();		
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
