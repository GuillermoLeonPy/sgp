package py.com.kyron.sgp.gui.view.comercialmanagement.orderregisterformview.components;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CreditOrderChargeConditionDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.service.PersonManagementService;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class CreditOrderCriteriaLayout extends HorizontalLayout {

	private final Logger logger = LoggerFactory.getLogger(CreditOrderCriteriaLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "order.management.form.";
	private SgpForm<OrderDTO> orderDTOCreditCriteriaForm;
	private final OrderDTO orderDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Long credit_order_fee_quantity;
	private Long credit_order_fee_periodicity_days_quantity;
	private ComercialManagementService comercialManagementService;
	private CreditOrderChargeConditionDTO creditOrderChargeConditionDTO;
	private BigDecimal calculatedCreditOrderPercentIncrement;
	private VerticalLayout orderDTOCreditCriteriaFormVerticalLayout;
	private TextField creditOrderPercentIncrementTextField; 
	private final OrderDTOTabLayout orderDTOTabLayout;
	
	public CreditOrderCriteriaLayout(final OrderDTOTabLayout orderDTOTabLayout, final OrderDTO orderDTO) {
		// TODO Auto-generated constructor stub
		this.orderDTO = orderDTO;
		this.orderDTOTabLayout = orderDTOTabLayout;
		try{
			logger.info("\n CreditOrderCriteriaLayout()...");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			//this.setSpacing(true);
			//this.setMargin(new MarginInfo(false, true, false, false));
			this.setWidth(300f, Unit.PIXELS);
			this.initServices();
			this.setResponsive(true);
			Responsive.makeResponsive(this);
			DashboardEventBus.register(this);
			if(this.orderDTO.getId()!=null){
				this.credit_order_fee_quantity = this.orderDTO.getCredit_order_fee_quantity();
				this.credit_order_fee_periodicity_days_quantity = this.orderDTO.getCredit_order_fee_periodicity_days_quantity();
			}
			this.prepareCreditOrderCriteriaForm();
			this.addComponent(this.orderDTOCreditCriteriaFormVerticalLayout);			
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	private void initServices() throws Exception{
		this.comercialManagementService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
	}
	/*public CreditOrderCriteriaLayout(Component... children) {
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
	
	private void prepareCreditOrderCriteriaForm() throws PmsServiceException{
		this.orderDTOCreditCriteriaForm = new SgpForm<OrderDTO>(OrderDTO.class, new BeanItem<OrderDTO>(this.orderDTO), "light", true);
		this.orderDTOCreditCriteriaForm.bindAndSetPositionFormLayoutTextField("credit_order_fee_quantity", 
				this.messages.get(this.VIEW_NAME + "tab.order.form.text.field.credit.order.fee.quantity.label")/**/, 
				true, 100, true,
				this.messages.get(this.VIEW_NAME + "tab.order.form.text.field.credit.order.fee.quantity.required.message"), true,
				null,
				this.setUpTextFieldBlurListener());
		this.orderDTOCreditCriteriaForm.bindAndSetPositionFormLayoutTextField("credit_order_fee_periodicity_days_quantity", 
				this.messages.get(this.VIEW_NAME + "tab.order.form.text.field.credit.order.fee.periodicity.days.quantity.label")/**/, 
				true, 100, true,
				this.messages.get(this.VIEW_NAME + "tab.order.form.text.field.credit.order.fee.periodicity.days.quantity.required.message"), true,
				null,
				this.setUpTextFieldBlurListener());
		/*this.orderDTOCreditCriteriaForm.bindAndSetPositionFormLayoutTextField("credit_order_payment_condition_surcharge_percentage", 
				this.messages.get(this.VIEW_NAME + "tab.order.form.label.credit.order.percent.increment.label"), 
				true, 100, false,
				null, false);*/
		this.prepareCreditOrderPercentIncrement();
		this.orderDTOCreditCriteriaFormVerticalLayout = new VerticalLayout(this.orderDTOCreditCriteriaForm.getSgpFormLayout());		
	}
	

	private BlurListener setUpTextFieldBlurListener(){
		return new BlurListener(){
			@Override
			public void blur(BlurEvent event) {
				// TODO Auto-generated method stub
				try{
					logger.info("\n************************"
							+"\n blur listener in text field"
							+"\n removing enter and escape keys shortcut listeners"
							+ "\n object class type: "+event.getSource().getClass());
					final TextField textField = (TextField)event.getSource();
					textField.commit();
					if(textField.getCaption().equals(messages.get(VIEW_NAME + "tab.order.form.text.field.credit.order.fee.quantity.label"))
					&& !textField.isEmpty())
						credit_order_fee_quantity = (Long)textField.getPropertyDataSource().getValue();
					else if(textField.getCaption().equals(messages.get(VIEW_NAME + "tab.order.form.text.field.credit.order.fee.periodicity.days.quantity.label"))
					&& !textField.isEmpty())
						credit_order_fee_periodicity_days_quantity = (Long)textField.getPropertyDataSource().getValue();
					
					if(credit_order_fee_quantity!=null && credit_order_fee_periodicity_days_quantity!=null){
						prepareCreditOrderPercentIncrement();
						orderDTOTabLayout.reSetLayoutAfterAnItemHasBeenAdded();//to recalculate table totals						
					}
					logger.info(" \n================================================"
								+"\n credit order fee quantity: "+credit_order_fee_quantity
								+"\n credit order fee periodicity quantity: "+credit_order_fee_periodicity_days_quantity
								+"\n================================================");
				}catch(Exception e){
					//commonExceptionErrorNotification.showErrorMessageNotification(e);
					logger.error("\n error", e);
				}
				
			}			
		};
	}
	
	@SuppressWarnings("unchecked")
	private void prepareCreditOrderPercentIncrement() throws PmsServiceException{
				
		if(this.credit_order_fee_quantity != null && this.credit_order_fee_periodicity_days_quantity!=null)
			this.checkCreditOrderChargeConditionDTO();
		else
			this.calculatedCreditOrderPercentIncrement = BigDecimal.ZERO;
		

		if(this.creditOrderPercentIncrementTextField == null){			
			final ObjectProperty<BigDecimal> property =	new ObjectProperty<BigDecimal>(this.calculatedCreditOrderPercentIncrement);
			this.creditOrderPercentIncrementTextField = new TextField(property);
			this.creditOrderPercentIncrementTextField.setCaption(this.messages.get(this.VIEW_NAME + "tab.order.form.label.credit.order.percent.increment.label"));
			this.creditOrderPercentIncrementTextField.setEnabled(false);		
			this.orderDTOCreditCriteriaForm.getSgpFormLayout().addComponent(this.creditOrderPercentIncrementTextField);
		}else{
			this.creditOrderPercentIncrementTextField.getPropertyDataSource().setValue(this.calculatedCreditOrderPercentIncrement);
			this.creditOrderPercentIncrementTextField.markAsDirty();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void checkCreditOrderChargeConditionDTO() throws PmsServiceException{
		Long id = null;
		if(this.orderDTO.getStatus() == null || this.orderDTO.getStatus().equals("application.common.status.pending"))
			id = this.comercialManagementService.getCreditOrderChargeConditionValidRowId();
		else
			id = this.orderDTO.getId_credit_order_charge_condition();
		
		if (id !=null){
			this.creditOrderChargeConditionDTO = this.comercialManagementService.listCreditOrderChargeConditionDTO(new CreditOrderChargeConditionDTO(id)).get(0);
			this.calculatedCreditOrderPercentIncrement = 
					(new BigDecimal(this.credit_order_fee_quantity * this.credit_order_fee_periodicity_days_quantity)
					.divide(
					new BigDecimal(this.creditOrderChargeConditionDTO.getDays_interval()))
					.multiply(this.creditOrderChargeConditionDTO.getDays_interval_percent_increment())
					);
			this.orderDTO.setId_credit_order_charge_condition(this.creditOrderChargeConditionDTO.getId());
			this.orderDTO.setCredit_order_payment_condition_surcharge_percentage(this.calculatedCreditOrderPercentIncrement);			
		}else{
			this.calculatedCreditOrderPercentIncrement = BigDecimal.ZERO;
		}
		/*this.creditOrderPercentIncrementTextField.getPropertyDataSource().setValue(this.calculatedCreditOrderPercentIncrement);
		this.creditOrderPercentIncrementTextField.markAsDirty();*/
	}
	
	
	public void commitOrderDTOCreditCriteriaForm() throws CommitException{		
			this.orderDTOCreditCriteriaForm.commit();
	}
	
	public void restartLayout() throws PmsServiceException{
		this.orderDTOCreditCriteriaForm.discard();
		this.removeAllComponents();
		this.creditOrderPercentIncrementTextField = null;
		this.credit_order_fee_quantity = null;
		this.credit_order_fee_periodicity_days_quantity = null;
		this.orderDTO.setCredit_order_fee_quantity(null);
		this.orderDTO.setCredit_order_fee_periodicity_days_quantity(null);
		this.orderDTO.setCredit_order_payment_condition_surcharge_percentage(null);
		this.orderDTO.setId_credit_order_charge_condition(null);
		this.prepareCreditOrderCriteriaForm();
		this.addComponent(this.orderDTOCreditCriteriaFormVerticalLayout);
		this.prepareCreditOrderPercentIncrement();
	}
}
