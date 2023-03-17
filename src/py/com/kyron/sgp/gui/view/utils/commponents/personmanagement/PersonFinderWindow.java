package py.com.kyron.sgp.gui.view.utils.commponents.personmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.personmanagement.service.PersonManagementService;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class PersonFinderWindow extends Window {

	private final Logger logger = LoggerFactory.getLogger(PersonFinderWindow.class);
	private final String VIEW_NAME = "person.finder.window.";
	private Map<String,String> messages;
	private final boolean customerFindMode;
	private final boolean supplierFindMode;
	private final boolean functionaryFindMode;
	private final String SUPPLIER_FLAG = "supplier";
	private final String CUSTOMER_FLAG = "customer";
	private final String FUNCTIONARY_FLAG = "functionary";
	private final String PERSON_FLAG = "person";
	private VerticalLayout mainViewLayout;
	private SgpForm<PersonDTO> personForm;
	private PersonDTO personDTO;
	private PersonDTO personDTOTableRowSelected;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Table personDTOTable;
	private List<PersonDTO> listPersonDTO;
	private PersonManagementService personManagementService;
	private final List<ShortcutListener> listShortcutListener;
	
	public PersonFinderWindow(boolean customerFindMode,boolean supplierFindMode,boolean functionaryFindMode) {
		// TODO Auto-generated constructor stub
		this.customerFindMode = customerFindMode;
		this.supplierFindMode = supplierFindMode;
		this.functionaryFindMode = functionaryFindMode;
		this.listShortcutListener = this.buildSearchShortcutListeners();
		try{
			
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			//this.addStyleName("v-scrollable");
			this.addStyleName("profile-window");
			this.setUpWindowCaptionAndMainTitle();
			this.initServices();
			this.initMainViewLayout();
			this.setPersonDTOForm();
			this.setContent(this.mainViewLayout);
			Responsive.makeResponsive(this);
			UI.getCurrent().addWindow(this);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	
	/*public PersonFinderWindow(String caption) {
		super(caption);
		// TODO Auto-generated constructor stub
	}*/

	/*public PersonFinderWindow(String caption, Component content) {
		super(caption, content);
		// TODO Auto-generated constructor stub
	}*/

	private void initTitles(){
		Label title = new Label(this.messages.get(this.VIEW_NAME + "form.main.title"));
        title.addStyleName(ValoTheme.LABEL_H4);
        title.addStyleName(ValoTheme.LABEL_COLORED);
        this.personForm.getSgpFormLayout().addComponent(title);
	}
	private String determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView() throws PmsServiceException{
		
		if(!this.customerFindMode && !this.supplierFindMode && !this.functionaryFindMode)
			return this.PERSON_FLAG;
		else if(this.customerFindMode && !this.supplierFindMode && !this.functionaryFindMode)
			return this.CUSTOMER_FLAG;
		else if(!this.customerFindMode && this.supplierFindMode && !this.functionaryFindMode)
			return this.SUPPLIER_FLAG;
		else if(!this.customerFindMode && !this.supplierFindMode && this.functionaryFindMode)
			return this.FUNCTIONARY_FLAG;
		else{			
			throw new PmsServiceException("application.common.gui.exception.by.programmer", null, null);
		}
	}
	
	private void setUpWindowCaptionAndMainTitle() throws PmsServiceException{
		logger.info("\n************************"
					+"\n window caption: " + this.buildCaption()
					+"\n************************");
		this.setCaption(this.buildCaption());
		this.setModal(true);
		this.setClosable(true);
		this.center();
		this.setSizeFull();//
		this.setResizable(true);
        this.setIcon(FontAwesome.USER);
	}
	
	private String buildCaption() throws PmsServiceException{
		if(this.determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView().equals(this.CUSTOMER_FLAG))
			return this.messages.get(this.VIEW_NAME + "caption.customer.mode");
		else if(this.determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView().equals(this.SUPPLIER_FLAG))
			return this.messages.get(this.VIEW_NAME + "caption.supplier.mode");
		else if(this.determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView().equals(this.FUNCTIONARY_FLAG))
			return this.messages.get(this.VIEW_NAME + "caption.functionary.mode");
		else 
			return this.messages.get(this.VIEW_NAME + "caption.person.mode");
	}
	
	private void initMainViewLayout(){
		this.mainViewLayout = new VerticalLayout();
		this.mainViewLayout.setSpacing(true);
		this.mainViewLayout.setMargin(true);
		//this.mainViewLayout.addStyleName("v-scrollable");
		this.mainViewLayout.setSizeFull();
		this.mainViewLayout.setMargin(new MarginInfo(true, false, false, false));
		this.mainViewLayout.setIcon(FontAwesome.COGS);
	}
	
	private void setPersonDTOForm() throws PmsServiceException{
		this.personDTO = new PersonDTO(this.supplierFindMode, this.customerFindMode, this.functionaryFindMode);
		this.personForm = new SgpForm<PersonDTO>(PersonDTO.class, new BeanItem<PersonDTO>(this.personDTO), ValoTheme.FORMLAYOUT_LIGHT/*"light"*/, true);
		this.initTitles();
		if(this.determinePersonOrCustomerOrSupplierOrFunctionaryModeOfTheView().equals(this.SUPPLIER_FLAG)){
			this.personForm.bindAndSetPositionFormLayoutTextField("ruc", this.messages.get(this.VIEW_NAME + "text.field.ruc.label")/**/, true, 90, false,null, true, this.setUpTextFieldFocusListener(),this.setUpTextFieldBlurListener());
    		this.personForm.bindAndSetPositionFormLayoutTextField("commercial_name", this.messages.get(this.VIEW_NAME + "text.field.comercial.name.label")/**/, true, 90, false,null, true, this.setUpTextFieldFocusListener(),this.setUpTextFieldBlurListener());
		}else{				
			this.personForm.bindAndSetPositionFormLayoutTextField("personal_civil_id_document", this.messages.get(this.VIEW_NAME + "text.field.personal.civil.id.doc.label")/**/, true, 90, false,null, true, this.setUpTextFieldFocusListener(),this.setUpTextFieldBlurListener());
    		this.personForm.bindAndSetPositionFormLayoutTextField("personal_name", this.messages.get(this.VIEW_NAME + "text.field.personal.name.label")/**/, true, 90, false,null, true, this.setUpTextFieldFocusListener(),this.setUpTextFieldBlurListener());
    		this.personForm.bindAndSetPositionFormLayoutTextField("personal_last_name", this.messages.get(this.VIEW_NAME + "text.field.personal.last.name.label")/**/, true, 90, false,null, true, this.setUpTextFieldFocusListener(),this.setUpTextFieldBlurListener());
		}
		
		 HorizontalLayout root = new HorizontalLayout();
	        
	        root.setWidth(100.0f, Unit.PERCENTAGE);
	        root.setSpacing(true);
	        root.setMargin(true);
	        root.addStyleName("profile-form");
	        root.addComponent(this.personForm.getSgpFormLayout());
	        root.setExpandRatio(this.personForm.getSgpFormLayout(), 1);
		this.mainViewLayout.addComponent(root);
	}
	
	private List<ShortcutListener> buildSearchShortcutListeners(){
		ShortcutListener enterShortcutListener =  new ShortcutListener("Enter", KeyCode.ENTER, null) {
	    	 @Override
	         public void handleAction(final Object sender, final Object target) {
	    		try{
		    		 logger.info("\n*************************************"
		    				 	+"\nENTER LISTENER"
		    				 	+"\nsender class: "+sender.getClass()
		    				 	+"\ntarget class: "+target.getClass());
		    		final TextField vTextField = (TextField)target;
		    		logger.info("\nsender text field value: "+vTextField.getValue()
		    					+"\n*************************************");
	    			personForm.commit();
	    			doSearch();
	         	}catch(Exception e){		
	        		commonExceptionErrorNotification.showErrorMessageNotification(e);
	        	}
	         }
	     };
	     List<ShortcutListener> listShortcutListener = new ArrayList<ShortcutListener>();
	     listShortcutListener.add(enterShortcutListener);
	     listShortcutListener.add(this.buildEscapeKeyShortcutListener());
	     return listShortcutListener;
	}
	
	private ShortcutListener buildEscapeKeyShortcutListener(){
		return new ShortcutListener("Clear", KeyCode.ESCAPE, null) {
	    	 @Override
	         public void handleAction(final Object sender, final Object target) {
	    		 try{
		    		 logger.info("\n*************************************"
		    				 	+"\nESCAPE LISTENER"
		    				 	+"\nsender class: "+sender.getClass()
		    				 	+"\ntarget class: "+target.getClass());
		            final TextField vTextField = (TextField)target;
			    	logger.info("\nsender text field value: "+vTextField.getValue()
		    					+"\n*************************************");
		             vTextField.setValue("");
		             personForm.discard();
	         	}catch(Exception e){		
	        		commonExceptionErrorNotification.showErrorMessageNotification(e);
	        	}
	         }
	     };
	}
	
	private void initServices() throws Exception{
		this.personManagementService = (PersonManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PERSON_MANAGEMENT_SERVICE);
	}//private void initServices() throws Exception

	private void doSearch() throws PmsServiceException{
		logger.info("\n*********************** search person filter"
					+"\n" + this.personDTO.toString()
					+"\n***********************");
		this.listPersonDTO = this.personManagementService.listPersonDTO(this.personDTO);
		if(this.personDTOTable!=null)this.mainViewLayout.removeComponent(this.personDTOTable);
		if(this.listPersonDTO!=null && !this.listPersonDTO.isEmpty()){
			this.buildpersonDTOTable();
			this.mainViewLayout.addComponent(this.personDTOTable);
		}else
			throw new PmsServiceException("py.com.kyron.sgp.persistence.common.search.no.results.found.error", null, null);
	}
	
	 private void buildpersonDTOTable() throws PmsServiceException{
	    	this.personDTOTable = new Table();
	    	BeanItemContainer<PersonDTO> PersonDTOBeanItemContainer	= new BeanItemContainer<PersonDTO>(PersonDTO.class);
	    	PersonDTOBeanItemContainer.addAll(this.listPersonDTO);
	    	this.personDTOTable.setContainerDataSource(PersonDTOBeanItemContainer);	    	
	    	this.personDTOTable.setVisibleColumns(new Object[] {"commercial_name","personal_name","personal_last_name"});
	    	this.personDTOTable.setColumnHeader("commercial_name", this.messages.get(this.VIEW_NAME + "text.field.comercial.name.label"));
	    	this.personDTOTable.setColumnAlignment("commercial_name", Table.Align.LEFT);
	    	this.personDTOTable.setColumnHeader("personal_name", this.messages.get(this.VIEW_NAME + "text.field.personal.name.label"));
	    	this.personDTOTable.setColumnAlignment("personal_name", Table.Align.LEFT);
	    	this.personDTOTable.setColumnHeader("personal_last_name", this.messages.get(this.VIEW_NAME + "text.field.personal.last.name.label"));
	    	this.personDTOTable.setColumnAlignment("personal_last_name", Table.Align.LEFT);
	    	this.personDTOTable.setSizeFull();
	    	//this.personDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
	    	this.personDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
	    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
	    	this.personDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
	    	this.personDTOTable.setColumnExpandRatio("commercial_name", 0.1f); 
	    	this.personDTOTable.setColumnExpandRatio("personal_name", 0.1f);    	
	    	this.personDTOTable.setColumnExpandRatio("personal_last_name", 0.1f);
	    	this.personDTOTable.setSelectable(true);
	    	this.personDTOTable.setColumnCollapsingAllowed(true);
	    	this.personDTOTable.setColumnCollapsible("commercial_name", false);
	    	//this.rolesTable.setColumnCollapsible("role_description", false);
	    	//this.personDTOTable.setSortContainerPropertyId("commercial_name");
	    	//this.personDTOTable.setSortAscending(false);
	    	this.personDTOTable.setColumnReorderingAllowed(true);
	    	this.personDTOTable.setFooterVisible(true);
	    	this.personDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	    	this.personDTOTable.addShortcutListener(this.buildTableEnterKeyShortcutListener());
	 }//private void buildMachinesTable(){
	 
	 
	 private ShortcutListener buildTableEnterKeyShortcutListener(){
		 return new ShortcutListener("Enter", KeyCode.ENTER, null) {
	    	 @Override
	         public void handleAction(final Object sender, final Object target) {
	    		try{
		    		 logger.info("\n*************************************"
		    				 	+"\nTABLE ENTER LISTENER"
		    				 	+"\nsender class: "+sender.getClass()
		    				 	+"\ntarget class: "+target.getClass());
		    		personDTOTableRowSelected = null;
		    		final Table table = (Table)target;
		    		/*List<PersonDTO> vListPersonDTO = (List<PersonDTO>)table.getValue();
		    		logger.info("\ntable selected rows count: "
		    		+ (vListPersonDTO!=null && !vListPersonDTO.isEmpty() ? vListPersonDTO.size() : " no selected elements in the table")
		    					+"\n*************************************");
		    		if(vListPersonDTO!=null && !vListPersonDTO.isEmpty() && vListPersonDTO.size()==1)
		    			close();
		    		*/
		    		personDTOTableRowSelected = (PersonDTO)table.getValue();
		    		if(personDTOTableRowSelected!=null)
		    			close();
		    		
	         	}catch(Exception e){		
	        		commonExceptionErrorNotification.showErrorMessageNotification(e);
	        	}
	         }
	     };
	 }

	public List<PersonDTO> getListPersonDTO() {
		return listPersonDTO;
	}

	public void setListPersonDTO(List<PersonDTO> listPersonDTO) {
		this.listPersonDTO = listPersonDTO;
	}
	
	public void adjuntWindowSizeAccordingToCientDisplay(){
		logger.info("\n*************************************"
					+"\n adjusting window width and height according to client display size"
					+"\n browser width: " + Page.getCurrent().getBrowserWindowWidth()
					+"\n browser height: " + Page.getCurrent().getBrowserWindowHeight()
					+"\n*************************************");
		if(Page.getCurrent().getBrowserWindowWidth() < 800)
			this.setSizeFull();
		else{
			this.setWidth("650px");
			this.setHeight("800px");
		}			
	}
	
	private BlurListener setUpTextFieldBlurListener(){
		return new BlurListener(){

			@Override
			public void blur(BlurEvent event) {
				// TODO Auto-generated method stub
				logger.info("\n************************"
							+"\n blur listener in text field"
							+"\n removing enter and escape keys shortcut listeners"
							+ "\n object class type: "+event.getSource().getClass());
				final TextField textField = (TextField)event.getSource();
				int listenersCount = 0;
				for(ShortcutListener vShortcutListener : listShortcutListener){
					listenersCount++;
					textField.removeListener(ShortcutListener.class, vShortcutListener);
					textField.removeShortcutListener(vShortcutListener);
				}
				logger.info("\n-------------------------"
							+"\n removed listeners count : " + listenersCount
							+"\n************************");
			}
			
		};
	}
	
	private FocusListener setUpTextFieldFocusListener(){
		return new FocusListener(){

			@Override
			public void focus(FocusEvent event) {
				// TODO Auto-generated method stub
				logger.info("\n************************"
						+"\n focus listener in text field"
						+"\n adding enter and escape keys shortcut listeners"
						+"\n************************");
				final TextField textField = (TextField)event.getSource();
				//final List<ShortcutListener> listShortcutListener = buildSearchShortcutListeners();
				for(ShortcutListener vShortcutListener : listShortcutListener)
					textField.addShortcutListener(vShortcutListener);
			}
			
		};
	}


	/**
	 * @return the personDTOTableRowSelected
	 */
	public PersonDTO getPersonDTOTableRowSelected() {
		return personDTOTableRowSelected;
	}


	/**
	 * @param personDTOTableRowSelected the personDTOTableRowSelected to set
	 */
	public void setPersonDTOTableRowSelected(PersonDTO personDTOTableRowSelected) {
		this.personDTOTableRowSelected = personDTOTableRowSelected;
	}
}
