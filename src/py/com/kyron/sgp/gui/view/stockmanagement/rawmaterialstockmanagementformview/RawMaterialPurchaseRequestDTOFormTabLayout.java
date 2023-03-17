package py.com.kyron.sgp.gui.view.stockmanagement.rawmaterialstockmanagementformview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MeasurmentUnitDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialExistenceDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialPurchaseRequestDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialPurchaseRequestDTO;
import py.com.kyron.sgp.bussines.stockmanagement.service.StockManagementService;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.stockmanagement.RawMaterialStockManagementFormView;
import py.com.kyron.sgp.gui.view.utils.commponents.personmanagement.PersonFinderWindow;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

@SuppressWarnings("serial")
public class RawMaterialPurchaseRequestDTOFormTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(RawMaterialPurchaseRequestDTOFormTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "rawmaterial.stock.management.form.";
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final boolean rawMaterialPurchaseRequestDTOFormEditMode;
	private SgpForm<RawMaterialPurchaseRequestDTO> rawMaterialPurchaseRequestDTOForm;
	private final RawMaterialPurchaseRequestDTO rawMaterialPurchaseRequestDTO;
	private final RawMaterialStockManagementFormView rawMaterialStockManagementFormView;
	private ComboBox measurmentUnitComboBox;
	private ProductionManagementService productionManagementService;
	private PersonDTO personDTO;
	private SgpForm<PersonDTO> personDTOForm;
	private VerticalLayout personDTOFormVerticalLayout;
	private PersonFinderWindow personFinderWindow;
	private Button searchSupplierButton;
	private List<MeasurmentUnitDTO> listMeasurmentUnitDTO;
	private StockManagementService stockManagementService;
	private final RawMaterialDTO rawMaterialDTO;
	
	public RawMaterialPurchaseRequestDTOFormTabLayout(
			RawMaterialStockManagementFormView rawMaterialStockManagementFormView,
			RawMaterialDTO rawMaterialDTO,
			RawMaterialPurchaseRequestDTO rawMaterialPurchaseRequestDTO,
			boolean rawMaterialPurchaseRequestDTOFormEditMode) {
		// TODO Auto-generated constructor stub
		this.rawMaterialPurchaseRequestDTOFormEditMode = rawMaterialPurchaseRequestDTOFormEditMode;
		this.rawMaterialStockManagementFormView = rawMaterialStockManagementFormView;
		this.rawMaterialPurchaseRequestDTO = rawMaterialPurchaseRequestDTO;
		this.rawMaterialDTO = rawMaterialDTO;
		try{
			logger.info("\n RawMaterialPurchaseRequestDTOFormTabLayout()...");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			//this.setSpacing(true);
			this.setMargin(true);
			Responsive.makeResponsive(this);
			this.initServices();
	        this.initTitles();
	        this.fillMeasurmentUnitList();
	        if(this.rawMaterialPurchaseRequestDTO.getPersonDTO()!=null)this.setUpPersonFormVerticalLayout(this.rawMaterialPurchaseRequestDTO.getPersonDTO());
	        else this.setUpPersonFormVerticalLayout(null);
	        this.setUprawMaterialPurchaseRequestDTOForm();
	        this.setUpOkCancelButtons();
	        this.addComponent(this.personDTOFormVerticalLayout);
	        this.addComponent(this.rawMaterialPurchaseRequestDTOForm.getSgpFormLayout());
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}		
	}

	/*public rawMaterialPurchaseRequestDTOFormTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	

	private void initServices() throws Exception{
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
		this.stockManagementService = (StockManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.STOCK_MANAGEMENT_SERVICE);
	}
	
	private void initTitles(){
		Label title = new Label(!this.rawMaterialPurchaseRequestDTOFormEditMode ? 
				this.messages.get(this.VIEW_NAME + "tab.rawmaterial.purchase.request.form.main.title.register") : 
					this.messages.get(this.VIEW_NAME + "tab.rawmaterial.purchase.request.form.main.title.edit"));
        title.addStyleName("h1");
        this.addComponent(title);
	}

	private void setUprawMaterialPurchaseRequestDTOForm() throws PmsServiceException{
		
		if(this.rawMaterialPurchaseRequestDTOFormEditMode)
			this.printRawMaterialPurchaseRequestDTOToEdit();
		
		this.rawMaterialPurchaseRequestDTOForm = new SgpForm<RawMaterialPurchaseRequestDTO>(RawMaterialPurchaseRequestDTO.class, new BeanItem<RawMaterialPurchaseRequestDTO>(this.rawMaterialPurchaseRequestDTO), "light", true);
		this.rawMaterialPurchaseRequestDTOForm.getSgpFormLayout().addComponent(this.buildmeasurmentUnitComboBox());
		this.rawMaterialPurchaseRequestDTOForm.bindAndSetPositionFormLayoutTextField(
				"quantity", 
				this.messages.get("application.common.quantity.label")/**/, 
				true, 100, true,
				this.messages.get("application.common.quantity.tex.field.required.message"),
				true);

	}
	
	private void printRawMaterialPurchaseRequestDTOToEdit(){
		logger.info("\n**********\nRawMaterialPurchaseRequestDTOToEdit\n**********\nthis.RawMaterialPurchaseRequestDTO.toString() : \n" + this.rawMaterialPurchaseRequestDTO.toString());

	}
	
	private Component buildmeasurmentUnitComboBox() throws PmsServiceException{
		this.measurmentUnitComboBox = new ComboBox(this.messages.get(this.VIEW_NAME + "tab.rawmaterialexistence.form.measurment.unit.combo.box.label"));
		this.measurmentUnitComboBox.setInputPrompt(this.messages.get(this.VIEW_NAME + "tab.rawmaterialexistence.form.measurment.unit.combo.box.input.prompt"));
        BeanItemContainer<MeasurmentUnitDTO> measurmentUnitDTOBeanItemContainer = new BeanItemContainer<MeasurmentUnitDTO>(MeasurmentUnitDTO.class);
        measurmentUnitDTOBeanItemContainer.addAll(this.listMeasurmentUnitDTO);
        this.measurmentUnitComboBox.setContainerDataSource(measurmentUnitDTOBeanItemContainer);
        this.measurmentUnitComboBox.setItemCaptionPropertyId("measurment_unit_id");
        
        if(this.rawMaterialPurchaseRequestDTOFormEditMode){
        	this.measurmentUnitComboBox.select(this.rawMaterialPurchaseRequestDTO.getMeasurmentUnitDTO());
        	this.measurmentUnitComboBox.setValue(this.rawMaterialPurchaseRequestDTO.getMeasurmentUnitDTO());
        }
        this.measurmentUnitComboBox.setNullSelectionAllowed(false);
        this.measurmentUnitComboBox.addStyleName("small");
        this.measurmentUnitComboBox.addValueChangeListener(this.setUpValueChangeListenerForMeasurmentUnitComboBox());
        this.measurmentUnitComboBox.setRequired(true);
        this.measurmentUnitComboBox.setRequiredError(this.messages.get(this.VIEW_NAME + "tab.rawmaterialexistence.form.measurment.unit.combo.box.required.message"));
        //this.measurmentUnitComboBox.setEnabled(!this.rawMaterialPurchaseRequestDTOFormEditMode);
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
				rawMaterialPurchaseRequestDTO.setId_measurment_unit(((MeasurmentUnitDTO)event.getProperty().getValue()).getId());
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
		            		rawMaterialPurchaseRequestDTOForm.commit();
		            		measurmentUnitComboBox.validate();
		            		measurmentUnitComboBox.commit();
		            		logger.info("\nrawMaterialExistenceDTO.toString():\n" + rawMaterialPurchaseRequestDTO.toString());		            		
		            		rawMaterialStockManagementFormView.saveButtonRawMaterialPurchaseRequestDTOFormTabLayout(rawMaterialPurchaseRequestDTO, rawMaterialPurchaseRequestDTOFormEditMode);
		            		logger.info("\n when click in save button; go to purchase requests table tab"); 
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
		            		rawMaterialPurchaseRequestDTOForm.discard();
		            		measurmentUnitComboBox.discard();		            		
		            		rawMaterialStockManagementFormView.cancelButtonRawMaterialPurchaseRequestDTOFormTabLayout(rawMaterialPurchaseRequestDTOFormEditMode);
		            		logger.info("\n when click in cancel button; go to purchase requests table tab");
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		HorizontalLayout okCancelHorizontalLayout = new HorizontalLayout();
		okCancelHorizontalLayout.setMargin(new MarginInfo(true, false, true, false));
		okCancelHorizontalLayout.setSpacing(true);
		//okCancelHorizontalLayout.setMargin(true);
		okCancelHorizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		okCancelHorizontalLayout.addComponent(saveButton);
		okCancelHorizontalLayout.addComponent(cancelButton);
		this.rawMaterialPurchaseRequestDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);
	}
	
	private void setUpPersonDTOForm(){
		this.personDTOForm = new SgpForm<PersonDTO>(PersonDTO.class, new BeanItem<PersonDTO>(this.personDTO), "light", true);
		//this.personDTOForm.getSgpFormLayout().addComponent(this.buildSearchSupplierButton());
    	if(this.personDTO.getCommercial_name()!=null){
    		this.personDTOForm.bindAndSetPositionFormLayoutTextField("ruc", this.messages.get(this.VIEW_NAME + "tab.rawmaterial.purchase.request.form.text.field.ruc.label")/**/, true, 100, false,null, false);
    		this.personDTOForm.bindAndSetPositionFormLayoutTextField("commercial_name", this.messages.get(this.VIEW_NAME + "tab.rawmaterial.purchase.request.form.text.field.comercial.name.label")/**/, true, 100, false,null, false);
    	}if(this.personDTO.getPersonal_name()!=null && this.personDTO.getPersonal_last_name()!=null){
    		this.personDTOForm.bindAndSetPositionFormLayoutTextField("personal_civil_id_document", this.messages.get(this.VIEW_NAME + "tab.rawmaterial.purchase.request.form.text.field.personal.civil.id.document.label")/**/, true, 100, false,null, false);
    		this.personDTOForm.bindAndSetPositionFormLayoutTextField("personal_name", this.messages.get(this.VIEW_NAME + "tab.rawmaterial.purchase.request.form.text.field.personal.name.label")/**/, true, 100, false,null, false);
    		this.personDTOForm.bindAndSetPositionFormLayoutTextField("personal_last_name", this.messages.get(this.VIEW_NAME + "tab.rawmaterial.purchase.request.form.text.field.personal.last.name.label")/**/, true, 100, false,null, false);
    	}
	}
	
	private void initPersonDTOFormVerticalLayout(){
		if(this.personDTOFormVerticalLayout == null){
			this.personDTOFormVerticalLayout = new VerticalLayout();
			this.personDTOFormVerticalLayout.setSpacing(true);
			//this.personDTOFormVerticalLayout.setMargin(true);
		}else this.personDTOFormVerticalLayout.removeAllComponents();	
		this.personDTOFormVerticalLayout.addComponent(this.buildSearchSupplierButton());
	}
	
	private void setUpPersonFormVerticalLayout(PersonDTO vPersonDTO){
		this.initPersonDTOFormVerticalLayout();
		if(vPersonDTO!=null)this.personDTO = vPersonDTO;
		else this.personDTO = new PersonDTO();
		if(this.rawMaterialPurchaseRequestDTO!=null)this.rawMaterialPurchaseRequestDTO.setId_person_supplier(this.personDTO.getId());
		this.setUpPersonDTOForm();
		this.personDTOFormVerticalLayout.addComponent(this.personDTOForm.getSgpFormLayout());
	}
	
	private HorizontalLayout buildSearchSupplierButton(){
		HorizontalLayout header = new HorizontalLayout();
		header.setSpacing(true);
		//header.setMargin(true);
		header.addStyleName("toolbar");
		Label title = new Label(this.messages.get("application.common.supplier.label"));
		title.addStyleName("colored");
		header.addComponent(title);
		header.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
		if(this.searchSupplierButton == null)this.setUpSearchSupplierButton();
		header.addComponent(this.searchSupplierButton);
		header.setComponentAlignment(this.searchSupplierButton, Alignment.MIDDLE_RIGHT);
		return header;
	}
	
	private void setUpSearchSupplierButton(){
		this.searchSupplierButton = new Button(FontAwesome.SEARCH_PLUS);
		this.searchSupplierButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.rawmaterial.purchase.request.form.supplier.search.button.description"));
		this.searchSupplierButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                try{
                	personFinderWindow = new PersonFinderWindow(false,true,false);
                	personFinderWindow.addCloseListener(setUpPersonFinderWindowCloseListener());
                	personFinderWindow.adjuntWindowSizeAccordingToCientDisplay();
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }
            }
        });
	}
	
	private CloseListener setUpPersonFinderWindowCloseListener(){
		return new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{
					//List<PersonDTO> listPerson =personFinderWindow.getListPersonDTO();
					PersonDTO vPersonDTO = personFinderWindow.getPersonDTOTableRowSelected();
					logger.info("\n******************************"
								+"\nthe person finder window has been closed"
								+"\nperson found: \n" + vPersonDTO
								+"\n******************************");
					setUpPersonFormVerticalLayout(vPersonDTO);
					refreshParentContainer();
					//setSizeFull();
					//markAsDirty();
					//reDrawAllLayaout(vPersonDTO);
				}catch(Exception ex){
					commonExceptionErrorNotification.showErrorMessageNotification(ex);
				}
			}			
		};
	}
	
	private void reDrawAllLayaout(PersonDTO vPersonDTO) throws PmsServiceException{
        this.removeAllComponents();
        this.initTitles();
		this.setUpPersonFormVerticalLayout(vPersonDTO);
        this.setUprawMaterialPurchaseRequestDTOForm();
        this.setUpOkCancelButtons();
        this.addComponent(this.personDTOFormVerticalLayout);
        this.addComponent(this.rawMaterialPurchaseRequestDTOForm.getSgpFormLayout());		
	}
	
	private void refreshParentContainer(){
		HasComponents vHasComponents = this.getParent();
		logger.info("\n****************************"
				+ "\nrefreshing vertical layout parent container, parent id: " + vHasComponents.getId()
				+ "\n class type: "+ vHasComponents.getClass()
				+ "\n****************************");
		//vHasComponents.markAsDirtyRecursive();
		/*TabSheet vTabSheet = (TabSheet)vHasComponents;
		Tab vTab = vTabSheet.getTab(4);
		vTabSheet.removeTab(vTab);
		vTabSheet.addTab(vTab.getComponent(),4);
		vTabSheet.setSelectedTab(4);*/
	}
	/*private AttachListener setUpPersonFinderAttachListener(){
		return new AttachListener(){

			@Override
			public void attach(AttachEvent event) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}*/
	
	private void fillMeasurmentUnitList() throws PmsServiceException{
		this.listMeasurmentUnitDTO = new ArrayList<MeasurmentUnitDTO>();
		List<RawMaterialExistenceDTO> listRawMaterialExistenceDTO = this.stockManagementService.listRawMaterialExistenceDTO(new RawMaterialExistenceDTO(this.rawMaterialDTO.getId(), null));
		for(RawMaterialExistenceDTO vRawMaterialExistenceDTO : listRawMaterialExistenceDTO){
			this.listMeasurmentUnitDTO.add(
			this.productionManagementService.listMeasurmentUnitDTO(new MeasurmentUnitDTO(vRawMaterialExistenceDTO.getId_measurment_unit())).get(0));
		}
		
	}
}
