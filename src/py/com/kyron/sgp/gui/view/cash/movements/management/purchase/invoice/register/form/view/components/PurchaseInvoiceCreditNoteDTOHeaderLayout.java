package py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceCreditNoteDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class PurchaseInvoiceCreditNoteDTOHeaderLayout extends HorizontalLayout {

	private final Logger logger = LoggerFactory.getLogger(PurchaseInvoiceCreditNoteDTOHeaderLayout.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final PurchaseInvoiceCreditNoteDTO purchaseInvoiceCreditNoteDTO;
	private final PurchaseInvoiceDTO purchaseInvoiceDTO;
	private TextField identifier_numberValueTextField;
	private TextField stamping_numberValueTextField;
	private DateField stamping_number_start_validity_DateField;
	private DateField emission_date_DateField;
	private SgpForm<PurchaseInvoiceCreditNoteDTO> purchaseInvoiceCreditNoteDTOForm;
	private BussinesSessionUtils bussinesSessionUtils;
	private SgpUtils sgpUtils;
	private VerticalLayout column01;
	private VerticalLayout column02;
	private VerticalLayout column03;
	private VerticalLayout column04;
	private VerticalLayout column05;
	private VerticalLayout column06;
	private ComercialManagementService comercialManagementService;
	private Label rucLabel;
	private Label rucValue;
	private Label commercialNameLabel;
	private Label commercialNameValue;
	
	private Label purchaseInvoiceNumberIndicatorLabel;
	private Label purchaseInvoiceNumberLabel;
	private Label statusLabel;
	private Label statusValue;
	private Label currencyLabel;
	private Label currencyValue;
	
	private HorizontalLayout rucHorizontalLayout;
	private HorizontalLayout commercialNameHorizontalLayout;

	
	public PurchaseInvoiceCreditNoteDTOHeaderLayout(
			final String VIEW_NAME,
			final PurchaseInvoiceCreditNoteDTO purchaseInvoiceCreditNoteDTO,
			final PurchaseInvoiceDTO purchaseInvoiceDTO) {
		// TODO Auto-generated constructor stub
		this.VIEW_NAME = VIEW_NAME;
		this.purchaseInvoiceCreditNoteDTO = purchaseInvoiceCreditNoteDTO;
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

	/*public PurchaseInvoiceCreditNoteDTOHeaderLayout(Component... children) {
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
		this.addComponents(this.column01,this.column02,this.column03,this.column04,this.column05,this.column06);
	}
	
	private void initForm(){
		this.purchaseInvoiceCreditNoteDTOForm = new SgpForm<PurchaseInvoiceCreditNoteDTO>(PurchaseInvoiceCreditNoteDTO.class,new BeanItem<PurchaseInvoiceCreditNoteDTO>(this.purchaseInvoiceCreditNoteDTO),10,1);
	}
	
	private void setUpRow00(){
		this.setRucAndComercialNameValues();		
		this.rucLabel = new Label(this.messages.get("application.common.ruc.indicator.label"));
		this.rucLabel.addStyleName(ValoTheme.LABEL_COLORED);
		this.rucHorizontalLayout = new HorizontalLayout(rucLabel,rucValue);
		this.rucHorizontalLayout.setSpacing(true);
		this.rucHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.column01.addComponent(this.rucHorizontalLayout);
		
		this.commercialNameLabel = new Label(this.messages.get("application.common.commercial.name.label"));
		this.commercialNameLabel.addStyleName(ValoTheme.LABEL_COLORED);
		this.commercialNameHorizontalLayout = new HorizontalLayout(commercialNameLabel,commercialNameValue);
		this.commercialNameHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.commercialNameHorizontalLayout.setSpacing(true);
		this.column02.addComponent(this.commercialNameHorizontalLayout);
				
		//this.identifier_numberLabel = new Label(this.messages.get("application.common.sale.invoice.number.indicator.label"));
		//this.identifier_numberLabel.addStyleName(ValoTheme.LABEL_COLORED);
		this.identifier_numberValueTextField = this.purchaseInvoiceCreditNoteDTOForm.bindAndBuildTextFieldWithExplicitValidateBlurListener("identifier_number", ""/*caption*/, /*immediate*/true, 10, true/*required*/, this.messages.get( this.VIEW_NAME + "tab.purchase.invoice.credit.note.form.text.field.identifier.number.required.message")/*required message*/,false);
		this.identifier_numberValueTextField.setCaption(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.credit.note.form.text.field.identifier.number"));
		
		final HorizontalLayout identifier_numberHorizontalLayout = new HorizontalLayout(/*identifier_numberLabel,*/identifier_numberValueTextField);
		identifier_numberHorizontalLayout.setSpacing(true);
		identifier_numberHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.column03.addComponent(identifier_numberHorizontalLayout);
		
		//this.stamping_numberLabel = new Label(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.label.stamping.number"));
		//this.stamping_numberLabel.addStyleName(ValoTheme.LABEL_COLORED);
		this.stamping_numberValueTextField = this.purchaseInvoiceCreditNoteDTOForm.bindAndBuildTextFieldWithExplicitValidateBlurListener("stamping_number", ""/*caption*/, /*immediate*/true, 10, true/*required*/, this.messages.get( this.VIEW_NAME + "tab.purchase.invoice.form.text.field.stamping.number.required.message")/*required message*/,false);
		this.stamping_numberValueTextField.setCaption(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.label.stamping.number"));
		final HorizontalLayout stamping_numberHorizontalLayout = new HorizontalLayout(/*stamping_numberLabel,*/stamping_numberValueTextField);
		stamping_numberHorizontalLayout.setSpacing(true);
		stamping_numberHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.column04.addComponent(stamping_numberHorizontalLayout);
		
		this.stamping_number_start_validity_DateField = this.purchaseInvoiceCreditNoteDTOForm.bindAndBuildDateField("stamping_number_start_validity_date", this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.date.field.stamping.number.start.validity.date")/*caption*/, /*immediate*/true, 10, true/*required*/, this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.date.field.stamping.number.start.validity.date.required.message")/*required message*/,false);
		final HorizontalLayout stamping_number_start_validity_DateHorizontalLayout = new HorizontalLayout(stamping_number_start_validity_DateField);
		stamping_number_start_validity_DateHorizontalLayout.setSpacing(true);
		stamping_number_start_validity_DateHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.column05.addComponent(stamping_number_start_validity_DateHorizontalLayout);
	}
	
	private void setRucAndComercialNameValues(){
		final ObjectProperty<String> rucValueProperty =	new ObjectProperty<String>(this.purchaseInvoiceCreditNoteDTO.getBussines_ci_ruc() == null ? "" : this.purchaseInvoiceCreditNoteDTO.getBussines_ci_ruc());
		this.rucValue = new Label(rucValueProperty);
		final ObjectProperty<String> ommercialNameValueProperty =	new ObjectProperty<String>(this.purchaseInvoiceCreditNoteDTO.getBussines_name() == null ? "" : this.purchaseInvoiceCreditNoteDTO.getBussines_name());
		this.commercialNameValue = new Label(ommercialNameValueProperty);
	}
	
	private void setUpRow01() throws PmsServiceException{
		this.currencyLabel = new Label(this.messages.get("application.common.currency.label"));
		this.currencyLabel.addStyleName(ValoTheme.LABEL_COLORED);
		this.currencyValue = new Label(this.purchaseInvoiceCreditNoteDTO.getCurrencyDTO().getId_code() + " / " + this.purchaseInvoiceCreditNoteDTO.getCurrencyDTO().getDescription());
		final HorizontalLayout currencyHorizontalLayout = new HorizontalLayout(this.currencyLabel,this.currencyValue);
		currencyHorizontalLayout.setSpacing(true);
		currencyHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.column01.addComponent(currencyHorizontalLayout);
		
		this.emission_date_DateField = this.purchaseInvoiceCreditNoteDTOForm.bindAndBuildDateField("emission_date", this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.date.field.emission.date")/*caption*/, /*immediate*/true, 10, true/*required*/, this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.date.field.emission.date.required.message")/*required message*/,false);
		final HorizontalLayout emission_date_DateFieldHorizontalLayout = new HorizontalLayout(emission_date_DateField);
		emission_date_DateFieldHorizontalLayout.setSpacing(true);
		emission_date_DateFieldHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.column05.addComponent(emission_date_DateFieldHorizontalLayout);		
		
		this.purchaseInvoiceNumberIndicatorLabel = new Label(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.label.identifier.number"));
		this.purchaseInvoiceNumberIndicatorLabel.addStyleName(ValoTheme.LABEL_COLORED);
		this.purchaseInvoiceNumberLabel = new Label(this.purchaseInvoiceDTO.getIdentifier_number());
		final HorizontalLayout purchaseInvoiceNumberHorizontalLayout = new HorizontalLayout(this.purchaseInvoiceNumberIndicatorLabel,this.purchaseInvoiceNumberLabel);
		purchaseInvoiceNumberHorizontalLayout.setSpacing(true);
		purchaseInvoiceNumberHorizontalLayout.setMargin(new MarginInfo(false, false, true, false));
		this.column02.addComponent(purchaseInvoiceNumberHorizontalLayout);
	}
	
    public void commitFormsValues() throws CommitException{    	
    	this.purchaseInvoiceCreditNoteDTOForm.commit();
    }
    
    public void discardFormsValues(){
    	this.purchaseInvoiceCreditNoteDTOForm.discard();
    }
}
