package py.com.kyron.sgp.gui.component;

import java.util.List;
import java.util.Locale;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class SgpForm<T> extends BeanFieldGroup<T> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private GridLayout sgpGridLayout;
	private FormLayout sgpFormLayout;
	private TextField textFieldField;
	private HorizontalLayout wrap;
	private CheckBox checkBoxField;
	//private BeanValidator beanValidator;
	private DateField dateField;
	
	public SgpForm(Class<T> beanType) {
		super(beanType);
		// TODO Auto-generated constructor stub
		//BeanValidator bv = new BeanValidator(Moneda.class, "");
		//bv.setLocale(locale);
	}

	public SgpForm(Class<T> beanType, BeanItem<T> beanItem) {
		super(beanType);
		this.setItemDataSource(beanItem);
		this.sgpFormLayout = new FormLayout();
		// TODO Auto-generated constructor stub
	}
	
	public SgpForm(Class<T> beanType, BeanItem<T> beanItem, int columns, int rows) {
		super(beanType);
		// TODO Auto-generated constructor stub
		this.setItemDataSource(beanItem);//Updates the item that is used by this FieldBinder. Rebinds all fields to the properties in the new item.
		this.sgpGridLayout = new GridLayout(columns, rows);
	}//public SgpForm(Class<T> beanType, BeanItem<T> beanItem, int columns, int rows, FieldGroupFieldFactory fieldFactory)
	
	public SgpForm(Class<T> beanType, BeanItem<T> beanItem, String formStyle, boolean formWithMargin) {
		super(beanType);
		this.setItemDataSource(beanItem);//Updates the item that is used by this FieldBinder. Rebinds all fields to the properties in the new item.
		this.sgpFormLayout = new FormLayout();
		this.sgpFormLayout.addStyleName(formStyle);
		this.sgpFormLayout.setMargin(formWithMargin);
		
	}//public SgpForm(Class<T> beanType, BeanItem<T> beanItem)
	
