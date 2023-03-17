package py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessregisterformview.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessDTO;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.productionmanagement.ProductionProcessManagementOperationView;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.ProductionProcessRegisterFormView;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.ConfirmOptionsWindow;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.ConfirmOptionsWindow.SelectOneOption;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.ConfirmWindowDecision;

import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

@SuppressWarnings("serial")
public class ProductionProcessDTOTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(ProductionProcessDTOTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.register.form.";
	private final ProductionProcessDTO productionProcessDTO;
	private SgpForm<ProductionProcessDTO> productionProcessDTOForm;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final ProductionProcessRegisterFormView productionProcessRegisterFormView;
	private final boolean editFormMode;
	private ConfirmOptionsWindow vConfirmOptionsWindow;
	private SelectOneOption vSelectOneOptionSelectedOption;
	
	public ProductionProcessDTOTabLayout(ProductionProcessRegisterFormView productionProcessRegisterFormView, ProductionProcessDTO productionProcessDTO, boolean editFormMode) {
		// TODO Auto-generated constructor stub
		this.productionProcessRegisterFormView = productionProcessRegisterFormView;
		this.productionProcessDTO = productionProcessDTO;
		this.editFormMode = editFormMode;
		try{
			logger.info("\n ProductionProcessDTOTabLayout..");
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

	/*public ProductionProcessDTOTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	private void initTitles(){
		Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.production.process.register.form"));
        title.addStyleName("h1");
        this.addComponent(title);

	}
	
	private void setUpproductionProcessDTOForm(){
		this.productionProcessDTOForm = new SgpForm<ProductionProcessDTO>(ProductionProcessDTO.class, new BeanItem<ProductionProcessDTO>(this.productionProcessDTO), /*ValoTheme.FORMLAYOUT_LIGHT*/"light", true);
		this.productionProcessDTOForm.bindAndSetPositionFormLayoutTextField("process_id", this.messages.get("application.common.identifier.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "tab.production.process.register.form.text.field.process.id.required.message"), true);
		this.productionProcessDTOForm.bindAndSetPositionFormLayoutTextField("process_description", this.messages.get("application.common.description.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "tab.production.process.register.form.text.field.description.required.message"), true);
	}
	
	private void setUpLayoutContent(){
		//this.initTitles();
    	this.setUpproductionProcessDTOForm();
		final Button okButton = new Button(this.messages.get("application.common.button.save.label"));/*"Cancelar"*/
		okButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		productionProcessDTOForm.commit();		            		
		            		//productionProcessRegisterFormView.saveButtonActionProductionProcessDTOTabLayout(productionProcessDTO, editFormMode);
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
		            		productionProcessDTOForm.discard();
		            		productionProcessRegisterFormView.navigateToCallerView(DashboardViewType.PRODUCTION_PROCESS_MANAGEMENT_OPERATION.getViewName());
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
		
		this.productionProcessDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);		
		this.addComponent(this.productionProcessDTOForm.getSgpFormLayout());
		//this.addComponent(new Label("hola"));
	}
	
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n ProductionProcessDTOTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
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
				this.messages.get(this.VIEW_NAME + "production.process.register.form.confirm.options.window.caption"), 
				this.messages.get(this.VIEW_NAME + "production.process.register.form.confirm.options.window.question"), 
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
			vSelectOneOptionSelectedOption = (SelectOneOption)vConfirmOptionsWindow.getSelectOptionGroup().getValue();		
			productionProcessRegisterFormView.saveButtonActionProductionProcessDTOTabLayout
			(productionProcessDTO, 
					editFormMode, 
					vSelectOneOptionSelectedOption.getKey().equals("application.common.definitive.save.action") ? true : false);
			commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		}

		@Override
		public void no(ClickEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
