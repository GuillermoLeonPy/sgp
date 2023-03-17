package py.com.kyron.sgp.gui.view.personmanagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.application.utils.VerifierDigitChecker;
import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityProgramDTO;
import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityRolDTO;
import py.com.kyron.sgp.bussines.applicationsecurity.service.ApplicationSecurityService;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.personmanagement.service.PersonManagementService;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.EditMonedaEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.PersonRegisterFormViewEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.RegisterCurrencyViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.comercialmanagement.RegisterCurrencyFormView;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import py.com.kyron.sgp.gui.component.TestIcon;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class PersonRegisterFormView extends VerticalLayout implements View {
	
	private final Logger logger = LoggerFactory.getLogger(PersonRegisterFormView.class);
	private SgpForm<PersonDTO> personForm;
	private SgpForm<PersonDTO> personFormCredentialsTab;
	//private Moneda moneda;
	private Map<String,String> messages;
	private final String VIEW_NAME = "person.register.form.";
	private boolean editFormMode;
	private PersonDTO personDTO;
	private PersonManagementService personManagementService;
	private ApplicationSecurityService applicationSecurityService;
	private TabSheet tabs;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private String CALLER_VIEW;
	private VerticalLayout mainViewLayout;
	private boolean massiveInsertMode;
	private boolean insertInSupplierMode;
	private boolean insertInCustomerMode;
	private boolean insertInFuntionaryMode;
	private OptionGroup selectRoleOptionGroup;
	private final String SUPPLIER_FLAG = "supplier";
	private final String CUSTOMER_FLAG = "customer";
	private final String FUNCTIONARY_FLAG = "functionary";
	private final String PERSON_FLAG = "person";
	private Table programsByRoleTable;
	private HorizontalLayout selectOneRoleHorizontalLayout;
	
	public PersonRegisterFormView() {
		// TODO Auto-generated constructor stub
		try {
			logger.info("\nPersonRegisterFormView..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();			
	        this.addStyleName("v-scrollable");
	        this.setHeight("100%");
	        
	        Responsive.makeResponsive(this);
			DashboardEventBus.register(this);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null));
			logger.error(e.getMessage(), e);
			addComponent(new Label(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null)));
		}
	}

	
	private void initMainViewLayout(){
		this.mainViewLayout = new VerticalLayout();
		this.mainViewLayout.setSpacing(true);
		this.mainViewLayout.setMargin(true);
	}
	
	private void removeAndReAddMainViewLayout(){
		this.removeAllComponents();
		this.addComponent(this.mainViewLayout);
	}
	
	private void initServices() throws Exception{
		this.personManagementService = (PersonManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PERSON_MANAGEMENT_SERVICE);
		this.applicationSecurityService = (ApplicationSecurityService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.APPLICATION_SECURITY_SERVICE);
	}//private void initServices() throws Exception
	
	public PersonRegisterFormView(Component... children) {
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
	
	private void restartFormAfterSuccesfulInsertOperation(){
		try{
			this.initMainViewLayout();
			this.editFormMode = false;
		    this.initTitles();
		    this.setUpMainTabForm(null);
		    this.setUpUserDataTab();
		    this.setUpCredentialsDataTab();
		    this.mainViewLayout.addComponent(tabs);
		    this.removeAndReAddMainViewLayout();
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	private String determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView() throws PmsServiceException{
		/*logger.info("\ndeterminePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView():"
				+"\nthis.insertInCustomerMode : "+this.insertInCustomerMode
				+"\nthis.insertInFuntionaryMode: "+this.insertInFuntionaryMode
				+"\nthis.insertInSupplierMode: "+this.insertInSupplierMode
				+"\nthis.editFormMode : "+this.editFormMode);*/
		
		if(!this.insertInCustomerMode && !this.insertInSupplierMode && !this.insertInFuntionaryMode)
			return this.PERSON_FLAG;
		else if(this.insertInCustomerMode && !this.insertInSupplierMode && !this.insertInFuntionaryMode)
			return this.CUSTOMER_FLAG;
		else if(!this.insertInCustomerMode && this.insertInSupplierMode && !this.insertInFuntionaryMode)
			return this.SUPPLIER_FLAG;
		else if(!this.insertInCustomerMode && !this.insertInSupplierMode && this.insertInFuntionaryMode)
			return this.FUNCTIONARY_FLAG;
		else{			
			throw new PmsServiceException("application.common.gui.exception.by.programmer", null, null);
		}
	}
	
    @Subscribe
    public void setPersonToEdit(final PersonRegisterFormViewEvent personRegisterFormViewEvent){
    	this.CALLER_VIEW = personRegisterFormViewEvent.getCallerView();
    	this.massiveInsertMode = personRegisterFormViewEvent.isMassiveInsertMode();
    	this.insertInCustomerMode = personRegisterFormViewEvent.isInsertInCustomerMode();
    	this.insertInSupplierMode = personRegisterFormViewEvent.isInsertInSupplierMode();
    	this.insertInFuntionaryMode = personRegisterFormViewEvent.isInsertInFuntionaryMode();
    	try{
    		this.initMainViewLayout();
		    logger.info("\n : " + personRegisterFormViewEvent.getPersonDTO() + "\nthis.CALLER_VIEW : " + this.CALLER_VIEW );
		    this.editFormMode = personRegisterFormViewEvent.getPersonDTO()!=null;
		    this.initTitles();
		    this.setUpMainTabForm(personRegisterFormViewEvent.getPersonDTO());
		    this.setUpUserDataTab();
		    this.setUpCredentialsDataTab();
		    this.mainViewLayout.addComponent(tabs);
		    this.removeAndReAddMainViewLayout();
		}catch(Exception e){
			logger.error("\nerror", e);
		}
    }//public void setMonedaToEdit(final EditMonedaEvent editMonedaEvent)
	
    private void setUpMainTabForm(PersonDTO personDTO){
    	if(personDTO!=null)
    		this.personDTO = personDTO;
    	else
    		this.personDTO = new PersonDTO(this.insertInSupplierMode, this.insertInCustomerMode, this.insertInFuntionaryMode);
    	this.printPersonDTO();
    	this.personForm = new SgpForm<PersonDTO>(PersonDTO.class, new BeanItem<PersonDTO>(this.personDTO), "light", true);
    	this.personForm.bindAndSetPositionFormLayoutTextField("personal_civil_id_document", this.messages.get(this.VIEW_NAME + "text.field.personal.civil.id.doc.label")/**/, true, 25, false,null, true);
    	//this.personForm.bindAndSetPositionFormLayoutTextField("ruc", this.messages.get(this.VIEW_NAME + "text.field.ruc.label")/**/, true, 25, false,null, true);
    	this.personForm.bindAndSetPositionFormLayoutTextField("ruc", this.messages.get(this.VIEW_NAME + "text.field.ruc.label")/**/, true, 25, false,null, true, null,this.setUpRucTextFieldBlurListener());
    	this.personForm.bindAndSetPositionFormLayoutTextField("commercial_name", this.messages.get(this.VIEW_NAME + "text.field.comercial.name.label")/**/, true, 100, false,null, true);
    	this.personForm.bindAndSetPositionFormLayoutTextField("personal_name", this.messages.get(this.VIEW_NAME + "text.field.personal.name.label")/**/, true, 100, false,null, true);
    	this.personForm.bindAndSetPositionFormLayoutTextField("personal_last_name", this.messages.get(this.VIEW_NAME + "text.field.personal.last.name.label")/**/, true, 100, false,null, true);
    	this.personForm.bindAndSetPositionFormLayoutTextField("personal_telephone_number", this.messages.get(this.VIEW_NAME + "text.field.personal.telephone.label")/**/, true, 100, false,null, true);
    	this.personForm.bindAndSetPositionFormLayoutTextField("personal_email_address", this.messages.get(this.VIEW_NAME + "text.field.personal.email.label")/**/, true, 100, false,null, true);
    	this.personForm.bindAndSetPositionFormLayoutTextField("personal_address", this.messages.get(this.VIEW_NAME + "text.field.personal.address.label")/**/, true, 100, false,null, true);
    	
    	try{
    		if(this.determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView().equalsIgnoreCase(this.CUSTOMER_FLAG))
    			this.personForm.bindAndSetPositionFormLayoutCheckBox("is_customer", this.messages.get(this.VIEW_NAME + "check.box.customer.label")/*"*/, "",/*false*/this.personDTO.getIs_customer() == null && !this.editFormMode ? true: this.personDTO.getIs_customer(), false);
    		if(this.determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView().equalsIgnoreCase(this.SUPPLIER_FLAG))
    			this.personForm.bindAndSetPositionFormLayoutCheckBox("is_supplier", this.messages.get(this.VIEW_NAME + "check.box.supplier.label")/**/, "",/*false*/this.personDTO.getIs_supplier() == null  && !this.editFormMode ? true: this.personDTO.getIs_supplier(), false);
    		if(this.determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView().equalsIgnoreCase(this.FUNCTIONARY_FLAG))
    			this.personForm.bindAndSetPositionFormLayoutCheckBox("is_functionary", this.messages.get(this.VIEW_NAME + "check.box.functionary.label")/**/, "",/*false*/this.personDTO.getIs_functionary() == null  && !this.editFormMode ? true: this.personDTO.getIs_functionary(), false);
    		if(this.determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView().equalsIgnoreCase(this.PERSON_FLAG)){
    			this.personForm.bindAndSetPositionFormLayoutCheckBox("is_customer", this.messages.get(this.VIEW_NAME + "check.box.customer.label")/*"*/, "",/*false*/this.personDTO.getIs_customer() == null ? false: this.personDTO.getIs_customer(), true);
    			this.personForm.bindAndSetPositionFormLayoutCheckBox("is_supplier", this.messages.get(this.VIEW_NAME + "check.box.supplier.label")/**/, "",/*false*/this.personDTO.getIs_supplier() == null ? false: this.personDTO.getIs_supplier(), true);
    			this.personForm.bindAndSetPositionFormLayoutCheckBox("is_functionary", this.messages.get(this.VIEW_NAME + "check.box.functionary.label")/**/, "",/*false*/this.personDTO.getIs_functionary() == null ? false: this.personDTO.getIs_functionary(), true);
    		}
    		
		}catch(Exception e){
			commonExceptionErrorNotification.showErrorMessageNotification(e);
		}
    	/**/
    }//private void setUpMainTabForm(PersonDTO personDTO)
    
	private BlurListener setUpRucTextFieldBlurListener(){
		return new BlurListener(){
			@Override
			public void blur(BlurEvent event) {
				// TODO Auto-generated method stub
				try{
					logger.info("\n************************"
							+"\n blur listener in ruc text field"
							+ "\n object class type: "+event.getSource().getClass());
					final TextField textField = (TextField)event.getSource();
					if(!textField.isEmpty()){
						//textField.commit();
						final String textFieldValue = textField.getValue();
						logger.info( "\n ======================= "
									+"\n entered ruc: " + textFieldValue/*personDTO.getRuc()*/
									+"\n ======================= ");
						try{
							validateRuc(textFieldValue);
						}catch(Exception e){					
							commonExceptionErrorNotification.showErrorMessageNotification(e);
							textField.discard();							
							textField.focus();
							textField.setValue(textFieldValue);
						}
					}else{
						textField.discard();
						textField.setIcon(null);
						personDTO.setRuc(null);						
					}
				}catch(Exception e){					
					commonExceptionErrorNotification.showErrorMessageNotification(e);
				}
				
			}			
		};
	}
	
	private void validateRuc(final String ruc) throws PmsServiceException{
		//check the presence of '-' character
		if(!ruc.contains("-"))throw new PmsServiceException("person.register.form.text.field.ruc.invalid.format.error", null, null);
		this.validateRucVerifierDigit(ruc.substring(0, ruc.length() - 2), ruc.substring(ruc.length() - 1));
	}
	
	private void validateRucVerifierDigit(final String ruc, final String vd) throws PmsServiceException{
		if(!vd.equals(VerifierDigitChecker.getDigitoVerificador(ruc)))throw new PmsServiceException("person.register.form.text.field.ruc.invalid.verifier.digit.error", null, null);
	}
    
    private void printPersonDTO(){
    	logger.info("\n*************\n" + this.personDTO + "\n*************"
    			+"\nthis.personDTO.getApplicationSecurityRolDTO() : " + this.personDTO.getApplicationSecurityRolDTO());
    }
    
    private void setUpUserDataTab(){
    	this.tabs = new TabSheet();
    	
    	this.tabs.addStyleName("framed");
    	VerticalLayout personalDataContent = new VerticalLayout();
    	personalDataContent.setMargin(true);
    	personalDataContent.setSpacing(true);

        final Button saveButton = new Button(this.messages.get("application.common.button.save.label"));/*"Guardar"*/
        saveButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{            		
		            		personForm.commit();
		            		logger.info("\npersonDTO.toString():\n" + personDTO.toString());
		            		saveButtonAction();
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
		            		personForm.discard();
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
		
		this.personForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);
    	personalDataContent.addComponent(this.personForm.getSgpFormLayout());    	
        Tab personalDataTab = this.tabs.addTab(personalDataContent, this.messages.get(this.VIEW_NAME + "tab.personal.data"));
        
        personalDataTab.setClosable(false);
        personalDataTab.setEnabled(true);
        //TestIcon testIcon = new TestIcon(60);
        //personalDataTab.setIcon(testIcon.get(false));
        personalDataTab.setIcon(FontAwesome.USERS);
        /**/
    }//private void setUpUserDataTab(){
    
    
    private void saveButtonAction() throws PmsServiceException{
    	if(!this.editFormMode)
    		this.personManagementService.registerPerson(this.personDTO);
    	else{
    		logger.info("\nhere call update method, then return to caller view, because its a fast edit");
    		this.personManagementService.updatePerson(this.personDTO);
    		navigateToCallerView();
    	}
    	
		commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		
		if(!this.editFormMode && this.massiveInsertMode) 
			this.restartFormAfterSuccesfulInsertOperation();    	
    }
    
    private void setUpCredentialsDataTab() throws PmsServiceException{
        VerticalLayout userCredentialsContent = new VerticalLayout();
        //userCredentialsContent.addComponent(new Label("aqui credenciales de usuario"));
        userCredentialsContent.setMargin(true);
        userCredentialsContent.setSpacing(true);
        this.personFormCredentialsTab = new SgpForm<PersonDTO>(PersonDTO.class, new BeanItem<PersonDTO>(this.personDTO), "light", true);
        this.personFormCredentialsTab.bindAndSetPositionFormLayoutTextField("application_user_name", this.messages.get(this.VIEW_NAME + "tab.user.credentials.data.text.field.username.label")/**/, true, 25, false,null, true);
        this.personFormCredentialsTab.bindAndSetPositionFormLayoutTextField("application_password", this.messages.get(this.VIEW_NAME + "tab.user.credentials.data.text.field.password.label")/**/, true, 25, false,null, true);
        
        //select one
        this.selectOneRoleHorizontalLayout = this.buildSelectOneRole();
        
        //this.personFormCredentialsTab.getSgpFormLayout().addComponent(this.buildSelectOneRole());
        this.prepareProgramsByRoleTableToCredentialTabs();
        this.selectOneRoleHorizontalLayout.addComponent(this.programsByRoleTable);
        //this.personFormCredentialsTab.getSgpFormLayout().addComponent(this.programsByRoleTable);
        this.personFormCredentialsTab.getSgpFormLayout().addComponent(this.selectOneRoleHorizontalLayout);
        this.personFormCredentialsTab.getSgpFormLayout().addComponent(this.buildCredentialsOkCancelButtons());
        userCredentialsContent.addComponent(this.personFormCredentialsTab.getSgpFormLayout());
        
        
        Tab userCredentialsTab = this.tabs.addTab(userCredentialsContent, this.messages.get(this.VIEW_NAME + "tab.user.credentials.data"));
        userCredentialsTab.setClosable(false);
        userCredentialsTab.setEnabled(true);
        userCredentialsTab.setVisible(this.determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView().equalsIgnoreCase(this.PERSON_FLAG));
        //TestIcon testIcon = new TestIcon(60);
        //userCredentialsTab.setIcon(testIcon.get(false));
        userCredentialsTab.setIcon(FontAwesome.USER_SECRET);
        
    }//private void setUpCredentialsDataTab(){
    
    
    private HorizontalLayout buildSelectOneRole() throws PmsServiceException{
    	
    	this.selectRoleOptionGroup = new OptionGroup(this.messages.get(this.VIEW_NAME + "tab.user.credentials.data.selectone.role.label"));//
    	this.selectRoleOptionGroup.addStyleName("small");
    	this.selectRoleOptionGroup.setMultiSelect(false);
    	
    	BeanItemContainer<ApplicationSecurityRolDTO> applicationSecurityRolDTOBeanItemContainer	= new BeanItemContainer<ApplicationSecurityRolDTO>(ApplicationSecurityRolDTO.class);
    	applicationSecurityRolDTOBeanItemContainer.addAll(this.applicationSecurityService.listApplicationSecurityRolDTO(null));
    	this.selectRoleOptionGroup.setContainerDataSource(applicationSecurityRolDTOBeanItemContainer);
    	this.selectRoleOptionGroup.setItemCaptionPropertyId("role_name");
    	
    	if(this.personDTO.getApplicationSecurityRolDTO()!=null){
    		this.selectRoleOptionGroup.select(this.personDTO.getApplicationSecurityRolDTO());
    	}
    	this.selectRoleOptionGroup.setNullSelectionAllowed(false);
    	this.selectRoleOptionGroup.addValueChangeListener(this.setUpValueChangeListenerForSelectRoleOptionGroup());
    	
		HorizontalLayout selectOneRoleHorizontalLayout = new HorizontalLayout();
		selectOneRoleHorizontalLayout.setMargin(new MarginInfo(true, false, true, false));
		selectOneRoleHorizontalLayout.setSpacing(true);
		selectOneRoleHorizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		selectOneRoleHorizontalLayout.addComponent(this.selectRoleOptionGroup);
		return selectOneRoleHorizontalLayout;
    }
    
    @SuppressWarnings("unchecked")
	private List<ApplicationSecurityProgramDTO> getRoleProgramsByRoleId(Long id) throws PmsServiceException{
    	//List<ApplicationSecurityRolDTO> listApplicationSecurityRolDTO = this.applicationSecurityService.listApplicationSecurityRolDTO(null);
    	//Collection<ApplicationSecurityRolDTO> colApplicationSecurityRolDTO = (Collection<ApplicationSecurityRolDTO>)this.selectRoleOptionGroup.getContainerDataSource();
    	BeanItemContainer<ApplicationSecurityRolDTO> applicationSecurityRolDTOBeanItemContainer	=
    	(BeanItemContainer<ApplicationSecurityRolDTO>)this.selectRoleOptionGroup.getContainerDataSource();
    	List<ApplicationSecurityRolDTO> listApplicationSecurityRolDTO = applicationSecurityRolDTOBeanItemContainer.getItemIds();
    	ApplicationSecurityRolDTO toReturnvApplicationSecurityRolDTO = null;
    	for(ApplicationSecurityRolDTO vApplicationSecurityRolDTO : listApplicationSecurityRolDTO){
    		if(vApplicationSecurityRolDTO.getId().equals(id)){
    			toReturnvApplicationSecurityRolDTO = vApplicationSecurityRolDTO;
    			break;
    		}
    	}
    	return toReturnvApplicationSecurityRolDTO.getRoleProgramList();
    }
    
    private void prepareProgramsByRoleTableToCredentialTabs() throws PmsServiceException{
    	this.programsByRoleTable = new Table();
    	BeanItemContainer<ApplicationSecurityProgramDTO> applicationSecurityProgramDTOBeanItemContainer	= new BeanItemContainer<ApplicationSecurityProgramDTO>(ApplicationSecurityProgramDTO.class);
    	ApplicationSecurityRolDTO vApplicationSecurityRolDTO = (ApplicationSecurityRolDTO)this.selectRoleOptionGroup.getValue();
    	if(vApplicationSecurityRolDTO!=null)
    		applicationSecurityProgramDTOBeanItemContainer.addAll(this.getRoleProgramsByRoleId(vApplicationSecurityRolDTO.getId())/*vApplicationSecurityRolDTO.getRoleProgramList()*/);
    	else
    		applicationSecurityProgramDTOBeanItemContainer.addAll(new ArrayList<ApplicationSecurityProgramDTO>());
    	this.programsByRoleTable.setContainerDataSource(applicationSecurityProgramDTOBeanItemContainer);
    	this.programsByRoleTable.setVisibleColumns(new Object[] {"program_key_value"});
    	this.programsByRoleTable.setColumnHeader("program_key_value", this.messages.get(this.VIEW_NAME + "tab.user.credentials.data.programs.by.role.table.column.label"));
    	this.programsByRoleTable.setColumnAlignment("program_key_value", Table.Align.LEFT);
    	this.programsByRoleTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.programsByRoleTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	this.programsByRoleTable.addStyleName(ValoTheme.TABLE_SMALL);
    	this.programsByRoleTable.setSelectable(true);
    	this.programsByRoleTable.setSortContainerPropertyId("program_key_value");
    	this.programsByRoleTable.setSortAscending(true);
    	this.programsByRoleTable.setFooterVisible(true);
    	this.programsByRoleTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
    	this.programsByRoleTable.setPageLength(6);
    	this.programsByRoleTable.setSizeFull();
    	this.programsByRoleTable.setHeight(200f /*+ (applicationSecurityProgramDTOBeanItemContainer.size() * 5)*/, Unit.PIXELS);
    	//this.programsByRoleTable.setColumnExpandRatio("program_key_value", 0.1f);
    	
    	float widthSize = this.programsByRoleTable.getWidth();
    	Unit widthUnit = this.programsByRoleTable.getWidthUnits();
    	logger.info( "\n ========================="
    				+"\n programs role table size"
    				+"\n widthUnit: "+widthUnit
    				+"\n widthSize: "+widthSize
    				+"\n =========================");
    	/*if(widthUnit == Unit.PIXELS && widthSize < 100f)
    		this.programsByRoleTable.setWidth(100f, Unit.PIXELS);*/
    	this.programsByRoleTable.setWidth(600f, Unit.PIXELS);
    }
    
    private Property.ValueChangeListener setUpValueChangeListenerForSelectRoleOptionGroup(){
    	return new Property.ValueChangeListener() {            
			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				logger.info("\nsetUpValueChangeListenerForSelectRoleOptionGroup\n");
				logger.info("\nevent.getProperty().getType().getName()) : " + event.getProperty().getType().getName());
                if (event.getProperty().getValue() != null){
                	logger.info("\nevent.getProperty().getValue().getClass().getName() : " + event.getProperty().getValue().getClass().getName());
                	ApplicationSecurityRolDTO applicationSecurityRolDTO = (ApplicationSecurityRolDTO)event.getProperty().getValue();
               		logger.info("\napplicationSecurityRolDTO\n : " + applicationSecurityRolDTO);
               		logger.info("\n********\nupdate table content\n********");
               		selectOneRoleHorizontalLayout.removeComponent(programsByRoleTable);
               		try {
						prepareProgramsByRoleTableToCredentialTabs();
						selectOneRoleHorizontalLayout.addComponent(programsByRoleTable);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("\nerror:",e);
					}
               		/*BeanItemContainer<ApplicationSecurityProgramDTO> applicationSecurityProgramDTOBeanItemContainer	=
               		(BeanItemContainer<ApplicationSecurityProgramDTO>)programsByRoleTable.getContainerDataSource();
               		applicationSecurityProgramDTOBeanItemContainer.removeAllItems();
               		applicationSecurityProgramDTOBeanItemContainer.addAll(applicationSecurityRolDTO.getRoleProgramList());
               		programsByRoleTable.markAsDirty();*/
               		
                }//if (event.getProperty().getValue() != null){
			}//public void valueChange(ValueChangeEvent event)
        };//return new Property.ValueChangeListener() 
    }
    
    private void applySelectedRoleToPerson(){
    	this.personDTO.setApplicationSecurityRolDTO(
    			(ApplicationSecurityRolDTO)this.selectRoleOptionGroup.getValue()
    			);
    }
    
    private HorizontalLayout buildCredentialsOkCancelButtons(){
        final Button saveButton = new Button(this.messages.get("application.common.button.save.label"));/*"Guardar"*/
        saveButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{            		
		            		//personForm.commit();
		            		personFormCredentialsTab.commit();
		            		selectRoleOptionGroup.commit();
		            		applySelectedRoleToPerson();
		            		logger.info("\npersonDTO.toString():\n" + personDTO.toString());
		            		//saveButtonAction();
		            	}catch(Exception e){		
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);
		            		return;
		            	}
		            	
		            	try{
		            		personForm.commit();
		            		//logger.info("\napplicationSecurityRolDTO.toString():\n" + applicationSecurityRolDTO.toString());
		            		saveButtonAction();
		            	}catch(Exception e){		
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);
		            		tabs.setSelectedTab(0);
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
		            		personForm.discard();
		            		personFormCredentialsTab.discard();
		            		selectRoleOptionGroup.discard();
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
    
	private void initTitles(){

        /*Label title = new Label
        (!this.editFormMode ? this.messages.get(this.VIEW_NAME + "main.title.register") : this.messages.get(this.VIEW_NAME + "main.title.edit"));*/
		try{
			Label title = new Label(this.determineMainTittleLabel());
        	title.addStyleName("h1");
        	this.mainViewLayout.addComponent(title);
		}catch(Exception e){
			commonExceptionErrorNotification.showErrorMessageNotification(e);
		}
	}//private void initTitles()
	
	private void navigateToCallerView(){
		logger.info("\nPersonRegisterFormView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW);
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new PersonRegisterFormViewEvent(this.personDTO, this.CALLER_VIEW, false, /*always return false from this view*/false, false, false));
			//DashboardEventBus.post(new EditMonedaEvent(moneda));
		}catch(Exception e){
			logger.error(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null));
			logger.error(e.getMessage(), e);
		}
	}
	
	private String determineMainTittleLabel() throws PmsServiceException{
		if((this.determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView().equalsIgnoreCase(this.PERSON_FLAG)) && !this.editFormMode)
			return this.messages.get(this.VIEW_NAME + "main.title.register");
		else if((this.determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView().equalsIgnoreCase(this.PERSON_FLAG)) && this.editFormMode)
			return this.messages.get(this.VIEW_NAME + "main.title.edit");
		else if((this.determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView().equalsIgnoreCase(this.CUSTOMER_FLAG)) && !this.editFormMode)
			return this.messages.get(this.VIEW_NAME + "main.title.register.customer");
		else if((this.determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView().equalsIgnoreCase(this.CUSTOMER_FLAG)) && this.editFormMode)
			return this.messages.get(this.VIEW_NAME + "main.title.edit.customer");
		else if((this.determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView().equalsIgnoreCase(this.SUPPLIER_FLAG)) && !this.editFormMode)
			return this.messages.get(this.VIEW_NAME + "main.title.register.supplier");
		else if((this.determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView().equalsIgnoreCase(this.SUPPLIER_FLAG)) && this.editFormMode)
			return this.messages.get(this.VIEW_NAME + "main.title.edit.supplier");
		else if((this.determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView().equalsIgnoreCase(this.FUNCTIONARY_FLAG)) && !this.editFormMode)
			return this.messages.get(this.VIEW_NAME + "main.title.register.functionary");
		else if((this.determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView().equalsIgnoreCase(this.FUNCTIONARY_FLAG)) && this.editFormMode)
			return this.messages.get(this.VIEW_NAME + "main.title.edit.functionary");
		else
			throw new PmsServiceException("application.common.gui.exception.by.programmer", null, null);
			
	}
}
