package py.com.kyron.sgp.gui.view.productionmanagement.production.process.activities.instances.assignment.management.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.personmanagement.service.PersonManagementService;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityInstanceDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SearchProductionProcessActivityInstanceDTOComponent extends
		HorizontalLayout {

	private final Logger logger = LoggerFactory.getLogger(SearchProductionProcessActivityInstanceDTOComponent.class);
	private Label title;
	private TextField personalCivilIdDocumentFilter;
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private List<ProductionProcessActivityInstanceDTO> listProductionProcessActivityInstanceDTO;
	private ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO;
	private PersonManagementService personManagementService;
    private BussinesSessionUtils bussinesSessionUtils;
    private ProductionManagementService productionManagementService;
    private OptionGroup selectOptionGroup;
    private List<SelectOneOption> listSelectOneOption;
    private PersonDTO personDTO;
    private List<PersonDTO> listPersonDTO;
    private final SearchProductionProcessActivityInstanceDTOComponentHostViewFunctions searchProductionProcessActivityInstanceDTOComponentHostViewFunctions;
    private SgpForm<PersonDTO> personDTOForm;
    
	public SearchProductionProcessActivityInstanceDTOComponent(
			final SearchProductionProcessActivityInstanceDTOComponentHostViewFunctions searchProductionProcessActivityInstanceDTOComponentHostViewFunctions,
			final String VIEW_NAME) {
		// TODO Auto-generated constructor stub
		this.VIEW_NAME = VIEW_NAME;
		this.searchProductionProcessActivityInstanceDTOComponentHostViewFunctions = searchProductionProcessActivityInstanceDTOComponentHostViewFunctions;
		try{
			logger.info("\n SearchProductionProcessActivityInstanceDTOComponent()...");
			this.initServices();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
	        this.setSpacing(true);
	        Responsive.makeResponsive(this);
	        this.addStyleName("viewheader");
	        this.setSizeFull();
	        this.initTitles();
	        this.buildToolBar();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public SearchProductionProcessActivityInstanceDTOComponent(
			Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void initServices(){
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
		this.personManagementService = (PersonManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PERSON_MANAGEMENT_SERVICE);
	}
	
	private void initTitles(){
        this.title = new Label(this.messages.get(this.VIEW_NAME + "main.title"));
        //this.title.setSizeUndefined();
        this.title.addStyleName(ValoTheme.LABEL_H2);
        this.title.addStyleName(ValoTheme.LABEL_COLORED);
        this.title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        this.addComponent(this.title);
        this.setComponentAlignment(this.title, Alignment.TOP_LEFT);
	}
	
	private void buildToolBar(){
		HorizontalLayout tools = new HorizontalLayout();
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        this.setUpSelectOptionGroup();
        tools.addComponent(this.selectOptionGroup);
        tools.addComponent(this.buildFilterTextField());
        this.addComponent(tools);
        this.setComponentAlignment(tools, Alignment.MIDDLE_CENTER);
	}
	
	private void setUpSelectOptionGroup(){
    	this.selectOptionGroup = new OptionGroup();//
    	this.selectOptionGroup.addStyleName("small");
    	this.selectOptionGroup.setMultiSelect(false);
    	this.selectOptionGroup.setCaption(this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.search.tool.option.group.activities.query.filter.caption"));
    	this.setUpListSelectOneOption();
    	BeanItemContainer<SelectOneOption> vSelectOneOptionBeanItemContainer = new BeanItemContainer<SelectOneOption>(SelectOneOption.class);
    	vSelectOneOptionBeanItemContainer.addAll(listSelectOneOption);
    	this.selectOptionGroup.setContainerDataSource(vSelectOneOptionBeanItemContainer);
    	this.selectOptionGroup.setItemCaptionPropertyId("value");
    	this.selectOptionGroup.setNullSelectionAllowed(false);
    	this.selectOptionGroup.setRequired(true);
    	this.selectOptionGroup.setRequiredError(this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.search.tool.option.group.activities.query.filter.required.message"));
    	this.selectOptionGroup.addValueChangeListener(this.setUpValueChangeListenerForSelectOptionGroup());
	}

    private Property.ValueChangeListener setUpValueChangeListenerForSelectOptionGroup(){
    	return new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				try{
	                if (event.getProperty().getValue() != null){
	                	final SelectOneOption vSelectOneOption = (SelectOneOption)event.getProperty().getValue();
		    			personalCivilIdDocumentFilter.setValidationVisible(true);
		    			personDTOForm.commit();
		    			logger.info("\n ===================================== " 
		    					+ 	"\n vSelectOneOption.toString() : " + vSelectOneOption.toString()
		    					+	"\n ===================================== ");
		    			validatePersonalCivilDocumentFilter();
	                	setQueryFilter(vSelectOneOption);
	                	performSearchProductionProcessActivityInstance();	                	
	                	searchProductionProcessActivityInstanceDTOComponentHostViewFunctions.buildProductionProcessActivityInstanceDTOTable(listProductionProcessActivityInstanceDTO);
	                }//if (event.getProperty().getValue() != null){
				}catch(Exception e){
					commonExceptionErrorNotification.showErrorMessageNotification(e);
				}
				/* to clear the table in case of no results found error */
				if(listProductionProcessActivityInstanceDTO == null || listProductionProcessActivityInstanceDTO.isEmpty()){
					try{
						searchProductionProcessActivityInstanceDTOComponentHostViewFunctions.buildProductionProcessActivityInstanceDTOTable(null);
					}catch(Exception e){
						logger.error("\n error",e);
					}
				}
			}//public void valueChange(ValueChangeEvent event)
        };//return new Property.ValueChangeListener() 
    }
    
    private void performSearchProductionProcessActivityInstance() throws PmsServiceException{
    	this.doSearchProductionProcessActivityInstanceDTO();
    	if(listProductionProcessActivityInstanceDTO == null || listProductionProcessActivityInstanceDTO.isEmpty())
    		throw new PmsServiceException("py.com.kyron.sgp.persistence.common.search.no.results.found.error", null, null);	                	
    	this.searchProductionProcessActivityInstanceDTOComponentHostViewFunctions.buildProductionProcessActivityInstanceDTOTable(listProductionProcessActivityInstanceDTO);
    }
    
    private void setQueryFilter(final SelectOneOption vSelectOneOption){
    	
    	if(vSelectOneOption.getKey().equals("application.common.activities.instance.query.criteria.status.pending"))
	    	this.productionProcessActivityInstanceDTO = 
	    			new ProductionProcessActivityInstanceDTO
	    			(null, 
	    			true, 
	    			true, 
	    			null, 
	    			null,
	    			null,
	    			null, 
	    			null,
	    			null);
    	else if(vSelectOneOption.getKey().equals("application.common.activities.instance.query.criteria.status.assigned.require.partial.result.recall"))
	    	this.productionProcessActivityInstanceDTO = 
			new ProductionProcessActivityInstanceDTO
			(/*this.listPersonDTO!=null && !this.listPersonDTO.isEmpty()  ? this.listPersonDTO.get(0).getId() : null*/null, 
					null, 
					null, 
					true, 
					"application.common.status.assigned",
					null,
					"application.common.status.partial.result.recalled", 
					null,
	    			null);
    	else if(vSelectOneOption.getKey().equals("application.common.activities.instance.query.criteria.status.assigned.require.raw.material.supply"))
	    	this.productionProcessActivityInstanceDTO = 
			new ProductionProcessActivityInstanceDTO
			(/*this.listPersonDTO!=null && !this.listPersonDTO.isEmpty()  ? this.listPersonDTO.get(0).getId() : null*/null,
					null,  
					null,  
					null,  
					"application.common.status.partial.result.recalled",
					"application.common.status.assigned",		
					"application.common.status.in.progress", 
					null,
	    			null);
    	else if(vSelectOneOption.getKey().equals("application.common.activities.instance.query.criteria.status.in.progress"))
	    	this.productionProcessActivityInstanceDTO = 
			new ProductionProcessActivityInstanceDTO
			(/*this.listPersonDTO!=null && !this.listPersonDTO.isEmpty()  ? this.listPersonDTO.get(0).getId() : null*/null,
					null, 
					null, 
					null, 
					"application.common.status.in.progress",
					null,
					null,
					null,
	    			null);
    	else if(vSelectOneOption.getKey().equals("application.common.activities.instance.query.criteria.status.finalized.require.partial.result.delivery"))
	    	this.productionProcessActivityInstanceDTO = 
			new ProductionProcessActivityInstanceDTO
			(/*this.listPersonDTO!=null && !this.listPersonDTO.isEmpty()  ? this.listPersonDTO.get(0).getId() : null*/null,
					null, 
					null, 
					null, 
					"application.common.status.finalized",
					null,
					"application.common.status.partial.result.delivered", 
					true,
	    			null);
    	else if(vSelectOneOption.getKey().equals("application.common.activities.instance.query.criteria.status.finalized.require.product.delivery"))
	    	this.productionProcessActivityInstanceDTO = 
			new ProductionProcessActivityInstanceDTO
			(/*this.listPersonDTO!=null && !this.listPersonDTO.isEmpty()  ? this.listPersonDTO.get(0).getId() : null*/null,
					null, 
					null, 
					null, 
					"application.common.status.finalized",
					null,
					"application.common.status.final.product.delivered", 
					null,
	    			true);
    }
	
    private void setUpListSelectOneOption(){
    	this.listSelectOneOption = new ArrayList<SearchProductionProcessActivityInstanceDTOComponent.SelectOneOption>();
    	this.listSelectOneOption.add(
    			new SelectOneOption("application.common.activities.instance.query.criteria.status.pending", 
    					this.messages.get("application.common.activities.instance.query.criteria.status.pending")));
    	this.listSelectOneOption.add(
    			new SelectOneOption("application.common.activities.instance.query.criteria.status.assigned.require.partial.result.recall", 
    					this.messages.get("application.common.activities.instance.query.criteria.status.assigned.require.partial.result.recall")));
    	this.listSelectOneOption.add(
    			new SelectOneOption("application.common.activities.instance.query.criteria.status.assigned.require.raw.material.supply", 
    					this.messages.get("application.common.activities.instance.query.criteria.status.assigned.require.raw.material.supply")));
    	this.listSelectOneOption.add(
    			new SelectOneOption("application.common.activities.instance.query.criteria.status.in.progress", 
    					this.messages.get("application.common.activities.instance.query.criteria.status.in.progress")));
    	this.listSelectOneOption.add(
    			new SelectOneOption("application.common.activities.instance.query.criteria.status.finalized.require.partial.result.delivery", 
    					this.messages.get("application.common.activities.instance.query.criteria.status.finalized.require.partial.result.delivery")));
    	this.listSelectOneOption.add(
    			new SelectOneOption("application.common.activities.instance.query.criteria.status.finalized.require.product.delivery", 
    					this.messages.get("application.common.activities.instance.query.criteria.status.finalized.require.product.delivery")));
    }
    
    private void doSearchProductionProcessActivityInstanceDTO() throws PmsServiceException{
    	this.productionProcessActivityInstanceDTO.setId_person(this.listPersonDTO!=null && !this.listPersonDTO.isEmpty()  ? this.listPersonDTO.get(0).getId() : null);
    	logger.info(	"\n =========================="
    				+	"\n doSearchProductionProcessActivityInstanceDTO"
    				+	"\n this.productionProcessActivityInstanceDTO : " + this.productionProcessActivityInstanceDTO.toString()
    				+	"\n ==========================");
    	this.listProductionProcessActivityInstanceDTO = this.productionManagementService.listProductionProcessActivityInstanceDTO(this.productionProcessActivityInstanceDTO);
    }
    
    private void clearListProductionProcessActivityInstanceDTO(){
    	this.listProductionProcessActivityInstanceDTO = new ArrayList<ProductionProcessActivityInstanceDTO>();
    	this.searchProductionProcessActivityInstanceDTOComponentHostViewFunctions.buildProductionProcessActivityInstanceDTOTable(this.listProductionProcessActivityInstanceDTO);
    }
    
    private void validatePersonalCivilDocumentFilter() throws CommitException, PmsServiceException{
		personalCivilIdDocumentFilter.setValidationVisible(true);
		personDTOForm.commit();		
    }
    
	private TextField buildFilterTextField(){
		this.personDTO= new PersonDTO();
		this.personDTO.setIs_functionary(true);
		this.personDTOForm = new SgpForm<PersonDTO>(PersonDTO.class, new BeanItem<PersonDTO>(this.personDTO), 10, 1);
		this.personalCivilIdDocumentFilter =  this.personDTOForm.bindAndBuildTextField("personal_civil_id_document", ""/*caption*/, /*immediate*/false, 10, false/*required*/, null/*required message*/);
		this.personalCivilIdDocumentFilter.setInputPrompt(this.messages.get("application.common.personal.civil.id.document.filter.input.prompt"));
		this.personalCivilIdDocumentFilter.setIcon(FontAwesome.SEARCH);
		this.personalCivilIdDocumentFilter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		this.personalCivilIdDocumentFilter.addShortcutListener(new ShortcutListener("Search", KeyCode.ENTER, null) {
	    	 @Override
	         public void handleAction(final Object sender, final Object target) {
	    		 try {
	    			clearListProductionProcessActivityInstanceDTO();
	    			personalCivilIdDocumentFilter.setValidationVisible(true);
	    			personDTOForm.commit();
	    			selectOptionGroup.validate();
	    			selectOptionGroup.commit();
	    			searchPerson();
	    			performSearchProductionProcessActivityInstance();				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					commonExceptionErrorNotification.showErrorMessageNotification(e);
				}
	    		if(listProductionProcessActivityInstanceDTO == null || listProductionProcessActivityInstanceDTO.isEmpty()){
	    			try{
						searchProductionProcessActivityInstanceDTOComponentHostViewFunctions.buildProductionProcessActivityInstanceDTOTable(null);
					}catch(Exception e){
						logger.error("\n error",e);
					}
				}
	         }
	    });
		this.personalCivilIdDocumentFilter.addShortcutListener(new ShortcutListener("Clear", KeyCode.ESCAPE, null) {
	    	 @Override
	         public void handleAction(final Object sender, final Object target) {
	    		 personDTOForm.discard();
	    		 personalCivilIdDocumentFilter.setValidationVisible(false);
	    		 personalCivilIdDocumentFilter.setValue("");	             
	         }
	     });
		this.personalCivilIdDocumentFilter.setValidationVisible(false);		
		return this.personalCivilIdDocumentFilter;
	}
	
	private void searchPerson() throws PmsServiceException{
		logger.info("\nsearchPerson(): "+ this.personDTO.toString());
		if(this.personDTO.getPersonal_civil_id_document()!=null
				&& this.listPersonDTO!=null 
				&& !this.listPersonDTO.isEmpty() 
				&& this.listPersonDTO.get(0).getPersonal_civil_id_document().equals(this.personDTO.getPersonal_civil_id_document()))			
			return;
		else if (this.personDTO.getPersonal_civil_id_document()==null){
			this.listPersonDTO = null;
			return;
		}
		this.listPersonDTO = this.personManagementService.listPersonDTO(this.personDTO);
		if(this.listPersonDTO == null || this.listPersonDTO.isEmpty())
			throw new PmsServiceException("py.com.kyron.sgp.persistence.common.search.no.results.found.error", null, null);
	}
    
	public class SelectOneOption{
		private String key;
		private String value;

		/**
		 * @param key
		 * @param value
		 */
		public SelectOneOption(String key, String value) {
			super();
			this.key = key;
			this.value = value;
		}

		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * @param key the key to set
		 */
		public void setKey(String key) {
			this.key = key;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "SelectOneOption [key=" + key + ", value=" + value + "]";
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SelectOneOption other = (SelectOneOption) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			return true;
		}

		private SearchProductionProcessActivityInstanceDTOComponent getOuterType() {
			return SearchProductionProcessActivityInstanceDTOComponent.this;
		}
	}
}
