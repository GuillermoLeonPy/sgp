package py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessactivityregisterformview.components;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MeasurmentUnitDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialRequirementDTO;
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
public class RawMaterialRequirementDTOTableTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(RawMaterialRequirementDTOTableTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.activity.register.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private final ProductionProcessActivityDTO productionProcessActivityDTO;
	private List<RawMaterialRequirementDTO> listRawMaterialRequirementDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final ProductionProcessActivityRegisterFormView productionProcessActivityRegisterFormView;
	private Table rawMaterialRequirementDTOTable;
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_registration_date","g_validity_end_date","g_measurment_unit_id","quantity"};
    private BussinesSessionUtils bussinesSessionUtils;
    private ProductionManagementService productionManagementService;
    
	public RawMaterialRequirementDTOTableTabLayout(ProductionProcessActivityRegisterFormView productionProcessActivityRegisterFormView,ProductionProcessActivityDTO productionProcessActivityDTO) {
		// TODO Auto-generated constructor stub
		this.productionProcessActivityRegisterFormView = productionProcessActivityRegisterFormView;
		this.productionProcessActivityDTO = productionProcessActivityDTO;
		try{
			logger.info("\n rawMaterialRequirementDTOTableTabLayout..");
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

	/*public RawMaterialRequirementDTOTableTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	private void initServices(){
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	private void setUpLayoutContent() throws PmsServiceException{
		this.buildrawMaterialRequirementDTOTable();
		addComponent(this.buildToolbar());
        addComponent(rawMaterialRequirementDTOTable);
        setExpandRatio(rawMaterialRequirementDTOTable, 1);
        //this.printTableDataSource();
	}
	
	@SuppressWarnings("unchecked")
	private void printTableDataSource(){
		BeanItemContainer<RawMaterialRequirementDTO> container = 
		(BeanItemContainer<RawMaterialRequirementDTO>)this.rawMaterialRequirementDTOTable.getContainerDataSource();
		List<RawMaterialRequirementDTO> listRawMaterialRequirementDTO =  container.getItemIds();
		logger.info("\n after table has been added to the layout, count of datasource elements = "+listRawMaterialRequirementDTO.size()
				+"\n this.listRawMaterialRequirementDTO.size() : "+this.listRawMaterialRequirementDTO.size()
				+"\n this.productionProcessActivityDTO.getListRawMaterialRequirementDTO().size() : "
				+(this.productionProcessActivityDTO.getListRawMaterialRequirementDTO()!=null ? this.productionProcessActivityDTO.getListRawMaterialRequirementDTO().size(): "vacio"));
		
		BeanItemContainer<RawMaterialRequirementDTO> RawMaterialRequirementDTOBeanItemContainer	= new BeanItemContainer<RawMaterialRequirementDTO>(RawMaterialRequirementDTO.class);
		RawMaterialRequirementDTOBeanItemContainer.addAll(this.listRawMaterialRequirementDTO);
		container.addAll(this.listRawMaterialRequirementDTO);
		logger.info("\n container.size() : "+ container.size()
				+"RawMaterialRequirementDTOBeanItemContainer.size() : " + RawMaterialRequirementDTOBeanItemContainer.size());
		//this.rawMaterialRequirementDTOTable.setContainerDataSource(RawMaterialRequirementDTOBeanItemContainer);
		this.rawMaterialRequirementDTOTable.refreshRowCache();
	}
	private void buildrawMaterialRequirementDTOTable() throws PmsServiceException{
    	this.rawMaterialRequirementDTOTable = new Table();
    	BeanItemContainer<RawMaterialRequirementDTO> RawMaterialRequirementDTOBeanItemContainer	= new BeanItemContainer<RawMaterialRequirementDTO>(RawMaterialRequirementDTO.class);
    	this.updatelistRawMaterialRequirementDTO();
    	RawMaterialRequirementDTOBeanItemContainer.addAll(this.listRawMaterialRequirementDTO);    	
    	this.rawMaterialRequirementDTOTable.setContainerDataSource(RawMaterialRequirementDTOBeanItemContainer); 
    	
    	this.rawMaterialRequirementDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final RawMaterialRequirementDTO vRawMaterialRequirementDTO = (RawMaterialRequirementDTO)itemId;
				final Button editButton = new Button();
				editButton.setDescription(messages.get("application.common.table.column.operations.edit"));
				editButton.setIcon(FontAwesome.EDIT);
				editButton.addStyleName("borderless");
				editButton.addClickListener(new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		                logger.info("\n editar RawMaterialRequirementDTO...\n" + vRawMaterialRequirementDTO.toString());
		                try{		                	
		                	productionProcessActivityRegisterFormView.editRawMaterialRequirementDTO(vRawMaterialRequirementDTO);
		                }catch(Exception e){
		                	commonExceptionErrorNotification.showErrorMessageNotification(e);
		                }			                
		            }
		        });
				
				try {
					editButton.setEnabled(vRawMaterialRequirementDTO.getValidity_end_date() == null
					/*&& bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
							.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "tab.rawmaterial.purchase.request.form")*/);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("\nerror",e);
				}
				final HorizontalLayout operationButtonPanel = new HorizontalLayout();				
				operationButtonPanel.addComponents(editButton, buildSetEndValidityDateButton(vRawMaterialRequirementDTO));				
				return operationButtonPanel;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//rawMaterialRequirementDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.rawMaterialRequirementDTOTable.addGeneratedColumn("g_raw_material_id", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final RawMaterialRequirementDTO vRawMaterialRequirementDTO = (RawMaterialRequirementDTO)itemId;
				return vRawMaterialRequirementDTO.getRawMaterialDTO().getRaw_material_id();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.rawMaterialRequirementDTOTable.addGeneratedColumn("g_measurment_unit_id", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final RawMaterialRequirementDTO vRawMaterialRequirementDTO = (RawMaterialRequirementDTO)itemId;
				return vRawMaterialRequirementDTO.getMeasurmentUnitDTO().getMeasurment_unit_id();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.rawMaterialRequirementDTOTable.addGeneratedColumn("g_registration_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final RawMaterialRequirementDTO vRawMaterialRequirementDTO = (RawMaterialRequirementDTO)itemId;
				return (vRawMaterialRequirementDTO.getRegistration_date() != null ? 
						SgpUtils.dateFormater.format(vRawMaterialRequirementDTO.getRegistration_date()) : 
							buildIsInPreliminarySaveStatusLabel());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.rawMaterialRequirementDTOTable.addGeneratedColumn("g_validity_end_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final RawMaterialRequirementDTO vRawMaterialRequirementDTO = (RawMaterialRequirementDTO)itemId;
				if(vRawMaterialRequirementDTO.getId()!=null && vRawMaterialRequirementDTO.getValidity_end_date()!=null)
					return SgpUtils.dateFormater.format(vRawMaterialRequirementDTO.getValidity_end_date());
				else if (vRawMaterialRequirementDTO.getId()!=null && vRawMaterialRequirementDTO.getValidity_end_date()==null)
					return buildIsValidLabel();
				else
					return buildIsInPreliminarySaveStatusLabel();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.rawMaterialRequirementDTOTable.setVisibleColumns(new Object[] {"operations","g_raw_material_id","g_measurment_unit_id","quantity","g_registration_date","g_validity_end_date"});
    	this.rawMaterialRequirementDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.rawMaterialRequirementDTOTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.rawMaterialRequirementDTOTable.setColumnHeader("g_raw_material_id", this.messages.get("application.common.rawmaterialid.label"));
    	this.rawMaterialRequirementDTOTable.setColumnAlignment("g_raw_material_id", Table.Align.LEFT);
    	
    	this.rawMaterialRequirementDTOTable.setColumnHeader("g_measurment_unit_id", this.messages.get("application.common.measurmentunitid.label"));
    	this.rawMaterialRequirementDTOTable.setColumnAlignment("g_measurment_unit_id", Table.Align.LEFT);

    	this.rawMaterialRequirementDTOTable.setColumnHeader("quantity", this.messages.get("application.common.table.column.any.amount.label"));
    	this.rawMaterialRequirementDTOTable.setColumnAlignment("quantity", Table.Align.RIGHT);
    	
    	this.rawMaterialRequirementDTOTable.setColumnHeader("g_registration_date", this.messages.get("application.common.table.column.registration.date.label"));
    	this.rawMaterialRequirementDTOTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
  	
    	this.rawMaterialRequirementDTOTable.setColumnHeader("g_validity_end_date", this.messages.get("application.common.table.column.validity.end.date.label"));
    	this.rawMaterialRequirementDTOTable.setColumnAlignment("g_validity_end_date", Table.Align.LEFT);
    	
    	this.rawMaterialRequirementDTOTable.setSizeFull();
    	this.rawMaterialRequirementDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.rawMaterialRequirementDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.rawMaterialRequirementDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.rawMaterialRequirementDTOTable.setColumnExpandRatio("operations", 0.012f); 
    	this.rawMaterialRequirementDTOTable.setColumnExpandRatio("g_raw_material_id", 0.019f);
    	this.rawMaterialRequirementDTOTable.setColumnExpandRatio("g_measurment_unit_id", 0.014f);
    	this.rawMaterialRequirementDTOTable.setColumnExpandRatio("quantity", 0.011f);
    	this.rawMaterialRequirementDTOTable.setColumnExpandRatio("g_registration_date", 0.013f);
    	this.rawMaterialRequirementDTOTable.setColumnExpandRatio("g_validity_end_date", 0.013f);
    	this.rawMaterialRequirementDTOTable.setSelectable(true);
    	this.rawMaterialRequirementDTOTable.setColumnCollapsingAllowed(true);
    	this.rawMaterialRequirementDTOTable.setColumnCollapsible("operations", false);
    	this.rawMaterialRequirementDTOTable.setColumnCollapsible("g_measurment_unit_id", true);
    	this.rawMaterialRequirementDTOTable.setColumnCollapsible("quantity", true);
    	this.rawMaterialRequirementDTOTable.setColumnCollapsible("g_registration_date", true);
    	this.rawMaterialRequirementDTOTable.setColumnCollapsible("g_validity_end_date", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.rawMaterialRequirementDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.rawMaterialRequirementDTOTable.setSortAscending(false);
    	this.rawMaterialRequirementDTOTable.setColumnReorderingAllowed(true);
    	this.rawMaterialRequirementDTOTable.setFooterVisible(true);
    	this.rawMaterialRequirementDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
    private void updatelistRawMaterialRequirementDTO() throws PmsServiceException{
    	
    	/*if(this.productionProcessActivityDTO.getId()!=null)
    		this.listRawMaterialRequirementDTO = this.productionManagementService.listRawMaterialRequirementDTO(new RawMaterialRequirementDTO(null,this.productionProcessActivityDTO.getId()));
    	if(this.listRawMaterialRequirementDTO == null) this.listRawMaterialRequirementDTO = new ArrayList<RawMaterialRequirementDTO>();*/
    	if(this.productionProcessActivityDTO.getListRawMaterialRequirementDTO() == null)
    		this.listRawMaterialRequirementDTO = /*this.generateTestData()*/new ArrayList<RawMaterialRequirementDTO>();
    	else
    		this.listRawMaterialRequirementDTO = this.productionProcessActivityDTO.getListRawMaterialRequirementDTO();
    	/*this.listRawMaterialRequirementDTO = new ArrayList<RawMaterialRequirementDTO>();
		if(this.productionProcessActivityDTO.getListRawMaterialRequirementDTO()!=null)
	    	for(RawMaterialRequirementDTO vRawMaterialRequirementDTO : this.productionProcessActivityDTO.getListRawMaterialRequirementDTO()){
	    		this.listRawMaterialRequirementDTO.add(vRawMaterialRequirementDTO);
			}    	
    	*/
    	this.printListRawMaterialRequirementDTO();
    }
	
    private void printListRawMaterialRequirementDTO(){
		if(this.productionProcessActivityDTO.getListRawMaterialRequirementDTO()!=null)
	    	for(RawMaterialRequirementDTO vRawMaterialRequirementDTO : this.productionProcessActivityDTO.getListRawMaterialRequirementDTO()){
				logger.info( "\n raw material requeriment "
							+"\n -------------------------"
							+"\n"+vRawMaterialRequirementDTO);
			}
		else
			logger.info( "\n raw material requeriment list is null");
	}

    
	private Component buildToolbar() {
	        HorizontalLayout header = new HorizontalLayout();
	        header.addStyleName("viewheader");
	        header.setSpacing(true);
	        Responsive.makeResponsive(header);
	        logger.info("\n*********************remember: an HorizontalLayout is set with the instrucction: Responsive.makeResponsive(header); !!! "
	        		+"\n*********************");
	        Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.raw.material.requirement.table.main.title"));
	        title.setSizeUndefined();
	        title.addStyleName(ValoTheme.LABEL_H1);
	        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	        header.addComponent(title);
	        
	        HorizontalLayout tools = new HorizontalLayout(this.createRegisterRawMaterialRequirementButton());	        	        
	        tools.setSpacing(true);
	        tools.addStyleName("toolbar");
	        header.addComponent(tools);
	        return header;
	 }//private Component buildToolbar()
	
	 private Button createRegisterRawMaterialRequirementButton(){
	        final Button registerRawMaterial = new Button(this.messages.get("application.common.button.register.requirement"));
	        registerRawMaterial.setDescription(this.messages.get(this.VIEW_NAME + "tab.raw.material.requirement.table.button.register.raw.material.requirement.description"));
	        registerRawMaterial.addClickListener(new ClickListener() {
	            @Override
	            public void buttonClick(final ClickEvent event) {
	            	/*editRawMaterial(null, true);*/

	            	productionProcessActivityRegisterFormView.editRawMaterialRequirementDTO(null);
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
	    	   this.rawMaterialRequirementDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.rawMaterialRequirementDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
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
				+"\n RawMaterialRequirementDTOTableTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private Button buildSetEndValidityDateButton(final RawMaterialRequirementDTO vRawMaterialRequirementDTO){
		final Button setEndValidityDateButton = new Button();
		setEndValidityDateButton.setDescription(messages.get("application.common.set.end.validity.date.button.label"));
		setEndValidityDateButton.setIcon(FontAwesome.CALENDAR_CHECK_O);
		setEndValidityDateButton.addStyleName("borderless");
		setEndValidityDateButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n end validity date button...\n");
                try{
                	updateTable(productionProcessActivityRegisterFormView.rawMaterialRequirementDTOSetEndValidityDate(vRawMaterialRequirementDTO));
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		setEndValidityDateButton.setEnabled(vRawMaterialRequirementDTO.getId()!=null && vRawMaterialRequirementDTO.getValidity_end_date()==null);
		return setEndValidityDateButton;
	}
	
	
	@SuppressWarnings("unchecked")
	private void updateTable(RawMaterialRequirementDTO vRawMaterialRequirementDTO){
		BeanItemContainer<RawMaterialRequirementDTO> RawMaterialRequirementDTOBeanItemContainer = 
				(BeanItemContainer<RawMaterialRequirementDTO>)this.rawMaterialRequirementDTOTable.getContainerDataSource();
		int index = RawMaterialRequirementDTOBeanItemContainer.indexOfId(vRawMaterialRequirementDTO);
		RawMaterialRequirementDTOBeanItemContainer.removeItem(vRawMaterialRequirementDTO);
		RawMaterialRequirementDTOBeanItemContainer.addItemAt(index, vRawMaterialRequirementDTO);
		this.rawMaterialRequirementDTOTable.commit();
		this.rawMaterialRequirementDTOTable.refreshRowCache();		
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
	
	private List<RawMaterialRequirementDTO> generateTestData(){
		List<RawMaterialRequirementDTO> listRawMaterialRequirementDTO = new ArrayList<RawMaterialRequirementDTO>();
		for (int a=0; a < 10; a++){
			RawMaterialDTO vRawMaterialDTO = new RawMaterialDTO();
			vRawMaterialDTO.setRaw_material_id("raw material id : " + Math.round(Math.random() * 10000));
			
			MeasurmentUnitDTO vMeasurmentUnitDTO = new MeasurmentUnitDTO();
			vMeasurmentUnitDTO.setMeasurment_unit_id("measurment unit id : " + Math.round(Math.random() * 10000));
			
			RawMaterialRequirementDTO vRawMaterialRequirementDTO = new RawMaterialRequirementDTO(null,null,Math.round(Math.random() * 10000));
			vRawMaterialRequirementDTO.setRawMaterialDTO(vRawMaterialDTO);
			vRawMaterialRequirementDTO.setMeasurmentUnitDTO(vMeasurmentUnitDTO);
			vRawMaterialRequirementDTO.setQuantity(BigDecimal.valueOf(Math.round(Math.random() * 10000)));
			listRawMaterialRequirementDTO.add(vRawMaterialRequirementDTO);			
		}
		return listRawMaterialRequirementDTO;
	}
}
