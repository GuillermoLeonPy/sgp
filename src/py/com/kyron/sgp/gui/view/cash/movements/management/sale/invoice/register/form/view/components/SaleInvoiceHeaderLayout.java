package py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.register.form.view.components;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.service.CashMovementsManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SaleInvoiceHeaderLayout extends HorizontalLayout {

	private final Logger logger = LoggerFactory.getLogger(SaleInvoiceHeaderLayout.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final SaleInvoiceDTO saleInvoiceDTO;
	private Label invoiceNumberLabel;
	private Label invoiceNumberIndicatorLabel;
	private Label orderNumberIndicatorLabel;
	private Label orderNumberLabel;
	private Label statusLabel;
	private Label statusValue;
	private Label emissionDateLabel;
	private Label emissionDateValue;
	private BussinesSessionUtils bussinesSessionUtils;
	private Label paymentConditionLabel;
	private Label paymentConditionValue;
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
	
	public SaleInvoiceHeaderLayout(final String VIEW_NAME,final SaleInvoiceDTO saleInvoiceDTO) {
		// TODO Auto-generated constructor stub
		this.VIEW_NAME =VIEW_NAME;
		this.saleInvoiceDTO = saleInvoiceDTO;
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
		this.invoiceNumberLabel = new Label(this.messages.get("application.common.sale.invoice.number.indicator.label"));
		this.invoiceNumberLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> property =	new ObjectProperty<String>(this.saleInvoiceDTO.getIdentifier_number());
		this.invoiceNumberIndicatorLabel = new Label(property);
		HorizontalLayout vHorizontalLayout = new HorizontalLayout(this.invoiceNumberLabel,this.invoiceNumberIndicatorLabel);
		vHorizontalLayout.setSpacing(true);
		this.column01.addComponent(vHorizontalLayout);
		
		this.statusLabel = new Label(this.messages.get("application.common.status.label"));
		this.statusLabel.addStyleName(ValoTheme.LABEL_COLORED);
		this.statusValue = new Label(this.messages.get(this.saleInvoiceDTO.getStatus()));
		HorizontalLayout statusHorizontalLayout = new HorizontalLayout(this.statusLabel,this.statusValue);
		statusHorizontalLayout.setSpacing(true);
		this.column02.addComponent(statusHorizontalLayout);
		
		this.orderNumberLabel = new Label(this.messages.get("application.common.order.number.indicator.label"));
		this.orderNumberLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<Long> orderNumberProperty =	new ObjectProperty<Long>(this.saleInvoiceDTO.getOrderDTO().getIdentifier_number());
		this.orderNumberIndicatorLabel = new Label(orderNumberProperty);
		HorizontalLayout orderNumberHorizontalLayout = new HorizontalLayout(this.orderNumberLabel,this.orderNumberIndicatorLabel);
		orderNumberHorizontalLayout.setSpacing(true);
		this.column03.addComponent(orderNumberHorizontalLayout);
	}
	
	private void setUpEmissionDatePaymentConditionCurrency(){
		this.emissionDateLabel = new Label(this.messages.get("application.common.emission.date.indicator.label"));
		this.emissionDateLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> property =	new ObjectProperty<String>(SgpUtils.parseDateParamBySessionLocale(this.saleInvoiceDTO.getEmission_date(), this.getLocale()));
		this.emissionDateValue = new Label(property);
		HorizontalLayout vHorizontalLayout = new HorizontalLayout(this.emissionDateLabel,this.emissionDateValue);
		vHorizontalLayout.setSpacing(true);
		this.column01.addComponent(vHorizontalLayout);
		
		this.paymentConditionLabel = new Label(this.messages.get("application.common.payment.condition.selector.description"));
		this.paymentConditionLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> paymentConditionProperty =	new ObjectProperty<String>(this.messages.get(this.saleInvoiceDTO.getPayment_condition()));
		this.paymentConditionValue = new Label(paymentConditionProperty);
		HorizontalLayout paymentConditionHorizontalLayout = new HorizontalLayout(this.paymentConditionLabel,this.paymentConditionValue);
		paymentConditionHorizontalLayout.setSpacing(true);
		this.column02.addComponent(paymentConditionHorizontalLayout);
		
		/* currency */
		this.currencyLabel = new Label(this.messages.get("application.common.currency.label"));
		this.currencyLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> currencyProperty =	new ObjectProperty<String>(this.saleInvoiceDTO.getOrderDTO().getCurrencyDTO().getId_code());
		this.currencyValue = new Label(currencyProperty);
		HorizontalLayout currencyHorizontalLayout = new HorizontalLayout(this.currencyLabel,this.currencyValue);
		currencyHorizontalLayout.setSpacing(true); 
		this.column03.addComponent(currencyHorizontalLayout);
	}
	
	private void setUpRucAndCommercialName(){
		this.rucLabel = new Label(this.messages.get("application.common.ruc.indicator.label"));
		this.rucLabel.addStyleName(ValoTheme.LABEL_COLORED);		
		this.rucValue = new Label(this.determinateObjectPropertyForRuc(this.saleInvoiceDTO.getBussines_ci_ruc()));
		HorizontalLayout vHorizontalLayout = new HorizontalLayout(this.rucLabel,this.rucValue);
		vHorizontalLayout.setSpacing(true);
		this.column01.addComponent(vHorizontalLayout);
		
		this.commercialNameLabel = new Label(this.messages.get("application.common.commercial.name.label"));
		this.commercialNameLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> comercialNameProperty =	new ObjectProperty<String>(this.saleInvoiceDTO.getBussines_name());
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
