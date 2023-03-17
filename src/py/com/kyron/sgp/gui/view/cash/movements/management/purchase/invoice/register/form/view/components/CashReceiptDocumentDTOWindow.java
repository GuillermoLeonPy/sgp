package py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.CashReceiptDocumentDTO;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class CashReceiptDocumentDTOWindow extends Window {

	private final Logger logger = LoggerFactory.getLogger(CashReceiptDocumentDTOWindow.class);
	private final String VIEW_NAME = "cash.receipt.document.window.form.";
	private Map<String,String> messages;
	private VerticalLayout mainViewLayout;
	private final CashReceiptDocumentDTO cashReceiptDocumentDTO;
	private SgpForm<CashReceiptDocumentDTO> cashReceiptDocumentDTOForm;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final boolean queryWindowMode;
	private boolean formConfirmed;
	final boolean showOverdueOption;
	
	public CashReceiptDocumentDTOWindow(final CashReceiptDocumentDTO cashReceiptDocumentDTO, final boolean queryWindowMode, final boolean showOverdueOption) {
		// TODO Auto-generated constructor stub
		this.cashReceiptDocumentDTO = cashReceiptDocumentDTO;
		this.queryWindowMode = queryWindowMode;
		this.showOverdueOption = showOverdueOption;
		try{		
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			//this.addStyleName("v-scrollable");
			this.addStyleName("profile-window");
			this.setUpWindowCaptionAndMainTitle();
			this.initMainViewLayout();
			this.setCashReceiptDocumentDTOForm();
			this.setUpOkCancelButtons();
			this.setContent(this.mainViewLayout);
			Responsive.makeResponsive(this);
			UI.getCurrent().addWindow(this);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public CashReceiptDocumentDTOWindow(String caption) {
		super(caption);
		// TODO Auto-generated constructor stub
	}*/

	/*public CashReceiptDocumentDTOWindow(String caption, Component content) {
		super(caption, content);
		// TODO Auto-generated constructor stub
	}*/

	private void setUpWindowCaptionAndMainTitle(){
		this.setCaption(this.messages.get(this.VIEW_NAME + "caption"));
		this.setModal(true);
		this.setClosable(true);
		this.center();
		this.setSizeFull();//
		this.setResizable(true);
        this.setIcon(FontAwesome.PRODUCT_HUNT);
	}
	
	private void initMainViewLayout(){
		if(this.mainViewLayout == null){
			this.mainViewLayout = new VerticalLayout();
			this.mainViewLayout.setSpacing(true);
			this.mainViewLayout.setMargin(true);
			//this.mainViewLayout.addStyleName("v-scrollable");
			this.mainViewLayout.setSizeFull();
			this.mainViewLayout.setMargin(new MarginInfo(true, false, false, false));
			this.mainViewLayout.setIcon(FontAwesome.COGS);
		}else
			this.mainViewLayout.removeAllComponents();
	}
	
	private void setCashReceiptDocumentDTOForm(){
		this.cashReceiptDocumentDTO.setCurrency_id(this.cashReceiptDocumentDTO.getCurrencyDTO().getId_code());
		this.cashReceiptDocumentDTOForm = new SgpForm<CashReceiptDocumentDTO>(CashReceiptDocumentDTO.class,new BeanItem<CashReceiptDocumentDTO>(this.cashReceiptDocumentDTO), ValoTheme.FORMLAYOUT_LIGHT, true);
		this.initTitles();
		this.cashReceiptDocumentDTOForm.bindAndSetPositionFormLayoutTextField("identifier_number", this.messages.get(this.VIEW_NAME + "text.field.identifier.number.label")/**/, true, 90, true,this.messages.get(this.VIEW_NAME + "text.field.identifier.number.required.message"), !this.queryWindowMode);
		this.cashReceiptDocumentDTOForm.bindAndSetPositionFormLayoutTextField("currency_id", this.messages.get("application.common.currency.label")/**/, true, 90, false,null, false);
		//if(this.cashReceiptDocumentDTO.getOverduedPayment() == null || !this.cashReceiptDocumentDTO.getOverduedPayment())
			this.cashReceiptDocumentDTOForm.bindAndSetPositionFormLayoutTextField("amount", this.messages.get("application.common.amount.label")/**/, true, 90, false,null, false);
		if(this.showOverdueOption && (this.queryWindowMode || (this.cashReceiptDocumentDTO.getOverduedPayment() != null && this.cashReceiptDocumentDTO.getOverduedPayment())))
			this.cashReceiptDocumentDTOForm.bindAndSetPositionFormLayoutTextField("overduePaymentamount", this.messages.get(this.VIEW_NAME + "text.field.overdue.payment.amount.label")/**/, true, 90, true,this.messages.get(this.VIEW_NAME + "text.field.overdue.payment.amount.required.message"), !this.queryWindowMode);
		
		if(this.showOverdueOption && !this.queryWindowMode)
			this.cashReceiptDocumentDTOForm.bindAndSetPositionFormLayoutCheckBox("overduedPayment", this.messages.get(this.VIEW_NAME + "check.box.overdued.payment.label")/*"Moneda local"*/, "",/*false*/this.cashReceiptDocumentDTO.getOverduedPayment() == null ? false: this.cashReceiptDocumentDTO.getOverduedPayment(), true,this.setUpOverduePaymentValueChangeListener());
		this.cashReceiptDocumentDTOForm.bindAndSetPositionFormLayoutDateField("emission_date", this.messages.get("application.common.emission.date.indicator.label")/*caption*/, /*immediate*/true, 50, true/*required*/, this.messages.get(this.VIEW_NAME + "date.field.emission.date.required.message")/*required message*/,false,!this.queryWindowMode);	
		//this.cashReceiptDocumentDTOForm.setEnabled(!this.queryWindowMode);
		
		 HorizontalLayout root = new HorizontalLayout();	        
	        root.setWidth(100.0f, Unit.PERCENTAGE);
	        root.setSpacing(true);
	        root.setMargin(true);
	        root.addStyleName("profile-form");
	        root.addComponent(this.cashReceiptDocumentDTOForm.getSgpFormLayout());
	        root.setExpandRatio(this.cashReceiptDocumentDTOForm.getSgpFormLayout(), 1);
		this.mainViewLayout.addComponent(root);
	}
	
	private void initTitles(){
		Label title = new Label(this.messages.get(this.VIEW_NAME + "main.title"));
        title.addStyleName(ValoTheme.LABEL_H4);
        title.addStyleName(ValoTheme.LABEL_COLORED);
        this.cashReceiptDocumentDTOForm.getSgpFormLayout().addComponent(title);
	}
	
	private ValueChangeListener setUpOverduePaymentValueChangeListener(){
		return new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				try{
					
					logger.info("\n ============================================ "
							+	"\n OverduePaymentValueChangeListener"
							+	"\n this.cashReceiptDocumentDTO.getOverduedPayment() : " + cashReceiptDocumentDTO.getOverduedPayment()
							+	"\n "+ "event.getProperty().getType().getName()) : " + event.getProperty().getType().getName()
							+	"\n event.getProperty().getValue() : \n" + event.getProperty().getValue()
							+	"\n ============================================ ");
					cashReceiptDocumentDTO.setOverduedPayment((Boolean)event.getProperty().getValue());
					reStartLayout();
				}catch(Exception e){
					logger.error("\n error", e);
				}
			}
		};
	}
	
	private void reStartLayout(){
		this.initMainViewLayout();
		this.setCashReceiptDocumentDTOForm();
		this.setUpOkCancelButtons();
	}
	
    private void setUpOkCancelButtons(){
    	Button okButton = new Button(this.messages.get("application.common.button.save.label"));/*"Cancelar"*/
		okButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		cashReceiptDocumentDTOForm.commit();
		            		formConfirmed = true;
		            		close();
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		okButton.setEnabled(!this.queryWindowMode);
		
		final Button cancelButton = new Button(this.messages.get("application.common.button.cancel.label"));/*"Cancelar"*/
		cancelButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		cashReceiptDocumentDTOForm.discard();
		            		cashReceiptDocumentDTO.setOverduedPayment(null);
		            		close();
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		
		
		final HorizontalLayout wrapperHorizontalLayout = new HorizontalLayout();
		wrapperHorizontalLayout.setSpacing(true);
		wrapperHorizontalLayout.setMargin(true);
		wrapperHorizontalLayout.addComponent(okButton);
		wrapperHorizontalLayout.addComponent(cancelButton);		
		this.cashReceiptDocumentDTOForm.getSgpFormLayout().addComponent(wrapperHorizontalLayout);
		
    }
    
	public void adjuntWindowSizeAccordingToCientDisplay(){
		logger.info("\n*************************************"
					+"\n adjusting window width and height according to client display size"
					+"\n browser width: " + Page.getCurrent().getBrowserWindowWidth()
					+"\n browser height: " + Page.getCurrent().getBrowserWindowHeight()
					+"\n*************************************");
		if(Page.getCurrent().getBrowserWindowWidth() < 800)
			this.setSizeFull();
		else{
			this.setWidth("650px");
			this.setHeight("800px");
		}			
	}

	/**
	 * @return the cashReceiptDocumentDTO
	 */
	public CashReceiptDocumentDTO getCashReceiptDocumentDTO() {
		return cashReceiptDocumentDTO;
	}

	/**
	 * @return the queryWindowMode
	 */
	public boolean isQueryWindowMode() {
		return queryWindowMode;
	}

	/**
	 * @return the formConfirmed
	 */
	public boolean isFormConfirmed() {
		return formConfirmed;
	}


}