//	public static boolean isBeanValidationJSR303ImplementationAvailable(){
//		return isBeanValidationImplementationAvailable();
//	}

	public GridLayout getSgpGridLayout() {
		return sgpGridLayout;
	}

	public void setSgpGridLayout(GridLayout sgpGridLayout) {
		this.sgpGridLayout = sgpGridLayout;
	}
	
	public FormLayout getSgpFormLayout() {
		return sgpFormLayout;
	}

	public void setSgpFormLayout(FormLayout sgpFormLayout) {
		this.sgpFormLayout = sgpFormLayout;
	}

	public void bindAndSetPositionGridLayout(Field field, Object propertyId, int column, int row){
		this.bind(field, propertyId);
		this.sgpGridLayout.addComponent(field, column, row);
	}//public void bindAndSetPosition(Field field, Object propertyId, int column, int row)
	
	public void bindAndSetPositionGridLayout(Object propertyId, String caption, int column, int row, boolean immediate){
		Field field = this.buildAndBind(caption, propertyId);
		if(field.getClass() == TextField.class)	((TextField)field).setNullRepresentation("");
		((AbstractComponent)field).setImmediate(immediate);
		this.sgpGridLayout.addComponent(field, column, row);
	}//public void bindAndSetPosition(Object propertyId, String caption, int column, int row)

	public void bindAndSetPositionGridLayout(Object propertyId, String caption, int column, int row, boolean immediate, int withInPercent, boolean required, String requiredMessage){
		this.textFieldField = (TextField) this.buildAndBind(caption, propertyId);
//		this.textFieldField.removeAllValidators();
//		this.beanValidator = new BeanValidator(Moneda.class, propertyId.toString());
//		this.beanValidator.setLocale(new Locale("es_PY"));
//		this.textFieldField.addValidator(this.beanValidator);
		this.addExplicitValidateBlurListener(this.textFieldField);
		this.textFieldField.setNullRepresentation("");
		this.textFieldField.setRequired(required);
		if(required) this.textFieldField.setRequiredError(requiredMessage);
		this.textFieldField.setImmediate(immediate);		
		if(withInPercent > 0) this.textFieldField.setWidth(String.valueOf(withInPercent) + "%");
		this.sgpGridLayout.addComponent(this.textFieldField, column, row);
	}
	
	public void bindAndSetPositionFormLayoutTextField
	(Object propertyId, String caption, boolean immediate, int withInPercent, boolean required, String requiredMessage, boolean enabled){
		this.textFieldField = (TextField) this.buildAndBind(caption, propertyId);
		this.addExplicitValidateBlurListener(this.textFieldField);
		this.textFieldField.setNullRepresentation("");
		this.textFieldField.setRequired(required);
		if(required) this.textFieldField.setRequiredError(requiredMessage);
		this.textFieldField.setImmediate(immediate);		
		if(withInPercent > 0) this.textFieldField.setWidth(String.valueOf(withInPercent) + "%");
		this.textFieldField.setEnabled(enabled);
		this.sgpFormLayout.addComponent(this.textFieldField);
	}//public void bindAndSetPositionFormLayoutTextField

	
	public void bindAndSetPositionFormLayoutTextField
	(Object propertyId, String caption, boolean immediate, 
			int withInPercent, boolean required, String requiredMessage, boolean enabled,
			List<ShortcutListener> listShortcutListener){
		this.textFieldField = (TextField) this.buildAndBind(caption, propertyId);
		this.addExplicitValidateBlurListener(this.textFieldField);
		this.textFieldField.setNullRepresentation("");
		this.textFieldField.setRequired(required);
		if(required) this.textFieldField.setRequiredError(requiredMessage);
		this.textFieldField.setImmediate(immediate);		
		if(withInPercent > 0) this.textFieldField.setWidth(String.valueOf(withInPercent) + "%");
		this.textFieldField.setEnabled(enabled);
		for(ShortcutListener vShortcutListener : listShortcutListener)
			this.textFieldField.addShortcutListener(vShortcutListener);
		this.sgpFormLayout.addComponent(this.textFieldField);
	}//public void bindAndSetPositionFormLayoutTextField

	
	public void bindAndSetPositionFormLayoutTextField
	(Object propertyId, String caption, boolean immediate, 
			int withInPercent, boolean required, String requiredMessage, boolean enabled,
			FocusListener vFocusListener, BlurListener vBlurListener){
		this.textFieldField = (TextField) this.buildAndBind(caption, propertyId);
		this.addExplicitValidateBlurListener(this.textFieldField);
		this.textFieldField.setNullRepresentation("");
		this.textFieldField.setRequired(required);
		if(required) this.textFieldField.setRequiredError(requiredMessage);
		this.textFieldField.setImmediate(immediate);		
		if(withInPercent > 0) this.textFieldField.setWidth(String.valueOf(withInPercent) + "%");
		this.textFieldField.setEnabled(enabled);
		if(vFocusListener!=null)this.textFieldField.addFocusListener(vFocusListener);		
		if(vBlurListener!=null)this.textFieldField.addBlurListener(vBlurListener);
		this.sgpFormLayout.addComponent(this.textFieldField);
	}//public void bindAndSetPositionFormLayoutTextField
	
	public void bindAndSetPositionFormLayoutCheckBox(Object propertyId, String labelCaption, String checkBoxCaption, boolean initialState, boolean enabled){
		this.wrap = new HorizontalLayout();
		this.wrap.setSpacing(true);
		this.wrap.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		this.wrap.setCaption(labelCaption);
		this.checkBoxField = (CheckBox) this.buildAndBind(checkBoxCaption, propertyId);
		this.checkBoxField.setValue(initialState);
		this.checkBoxField.setEnabled(enabled);
		this.wrap.addComponent(this.checkBoxField);
		this.sgpFormLayout.addComponent(this.wrap);
		
	}//public void bindAndSetPositionFormLayoutCheckBox(Object propertyId, String caption)

	public void bindAndSetPositionFormLayoutCheckBox(Object propertyId, 
			String labelCaption,
			String checkBoxCaption, 
			boolean initialState, 
			boolean enabled, ValueChangeListener valueChangeListener){
		this.wrap = new HorizontalLayout();
		this.wrap.setSpacing(true);
		this.wrap.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		this.wrap.setCaption(labelCaption);
		this.checkBoxField = (CheckBox) this.buildAndBind(checkBoxCaption, propertyId);
		this.checkBoxField.setValue(initialState);
		this.checkBoxField.setEnabled(enabled);
		this.checkBoxField.addValueChangeListener(valueChangeListener);
		this.wrap.addComponent(this.checkBoxField);
		this.sgpFormLayout.addComponent(this.wrap);
		
	}//public void bindAndSetPositionFormLayoutCheckBox(Object propertyId, String caption)
	
	private void addExplicitValidateBlurListener(final TextField textField){
		textField.addBlurListener(new BlurListener(){
			//added because the @NotEmpty does not work with the textFieldField.setRequired(true); 
			@Override
			public void blur(BlurEvent event) {
				// TODO Auto-generated method stub
				try{
					//textFieldField.setValidationVisible(true);
					if(textField.getValue()==null || textField.getValue().isEmpty())
						textField.validate();
					if(textField.getIcon()!=null)
						textField.setIcon(null);
				}catch(Exception e){					
					logger.info("\n*******************SgpForm<T>.bindAndSetPositionFormLayoutTextField\nvalidation for text field executed with exception"
							+ "\nexception : " + e + "\n*******************"
							+ "\nexception message : " + e.getMessage() + "\n*******************");
					//if(e != null && e.getMessage() != null )
						textField.setIcon(new ThemeResource("../dashboard/myicons/form/Warning-16x16.png"));
				}
			}
			
		});		
	}
	
	private void addExplicitValidateBlurListenerHandlingValidationVisibility(final TextField textField){
		textField.addBlurListener(new BlurListener(){
			//added because the @NotEmpty does not work with the textFieldField.setRequired(true); 
			@Override
			public void blur(BlurEvent event) {
				// TODO Auto-generated method stub
				try{
					textField.setValidationVisible(true);
					if(textField.getValue()==null || textField.getValue().isEmpty())
						textField.validate();
					if(textField.getIcon()!=null)
						textField.setIcon(null);
				}catch(Exception e){
					textField.setIcon(new ThemeResource("../dashboard/myicons/form/Warning-16x16.png"));
					logger.info("\n*******************SgpForm<T>.bindAndSetPositionFormLayoutTextField\nvalidation for text field executed with exception"
							+ "\nexception message" + e.getMessage() + "\n*******************");
				}
			}
			
		});		
	}
	
	private void addExplicitValidateBlurListenerDateFieldHandlingValidationVisibility(final DateField dateField){
		dateField.addBlurListener(new BlurListener(){
			//added because the @NotEmpty does not work with the textFieldField.setRequired(true); 
			@Override
			public void blur(BlurEvent event) {
				// TODO Auto-generated method stub
				try{
					dateField.setValidationVisible(true);
					if(dateField.getValue()==null /*|| dateField.getValue().isEmpty()*/)
						dateField.validate();
					if(dateField.getIcon()!=null)
						dateField.setIcon(null);
				}catch(Exception e){
					dateField.setIcon(new ThemeResource("../dashboard/myicons/form/Warning-16x16.png"));
					logger.info("\n*******************SgpForm<T>.bindAndSetPositionFormLayoutTextField\nvalidation for text field executed with exception"
							+ "\nexception message" + e.getMessage() + "\n*******************");
				}
			}
			
		});		
	}
	
	public TextField bindAndBuildTextField(Object propertyId, String caption, boolean immediate, int withInPercent, boolean required, String requiredMessage){
		TextField textField = (TextField) this.buildAndBind(caption, propertyId);
		textField.setNullRepresentation("");
		textField.setImmediate(immediate);
		textField.setRequired(required);
		if(required) textField.setRequiredError(requiredMessage);
		if(withInPercent > 0) textField.setWidth(String.valueOf(withInPercent) + "%");
		return textField;
	}

	public TextField bindAndBuildTextFieldWithExplicitValidateBlurListener(Object propertyId, String caption, boolean immediate, int withInPercent, boolean required, String requiredMessage, boolean validationVisible){
		TextField textField = (TextField) this.buildAndBind(caption, propertyId);
		this.addExplicitValidateBlurListenerHandlingValidationVisibility(textField);
		textField.setValidationVisible(validationVisible);
		textField.setNullRepresentation("");
		textField.setImmediate(immediate);
		textField.setRequired(required);
		if(required) textField.setRequiredError(requiredMessage);
		if(withInPercent > 0) textField.setWidth(String.valueOf(withInPercent) + "%");
		return textField;
	}
	
	public DateField bindAndBuildDateField(Object propertyId, String caption, boolean immediate, int withInPercent, boolean required, String requiredMessage, boolean validationVisible){
		DateField dateField = (DateField) this.buildAndBind(caption, propertyId);
		dateField.setValidationVisible(validationVisible);
		this.addExplicitValidateBlurListenerDateFieldHandlingValidationVisibility(dateField);
		dateField.setImmediate(immediate);
		dateField.setRequired(required);
		if(required) dateField.setRequiredError(requiredMessage);
		if(withInPercent > 0) dateField.setWidth(String.valueOf(withInPercent) + "%");
		return dateField;
	}
	
	public void bindAndSetPositionFormLayoutDateField
	(Object propertyId, 
			String caption, 
			boolean immediate, 
			int withInPercent, 
			boolean required, 
			String requiredMessage, 
			boolean validationVisible, 
			boolean enabled){
		this.dateField = (DateField) this.buildAndBind(caption, propertyId);
		this.dateField.setValidationVisible(validationVisible);
		this.addExplicitValidateBlurListenerDateFieldHandlingValidationVisibility(this.dateField);
		this.dateField.setImmediate(immediate);
		this.dateField.setRequired(required);
		this.dateField.setEnabled(enabled);
		if(required) this.dateField.setRequiredError(requiredMessage);
		if(withInPercent > 0) this.dateField.setWidth(String.valueOf(withInPercent) + "%");
		this.sgpFormLayout.addComponent(this.dateField);
	}
}//public class SgpForm<T> extends BeanFieldGroup<T> 
