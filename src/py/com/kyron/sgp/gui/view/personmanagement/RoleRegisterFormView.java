package py.com.kyron.sgp.gui.view.personmanagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityProgramDTO;
import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityRolDTO;
import py.com.kyron.sgp.bussines.applicationsecurity.service.ApplicationSecurityService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.service.PersonManagementService;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.component.TestIcon;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.PersonRegisterFormViewEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.RoleAdministrationViewEvent;
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
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;

@SuppressWarnings("serial")
public class RoleRegisterFormView extends VerticalLayout implements View {

	private final Logger logger = LoggerFactory.getLogger(RoleRegisterFormView.class);
	private SgpForm<ApplicationSecurityRolDTO> applicationSecurityRolDTOForm;
	private Map<String,String> messages;
	private final String VIEW_NAME = "role.register.form.";
	private boolean editFormMode;
	private ApplicationSecurityRolDTO applicationSecurityRolDTO;
	private ApplicationSecurityService applicationSecurityService;
	private TabSheet tabs;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private VerticalLayout mainViewLayout;
	private boolean massiveInsertMode;
	private String CALLER_VIEW;
	private TwinColSelect tcs;
	
	public RoleRegisterFormView() {
		// TODO Auto-generated constructor stub
		try {
			logger.info("\nRoleRegisterFormView..");
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
	
	private void initServices() throws Exception{
		this.applicationSecurityService = (ApplicationSecurityService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.APPLICATION_SECURITY_SERVICE);
	}//private void initServices() throws Exception

	public RoleRegisterFormView(Component... children) {
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
	public void setRoleToEdit(final RoleAdministrationViewEvent roleAdministrationViewEvent){
		try{
			this.CALLER_VIEW = roleAdministrationViewEvent.getCallerView();
			this.massiveInsertMode = roleAdministrationViewEvent.isMassiveInsertMode();
			this.editFormMode = roleAdministrationViewEvent.getApplicationSecurityRolDTO()!=null;
			this.initMainViewLayout();
			this.initTitles();
			this.setUpMainTabForm(roleAdministrationViewEvent.getApplicationSecurityRolDTO());
			this.setUpRoleDataTab();
		    this.setUpRoleProgramsDataTab();
		    this.mainViewLayout.addComponent(tabs);
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
		//this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
	}
	
	private void initTitles(){

        /*Label title = new Label
        (!this.editFormMode ? this.messages.get(this.VIEW_NAME + "main.title.register") : this.messages.get(this.VIEW_NAME + "main.title.edit"));*/
		try{
			Label title = new Label(!this.editFormMode ? this.messages.get(this.VIEW_NAME + "main.title.register") : this.messages.get(this.VIEW_NAME + "main.title.edit"));
        	title.addStyleName("h1");
        	this.mainViewLayout.addComponent(title);
		}catch(Exception e){
			commonExceptionErrorNotification.showErrorMessageNotification(e);
		}
	}
	
	private void initMainViewLayout(){
		this.mainViewLayout = new VerticalLayout();
		this.mainViewLayout.setSpacing(true);
		this.mainViewLayout.setMargin(true);
	}
	
	private void setUpMainTabForm(ApplicationSecurityRolDTO applicationSecurityRolDTO){
		this.applicationSecurityRolDTO = (applicationSecurityRolDTO == null ? new ApplicationSecurityRolDTO(true) : applicationSecurityRolDTO);
		if(this.editFormMode)this.printApplicationSecurityRolDTOToEdit();
		this.applicationSecurityRolDTOForm = new SgpForm<ApplicationSecurityRolDTO>(ApplicationSecurityRolDTO.class, new BeanItem<ApplicationSecurityRolDTO>(this.applicationSecurityRolDTO), "light", true);
		this.applicationSecurityRolDTOForm.bindAndSetPositionFormLayoutTextField("role_name", this.messages.get(this.VIEW_NAME + "text.field.role.name.label")/**/, true, 50, true,this.messages.get(this.VIEW_NAME + "text.field.role.name.required.message"), this.applicationSecurityRolDTO.getIs_editable());
		this.applicationSecurityRolDTOForm.bindAndSetPositionFormLayoutTextField("role_description", this.messages.get(this.VIEW_NAME + "text.field.role.description.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "text.field.role.description.required.message"), this.applicationSecurityRolDTO.getIs_editable());
	}
	
	private void printApplicationSecurityRolDTOToEdit(){
		logger.info("\n**********\nApplicationSecurityRolDTOToEdit\n**********\nthis.applicationSecurityRolDTO.toString() : \n" + this.applicationSecurityRolDTO.toString());
		if(this.applicationSecurityRolDTO.getRoleProgramList()!=null)
			for(ApplicationSecurityProgramDTO v : this.applicationSecurityRolDTO.getRoleProgramList()){
				logger.info("\n" + v);
			}
	}
	
	private void setUpRoleDataTab(){
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
		            		applicationSecurityRolDTOForm.commit();
		            		logger.info("\napplicationSecurityRolDTO.toString():\n" + applicationSecurityRolDTO.toString());
		            		saveButtonAction();
		            	}catch(Exception e){		
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);
		            	}
		            }
		        }
			);
        
        saveButton.setEnabled(this.applicationSecurityRolDTO.getIs_editable());
        
		final Button cancelButton = new Button(this.messages.get("application.common.button.cancel.label"));/*"Cancelar"*/
		cancelButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		applicationSecurityRolDTOForm.discard();
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
		
		this.applicationSecurityRolDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);
    	personalDataContent.addComponent(this.applicationSecurityRolDTOForm.getSgpFormLayout());    	
        Tab personalDataTab = this.tabs.addTab(personalDataContent, this.messages.get(this.VIEW_NAME + "tab.role.data"));
        
        personalDataTab.setClosable(false);
        personalDataTab.setEnabled(true);
        //TestIcon testIcon = new TestIcon(60);
        //personalDataTab.setIcon(testIcon.get(false));
        personalDataTab.setIcon(FontAwesome.CERTIFICATE);
	}
	
