package py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceItemDTO;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class PurchaseInvoiceItemDTOWindow extends Window {

	private final Logger logger = LoggerFactory.getLogger(PurchaseInvoiceItemDTOWindow.class);
	private final String VIEW_NAME = "purchase.invoice.item.window.";
	private Map<String,String> messages;
	private VerticalLayout mainViewLayout;
	private final PurchaseInvoiceItemDTO purchaseInvoiceItemDTO;
	private SgpForm<PurchaseInvoiceItemDTO> purchaseInvoiceItemDTOForm;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final List<ShortcutListener> listShortcutListener;
	
	public PurchaseInvoiceItemDTOWindow(final PurchaseInvoiceItemDTO purchaseInvoiceItemDTO) {
		// TODO Auto-generated constructor stub
		this.purchaseInvoiceItemDTO = purchaseInvoiceItemDTO;
		this.listShortcutListener = this.buildSearchShortcutListeners();
		try{
			
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			//this.addStyleName("v-scrollable");
			this.addStyleName("profile-window");
			this.setUpWindowCaptionAndMainTitle();
			this.initMainViewLayout();
			this.setPurchaseInvoiceItemDTOForm();
			this.setContent(this.mainViewLayout);
			Responsive.makeResponsive(this);
			UI.getCurrent().addWindow(this);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public PurchaseInvoiceItemDTOWindow(String caption) {
		super(caption);
		// TODO Auto-generated constructor stub
	}*/

	/*public PurchaseInvoiceItemDTOWindow(String caption, Component content) {
		super(caption, content);
		// TODO Auto-generated constructor stub
	}*/

	private List<ShortcutListener> buildSearchShortcutListeners(){
		ShortcutListener enterShortcutListener =  new ShortcutListener("Enter", KeyCode.ENTER, null) {
	    	 @Override
	         public void handleAction(final Object sender, final Object target) {
	    		try{
		    		 logger.info("\n*************************************"
		    				 	+"\nENTER LISTENER"
		    				 	+"\nsender class: "+sender.getClass()
		    				 	+"\ntarget class: "+target.getClass());
		    		final TextField vTextField = (TextField)target;
		    		logger.info("\nsender text field value: "+vTextField.getValue()
		    					+"\n*************************************");
		    		purchaseInvoiceItemDTOForm.commit();
		    		close();
	         	}catch(Exception e){		
	        		commonExceptionErrorNotification.showErrorMessageNotification(e);
	        	}
	         }
	     };
	     List<ShortcutListener> listShortcutListener = new ArrayList<ShortcutListener>();
	     listShortcutListener.add(enterShortcutListener);
	     listShortcutListener.add(this.buildEscapeKeyShortcutListener());
	     return listShortcutListener;
	}
	
	private ShortcutListener buildEscapeKeyShortcutListener(){
		return new ShortcutListener("Clear", KeyCode.ESCAPE, null) {
	    	 @Override
	         public void handleAction(final Object sender, final Object target) {
	    		 try{
		    		 logger.info("\n*************************************"
		    				 	+"\nESCAPE LISTENER"
		    				 	+"\nsender class: "+sender.getClass()
		    				 	+"\ntarget class: "+target.getClass());
		            final TextField vTextField = (TextField)target;
			    	logger.info("\nsender text field value: "+vTextField.getValue()
		    					+"\n*************************************");
		             vTextField.setValue("");
		             purchaseInvoiceItemDTOForm.discard();
	         	}catch(Exception e){		
	        		commonExceptionErrorNotification.showErrorMessageNotification(e);
	        	}
	         }
	     };
	}
	
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
		this.mainViewLayout = new VerticalLayout();
		this.mainViewLayout.setSpacing(true);
		this.mainViewLayout.setMargin(true);
		//this.mainViewLayout.addStyleName("v-scrollable");
		this.mainViewLayout.setSizeFull();
		this.mainViewLayout.setMargin(new MarginInfo(true, false, false, false));
		this.mainViewLayout.setIcon(FontAwesome.COGS);
	}
	
	private void setPurchaseInvoiceItemDTOForm(){
		this.purchaseInvoiceItemDTO.setRaw_material_id(this.purchaseInvoiceItemDTO.getRawMaterialDTO().getRaw_material_id());
		this.purchaseInvoiceItemDTO.setMeasurment_unit_id(this.purchaseInvoiceItemDTO.getMeasurmentUnitDTO().getMeasurment_unit_id());
		this.purchaseInvoiceItemDTOForm = new SgpForm<PurchaseInvoiceItemDTO>(PurchaseInvoiceItemDTO.class, new BeanItem<PurchaseInvoiceItemDTO>(this.purchaseInvoiceItemDTO), ValoTheme.FORMLAYOUT_LIGHT/*"light"*/, true);
		this.initTitles();
		this.purchaseInvoiceItemDTOForm.bindAndSetPositionFormLayoutTextField("raw_material_id", this.messages.get("application.common.rawmaterialid.label")/**/, true, 90, false,null, false);
		this.purchaseInvoiceItemDTOForm.bindAndSetPositionFormLayoutTextField("measurment_unit_id", this.messages.get("application.common.measurmentunitid.label")/**/, true, 90, false,null, false);
		this.purchaseInvoiceItemDTOForm.bindAndSetPositionFormLayoutTextField("quantity", this.messages.get("application.common.quantity.label")/**/, true, 90, true,this.messages.get("application.common.quantity.tex.field.required.message"), true, this.setUpTextFieldFocusListener(),this.setUpTextFieldBlurListener());
    	this.purchaseInvoiceItemDTOForm.bindAndSetPositionFormLayoutTextField("unit_price_amount", this.messages.get("application.common.table.column.unit.price.amount.label")/**/, true, 90, true,this.messages.get(this.VIEW_NAME + "form.text.field.unit.price.amount.required.message"), true, this.setUpTextFieldFocusListener(),this.setUpTextFieldBlurListener());
				
		 HorizontalLayout root = new HorizontalLayout();	        
	        root.setWidth(100.0f, Unit.PERCENTAGE);
	        root.setSpacing(true);
	        root.setMargin(true);
	        root.addStyleName("profile-form");
	        root.addComponent(this.purchaseInvoiceItemDTOForm.getSgpFormLayout());
	        root.setExpandRatio(this.purchaseInvoiceItemDTOForm.getSgpFormLayout(), 1);
		this.mainViewLayout.addComponent(root);
	}
	
	private void initTitles(){
		Label title = new Label(this.messages.get(this.VIEW_NAME + "form.main.title"));
        title.addStyleName(ValoTheme.LABEL_H4);
        title.addStyleName(ValoTheme.LABEL_COLORED);
        
        Label title2 = new Label(this.messages.get("application.common.rawmaterialid.label"));
        title2.addStyleName(ValoTheme.LABEL_COLORED);
        Label title2Value = new Label(this.purchaseInvoiceItemDTO.getRawMaterialDTO().getRaw_material_id());
        HorizontalLayout title2HorizontalLayout = new HorizontalLayout(title2,title2Value);
        title2HorizontalLayout.setSpacing(true);
        
        Label title3 = new Label(this.messages.get("application.common.measurmentunitid.label"));
        title3.addStyleName(ValoTheme.LABEL_COLORED);
        Label title3Value = new Label(this.purchaseInvoiceItemDTO.getMeasurmentUnitDTO().getMeasurment_unit_id());
        HorizontalLayout title3HorizontalLayout = new HorizontalLayout(title3,title3Value);
        title3HorizontalLayout.setSpacing(true);
        this.purchaseInvoiceItemDTOForm.getSgpFormLayout().addComponent(title);
        /*this.purchaseInvoiceItemDTOForm.getSgpFormLayout().addComponent(title2Value);
        this.purchaseInvoiceItemDTOForm.getSgpFormLayout().addComponent(title3Value);*/
        /*this.mainViewLayout.addComponent(title);
        this.mainViewLayout.addComponent(title2HorizontalLayout);
        this.mainViewLayout.addComponent(title3HorizontalLayout);*/
	}
	private FocusListener setUpTextFieldFocusListener(){
		return new FocusListener(){

			@Override
			public void focus(FocusEvent event) {
				// TODO Auto-generated method stub
				logger.info("\n************************"
						+"\n focus listener in text field"
						+"\n adding enter and escape keys shortcut listeners"
						+"\n************************");
				final TextField textField = (TextField)event.getSource();
				//final List<ShortcutListener> listShortcutListener = buildSearchShortcutListeners();
				for(ShortcutListener vShortcutListener : listShortcutListener)
					textField.addShortcutListener(vShortcutListener);
			}
			
		};
	}
	
	private BlurListener setUpTextFieldBlurListener(){
		return new BlurListener(){

			@Override
			public void blur(BlurEvent event) {
				// TODO Auto-generated method stub
				logger.info("\n************************"
							+"\n blur listener in text field"
							+"\n removing enter and escape keys shortcut listeners"
							+ "\n object class type: "+event.getSource().getClass());
				final TextField textField = (TextField)event.getSource();
				int listenersCount = 0;
				for(ShortcutListener vShortcutListener : listShortcutListener){
					listenersCount++;
					textField.removeListener(ShortcutListener.class, vShortcutListener);
					textField.removeShortcutListener(vShortcutListener);
				}
				logger.info("\n-------------------------"
							+"\n removed listeners count : " + listenersCount
							+"\n************************");
			}
			
		};
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
	 * @return the purchaseInvoiceItemDTO
	 */
	public PurchaseInvoiceItemDTO getPurchaseInvoiceItemDTO() {
		return purchaseInvoiceItemDTO;
	}
}
