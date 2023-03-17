package py.com.kyron.sgp.gui.view.comercialmanagement.ordermanagementview.components;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.personmanagement.service.PersonManagementService;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.OrderRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.personmanagement.components.SearchPersonToolComponent.FilterDTO;

import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SearchOrderToolComponent extends HorizontalLayout {

	private final Logger logger = LoggerFactory.getLogger(SearchOrderToolComponent.class);
	private Label title;
	private TextField filter;
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private SgpForm<FilterDTO> filterForm;
	private FilterDTO filterDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private List<OrderDTO> listOrderDTO;
    private ComercialManagementService comercialManagementService;
    private final boolean registerOrderButtonVisible;
    private PersonDTO personDTO;
    private List<PersonDTO> listPersonDTO;
    private PersonManagementService personManagementService;
    private final SearchOrderToolComponentHostView searchOrderToolComponentHostView;
    
	public SearchOrderToolComponent(final SearchOrderToolComponentHostView searchOrderToolComponentHostView,String VIEW_NAME,boolean registerOrderButtonVisible) {
		// TODO Auto-generated constructor stub
		this.VIEW_NAME = VIEW_NAME;
		this.registerOrderButtonVisible = registerOrderButtonVisible;
		this.searchOrderToolComponentHostView = searchOrderToolComponentHostView;
		try{
			logger.info("\n SearchOrderToolComponent()...");
			this.initServices();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
	        this.setSpacing(true);
	        Responsive.makeResponsive(this);
	        this.addStyleName("viewheader");
	        this.initTitles();
	        this.buildToolBar();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public SearchOrderToolComponent(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	
	private void initServices() throws Exception{
		this.comercialManagementService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
		this.personManagementService = (PersonManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PERSON_MANAGEMENT_SERVICE);
	}

	private void initTitles(){
        this.title = new Label(this.messages.get(this.VIEW_NAME + "main.title"));
        this.title.setSizeUndefined();
        this.title.addStyleName(ValoTheme.LABEL_H1);
        this.title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        final FormLayout vFormLayout = new FormLayout(this.title);
        Responsive.makeResponsive(vFormLayout);
        this.addComponent(vFormLayout);
        //this.addComponent(new FormLayout(this.title));
	}
	
	private void buildToolBar(){
		final FormLayout vFormLayout = new FormLayout(this.buildFilterTextField());
		Responsive.makeResponsive(vFormLayout);
		final FormLayout vFormLayout2 = new FormLayout(this.createRegisterOrderButton());
		Responsive.makeResponsive(vFormLayout2);
		/*HorizontalLayout vHorizontalLayout = 
				new HorizontalLayout(
						new FormLayout(this.buildFilterTextField()),
						new FormLayout(this.createRegisterOrderButton()));*/
		HorizontalLayout vHorizontalLayout = 
				new HorizontalLayout(
						vFormLayout,
						vFormLayout2);
		vHorizontalLayout.setSpacing(true);
		vHorizontalLayout.addStyleName("toolbar");
		Responsive.makeResponsive(vHorizontalLayout);
		this.addComponent(vHorizontalLayout);
	}
	
	
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
					searchOrders();					
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
	
	
    private Button createRegisterOrderButton(){
        final Button registerOrder = new Button(this.messages.get(this.VIEW_NAME + "button.register.order"));
        registerOrder.setDescription(this.messages.get(this.VIEW_NAME + "button.register.order.description"));
        registerOrder.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
            	try{
            		logger.info("\nc RegisterOrderButton clicked !");
                	goToRegisterOrderView(null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					commonExceptionErrorNotification.showErrorMessageNotification(e);
				}
            }
        });
        registerOrder.setEnabled(true);
        registerOrder.setVisible(this.registerOrderButtonVisible);
        return registerOrder;
    }
	
    private void searchOrders() throws PmsServiceException,Exception{
    	this.personDTO = new PersonDTO();
		this.personDTO.setPersonal_civil_id_document(this.checkPersonalCivilIdDocument());
		if(this.personDTO.getPersonal_civil_id_document()==null) this.personDTO.setRuc(this.filterDTO.getText());
		
		logger.info("\nsearchPerson(): "+ this.personDTO.toString());
		this.listPersonDTO = this.personManagementService.listPersonDTO(personDTO);		
		if(this.listPersonDTO == null || this.listPersonDTO.isEmpty())
			throw new PmsServiceException("py.com.kyron.sgp.persistence.common.search.no.results.found.error", null, null);
		this.printRetrievedPersonList();
		
		this.listOrderDTO = this.comercialManagementService.listOrderDTO(new OrderDTO(null, this.listPersonDTO.get(0).getId()));
		if(this.listOrderDTO == null || this.listOrderDTO.isEmpty())
			throw new PmsServiceException("py.com.kyron.sgp.persistence.common.search.no.results.found.error", null, null);		
		this.printRetrievedOrderList();
		
		this.searchOrderToolComponentHostView.buildOrderDTOTable(this.listOrderDTO);
    }
    
	private Long checkPersonalCivilIdDocument(){
		try{
			return Long.parseLong(this.filterDTO.getText());
		}catch(Exception e){
			logger.info("\n SearchOrderToolComponent.checkPersonalCivilIdDocument\nfilterText : " + this.filterDTO.getText()
					+ "not a Personal Civil Id Document");
		}
		return null;
	}
	
	private void printRetrievedPersonList(){
		for(PersonDTO personDTO: this.listPersonDTO){
			logger.info("\nprintRetrievedPersonList : "+personDTO.toString());
		}			
	}

	private void printRetrievedOrderList(){
		for(OrderDTO vOrderDTO: this.listOrderDTO){
			logger.info("\n order : "+vOrderDTO.toString());
		}			
	}
	
    private void goToRegisterOrderView(OrderDTO vOrderDTO){
		try{
			if(vOrderDTO == null)vOrderDTO = new OrderDTO();
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.REGISTER_ORDER_FORM.getViewName());
			DashboardEventBus.post(new OrderRegisterFormViewEvent(
					vOrderDTO, 
					DashboardViewType.ORDER_MANAGEMENT.getViewName(),
					vOrderDTO!=null && vOrderDTO.getId()!=null,
					true));
		}catch(Exception e){
			logger.error("\nerror: ",e);
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
}