    private void saveButtonAction() throws PmsServiceException{
    	if(!this.editFormMode)
    		this.applicationSecurityService.insertApplicationSecurityRolDTO(this.applicationSecurityRolDTO);
    	else{
    		logger.info("\nhere call update method, then return to caller view, because its a fast edit");
    		this.applicationSecurityService.updateApplicationSecurityRolDTO(this.applicationSecurityRolDTO);
    		navigateToCallerView();
    	}
    	
		commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		
		if(!this.editFormMode && this.massiveInsertMode) 
			this.restartFormAfterSuccesfulInsertOperation();    	
    }
    
	private void restartFormAfterSuccesfulInsertOperation(){
		this.initMainViewLayout();
		this.editFormMode = false;
	    this.initTitles();
	    this.setUpMainTabForm(null);
	    this.setUpRoleDataTab();
	    this.setUpRoleProgramsDataTab();
	    this.mainViewLayout.addComponent(tabs);
	    this.removeAndReAddMainViewLayout();
	}
	
    private void setUpRoleProgramsDataTab() {
    	try{
	        VerticalLayout userCredentialsContent = new VerticalLayout();
	        userCredentialsContent.setMargin(true);
	        userCredentialsContent.setSpacing(true);
	        
	        HorizontalLayout twinColSelectHorizontalLayout = new HorizontalLayout();
	        twinColSelectHorizontalLayout.setSpacing(true);
	        twinColSelectHorizontalLayout.addStyleName("wrapping");
	        twinColSelectHorizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
	        twinColSelectHorizontalLayout.setSizeFull();
	        //userCredentialsContent.addComponent(new Label("aqui los programas del rol"));
	        this.setUpTwinColSelect();
	        twinColSelectHorizontalLayout.addComponent(this.tcs);
	        
	        userCredentialsContent.addComponent(twinColSelectHorizontalLayout);
	        userCredentialsContent.setSizeFull();
	        userCredentialsContent.addComponent(this.prepareOkCancelButtonForRoleProgramsDataTab());
	        Tab userCredentialsTab = this.tabs.addTab(userCredentialsContent, this.messages.get(this.VIEW_NAME + "tab.role.programs"));
	        userCredentialsTab.setClosable(false);
	        userCredentialsTab.setEnabled(true);
	        //TestIcon testIcon = new TestIcon(60);
	        //userCredentialsTab.setIcon(testIcon.get(false));
	        userCredentialsTab.setIcon(FontAwesome.PUZZLE_PIECE);
	        //userCredentialsContent.setSizeFull();
		}catch(Exception e){
			logger.error("\nerror", e);
		}
    }//private void setUpCredentialsDataTab(){
    
