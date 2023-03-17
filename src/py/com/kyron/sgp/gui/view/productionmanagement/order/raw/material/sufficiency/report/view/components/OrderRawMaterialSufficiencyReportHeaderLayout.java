package py.com.kyron.sgp.gui.view.productionmanagement.order.raw.material.sufficiency.report.view.components;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.productionmanagement.domain.OrderRawMaterialSufficiencyReportDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.utils.TableNumericColumnCellLabelHelper;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class OrderRawMaterialSufficiencyReportHeaderLayout extends
		HorizontalLayout {

	private final Logger logger = LoggerFactory.getLogger(OrderRawMaterialSufficiencyReportHeaderLayout.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final OrderRawMaterialSufficiencyReportDTO orderRawMaterialSufficiencyReportDTO;
	private BussinesSessionUtils bussinesSessionUtils;
	private ComercialManagementService comercialManagementService;
	private SgpUtils sgpUtils;
	private Label order_identifier_numberLabel;
	private Label order_identifier_numberValue;
	private Label emissionDateLabel;
	private Label emissionDateValue;
	private Label sale_invoice_identifier_numberLabel;
	private Label sale_invoice_identifier_numberValue;
	private Label sale_invoice_emission_dateLabel;
	private Label sale_invoice_emission_dateValue;
	private Label bussines_nameLabel;
	private Label bussines_nameValue;
	private Label sale_invoice_payment_conditionLabel;
	private Label sale_invoice_payment_conditionValue;
	private Label sale_invoice_statusLabel;
	private Label sale_invoice_statusValue;
	private Label sale_invoice_total_amountLabel;
	private Label sale_invoice_total_amountValue;
	private Label currency_id_codeLabel;
	private Label currency_id_codeValue;
	private VerticalLayout column01;
	private VerticalLayout column02;
	private VerticalLayout column03;
	
	public OrderRawMaterialSufficiencyReportHeaderLayout(final OrderRawMaterialSufficiencyReportDTO orderRawMaterialSufficiencyReportDTO,final String VIEW_NAME) {
		this.orderRawMaterialSufficiencyReportDTO = orderRawMaterialSufficiencyReportDTO;
		this.VIEW_NAME = VIEW_NAME;
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n SaleInvoiceHeaderLayout()...");
			this.initServices();
			this.setLocale(this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale());
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.sgpUtils = new SgpUtils();
	        //this.setSpacing(true);
			this.setMargin(new MarginInfo(true, true, true, true));
	        Responsive.makeResponsive(this);
	        this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public OrderRawMaterialSufficiencyReportHeaderLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	private void initServices() throws Exception{
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		this.comercialManagementService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
	}
	
	private void setUpLayoutContent(){
		this.column01 = new VerticalLayout();
		this.column01.setMargin(new MarginInfo(false, true, false, false));
		this.column02 = new VerticalLayout();
		this.column02.setMargin(new MarginInfo(false, true, false, false));
		this.column03 = new VerticalLayout();
		this.column03.setMargin(new MarginInfo(false, true, false, false));
		this.setUpRow01();
		this.setUpRow02();
		this.setUpRow03();
		this.addComponents(this.column01,this.column02,this.column03);
		
	}
	 
	
	private void setUpRow01(){
		this.emissionDateLabel = new Label(this.messages.get("application.common.emission.date.indicator.label"));
		this.emissionDateLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> property =	new ObjectProperty<String>(SgpUtils.parseDateParamBySessionLocale(this.orderRawMaterialSufficiencyReportDTO.getReport_emission_date(), this.getLocale()));
		this.emissionDateValue = new Label(property);		
		HorizontalLayout vHorizontalLayout = new HorizontalLayout(this.emissionDateLabel,this.emissionDateValue);
		vHorizontalLayout.setSpacing(true);
		this.column01.addComponent(vHorizontalLayout);
		
		this.order_identifier_numberLabel = new Label(this.messages.get("application.common.order.number.indicator.label"));
		this.order_identifier_numberLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<Long> property2 =	new ObjectProperty<Long>(this.orderRawMaterialSufficiencyReportDTO.getOrder_identifier_number());
		this.order_identifier_numberValue = new Label(property2);
		HorizontalLayout vHorizontalLayout2 = new HorizontalLayout(this.order_identifier_numberLabel,this.order_identifier_numberValue);
		vHorizontalLayout2.setSpacing(true);
		this.column02.addComponent(vHorizontalLayout2);
		
		this.sale_invoice_identifier_numberLabel = new Label(this.messages.get("application.common.sale.invoice.number.indicator.label"));
		this.sale_invoice_identifier_numberLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> property3 =	new ObjectProperty<String>(this.orderRawMaterialSufficiencyReportDTO.getSale_invoice_identifier_number());
		this.sale_invoice_identifier_numberValue = new Label(property3);
		HorizontalLayout vHorizontalLayout3 = new HorizontalLayout(this.sale_invoice_identifier_numberLabel,this.sale_invoice_identifier_numberValue);
		vHorizontalLayout3.setSpacing(true);
		this.column03.addComponent(vHorizontalLayout3);
	}
	
	private void setUpRow02(){
		this.bussines_nameLabel = new Label(this.messages.get("application.common.commercial.name.label"));
		this.bussines_nameLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> property2 =	new ObjectProperty<String>(this.orderRawMaterialSufficiencyReportDTO.getBussines_name());
		this.bussines_nameValue = new Label(property2);			
		HorizontalLayout vHorizontalLayout = new HorizontalLayout(this.bussines_nameLabel,this.bussines_nameValue);
		vHorizontalLayout.setSpacing(true);
		this.column01.addComponent(vHorizontalLayout);		
		
		this.sale_invoice_payment_conditionLabel = new Label(this.messages.get("application.common.payment.condition.selector.description"));
		this.sale_invoice_payment_conditionLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> property3 =	new ObjectProperty<String>(this.messages.get(this.orderRawMaterialSufficiencyReportDTO.getSale_invoice_payment_condition()));
		this.sale_invoice_payment_conditionValue = new Label(property3);
		HorizontalLayout vHorizontalLayout2 = new HorizontalLayout(this.sale_invoice_payment_conditionLabel,this.sale_invoice_payment_conditionValue);
		vHorizontalLayout2.setSpacing(true);
		this.column02.addComponent(vHorizontalLayout2);		
		
		this.sale_invoice_emission_dateLabel = new Label(this.messages.get("application.common.emission.date.indicator.label"));
		this.sale_invoice_emission_dateLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> property =	new ObjectProperty<String>(SgpUtils.parseDateParamBySessionLocale(this.orderRawMaterialSufficiencyReportDTO.getSale_invoice_emission_date(), this.getLocale()));
		this.sale_invoice_emission_dateValue = new Label(property);
		HorizontalLayout vHorizontalLayout3 = new HorizontalLayout(this.sale_invoice_emission_dateLabel,this.sale_invoice_emission_dateValue);
		vHorizontalLayout3.setSpacing(true);
		this.column03.addComponent(vHorizontalLayout3);
	}
	
	private void setUpRow03(){

		this.currency_id_codeLabel = new Label(this.messages.get("application.common.currency.label"));
		this.currency_id_codeLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> property3 =	new ObjectProperty<String>(this.orderRawMaterialSufficiencyReportDTO.getCurrency_id_code());
		this.currency_id_codeValue = new Label(property3);		
		HorizontalLayout vHorizontalLayout = new HorizontalLayout(this.currency_id_codeLabel,this.currency_id_codeValue);
		vHorizontalLayout.setSpacing(true);
		this.column01.addComponent(vHorizontalLayout);
		
		this.sale_invoice_total_amountLabel = new Label(this.messages.get("application.common.amount.label"));
		this.sale_invoice_total_amountLabel.addStyleName(ValoTheme.LABEL_COLORED);		
		this.sale_invoice_total_amountValue = TableNumericColumnCellLabelHelper.buildNumericLabelByBigDecimalNumber(this.orderRawMaterialSufficiencyReportDTO.getSale_invoice_total_amount());
		HorizontalLayout vHorizontalLayout2 = new HorizontalLayout(this.sale_invoice_total_amountLabel,this.sale_invoice_total_amountValue);
		vHorizontalLayout2.setSpacing(true);
		this.column02.addComponent(vHorizontalLayout2);
		
		this.sale_invoice_statusLabel = new Label(this.messages.get("application.common.status.label"));
		this.sale_invoice_statusLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> property =	new ObjectProperty<String>(this.messages.get(this.orderRawMaterialSufficiencyReportDTO.getSale_invoice_status()));
		this.sale_invoice_statusValue = new Label(property);	
		HorizontalLayout vHorizontalLayout3 = new HorizontalLayout(this.sale_invoice_statusLabel,this.sale_invoice_statusValue);
		vHorizontalLayout3.setSpacing(true);
		this.column03.addComponent(vHorizontalLayout3);
	}
}
