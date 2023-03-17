package py.com.kyron.sgp.gui.view.productionmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MeasurmentUnitDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.TariffDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.MeasurmentUnitRegisterFormViewEvent;
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
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MeasurmentUnitRegisterFormView extends VerticalLayout implements
		View {

	private final Logger logger = LoggerFactory.getLogger(MeasurmentUnitRegisterFormView.class);
	private SgpForm<MeasurmentUnitDTO> measurmentUnitDTOForm;
	private Map<String,String> messages;
	private final String VIEW_NAME = "measurmentunit.register.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private boolean editFormMode;
	private MeasurmentUnitDTO measurmentUnitDTO;
	private TariffDTO tariffDTO;
	private SgpForm<TariffDTO> tariffDTOForm;
	private ProductionManagementService productionManagementService;
	private BussinesSessionUtils bussinesSessionUtils;
	private TabSheet tabs;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private VerticalLayout mainViewLayout;
	private boolean massiveInsertMode;
	private String CALLER_VIEW;
	private Table asociatedTariffTable;
	private ComercialManagementService comercialManagementService;
	private ComboBox currencyComboBox;
	private String selectedTabContentComponentId = null;
	private List<TariffDTO> listTariffDTO = null;
	private VerticalLayout asociatedTariffContent = null;
	public MeasurmentUnitRegisterFormView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\nMeasurmentUnitRegisterFormView..");
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
		this.comercialManagementService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}

	public MeasurmentUnitRegisterFormView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		logger.info("\n==========================================="
					+"\n nMeasurmentUnitRegisterFormView"
					+"\n a view change event has been registered"
					+"\n===========================================");
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n nMeasurmentUnitRegisterFormView" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	@Subscribe
	public void setMeasurmentUnitToEdit(final MeasurmentUnitRegisterFormViewEvent productRegisterFormViewEvent){
		try{
			this.CALLER_VIEW = productRegisterFormViewEvent.getCallerView();
			this.massiveInsertMode = productRegisterFormViewEvent.isMassiveInsertMode();
			this.editFormMode = productRegisterFormViewEvent.getMeasurmentUnitDTO()!=null;
			this.initMainViewLayout();
			this.initTitles();
			this.setUpMainTabForm(productRegisterFormViewEvent.getMeasurmentUnitDTO());
			this.setUpMeasurmentUnitDataTab();
			this.setUpAsociatedTariffTab();
			this.setUpAsociationTariffFormTab();
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
	
	private void setUpMainTabForm(MeasurmentUnitDTO measurmentUnitDTO){
		this.measurmentUnitDTO = (measurmentUnitDTO == null ? new MeasurmentUnitDTO() : measurmentUnitDTO);
		if(this.editFormMode)this.printMeasurmentUnitDTOToEdit();
		this.measurmentUnitDTOForm = new SgpForm<MeasurmentUnitDTO>(MeasurmentUnitDTO.class, new BeanItem<MeasurmentUnitDTO>(this.measurmentUnitDTO), "light", true);
		this.measurmentUnitDTOForm.bindAndSetPositionFormLayoutTextField("measurment_unit_id", this.messages.get(this.VIEW_NAME + "text.field.measurmentunit.id.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "text.field.measurmentunit.id.required.message"), true);
		this.measurmentUnitDTOForm.bindAndSetPositionFormLayoutTextField("measurment_unit_description", this.messages.get(this.VIEW_NAME + "text.field.measurmentunit.description.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "text.field.measurmentunit.description.required.message"), true);
	}
	
	private void printMeasurmentUnitDTOToEdit(){
		logger.info("\n**********\nmeasurmentUnitDTOToEdit\n**********\nthis.measurmentUnitDTO.toString() : \n" + this.measurmentUnitDTO.toString());

	}
	
	private void setUpMeasurmentUnitDataTab(){
    	this.tabs = new TabSheet();    	
    	//this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
    	this.tabs.addStyleName("framed");
    	VerticalLayout measurmentUnitDataContent = new VerticalLayout();
    	measurmentUnitDataContent.setMargin(true);
    	measurmentUnitDataContent.setSpacing(true);
    	measurmentUnitDataContent.setId("measurmentUnitDataContent");
        final Button saveButton = new Button(this.messages.get("application.common.button.save.label"));/*"Guardar"*/
        saveButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{            		
		            		measurmentUnitDTOForm.commit();
		            		logger.info("\nmeasurmentUnitDTO.toString():\n" + measurmentUnitDTO.toString());
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
		            		measurmentUnitDTOForm.discard();
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
		
		this.measurmentUnitDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);
		measurmentUnitDataContent.addComponent(this.measurmentUnitDTOForm.getSgpFormLayout());    	
        Tab measurmentUnitDataTab = this.tabs.addTab(measurmentUnitDataContent, this.messages.get(this.VIEW_NAME + "tab.measurmentunit.data"));
                
        measurmentUnitDataTab.setClosable(false);
        measurmentUnitDataTab.setEnabled(true);
        //TestIcon testIcon = new TestIcon(60);
        //measurmentUnitDataTab.setIcon(testIcon.get(false));
        measurmentUnitDataTab.setIcon(FontAwesome.PENCIL_SQUARE);
	}
	
    private void saveButtonAction() throws PmsServiceException,Exception{
    	if(!this.editFormMode)
    		this.productionManagementService.insertMeasurmentUnitDTO(this.measurmentUnitDTO);
    	else{
    		logger.info("\nhere call update method, then return to caller view, because its a fast edit");
    		this.navigateToCallerView();
    	}
    	
		commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		
		if(!this.editFormMode && this.massiveInsertMode) 
			this.restartFormAfterSuccesfulInsertOperation();    	
    }
    
	private void navigateToCallerView(){
		logger.info("\nMeasurmentUnitRegisterFormView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new MeasurmentUnitRegisterFormViewEvent(this.measurmentUnitDTO, this.CALLER_VIEW, false /*always return false from this view*/));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	private void restartFormAfterSuccesfulInsertOperation() throws PmsServiceException,Exception{
		this.initMainViewLayout();
		this.editFormMode = false;
	    this.initTitles();
	    this.setUpMainTabForm(null);
	    this.setUpMeasurmentUnitDataTab();
	    this.setUpAsociatedTariffTab();
	    this.setUpAsociationTariffFormTab();
	    this.mainViewLayout.addComponent(this.tabs);
	    this.removeAndReAddMainViewLayout();
	}
	
	private void removeAndReAddMainViewLayout(){
		this.removeAllComponents();
		this.addComponent(this.mainViewLayout);
	}

	private void setUpAsociatedTariffTab() throws PmsServiceException{
    	this.asociatedTariffContent = new VerticalLayout();
    	this.asociatedTariffContent.setId("asociatedTariffContent");
    	this.asociatedTariffContent.setMargin(true);
    	this.asociatedTariffContent.setSpacing(true);
    	this.buildAsociatedTariffTable();
    	this.asociatedTariffContent.addComponent(this.asociatedTariffTable);    	
        Tab asociatedTariffDataTab = this.tabs.addTab(this.asociatedTariffContent, this.messages.get(this.VIEW_NAME + "tab.measurmentunit.asociated.tariff"));
        asociatedTariffDataTab.setId("asociatedTariffDataTab");
        asociatedTariffDataTab.setClosable(false);
        asociatedTariffDataTab.setEnabled(this.editFormMode);
        //TestIcon testIcon = new TestIcon(60);
        //measurmentUnitDataTab.setIcon(testIcon.get(false));
        asociatedTariffDataTab.setIcon(FontAwesome.PENCIL_SQUARE_O);
	}
	
	private void buildAsociatedTariffTable() throws PmsServiceException{
    	this.asociatedTariffTable = new Table();
    	BeanItemContainer<TariffDTO> tariffDTOBeanItemContainer	= new BeanItemContainer<TariffDTO>(TariffDTO.class);
    	this.updateListTariffDTO();
   		tariffDTOBeanItemContainer.addAll(this.listTariffDTO);    	
    	this.asociatedTariffTable.setContainerDataSource(tariffDTOBeanItemContainer);     	
    	this.asociatedTariffTable.setVisibleColumns(new Object[] {"tariff_id"});
    	this.asociatedTariffTable.setColumnHeader("tariff_id", this.messages.get(this.VIEW_NAME + "tab.measurmentunit.asociated.tariff.table.tariff.column.tariffid.label"));
    	this.asociatedTariffTable.setColumnAlignment("tariff_id", Table.Align.LEFT);
    	this.asociatedTariffTable.setSizeFull();
    	this.asociatedTariffTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.asociatedTariffTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.asociatedTariffTable.addStyleName(ValoTheme.TABLE_SMALL); 
    	this.asociatedTariffTable.setSelectable(true);
    	this.asociatedTariffTable.setColumnCollapsingAllowed(false);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	this.asociatedTariffTable.setSortContainerPropertyId("tariff_id");
    	this.asociatedTariffTable.setSortAscending(false);
    	this.asociatedTariffTable.setColumnReorderingAllowed(true);
    	this.asociatedTariffTable.setFooterVisible(true);
    	this.asociatedTariffTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
	private void setUpAsociationTariffFormTab() throws PmsServiceException,Exception{
		this.setUpAsociationTariffForm();
    	VerticalLayout asociationTariffContent = new VerticalLayout();
    	asociationTariffContent.setMargin(true);
    	asociationTariffContent.setSpacing(true);
    	asociationTariffContent.setId("asociationTariffContent");
    	asociationTariffContent.addComponent(this.buildMeasurmentUnitIndicator());
    	this.tariffDTOForm.getSgpFormLayout().addComponent(this.buildCurrencyComboBox());
    	this.tariffDTOForm.getSgpFormLayout().addComponent(this.buildAsociationTariffFormOkCancelButtons());
    	//this.tariffDTOForm.getSgpFormLayout().addComponent(this.buildMeasurmentUnitIndicator());
    	asociationTariffContent.addComponent(this.tariffDTOForm.getSgpFormLayout());    
        Tab asociationTariffFormTab = this.tabs.addTab(asociationTariffContent, this.messages.get(this.VIEW_NAME + "tab.measurmentunit.asociation.tariff"));
        
        asociationTariffFormTab.setClosable(false);
        asociationTariffFormTab.setEnabled(this.editFormMode);
        asociationTariffFormTab.setVisible(
        		this.bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
	        	.contains(this.SECURED_PROGRAM_PREFIX + this.VIEW_NAME + "tab.measurmentunit.asociation.tariff.form"));
        //TestIcon testIcon = new TestIcon(60);
        //measurmentUnitDataTab.setIcon(testIcon.get(false));
        asociationTariffFormTab.setIcon(FontAwesome.FLAG);		
	}
	
	private HorizontalLayout buildMeasurmentUnitIndicator(){
		HorizontalLayout measurmentUnitIndicator = new HorizontalLayout();
		measurmentUnitIndicator.setMargin(new MarginInfo(true, false, true, false));
		measurmentUnitIndicator.setSpacing(true);
		measurmentUnitIndicator.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		Label measurmentUnitLabel = new Label(this.messages.get(this.VIEW_NAME + "tab.measurmentunit.asociation.tariff.form.measurmentunit.indicator.label"));
		measurmentUnitLabel.addStyleName("bold");
		Label measurmentUnitDescription = new Label(this.measurmentUnitDTO.getMeasurment_unit_description());
		measurmentUnitDescription.addStyleName("colored");
		measurmentUnitIndicator.addComponent(measurmentUnitLabel);
		measurmentUnitIndicator.addComponent(measurmentUnitDescription);
		return measurmentUnitIndicator;
	}
	
	private void setUpAsociationTariffForm(){
		this.tariffDTO = new TariffDTO();		
		this.tariffDTOForm = new SgpForm<TariffDTO>(TariffDTO.class, new BeanItem<TariffDTO>(this.tariffDTO), "light", true);
		this.tariffDTOForm.bindAndSetPositionFormLayoutTextField("tariff_id", this.messages.get(this.VIEW_NAME + "tab.measurmentunit.asociation.tariff.form.text.field.tariffid.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "tab.measurmentunit.asociation.tariff.form.text.field.tariffid.required.message"), true);		
	}
	
	private HorizontalLayout buildAsociationTariffFormOkCancelButtons(){
        final Button saveButton = new Button(this.messages.get("application.common.button.save.label"));/*"Guardar"*/
        saveButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{            		
		            		tariffDTOForm.commit();
		            		logger.info("\nbefore validate and commit; currencyComboBox.getValue() : " + currencyComboBox.getValue());
		            		CurrencyDTO vCurrencyDTO = ((CurrencyDTO)currencyComboBox.getValue());
		            		logger.info("\nbefore validate and commit; selected CurrencyDTO : " + vCurrencyDTO);
		            		currencyComboBox.validate();
		            		currencyComboBox.commit();
		            		logger.info("\ncurrencyComboBox.getValue() : " + currencyComboBox.getValue());
		            		vCurrencyDTO = null;
		            		vCurrencyDTO = ((CurrencyDTO)currencyComboBox.getValue());
		            		logger.info("\nselected CurrencyDTO : " + vCurrencyDTO);
		            		logger.info("\ntariffDTO.toString():\n" + tariffDTO.toString());
		            		saveTariffButtonAction();
		            		commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
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
		            		tariffDTOForm.discard();
		            		currencyComboBox.discard();
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
		return okCancelHorizontalLayout;
	}
	
	private Component buildCurrencyComboBox() throws PmsServiceException{
		this.currencyComboBox = new ComboBox(this.messages.get(this.VIEW_NAME + "tab.measurmentunit.asociation.tariff.form.currency.combo.label"));
		this.currencyComboBox.setInputPrompt(this.messages.get(this.VIEW_NAME + "tab.measurmentunit.asociation.tariff.form.currency.combo.input.prompt"));
        BeanItemContainer<CurrencyDTO> currencyBeanItemContainer = new BeanItemContainer<CurrencyDTO>(CurrencyDTO.class);
        currencyBeanItemContainer.addAll(this.comercialManagementService.listCurrencyDTO(null));
        this.currencyComboBox.setContainerDataSource(currencyBeanItemContainer);
        this.currencyComboBox.setItemCaptionPropertyId("description");
        this.currencyComboBox.setNullSelectionAllowed(false);
        this.currencyComboBox.addStyleName("small");
        this.currencyComboBox.addValueChangeListener(this.setUpValueChangeListenerForCurrencyComboBox());
        this.currencyComboBox.setRequired(true);
        this.currencyComboBox.setRequiredError(this.messages.get(this.VIEW_NAME + "tab.measurmentunit.asociation.tariff.form.currency.combo.required.error"));
        this.currencyComboBox.setWidth(25, Unit.PERCENTAGE);
        return this.currencyComboBox;
	}
	
	private Property.ValueChangeListener setUpValueChangeListenerForCurrencyComboBox(){
    	return new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				logger.info("\nCurrency combo box: value change event"
							+"\n======================================"	+"\nevent.getProperty().getType().getName()) : " 
							+ event.getProperty().getType().getName()
							+"\nevent.getProperty().getValue() : \n" + event.getProperty().getValue());
				tariffDTO.setId_currency(((CurrencyDTO)event.getProperty().getValue()).getId());
			}
    	};
	}
	
    private void saveTariffButtonAction() throws PmsServiceException, Exception{
    	this.tariffDTO.setId_measurment_unit(this.measurmentUnitDTO.getId());
   		this.productionManagementService.insertTariffDTO(this.tariffDTO);		
		this.tabs.removeTab(this.tabs.getTab(2));
		this.setUpAsociationTariffFormTab();
		this.tabs.setSelectedTab(2);
    }
    
    private SelectedTabChangeListener setUpTabsSelectionListener(){
    	return new SelectedTabChangeListener(){

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				// TODO Auto-generated method stub				
				selectedTabContentComponentId =  event.getTabSheet().getSelectedTab().getId();
				logger.info("\ntab selection listener"
						+"\n selectedTabContentComponentId : " + selectedTabContentComponentId);
				
				if(selectedTabContentComponentId!=null && selectedTabContentComponentId.equals("asociatedTariffContent")){
					logger.info("\nupdating asociated tariff data source...");
					try {
						/*updateListTariffDTO();
						asociatedTariffTable.refreshRowCache();
						event.getTabSheet().getSelectedTab().markAsDirtyRecursive();*/
						asociatedTariffContent.removeAllComponents();
						buildAsociatedTariffTable();
						asociatedTariffContent.addComponent(asociatedTariffTable);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("\nerror", e);
						commonExceptionErrorNotification.showErrorMessageNotification(e);
					}
				}
			}					
			
    		
    	};
    }
	
    private void updateListTariffDTO() throws PmsServiceException{
    	if(this.measurmentUnitDTO.getId()!=null)
    		this.listTariffDTO = this.productionManagementService.listTariffDTO(new TariffDTO(this.measurmentUnitDTO.getId(),null));
    	if(this.listTariffDTO == null) this.listTariffDTO = new ArrayList<TariffDTO>();
    }
}
