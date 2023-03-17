package py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessactivityregisterformview.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineRequirementDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialRequirementDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
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
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MachineRequirementDTOTableTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(MachineRequirementDTOTableTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.activity.register.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private final ProductionProcessActivityDTO productionProcessActivityDTO;
	private List<MachineRequirementDTO> listMachineRequirementDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final ProductionProcessActivityRegisterFormView productionProcessActivityRegisterFormView;
	private Table machineRequirementDTOTable;
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_registration_date","g_validity_end_date"};
    private BussinesSessionUtils bussinesSessionUtils;
    private ProductionManagementService productionManagementService;
    private Button registerMachineRequirementButton;
    
	public MachineRequirementDTOTableTabLayout(ProductionProcessActivityRegisterFormView productionProcessActivityRegisterFormView, ProductionProcessActivityDTO productionProcessActivityDTO) {
		// TODO Auto-generated constructor stub
		this.productionProcessActivityRegisterFormView = productionProcessActivityRegisterFormView;
		this.productionProcessActivityDTO = productionProcessActivityDTO;
		try{
			logger.info("\n MachineRequirementDTOTableTabLayout..");
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

	/*public MachineRequirementDTOTableTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	private void initServices(){
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	private void setUpLayoutContent() throws PmsServiceException{
		this.buildmachineRequirementDTOTable();
		addComponent(this.buildToolbar());
        addComponent(machineRequirementDTOTable);
        setExpandRatio(machineRequirementDTOTable, 1);
	}
	
	private void buildmachineRequirementDTOTable() throws PmsServiceException{
    	this.machineRequirementDTOTable = new Table();
    	BeanItemContainer<MachineRequirementDTO> MachineRequirementDTOBeanItemContainer	= new BeanItemContainer<MachineRequirementDTO>(MachineRequirementDTO.class);
    	this.updatelistMachineRequirementDTO();
    	MachineRequirementDTOBeanItemContainer.addAll(this.listMachineRequirementDTO);    	
    	this.machineRequirementDTOTable.setContainerDataSource(MachineRequirementDTOBeanItemContainer); 
    	
    	this.machineRequirementDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final MachineRequirementDTO vMachineRequirementDTO = (MachineRequirementDTO)itemId;
				final Button editButton = new Button();
				editButton.setDescription(messages.get("application.common.table.column.operations.edit"));
				editButton.setIcon(FontAwesome.EDIT);
				editButton.addStyleName("borderless");
				editButton.addClickListener(new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		                logger.info("\n editar MachineRequirementDTO...\n" + vMachineRequirementDTO.toString());
		                try{		                	
		                	productionProcessActivityRegisterFormView.editMachineRequirementDTO(vMachineRequirementDTO);
		                }catch(Exception e){
		                	commonExceptionErrorNotification.showErrorMessageNotification(e);
		                }			                
		            }
		        });
				
				try {
					editButton.setEnabled(vMachineRequirementDTO.getValidity_end_date() == null
					/*&& bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
							.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "tab.rawmaterial.purchase.request.form")*/);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("\nerror",e);
				}
				final HorizontalLayout operationButtonPanel = new HorizontalLayout();
				operationButtonPanel.addComponents(editButton, buildSetEndValidityDateButton(vMachineRequirementDTO));
				return operationButtonPanel;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineRequirementDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.machineRequirementDTOTable.addGeneratedColumn("g_machine_id", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final MachineRequirementDTO vMachineRequirementDTO = (MachineRequirementDTO)itemId;
				return vMachineRequirementDTO.getMachineDTO().getMachine_id();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.machineRequirementDTOTable.addGeneratedColumn("g_registration_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final MachineRequirementDTO vMachineRequirementDTO = (MachineRequirementDTO)itemId;
				return (vMachineRequirementDTO.getRegistration_date()!=null ? SgpUtils.dateFormater.format(vMachineRequirementDTO.getRegistration_date()):buildIsInPreliminarySaveStatusLabel());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.machineRequirementDTOTable.addGeneratedColumn("g_validity_end_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final MachineRequirementDTO vMachineRequirementDTO = (MachineRequirementDTO)itemId;
				if(vMachineRequirementDTO.getId()!=null && vMachineRequirementDTO.getValidity_end_date()!=null)
					return SgpUtils.dateFormater.format(vMachineRequirementDTO.getValidity_end_date());
				else if (vMachineRequirementDTO.getId()!=null && vMachineRequirementDTO.getValidity_end_date()==null)
					return buildIsValidLabel();
				else
					return buildIsInPreliminarySaveStatusLabel();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.machineRequirementDTOTable.setVisibleColumns(new Object[] {"operations","g_machine_id","minutes_quantity","g_registration_date","g_validity_end_date"});
    	this.machineRequirementDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.machineRequirementDTOTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.machineRequirementDTOTable.setColumnHeader("g_machine_id", this.messages.get("application.common.machine.label"));
    	this.machineRequirementDTOTable.setColumnAlignment("g_machine_id", Table.Align.LEFT);

    	this.machineRequirementDTOTable.setColumnHeader("minutes_quantity", this.messages.get("application.common.time.space.minutes.label"));
    	this.machineRequirementDTOTable.setColumnAlignment("minutes_quantity", Table.Align.RIGHT);
    	
    	this.machineRequirementDTOTable.setColumnHeader("g_registration_date", this.messages.get("application.common.table.column.registration.date.label"));
    	this.machineRequirementDTOTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
  	
    	this.machineRequirementDTOTable.setColumnHeader("g_validity_end_date", this.messages.get("application.common.table.column.validity.end.date.label"));
    	this.machineRequirementDTOTable.setColumnAlignment("g_validity_end_date", Table.Align.LEFT);
    	
    	this.machineRequirementDTOTable.setSizeFull();
    	this.machineRequirementDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.machineRequirementDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.machineRequirementDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.machineRequirementDTOTable.setColumnExpandRatio("operations", 0.012f); 
    	this.machineRequirementDTOTable.setColumnExpandRatio("g_machine_id", 0.019f);
    	this.machineRequirementDTOTable.setColumnExpandRatio("minutes_quantity", 0.011f);
    	this.machineRequirementDTOTable.setColumnExpandRatio("g_registration_date", 0.013f);
    	this.machineRequirementDTOTable.setColumnExpandRatio("g_validity_end_date", 0.013f);
    	this.machineRequirementDTOTable.setSelectable(true);
    	this.machineRequirementDTOTable.setColumnCollapsingAllowed(true);
    	this.machineRequirementDTOTable.setColumnCollapsible("operations", false);
    	this.machineRequirementDTOTable.setColumnCollapsible("g_machine_id", false);
    	this.machineRequirementDTOTable.setColumnCollapsible("minutes_quantity", false);
    	this.machineRequirementDTOTable.setColumnCollapsible("g_supplier_data", true);
    	this.machineRequirementDTOTable.setColumnCollapsible("g_validity_end_date", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.machineRequirementDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.machineRequirementDTOTable.setSortAscending(false);
    	this.machineRequirementDTOTable.setColumnReorderingAllowed(true);
    	this.machineRequirementDTOTable.setFooterVisible(true);
    	this.machineRequirementDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
    private void updatelistMachineRequirementDTO() throws PmsServiceException{
    	/*if(this.productionProcessActivityDTO.getId()!=null)
    		this.listMachineRequirementDTO = this.productionManagementService.listMachineRequirementDTO(new MachineRequirementDTO(null,this.productionProcessActivityDTO.getId()));
    	if(this.listMachineRequirementDTO == null) this.listMachineRequirementDTO = new ArrayList<MachineRequirementDTO>();*/
    	if(this.productionProcessActivityDTO.getListMachineRequirementDTO()==null)
    		this.listMachineRequirementDTO = new ArrayList<MachineRequirementDTO>();
    	else
    		this.listMachineRequirementDTO = this.productionProcessActivityDTO.getListMachineRequirementDTO();
    }

    
	private Component buildToolbar() {
	        HorizontalLayout header = new HorizontalLayout();
	        header.addStyleName("viewheader");
	        header.setSpacing(true);
	        Responsive.makeResponsive(header);
	        logger.info("\n*********************remember: an HorizontalLayout is set with the instrucction: Responsive.makeResponsive(header); !!! "
	        		+"\n*********************");
	        Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.machine.requirement.table.main.title"));
	        title.setSizeUndefined();
	        title.addStyleName(ValoTheme.LABEL_H1);
	        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	        header.addComponent(title);
	        
	        HorizontalLayout tools = new HorizontalLayout(this.createRegisterMachineRequirementButton());	        	        
	        tools.setSpacing(true);
	        tools.addStyleName("toolbar");
	        header.addComponent(tools);
	        return header;
	 }//private Component buildToolbar()
	
	 private Button createRegisterMachineRequirementButton(){
	        /*final Button*/ this.registerMachineRequirementButton = new Button(this.messages.get("application.common.button.register.requirement"));
	        this.registerMachineRequirementButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.machine.requirement.table.button.register.machine.requirement.description"));
	        this.registerMachineRequirementButton.addClickListener(new ClickListener() {
	            @Override
	            public void buttonClick(final ClickEvent event) {
	            	/*editRawMaterial(null, true);*/

	            	productionProcessActivityRegisterFormView.editMachineRequirementDTO(null);
	            }
	        });
	        //this.registerMachineRequirementButton.setEnabled(true);
	        this.updateStatusRegisterMachineRequirementButton();
	        return this.registerMachineRequirementButton;
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
	    	   this.machineRequirementDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.machineRequirementDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
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
				+"\n MachineRequirementDTOTableTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	

	private void updateStatusRegisterMachineRequirementButton(){
		if(this.productionProcessActivityDTO.getListMachineRequirementDTO() == null || this.productionProcessActivityDTO.getListMachineRequirementDTO().isEmpty())
			this.registerMachineRequirementButton.setEnabled(true);
		else{
			boolean loopBreaked = false;
			for(MachineRequirementDTO vMachineRequirementDTO:this.productionProcessActivityDTO.getListMachineRequirementDTO()){
				if(vMachineRequirementDTO.getValidity_end_date()==null){
					this.registerMachineRequirementButton.setEnabled(false);
					loopBreaked=true;
					break;
				}
			}
			if(!loopBreaked)this.registerMachineRequirementButton.setEnabled(true);
		}
	}
	
	private Button buildSetEndValidityDateButton(final MachineRequirementDTO vMachineRequirementDTO){
		final Button setEndValidityDateButton = new Button();
		setEndValidityDateButton.setDescription(messages.get("application.common.set.end.validity.date.button.label"));
		setEndValidityDateButton.setIcon(FontAwesome.CALENDAR_CHECK_O);
		setEndValidityDateButton.addStyleName("borderless");
		setEndValidityDateButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n end validity date button...\n");
                try{
                	updateTable(productionProcessActivityRegisterFormView.machineRequirementDTOSetEndValidityDate(vMachineRequirementDTO));
                	updateStatusRegisterMachineRequirementButton();
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		setEndValidityDateButton.setEnabled(vMachineRequirementDTO.getId()!=null && vMachineRequirementDTO.getValidity_end_date()==null);
		return setEndValidityDateButton;
	}
	
	
	@SuppressWarnings("unchecked")
	private void updateTable(MachineRequirementDTO vMachineRequirementDTO){
		BeanItemContainer<MachineRequirementDTO> MachineRequirementDTOBeanItemContainer = 
				(BeanItemContainer<MachineRequirementDTO>)this.machineRequirementDTOTable.getContainerDataSource();
		int index = MachineRequirementDTOBeanItemContainer.indexOfId(vMachineRequirementDTO);
		MachineRequirementDTOBeanItemContainer.removeItem(vMachineRequirementDTO);
		MachineRequirementDTOBeanItemContainer.addItemAt(index, vMachineRequirementDTO);
		this.machineRequirementDTOTable.commit();
		this.machineRequirementDTOTable.refreshRowCache();		
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
