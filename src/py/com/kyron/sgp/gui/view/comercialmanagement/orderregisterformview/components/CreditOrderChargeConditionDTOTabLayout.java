package py.com.kyron.sgp.gui.view.comercialmanagement.orderregisterformview.components;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.CreditOrderChargeConditionDTO;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.comercialmanagement.OrderRegisterFormView;

import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class CreditOrderChargeConditionDTOTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(CreditOrderChargeConditionDTOTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "order.management.form.";
	private final CreditOrderChargeConditionDTO creditOrderChargeConditionDTO;
	private SgpForm<CreditOrderChargeConditionDTO> creditOrderChargeConditionDTOForm;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final OrderRegisterFormView orderRegisterFormView;
	private final boolean editFormMode;
	
	public CreditOrderChargeConditionDTOTabLayout(
			final OrderRegisterFormView orderRegisterFormView,
			final CreditOrderChargeConditionDTO creditOrderChargeConditionDTO,
			final boolean editFormMode) {
		// TODO Auto-generated constructor stub
		this.orderRegisterFormView = orderRegisterFormView;
		this.creditOrderChargeConditionDTO = creditOrderChargeConditionDTO;
		this.editFormMode = editFormMode;
		try{
			logger.info("\n CreditOrderChargeConditionDTOTabLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();			
	        //this.addStyleName("v-scrollable");
	        //this.setHeight("100%");	        
	        Responsive.makeResponsive(this);
			DashboardEventBus.register(this);
			this.setSpacing(true);
			this.setMargin(true);
			this.setSizeFull();
			this.setUpLayoutContent();			
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public CreditOrderChargeConditionDTOTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n CreditOrderChargeConditionDTOTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void initTitles(){
		Label title = new Label(!this.editFormMode ? 
				this.messages.get(this.VIEW_NAME + "tab.credit.order.charge.condition.form.main.tittle.register"):
					this.messages.get(this.VIEW_NAME + "tab.credit.order.charge.condition.form.main.tittle.edit"));
        title.addStyleName("h1");
        this.addComponent(title);

	}
	
	private void setUpcreditOrderChargeConditionDTOForm(){
		this.creditOrderChargeConditionDTOForm = new SgpForm<CreditOrderChargeConditionDTO>(CreditOrderChargeConditionDTO.class, new BeanItem<CreditOrderChargeConditionDTO>(this.creditOrderChargeConditionDTO), /*ValoTheme.FORMLAYOUT_LIGHT*/"light", true);
		this.creditOrderChargeConditionDTOForm.bindAndSetPositionFormLayoutTextField("days_interval", this.messages.get(this.VIEW_NAME + "tab.credit.order.charge.condition.form.text.field.days.interval.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "tab.credit.order.charge.condition.form.text.field.days.interval.required.message"), !this.editFormMode);
		this.creditOrderChargeConditionDTOForm.bindAndSetPositionFormLayoutTextField("days_interval_percent_increment", this.messages.get(this.VIEW_NAME + "tab.credit.order.charge.condition.form.text.field.days.interval.percent.increment.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "tab.credit.order.charge.condition.form.text.field.days.interval.percent.increment.required.message"), !this.editFormMode);
	}
	
	private void setUpLayoutContent(){
		this.initTitles();
    	this.setUpcreditOrderChargeConditionDTOForm();
		final Button okButton = new Button(
				!this.editFormMode ? this.messages.get("application.common.button.save.label"):
					 this.messages.get("application.common.set.end.validity.date.button.label"));/*"Cancelar"*/
		okButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		if(!editFormMode)creditOrderChargeConditionDTOForm.commit();		            		
		            		orderRegisterFormView.saveButtonActionCreditOrderChargeConditionDTOTabLayout(creditOrderChargeConditionDTO, editFormMode);
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		final Button cancelButton = new Button(this.messages.get("application.common.button.cancel.label"));/*"Cancelar"*/
		cancelButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		creditOrderChargeConditionDTOForm.discard();
		            		orderRegisterFormView.cancelButtonActionCreditOrderChargeConditionDTOTabLayout();
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
		okCancelHorizontalLayout.addComponent(okButton);		
		okCancelHorizontalLayout.addComponent(cancelButton);		
		
		this.creditOrderChargeConditionDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);		
		this.addComponent(this.creditOrderChargeConditionDTOForm.getSgpFormLayout());
		//this.addComponent(new Label("hola"));
	}
	

}
