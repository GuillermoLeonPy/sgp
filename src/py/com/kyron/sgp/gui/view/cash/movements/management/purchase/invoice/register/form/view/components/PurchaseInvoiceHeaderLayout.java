package py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyExchangeRateDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class PurchaseInvoiceHeaderLayout extends HorizontalLayout {

	private final Logger logger = LoggerFactory.getLogger(PurchaseInvoiceHeaderLayout.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final PurchaseInvoiceDTO purchaseInvoiceDTO;
	//private Label identifier_numberLabel;
	private TextField identifier_numberValueTextField;
	//private Label stamping_numberLabel;
	private TextField stamping_numberValueTextField;
	private DateField stamping_number_start_validity_DateField;
	private DateField emission_date_DateField;
	private ComboBox listCurrencyDTOComboBox;
	private OptionGroup paymentConditionOptionGroup;
	//private Label credit_purchase_fee_quantityLabel;
	private TextField credit_purchase_fee_quantityValueTextField;	
	//private Label credit_purchase_fee_periodicity_days_quantityLabel;
	private TextField credit_purchase_fee_periodicity_days_quantityValueTextField;
	private DateField credit_purchase_first_payment_fee_date_DateField;
	private SgpForm<PurchaseInvoiceDTO> purchaseInvoiceDTOCreditCriteriaForm;
	private BussinesSessionUtils bussinesSessionUtils;
	private SgpUtils sgpUtils;
	private VerticalLayout column01;
	private VerticalLayout column02;
	private VerticalLayout column03;
	private VerticalLayout column04;
	private VerticalLayout column05;
	private VerticalLayout column06;
	private SgpForm<PurchaseInvoiceDTO> purchaseInvoiceDTOForm;
	private ComercialManagementService comercialManagementService;
	private Label rucLabel;
	private Label rucValue;
	private Label commercialNameLabel;
	private Label commercialNameValue;
	private Button searchProviderButton;
	private final PurchaseInvoiceHeaderLayoutFunctions purchaseInvoiceHeaderLayoutFunctions;
	private HorizontalLayout rucHorizontalLayout;
	private HorizontalLayout commercialNameHorizontalLayout;
	private HorizontalLayout credit_purchase_fee_quantityHorizontalLayout;
	private HorizontalLayout credit_purchase_fee_periodicity_days_quantityHorizontalLayout;
	private HorizontalLayout credit_purchase_first_payment_fee_date_DateFieldHorizontalLayout;
	
	public PurchaseInvoiceHeaderLayout(final String VIEW_NAME,final PurchaseInvoiceHeaderLayoutFunctions purchaseInvoiceHeaderLayoutFunctions,final PurchaseInvoiceDTO purchaseInvoiceDTO) {
		// TODO Auto-generated constructor stub
		this.VIEW_NAME = VIEW_NAME;
		this.purchaseInvoiceHeaderLayoutFunctions = purchaseInvoiceHeaderLayoutFunctions;
		this.purchaseInvoiceDTO = purchaseInvoiceDTO;
		try{
			logger.info("\n SaleInvoiceHeaderLayout()...");
			this.initServices();
			this.setLocale(this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale());
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.sgpUtils = new SgpUtils();
	        //this.setSpacing(true);
			this.setMargin(new MarginInfo(true, true, false, true));
	        Responsive.makeResponsive(this);
	        this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public PurchaseInvoiceHeaderLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/

	private void initServices() throws Exception{
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		this.comercialManagementService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
	}
	
	private void setUpLayoutContent() throws PmsServiceException{
		this.column01 = new VerticalLayout();
		this.column01.setMargin(new MarginInfo(false, true, false, false));
		this.column02 = new VerticalLayout();
		this.column02.setMargin(new MarginInfo(false, true, false, false));
		this.column03 = new VerticalLayout();
		this.column03.setMargin(new MarginInfo(false, true, false, false));
		this.column04 = new VerticalLayout();
		this.column04.setMargin(new MarginInfo(false, true, false, false));
		this.column05 = new VerticalLayout();
		this.column05.setMargin(new MarginInfo(false, true, false, false));
		this.column06 = new VerticalLayout();
		this.column06.setMargin(new MarginInfo(false, true, false, false));
		this.initForm();
		this.setUpRow00();
		this.setUpRow01();
		this.setUpCreditCriteriaFields();
		this.addComponents(this.column01,this.column02,this.column03,this.column04,this.column05,this.column06);
	}
	
	private void initForm(){
		this.purchaseInvoiceDTOForm = new SgpForm<PurchaseInvoiceDTO>(PurchaseInvoiceDTO.class,new BeanItem<PurchaseInvoiceDTO>(this.purchaseInvoiceDTO),10,1);		
	}
	
	private void setUpRow00(){
		this.setRucAndComercialNameValues();

		this.setUpSearchProviderButton();
		final HorizontalLayout searchProviderButtonHorizontalLayout = new HorizontalLayout(this.searchProviderButton);
		searchProviderButtonHorizontalLayout.setSpacing(true);
		searchProviderButtonHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.column01.addComponent(searchProviderButtonHorizontalLayout);
		
		this.rucLabel = new Label(this.messages.get("application.common.ruc.indicator.label"));
		this.rucLabel.addStyleName(ValoTheme.LABEL_COLORED);
		this.rucHorizontalLayout = new HorizontalLayout(rucLabel,rucValue);
		this.rucHorizontalLayout.setSpacing(true);
		this.rucHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.column02.addComponent(this.rucHorizontalLayout);
		
		this.commercialNameLabel = new Label(this.messages.get("application.common.commercial.name.label"));
		this.commercialNameLabel.addStyleName(ValoTheme.LABEL_COLORED);
		this.commercialNameHorizontalLayout = new HorizontalLayout(commercialNameLabel,commercialNameValue);
		this.commercialNameHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.commercialNameHorizontalLayout.setSpacing(true);
		this.column03.addComponent(this.commercialNameHorizontalLayout);
				
		//this.identifier_numberLabel = new Label(this.messages.get("application.common.sale.invoice.number.indicator.label"));
		//this.identifier_numberLabel.addStyleName(ValoTheme.LABEL_COLORED);
		this.identifier_numberValueTextField = this.purchaseInvoiceDTOForm.bindAndBuildTextFieldWithExplicitValidateBlurListener("identifier_number", ""/*caption*/, /*immediate*/true, 10, true/*required*/, this.messages.get( this.VIEW_NAME + "tab.purchase.invoice.form.text.field.identifier.number.required.message")/*required message*/,false);
		this.identifier_numberValueTextField.setCaption(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.label.identifier.number"));
		
		final HorizontalLayout identifier_numberHorizontalLayout = new HorizontalLayout(/*identifier_numberLabel,*/identifier_numberValueTextField);
		identifier_numberHorizontalLayout.setSpacing(true);
		identifier_numberHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.column04.addComponent(identifier_numberHorizontalLayout);
		
		//this.stamping_numberLabel = new Label(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.label.stamping.number"));
		//this.stamping_numberLabel.addStyleName(ValoTheme.LABEL_COLORED);
		this.stamping_numberValueTextField = this.purchaseInvoiceDTOForm.bindAndBuildTextFieldWithExplicitValidateBlurListener("stamping_number", ""/*caption*/, /*immediate*/true, 10, true/*required*/, this.messages.get( this.VIEW_NAME + "tab.purchase.invoice.form.text.field.stamping.number.required.message")/*required message*/,false);
		this.stamping_numberValueTextField.setCaption(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.label.stamping.number"));
		final HorizontalLayout stamping_numberHorizontalLayout = new HorizontalLayout(/*stamping_numberLabel,*/stamping_numberValueTextField);
		stamping_numberHorizontalLayout.setSpacing(true);
		stamping_numberHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.column05.addComponent(stamping_numberHorizontalLayout);
		
		this.stamping_number_start_validity_DateField = this.purchaseInvoiceDTOForm.bindAndBuildDateField("stamping_number_start_validity_date", this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.date.field.stamping.number.start.validity.date")/*caption*/, /*immediate*/true, 10, true/*required*/, this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.date.field.stamping.number.start.validity.date.required.message")/*required message*/,false);
		final HorizontalLayout stamping_number_start_validity_DateHorizontalLayout = new HorizontalLayout(stamping_number_start_validity_DateField);
		stamping_number_start_validity_DateHorizontalLayout.setSpacing(true);
		stamping_number_start_validity_DateHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.column06.addComponent(stamping_number_start_validity_DateHorizontalLayout);
	}
	
	private void setRucAndComercialNameValues(){
		final ObjectProperty<String> rucValueProperty =	new ObjectProperty<String>(this.purchaseInvoiceDTO.getBussines_ci_ruc() == null ? "" : this.purchaseInvoiceDTO.getBussines_ci_ruc());
		this.rucValue = new Label(rucValueProperty);
		final ObjectProperty<String> ommercialNameValueProperty =	new ObjectProperty<String>(this.purchaseInvoiceDTO.getBussines_name() == null ? "" : this.purchaseInvoiceDTO.getBussines_name());
		this.commercialNameValue = new Label(ommercialNameValueProperty);
	}
	
	public void refreshRucAndComercialNameValues(){
		this.rucHorizontalLayout.removeComponent(this.rucValue);
		this.commercialNameHorizontalLayout.removeComponent(this.commercialNameValue);
		this.purchaseInvoiceDTO.setBussines_ci_ruc(this.purchaseInvoiceDTO.getPersonDTO().getRuc());
		this.purchaseInvoiceDTO.setBussines_name(this.purchaseInvoiceDTO.getPersonDTO().getCommercial_name());
		this.setRucAndComercialNameValues();
		this.rucHorizontalLayout.addComponent(this.rucValue);
		this.commercialNameHorizontalLayout.addComponent(this.commercialNameValue);

		
	}
	
	
	private void setUpRow01() throws PmsServiceException{
		this.emission_date_DateField = this.purchaseInvoiceDTOForm.bindAndBuildDateField("emission_date", this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.date.field.emission.date")/*caption*/, /*immediate*/true, 10, true/*required*/, this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.date.field.emission.date.required.message")/*required message*/,false);
		final HorizontalLayout emission_date_DateFieldHorizontalLayout = new HorizontalLayout(emission_date_DateField);
		emission_date_DateFieldHorizontalLayout.setSpacing(true);
		emission_date_DateFieldHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.column02.addComponent(emission_date_DateFieldHorizontalLayout);
		
		if(this.purchaseInvoiceDTO.getId()!=null){
			final Label statusLabelIndicator = new Label(this.messages.get("application.common.status.label") + ": ");
			statusLabelIndicator.addStyleName(ValoTheme.LABEL_COLORED);
			final Label statusLabelValue = new Label(this.messages.get(this.purchaseInvoiceDTO.getStatus()));
			final HorizontalLayout statusHorizontalLayout = new HorizontalLayout(statusLabelIndicator,statusLabelValue);
			statusHorizontalLayout.setSpacing(true);
			this.column02.addComponent(statusHorizontalLayout);
		}
		
		this.setUpCurrencyDTOComboBox();
		final HorizontalLayout currencyDTOComboBoxHorizontalLayout = new HorizontalLayout(this.listCurrencyDTOComboBox);
		currencyDTOComboBoxHorizontalLayout.setSpacing(true);
		currencyDTOComboBoxHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.column03.addComponent(currencyDTOComboBoxHorizontalLayout);		
		
		this.setUpPaymentConditionOptionGroup();
		final HorizontalLayout paymentConditionOptionGroupHorizontalLayout = new HorizontalLayout(this.paymentConditionOptionGroup);
		paymentConditionOptionGroupHorizontalLayout.setSpacing(true);
		paymentConditionOptionGroupHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.column04.addComponent(paymentConditionOptionGroupHorizontalLayout);
	}	

	private void setUpCreditCriteriaFields(){
		this.setUpCreditCriteriaForm();
		
		//this.credit_purchase_fee_quantityLabel = new Label(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.text.field.credit.order.fee.quantity.label"));
		//this.credit_purchase_fee_quantityLabel.addStyleName(ValoTheme.LABEL_COLORED);
		this.credit_purchase_fee_quantityHorizontalLayout = new HorizontalLayout(/*credit_purchase_fee_quantityLabel,*/);
		this.credit_purchase_fee_quantityHorizontalLayout.setSpacing(true);
		this.credit_purchase_fee_quantityHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.column05.addComponent(this.credit_purchase_fee_quantityHorizontalLayout);
		
		//this.credit_purchase_fee_periodicity_days_quantityLabel = new Label(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.text.field.credit.order.fee.periodicity.days.quantity.label"));
		//this.credit_purchase_fee_periodicity_days_quantityLabel.addStyleName(ValoTheme.LABEL_COLORED);
		this.credit_purchase_fee_periodicity_days_quantityValueTextField.setCaption(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.text.field.credit.order.fee.periodicity.days.quantity.label"));
		this.credit_purchase_fee_periodicity_days_quantityHorizontalLayout = new HorizontalLayout(/*credit_purchase_fee_periodicity_days_quantityLabel,*/);
		this.credit_purchase_fee_periodicity_days_quantityHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.column06.addComponent(this.credit_purchase_fee_periodicity_days_quantityHorizontalLayout);
		this.credit_purchase_first_payment_fee_date_DateFieldHorizontalLayout = new HorizontalLayout();
		this.credit_purchase_first_payment_fee_date_DateFieldHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));		
		this.column06.addComponent(this.credit_purchase_first_payment_fee_date_DateFieldHorizontalLayout);
		if(this.purchaseInvoiceDTO.getPayment_condition().equals("application.common.payment.condition.credit"))this.addRemoveCreditOrderCriteriaFields(true);
	}
	
	private void setUpCreditCriteriaForm(){
		this.purchaseInvoiceDTOCreditCriteriaForm = new SgpForm<PurchaseInvoiceDTO>(PurchaseInvoiceDTO.class,new BeanItem<PurchaseInvoiceDTO>(this.purchaseInvoiceDTO),10,1);
		this.credit_purchase_fee_quantityValueTextField = this.purchaseInvoiceDTOCreditCriteriaForm.bindAndBuildTextFieldWithExplicitValidateBlurListener("credit_purchase_fee_quantity", ""/*caption*/, /*immediate*/true, 10, true/*required*/, this.messages.get( this.VIEW_NAME + "tab.purchase.invoice.form.text.field.credit.order.fee.quantity.required.message")/*required message*/,false);
		this.credit_purchase_fee_quantityValueTextField.setCaption(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.text.field.credit.order.fee.quantity.label"));
		this.credit_purchase_fee_periodicity_days_quantityValueTextField = this.purchaseInvoiceDTOCreditCriteriaForm.bindAndBuildTextFieldWithExplicitValidateBlurListener("credit_purchase_fee_periodicity_days_quantity", ""/*caption*/, /*immediate*/true, 10, true/*required*/, this.messages.get( this.VIEW_NAME + "tab.purchase.invoice.form.text.field.credit.order.fee.periodicity.days.quantity.required.message")/*required message*/,false);
		this.credit_purchase_fee_periodicity_days_quantityValueTextField.setCaption(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.text.field.credit.order.fee.periodicity.days.quantity.label"));
		this.credit_purchase_first_payment_fee_date_DateField = this.purchaseInvoiceDTOCreditCriteriaForm.bindAndBuildDateField("credit_purchase_first_payment_fee_date", this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.label.credit.purchase.first.payment.fee.date")/*caption*/, /*immediate*/true, 10, true/*required*/, this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.date.field.credit.purchase.first.payment.fee.date.required.message")/*required message*/,false);
	}
	
	private void setUpCurrencyDTOComboBox() throws PmsServiceException{
		this.listCurrencyDTOComboBox = new ComboBox(this.messages.get("application.common.currency.label"));
		this.listCurrencyDTOComboBox.setInputPrompt(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.combo.box.currency.input.prompt"));
        BeanItemContainer<CurrencyDTO> vCurrencyDTOBeanItemContainer = new BeanItemContainer<CurrencyDTO>(CurrencyDTO.class);
        vCurrencyDTOBeanItemContainer.addAll(this.determinateCurrencyDTOList());
        this.listCurrencyDTOComboBox.setContainerDataSource(vCurrencyDTOBeanItemContainer);
        this.listCurrencyDTOComboBox.setItemCaptionPropertyId("id_code");
        
        if(this.purchaseInvoiceDTO.getId_currency()!=null){
        	this.listCurrencyDTOComboBox.select(this.purchaseInvoiceDTO.getCurrencyDTO());
        	this.listCurrencyDTOComboBox.setValue(this.purchaseInvoiceDTO.getCurrencyDTO());
        }else{
        	this.listCurrencyDTOComboBox.select(vCurrencyDTOBeanItemContainer.getIdByIndex(0));
        	this.purchaseInvoiceDTO.setId_currency(vCurrencyDTOBeanItemContainer.getIdByIndex(0).getId());
        }
        this.listCurrencyDTOComboBox.setNullSelectionAllowed(false);
        this.listCurrencyDTOComboBox.addStyleName("small");
        this.listCurrencyDTOComboBox.addValueChangeListener(this.setUpValueChangeListenerCurrencyDTOComboBox());
        this.listCurrencyDTOComboBox.setRequired(true);
        this.listCurrencyDTOComboBox.setRequiredError(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.combo.box.currency.required.message"));
        this.listCurrencyDTOComboBox.setWidth(35, Unit.PERCENTAGE);
               
	}
	
	private List<CurrencyDTO> determinateCurrencyDTOList() throws PmsServiceException{
		List<CurrencyExchangeRateDTO> listCurrencyExchangeRateDTO = this.comercialManagementService.listCurrencyExchangeRateDTO(new CurrencyExchangeRateDTO(true));
		List<CurrencyDTO> currencyDTOList = new ArrayList<CurrencyDTO>();
		for(CurrencyExchangeRateDTO vCurrencyExchangeRateDTO:listCurrencyExchangeRateDTO){
			currencyDTOList.add(this.comercialManagementService.listCurrencyDTO(new CurrencyDTO(vCurrencyExchangeRateDTO.getId_currency())).get(0));
		}
		currencyDTOList.add(0, this.comercialManagementService.listCurrencyDTO(new CurrencyDTO(true)).get(0));
		return currencyDTOList;
	}
	
	private Property.ValueChangeListener setUpValueChangeListenerCurrencyDTOComboBox(){
    	return new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				try{
					logger.info("\n CurrencyDTO combo box: value change event"
								+"\n======================================"	+"\nevent.getProperty().getType().getName()) : " 
								+ event.getProperty().getType().getName()
								+"\nevent.getProperty().getValue() : \n" + event.getProperty().getValue());
					purchaseInvoiceDTO.setId_currency(((CurrencyDTO)event.getProperty().getValue()).getId());
				}catch(Exception e){
					logger.error("\n error", e);
				}
			}
    	};
	}
	
	private void setUpPaymentConditionOptionGroup(){
		/*logger.info("\n ============================== "
				+	"\n this.purchaseInvoiceDTO.getPayment_condition() : " + this.purchaseInvoiceDTO.getPayment_condition()
				+	"\n ============================== ");*/
		List<SelectOneOption> listSelectOneOption = new ArrayList<SelectOneOption>();
		listSelectOneOption.add(
		new SelectOneOption("application.common.payment.condition.cash",this.messages.get("application.common.payment.condition.cash"))
		);
		listSelectOneOption.add(
		new SelectOneOption("application.common.payment.condition.credit",this.messages.get("application.common.payment.condition.credit"))
		);		
		
    	this.paymentConditionOptionGroup = new OptionGroup();//
    	this.paymentConditionOptionGroup.addStyleName("small");
    	this.paymentConditionOptionGroup.setMultiSelect(false);
    	this.paymentConditionOptionGroup.setCaption(this.messages.get("application.common.payment.condition.selector.description"));
    	this.paymentConditionOptionGroup.setDescription(this.messages.get("application.common.payment.condition.selector.description"));
    	BeanItemContainer<SelectOneOption> vSelectOneOptionBeanItemContainer = new BeanItemContainer<SelectOneOption>(SelectOneOption.class);
    	vSelectOneOptionBeanItemContainer.addAll(listSelectOneOption);
    	this.paymentConditionOptionGroup.setContainerDataSource(vSelectOneOptionBeanItemContainer);
    	if(this.purchaseInvoiceDTO.getPayment_condition()!=null){
    		SelectOneOption vSelectOneOption = new SelectOneOption(this.purchaseInvoiceDTO.getPayment_condition(),null);
    		/*BeanItem<SelectOneOption> vSelectOneOptionBeanItem = 
    				vSelectOneOptionBeanItemContainer.getItem(new SelectOneOption(this.purchaseInvoiceDTO.getPayment_condition(),null));*/
    		/*SelectOneOption vSelectOneOption = listSelectOneOption.get(listSelectOneOption.indexOf(new SelectOneOption(this.purchaseInvoiceDTO.getPayment_condition(),null)));*/
    		/*logger.info("\n ============================== "
    				+	"\n vSelectOneOption : " + vSelectOneOption
    				+	"\n ============================== ");*/
    		
    		/* DOUBLE CHECK DE EQUALS METHOD OF THE BEAN !!!!!!! */
    		
    		this.paymentConditionOptionGroup.select(vSelectOneOption);
    		this.paymentConditionOptionGroup.setValue(vSelectOneOption);
    	}else{
    		this.paymentConditionOptionGroup.select(new SelectOneOption("application.common.payment.condition.cash",this.messages.get("application.common.payment.condition.cash")));
    		this.purchaseInvoiceDTO.setPayment_condition("application.common.payment.condition.cash");
    	}
    	this.paymentConditionOptionGroup.setItemCaptionPropertyId("value");
    	this.paymentConditionOptionGroup.setNullSelectionAllowed(false);
    	this.paymentConditionOptionGroup.addValueChangeListener(this.setUpValueChangeListenerForSelectOptionGroup());    	
	}
	
    private Property.ValueChangeListener setUpValueChangeListenerForSelectOptionGroup(){
    	return new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				try{
					if(((SelectOneOption)event.getProperty().getValue()).getKey().equals("application.common.payment.condition.credit")){
						addRemoveCreditOrderCriteriaFields(true);
						purchaseInvoiceDTO.setPayment_condition("application.common.payment.condition.credit");
					}else{
						discardCreditOrderCriteriaFieldsValues();
						addRemoveCreditOrderCriteriaFields(false);
						purchaseInvoiceDTO.setPayment_condition("application.common.payment.condition.cash");
					}										
				}catch(Exception e){
					logger.error("\n error", e);
				}
			}//public void valueChange(ValueChangeEvent event)
        };//return new Property.ValueChangeListener() 
    }
    
    private void addRemoveCreditOrderCriteriaFields(boolean add){
    	//this.credit_purchase_fee_quantityLabel.setVisible(visible);
    	if(add){
    		logger.info(" \n ============================================== "
    					+	"adding credit criteria fields"
						+"\n ============================================== ");
    		this.credit_purchase_fee_quantityHorizontalLayout.addComponent(this.credit_purchase_fee_quantityValueTextField);
    		//this.credit_purchase_fee_periodicity_days_quantityLabel.setVisible(visible);
    		this.credit_purchase_fee_periodicity_days_quantityHorizontalLayout.addComponent(this.credit_purchase_fee_periodicity_days_quantityValueTextField);
    		this.credit_purchase_first_payment_fee_date_DateFieldHorizontalLayout.addComponent(this.credit_purchase_first_payment_fee_date_DateField);
    	}else{
    		logger.info(" \n ============================================== "
    					+	"removing credit criteria fields"
    					+"\n ============================================== ");
    		//this.credit_purchase_fee_quantityHorizontalLayout.removeComponent(this.credit_purchase_fee_quantityValueTextField);
    		this.credit_purchase_fee_quantityHorizontalLayout.removeAllComponents();
    		this.credit_purchase_fee_quantityHorizontalLayout.markAsDirty();
    		//this.credit_purchase_fee_periodicity_days_quantityHorizontalLayout.removeComponent(this.credit_purchase_fee_periodicity_days_quantityValueTextField);
    		this.credit_purchase_fee_periodicity_days_quantityHorizontalLayout.removeAllComponents();
    		this.credit_purchase_fee_periodicity_days_quantityHorizontalLayout.markAsDirty();
    		this.credit_purchase_first_payment_fee_date_DateFieldHorizontalLayout.removeAllComponents();
    		this.credit_purchase_first_payment_fee_date_DateFieldHorizontalLayout.markAsDirty();
    	}
    }
    
    private void discardCreditOrderCriteriaFieldsValues(){
    	this.purchaseInvoiceDTOCreditCriteriaForm.discard();
    	this.credit_purchase_fee_quantityValueTextField.discard();
    	this.credit_purchase_fee_periodicity_days_quantityValueTextField.discard();
    	this.credit_purchase_first_payment_fee_date_DateField.discard();
    	this.purchaseInvoiceDTO.setCredit_purchase_fee_quantity(null);
    	this.purchaseInvoiceDTO.setCredit_purchase_fee_periodicity_days_quantity(null);
    	this.purchaseInvoiceDTO.setCredit_purchase_first_payment_fee_date(null);
    	this.setUpCreditCriteriaForm();
    }
    
    public void commitFormsValues() throws CommitException{    	
    	this.listCurrencyDTOComboBox.commit();
    	this.paymentConditionOptionGroup.commit();
    	this.purchaseInvoiceDTOForm.commit();
    	if(this.purchaseInvoiceDTO.getPayment_condition().equals("application.common.payment.condition.credit"))
    		this.purchaseInvoiceDTOCreditCriteriaForm.commit();
    	else this.purchaseInvoiceDTOCreditCriteriaForm.discard();
    }
    
    public void discardFormsValues(){
    	this.listCurrencyDTOComboBox.discard();
    	this.paymentConditionOptionGroup.discard();
    	this.purchaseInvoiceDTOForm.discard();
    	this.purchaseInvoiceDTOCreditCriteriaForm.discard();
    	this.discardCreditOrderCriteriaFieldsValues();
    }
    
    private void setUpSearchProviderButton(){
		this.searchProviderButton = new Button(FontAwesome.SEARCH_PLUS);
		this.searchProviderButton.setDescription(this.messages.get(VIEW_NAME + "tab.purchase.invoice.form.button.search.provider.description"));
		this.searchProviderButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                try{
                	purchaseInvoiceHeaderLayoutFunctions.searchProvider();
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }
            }
        });
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

		private PurchaseInvoiceHeaderLayout getOuterType() {
			return PurchaseInvoiceHeaderLayout.this;
		}

	}
	
	
}
