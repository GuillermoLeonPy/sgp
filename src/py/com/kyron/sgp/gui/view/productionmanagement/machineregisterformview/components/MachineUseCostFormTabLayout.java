package py.com.kyron.sgp.gui.view.productionmanagement.machineregisterformview.components;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineUseCostDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.TariffDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.productionmanagement.MachineRegisterFormView;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class MachineUseCostFormTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(MachineUseCostFormTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "machine.register.form.";
    private ProductionManagementService productionManagementService;
    private CommonExceptionErrorNotification commonExceptionErrorNotification;
    private boolean machineUseCostFormTabEditMode;
    private SgpForm<MachineUseCostDTO> machineUseCostDTOForm;
    private MachineUseCostDTO machineUseCostDTO;
    private ComboBox tariffComboBox;
    private MachineRegisterFormView machineRegisterFormView;
    
	public MachineUseCostFormTabLayout(
			MachineRegisterFormView vMachineRegisterFormView, 
			MachineUseCostDTO machineUseCostDTO,
			boolean machineUseCostFormTabEditMode) {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n MachineUseCostFormTabLayout()...");
			this.machineUseCostDTO = machineUseCostDTO;
			this.machineUseCostFormTabEditMode = machineUseCostFormTabEditMode;
			this.machineRegisterFormView = vMachineRegisterFormView;			
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.setSpacing(true);
			this.setMargin(true);
	        Responsive.makeResponsive(this);
	        
	        this.initTitlesMachineUseCostForm();
	        this.setUpMachineUseCostForm();
	        this.setUpOkCancelButtons();
	        this.addComponent(this.machineUseCostDTOForm.getSgpFormLayout());
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	public MachineUseCostFormTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	private void initServices() throws Exception{
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
	}
	
	
	private void initTitlesMachineUseCostForm(){
		Label title = new Label(!this.machineUseCostFormTabEditMode ? 
				this.messages.get(this.VIEW_NAME + "tab.machine.use.cost.register.form.main.title.register") : 
					this.messages.get(this.VIEW_NAME + "tab.machine.use.cost.register.form.main.title.edit"));
        title.addStyleName("h1");
        this.addComponent(title);
	}
	
	private void setUpMachineUseCostForm() throws PmsServiceException{
		
		if(this.machineUseCostFormTabEditMode)
			this.printMachineUseCostDTOToEdit();
		else
			this.machineUseCostDTO = new MachineUseCostDTO();
		this.machineUseCostDTOForm = new SgpForm<MachineUseCostDTO>(MachineUseCostDTO.class, new BeanItem<MachineUseCostDTO>(this.machineUseCostDTO), "light", true);
		this.machineUseCostDTOForm.getSgpFormLayout().addComponent(this.buildTariffComboBox());
		this.machineUseCostDTOForm.bindAndSetPositionFormLayoutTextField(
				"tariff_amount", 
				this.messages.get(this.VIEW_NAME + "tab.machine.use.cost.register.form.tariff.amount.text.field.label")/**/, 
				true, 100, true,
				this.messages.get(this.VIEW_NAME + "tab.machine.use.cost.register.form.tariff.amount.text.field.required.message"),
				!this.machineUseCostFormTabEditMode);
	}
	
	private void printMachineUseCostDTOToEdit(){
		logger.info("\n**********\nMachineUseCostDTOToEdit\n**********\nthis.MachineUseCostDTO.toString() : \n" + this.machineUseCostDTO.toString());

	}
	
	private Component buildTariffComboBox() throws PmsServiceException{
		this.tariffComboBox = new ComboBox(this.messages.get(this.VIEW_NAME + "tab.machine.use.cost.register.form.tariff.combo.label"));
		this.tariffComboBox.setInputPrompt(this.messages.get(this.VIEW_NAME + "tab.machine.use.cost.register.form.tariff.combo.input.prompt"));
        BeanItemContainer<TariffDTO> tariffDTOBeanItemContainer = new BeanItemContainer<TariffDTO>(TariffDTO.class);
        tariffDTOBeanItemContainer.addAll(this.productionManagementService.listTariffDTO(new TariffDTO(5L, null)));
        this.tariffComboBox.setContainerDataSource(tariffDTOBeanItemContainer);
        this.tariffComboBox.setItemCaptionPropertyId("tariff_id");
        
        if(this.machineUseCostFormTabEditMode){
        	this.tariffComboBox.select(this.machineUseCostDTO.getTariffDTO());
        	this.tariffComboBox.setValue(this.machineUseCostDTO.getTariffDTO());
        }
        this.tariffComboBox.setNullSelectionAllowed(false);
        this.tariffComboBox.addStyleName("small");
        this.tariffComboBox.addValueChangeListener(this.setUpValueChangeListenerFortariffComboBox());
        this.tariffComboBox.setRequired(true);
        this.tariffComboBox.setRequiredError(this.messages.get(this.VIEW_NAME + "tab.machine.use.cost.register.form.tariff.combo.required.message"));
        this.tariffComboBox.setEnabled(!this.machineUseCostFormTabEditMode);
        this.tariffComboBox.setWidth(35, Unit.PERCENTAGE);
        return this.tariffComboBox;
	}
	
	private Property.ValueChangeListener setUpValueChangeListenerFortariffComboBox(){
    	return new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				logger.info("\nTariffComboBox combo box: value change event"
							+"\n======================================"	+"\nevent.getProperty().getType().getName()) : " 
							+ event.getProperty().getType().getName()
							+"\nevent.getProperty().getValue() : \n" + event.getProperty().getValue());
				machineUseCostDTO.setId_tariff(((TariffDTO)event.getProperty().getValue()).getId());
			}
    	};
	}
	
	private void setUpOkCancelButtons(){
    	
        final Button saveButton = new Button(
        !this.machineUseCostFormTabEditMode ? this.messages.get("application.common.register.cost.button.label")
        : this.messages.get("application.common.set.end.validity.date.button.label")
        		);/*"Guardar"*/
        saveButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		if(!machineUseCostFormTabEditMode){
		            			machineUseCostDTOForm.commit();
		            			tariffComboBox.validate();
		            			tariffComboBox.commit();
		            		}
		            		logger.info("\nmachineUseCostDTO.toString():\n" + machineUseCostDTO.toString());		            		
		            		machineRegisterFormView.saveButtonActionMachineUseCostDTOFormTab(machineUseCostDTO, machineUseCostFormTabEditMode);
		            		logger.info("\n when click in save button; go to cost table tab"); 
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
		            		machineUseCostDTOForm.discard();
		            		tariffComboBox.discard();
		            		logger.info("\n when click in cancel button; go to cost table tab");
		            		machineRegisterFormView.updateStatusMachineUseCostDTOFormTab();
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
		this.machineUseCostDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);
	}
}
