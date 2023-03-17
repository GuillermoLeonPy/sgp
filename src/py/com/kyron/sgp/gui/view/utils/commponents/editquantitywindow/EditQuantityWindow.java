package py.com.kyron.sgp.gui.view.utils.commponents.editquantitywindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.PersistenceErrorMessagesDecoder;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
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
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class EditQuantityWindow extends Window {

	private final Logger logger = LoggerFactory.getLogger(EditQuantityWindow.class);
	private final String VIEW_NAME = "edit.quantity.window.";
	private Map<String,String> messages;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private VerticalLayout layout;
	private SgpForm<QuantityEditor> quantityEditorForm;
	private QuantityEditor quantityEditor;
	private Long initialValue;
	private Long minValue;
	private Long maxValue;
	private final List<ShortcutListener> quantityTextFieldShortcutListeners;
	
	/*public EditQuantityWindow() {
		// TODO Auto-generated constructor stub
	}

	public EditQuantityWindow(String caption) {
		super(caption);
		// TODO Auto-generated constructor stub
	}

	public EditQuantityWindow(String caption, Component content) {
		super(caption, content);
		// TODO Auto-generated constructor stub
	}*/

	public EditQuantityWindow(String caption, String mainTitle, Long initialValue, Long minValue, Long maxValue){
		// TODO Auto-generated constructor stub
		this.quantityTextFieldShortcutListeners = this.buildQuantityTextFieldShortcutListeners();
		try{
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initialValue = initialValue;
			this.minValue = minValue;
			this.maxValue = maxValue;
			this.setCaption(caption);
			this.setModal(true);
			this.setClosable(true);
			this.center();
			this.setSizeFull();//
			this.setResizable(true);
	        this.setIcon(FontAwesome.PRODUCT_HUNT);
			
			Label questionLabel = new Label(mainTitle);
			questionLabel.addStyleName(ValoTheme.LABEL_H4);
			questionLabel.addStyleName(ValoTheme.LABEL_COLORED);
			
			this.quantityEditor = new QuantityEditor(initialValue);
			this.quantityEditorForm = new SgpForm<QuantityEditor>(QuantityEditor.class, new BeanItem<QuantityEditor>(this.quantityEditor), ValoTheme.FORMLAYOUT_LIGHT/*"light"*/, true);
			this.quantityEditorForm.getSgpFormLayout().addComponent(questionLabel);
			this.quantityEditorForm.bindAndSetPositionFormLayoutTextField("quantity", this.messages.get("application.common.quantity.label")/**/, true, 90, true,this.messages.get("application.common.quantity.tex.field.required.message"), true,this.setUpTextFieldFocusListener(),this.setUpTextFieldBlurListener());
			
			this.layout = new VerticalLayout();
			this.layout.setSpacing(true);
			this.layout.setMargin(true);
			//this.mainViewLayout.addStyleName("v-scrollable");
			this.layout.setSizeFull();
			this.layout.setIcon(FontAwesome.COGS);
			
			this.layout.addComponent(this.quantityEditorForm.getSgpFormLayout());
			this.setContent(this.layout);
			Responsive.makeResponsive(this);
			UI.getCurrent().addWindow(this);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
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
				for(ShortcutListener vShortcutListener : quantityTextFieldShortcutListeners)
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
				for(ShortcutListener vShortcutListener : quantityTextFieldShortcutListeners){
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
			this.setWidth("500px");
			this.setHeight("250px");
		}			
	}
	
	/*private List<ShortcutListener> buildQuantityTextFieldShortcutListeners(){
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
					quantityEditorForm.commit();
					
					if(initialValue.equals(quantityEditor.getQuantity()) || initialValue.compareTo(quantityEditor.getQuantity()) < 0){
						Object [] objectArray = new Object [1];
						objectArray[0] = initialValue.toString();
						throw new PmsServiceException(VIEW_NAME + "new.quantity.less.than.old.quantity.required.error", null, objectArray);
					}
					close();
	         	}catch(Exception e){		
	        		commonExceptionErrorNotification.showErrorMessageNotification(e);
	        	}
	         }
	     };
	     List<ShortcutListener> listShortcutListener = new ArrayList<ShortcutListener>();
	     listShortcutListener.add(enterShortcutListener);
	     return listShortcutListener;
	}*/

	private List<ShortcutListener> buildQuantityTextFieldShortcutListeners(){
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
					quantityEditorForm.commit();
					
					if(minValue.compareTo(quantityEditor.getQuantity()) > 0 || maxValue.compareTo(quantityEditor.getQuantity()) < 0){
						Object [] objectArray = new Object [2];
						objectArray[0] = minValue.toString();
						objectArray[1] = maxValue.toString();
						throw new PmsServiceException(VIEW_NAME + "new.quantity.out.of.bounds.error", null, objectArray);
					}
					close();
	         	}catch(Exception e){		
	        		commonExceptionErrorNotification.showErrorMessageNotification(e);
	        	}
	         }
	     };
	     List<ShortcutListener> listShortcutListener = new ArrayList<ShortcutListener>();
	     listShortcutListener.add(enterShortcutListener);
	     return listShortcutListener;
	}
	public class QuantityEditor{
		
		@Range(min=1L,max=250L)
		private Long quantity;	

		/**
		 * 
		 */
		public QuantityEditor() {
			super();
			// TODO Auto-generated constructor stub
		}

		/**
		 * @param quantity
		 */
		public QuantityEditor(Long quantity) {
			super();
			this.quantity = quantity;
		}

		/**
		 * @return the quantity
		 */
		public Long getQuantity() {
			return quantity;
		}

		/**
		 * @param quantity the quantity to set
		 */
		public void setQuantity(Long quantity) {
			this.quantity = quantity;
		}
		
	}

	/**
	 * @return the quantityEditor
	 */
	public QuantityEditor getQuantityEditor() {
		return quantityEditor;
	}

	/**
	 * @param quantityEditor the quantityEditor to set
	 */
	public void setQuantityEditor(QuantityEditor quantityEditor) {
		this.quantityEditor = quantityEditor;
	}
}
