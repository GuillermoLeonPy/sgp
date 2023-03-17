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
import py.com.kyron.sgp.gui.event.SgpEvent.ProductionProcessActivityRequirementRegisterFormViewEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.ProductionProcessRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessactivityregisterformview.components.ProductionProcessActivityDTOTabLayout;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessactivityrequirementregisterformview.components.MachineRequirementDTOTabLayout;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessactivityrequirementregisterformview.components.ManPowerRequirementDTOTabLayout;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessactivityrequirementregisterformview.components.RawMaterialRequirementDTOTabLayout;

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
public class ProductionProcessActivityRequirementRegisterFormView extends
		VerticalLayout implements View {

	private final Logger logger = LoggerFactory.getLogger(ProductionProcessActivityRequirementRegisterFormView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.activity.requeriment.register.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private ProductionProcessActivityDTO productionProcessActivityDTO;
	private ProductionProcessDTO productionProcessDTO;
	private ProductDTO productDTO;
	private RawMaterialRequirementDTO rawMaterialRequirementDTO;
	private MachineRequirementDTO machineRequirementDTO;
	private ManPowerRequirementDTO manPowerRequirementDTO;
	private BussinesSessionUtils bussinesSessionUtils;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private String CALLER_VIEW;
	private TabSheet tabs;
	private Tab rawMaterialRequirementDTOTabLayoutTab;
	private Tab machineRequirementDTOTabLayoutTab;
	private Tab manPowerRequirementDTOTabLayoutTab;
	private VerticalLayout mainViewLayout;
	private String selectedTabContentComponentId;
	private boolean editFormMode;
	private final String FORM_MODE_RAW_MATERIAL_REQUERIMENT = "form.mode.raw.material.requeriment";
	private final String FORM_MODE_MACHINE_REQUERIMENT = "form.mode.machine.requeriment";
	private final String FORM_MODE_MAN_POWER_REQUERIMENT = "form.mode.man.power.requeriment";
	private ProductionManagementService productionManagementService;
	private Button activity_pending_to_be_savedButton;
	
	public ProductionProcessActivityRequirementRegisterFormView() {
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
	
	public ProductionProcessActivityRequirementRegisterFormView(
			Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		logger.info("\n==========================================="
					+"\n ProductionProcessActivityRequirementRegisterFormView"
					+"\n a view change event has been registered"
					+"\n===========================================");
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n ProductionProcessActivityRequirementRegisterFormView" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()

	@Subscribe
	public void editProductionProcessActivityRequeriment(final ProductionProcessActivityRequirementRegisterFormViewEvent vProductionProcessActivityRequirementRegisterFormViewEvent){
		try{
			this.CALLER_VIEW = vProductionProcessActivityRequirementRegisterFormViewEvent.getCallerView();
			this.productDTO = vProductionProcessActivityRequirementRegisterFormViewEvent.getProductDTO();
			this.productionProcessDTO = vProductionProcessActivityRequirementRegisterFormViewEvent.getProductionProcessDTO();
			this.productionProcessActivityDTO = vProductionProcessActivityRequirementRegisterFormViewEvent.getProductionProcessActivityDTO();
			this.rawMaterialRequirementDTO = vProductionProcessActivityRequirementRegisterFormViewEvent.getRawMaterialRequirementDTO();
			this.machineRequirementDTO = vProductionProcessActivityRequirementRegisterFormViewEvent.getMachineRequirementDTO();
			this.manPowerRequirementDTO = vProductionProcessActivityRequirementRegisterFormViewEvent.getManPowerRequirementDTO();
			this.editFormMode = vProductionProcessActivityRequirementRegisterFormViewEvent.isEditFormMode();
			this.printParameterData();
			this.initMainViewLayout();
			this.initTitles();
			this.initTabSheet();
			this.setUpRawMaterialRequirementDTOTabLayoutTab();
		    this.setUpMachineRequirementDTOTabLayoutTab();//raw material
		    this.setUpManPowerRequirementDTOTabLayoutTab();//man power
		    this.mainViewLayout.addComponent(this.tabs);
		    this.removeAndReAddMainViewLayout();
		    this.refreshTabSelection();
		}catch(Exception e){
			logger.error("\nerror", e);
		}		
	}
	
	private void refreshTabSelection() throws PmsServiceException{
		logger.info("\n##############################"
					+"\n refreshing tab selection"
					+"\n##############################");
		int compomentCount = this.tabs.getComponentCount();
		for(int counter = (compomentCount - 1); counter >= 0 ; counter--){
			this.tabs.setSelectedTab(counter);
			logger.info("\n tab in position: "+ counter + " has been selected...");
		}

    	this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
    	this.rawMaterialRequirementDTOTabLayoutTab.setVisible(this.determinateFormMode().equals(this.FORM_MODE_RAW_MATERIAL_REQUERIMENT));
    	this.machineRequirementDTOTabLayoutTab.setVisible(this.determinateFormMode().equals(this.FORM_MODE_MACHINE_REQUERIMENT));
    	this.manPowerRequirementDTOTabLayoutTab.setVisible(this.determinateFormMode().equals(this.FORM_MODE_MAN_POWER_REQUERIMENT));
		this.tabs.markAsDirty();
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
	
	private void printParameterData(){
		logger.info("\n======================================"
				+"\n production process"
				+"\n ------------------"
				+"\n"+this.productionProcessDTO
				+"\n production process activity"
				+"\n ---------------------------"
				+"\n"+this.productionProcessActivityDTO
				+"\n production process activity raw material requirement"
				+"\n ----------------------------------------------------"
				+"\n"+this.rawMaterialRequirementDTO
				+"\n production process activity machine requirement"
				+"\n ----------------------------------------------------"
				+"\n"+this.machineRequirementDTO
				+"\n production process activity man power requirement"
				+"\n ----------------------------------------------------"
				+"\n"+this.manPowerRequirementDTO
				+"\n======================================");
	}
	
	private String determinateFormMode() throws PmsServiceException{
		if(this.rawMaterialRequirementDTO == null && this.machineRequirementDTO == null && this.manPowerRequirementDTO != null)
			return this.FORM_MODE_MAN_POWER_REQUERIMENT;
		else if(this.rawMaterialRequirementDTO == null && this.machineRequirementDTO != null && this.manPowerRequirementDTO == null)
			return this.FORM_MODE_MACHINE_REQUERIMENT;
		else if(this.rawMaterialRequirementDTO != null && this.machineRequirementDTO == null && this.manPowerRequirementDTO == null)
			return this.FORM_MODE_RAW_MATERIAL_REQUERIMENT;
		else
			throw new PmsServiceException("application.common.gui.exception.by.programmer", null, null);			
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
	
	private void initTitles() throws PmsServiceException{
		logger.info( "\n============================================="
					+"\n main title : "+this.determinateMainTitle()
					+"\n=============================================");
		Label title = new Label(this.determinateMainTitle());
       	title.addStyleName(ValoTheme.LABEL_H1);
       	
		Label productionProcessLabel = new Label(this.messages.get("application.common.activity.label") + ":");
		productionProcessLabel.addStyleName(ValoTheme.LABEL_H4);
		productionProcessLabel.addStyleName(ValoTheme.LABEL_COLORED);
       	Label productionProcessTitle = new Label(this.productionProcessActivityDTO.getActivity_id());
       	productionProcessTitle.addStyleName(ValoTheme.LABEL_H4);
       	productionProcessTitle.addStyleName(ValoTheme.LABEL_COLORED);
        HorizontalLayout hl = new HorizontalLayout(productionProcessLabel,productionProcessTitle);        
        hl.addStyleName("wrapping");
        hl.setSpacing(true);
        
		this.activity_pending_to_be_savedButton = new Button();
		this.activity_pending_to_be_savedButton.addStyleName("borderless");
		this.activity_pending_to_be_savedButton.setIcon(FontAwesome.SAVE);		
		this.activity_pending_to_be_savedButton.setVisible(this.productionProcessActivityDTO.getId() == null);		
		this.activity_pending_to_be_savedButton.setDescription(messages.get(VIEW_NAME + "main.title.button.activity.pending.to.be.saved.description"));
		this.activity_pending_to_be_savedButton.setResponsive(false);
		hl.addComponent(this.activity_pending_to_be_savedButton);
        
       	this.mainViewLayout.addComponent(title);
       	this.mainViewLayout.addComponent(hl);
	}
	
	private String determinateMainTitle() throws PmsServiceException{
		String mainTitle = !this.editFormMode ? this.messages.get(this.VIEW_NAME + "main.title.register") : this.messages.get(this.VIEW_NAME + "main.title.edit");
		if(this.determinateFormMode().equals(this.FORM_MODE_MACHINE_REQUERIMENT))
			mainTitle+=":" + this.messages.get("application.common.machine.label").toLowerCase();
		else if(this.determinateFormMode().equals(this.FORM_MODE_MAN_POWER_REQUERIMENT))
			mainTitle+=":" + this.messages.get("application.common.man.power.label").toLowerCase();
		else if(this.determinateFormMode().equals(this.FORM_MODE_RAW_MATERIAL_REQUERIMENT))
			mainTitle+=":" + this.messages.get("application.common.rawmaterialid.label").toLowerCase();
		
		return mainTitle;
	}
	
	private void initTabSheet(){
    	this.tabs = new TabSheet();
    	this.tabs.setId("tabs");
    	this.tabs.addStyleName("framed");
	}
	
	private void setUpRawMaterialRequirementDTOTabLayoutTab(){		
		RawMaterialRequirementDTOTabLayout vRawMaterialRequirementDTOTabLayout = 
				new RawMaterialRequirementDTOTabLayout(
						this, 
						this.productionProcessActivityDTO,
						this.rawMaterialRequirementDTO != null ? this.rawMaterialRequirementDTO : new RawMaterialRequirementDTO(), 
						this.editFormMode);
		vRawMaterialRequirementDTOTabLayout.setId("vRawMaterialRequirementDTOTabLayout");
    	this.rawMaterialRequirementDTOTabLayoutTab = this.tabs.addTab(vRawMaterialRequirementDTOTabLayout, this.messages.get(this.VIEW_NAME + "tab.raw.material.requeriment"));
    	
    	this.rawMaterialRequirementDTOTabLayoutTab.setClosable(false);
    	this.rawMaterialRequirementDTOTabLayoutTab.setEnabled(true);    	
    	this.rawMaterialRequirementDTOTabLayoutTab.setIcon(FontAwesome.DIAMOND);
	}
	
	public void saveButtonActionRawMaterialRequirementDTOTabLayout(RawMaterialRequirementDTO vRawMaterialRequirementDTO, boolean editFormMode) throws PmsServiceException{
		logger.info( "\n========================================"
				+"\n save button action raw material requeriment"
				+"\n editFormMode: "+ editFormMode
				+"\n" + (editFormMode ? "edit mode: do nothing" : "create mode, add element")
				+"\n========================================");
		//if(!editFormMode)
			this.productionManagementService.insertRawMaterialRequirementDTOValidate(this.productionProcessActivityDTO, vRawMaterialRequirementDTO);
			this.printListRawMaterialRequirementDTO();
		
		this.rawMaterialRequirementDTO = vRawMaterialRequirementDTO;
		this.navigateToCallerView();
	}
	
	private void printListRawMaterialRequirementDTO(){
		for(RawMaterialRequirementDTO vRawMaterialRequirementDTO : this.productionProcessActivityDTO.getListRawMaterialRequirementDTO()){
			logger.info( "\n raw material requeriment "
						+"\n -------------------------"
						+"\n"+vRawMaterialRequirementDTO);
		}
	}
	
	public void navigateToCallerView(){
		logger.info("\n ProductionProcessActivityRequirementRegisterFormView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new ProductionProcessActivityRegisterFormViewEvent(
					this.productDTO,
					this.productionProcessDTO,
					this.productionProcessActivityDTO, 
					DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_REQUIREMENT_REGISTER_FORM.getViewName(), 
					this.productionProcessDTO.getId()!=null));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	public void navigateToCallerView(String viewName){
		logger.info("\n ProductionProcessActivityRequirementRegisterFormView.navigateToCallerView()\nviewName : " + viewName
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(viewName);
			DashboardEventBus.post(new ProductionProcessActivityRegisterFormViewEvent(
					this.productDTO,
					this.productionProcessDTO,
					this.productionProcessActivityDTO,
					DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_REQUIREMENT_REGISTER_FORM.getViewName(), 
					this.productionProcessDTO.getId()!=null));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	public void saveButtonActionMachineRequirementDTOTabLayout(MachineRequirementDTO vMachineRequirementDTO, boolean editFormMode){
		logger.info( "\n========================================"
					+"\n save button action machine requeriment"
					+"\n editFormMode: "+ editFormMode
					+"\n" + (editFormMode ? "edit mode: do nothing" : "create mode, add element")
					+"\n========================================");
		
		if(!editFormMode){
			if(this.productionProcessActivityDTO.getListMachineRequirementDTO() == null)
				this.productionProcessActivityDTO.setListMachineRequirementDTO(new ArrayList<MachineRequirementDTO>());			
		}else{
			if(this.productionProcessActivityDTO.getListMachineRequirementDTO().contains(vMachineRequirementDTO))
				this.productionProcessActivityDTO.getListMachineRequirementDTO().remove(vMachineRequirementDTO);
		}
		this.productionProcessActivityDTO.getListMachineRequirementDTO().add(vMachineRequirementDTO);
		this.machineRequirementDTO = vMachineRequirementDTO;
		this.navigateToCallerView();
	}
	
	private void setUpMachineRequirementDTOTabLayoutTab(){		
		MachineRequirementDTOTabLayout vMachineRequirementDTOTabLayout = 
				new MachineRequirementDTOTabLayout(
						this, 
						this.productionProcessActivityDTO,
						this.machineRequirementDTO != null ? this.machineRequirementDTO : new MachineRequirementDTO(), 
						this.editFormMode);
		vMachineRequirementDTOTabLayout.setId("vMachineRequirementDTOTabLayout");
    	this.machineRequirementDTOTabLayoutTab = this.tabs.addTab(vMachineRequirementDTOTabLayout, this.messages.get(this.VIEW_NAME + "tab.machine.requeriment"));
    	
    	this.machineRequirementDTOTabLayoutTab.setClosable(false);
    	this.machineRequirementDTOTabLayoutTab.setEnabled(true);
    	this.machineRequirementDTOTabLayoutTab.setIcon(FontAwesome.GEARS);    	
	}
	
	public void saveButtonActionManPowerRequirementDTOTabLayout(ManPowerRequirementDTO vManPowerRequirementDTO, boolean editFormMode){
		logger.info( "\n========================================"
					+"\n save button action man power requeriment"
					+"\n editFormMode: "+ editFormMode
					+"\n" + (editFormMode ? "edit mode: do nothing" : "create mode, add element")
					+"\n========================================");
		if(!editFormMode){
			if(this.productionProcessActivityDTO.getListManPowerRequirementDTO()==null)
				this.productionProcessActivityDTO.setListManPowerRequirementDTO(new ArrayList<ManPowerRequirementDTO>());			
		}else{
			if(this.productionProcessActivityDTO.getListManPowerRequirementDTO().contains(vManPowerRequirementDTO))
				this.productionProcessActivityDTO.getListManPowerRequirementDTO().remove(vManPowerRequirementDTO);			
		}
		this.productionProcessActivityDTO.getListManPowerRequirementDTO().add(vManPowerRequirementDTO);
		this.manPowerRequirementDTO = vManPowerRequirementDTO;
		this.navigateToCallerView();
	}
	
	private void setUpManPowerRequirementDTOTabLayoutTab(){		
		ManPowerRequirementDTOTabLayout vManPowerRequirementDTOTabLayout = 
				new ManPowerRequirementDTOTabLayout(
						this, 
						this.productionProcessActivityDTO,
						this.manPowerRequirementDTO!=null ? this.manPowerRequirementDTO : new ManPowerRequirementDTO(), 
						this.editFormMode);
		vManPowerRequirementDTOTabLayout.setId("vManPowerRequirementDTOTabLayout");
    	this.manPowerRequirementDTOTabLayoutTab = this.tabs.addTab(vManPowerRequirementDTOTabLayout, this.messages.get(this.VIEW_NAME + "tab.man.power.requeriment"));
    	
    	this.manPowerRequirementDTOTabLayoutTab.setClosable(false);
    	this.manPowerRequirementDTOTabLayoutTab.setEnabled(true);
    	this.manPowerRequirementDTOTabLayoutTab.setIcon(FontAwesome.USER_MD);    	
	}
}
