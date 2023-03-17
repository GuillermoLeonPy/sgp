package py.com.kyron.sgp.gui.view.personmanagement.components;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.personmanagement.service.PersonManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.EditMonedaEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.view.DashboardViewType;

import com.vaadin.data.Item;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import py.com.kyron.sgp.gui.event.SgpEvent.PersonRegisterFormViewEvent;

public class SearchPersonToolComponent extends HorizontalLayout {
	
	private final Logger logger = LoggerFactory.getLogger(SearchPersonToolComponent.class);
	private Label title;
	private HorizontalLayout tools;
	private TextField filter;
	private Map<String,String> messages;
	private final String VIEW_NAME = "search.person.tool.component.";
	private PersonManagementService personManagementService;
	private SgpForm<FilterDTO> filterForm;
	private FilterDTO filterDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private List<PersonDTO> listPersonDTO;
	private PersonDTO personDTO;
	private final String CALLER_VIEW;
	private final boolean massiveInsertMode;
	private final boolean insertInSupplierMode;
	private final boolean insertInCustomerMode;
	private final boolean insertInFuntionaryMode;
	
	public SearchPersonToolComponent(String mainTittle, final String CALLER_VIEW, boolean massiveInsertMode,
			boolean insertInSupplierMode, boolean insertInCustomerMode, boolean insertInFuntionaryMode) {
		// TODO Auto-generated constructor stub
		this.CALLER_VIEW = CALLER_VIEW;
		this.massiveInsertMode = massiveInsertMode;
		this.insertInSupplierMode = insertInSupplierMode;
		this.insertInCustomerMode = insertInCustomerMode;
		this.insertInFuntionaryMode = insertInFuntionaryMode;
		
		try{			
			this.initServices();
			this.prepareMessages();
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
	        //this.addStyleName("viewheader");
			//this.addStyleName("viewheader");
	        this.setSpacing(true);
	        Responsive.makeResponsive(this);
	        
	        this.title = new Label(mainTittle);
	        this.title.setSizeUndefined();
	        //this.title.addStyleName(mainTittle.length() <= 26 ? ValoTheme.LABEL_H1 :ValoTheme.LABEL_H2 );
	        this.title.addStyleName( ValoTheme.LABEL_H1 );
	        this.title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	        
	        /*GridLayout gridLayout = new GridLayout(10,1);
	        gridLayout.addComponent(new FormLayout(this.title), 0, 0);
	        gridLayout.addComponent(new FormLayout(this.buildFilterTextField()), 8, 0);
	        gridLayout.addComponent(new FormLayout(createRegisterPersonButton()), 9, 0);
	        this.tools = new HorizontalLayout(gridLayout);*/
	        if(mainTittle.length() <= 26 )
	        	this.tools = new HorizontalLayout(new FormLayout(this.title),/*new GridLayout(10,1),new GridLayout(10,1),new GridLayout(10,1),*/
	        								new FormLayout(this.buildFilterTextField()),
	        								new FormLayout(createRegisterPersonButton()));
	        else
	        	this.tools = new HorizontalLayout(new FormLayout(this.title),
						new FormLayout(this.buildFilterTextField()),
						new FormLayout(createRegisterPersonButton()));	        	
	        
	        //this.tools = new HorizontalLayout(this.buildTextFieldFilterAndCreatePersonButtonInGridLayout());
	        this.tools.setSpacing(true);
	        this.tools.addStyleName("toolbar");
	        //this.addComponent(this.title);
	        this.addComponent(this.tools);
		}catch(Exception e){
			logger.error(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null));
			logger.error(e.getMessage(), e);
			addComponent(new Label(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null)));
		}
	}

	/*
	public SearchPersonToolComponent(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/


	private void initServices() throws Exception{
		this.personManagementService = (PersonManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PERSON_MANAGEMENT_SERVICE);
	}//private void initServices() throws Exception
	
	private void searchPerson() throws PmsServiceException{
		this.personDTO = new PersonDTO();
		this.personDTO.setPersonal_civil_id_document(this.checkPersonalCivilIdDocument());
		if(this.personDTO.getPersonal_civil_id_document()==null) this.personDTO.setRuc(this.filterDTO.getText());
		
		if(this.insertInCustomerMode)this.personDTO.setIs_customer(this.insertInCustomerMode);
		else if(this.insertInFuntionaryMode)this.personDTO.setIs_functionary(this.insertInFuntionaryMode);
		else if(this.insertInSupplierMode)this.personDTO.setIs_supplier(this.insertInSupplierMode);		
		
		logger.info("\nsearchPerson(): "+ this.personDTO.toString());
		this.listPersonDTO = this.personManagementService.listPersonDTO(personDTO);		
		if(this.listPersonDTO == null || this.listPersonDTO.isEmpty())
			throw new PmsServiceException("py.com.kyron.sgp.persistence.common.search.no.results.found.error", null, null);
		this.printRetrievedPersonList();
	}
	
	private Long checkPersonalCivilIdDocument(){
		try{
			return Long.parseLong(this.filterDTO.getText());
		}catch(Exception e){
			logger.info("\nSearchPersonToolComponent.checkPersonalCivilIdDocument\nfilterText : " + this.filterDTO.getText()
					+ "not a Personal Civil Id Document");
		}
		return null;
	}
	
	@SuppressWarnings("serial")
	private TextField buildFilterTextField(){
		this.filterDTO = new FilterDTO();		
		this.filterForm = new SgpForm<FilterDTO>(FilterDTO.class, new BeanItem<FilterDTO>(this.filterDTO), 10, 1);
		this.filter =  this.filterForm.bindAndBuildTextField("text", ""/*caption*/, /*immediate*/false, 10, false/*required*/, this.messages.get(this.VIEW_NAME + "filter.input.required.message")/*required message*/);
		this.filter.setInputPrompt(this.messages.get(this.VIEW_NAME + "filter.input.prompt"));
		//this.filter.setIcon(FontAwesome.SEARCH);
		this.filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		this.filter.addShortcutListener(new ShortcutListener("Search", KeyCode.ENTER, null) {
	    	 @Override
	         public void handleAction(final Object sender, final Object target) {
	    		 try {
	    			filter.setValidationVisible(true);
					filterForm.commit();
					searchPerson();
					goToRegisterPersonaView(listPersonDTO.get(0));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					commonExceptionErrorNotification.showErrorMessageNotification(e);
				}
	         }
	    });
		this.filter.addShortcutListener(new ShortcutListener("Clear", KeyCode.ESCAPE, null) {
	    	 @Override
	         public void handleAction(final Object sender, final Object target) {
	    		 filterForm.discard();
	    		 filter.setValidationVisible(false);
	    		 filter.setValue("");	             
	         }
	     });
		this.filter.setValidationVisible(false);		
		return this.filter;
	}
	
	private FormLayout buildTextFieldInFormLayout(){
		this.buildFilterTextField();
		this.filterForm.getSgpFormLayout().addComponent(this.filter);
		return this.filterForm.getSgpFormLayout();
	}
	
	private GridLayout buildTextFieldFilterAndCreatePersonButtonInGridLayout(){
		this.buildFilterTextField();
		//this.filterForm.setSgpFormLayout(new FormLayout());
		//this.filterForm.getSgpFormLayout().addComponent(this.filter);
		this.filterForm.getSgpGridLayout().addComponent(new FormLayout(this.filter), 0, 0);
		this.filterForm.getSgpGridLayout().addComponent(new FormLayout(createRegisterPersonButton()), 5, 0);
		return this.filterForm.getSgpGridLayout();
	}
	
    private void prepareMessages() throws Exception{    	
    	BussinesSessionUtils bussinesSessionUtils =	(BussinesSessionUtils)SgpApplicationContextAware.getXmlWebApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
    	this.setLocale(bussinesSessionUtils.getRawSessionData().getUserSessionLocale());
    	this.messages = bussinesSessionUtils.getApplicationUtils().loadMessagesByViewAndKeysAndUserSessionLocale(this.VIEW_NAME, this.getLocale());
    }
    
    private Button createRegisterPersonButton(){
        final Button registerPerson = new Button(this.getCreateRegisterButtonLabel());
        registerPerson.setDescription(this.getCreateRegisterButtonLabelDescription());
        registerPerson.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
            	try{
            		logger.info("\ncreateRegisterPersonButton() clicked !");
                	goToRegisterPersonaView(null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					commonExceptionErrorNotification.showErrorMessageNotification(e);
				}
            }
        });
        registerPerson.setEnabled(true);
        return registerPerson;
    }
    
    private String getCreateRegisterButtonLabel(){
		if(!this.insertInCustomerMode && !this.insertInSupplierMode && !this.insertInFuntionaryMode)
			return this.messages.get(this.VIEW_NAME + "button.register.person");
		else if(this.insertInCustomerMode && !this.insertInSupplierMode && !this.insertInFuntionaryMode)
			return this.messages.get(this.VIEW_NAME + "button.register.customer");
		else if(!this.insertInCustomerMode && this.insertInSupplierMode && !this.insertInFuntionaryMode)
			return this.messages.get(this.VIEW_NAME + "button.register.supplier");
		else if(!this.insertInCustomerMode && !this.insertInSupplierMode && this.insertInFuntionaryMode)
			return this.messages.get(this.VIEW_NAME + "button.register.functionary");
		
		logger.info("\ngetCreateRegisterButtonLabel(): error in flags values !"
				+"\nthis.insertInCustomerMode : "+this.insertInCustomerMode
				+"\nthis.insertInFuntionaryMode: "+this.insertInFuntionaryMode
				+"\nthis.insertInSupplierMode: "+this.insertInSupplierMode);
		commonExceptionErrorNotification.showErrorMessageNotification(new PmsServiceException("application.common.gui.exception.by.programmer", null, null));
		return this.messages.get(this.VIEW_NAME + "button.register.person");
    }
    
    
    private String getCreateRegisterButtonLabelDescription(){
		if(!this.insertInCustomerMode && !this.insertInSupplierMode && !this.insertInFuntionaryMode)
			return this.messages.get(this.VIEW_NAME + "button.register.person.description");
		else if(this.insertInCustomerMode && !this.insertInSupplierMode && !this.insertInFuntionaryMode)
			return this.messages.get(this.VIEW_NAME + "button.register.customer.description");
		else if(!this.insertInCustomerMode && this.insertInSupplierMode && !this.insertInFuntionaryMode)
			return this.messages.get(this.VIEW_NAME + "button.register.supplier.description");
		else if(!this.insertInCustomerMode && !this.insertInSupplierMode && this.insertInFuntionaryMode)
			return this.messages.get(this.VIEW_NAME + "button.register.functionary.description");
		logger.info("getCreateRegisterButtonLabel(): error in flags values !"
				+"\nthis.insertInCustomerMode : "+this.insertInCustomerMode
				+"\nthis.insertInFuntionaryMode: "+this.insertInFuntionaryMode
				+"\nthis.insertInSupplierMode: "+this.insertInSupplierMode);
		commonExceptionErrorNotification.showErrorMessageNotification(new PmsServiceException("application.common.gui.exception.by.programmer", null, null));
		return this.messages.get(this.VIEW_NAME + "button.register.person.description");
    }
    
    private void goToRegisterPersonaView(PersonDTO personDTO){
		try{
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.REGISTER_PERSON_FORM.getViewName());
			DashboardEventBus.post(new PersonRegisterFormViewEvent(personDTO, this.CALLER_VIEW, this.massiveInsertMode, 
					this.insertInSupplierMode, this.insertInCustomerMode, this.insertInFuntionaryMode));
		}catch(Exception e){
			logger.error(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null));
			logger.error(e.getMessage(), e);
		}
    }
    
	public class FilterDTO {
		@NotEmpty
		@Pattern(regexp = "^[0-9]+(-?[0-9]+)+$")
		@Size(min=6, max=20)
		private String text;

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}
	
	public List<PersonDTO> getListPersonDTO() {
		return listPersonDTO;
	}

	private void printRetrievedPersonList(){
		for(PersonDTO personDTO: this.listPersonDTO){
			logger.info("\nprintRetrievedPersonList : "+personDTO.toString());
		}			
	}
}
