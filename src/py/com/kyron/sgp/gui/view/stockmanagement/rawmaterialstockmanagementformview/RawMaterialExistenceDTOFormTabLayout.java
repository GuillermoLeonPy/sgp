package py.com.kyron.sgp.gui.view.stockmanagement.rawmaterialstockmanagementformview;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MeasurmentUnitDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.TariffDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialExistenceDTO;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.productionmanagement.machineregisterformview.components.MachineUseCostFormTabLayout;
import py.com.kyron.sgp.gui.view.stockmanagement.RawMaterialStockManagementFormView;

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
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class RawMaterialExistenceDTOFormTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(RawMaterialExistenceDTOFormTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "rawmaterial.stock.management.form.";
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final boolean rawMaterialExistenceDTOFormEditMode;
	private SgpForm<RawMaterialExistenceDTO> rawMaterialExistenceDTOForm;
	private final RawMaterialExistenceDTO rawMaterialExistenceDTO;
	private final RawMaterialStockManagementFormView rawMaterialStockManagementFormView;
	private ComboBox measurmentUnitComboBox;
	private ProductionManagementService productionManagementService;
	
	public RawMaterialExistenceDTOFormTabLayout(
			RawMaterialStockManagementFormView rawMaterialStockManagementFormView,
			RawMaterialExistenceDTO rawMaterialExistenceDTO,
			boolean rawMaterialExistenceDTOFormEditMode) {
		this.rawMaterialStockManagementFormView = rawMaterialStockManagementFormView;
		this.rawMaterialExistenceDTO = rawMaterialExistenceDTO;
		this.rawMaterialExistenceDTOFormEditMode = rawMaterialExistenceDTOFormEditMode;
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n RawMaterialExistenceDTOFormTabLayout()...");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.setSpacing(true);
			this.setMargin(true);
			Responsive.makeResponsive(this);
			this.initServices();
	        this.initTitles();
	        this.setUpRawMaterialExistenceDTOForm();
	        this.setUpOkCancelButtons();
	        this.addComponent(this.rawMaterialExistenceDTOForm.getSgpFormLayout());
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public RawMaterialExistenceDTOFormTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	private void initServices() throws Exception{
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
	}
	
	private void initTitles(){
		Label title = new Label(!this.rawMaterialExistenceDTOFormEditMode ? 
				this.messages.get(this.VIEW_NAME + "tab.rawmaterialexistence.form.main.title.register") : 
					this.messages.get(this.VIEW_NAME + "tab.rawmaterialexistence.form.main.title.edit"));
        title.addStyleName("h1");
        this.addComponent(title);
	}
	
	private void setUpRawMaterialExistenceDTOForm() throws PmsServiceException{
		
		if(this.rawMaterialExistenceDTOFormEditMode)
			this.printRawMaterialExistenceDTOToEdit();
		
		this.rawMaterialExistenceDTOForm = new SgpForm<RawMaterialExistenceDTO>(RawMaterialExistenceDTO.class, new BeanItem<RawMaterialExistenceDTO>(this.rawMaterialExistenceDTO), "light", true);
		this.rawMaterialExistenceDTOForm.getSgpFormLayout().addComponent(this.buildmeasurmentUnitComboBox());
		this.rawMaterialExistenceDTOForm.bindAndSetPositionFormLayoutTextField(
				"limit_calculated_quantity", 
				this.messages.get(this.VIEW_NAME + "tab.rawmaterialexistence.form.text.field.limit.calculated.quantity.label")/**/, 
				true, 100, true,
				this.messages.get(this.VIEW_NAME + "tab.rawmaterialexistence.form.text.field.limit.calculated.quantity.required.message"),
				true);
		
		this.rawMaterialExistenceDTOForm.bindAndSetPositionFormLayoutTextField(
				"automatic_purchase_request_quantity", 
				this.messages.get(this.VIEW_NAME + "tab.rawmaterialexistence.form.text.field.automatic.purchase.request.quantity.label")/**/, 
				true, 100, true,
				this.messages.get(this.VIEW_NAME + "tab.rawmaterialexistence.form.text.field.automatic.purchase.request.quantity.required.message"),
				true);
	}
	
	private void printRawMaterialExistenceDTOToEdit(){
		logger.info("\n**********\nRawMaterialExistenceDTOToEdit\n**********\nthis.RawMaterialExistenceDTO.toString() : \n" + this.rawMaterialExistenceDTO.toString());

	}
	
	private Component buildmeasurmentUnitComboBox() throws PmsServiceException{
		this.measurmentUnitComboBox = new ComboBox(this.messages.get(this.VIEW_NAME + "tab.rawmaterialexistence.form.measurment.unit.combo.box.label"));
		this.measurmentUnitComboBox.setInputPrompt(this.messages.get(this.VIEW_NAME + "tab.rawmaterialexistence.form.measurment.unit.combo.box.input.prompt"));
        BeanItemContainer<MeasurmentUnitDTO> measurmentUnitDTOBeanItemContainer = new BeanItemContainer<MeasurmentUnitDTO>(MeasurmentUnitDTO.class);
        measurmentUnitDTOBeanItemContainer.addAll(this.productionManagementService.listMeasurmentUnitDTO(null));
        this.measurmentUnitComboBox.setContainerDataSource(measurmentUnitDTOBeanItemContainer);
        this.measurmentUnitComboBox.setItemCaptionPropertyId("measurment_unit_id");
        
        if(this.rawMaterialExistenceDTOFormEditMode){
        	this.measurmentUnitComboBox.select(this.rawMaterialExistenceDTO.getMeasurmentUnitDTO());
        	this.measurmentUnitComboBox.setValue(this.rawMaterialExistenceDTO.getMeasurmentUnitDTO());
        }
        this.measurmentUnitComboBox.setNullSelectionAllowed(false);
        this.measurmentUnitComboBox.addStyleName("small");
        this.measurmentUnitComboBox.addValueChangeListener(this.setUpValueChangeListenerForMeasurmentUnitComboBox());
        this.measurmentUnitComboBox.setRequired(true);
        this.measurmentUnitComboBox.setRequiredError(this.messages.get(this.VIEW_NAME + "tab.rawmaterialexistence.form.measurment.unit.combo.box.required.message"));
        this.measurmentUnitComboBox.setEnabled(!this.rawMaterialExistenceDTOFormEditMode);
        this.measurmentUnitComboBox.setWidth(35, Unit.PERCENTAGE);
        return this.measurmentUnitComboBox;
	}
	
	private Property.ValueChangeListener setUpValueChangeListenerForMeasurmentUnitComboBox(){
    	return new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				logger.info("\nTariffComboBox combo box: value change event"
							+"\n======================================"	+"\nevent.getProperty().getType().getName()) : " 
							+ event.getProperty().getType().getName()
							+"\nevent.getProperty().getValue() : \n" + event.getProperty().getValue());
				rawMaterialExistenceDTO.setId_measurment_unit(((MeasurmentUnitDTO)event.getProperty().getValue()).getId());
			}
    	};
	}
	
	private void setUpOkCancelButtons(){
    	
        final Button saveButton = new Button(
        this.messages.get("application.common.button.save.label"));/*"Guardar"*/
        saveButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		rawMaterialExistenceDTOForm.commit();
		            		measurmentUnitComboBox.validate();
		            		measurmentUnitComboBox.commit();
		            		logger.info("\nrawMaterialExistenceDTO.toString():\n" + rawMaterialExistenceDTO.toString());		            		
		            		rawMaterialStockManagementFormView.saveButtonRawMaterialExistenceDTOFormTabLayout(rawMaterialExistenceDTO, rawMaterialExistenceDTOFormEditMode);
		            		logger.info("\n when click in save button; go to raw material existence table tab"); 
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
		            		rawMaterialExistenceDTOForm.discard();
		            		measurmentUnitComboBox.discard();
		            		logger.info("\n when click in cancel button; go to existence table tab");
		            		rawMaterialStockManagementFormView.cancelButtonRawMaterialExistenceDTOFormTabLayout(rawMaterialExistenceDTOFormEditMode);
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
		this.rawMaterialExistenceDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);
	}
}
