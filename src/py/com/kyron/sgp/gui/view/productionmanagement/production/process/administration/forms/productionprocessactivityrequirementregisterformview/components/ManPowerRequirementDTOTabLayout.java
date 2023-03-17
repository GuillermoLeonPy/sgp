package py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessactivityrequirementregisterformview.components;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ManPowerRequirementDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ManPowerRequirementDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.ProductionProcessActivityRequirementRegisterFormView;

import com.vaadin.data.util.BeanItem;
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
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ManPowerRequirementDTOTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(ManPowerRequirementDTOTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.activity.requeriment.register.form.";
	private final ProductionProcessActivityDTO productionProcessActivityDTO;
	private final ManPowerRequirementDTO manPowerRequirementDTO;
	private SgpForm<ManPowerRequirementDTO> manPowerRequirementDTOForm;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final ProductionProcessActivityRequirementRegisterFormView productionProcessActivityRequirementRegisterFormView;
	private final boolean editFormMode;
    private BussinesSessionUtils bussinesSessionUtils;
    private ProductionManagementService productionManagementService;
    
	public ManPowerRequirementDTOTabLayout(ProductionProcessActivityRequirementRegisterFormView productionProcessActivityRequirementRegisterFormView,ProductionProcessActivityDTO productionProcessActivityDTO,ManPowerRequirementDTO manPowerRequirementDTO,boolean editFormMode) {
		// TODO Auto-generated constructor stub
		this.productionProcessActivityRequirementRegisterFormView = productionProcessActivityRequirementRegisterFormView;
		this.productionProcessActivityDTO = productionProcessActivityDTO;
		this.manPowerRequirementDTO = manPowerRequirementDTO;
		this.editFormMode = editFormMode;
		try{
			logger.info("\n ManPowerRequirementDTOTabLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();			
	        //this.addStyleName("v-scrollable");
	        //this.setHeight("100%");
			this.initServices();
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

	/*public ManPowerRequirementDTOTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	private void initServices(){
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n ManPowerRequirementDTOTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void setUpmanPowerRequirementDTOForm() throws PmsServiceException{
		this.manPowerRequirementDTOForm = new SgpForm<ManPowerRequirementDTO>(ManPowerRequirementDTO.class, new BeanItem<ManPowerRequirementDTO>(this.manPowerRequirementDTO), ValoTheme.FORMLAYOUT_LIGHT/*"light"*/, true);
		this.manPowerRequirementDTOForm.bindAndSetPositionFormLayoutTextField("minutes_quantity", this.messages.get("application.common.time.space.minutes.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "tab.man.power.requeriment.text.field.minutes.quantity.required.message"), true);
		
	}
	
	private void setUpLayoutContent() throws PmsServiceException{
		//this.initTitles();
    	this.setUpmanPowerRequirementDTOForm();
		final Button okButton = new Button(this.messages.get("application.common.button.save.label"));/*"Cancelar"*/
		okButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{		            		
		            		manPowerRequirementDTOForm.commit();		            		
		            		productionProcessActivityRequirementRegisterFormView.saveButtonActionManPowerRequirementDTOTabLayout(manPowerRequirementDTO, editFormMode);
		            		commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
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
		            		manPowerRequirementDTOForm.discard();
		            		productionProcessActivityRequirementRegisterFormView.navigateToCallerView(DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_REGISTER_FORM.getViewName());
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
		
		this.manPowerRequirementDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);		
		this.addComponent(this.manPowerRequirementDTOForm.getSgpFormLayout());
		//this.addComponent(new Label("hola"));
	}
}
