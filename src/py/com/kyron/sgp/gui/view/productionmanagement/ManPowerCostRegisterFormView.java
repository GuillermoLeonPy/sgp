package py.com.kyron.sgp.gui.view.productionmanagement;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ManPowerCostDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.TariffDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.ManPowerCostRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;

@SuppressWarnings("serial")
public class ManPowerCostRegisterFormView extends VerticalLayout implements
		View {

	private final Logger logger = LoggerFactory.getLogger(MachineRegisterFormView.class);
	private SgpForm<ManPowerCostDTO> manPowerCostDTOForm;
	private Map<String,String> messages;
	private final String VIEW_NAME = "manpowercost.register.form.";
	//private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private boolean editFormMode;
	private ManPowerCostDTO manPowerCostDTO;
	private ProductionManagementService productionManagementService;
	//private BussinesSessionUtils bussinesSessionUtils;
	private TabSheet tabs;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private VerticalLayout mainViewLayout;
	private boolean massiveInsertMode;
	private String CALLER_VIEW;
	private ComboBox tariffComboBox;
		
	public ManPowerCostRegisterFormView() {
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
	}
	public ManPowerCostRegisterFormView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		logger.info("\n==========================================="
					+"\n ManPowerCostRegisterFormView"
					+"\n a view change event has been registered"
					+"\n===========================================");
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n ManPowerCostRegisterFormView" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	@Subscribe
	public void setManPowerCostToEdit(final ManPowerCostRegisterFormViewEvent manPowerCostRegisterFormViewEvent){
		try{
			this.CALLER_VIEW = manPowerCostRegisterFormViewEvent.getCallerView();
			this.massiveInsertMode = manPowerCostRegisterFormViewEvent.isMassiveInsertMode();
			this.editFormMode = manPowerCostRegisterFormViewEvent.getManPowerCostDTO()!=null;
			this.initMainViewLayout();
			this.initTitles();
			this.setUpMainTabForm(manPowerCostRegisterFormViewEvent.getManPowerCostDTO());
			this.setUpManPowerCostDataTab();
		    this.mainViewLayout.addComponent(this.tabs);
		    this.removeAndReAddMainViewLayout();
		    if(this.editFormMode)this.printTariffComboBoxSelectedElement();
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
		//this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
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
	
	private void setUpMainTabForm(ManPowerCostDTO manPowerCostDTO) throws PmsServiceException{
		this.manPowerCostDTO = (manPowerCostDTO == null ? new ManPowerCostDTO() : manPowerCostDTO);
		if(this.editFormMode)this.printmanPowerCostDTOToEdit();
		this.manPowerCostDTOForm = new SgpForm<ManPowerCostDTO>(ManPowerCostDTO.class, new BeanItem<ManPowerCostDTO>(this.manPowerCostDTO), "light", true);
		this.manPowerCostDTOForm.getSgpFormLayout().addComponent(this.buildTariffComboBox());
		this.manPowerCostDTOForm.bindAndSetPositionFormLayoutTextField("tariff_amount", this.messages.get(this.VIEW_NAME + "text.field.manpowercost.tariff.amount.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "text.field.manpowercost.tariff.amount.required.message"), !this.editFormMode);
	}
	
	private void printmanPowerCostDTOToEdit(){
		logger.info("\n**********\nmanPowerCostDTOToEdit\n**********\nthis.manPowerCostDTO.toString() : \n" + this.manPowerCostDTO.toString());

	}
	
	private void setUpManPowerCostDataTab(){
    	this.tabs = new TabSheet();    	
    	this.tabs.addStyleName("framed");
    	VerticalLayout machineDataContent = new VerticalLayout();
    	machineDataContent.setMargin(true);
    	machineDataContent.setSpacing(true);
    	
        final Button saveButton = new Button(
        !this.editFormMode ? this.messages.get(this.VIEW_NAME + "tab.manpowercost.form.register.button.label")
        : this.messages.get(this.VIEW_NAME + "tab.manpowercost.form.set.end.validity.date.button.label")
        		);/*"Guardar"*/
        saveButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		if(!editFormMode){
		            			manPowerCostDTOForm.commit();
		            			tariffComboBox.validate();
		            			tariffComboBox.commit();
		            		}
		            		logger.info("\nmanPowerCostDTO.toString():\n" + manPowerCostDTO.toString());
		            		saveButtonAction();
		            		navigateToCallerView();
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
		            		manPowerCostDTOForm.discard();
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
		
		this.manPowerCostDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);
		machineDataContent.addComponent(this.manPowerCostDTOForm.getSgpFormLayout());    	
        Tab machineDataTab = this.tabs.addTab(machineDataContent, this.messages.get(this.VIEW_NAME + "tab.machine.data"));
        
        machineDataTab.setClosable(false);
        machineDataTab.setEnabled(true);
        //TestIcon testIcon = new TestIcon(60);
        //machineDataTab.setIcon(testIcon.get(false));
        machineDataTab.setIcon(FontAwesome.USERS);
	}

    private void saveButtonAction() throws PmsServiceException{
    	if(!this.editFormMode)
    		this.productionManagementService.insertManPowerCostDTO(this.manPowerCostDTO);
    	else{
    		logger.info("\nhere call update method, then return to caller view, because its a fast edit");
    		this.productionManagementService.updateManPowerCostDTO(this.manPowerCostDTO);
    		this.navigateToCallerView();
    	}
    	
		commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		
		/*if(!this.editFormMode && this.massiveInsertMode) 
			this.restartFormAfterSuccesfulInsertOperation();    	*/
    }
    
	private void navigateToCallerView(){
		logger.info("\nManPowerCostRegisterFormView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new ManPowerCostRegisterFormViewEvent(this.manPowerCostDTO, this.CALLER_VIEW, false /*always return false from this view*/));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	/*private void restartFormAfterSuccesfulInsertOperation() throws PmsServiceException{
		this.initMainViewLayout();
		this.editFormMode = false;
	    this.initTitles();
	    this.setUpMainTabForm(null);
	    this.setUpManPowerCostDataTab();
	    this.mainViewLayout.addComponent(this.tabs);
	    this.removeAndReAddMainViewLayout();
	}*/
	

    
	private void removeAndReAddMainViewLayout(){
		this.removeAllComponents();
		this.addComponent(this.mainViewLayout);
	}
	
	private Component buildTariffComboBox() throws PmsServiceException{
		this.tariffComboBox = new ComboBox(this.messages.get(this.VIEW_NAME + "tab.manpowercost.form.tariff.combo.label"));
		this.tariffComboBox.setInputPrompt(this.messages.get(this.VIEW_NAME + "tab.manpowercost.form.tariff.combo.input.prompt"));
        BeanItemContainer<TariffDTO> tariffDTOBeanItemContainer = new BeanItemContainer<TariffDTO>(TariffDTO.class);
        tariffDTOBeanItemContainer.addAll(this.productionManagementService.listTariffDTO(new TariffDTO(5L, null)));
        this.tariffComboBox.setContainerDataSource(tariffDTOBeanItemContainer);
        this.tariffComboBox.setItemCaptionPropertyId("tariff_id");
        
        if(this.editFormMode){
        	this.tariffComboBox.select(this.manPowerCostDTO.getTariffDTO());
        	this.tariffComboBox.setValue(this.manPowerCostDTO.getTariffDTO());
        }
        this.tariffComboBox.setNullSelectionAllowed(false);
        this.tariffComboBox.addStyleName("small");
        this.tariffComboBox.addValueChangeListener(this.setUpValueChangeListenerFortariffComboBox());
        this.tariffComboBox.setRequired(true);
        this.tariffComboBox.setRequiredError(this.messages.get(this.VIEW_NAME + "tab.manpowercost.form.tariff.combo.required.error"));
        this.tariffComboBox.setEnabled(!this.editFormMode);
        this.tariffComboBox.setWidth(25, Unit.PERCENTAGE);
        return this.tariffComboBox;
	}
	
	@SuppressWarnings("unchecked")
	private void printTariffComboBoxSelectedElement(){
		logger.info("\n================================"
					+"\ntariff combo box selected element: \n" + (TariffDTO)this.tariffComboBox.getValue()
					+"\n================================"
					+"\nthis.manPowerCostDTO.getTariffDTO() : \n" + this.manPowerCostDTO.getTariffDTO());
		BeanItemContainer<TariffDTO> c = (BeanItemContainer<TariffDTO>) this.tariffComboBox.getContainerDataSource();
		List<TariffDTO> items = c.getItemIds();
		for(TariffDTO vTariffDTO : items){
			logger.info("\n" + vTariffDTO);
			if(vTariffDTO.equals(this.manPowerCostDTO.getTariffDTO()))
				logger.info("\n########################\n found the selected element\n########################\n");
		}
	}
	
	private Property.ValueChangeListener setUpValueChangeListenerFortariffComboBox(){
    	return new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				logger.info("\nTariffComboBox combo box: value change event"
							+"\n======================================"	+"\nevent.getProperty().getType().getName()) : " 
							+ event.getProperty().getType().getName()
							+"\nevent.getProperty().getValue() : \n" + event.getProperty().getValue());
				manPowerCostDTO.setId_tariff(((TariffDTO)event.getProperty().getValue()).getId());
			}
    	};
	}

}
