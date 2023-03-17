package py.com.kyron.sgp.gui.view.productionmanagement;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineUseCostDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.MachineRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.productionmanagement.machineregisterformview.components.MachineUseCostFormTabLayout;
import py.com.kyron.sgp.gui.view.productionmanagement.machineregisterformview.components.MachineUseCostTableTabLayout;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;

import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;

@SuppressWarnings("serial")
public class MachineRegisterFormView extends VerticalLayout implements View {

	private final Logger logger = LoggerFactory.getLogger(MachineRegisterFormView.class);
	private SgpForm<MachineDTO> machineDTOForm;
	private Map<String,String> messages;
	private final String VIEW_NAME = "machine.register.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private boolean editFormMode;
	private MachineDTO machineDTO;
	private ProductionManagementService productionManagementService;
	private BussinesSessionUtils bussinesSessionUtils;
	private TabSheet tabs;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private VerticalLayout mainViewLayout;
	private boolean massiveInsertMode;
	private String CALLER_VIEW;
	private Tab machineUseCostDTOFormContainerTab;
	private Tab machineUseCostTableTab;
	private boolean enableMachineUseCostDTOFormContainerTab;
	private String selectedTabContentComponentId;
	private boolean updatedFlagMachineUseCostTableTab;
	
	public MachineRegisterFormView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\nMachineRegisterFormView..");
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
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}

	public MachineRegisterFormView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

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
	
	@Subscribe
	public void setMachineToEdit(final MachineRegisterFormViewEvent machineRegisterFormViewEvent){
		try{
			this.CALLER_VIEW = machineRegisterFormViewEvent.getCallerView();
			this.massiveInsertMode = machineRegisterFormViewEvent.isMassiveInsertMode();
			this.editFormMode = machineRegisterFormViewEvent.getMachineDTO()!=null;
			this.initMainViewLayout();
			this.initTitles();
			this.setUpMainTabForm(machineRegisterFormViewEvent.getMachineDTO());
			this.setUpMachineDataTab();
		    this.setUpMachineUseCostTableTab();
		    this.editMachineUseCost(null);
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
		//this.tabs.setSelectedTab(1);
		//this.tabs.markAsDirty();
		int compomentCount = this.tabs.getComponentCount();
		for(int counter = (compomentCount - 1); counter >= 0 ; counter--){
			this.tabs.setSelectedTab(counter);
			logger.info("\n tab in position: "+ counter + " has been selected...");
		}
		this.tabs.markAsDirty();
		this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
	}
	private void initMainViewLayout(){
		this.mainViewLayout = new VerticalLayout();
		this.mainViewLayout.setSpacing(true);
		this.mainViewLayout.setMargin(true);
	}
	
	private void initTitles(){
		try{
			Label title = new Label(!this.editFormMode ? this.messages.get(this.VIEW_NAME + "main.title.register") : this.messages.get(this.VIEW_NAME + "main.title.edit"));
        	title.addStyleName("h1");
        	this.mainViewLayout.addComponent(title);
		}catch(Exception e){
			commonExceptionErrorNotification.showErrorMessageNotification(e);
		}
	}
	
	private void setUpMainTabForm(MachineDTO machineDTO){
		this.machineDTO = (machineDTO == null ? new MachineDTO() : machineDTO);
		if(this.editFormMode)this.printMachineDTOToEdit();
		this.machineDTOForm = new SgpForm<MachineDTO>(MachineDTO.class, new BeanItem<MachineDTO>(this.machineDTO), "light", true);
		this.machineDTOForm.bindAndSetPositionFormLayoutTextField("machine_id", this.messages.get(this.VIEW_NAME + "text.field.machine.id.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "text.field.machine.id.required.message"), true);
		this.machineDTOForm.bindAndSetPositionFormLayoutTextField("machine_description", this.messages.get(this.VIEW_NAME + "text.field.machine.description.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "text.field.machine.description.required.message"), true);
	}
	
	private void printMachineDTOToEdit(){
		logger.info("\n**********\nMachineDTOToEdit\n**********\nthis.machineDTO.toString() : \n" + this.machineDTO.toString());

	}
	
	private void setUpMachineDataTab(){
    	this.tabs = new TabSheet();
    	//this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
    	this.tabs.addStyleName("framed");
    	VerticalLayout machineDataContent = new VerticalLayout();
    	machineDataContent.setMargin(true);
    	machineDataContent.setSpacing(true);
    	
        final Button saveButton = new Button(this.messages.get("application.common.button.save.label"));/*"Guardar"*/
        saveButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{            		
		            		machineDTOForm.commit();
		            		logger.info("\nmachineDTO.toString():\n" + machineDTO.toString());
		            		saveButtonAction();
		            	}catch(Exception e){		
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);
		            	}
		            }
		        }
			);
        
        saveButton.setEnabled(true);
        
		final Button cancelButton = new Button(this.messages.get("application.common.button.cancel.label"));/*"Cancelar"*/
		cancelButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		machineDTOForm.discard();
		            		navigateToCallerView();
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		HorizontalLayout okCancelHorizontalLayout = new HorizontalLayout();
		okCancelHorizontalLayout.setMargin(new MarginInfo(true, false, true, false));
		okCancelHorizontalLayout.setSpacing(true);
		okCancelHorizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		okCancelHorizontalLayout.addComponent(saveButton);
		okCancelHorizontalLayout.addComponent(cancelButton);
		
		this.machineDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);
		machineDataContent.addComponent(this.machineDTOForm.getSgpFormLayout());    	
        Tab machineDataTab = this.tabs.addTab(machineDataContent, this.messages.get(this.VIEW_NAME + "tab.machine.data"));
        
        machineDataTab.setClosable(false);
        machineDataTab.setEnabled(true);
        //TestIcon testIcon = new TestIcon(60);
        //machineDataTab.setIcon(testIcon.get(false));
        machineDataTab.setIcon(FontAwesome.BOOK);
	}

    private void saveButtonAction() throws PmsServiceException,Exception{
    	if(!this.editFormMode)
    		this.productionManagementService.insertMachineDTO(this.machineDTO);
    	else{
    		logger.info("\nhere call update method, then return to caller view, because its a fast edit");
    		this.navigateToCallerView();
    	}
    	
		commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		
		if(!this.editFormMode && this.massiveInsertMode) 
			this.restartFormAfterSuccesfulInsertOperation();    	
    }
    
	private void navigateToCallerView(){
		logger.info("\nMachineRegisterFormView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW);
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new MachineRegisterFormViewEvent(this.machineDTO, this.CALLER_VIEW, false /*always return false from this view*/));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	private void restartFormAfterSuccesfulInsertOperation() throws PmsServiceException,Exception{
		this.initMainViewLayout();
		this.editFormMode = false;
	    this.initTitles();
	    this.setUpMainTabForm(null);
	    this.setUpMachineDataTab();
	    this.setUpMachineUseCostTableTab();
	    this.editMachineUseCost(null);
	    this.mainViewLayout.addComponent(this.tabs);
	    this.removeAndReAddMainViewLayout();
	}
	
    private void setUpMachineUseCostTableTab() {
    	try{
    		MachineUseCostTableTabLayout vMachineUseCostTableTabLayout = new MachineUseCostTableTabLayout(this, this.machineDTO);
    		vMachineUseCostTableTabLayout.setId("vMachineUseCostTableTabLayout");
	        
    		if(this.machineUseCostTableTab!=null)this.tabs.removeTab(this.machineUseCostTableTab);
    		
	        this.machineUseCostTableTab = 
	        		this.tabs.addTab(vMachineUseCostTableTabLayout, 
	        				this.messages.get(this.VIEW_NAME + "tab.machine.use.cost.table"), 
	        				FontAwesome.REGISTERED, 
	        				1);
	        this.machineUseCostTableTab.setClosable(false);
	        this.machineUseCostTableTab.setEnabled(true);
	        if(this.updatedFlagMachineUseCostTableTab){
	        	this.tabs.setSelectedTab(1);
	        	this.updatedFlagMachineUseCostTableTab = false;
	        }
		}catch(Exception e){
			logger.error("\nerror", e);
		}
    }//private void setUpCredentialsDataTab(){
    

    
	private void removeAndReAddMainViewLayout(){
		this.removeAllComponents();
		this.addComponent(this.mainViewLayout);
	}
	
	
    public void editMachineUseCost(MachineUseCostDTO vMachineUseCostDTO) throws PmsServiceException,Exception{
    	MachineUseCostFormTabLayout vMachineUseCostFormTabLayout = 
    			new MachineUseCostFormTabLayout
    			(this, vMachineUseCostDTO, vMachineUseCostDTO!=null && vMachineUseCostDTO.getId()!=null);
    	vMachineUseCostFormTabLayout.setId("vMachineUseCostFormTabLayout");
    	if(this.machineUseCostDTOFormContainerTab!=null)this.tabs.removeTab(this.machineUseCostDTOFormContainerTab);
    	this.machineUseCostDTOFormContainerTab = this.tabs.addTab(vMachineUseCostFormTabLayout, this.messages.get(this.VIEW_NAME + "tab.machine.use.cost.register.form"));
    	this.machineUseCostDTOFormContainerTab.setIcon(FontAwesome.MONEY);
        this.machineUseCostDTOFormContainerTab.setClosable(false);
        
        if(vMachineUseCostDTO!=null && vMachineUseCostDTO.getId()!=null)
        	this.enableMachineUseCostDTOFormContainerTab = true;
        else
        	this.setEnableMachineUseCostDTOFormFlag();
        this.machineUseCostDTOFormContainerTab.setEnabled(this.editFormMode	&& this.enableMachineUseCostDTOFormContainerTab);
        
        this.machineUseCostDTOFormContainerTab.setVisible(
        		this.bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
        		.contains(this.SECURED_PROGRAM_PREFIX + this.VIEW_NAME + "tab.machine.use.cost.register.form"));
        
        if(vMachineUseCostDTO!=null && vMachineUseCostDTO.getId()!=null)
        	this.tabs.setSelectedTab(2);
    }
    
    public void saveButtonActionMachineUseCostDTOFormTab(MachineUseCostDTO machineUseCostDTO,boolean machineUseCostFormTabEditMode) throws PmsServiceException,Exception{

	    	if(!machineUseCostFormTabEditMode){
	    		machineUseCostDTO.setId_machine(this.machineDTO.getId());
	    		this.productionManagementService.insertMachineUseCostDTO(machineUseCostDTO);
	    	}else    		
	    		this.productionManagementService.updateMachineUseCostDTO(machineUseCostDTO);
	    	
	    	this.editMachineUseCost(null);
	    	this.updateStatusMachineUseCostDTOFormTab();
			this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();

    }
    
    public void updateStatusMachineUseCostDTOFormTab() throws PmsServiceException{
    	this.setEnableMachineUseCostDTOFormFlag();
    	this.tabs.setSelectedTab(1);
    	this.machineUseCostDTOFormContainerTab.setEnabled(this.editFormMode && this.enableMachineUseCostDTOFormContainerTab);
    	this.tabs.markAsDirty();    	
    }
    
	 private void setEnableMachineUseCostDTOFormFlag() throws PmsServiceException{
		 Long id = this.productionManagementService.getMachineUseCostValidRowId(this.machineDTO.getId());
		 if(id != null)this.enableMachineUseCostDTOFormContainerTab = false; else this.enableMachineUseCostDTOFormContainerTab = true;
	 }
	 
	 private SelectedTabChangeListener setUpTabsSelectionListener(){
	  	return new SelectedTabChangeListener(){

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				// TODO Auto-generated method stub				
				selectedTabContentComponentId =  event.getTabSheet().getSelectedTab().getId();
				logger.info("\ntab selection listener"
						+"\n selectedTabContentComponentId : " + selectedTabContentComponentId);
					
				if(selectedTabContentComponentId!=null && selectedTabContentComponentId.equals("vMachineUseCostTableTabLayout")
					&& !updatedFlagMachineUseCostTableTab){
					logger.info("\nupdating asociated tariff data source...");
					try {
						updatedFlagMachineUseCostTableTab = true;
						setUpMachineUseCostTableTab();						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("\nerror", e);
						commonExceptionErrorNotification.showErrorMessageNotification(e);
					}
				}
			}	    		
	    };
	 }
}
