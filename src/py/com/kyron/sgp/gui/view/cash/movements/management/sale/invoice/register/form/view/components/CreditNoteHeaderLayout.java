package py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.register.form.view.components;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class CreditNoteHeaderLayout extends HorizontalLayout {

	private final Logger logger = LoggerFactory.getLogger(CreditNoteHeaderLayout.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final CreditNoteDTO creditNoteDTO;
	private Label creditNoteNumberLabel;
	private Label creditNoteNumberIndicatorLabel;
	private Label saleInvoiceNumberIndicatorLabel;
	private Label saleInvoiceNumberLabel;
	private Label statusLabel;
	private Label statusValue;
	private Label emissionDateLabel;
	private Label emissionDateValue;
	private BussinesSessionUtils bussinesSessionUtils;
	private Label rucLabel;
	private Label rucValue;
	private Label commercialNameLabel;
	private Label commercialNameValue;
	private SgpUtils sgpUtils;
	private Label currencyLabel;
	private Label currencyValue;
	private VerticalLayout column01;
	private VerticalLayout column02;
	private VerticalLayout column03;
	
	public CreditNoteHeaderLayout(final String VIEW_NAME,final CreditNoteDTO creditNoteDTO) {
		// TODO Auto-generated constructor stub
		this.VIEW_NAME =VIEW_NAME;
		this.creditNoteDTO = creditNoteDTO;
		try{
			logger.info("\n CreditNoteHeaderLayout()...");
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

	/*public CreditNoteHeaderLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/

	
	private void initServices() throws Exception{
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}

	/*public SaleInvoiceHeaderLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	
	private void setUpLayoutContent(){
		this.column01 = new VerticalLayout();
		this.column01.setMargin(new MarginInfo(false, true, false, false));
		this.column02 = new VerticalLayout();
		this.column02.setMargin(new MarginInfo(false, true, false, false));
		this.column03 = new VerticalLayout();
		this.column03.setMargin(new MarginInfo(false, true, false, false));
		this.setUpInvoiceNumberStatusOrderNumber();
		this.setUpEmissionDatePaymentConditionCurrency();
		this.setUpRucAndCommercialName();
		this.addComponents(this.column01,this.column02,this.column03);
		
	}
	
	private void setUpInvoiceNumberStatusOrderNumber(){
		this.creditNoteNumberLabel = new Label(this.messages.get("application.common.number.indicator.label"));
		this.creditNoteNumberLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> property =	new ObjectProperty<String>(this.creditNoteDTO.getIdentifier_number());
		this.creditNoteNumberIndicatorLabel = new Label(property);
		HorizontalLayout vHorizontalLayout = new HorizontalLayout(this.creditNoteNumberLabel,this.creditNoteNumberIndicatorLabel);
		vHorizontalLayout.setSpacing(true);
		this.column01.addComponent(vHorizontalLayout);
		
		this.statusLabel = new Label(this.messages.get("application.common.status.label"));
		this.statusLabel.addStyleName(ValoTheme.LABEL_COLORED);
		this.statusValue = new Label(this.messages.get(this.creditNoteDTO.getStatus()));
		HorizontalLayout statusHorizontalLayout = new HorizontalLayout(this.statusLabel,this.statusValue);
		statusHorizontalLayout.setSpacing(true);
		this.column02.addComponent(statusHorizontalLayout);
		
		this.saleInvoiceNumberLabel = new Label(this.messages.get("application.common.sale.invoice.number.indicator.label"));
		this.saleInvoiceNumberLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> saleInvoiceNumberProperty =	new ObjectProperty<String>(this.creditNoteDTO.getModified_documens_identifier_numbers());
		this.saleInvoiceNumberIndicatorLabel = new Label(saleInvoiceNumberProperty);
		HorizontalLayout orderNumberHorizontalLayout = new HorizontalLayout(this.saleInvoiceNumberLabel,this.saleInvoiceNumberIndicatorLabel);
		orderNumberHorizontalLayout.setSpacing(true);
		this.column03.addComponent(orderNumberHorizontalLayout);
	}
	
	private void setUpEmissionDatePaymentConditionCurrency(){
		this.emissionDateLabel = new Label(this.messages.get("application.common.emission.date.indicator.label"));
		this.emissionDateLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> property =	new ObjectProperty<String>(SgpUtils.parseDateParamBySessionLocale(this.creditNoteDTO.getEmission_date(), this.getLocale()));
		this.emissionDateValue = new Label(property);
		HorizontalLayout vHorizontalLayout = new HorizontalLayout(this.emissionDateLabel,this.emissionDateValue);
		vHorizontalLayout.setSpacing(true);
		this.column01.addComponent(vHorizontalLayout);
		
		/* currency */
		this.currencyLabel = new Label(this.messages.get("application.common.currency.label"));
		this.currencyLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> currencyProperty =	new ObjectProperty<String>(this.creditNoteDTO.getCurrencyDTO().getId_code());
		this.currencyValue = new Label(currencyProperty);
		HorizontalLayout currencyHorizontalLayout = new HorizontalLayout(this.currencyLabel,this.currencyValue);
		currencyHorizontalLayout.setSpacing(true); 
		this.column03.addComponent(currencyHorizontalLayout);
	}
	
	private void setUpRucAndCommercialName(){
		this.rucLabel = new Label(this.messages.get("application.common.ruc.indicator.label"));
		this.rucLabel.addStyleName(ValoTheme.LABEL_COLORED);		
		this.rucValue = new Label(this.determinateObjectPropertyForRuc(this.creditNoteDTO.getBussines_ci_ruc()));
		HorizontalLayout vHorizontalLayout = new HorizontalLayout(this.rucLabel,this.rucValue);
		vHorizontalLayout.setSpacing(true);
		this.column01.addComponent(vHorizontalLayout);
		
		this.commercialNameLabel = new Label(this.messages.get("application.common.commercial.name.label"));
		this.commercialNameLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> comercialNameProperty =	new ObjectProperty<String>(this.creditNoteDTO.getBussines_name());
		this.commercialNameValue = new Label(comercialNameProperty);
		HorizontalLayout comercialNameHorizontalLayout = new HorizontalLayout(this.commercialNameLabel,this.commercialNameValue);
		comercialNameHorizontalLayout.setSpacing(true);	
		this.column02.addComponent(comercialNameHorizontalLayout);
		
	}
	
	private ObjectProperty<?> determinateObjectPropertyForRuc(String ciRuc){
		if(this.sgpUtils.checkPersonalCivilIdDocument(ciRuc)!=null)
			return new ObjectProperty<Long>(this.sgpUtils.checkPersonalCivilIdDocument(ciRuc));
		else
			return new ObjectProperty<String>(ciRuc);
	}
}
