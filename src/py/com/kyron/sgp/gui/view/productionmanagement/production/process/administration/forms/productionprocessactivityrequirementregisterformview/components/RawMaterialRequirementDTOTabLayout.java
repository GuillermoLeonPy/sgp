package py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessactivityrequirementregisterformview.components;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MeasurmentUnitDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialRequirementDTO;
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
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.ProductionProcessActivityRegisterFormView;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.ProductionProcessActivityRequirementRegisterFormView;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessactivityregisterformview.components.ProductionProcessActivityDTOTabLayout;

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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class RawMaterialRequirementDTOTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(RawMaterialRequirementDTOTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.activity.requeriment.register.form.";
	private final ProductionProcessActivityDTO productionProcessActivityDTO;
	private final RawMaterialRequirementDTO rawMaterialRequirementDTO;
	private SgpForm<RawMaterialRequirementDTO> rawMaterialRequirementDTOForm;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final ProductionProcessActivityRequirementRegisterFormView productionProcessActivityRequirementRegisterFormView;
	private final boolean editFormMode;
	private ComboBox measurmentUnitComboBox;
	private ComboBox rawMaterialComboBox;
	private List<RawMaterialDTO> listRawMaterialDTO;
	private List<MeasurmentUnitDTO> listMeasurmentUnitDTO;
	private List<RawMaterialExistenceDTO> listRawMaterialExistenceDTO;
    private StockManagementService stockManagementService;
    private BussinesSessionUtils bussinesSessionUtils;
    private ProductionManagementService productionManagementService;
	
	public RawMaterialRequirementDTOTabLayout(ProductionProcessActivityRequirementRegisterFormView productionProcessActivityRequirementRegisterFormView, ProductionProcessActivityDTO productionProcessActivityDTO, RawMaterialRequirementDTO rawMaterialRequirementDTO, boolean editFormMode) {
		// TODO Auto-generated constructor stub
		this.productionProcessActivityRequirementRegisterFormView = productionProcessActivityRequirementRegisterFormView;
		this.productionProcessActivityDTO = productionProcessActivityDTO;
		this.rawMaterialRequirementDTO = rawMaterialRequirementDTO;
		this.editFormMode = editFormMode;
		try{
			logger.info("\n RawMaterialRequirementDTOTabLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();			
	        //this.addStyleName("v-scrollable");
	        //this.setHeight("100%");
			this.initServices();
			this.setUpRawMaterialAndMeasurmentUnitLists();
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

	/*public RawMaterialRequirementDTOTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	
	private void initServices(){
		this.stockManagementService = (StockManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.STOCK_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n RawMaterialRequirementDTOTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void setUprawMaterialRequirementDTOForm() throws PmsServiceException{
		this.rawMaterialRequirementDTOForm = new SgpForm<RawMaterialRequirementDTO>(RawMaterialRequirementDTO.class, new BeanItem<RawMaterialRequirementDTO>(this.rawMaterialRequirementDTO), ValoTheme.FORMLAYOUT_LIGHT/*"light"*/, true);
		this.rawMaterialRequirementDTOForm.getSgpFormLayout().addComponent(this.buildrawMaterialComboBox());
		this.rawMaterialRequirementDTOForm.getSgpFormLayout().addComponent(this.buildmeasurmentUnitComboBox());
		this.rawMaterialRequirementDTOForm.bindAndSetPositionFormLayoutTextField("quantity", this.messages.get("application.common.quantity.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "tab.raw.material.requeriment.text.field.quantity.required.message"), true);
		
	}
	
	private void setUpLayoutContent() throws PmsServiceException{
		//this.initTitles();
    	this.setUprawMaterialRequirementDTOForm();
		final Button okButton = new Button(this.messages.get("application.common.button.save.label"));/*"Cancelar"*/
		okButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		rawMaterialComboBox.validate();
		            		rawMaterialComboBox.commit();
		            		measurmentUnitComboBox.validate();
		            		measurmentUnitComboBox.commit();		            		
		            		rawMaterialRequirementDTOForm.commit();	            		
		            		productionProcessActivityRequirementRegisterFormView.saveButtonActionRawMaterialRequirementDTOTabLayout(rawMaterialRequirementDTO, editFormMode);
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
		            		rawMaterialComboBox.discard();
		            		measurmentUnitComboBox.discard();
		            		rawMaterialRequirementDTOForm.discard();
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
		
		this.rawMaterialRequirementDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);		
		this.addComponent(this.rawMaterialRequirementDTOForm.getSgpFormLayout());
		//this.addComponent(new Label("hola"));
	}
	
	private Component buildmeasurmentUnitComboBox() throws PmsServiceException{
		this.measurmentUnitComboBox = new ComboBox(this.messages.get(this.VIEW_NAME + "tab.raw.material.requeriment.measurment.unit.combo.box.label"));
		this.measurmentUnitComboBox.setInputPrompt(this.messages.get(this.VIEW_NAME + "tab.raw.material.requeriment.measurment.unit.combo.box.input.prompt"));
        BeanItemContainer<MeasurmentUnitDTO> measurmentUnitDTOBeanItemContainer = new BeanItemContainer<MeasurmentUnitDTO>(MeasurmentUnitDTO.class);
        measurmentUnitDTOBeanItemContainer.addAll(this.listMeasurmentUnitDTO);
        this.measurmentUnitComboBox.setContainerDataSource(measurmentUnitDTOBeanItemContainer);
        this.measurmentUnitComboBox.setItemCaptionPropertyId("measurment_unit_id");
        
        if(this.editFormMode){
        	this.measurmentUnitComboBox.select(this.rawMaterialRequirementDTO.getMeasurmentUnitDTO());
        	this.measurmentUnitComboBox.setValue(this.rawMaterialRequirementDTO.getMeasurmentUnitDTO());
        }
        this.measurmentUnitComboBox.setNullSelectionAllowed(false);
        this.measurmentUnitComboBox.addStyleName("small");
        this.measurmentUnitComboBox.addValueChangeListener(this.setUpValueChangeListenerForMeasurmentUnitComboBox());
        this.measurmentUnitComboBox.setRequired(true);
        this.measurmentUnitComboBox.setRequiredError(this.messages.get(this.VIEW_NAME + "tab.raw.material.requeriment.measurment.unit.combo.box.required.message"));
        this.measurmentUnitComboBox.setEnabled(!this.editFormMode);
        this.measurmentUnitComboBox.setWidth(100, Unit.PERCENTAGE);
        return this.measurmentUnitComboBox;
	}
	
	private Property.ValueChangeListener setUpValueChangeListenerForMeasurmentUnitComboBox(){
    	return new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				logger.info("\n measurmentUnit combo box: value change event"
							+"\n======================================"	+"\nevent.getProperty().getType().getName()) : " 
							+ event.getProperty().getType().getName()
							+"\nevent.getProperty().getValue() : \n" + event.getProperty().getValue());
				rawMaterialRequirementDTO.setId_measurment_unit(((MeasurmentUnitDTO)event.getProperty().getValue()).getId());
				rawMaterialRequirementDTO.setMeasurmentUnitDTO((MeasurmentUnitDTO)event.getProperty().getValue());
			}
    	};
	}
	
	private Component buildrawMaterialComboBox() throws PmsServiceException{
		this.rawMaterialComboBox = new ComboBox(this.messages.get(this.VIEW_NAME + "tab.raw.material.requeriment.raw.material.combo.box.label"));
		this.rawMaterialComboBox.setInputPrompt(this.messages.get(this.VIEW_NAME + "tab.raw.material.requeriment.raw.material.combo.box.input.prompt"));
        BeanItemContainer<RawMaterialDTO> RawMaterialDTOBeanItemContainer = new BeanItemContainer<RawMaterialDTO>(RawMaterialDTO.class);
        RawMaterialDTOBeanItemContainer.addAll(this.listRawMaterialDTO);
        this.rawMaterialComboBox.setContainerDataSource(RawMaterialDTOBeanItemContainer);
        this.rawMaterialComboBox.setItemCaptionPropertyId("raw_material_id");
        
        if(this.editFormMode){
        	this.rawMaterialComboBox.select(this.rawMaterialRequirementDTO.getRawMaterialDTO());
        	this.rawMaterialComboBox.setValue(this.rawMaterialRequirementDTO.getRawMaterialDTO());
        }
        this.rawMaterialComboBox.setNullSelectionAllowed(false);
        this.rawMaterialComboBox.addStyleName("small");
        this.rawMaterialComboBox.addValueChangeListener(this.setUpValueChangeListenerForRawMaterialComboBox());
        this.rawMaterialComboBox.setRequired(true);
        this.rawMaterialComboBox.setRequiredError(this.messages.get(this.VIEW_NAME + "tab.raw.material.requeriment.raw.material.combo.box.required.message"));
        this.rawMaterialComboBox.setEnabled(!this.editFormMode);
        this.rawMaterialComboBox.setWidth(100, Unit.PERCENTAGE);
        return this.rawMaterialComboBox;
	}
	
	private Property.ValueChangeListener setUpValueChangeListenerForRawMaterialComboBox(){
    	return new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				logger.info("\n rawMaterial combo box: value change event"
							+"\n======================================"	+"\nevent.getProperty().getType().getName()) : " 
							+ event.getProperty().getType().getName()
							+"\nevent.getProperty().getValue() : \n" + event.getProperty().getValue());
				rawMaterialRequirementDTO.setId_raw_material(((RawMaterialDTO)event.getProperty().getValue()).getId());
				rawMaterialRequirementDTO.setRawMaterialDTO((RawMaterialDTO)event.getProperty().getValue());
			}
    	};
	}
	
	private void setUpRawMaterialAndMeasurmentUnitLists() throws PmsServiceException{
		this.listRawMaterialExistenceDTO = this.stockManagementService.listRawMaterialExistenceDTO(null);
		this.listRawMaterialDTO = new ArrayList<RawMaterialDTO>();
		this.listMeasurmentUnitDTO = new ArrayList<MeasurmentUnitDTO>();
		for(RawMaterialExistenceDTO vRawMaterialExistenceDTO : this.listRawMaterialExistenceDTO){
			this.listRawMaterialDTO.add(this.productionManagementService.listRawMaterialDTO(new RawMaterialDTO(vRawMaterialExistenceDTO.getId_raw_material())).get(0));
			this.listMeasurmentUnitDTO.add(this.productionManagementService.listMeasurmentUnitDTO(new MeasurmentUnitDTO(vRawMaterialExistenceDTO.getId_measurment_unit())).get(0));
		}
	}
}
