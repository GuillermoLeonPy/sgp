package py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessactivityregisterformview.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessDTO;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.ProductionProcessActivityRegisterFormView;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.ConfirmOptionsWindow;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.ConfirmWindowDecision;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.ConfirmOptionsWindow.SelectOneOption;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ProductionProcessActivityDTOTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(ProductionProcessActivityDTOTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.activity.register.form.";
	private final ProductionProcessActivityDTO productionProcessActivityDTO;
	private final ProductionProcessDTO productionProcessDTO;
	private SgpForm<ProductionProcessActivityDTO> productionProcessActivityDTOForm;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final ProductionProcessActivityRegisterFormView productionProcessActivityRegisterFormView;
	private final boolean editFormMode;
	private ComboBox productionProcessActivityDTOComboBox;
	private ConfirmOptionsWindow vConfirmOptionsWindow;
	private SelectOneOption vSelectOneOptionSelectedOption;
	List<ProductionProcessActivityDTO> previousActivitySelectListProductionProcessActivityDTO;
	
	public ProductionProcessActivityDTOTabLayout(ProductionProcessActivityRegisterFormView productionProcessActivityRegisterFormView,ProductionProcessActivityDTO productionProcessActivityDTO, ProductionProcessDTO productionProcessDTO, boolean editFormMode) {
		// TODO Auto-generated constructor stub
		this.productionProcessActivityRegisterFormView = productionProcessActivityRegisterFormView;
		this.productionProcessActivityDTO = productionProcessActivityDTO;
		this.productionProcessDTO = productionProcessDTO;
		this.editFormMode = editFormMode;
		try{
			logger.info("\n ProductionProcessActivityDTOTabLayout..");
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

	/*public ProductionProcessActivityDTOTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	private void setUpproductionProcessActivityDTOForm(){
		this.productionProcessActivityDTOForm = new SgpForm<ProductionProcessActivityDTO>(ProductionProcessActivityDTO.class, new BeanItem<ProductionProcessActivityDTO>(this.productionProcessActivityDTO), ValoTheme.FORMLAYOUT_LIGHT/*"light"*/, true);
		this.productionProcessActivityDTOForm.bindAndSetPositionFormLayoutTextField("activity_id", this.messages.get("application.common.identifier.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "tab.production.process.activity.register.form.text.field.activity.id.required.message"), true);
		this.productionProcessActivityDTOForm.bindAndSetPositionFormLayoutTextField("activity_description", this.messages.get("application.common.description.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "tab.production.process.activity.register.form.text.field.activity.description.required.message"), true);
		this.productionProcessActivityDTOForm.bindAndSetPositionFormLayoutTextField("minutes_quantity", this.messages.get("application.common.time.space.minutes.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "tab.production.process.activity.register.form.text.field.activity.minutes.quantity.required.message"), true);
		this.productionProcessActivityDTOForm.getSgpFormLayout().addComponent(this.buildProductionProcessActivityDTOComboBox());
	}
	
	private void setUpLayoutContent(){
		//this.initTitles();
    	this.setUpproductionProcessActivityDTOForm();
		final Button okButton = new Button(this.messages.get("application.common.button.save.label"));/*"Cancelar"*/
		okButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		productionProcessActivityDTOForm.commit();
		            		productionProcessActivityDTOComboBox.validate();
		            		productionProcessActivityDTOComboBox.commit();
		            		//productionProcessActivityRegisterFormView.saveButtonActionProductionProcessActivityDTOTabLayout(productionProcessActivityDTO, editFormMode);
		            		setUpConfirmOptionsWindow();		            		
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
		            		productionProcessActivityDTOForm.discard();
		            		productionProcessActivityDTOComboBox.discard();
		            		productionProcessActivityRegisterFormView.navigateToCallerView(DashboardViewType.PRODUCTION_PROCESS_REGISTER_FORM.getViewName());
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
		
		this.productionProcessActivityDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);		
		this.addComponent(this.productionProcessActivityDTOForm.getSgpFormLayout());
		//this.addComponent(new Label("hola"));
	}
	
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n ProductionProcessActivityDTOTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private Component buildProductionProcessActivityDTOComboBox(){
		this.productionProcessActivityDTOComboBox = new ComboBox(this.messages.get(this.VIEW_NAME + "tab.production.process.activity.register.form.previous.production.process.activity.combo.box.label"));
		this.productionProcessActivityDTOComboBox.setInputPrompt(this.messages.get(this.VIEW_NAME + "tab.production.process.activity.register.form.previous.production.process.activity.combo.box.input.prompt"));
        BeanItemContainer<ProductionProcessActivityDTO> ProductionProcessActivityDTOBeanItemContainer = new BeanItemContainer<ProductionProcessActivityDTO>(ProductionProcessActivityDTO.class);
        if(this.productionProcessDTO.getListProductionProcessActivityDTO() != null)
        	ProductionProcessActivityDTOBeanItemContainer.addAll(this.buildPreviousActivitySelectListProductionProcessActivityDTO());
        else
        	ProductionProcessActivityDTOBeanItemContainer.addAll(new ArrayList<ProductionProcessActivityDTO>());
        this.productionProcessActivityDTOComboBox.setContainerDataSource(ProductionProcessActivityDTOBeanItemContainer);
        this.productionProcessActivityDTOComboBox.setItemCaptionPropertyId("activity_id");
        
        if(/*this.editFormMode*/this.productionProcessActivityDTO.getId_previous_activity()!=null){
        	this.productionProcessActivityDTOComboBox.select(this.findPreviosActivity());
        	this.productionProcessActivityDTOComboBox.setValue(this.findPreviosActivity());
        }
        this.productionProcessActivityDTOComboBox.setNullSelectionAllowed(false);
        this.productionProcessActivityDTOComboBox.addStyleName("small");
        this.productionProcessActivityDTOComboBox.addValueChangeListener(this.setUpValueChangeListenerForproductionProcessActivityDTOComboBox());
        this.productionProcessActivityDTOComboBox.setRequired(this.previousActivitySelectListProductionProcessActivityDTO != null && !this.previousActivitySelectListProductionProcessActivityDTO.isEmpty());
        this.productionProcessActivityDTOComboBox.setRequiredError(this.messages.get(this.VIEW_NAME + "tab.production.process.activity.register.form.previous.production.process.activity.combo.box.required.message"));
        this.productionProcessActivityDTOComboBox.setEnabled(this.previousActivitySelectListProductionProcessActivityDTO != null && !this.previousActivitySelectListProductionProcessActivityDTO.isEmpty());
        this.productionProcessActivityDTOComboBox.setWidth(100, Unit.PERCENTAGE);
        return this.productionProcessActivityDTOComboBox;
	}
	
	private ProductionProcessActivityDTO findPreviosActivity(){
		for(ProductionProcessActivityDTO vProductionProcessActivityDTO: this.productionProcessDTO.getListProductionProcessActivityDTO()){
			if(vProductionProcessActivityDTO.getId().equals(this.productionProcessActivityDTO.getId_previous_activity()))
				return vProductionProcessActivityDTO;
		}
		logger.info("\n************************************************************************"
					+"\n find previous activity method did not found an element, returning null"
					+"\n************************************************************************");
		return null;
	}
	
	private Property.ValueChangeListener setUpValueChangeListenerForproductionProcessActivityDTOComboBox(){
    	return new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				logger.info("\nTariffComboBox combo box: value change event"
							+"\n======================================"	+"\nevent.getProperty().getType().getName()) : " 
							+ event.getProperty().getType().getName()
							+"\nevent.getProperty().getValue() : \n" + event.getProperty().getValue());
				productionProcessActivityDTO.setId_previous_activity(((ProductionProcessActivityDTO)event.getProperty().getValue()).getId());
			}
    	};
	}
	
	private void setUpConfirmOptionsWindow(){
		this.vConfirmOptionsWindow = new ConfirmOptionsWindow();
		List<SelectOneOption> listSelectOneOption = new ArrayList<SelectOneOption>();
		listSelectOneOption.add(
		this.vConfirmOptionsWindow.new SelectOneOption("application.common.preliminary.save.action",this.messages.get("application.common.preliminary.save.action"))
		);
		listSelectOneOption.add(
		this.vConfirmOptionsWindow.new SelectOneOption("application.common.definitive.save.action",this.messages.get("application.common.definitive.save.action"))
		);		
		
		this.vConfirmOptionsWindow = new ConfirmOptionsWindow(
				this.messages.get(this.VIEW_NAME + "tab.production.process.activity.register.form.confirm.options.window.caption"), 
				this.messages.get(this.VIEW_NAME + "tab.production.process.activity.register.form.confirm.options.window.question"), 
				this.messages.get("application.common.button.save.label"), 
				this.messages.get("application.common.button.cancel.label"), 
				listSelectOneOption,
				"");
		this.vConfirmOptionsWindow.addCloseListener(this.setUpConfirmOptionsWindowCloseListener());
		this.vConfirmOptionsWindow.setDecision(new ConfirmOptionsWindowOkCancelHandler());
		this.vConfirmOptionsWindow.adjuntWindowSizeAccordingToCientDisplay();
	}
	
	private CloseListener setUpConfirmOptionsWindowCloseListener(){
		return new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{
					//vSelectOneOptionSelectedOption = (SelectOneOption)vConfirmOptionsWindow.getSelectOptionGroup().getValue();
					logger.info( "\n******************************************************"
								+"\n the confirm option window has been closed"
								+"\n option selected: \n" + vSelectOneOptionSelectedOption
								+"\n******************************************************");
					
				}catch(Exception ex){
					commonExceptionErrorNotification.showErrorMessageNotification(ex);
				}
			}			
		};
	}
	
	private class ConfirmOptionsWindowOkCancelHandler implements ConfirmWindowDecision{

		@Override
		public void yes(ClickEvent event) {
			// TODO Auto-generated method stub
			try{
				vSelectOneOptionSelectedOption = (SelectOneOption)vConfirmOptionsWindow.getSelectOptionGroup().getValue();		
				productionProcessActivityRegisterFormView.saveButtonActionProductionProcessActivityDTOTabLayout
				(productionProcessActivityDTO, 
						editFormMode, 
						vSelectOneOptionSelectedOption.getKey().equals("application.common.definitive.save.action") ? true : false);
				commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
        	}catch(Exception e){
        		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
        	}
		}

		@Override
		public void no(ClickEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}

	private List<ProductionProcessActivityDTO> buildPreviousActivitySelectListProductionProcessActivityDTO(){
		this.previousActivitySelectListProductionProcessActivityDTO = 
				new ArrayList<ProductionProcessActivityDTO>(productionProcessDTO.getListProductionProcessActivityDTO());
		if(this.previousActivitySelectListProductionProcessActivityDTO.contains(this.productionProcessActivityDTO))
			this.previousActivitySelectListProductionProcessActivityDTO.remove(this.productionProcessActivityDTO);
		return this.previousActivitySelectListProductionProcessActivityDTO;
	}
}
