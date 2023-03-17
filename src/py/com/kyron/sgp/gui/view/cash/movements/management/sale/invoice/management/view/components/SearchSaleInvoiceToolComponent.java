package py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.management.view.components;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.service.CashMovementsManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.personmanagement.service.PersonManagementService;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;

import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SearchSaleInvoiceToolComponent extends HorizontalLayout {

	private final Logger logger = LoggerFactory.getLogger(SearchSaleInvoiceToolComponent.class);
	private Label title;
	private TextField filter;
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private SgpForm<FilterDTO> filterForm;
	private FilterDTO filterDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final SearchSaleInvoiceToolComponentHostView searchSaleInvoiceToolComponentHostView;
	private final boolean generateSaleInvoiceButtonVisible;
	private List<SaleInvoiceDTO> listSaleInvoiceDTO;
	private CashMovementsManagementService cashMovementsManagementService;
    private PersonDTO personDTO;
    private List<PersonDTO> listPersonDTO;
    private PersonManagementService personManagementService;
	
	public SearchSaleInvoiceToolComponent(
			final SearchSaleInvoiceToolComponentHostView searchSaleInvoiceToolComponentHostView,
			String VIEW_NAME,boolean generateSaleInvoiceButtonVisible) {
		// TODO Auto-generated constructor stub
		this.searchSaleInvoiceToolComponentHostView = searchSaleInvoiceToolComponentHostView;
		this.VIEW_NAME = VIEW_NAME;
		this.generateSaleInvoiceButtonVisible = generateSaleInvoiceButtonVisible;
		try{
			logger.info("\n SearchSaleInvoiceToolComponent()...");
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

	/*public SearchSaleInvoiceToolComponent(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	private void initServices() throws Exception{
		this.cashMovementsManagementService = (CashMovementsManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.CASH_MOVEMENTS_MANAGEMENT_SERVICE);
		this.personManagementService = (PersonManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PERSON_MANAGEMENT_SERVICE);
	}

	private void initTitles(){
        this.title = new Label(this.messages.get(this.VIEW_NAME + "main.title"));
        this.title.setSizeUndefined();
        this.title.addStyleName(ValoTheme.LABEL_H1);
        this.title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        this.addComponent(new FormLayout(this.title));
	}
	
	private void buildToolBar(){
		HorizontalLayout vHorizontalLayout = 
				new HorizontalLayout(
						new FormLayout(this.buildFilterTextField()),
						new FormLayout(this.createGenerateSaleInvoiceButton()));
		vHorizontalLayout.setSpacing(true);
		vHorizontalLayout.addStyleName("toolbar");
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
	
	
    private Button createGenerateSaleInvoiceButton(){
        final Button generateSaleInvoice = new Button(this.messages.get(this.VIEW_NAME + "button.generate.sale.invoice"));
        generateSaleInvoice.setDescription(this.messages.get(this.VIEW_NAME + "button.generate.sale.invoice.description"));
        generateSaleInvoice.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
            	try{
            		logger.info("\n GenerateSaleInvoiceButton clicked !");
                	goToOrderManagementView();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					commonExceptionErrorNotification.showErrorMessageNotification(e);
				}
            }
        });
        generateSaleInvoice.setEnabled(true);
        generateSaleInvoice.setVisible(this.generateSaleInvoiceButtonVisible);
        return generateSaleInvoice;
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
		
		this.listSaleInvoiceDTO = this.cashMovementsManagementService.listSaleInvoiceDTO(new SaleInvoiceDTO(null, this.listPersonDTO.get(0).getId()));
		if(this.listSaleInvoiceDTO == null || this.listSaleInvoiceDTO.isEmpty())
			throw new PmsServiceException("py.com.kyron.sgp.persistence.common.search.no.results.found.error", null, null);		
		this.printRetrievedOrderList();
		
		this.searchSaleInvoiceToolComponentHostView.buildSaleInvoiceDTOTable(this.listSaleInvoiceDTO);
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
		for(SaleInvoiceDTO vSaleInvoiceDTO: this.listSaleInvoiceDTO){
			logger.info("\n order : "+vSaleInvoiceDTO.toString());
		}			
	}
	
    private void goToOrderManagementView(){
		try{						
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.ORDER_MANAGEMENT.getViewName());
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
