package py.com.kyron.sgp.gui.view.comercialmanagement.registercurrencyformview.components;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyExchangeRateDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.comercialmanagement.RegisterCurrencyFormView;

import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class CurrencyExchangeRateFormTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(CurrencyExchangeRateFormTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "register.currency.form.";
	private ComercialManagementService gestionComercialService;
    private CommonExceptionErrorNotification commonExceptionErrorNotification;
    private boolean currencyExchangeRateFormTabLayoutEditMode;
    private SgpForm<CurrencyExchangeRateDTO> currencyExchangeRateDTOForm;
    private CurrencyExchangeRateDTO currencyExchangeRateDTO;
    private RegisterCurrencyFormView registerCurrencyFormView;
    private CurrencyDTO localCurrencyCurrencyDTO;
    
	public CurrencyExchangeRateFormTabLayout(
			RegisterCurrencyFormView vRegisterCurrencyFormView,
			CurrencyExchangeRateDTO vCurrencyExchangeRateDTO,
			boolean currencyExchangeRateFormTabLayoutEditMode) {
		try{
			logger.info("\n CurrencyExchangeRateFormTabLayout()...");
			this.currencyExchangeRateDTO = vCurrencyExchangeRateDTO;
			this.currencyExchangeRateFormTabLayoutEditMode = currencyExchangeRateFormTabLayoutEditMode;
			this.registerCurrencyFormView = vRegisterCurrencyFormView;			
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.setSpacing(true);
			this.setMargin(true);
	        Responsive.makeResponsive(this);
	        
	        this.initTitlesCurrencyExchangeRateForm();
	        this.setUpLocalCurrencyLabel();
	        this.setUpCurrencyExchangeRateDTOForm();
	        this.setUpOkCancelButtons();
	        this.addComponent(this.currencyExchangeRateDTOForm.getSgpFormLayout());
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	public CurrencyExchangeRateFormTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}
	private void initServices() throws Exception{
		this.gestionComercialService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
	}
	
	private void initTitlesCurrencyExchangeRateForm(){
		Label title = new Label(!this.currencyExchangeRateFormTabLayoutEditMode ? 
				this.messages.get(this.VIEW_NAME + "tab.exchangerate.register.form.main.title.register") : 
					this.messages.get(this.VIEW_NAME + "tab.exchangerate.register.form.main.title.edit"));
        title.addStyleName("h1");
        this.addComponent(title);
	}
	
	private void setUpCurrencyExchangeRateDTOForm() throws PmsServiceException{
		
		if(this.currencyExchangeRateFormTabLayoutEditMode)
			this.printCurrencyExchangeRateDTOToEdit();
		else
			this.currencyExchangeRateDTO = new CurrencyExchangeRateDTO();
		this.currencyExchangeRateDTOForm = new SgpForm<CurrencyExchangeRateDTO>(CurrencyExchangeRateDTO.class, new BeanItem<CurrencyExchangeRateDTO>(this.currencyExchangeRateDTO), "light", true);		
		this.currencyExchangeRateDTOForm.bindAndSetPositionFormLayoutTextField(
				"amount", 
				this.messages.get(this.VIEW_NAME + "tab.exchangerate.register.form.exchangerate.amount.text.field.label")/**/, 
				true, 100, true,
				this.messages.get(this.VIEW_NAME + "tab.exchangerate.register.form.exchangerate.amount.text.field.required.message"),
				!this.currencyExchangeRateFormTabLayoutEditMode);
	}
	
	private void printCurrencyExchangeRateDTOToEdit(){
		logger.info("\n**********\nCurrencyExchangeRateDTOToEdit\n**********\nthis.CurrencyExchangeRateDTO.toString() : \n" + this.currencyExchangeRateDTO.toString());

	}
	
	private void setUpLocalCurrencyLabel() throws PmsServiceException{
		HorizontalLayout vHorizontalLayout = new HorizontalLayout();
		vHorizontalLayout.setSpacing(true);
		//vHorizontalLayout.setMargin(true);
		Label localCurrencyIdLabel = new Label(this.messages.get("application.common.local.currency.label") + ": ");
		localCurrencyIdLabel.addStyleName("bold");
		vHorizontalLayout.addComponent(localCurrencyIdLabel);
		this.localCurrencyCurrencyDTO = this.gestionComercialService.listCurrencyDTO(new CurrencyDTO(null, null, true)).get(0);
		Label localCurrencyLabel = new Label(this.localCurrencyCurrencyDTO.getDescription());
		localCurrencyLabel.addStyleName("colored");
		vHorizontalLayout.addComponent(localCurrencyLabel);
		this.addComponent(vHorizontalLayout);
	}
	
	private void setUpOkCancelButtons(){
    	
        final Button saveButton = new Button(
        !this.currencyExchangeRateFormTabLayoutEditMode ? this.messages.get(this.VIEW_NAME + "tab.exchangerate.register.form.register.exchangerate.button.label")
        : this.messages.get("application.common.set.end.validity.date.button.label")
        		);/*"Guardar"*/
        saveButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		if(!currencyExchangeRateFormTabLayoutEditMode){
		            			currencyExchangeRateDTOForm.commit();
		            			currencyExchangeRateDTO.setId_currency_local(localCurrencyCurrencyDTO.getId());
		            			currencyExchangeRateDTO.setLocalCurrencyDTO(localCurrencyCurrencyDTO);
		            		}
		            		logger.info("\ncurrencyExchangeRateDTO.toString():\n" + currencyExchangeRateDTO.toString());		            		
		            		registerCurrencyFormView.saveButtonActionCurrencyExchangeRateDTOFormTab(currencyExchangeRateDTO, currencyExchangeRateFormTabLayoutEditMode);
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
		            		currencyExchangeRateDTOForm.discard();
		            		logger.info("\n when click in cancel button; go to cost table tab");
		            		registerCurrencyFormView.updateStatusCurrencyExchangeRateDTOFormTab();
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
		this.currencyExchangeRateDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);
	}
}