    private void setUpTwinColSelect() throws PmsServiceException{
        this.tcs = new TwinColSelect(this.messages.get(this.VIEW_NAME + "tab.role.programs.twin.col.select.title"));
        this.tcs.setLeftColumnCaption(this.messages.get(this.VIEW_NAME + "tab.role.programs.twin.col.select.right.col"));
        this.tcs.setRightColumnCaption(this.messages.get(this.VIEW_NAME + "tab.role.programs.twin.col.select.left.col"));
        this.tcs.setNewItemsAllowed(false);
        
        BeanItemContainer<ApplicationSecurityProgramDTO> applicationSecurityProgramDTOBeanItemContainer	= new BeanItemContainer<ApplicationSecurityProgramDTO>(ApplicationSecurityProgramDTO.class);
        applicationSecurityProgramDTOBeanItemContainer.addAll(this.applicationSecurityService.listApplicationSecurityProgramDTO(null));
        this.tcs.setContainerDataSource(applicationSecurityProgramDTOBeanItemContainer);
        this.tcs.setItemCaptionPropertyId("program_key_value");
        
        if(this.applicationSecurityRolDTO.getRoleProgramList()==null) this.applicationSecurityRolDTO.setRoleProgramList(new ArrayList<ApplicationSecurityProgramDTO>());
       /* BeanItemContainer<ApplicationSecurityProgramDTO> preSelectedApplicationSecurityProgramDTOBeanItemContainer	= new BeanItemContainer<ApplicationSecurityProgramDTO>(ApplicationSecurityProgramDTO.class);	        
        preSelectedApplicationSecurityProgramDTOBeanItemContainer.addAll(this.applicationSecurityRolDTO.getRoleProgramList());
        tcs.setValue(preSelectedApplicationSecurityProgramDTOBeanItemContainer);*/
        for(ApplicationSecurityProgramDTO var : this.applicationSecurityRolDTO.getRoleProgramList()){
        	logger.info("\n selecting item: \n" + var);
        	tcs.select(var);
        }
        
        this.tcs.setMultiSelect(true);
        this.tcs.setNullSelectionAllowed(false);
        //this.tcs.unselect(itemId);
        this.tcs.addValueChangeListener(this.setUpValueChangeListenerForTwinColSelect());
        this.tcs.setRequired(true);
        this.tcs.setEnabled(this.applicationSecurityRolDTO.getIs_editable());
        this.tcs.setRequiredError(this.messages.get(this.VIEW_NAME + "tab.role.programs.twin.col.select.required.message"));
        //this.tcs.addStyleName("v-select-twincol");
        this.tcs.setPrimaryStyleName(".dashboard .v-select-twincol");
        this.tcs.setSizeFull();
    }
    
