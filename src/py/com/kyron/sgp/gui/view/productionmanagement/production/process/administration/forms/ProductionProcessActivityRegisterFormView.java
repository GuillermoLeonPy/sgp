package py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.ProductDTO;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineRequirementDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ManPowerRequirementDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialRequirementDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.ProductionProcessActivityRegisterFormViewEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.ProductionProcessRegisterFormViewEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.ProductionProcessActivityRequirementRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessactivityregisterformview.components.MachineRequirementDTOTableTabLayout;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessactivityregisterformview.components.ManPowerRequirementDTOTableTabLayout;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessactivityregisterformview.components.ProductionProcessActivityDTOTabLayout;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessactivityregisterformview.components.RawMaterialRequirementDTOTableTabLayout;

import com.google.common.eventbus.Subscribe;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ProductionProcessActivityRegisterFormView extends VerticalLayout
		implements View {

	private final Logger logger = LoggerFactory.getLogger(ProductionProcessActivityRegisterFormView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.activity.register.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private ProductDTO productDTO;
	private ProductionProcessActivityDTO productionProcessActivityDTO;
	private ProductionProcessDTO productionProcessDTO;
	private BussinesSessionUtils bussinesSessionUtils;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private String CALLER_VIEW;
	private TabSheet tabs;
	private Tab productionProcessActivityDTOTabLayoutTab;
	private Tab rawMaterialRequirementDTOTableTabLayoutTab;
	private Tab machineRequirementDTOTableTabLayoutTab;
	private Tab manPowerRequirementDTOTableTabLayoutTab;
	private VerticalLayout mainViewLayout;
	private String selectedTabContentComponentId;
	private boolean editFormMode;
	private ProductionManagementService productionManagementService;
	private Button process_pending_to_be_savedButton;
	
	public ProductionProcessActivityRegisterFormView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n ProductionProcessActivityRegisterFormView..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();			
	        this.addStyleName("v-scrollable");
	        this.setHeight("100%");
	        Responsive.makeResponsive(this);
			DashboardEventBus.register(this);
			
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	private void initServices() throws Exception{
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
	}
	
	public ProductionProcessActivityRegisterFormView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		logger.info("\n==========================================="
					+"\n ProductionProcessActivityRegisterFormView"
					+"\n a view change event has been registered"
					+"\n===========================================");
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n ProductionProcessActivityRegisterFormView" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()

	@Subscribe
	public void editProductionProcessActivity(final ProductionProcessActivityRegisterFormViewEvent vProductionProcessActivityRegisterFormViewEvent){
		try{
			this.CALLER_VIEW = vProductionProcessActivityRegisterFormViewEvent.getCallerView();
			this.productDTO = vProductionProcessActivityRegisterFormViewEvent.getProductDTO();
			this.productionProcessDTO = vProductionProcessActivityRegisterFormViewEvent.getProductionProcessDTO();
			this.productionProcessActivityDTO = vProductionProcessActivityRegisterFormViewEvent.getProductionProcessActivityDTO();
			this.editFormMode = vProductionProcessActivityRegisterFormViewEvent.isEditFormMode();
			this.printParameterData();
			this.initMainViewLayout();
			this.initTitles();
			this.initTabSheet();
			this.setUpProductionProcessActivityDTOTabLayoutTab();
		    this.setUpRawMaterialRequirementDTOTableTabLayoutTab();//raw material
		    this.setUpMachineRequirementDTOTableTabLayoutTab();//machine
		    this.setUpManPowerRequirementDTOTableTabLayoutTab();//man power
		    this.mainViewLayout.addComponent(this.tabs);
		    this.removeAndReAddMainViewLayout();
		    this.refreshTabSelection();
		}catch(Exception e){
			logger.error("\nerror", e);
		}		
	}
	
	private void refreshTabSelection(){
		logger.info("\n##############################"
					+"\n refreshing tab selection"
					+"\n##############################");
		int compomentCount = this.tabs.getComponentCount();
		for(int counter = (compomentCount - 1); counter >= 0 ; counter--){
			this.tabs.setSelectedTab(counter);
			logger.info("\n tab in position: "+ counter + " has been selected...");
		}

    	this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
		this.tabs.markAsDirty();
	}
	
	private void printParameterData(){
		logger.info("\n======================================"
				+"\n product: "
				+"\n --------"
				+"\n"+this.productDTO
				+"\n production process"
				+"\n ------------------"
				+"\n"+this.productionProcessDTO
				+"\n production process activity"
				+"\n ---------------------------"
				+"\n"+this.productionProcessActivityDTO
					+"\n======================================");
	}
	
	private void removeAndReAddMainViewLayout(){
		this.removeAllComponents();
		this.addComponent(this.mainViewLayout);
	}
		
	private void initMainViewLayout(){
		this.mainViewLayout = new VerticalLayout();
		this.mainViewLayout.setSpacing(true);
		this.mainViewLayout.setMargin(true);
	}
	
	private void initTitles(){
		Label title = new Label(!this.editFormMode ? this.messages.get(this.VIEW_NAME + "main.title.register") : this.messages.get(this.VIEW_NAME + "main.title.edit"));
       	title.addStyleName(ValoTheme.LABEL_H1);
       	
		Label productionProcessLabel = new Label(this.messages.get("application.common.process.label") + ":");
		productionProcessLabel.addStyleName(ValoTheme.LABEL_H4);
		productionProcessLabel.addStyleName(ValoTheme.LABEL_COLORED);
       	Label productionProcessTitle = new Label(this.productionProcessDTO.getProcess_id());
       	productionProcessTitle.addStyleName(ValoTheme.LABEL_H4);
       	productionProcessTitle.addStyleName(ValoTheme.LABEL_COLORED);
        HorizontalLayout hl = new HorizontalLayout(productionProcessLabel,productionProcessTitle);
        hl.addStyleName("wrapping");
        hl.setSpacing(true);
        
		this.process_pending_to_be_savedButton = new Button();
		this.process_pending_to_be_savedButton.addStyleName("borderless");
		this.process_pending_to_be_savedButton.setIcon(FontAwesome.SAVE);		
		this.process_pending_to_be_savedButton.setVisible(this.productionProcessDTO.getId() == null);		
		this.process_pending_to_be_savedButton.setDescription(messages.get(VIEW_NAME + "main.title.button.process.pending.to.be.saved.description"));
		this.process_pending_to_be_savedButton.setResponsive(false);
		hl.addComponent(this.process_pending_to_be_savedButton);
		
       	this.mainViewLayout.addComponent(title);
       	this.mainViewLayout.addComponent(hl);
       	
	}
	
	private void initTabSheet(){
    	this.tabs = new TabSheet();
    	this.tabs.setId("tabs");
    	this.tabs.addStyleName("framed");
	}
	
    private SelectedTabChangeListener setUpTabsSelectionListener(){
    	return new SelectedTabChangeListener(){
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				// TODO Auto-generated method stub				
				selectedTabContentComponentId =  event.getTabSheet().getSelectedTab().getId();
				logger.info("\ntab selection listener"
						+"\n selectedTabContentComponentId : " + selectedTabContentComponentId);			

			}    		
    	};
    }
    
    public void saveButtonActionProductionProcessActivityDTOTabLayout(ProductionProcessActivityDTO vProductionProcessActivityDTO, boolean editFormMode, boolean saveDefinitiveMode) throws PmsServiceException{
    	if(saveDefinitiveMode){
    		logger.info("\n ================== \n saving production activity to database \n ================== ");
	    	if(!editFormMode)
	    		this.insertProductionProcessActivityDTO(vProductionProcessActivityDTO);
	    	else	    		
	    		logger.info("\n======================================\n to implement: update a production process activity \n======================================");
	    	
	    	this.process_pending_to_be_savedButton.setVisible(this.productionProcessDTO.getId() == null);
	    	this.process_pending_to_be_savedButton.markAsDirty();
	    	this.navigateToCallerView(DashboardViewType.PRODUCTION_PROCESS_REGISTER_FORM.getViewName());
    	}else{
    		logger.info("\n ================== \n not saving production activity to database, just keeping in memory \n ================== ");
			if(this.productionProcessDTO.getListProductionProcessActivityDTO()==null)this.productionProcessDTO.setListProductionProcessActivityDTO(new ArrayList<ProductionProcessActivityDTO>());
			
			if(this.productionProcessDTO.getListProductionProcessActivityDTO().contains(vProductionProcessActivityDTO)){
				int index = this.productionProcessDTO.getListProductionProcessActivityDTO().indexOf(vProductionProcessActivityDTO);
				this.productionProcessDTO.getListProductionProcessActivityDTO().remove(index);
				this.productionProcessDTO.getListProductionProcessActivityDTO().add(index,vProductionProcessActivityDTO);
			}
			this.productionProcessDTO.getListProductionProcessActivityDTO().add(vProductionProcessActivityDTO);
			
			    		
    	}
    	
    }
    
	public void navigateToCallerView(){
		logger.info("\n ProductionProcessActivityRegisterFormView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new ProductionProcessRegisterFormViewEvent(
					this.productDTO, 
					this.productionProcessDTO,
					DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_REGISTER_FORM.getViewName(), 
					this.productionProcessDTO.getId()!=null));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	public void navigateToCallerView(String viewName){
		logger.info("\n ProductionProcessActivityRegisterFormView.navigateToCallerView()\nviewName : " + viewName
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(viewName);
			DashboardEventBus.post(new ProductionProcessRegisterFormViewEvent(
					this.productDTO, 
					this.productionProcessDTO,
					DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_REGISTER_FORM.getViewName(),
					this.productionProcessDTO.getId()!=null));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	private void setUpProductionProcessActivityDTOTabLayoutTab(){		
		ProductionProcessActivityDTOTabLayout vProductionProcessActivityDTOTabLayout = 
				new ProductionProcessActivityDTOTabLayout(
						this,
						this.productionProcessActivityDTO != null ? this.productionProcessActivityDTO :
						new ProductionProcessActivityDTO(null,Math.round(Math.random() * 1000000L)),
						this.productionProcessDTO,
						this.productionProcessActivityDTO != null && this.productionProcessActivityDTO.getId()!=null);
		vProductionProcessActivityDTOTabLayout.setId("vProductionProcessActivityDTOTabLayout");
    	this.productionProcessActivityDTOTabLayoutTab = this.tabs.addTab(vProductionProcessActivityDTOTabLayout, this.messages.get(this.VIEW_NAME + "tab.production.process.activity.register.form"));
    	
    	this.productionProcessActivityDTOTabLayoutTab.setClosable(false);
    	this.productionProcessActivityDTOTabLayoutTab.setEnabled(true);
    	this.productionProcessActivityDTOTabLayoutTab.setIcon(FontAwesome.BOOK);
	}
	
	public void editRawMaterialRequirementDTO(RawMaterialRequirementDTO vRawMaterialRequirementDTO){
		try{
			if(vRawMaterialRequirementDTO == null)vRawMaterialRequirementDTO = new RawMaterialRequirementDTO(null,null,Math.round(Math.random() * 10000));
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_REQUIREMENT_REGISTER_FORM.getViewName());
			DashboardEventBus.post(new ProductionProcessActivityRequirementRegisterFormViewEvent(
					this.productDTO, 
					this.productionProcessDTO,
					this.productionProcessActivityDTO,
					vRawMaterialRequirementDTO,
					null,
					null,
					DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_REGISTER_FORM.getViewName(), 
					vRawMaterialRequirementDTO.getId()!=null || 
					(vRawMaterialRequirementDTO.getRawMaterialDTO()!=null 
					&& vRawMaterialRequirementDTO.getMeasurmentUnitDTO()!=null 
					&& vRawMaterialRequirementDTO.getQuantity()!=null)));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	public RawMaterialRequirementDTO rawMaterialRequirementDTOSetEndValidityDate(RawMaterialRequirementDTO vRawMaterialRequirementDTO) throws PmsServiceException{
		return this.productionManagementService.rawMaterialRequirementDTOSetEndValidityDate(vRawMaterialRequirementDTO);
	}
	
	private void setUpRawMaterialRequirementDTOTableTabLayoutTab(){
		if(this.rawMaterialRequirementDTOTableTabLayoutTab!=null){
			this.tabs.removeTab(this.rawMaterialRequirementDTOTableTabLayoutTab);
			//this.rawMaterialExistenceDTOTableTabLayoutReFreshed = true;
		}
		RawMaterialRequirementDTOTableTabLayout vRawMaterialRequirementDTOTableTabLayout = 
				new RawMaterialRequirementDTOTableTabLayout(this, this.productionProcessActivityDTO);
		vRawMaterialRequirementDTOTableTabLayout.setId("vRawMaterialRequirementDTOTableTabLayout");
		this.rawMaterialRequirementDTOTableTabLayoutTab = 
				this.tabs.addTab(vRawMaterialRequirementDTOTableTabLayout,this.messages.get(this.VIEW_NAME + "tab.raw.material.requirement.table")); 
		this.rawMaterialRequirementDTOTableTabLayoutTab.setClosable(false);
		this.rawMaterialRequirementDTOTableTabLayoutTab.setEnabled(true);
		this.rawMaterialRequirementDTOTableTabLayoutTab.setIcon(FontAwesome.DIAMOND);
		
	}
	
	public void editMachineRequirementDTO(MachineRequirementDTO vMachineRequirementDTO){
		try{
			if(vMachineRequirementDTO == null)vMachineRequirementDTO = new MachineRequirementDTO(null,null,Math.round(Math.random() * 10000));
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_REQUIREMENT_REGISTER_FORM.getViewName());
			DashboardEventBus.post(new ProductionProcessActivityRequirementRegisterFormViewEvent(
					this.productDTO, 
					this.productionProcessDTO,
					this.productionProcessActivityDTO,
					null,
					vMachineRequirementDTO,
					null,
					DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_REGISTER_FORM.getViewName(), 
					vMachineRequirementDTO.getId()!=null
					|| (vMachineRequirementDTO.getMachineDTO()!=null && vMachineRequirementDTO.getMinutes_quantity()!=null)));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	public MachineRequirementDTO machineRequirementDTOSetEndValidityDate(MachineRequirementDTO vMachineRequirementDTO) throws PmsServiceException{
		return this.productionManagementService.machineRequirementDTOSetEndValidityDate(vMachineRequirementDTO);
	}
	private void setUpMachineRequirementDTOTableTabLayoutTab(){
		if(this.machineRequirementDTOTableTabLayoutTab!=null){
			this.tabs.removeTab(this.machineRequirementDTOTableTabLayoutTab);
			//this.rawMaterialExistenceDTOTableTabLayoutReFreshed = true;
		}
		MachineRequirementDTOTableTabLayout vMachineRequirementDTOTableTabLayout = 
				new MachineRequirementDTOTableTabLayout(this, this.productionProcessActivityDTO);
		vMachineRequirementDTOTableTabLayout.setId("vMachineRequirementDTOTableTabLayout");
		this.machineRequirementDTOTableTabLayoutTab = 
				this.tabs.addTab(vMachineRequirementDTOTableTabLayout,this.messages.get(this.VIEW_NAME + "tab.machine.requirement.table")); 
		this.machineRequirementDTOTableTabLayoutTab.setClosable(false);
		this.machineRequirementDTOTableTabLayoutTab.setEnabled(true);
		this.machineRequirementDTOTableTabLayoutTab.setIcon(FontAwesome.GEARS);
		
	}
	
	public void editManPowerRequirementDTO(ManPowerRequirementDTO vManPowerRequirementDTO){
		try{
			if(vManPowerRequirementDTO == null)vManPowerRequirementDTO = new ManPowerRequirementDTO(null,null,Math.round(Math.random() * 10000));
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_REQUIREMENT_REGISTER_FORM.getViewName());
			DashboardEventBus.post(new ProductionProcessActivityRequirementRegisterFormViewEvent(
					this.productDTO, 
					this.productionProcessDTO,
					this.productionProcessActivityDTO,
					null,
					null,
					vManPowerRequirementDTO,
					DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_REGISTER_FORM.getViewName(), 
					vManPowerRequirementDTO.getId()!=null
					|| vManPowerRequirementDTO.getMinutes_quantity()!=null));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	public ManPowerRequirementDTO manPowerRequirementDTOSetEndValidityDate(ManPowerRequirementDTO vManPowerRequirementDTO) throws PmsServiceException{
		return this.productionManagementService.manPowerRequirementDTOSetEndValidityDate(vManPowerRequirementDTO);
	}
	
	private void setUpManPowerRequirementDTOTableTabLayoutTab(){
		if(this.manPowerRequirementDTOTableTabLayoutTab!=null){
			this.tabs.removeTab(this.manPowerRequirementDTOTableTabLayoutTab);
			//this.rawMaterialExistenceDTOTableTabLayoutReFreshed = true;
		}
		ManPowerRequirementDTOTableTabLayout vManPowerRequirementDTOTableTabLayout = 
				new ManPowerRequirementDTOTableTabLayout(this, this.productionProcessActivityDTO);
		vManPowerRequirementDTOTableTabLayout.setId("vManPowerRequirementDTOTableTabLayout");
		this.manPowerRequirementDTOTableTabLayoutTab = 
				this.tabs.addTab(vManPowerRequirementDTOTableTabLayout,this.messages.get(this.VIEW_NAME + "tab.man.power.requirement.table")); 
		this.manPowerRequirementDTOTableTabLayoutTab.setClosable(false);
		this.manPowerRequirementDTOTableTabLayoutTab.setEnabled(true);
		this.manPowerRequirementDTOTableTabLayoutTab.setIcon(FontAwesome.USER_MD);
		
	}
	
	private void insertProductionProcessActivityDTO(ProductionProcessActivityDTO vProductionProcessActivityDTO) throws PmsServiceException{
		if(this.productionProcessDTO.getId()==null){
			if(this.productionProcessDTO.getListProductionProcessActivityDTO()==null)this.productionProcessDTO.setListProductionProcessActivityDTO(new ArrayList<ProductionProcessActivityDTO>());
    		if(this.productionProcessDTO.getListProductionProcessActivityDTO().contains(vProductionProcessActivityDTO))
    			this.productionProcessDTO.getListProductionProcessActivityDTO().remove(vProductionProcessActivityDTO);
    		this.productionProcessDTO.getListProductionProcessActivityDTO().add(vProductionProcessActivityDTO);
			this.productionManagementService.insertProductionProcessDTO(this.productionProcessDTO);
			this.productionProcessActivityDTO = vProductionProcessActivityDTO;
		}else{
			vProductionProcessActivityDTO.setId_production_process(this.productionProcessDTO.getId());
			this.productionProcessActivityDTO = this.productionManagementService.insertProductionProcessActivityDTO(vProductionProcessActivityDTO, Math.round(Math.random() * 1000000L));
    		if(this.productionProcessDTO.getListProductionProcessActivityDTO().contains(vProductionProcessActivityDTO))
    			this.productionProcessDTO.getListProductionProcessActivityDTO().remove(vProductionProcessActivityDTO);
			this.productionProcessDTO.getListProductionProcessActivityDTO().add(this.productionProcessActivityDTO);
		}
	}
}
