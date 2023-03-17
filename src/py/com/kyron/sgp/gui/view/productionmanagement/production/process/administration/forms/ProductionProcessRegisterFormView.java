package py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.ProductDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.ProductionProcessManagementOperationViewEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.ProductionProcessRegisterFormViewEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.ProductionProcessActivityRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessregisterformview.components.ProductionProcessActivityDTOTableTabLayout;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.productionprocessregisterformview.components.ProductionProcessDTOTabLayout;

import com.google.common.eventbus.Subscribe;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ProductionProcessRegisterFormView extends VerticalLayout implements
		View {

	private final Logger logger = LoggerFactory.getLogger(ProductionProcessRegisterFormView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.register.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private ProductionProcessDTO productionProcessDTO;
	private ProductDTO productDTO;
	private BussinesSessionUtils bussinesSessionUtils;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private String CALLER_VIEW;
	private TabSheet tabs;
	private Tab productionProcessDTOTabLayoutTab;
	private Tab productionProcessActivityDTOTableTabLayoutTab;
	private VerticalLayout mainViewLayout;
	private String selectedTabContentComponentId;
	private boolean editFormMode;
	private Button process_pending_to_be_savedButton;
	
	public ProductionProcessRegisterFormView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n ProductionProcessRegisterFormView..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();			
	        this.addStyleName("v-scrollable");
	        this.setHeight("100%");
	        Responsive.makeResponsive(this);
			DashboardEventBus.register(this);
			
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	private void initServices() throws Exception{
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	public ProductionProcessRegisterFormView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		logger.info("\n==========================================="
					+"\n ProductionProcessRegisterFormView"
					+"\n a view change event has been registered"
					+"\n===========================================");
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n ProductionProcessRegisterFormView" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()

	@Subscribe
	public void editProductProductionProcess(final ProductionProcessRegisterFormViewEvent vProductionProcessRegisterFormViewEvent){
		try{
			this.CALLER_VIEW = vProductionProcessRegisterFormViewEvent.getCallerView();
			this.productDTO = vProductionProcessRegisterFormViewEvent.getProductDTO();
			this.productionProcessDTO = vProductionProcessRegisterFormViewEvent.getProductionProcessDTO();
			this.editFormMode = vProductionProcessRegisterFormViewEvent.isEditFormMode();
			this.printParameterData();
			this.initMainViewLayout();
			this.initTitles();
			this.initTabSheet();
			this.setUpProductionProcessDTOTabLayoutTab();
		    this.setUpProductionProcessActivityDTOTableTabLayoutTab();
		    this.mainViewLayout.addComponent(this.tabs);
		    this.removeAndReAddMainViewLayout();
		    this.refreshTabSelection();
		}catch(Exception e){
			logger.error("\nerror", e);
		}		
	}
	
	private void refreshTabSelection(){
		logger.info("\n##############################"
					+"\n refreshing tab selection"
					+"\n##############################");
		//this.tabs.setSelectedTab(1);
		//this.tabs.markAsDirty();
		int compomentCount = this.tabs.getComponentCount();
		for(int counter = (compomentCount - 1); counter >= 0 ; counter--){
			this.tabs.setSelectedTab(counter);
			logger.info("\n tab in position: "+ counter + " has been selected...");
		}
		this.tabs.markAsDirty();
	}
	
	private void printParameterData(){
		logger.info("\n============================================"
					+"\n Production process\n------------------"
					+"\n"+this.productionProcessDTO.toString()
					+"\n Product\n----------"
					+"\n"+this.productDTO.toString()
					+"\n============================================");
	}
	
	private void removeAndReAddMainViewLayout(){
		this.removeAllComponents();
		this.addComponent(this.mainViewLayout);
	}
	
	
	
	private void initMainViewLayout(){
		this.mainViewLayout = new VerticalLayout();
		this.mainViewLayout.setSpacing(true);
		this.mainViewLayout.setMargin(true);
	}
	
	private void initTitles(){
		Label title = new Label(!this.editFormMode ? this.messages.get(this.VIEW_NAME + "main.title.register") : this.messages.get(this.VIEW_NAME + "main.title.edit"));
       	title.addStyleName("h1");
       	
		Label productionProcessLabel = new Label(this.messages.get("application.common.product.label") + ":");
		productionProcessLabel.addStyleName(ValoTheme.LABEL_H4);
		productionProcessLabel.addStyleName(ValoTheme.LABEL_COLORED);
       	Label productionProcessTitle = new Label(this.productDTO.getProduct_id());
       	productionProcessTitle.addStyleName(ValoTheme.LABEL_H4);
       	productionProcessTitle.addStyleName(ValoTheme.LABEL_COLORED);
        HorizontalLayout hl = new HorizontalLayout(productionProcessLabel,productionProcessTitle);
        hl.addStyleName("wrapping");
        hl.setSpacing(true);
        
		this.process_pending_to_be_savedButton = new Button();
		this.process_pending_to_be_savedButton.addStyleName("borderless");
		this.process_pending_to_be_savedButton.setIcon(FontAwesome.SAVE);		
		this.process_pending_to_be_savedButton.setVisible(
				this.CALLER_VIEW.equals(DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_REGISTER_FORM.getViewName())
				&&
				this.productionProcessDTO.getId()==null ? true : false
				);		
		this.process_pending_to_be_savedButton.setDescription(messages.get(VIEW_NAME + "main.title.button.process.pending.to.be.saved.description"));
		this.process_pending_to_be_savedButton.setResponsive(false);
		hl.addComponent(this.process_pending_to_be_savedButton);             
       	this.mainViewLayout.addComponent(title);
       	this.mainViewLayout.addComponent(hl);
	}
	
	private void initTabSheet(){
    	this.tabs = new TabSheet();
    	this.tabs.setId("tabs");
    	this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
    	this.tabs.addStyleName("framed");
    	//this.tabs.setWidth("100%");
	}
	
    private SelectedTabChangeListener setUpTabsSelectionListener(){
    	return new SelectedTabChangeListener(){
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				// TODO Auto-generated method stub				
				selectedTabContentComponentId =  event.getTabSheet().getSelectedTab().getId();
				logger.info("\ntab selection listener"
						+"\n selectedTabContentComponentId : " + selectedTabContentComponentId);			

			}    		
    	};
    }
    
    public void saveButtonActionProductionProcessDTOTabLayout(ProductionProcessDTO productionProcessDTO, boolean editFormMode, boolean saveDefinitiveMode){
    	if(!editFormMode)this.productionProcessDTO.setId_product(this.productDTO.getId());
    	
    	if(saveDefinitiveMode)
    		logger.info("\n ================== \n saving to database \n ================== ");
    	else{
    		logger.info("\n ================== \n not saving to database, just keeping in memory \n ================== ");
    		this.process_pending_to_be_savedButton.setVisible(this.productionProcessDTO.getId()==null);
    		this.process_pending_to_be_savedButton.markAsDirty();
    	}
    }
    
	public void navigateToCallerView(){
		logger.info("\n ProductionProcessRegisterFormView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new ProductionProcessManagementOperationViewEvent(this.productDTO, this.CALLER_VIEW));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	public void navigateToCallerView(String viewName){
		logger.info("\n ProductionProcessRegisterFormView.navigateToCallerView()\nviewName : " + viewName
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(viewName);
			DashboardEventBus.post(new ProductionProcessManagementOperationViewEvent(this.productDTO, viewName));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	private void setUpProductionProcessDTOTabLayoutTab(){		
		ProductionProcessDTOTabLayout vProductionProcessDTOTabLayout = new ProductionProcessDTOTabLayout(this,this.productionProcessDTO, this.editFormMode);
		vProductionProcessDTOTabLayout.setId("vProductionProcessDTOTabLayout");
    	this.productionProcessDTOTabLayoutTab = this.tabs.addTab(vProductionProcessDTOTabLayout, this.messages.get(this.VIEW_NAME + "tab.production.process.register.form"));
    	
    	this.productionProcessDTOTabLayoutTab.setClosable(false);
    	this.productionProcessDTOTabLayoutTab.setEnabled(true);
    	this.productionProcessDTOTabLayoutTab.setIcon(FontAwesome.BOOK);
	}
	
	public void editProductionProcessActivityDTO(ProductionProcessActivityDTO vProductionProcessActivityDTO){
		try{			
			if(vProductionProcessActivityDTO==null)
				vProductionProcessActivityDTO = new ProductionProcessActivityDTO(null,Math.round(Math.random() * 10000));
			
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PRODUCTION_PROCESS_ACTIVITY_REGISTER_FORM.getViewName());
			DashboardEventBus.post(new ProductionProcessActivityRegisterFormViewEvent(
					this.productDTO,
					this.productionProcessDTO,
					vProductionProcessActivityDTO,
					DashboardViewType.PRODUCTION_PROCESS_REGISTER_FORM.getViewName(),
					vProductionProcessActivityDTO !=null 
					&& vProductionProcessActivityDTO.getActivity_id()!=null));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	private void setUpProductionProcessActivityDTOTableTabLayoutTab(){
		if(this.productionProcessActivityDTOTableTabLayoutTab!=null){
			this.tabs.removeTab(this.productionProcessActivityDTOTableTabLayoutTab);
			//this.rawMaterialExistenceDTOTableTabLayoutReFreshed = true;
		}
		ProductionProcessActivityDTOTableTabLayout vProductionProcessActivityDTOTableTabLayout = 
				new ProductionProcessActivityDTOTableTabLayout(
						this, 
						this.productionProcessDTO);
		vProductionProcessActivityDTOTableTabLayout.setId("vProductionProcessActivityDTOTableTabLayout");
		this.productionProcessActivityDTOTableTabLayoutTab = 
				this.tabs.addTab(vProductionProcessActivityDTOTableTabLayout,this.messages.get(this.VIEW_NAME + "tab.production.process.activities")); 
		this.productionProcessActivityDTOTableTabLayoutTab.setClosable(false);
		this.productionProcessActivityDTOTableTabLayoutTab.setEnabled(true);
		this.productionProcessActivityDTOTableTabLayoutTab.setIcon(FontAwesome.STAR_HALF_EMPTY);
		
	}
}