    private Property.ValueChangeListener setUpValueChangeListenerForTwinColSelect(){
    	return new Property.ValueChangeListener() {            
			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				logger.info("\n\n");
				logger.info("\nevent.getProperty().getType().getName()) : " + event.getProperty().getType().getName());
                if (event.getProperty().getValue() != null){
                	logger.info("\nevent.getProperty().getValue().getClass().getName() : " + event.getProperty().getValue().getClass().getName());
                	Collection<ApplicationSecurityProgramDTO> listApplicationSecurityProgramDTO = (Collection<ApplicationSecurityProgramDTO>)event.getProperty().getValue();
                	if(listApplicationSecurityProgramDTO!=null){
                    	for(ApplicationSecurityProgramDTO vApplicationSecurityProgramDTO : listApplicationSecurityProgramDTO){
                    		logger.info("\nvApplicationSecurityProgramDTO : " + vApplicationSecurityProgramDTO);
                    	}
                    	
                	}
                	
                }//if (event.getProperty().getValue() != null){
			}//public void valueChange(ValueChangeEvent event)
        };//return new Property.ValueChangeListener() 
    }//private Property.ValueChangeListener setUpValueChangeListenerForTwinColSelect(){
    
    private HorizontalLayout prepareOkCancelButtonForRoleProgramsDataTab(){
        final Button saveButton = new Button(this.messages.get("application.common.button.save.label"));/*"Guardar"*/
        saveButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{            		
		            		tcs.commit();
		            		checkSelectedPrograms();
		            		logger.info("\napplicationSecurityRolDTO.toString():\n" + applicationSecurityRolDTO.toString());
		            		//saveButtonAction();
		            	}catch(Exception e){		
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);
		            		return;
		            	}
		            	try{
		            		applicationSecurityRolDTOForm.commit();
		            		//logger.info("\napplicationSecurityRolDTO.toString():\n" + applicationSecurityRolDTO.toString());
		            		saveButtonAction();
		            	}catch(Exception e){		
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);
		            		tabs.setSelectedTab(0);
		            	}
		            }
		        }
			);
        
        saveButton.setEnabled(this.applicationSecurityRolDTO.getIs_editable());
        
		final Button cancelButton = new Button(this.messages.get("application.common.button.cancel.label"));/*"Cancelar"*/
		cancelButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		tcs.discard();
		            		navigateToCallerView();
		            		//setUpTwinColSelect();
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
    
    
    @SuppressWarnings("unchecked")
	private void checkSelectedPrograms() throws PmsServiceException{
    	logger.info("\ncheckSelectedPrograms() ");
    	for(ApplicationSecurityProgramDTO vApplicationSecurityProgramDTO : this.applicationSecurityRolDTO.getRoleProgramList()){
    		logger.info("\nvApplicationSecurityProgramDTO : " + vApplicationSecurityProgramDTO);
    	}
    	Collection<ApplicationSecurityProgramDTO> selectedValuesListApplicationSecurityProgramDTO = (Collection<ApplicationSecurityProgramDTO>)this.tcs.getValue();
    	if(selectedValuesListApplicationSecurityProgramDTO!=null){
        	for(ApplicationSecurityProgramDTO vApplicationSecurityProgramDTO : selectedValuesListApplicationSecurityProgramDTO){
        		logger.info("\nvApplicationSecurityProgramDTO : " + vApplicationSecurityProgramDTO);
        		if(!this.applicationSecurityRolDTO.getRoleProgramList().contains(vApplicationSecurityProgramDTO)){
        			this.applicationSecurityRolDTO.getRoleProgramList().add(vApplicationSecurityProgramDTO);
        		}
        	}
        	Iterator<ApplicationSecurityProgramDTO> iterator = this.applicationSecurityRolDTO.getRoleProgramList().iterator();
        	while(iterator.hasNext()){
        		if(!selectedValuesListApplicationSecurityProgramDTO.contains(iterator.next())){
        			iterator.remove();
        		}
        	}
        	//this.applicationSecurityRolDTO.getRoleProgramList().addAll(listApplicationSecurityProgramDTO);
    	}else//
    		throw new PmsServiceException(this.messages.get(this.VIEW_NAME + "tab.role.programs.twin.col.select.required.message"), null, null);
    }//private void checkSelectedPrograms(){
    
	private void removeAndReAddMainViewLayout(){
		this.removeAllComponents();
		this.addComponent(this.mainViewLayout);
	}
	
	private void navigateToCallerView(){
		logger.info("\nRoleRegisterFormView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW);
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new RoleAdministrationViewEvent(this.applicationSecurityRolDTO, this.CALLER_VIEW, false /*always return false from this view*/));
			//DashboardEventBus.post(new EditMonedaEvent(moneda));
		}catch(Exception e){
			logger.error(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null));
			logger.error(e.getMessage(), e);
		}
	}
}
