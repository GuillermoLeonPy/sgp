package py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessactivityrequirementregisterformview.components;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineRequirementDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialExistenceDTO;
import py.com.kyron.sgp.bussines.stockmanagement.service.StockManagementService;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.ProductionProcessActivityRequirementRegisterFormView;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MachineRequirementDTOTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(MachineRequirementDTOTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.activity.requeriment.register.form.";
	private final ProductionProcessActivityDTO productionProcessActivityDTO;
	private final MachineRequirementDTO machineRequirementDTO;
	private SgpForm<MachineRequirementDTO> machineRequirementDTOForm;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final ProductionProcessActivityRequirementRegisterFormView productionProcessActivityRequirementRegisterFormView;
	private final boolean editFormMode;
	private ComboBox machineComboBox;
    //private StockManagementService stockManagementService;
    private BussinesSessionUtils bussinesSessionUtils;
    private ProductionManagementService productionManagementService;
    
	public MachineRequirementDTOTabLayout(ProductionProcessActivityRequirementRegisterFormView productionProcessActivityRequirementRegisterFormView, ProductionProcessActivityDTO productionProcessActivityDTO, MachineRequirementDTO machineRequirementDTO, boolean editFormMode) {
		// TODO Auto-generated constructor stub
		this.productionProcessActivityRequirementRegisterFormView = productionProcessActivityRequirementRegisterFormView;
		this.productionProcessActivityDTO = productionProcessActivityDTO;
		this.machineRequirementDTO = machineRequirementDTO;
		this.editFormMode = editFormMode;
		try{
			logger.info("\n MachineRequirementDTOTabLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();			
	        //this.addStyleName("v-scrollable");
	        //this.setHeight("100%");
			this.initServices();
	        Responsive.makeResponsive(this);
			DashboardEventBus.register(this);
			this.setSpacing(true);
			this.setMargin(true);
			this.setSizeFull();
			this.setUpLayoutContent();			
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public MachineRequirementDTOTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	private void initServices(){
		//this.stockManagementService = (StockManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.STOCK_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n MachineRequirementDTOTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void setUpmachineRequirementDTOForm() throws PmsServiceException{
		this.machineRequirementDTOForm = new SgpForm<MachineRequirementDTO>(MachineRequirementDTO.class, new BeanItem<MachineRequirementDTO>(this.machineRequirementDTO), ValoTheme.FORMLAYOUT_LIGHT/*"light"*/, true);
		this.machineRequirementDTOForm.getSgpFormLayout().addComponent(this.buildmachineComboBox());
		this.machineRequirementDTOForm.bindAndSetPositionFormLayoutTextField("minutes_quantity", this.messages.get("application.common.time.space.minutes.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "tab.machine.requeriment.text.field.minutes.quantity.required.message"), true);
		
	}
	
	private void setUpLayoutContent() throws PmsServiceException{
		//this.initTitles();
    	this.setUpmachineRequirementDTOForm();
		final Button okButton = new Button(this.messages.get("application.common.button.save.label"));/*"Cancelar"*/
		okButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		machineComboBox.validate();
		            		machineComboBox.commit();		            		
		            		machineRequirementDTOForm.commit();		            		
		            		productionProcessActivityRequirementRegisterFormView.saveButtonActionMachineRequirementDTOTabLayout(machineRequirementDTO, editFormMode);
		            		commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		final Button cancelButton = new Button(this.messages.get("application.common.button.cancel.label"));/*"Cancelar"*/
		cancelButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		machineComboBox.discard();
		            		machineRequirementDTOForm.discard();
		            		productionProcessActivityRequirementRegisterFormView.navigateToCallerView(DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_REGISTER_FORM.getViewName());
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
		okCancelHorizontalLayout.addComponent(okButton);		
		okCancelHorizontalLayout.addComponent(cancelButton);		
		
		this.machineRequirementDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);		
		this.addComponent(this.machineRequirementDTOForm.getSgpFormLayout());
		//this.addComponent(new Label("hola"));
	}
	
	private Component buildmachineComboBox() throws PmsServiceException{
		this.machineComboBox = new ComboBox(this.messages.get("application.common.machine.label"));
		this.machineComboBox.setInputPrompt(this.messages.get(this.VIEW_NAME + "tab.machine.requeriment.machine.combo.box.input.prompt"));
        BeanItemContainer<MachineDTO> machineRequirementDTOBeanItemContainer = new BeanItemContainer<MachineDTO>(MachineDTO.class);
        machineRequirementDTOBeanItemContainer.addAll(this.productionManagementService.listMachineDTO(null));
        this.machineComboBox.setContainerDataSource(machineRequirementDTOBeanItemContainer);
        this.machineComboBox.setItemCaptionPropertyId("machine_id");
        
        if(this.editFormMode){
        	this.machineComboBox.select(this.machineRequirementDTO.getMachineDTO());
        	this.machineComboBox.setValue(this.machineRequirementDTO.getMachineDTO());
        }
        this.machineComboBox.setNullSelectionAllowed(false);
        this.machineComboBox.addStyleName("small");
        this.machineComboBox.addValueChangeListener(this.setUpValueChangeListenerFormachineComboBox());
        this.machineComboBox.setRequired(true);
        this.machineComboBox.setRequiredError(this.messages.get(this.VIEW_NAME + "tab.machine.requeriment.machine.combo.box.required.message"));
        //this.machineComboBox.setEnabled(!this.rawMaterialPurchaseRequestDTOFormEditMode);
        this.machineComboBox.setWidth(100, Unit.PERCENTAGE);
        return this.machineComboBox;
	}
	
	private Property.ValueChangeListener setUpValueChangeListenerFormachineComboBox(){
    	return new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				logger.info("\n machine combo box: value change event"
							+"\n======================================"	+"\nevent.getProperty().getType().getName()) : " 
							+ event.getProperty().getType().getName()
							+"\nevent.getProperty().getValue() : \n" + event.getProperty().getValue());
				machineRequirementDTO.setId_machine(((MachineDTO)event.getProperty().getValue()).getId());
				machineRequirementDTO.setMachineDTO((MachineDTO)event.getProperty().getValue());
			}
    	};
	}
}
