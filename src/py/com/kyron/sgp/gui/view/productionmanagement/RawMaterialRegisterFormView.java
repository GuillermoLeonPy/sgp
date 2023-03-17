package py.com.kyron.sgp.gui.view.productionmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.ProductRegisterFormViewEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.RawMaterialRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.comercialmanagement.ProductRegisterFormView;
import py.com.kyron.sgp.gui.view.utils.TableNumericColumnCellLabelHelper;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ManPowerCostDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialCostDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.TariffDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;

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
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class RawMaterialRegisterFormView extends VerticalLayout implements View {

	private final Logger logger = LoggerFactory.getLogger(RawMaterialRegisterFormView.class);
	private SgpForm<RawMaterialDTO> rawMaterialDTOForm;
	private Map<String,String> messages;
	private final String VIEW_NAME = "rawmaterial.register.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private boolean editFormMode;
	private RawMaterialDTO rawMaterialDTO;
	private ProductionManagementService productionManagementService;
	private BussinesSessionUtils bussinesSessionUtils;
	private TabSheet tabs;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private VerticalLayout mainViewLayout;
	private boolean massiveInsertMode;
	private String CALLER_VIEW;
	private Table rawMaterialCostTable = null;
	private VerticalLayout rawMaterialCostTableContainer = null;
	private List<RawMaterialCostDTO> listRawMaterialCostDTO = null;
	private String selectedTabContentComponentId = null;
	//private Label isValidLabel = null;
	private boolean rawMaterialCostFormTabEditMode;
	private RawMaterialCostDTO rawMaterialCostDTO;
	private SgpForm<RawMaterialCostDTO> rawMaterialCostDTOForm;
	private VerticalLayout rawMaterialCostFormContainer = null;
	private ComboBox tariffComboBox = null;
	private boolean enableRawMaterialCostDTOForm;
	private Tab rawMaterialCostFormContainerTab;
	
	public RawMaterialRegisterFormView() {
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
	        /*this.isValidLabel = new Label(this.messages.get("application.common.validity.end.date.is.valid.value"));
	        this.isValidLabel.addStyleName("colored");*/
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}
	
	private void initServices() throws Exception{
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}

	public RawMaterialRegisterFormView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		logger.info("\n==========================================="
					+"\n RawMaterialRegisterFormView"
					+"\n a view change event has been registered"
					+"\n===========================================");
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n RawMaterialRegisterFormView" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()

	@Subscribe
	public void setRawMaterialToEdit(final RawMaterialRegisterFormViewEvent productRegisterFormViewEvent){
		try{
			this.CALLER_VIEW = productRegisterFormViewEvent.getCallerView();
			this.massiveInsertMode = productRegisterFormViewEvent.isMassiveInsertMode();
			this.editFormMode = productRegisterFormViewEvent.getRawMaterialDTO()!=null;
			this.initMainViewLayout();
			this.initTitles();
			this.setUpMainTabForm(productRegisterFormViewEvent.getRawMaterialDTO());
			this.setUpRawMaterialDataTab();
			this.setUpRawMaterialCostTableTab();
			this.editRawMaterialCost(null);
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
	
	private void setUpMainTabForm(RawMaterialDTO rawMaterialDTO){
		this.rawMaterialDTO = (rawMaterialDTO == null ? new RawMaterialDTO() : rawMaterialDTO);
		if(this.editFormMode)this.printRawMaterialDTOToEdit();
		this.rawMaterialDTOForm = new SgpForm<RawMaterialDTO>(RawMaterialDTO.class, new BeanItem<RawMaterialDTO>(this.rawMaterialDTO), "light", true);
		this.rawMaterialDTOForm.bindAndSetPositionFormLayoutTextField("raw_material_id", this.messages.get(this.VIEW_NAME + "text.field.rawmaterial.id.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "text.field.rawmaterial.id.required.message"), true);
		this.rawMaterialDTOForm.bindAndSetPositionFormLayoutTextField("raw_material_description", this.messages.get(this.VIEW_NAME + "text.field.rawmaterial.description.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "text.field.rawmaterial.description.required.message"), true);
	}
	
	private void printRawMaterialDTOToEdit(){
		logger.info("\n**********\nrawMaterialDTOToEdit\n**********\nthis.rawMaterialDTO.toString() : \n" + this.rawMaterialDTO.toString());

	}
	
	private void setUpRawMaterialDataTab(){
    	this.tabs = new TabSheet();    	
    	//this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
    	this.tabs.addStyleName("framed");
    	VerticalLayout rawMaterialDataContent = new VerticalLayout();
    	rawMaterialDataContent.setMargin(true);
    	rawMaterialDataContent.setSpacing(true);
    	
        final Button saveButton = new Button(this.messages.get("application.common.button.save.label"));/*"Guardar"*/
        saveButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{            		
		            		rawMaterialDTOForm.commit();
		            		logger.info("\nrawMaterialDTO.toString():\n" + rawMaterialDTO.toString());
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
		            		rawMaterialDTOForm.discard();
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
		
		this.rawMaterialDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);
		rawMaterialDataContent.addComponent(this.rawMaterialDTOForm.getSgpFormLayout());    	
        Tab rawMaterialDataTab = this.tabs.addTab(rawMaterialDataContent, this.messages.get(this.VIEW_NAME + "tab.rawmaterial.data"));
        
        rawMaterialDataTab.setClosable(false);
        rawMaterialDataTab.setEnabled(true);
        //TestIcon testIcon = new TestIcon(60);
        //rawMaterialDataTab.setIcon(testIcon.get(false));
        rawMaterialDataTab.setIcon(FontAwesome.BOOK);
	}
	
    private void saveButtonAction() throws PmsServiceException,Exception{
    	if(!this.editFormMode)
    		this.productionManagementService.insertRawMaterialDTO(this.rawMaterialDTO);
    	else{
    		logger.info("\nhere call update method, then return to caller view, because its a fast edit");
    		this.navigateToCallerView();
    	}
    	
		commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		
		if(!this.editFormMode && this.massiveInsertMode) 
			this.restartFormAfterSuccesfulInsertOperation();    	
    }
    
	private void navigateToCallerView(){
		logger.info("\nRawMaterialRegisterFormView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new RawMaterialRegisterFormViewEvent(this.rawMaterialDTO, this.CALLER_VIEW, false /*always return false from this view*/));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	private void restartFormAfterSuccesfulInsertOperation() throws PmsServiceException,Exception{
		this.initMainViewLayout();
		this.editFormMode = false;
	    this.initTitles();
	    this.setUpMainTabForm(null);
	    this.setUpRawMaterialDataTab();
		this.setUpRawMaterialCostTableTab();
		this.editRawMaterialCost(null);
	    this.mainViewLayout.addComponent(this.tabs);
	    this.removeAndReAddMainViewLayout();
	}
	
	private void removeAndReAddMainViewLayout(){
		this.removeAllComponents();
		this.addComponent(this.mainViewLayout);
	}
	
	private void setUpRawMaterialCostTableTab() throws PmsServiceException{
    	this.rawMaterialCostTableContainer = new VerticalLayout();
    	this.rawMaterialCostTableContainer.setId("rawMaterialCostTableContainer");
    	this.rawMaterialCostTableContainer.setMargin(true);
    	this.rawMaterialCostTableContainer.setSpacing(true);
    	this.buildrawMaterialCostTable();
    	this.rawMaterialCostTableContainer.addComponent(this.rawMaterialCostTable);    	
        Tab asociatedTariffDataTab = this.tabs.addTab(this.rawMaterialCostTableContainer, this.messages.get(this.VIEW_NAME + "tab.rawmaterial.cost.table"));
        
        asociatedTariffDataTab.setClosable(false);
        asociatedTariffDataTab.setEnabled(this.editFormMode);
        //TestIcon testIcon = new TestIcon(60);
        //measurmentUnitDataTab.setIcon(testIcon.get(false));
        asociatedTariffDataTab.setIcon(FontAwesome.REGISTERED);
	}
	
	private void buildrawMaterialCostTable() throws PmsServiceException{
    	this.rawMaterialCostTable = new Table();
    	BeanItemContainer<RawMaterialCostDTO> rawMaterialCostDTOBeanItemContainer	= new BeanItemContainer<RawMaterialCostDTO>(RawMaterialCostDTO.class);
    	this.updateListRawMaterialCostDTO();
    	rawMaterialCostDTOBeanItemContainer.addAll(this.listRawMaterialCostDTO);    	
    	this.rawMaterialCostTable.setContainerDataSource(rawMaterialCostDTOBeanItemContainer); 
    	
    	this.rawMaterialCostTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final RawMaterialCostDTO vRawMaterialCostDTO = (RawMaterialCostDTO)itemId;
				final Button editButton = new Button();
				editButton.setDescription(messages.get("application.common.table.column.operations.apply.validity.end.date.button.description"));
				editButton.setIcon(FontAwesome.EDIT);
				editButton.addStyleName("borderless");
				editButton.addClickListener(new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		                logger.info("\n editar RawMaterialCostDTO...\n" + vRawMaterialCostDTO.toString());
		                try{		                	
		                	editRawMaterialCost(vRawMaterialCostDTO);
		                }catch(Exception e){
		                	commonExceptionErrorNotification.showErrorMessageNotification(e);
		                }			                
		            }
		        });
				
				try {
					editButton.setEnabled(vRawMaterialCostDTO.getValidity_end_date()==null
							&&
							bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
							.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "tab.rawmaterial.cost.register.form"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("\nerror",e);
				}
				
				return editButton;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//rawMaterialCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.rawMaterialCostTable.addGeneratedColumn("g_tariff_id", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final RawMaterialCostDTO vRawMaterialCostDTO = (RawMaterialCostDTO)itemId;					
				return vRawMaterialCostDTO.getTariffDTO().getTariff_id();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//rawMaterialCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.rawMaterialCostTable.addGeneratedColumn("g_registration_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final RawMaterialCostDTO vRawMaterialCostDTO = (RawMaterialCostDTO)itemId;					
				return SgpUtils.dateFormater.format(vRawMaterialCostDTO.getRegistration_date());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//rawMaterialCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.rawMaterialCostTable.addGeneratedColumn("g_validity_end_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final RawMaterialCostDTO vRawMaterialCostDTO = (RawMaterialCostDTO)itemId;
				final Label isValidLabel = new Label(messages.get("application.common.validity.end.date.is.valid.value"));
		        isValidLabel.addStyleName("colored");
				return (vRawMaterialCostDTO.getValidity_end_date()!=null ? 
						SgpUtils.dateFormater.format(vRawMaterialCostDTO.getValidity_end_date()):isValidLabel);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//rawMaterialCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.rawMaterialCostTable.addGeneratedColumn("g_purchase_invoice_average_cost", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final RawMaterialCostDTO vRawMaterialCostDTO = (RawMaterialCostDTO)itemId;
				if(vRawMaterialCostDTO.getPurchase_invoice_average_cost() == null)return new Label("");
				final Label label = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vRawMaterialCostDTO.getPurchase_invoice_average_cost());
    			label.addStyleName("colored");
    			final HorizontalLayout vHorizontalLayout = new HorizontalLayout(label);
				vHorizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//rawMaterialCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.rawMaterialCostTable.setVisibleColumns(new Object[] {"operations","g_tariff_id","tariff_amount","g_purchase_invoice_average_cost","g_registration_date","g_validity_end_date"});
    	this.rawMaterialCostTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.rawMaterialCostTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.rawMaterialCostTable.setColumnHeader("g_tariff_id", this.messages.get("application.common.table.column.tariff.label"));
    	this.rawMaterialCostTable.setColumnAlignment("tariff_amount", Table.Align.RIGHT);
    	
    	this.rawMaterialCostTable.setColumnHeader("tariff_amount", this.messages.get("application.common.table.column.any.amount.label"));
    	this.rawMaterialCostTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);

    	this.rawMaterialCostTable.setColumnHeader("g_purchase_invoice_average_cost", this.messages.get(this.VIEW_NAME + "tab.rawmaterial.cost.table.column.purchase.invoice.average.cost"));
    	this.rawMaterialCostTable.setColumnAlignment("g_purchase_invoice_average_cost", Table.Align.RIGHT);
    	
    	this.rawMaterialCostTable.setColumnHeader("g_registration_date", this.messages.get("application.common.table.column.registration.date.label"));
    	this.rawMaterialCostTable.setColumnAlignment("g_validity_end_date", Table.Align.LEFT);
    	
    	this.rawMaterialCostTable.setColumnHeader("g_validity_end_date", this.messages.get("application.common.table.column.validity.end.date.label"));
    	this.rawMaterialCostTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
    	this.rawMaterialCostTable.setSizeFull();
    	this.rawMaterialCostTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.rawMaterialCostTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.rawMaterialCostTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.rawMaterialCostTable.setColumnExpandRatio("operations", 0.009f); 
    	this.rawMaterialCostTable.setColumnExpandRatio("g_tariff_id", 0.025f);    	
    	this.rawMaterialCostTable.setColumnExpandRatio("tariff_amount", 0.015f);
    	this.rawMaterialCostTable.setColumnExpandRatio("g_purchase_invoice_average_cost", 0.015f);
    	this.rawMaterialCostTable.setColumnExpandRatio("g_registration_date", 0.018f);
    	this.rawMaterialCostTable.setColumnExpandRatio("g_validity_end_date", 0.018f);
    	this.rawMaterialCostTable.setSelectable(true);
    	this.rawMaterialCostTable.setColumnCollapsingAllowed(true);
    	this.rawMaterialCostTable.setColumnCollapsible("operations", false);
    	this.rawMaterialCostTable.setColumnCollapsible("g_registration_date", true);
    	this.rawMaterialCostTable.setColumnCollapsible("g_validity_end_date", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.rawMaterialCostTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.rawMaterialCostTable.setSortAscending(false);
    	this.rawMaterialCostTable.setColumnReorderingAllowed(true);
    	this.rawMaterialCostTable.setFooterVisible(true);
    	this.rawMaterialCostTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
    private void updateListRawMaterialCostDTO() throws PmsServiceException{
    	if(this.rawMaterialDTO.getId()!=null)
    		this.listRawMaterialCostDTO = this.productionManagementService.listRawMaterialCostDTO(new RawMaterialCostDTO(this.rawMaterialDTO.getId(), null));
    	if(this.listRawMaterialCostDTO == null) this.listRawMaterialCostDTO = new ArrayList<RawMaterialCostDTO>();
    }
    
    private SelectedTabChangeListener setUpTabsSelectionListener(){
    	return new SelectedTabChangeListener(){

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				// TODO Auto-generated method stub				
				selectedTabContentComponentId =  event.getTabSheet().getSelectedTab().getId();
				logger.info("\ntab selection listener"
						+"\n selectedTabContentComponentId : " + selectedTabContentComponentId);
				
				if(selectedTabContentComponentId!=null && selectedTabContentComponentId.equals("rawMaterialCostTableContainer")){
					logger.info("\nupdating asociated tariff data source...");
					try {
						/*updateListTariffDTO();
						asociatedTariffTable.refreshRowCache();
						event.getTabSheet().getSelectedTab().markAsDirtyRecursive();*/
						rawMaterialCostTableContainer.removeAllComponents();
						buildrawMaterialCostTable();
						rawMaterialCostTableContainer.addComponent(rawMaterialCostTable);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("\nerror", e);
						commonExceptionErrorNotification.showErrorMessageNotification(e);
					}
				}
			}					
			
    		
    	};
    }
    
    private void editRawMaterialCost(RawMaterialCostDTO vRawMaterialCostDTO) throws PmsServiceException,Exception{
    	if(vRawMaterialCostDTO!=null && vRawMaterialCostDTO.getId()!=null){
    		this.rawMaterialCostFormTabEditMode = true;
    		this.rawMaterialCostDTO = vRawMaterialCostDTO;
    		
    	}else{
    		this.rawMaterialCostFormTabEditMode = false;
    		this.rawMaterialCostDTO = new RawMaterialCostDTO();     		
    	}
    	if(this.rawMaterialCostFormContainerTab!=null)this.tabs.removeTab(this.rawMaterialCostFormContainerTab);
    	this.rawMaterialCostDTO.setId_raw_material(this.rawMaterialDTO.getId());
    	this.initRawMaterialCostFormMainViewLayout();
    	this.initTitlesRawMaterialCostForm();
    	this.setUpRawMaterialCostForm();
    	if(vRawMaterialCostDTO!=null && vRawMaterialCostDTO.getId()!=null){
    		this.enableRawMaterialCostDTOForm = true;    		
    	}else
    		this.setEnableRawMaterialCostDTOFormFlag();
    	this.setUpRawMaterialCostFormTab();
    	if(vRawMaterialCostDTO!=null){
    		this.tabs.setSelectedTab(2);
    		if(vRawMaterialCostDTO.getTariffDTO()!=null){
    			this.tariffComboBox.select(this.rawMaterialCostDTO.getTariffDTO());
    			this.tariffComboBox.setValue(this.rawMaterialCostDTO.getTariffDTO());
    		}
    	}
    }
    
	private void initRawMaterialCostFormMainViewLayout(){
		this.rawMaterialCostFormContainer = new VerticalLayout();
		this.rawMaterialCostFormContainer.setSpacing(true);
		this.rawMaterialCostFormContainer.setMargin(true);
	}
	
	private void initTitlesRawMaterialCostForm(){
		Label title = new Label(!this.rawMaterialCostFormTabEditMode ? this.messages.get(this.VIEW_NAME + "tab.rawmaterial.cost.register.form.main.title.register") : this.messages.get(this.VIEW_NAME + "tab.rawmaterial.cost.register.form.main.title.edit"));
        title.addStyleName("h1");
        this.rawMaterialCostFormContainer.addComponent(title);
	}
	
	private void setUpRawMaterialCostForm() throws PmsServiceException{
		
		if(this.rawMaterialCostFormTabEditMode)this.printRawMaterialCostDTOToEdit();
		this.rawMaterialCostDTOForm = new SgpForm<RawMaterialCostDTO>(RawMaterialCostDTO.class, new BeanItem<RawMaterialCostDTO>(this.rawMaterialCostDTO), "light", true);
		this.rawMaterialCostDTOForm.getSgpFormLayout().addComponent(this.buildTariffComboBox());
		this.rawMaterialCostDTOForm.bindAndSetPositionFormLayoutTextField(
				"tariff_amount", 
				this.messages.get(this.VIEW_NAME + "tab.rawmaterial.cost.register.form.tariff.amount.text.field.label")/**/, 
				true, 100, true,
				this.messages.get(this.VIEW_NAME + "tab.rawmaterial.cost.register.form.tariff.amount.text.field.required.message"),
				!this.rawMaterialCostFormTabEditMode);
	}
	
	private void printRawMaterialCostDTOToEdit(){
		logger.info("\n**********\nrawMaterialCostDTOToEdit\n**********\nthis.rawMaterialCostDTO.toString() : \n" + this.rawMaterialCostDTO.toString());

	}
	
	private Component buildTariffComboBox() throws PmsServiceException{
		this.tariffComboBox = new ComboBox(this.messages.get(this.VIEW_NAME + "tab.rawmaterial.cost.register.form.tariff.combo.label"));
		this.tariffComboBox.setInputPrompt(this.messages.get(this.VIEW_NAME + "tab.rawmaterial.cost.register.form.tariff.combo.input.prompt"));
        BeanItemContainer<TariffDTO> tariffDTOBeanItemContainer = new BeanItemContainer<TariffDTO>(TariffDTO.class);
        tariffDTOBeanItemContainer.addAll(this.productionManagementService.listTariffDTO( null));
        this.tariffComboBox.setContainerDataSource(tariffDTOBeanItemContainer);
        this.tariffComboBox.setItemCaptionPropertyId("tariff_id");
        
        if(this.rawMaterialCostFormTabEditMode){
        	this.tariffComboBox.select(this.rawMaterialCostDTO.getTariffDTO());
        	this.tariffComboBox.setValue(this.rawMaterialCostDTO.getTariffDTO());
        }
        this.tariffComboBox.setNullSelectionAllowed(false);
        this.tariffComboBox.addStyleName("small");
        this.tariffComboBox.addValueChangeListener(this.setUpValueChangeListenerFortariffComboBox());
        this.tariffComboBox.setRequired(true);
        this.tariffComboBox.setRequiredError(this.messages.get(this.VIEW_NAME + "tab.rawmaterial.cost.register.form.tariff.combo.required.message"));
        this.tariffComboBox.setEnabled(!this.rawMaterialCostFormTabEditMode);
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
				rawMaterialCostDTO.setId_tariff(((TariffDTO)event.getProperty().getValue()).getId());
			}
    	};
	}
	
	private void setUpRawMaterialCostFormTab() throws Exception{
    	
        final Button saveButton = new Button(
        !this.rawMaterialCostFormTabEditMode ? this.messages.get("application.common.register.cost.button.label")
        : this.messages.get("application.common.set.end.validity.date.button.label")
        		);/*"Guardar"*/
        saveButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		if(!rawMaterialCostFormTabEditMode){
		            			rawMaterialCostDTOForm.commit();
		            			tariffComboBox.validate();
		            			tariffComboBox.commit();
		            		}
		            		logger.info("\nrawMaterialCostDTO.toString():\n" + rawMaterialCostDTO.toString());
		            		saveButtonActionRawMaterialCostFormTab();
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
		            		rawMaterialCostDTOForm.discard();
		            		tariffComboBox.discard();
		            		logger.info("\n when click in cancel button; go to cost table tab"); 
		            		updateStatusRawMaterialCostFormTab();
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
		
		this.rawMaterialCostDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);
		this.rawMaterialCostFormContainer.addComponent(this.rawMaterialCostDTOForm.getSgpFormLayout());
        this.rawMaterialCostFormContainerTab = this.tabs.addTab(this.rawMaterialCostFormContainer, this.messages.get(this.VIEW_NAME + "tab.rawmaterial.cost.register.form"));
        
        this.rawMaterialCostFormContainerTab.setClosable(false);
        this.rawMaterialCostFormContainerTab.setEnabled(this.editFormMode && this.enableRawMaterialCostDTOForm);
        this.rawMaterialCostFormContainerTab.setVisible(
        		this.bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
        		.contains(this.SECURED_PROGRAM_PREFIX + this.VIEW_NAME + "tab.rawmaterial.cost.register.form"));
        //TestIcon testIcon = new TestIcon(60);
        //machineDataTab.setIcon(testIcon.get(false));
        this.rawMaterialCostFormContainerTab.setIcon(FontAwesome.MONEY);
	}
	
    private void saveButtonActionRawMaterialCostFormTab() throws PmsServiceException,Exception{
    	if(!this.rawMaterialCostFormTabEditMode)
    		this.productionManagementService.insertRawMaterialCostDTO(this.rawMaterialCostDTO);
    	else{
    		
    		this.productionManagementService.updateRawMaterialCostDTO(this.rawMaterialCostDTO);
    	}
    	this.editRawMaterialCost(null);
    	this.updateStatusRawMaterialCostFormTab();
		this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		
    }
    
    private void updateStatusRawMaterialCostFormTab() throws PmsServiceException{
    	this.setEnableRawMaterialCostDTOFormFlag();
    	this.tabs.setSelectedTab(1);
    	this.rawMaterialCostFormContainerTab.setEnabled(this.editFormMode && this.enableRawMaterialCostDTOForm);
    	this.tabs.markAsDirty();    	
    }
    
	 private void setEnableRawMaterialCostDTOFormFlag() throws PmsServiceException{
		 Long id = this.productionManagementService.getRawMaterialCostValidRowId(this.rawMaterialDTO.getId());
		 if(id != null)this.enableRawMaterialCostDTOForm = false; else this.enableRawMaterialCostDTOForm = true;
	 }
}
