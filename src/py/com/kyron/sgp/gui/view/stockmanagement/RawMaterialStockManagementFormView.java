package py.com.kyron.sgp.gui.view.stockmanagement;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialExistenceDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialPurchaseRequestDTO;
import py.com.kyron.sgp.bussines.stockmanagement.service.StockManagementService;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.RawMaterialStockManagementFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.stockmanagement.rawmaterialstockmanagementformview.RawMaterialDTOTabLayout;
import py.com.kyron.sgp.gui.view.stockmanagement.rawmaterialstockmanagementformview.RawMaterialExistenceDTOFormTabLayout;
import py.com.kyron.sgp.gui.view.stockmanagement.rawmaterialstockmanagementformview.RawMaterialExistenceDTOTableTabLayout;
import py.com.kyron.sgp.gui.view.stockmanagement.rawmaterialstockmanagementformview.RawMaterialPurchaseRequestDTOFormTabLayout;
import py.com.kyron.sgp.gui.view.stockmanagement.rawmaterialstockmanagementformview.RawMaterialPurchaseRequestDTOTableTabLayout;

import com.google.common.eventbus.Subscribe;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;

@SuppressWarnings("serial")
public class RawMaterialStockManagementFormView extends VerticalLayout
		implements View {

	private final Logger logger = LoggerFactory.getLogger(RawMaterialStockManagementFormView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "rawmaterial.stock.management.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private RawMaterialDTO rawMaterialDTO;
	private BussinesSessionUtils bussinesSessionUtils;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private String CALLER_VIEW;
	private TabSheet tabs;
	private Tab rawMaterialDTOTabLayoutTab;
	private Tab rawMaterialExistenceDTOTableTabLayout;
	private Tab rawMaterialExistenceDTOFormTabLayoutTab;
	private Tab rawMaterialPurchaseRequestDTOTableTabLayoutTab;
	private Tab rawMaterialPurchaseRequestDTOFormTabLayoutTab;
	private VerticalLayout mainViewLayout;
	private String selectedTabContentComponentId;
	private boolean rawMaterialExistenceDTOTableTabLayoutReFreshed;
	private boolean rawMaterialPurchaseRequestDTOTableTabLayoutReFreshed;
	private StockManagementService stockManagementService;
	private boolean flagEnableRawMaterialPurchaseRequestDTOFormTabLayout;
	
	public RawMaterialStockManagementFormView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\nRawMaterialStockManagementFormView..");
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
		this.stockManagementService = (StockManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.STOCK_MANAGEMENT_SERVICE);
	}


	public RawMaterialStockManagementFormView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		logger.info("\n==========================================="
					+"\n RawMaterialStockManagementFormView"
					+"\n a view change event has been registered"
					+"\n===========================================");
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n RawMaterialStockManagementFormView" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	@Subscribe
	public void manageRawMaterialStock(final RawMaterialStockManagementFormViewEvent vRawMaterialStockManagementFormViewEvent){
		try{
			this.CALLER_VIEW = vRawMaterialStockManagementFormViewEvent.getCallerView();
			this.rawMaterialDTO = vRawMaterialStockManagementFormViewEvent.getRawMaterialDTO();
			this.initMainViewLayout();
			this.initTitles();
			this.initTabSheet();
			this.setUpRawMaterialDataTab();
		    this.setUpRawMaterialExistenceDTOTableTabLayout();
		    this.setUpRawMaterialExistenceDTOFormTabLayout(new RawMaterialExistenceDTO());
		    this.setUpRawMaterialPurchaseRequestDTOTableTabLayout();
		    this.setFlagEnableRawMaterialPurchaseRequestDTOFormTabLayout();
		    this.setUpRawMaterialPurchaseRequestDTOFormTabLayout(new RawMaterialPurchaseRequestDTO());
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
		this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
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
		try{
			Label title = new Label(this.messages.get(this.VIEW_NAME + "main.title"));
        	title.addStyleName("h1");
        	this.mainViewLayout.addComponent(title);
		}catch(Exception e){
			commonExceptionErrorNotification.showErrorMessageNotification(e);
		}
	}
	
	private void initTabSheet(){
    	this.tabs = new TabSheet();
    	this.tabs.setId("tabs");
    	//this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
    	this.tabs.addStyleName("framed");	
	}
	
	private void setUpRawMaterialDataTab(){
    	RawMaterialDTOTabLayout vRawMaterialDTOTabLayout = new RawMaterialDTOTabLayout(this,this.rawMaterialDTO);
    	vRawMaterialDTOTabLayout.setId("vRawMaterialDTOTabLayout");
    	this.rawMaterialDTOTabLayoutTab = this.tabs.addTab(vRawMaterialDTOTabLayout, this.messages.get(this.VIEW_NAME + "tab.rawmaterial.data"));        
    	this.rawMaterialDTOTabLayoutTab.setClosable(false);
    	this.rawMaterialDTOTabLayoutTab.setEnabled(true);
    	this.rawMaterialDTOTabLayoutTab.setIcon(FontAwesome.BOOK);
    	//this.reSetTabsInPositions();
	}
	
    private SelectedTabChangeListener setUpTabsSelectionListener(){
    	return new SelectedTabChangeListener(){
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				// TODO Auto-generated method stub				
				selectedTabContentComponentId =  event.getTabSheet().getSelectedTab().getId();
				logger.info("\ntab selection listener"
						+"\n selectedTabContentComponentId : " + selectedTabContentComponentId);
				
				if(selectedTabContentComponentId!=null && selectedTabContentComponentId.equals("vRawMaterialExistenceDTOTableTabLayout"))
				{
					if(!rawMaterialExistenceDTOTableTabLayoutReFreshed)setUpRawMaterialExistenceDTOTableTabLayout();
					else rawMaterialExistenceDTOTableTabLayoutReFreshed = false;					
				}
				
				if(selectedTabContentComponentId!=null && selectedTabContentComponentId.equals("vRawMaterialPurchaseRequestDTOTableTabLayout"))
				{
					if(!rawMaterialPurchaseRequestDTOTableTabLayoutReFreshed)setUpRawMaterialPurchaseRequestDTOTableTabLayout();
					else rawMaterialPurchaseRequestDTOTableTabLayoutReFreshed = false;					
				}
			}    		
    	};
    }
    
	public void navigateToCallerView(){
		logger.info("\nRawMaterialStockManagementFormView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new RawMaterialStockManagementFormViewEvent(this.rawMaterialDTO, this.CALLER_VIEW));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	private void setUpRawMaterialExistenceDTOTableTabLayout(){
		if(this.rawMaterialExistenceDTOTableTabLayout!=null){
			this.tabs.removeTab(this.rawMaterialExistenceDTOTableTabLayout);
			this.rawMaterialExistenceDTOTableTabLayoutReFreshed = true;
		}
		RawMaterialExistenceDTOTableTabLayout vRawMaterialExistenceDTOTableTabLayout = new RawMaterialExistenceDTOTableTabLayout(this, this.rawMaterialDTO);
		vRawMaterialExistenceDTOTableTabLayout.setId("vRawMaterialExistenceDTOTableTabLayout");
		this.rawMaterialExistenceDTOTableTabLayout = 
				this.tabs.addTab(vRawMaterialExistenceDTOTableTabLayout,this.messages.get(this.VIEW_NAME + "tab.rawmaterialexistence"), 
						FontAwesome.STAR_HALF_EMPTY, 1);
		//this.reSetTabsInPositions();
		if(this.rawMaterialExistenceDTOTableTabLayoutReFreshed)this.tabs.setSelectedTab(1);
	}
	
	
	public void editRawMaterialExistenceDTO(RawMaterialExistenceDTO vRawMaterialExistenceDTO) throws Exception{
		this.setUpRawMaterialExistenceDTOFormTabLayout(vRawMaterialExistenceDTO);		
	}
	
	private void setUpRawMaterialExistenceDTOFormTabLayout(RawMaterialExistenceDTO vRawMaterialExistenceDTO) throws Exception{
		if(this.rawMaterialExistenceDTOFormTabLayoutTab!=null)this.tabs.removeTab(this.rawMaterialExistenceDTOFormTabLayoutTab);
		RawMaterialExistenceDTOFormTabLayout vRawMaterialExistenceDTOFormTabLayout = 
				new RawMaterialExistenceDTOFormTabLayout(this,vRawMaterialExistenceDTO,vRawMaterialExistenceDTO.getId()!=null);
		vRawMaterialExistenceDTOFormTabLayout.setId("vRawMaterialExistenceDTOFormTabLayout");
		this.rawMaterialExistenceDTOFormTabLayoutTab = 
				this.tabs.addTab(vRawMaterialExistenceDTOFormTabLayout, this.messages.get(this.VIEW_NAME + "tab.rawmaterialexistence.form"), 
						FontAwesome.ADJUST, 2);
		/*this.rawMaterialExistenceDTOFormTabLayoutTab.setVisible(bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
				.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "tab.rawmaterialexistence.form"));*/
		this.rawMaterialExistenceDTOFormTabLayoutTab.setVisible(false);
		//this.reSetTabsInPositions();
		if(vRawMaterialExistenceDTO.getId()!=null)this.tabs.setSelectedTab(2);
	}
	
	public void saveButtonRawMaterialExistenceDTOFormTabLayout(RawMaterialExistenceDTO vRawMaterialExistenceDTO,boolean rawMaterialExistenceDTOFormEditMode) throws PmsServiceException,Exception{
		if(!rawMaterialExistenceDTOFormEditMode){
			vRawMaterialExistenceDTO.setId_raw_material(this.rawMaterialDTO.getId());
			this.stockManagementService.insertRawMaterialExistenceDTO(vRawMaterialExistenceDTO);
		}else
			logger.info("\n***************************************"
						+"\n update the existence minimun and automatic purchase request quantity parameters, pending to implement :)"
						+"\n***************************************");
		
		this.setUpRawMaterialExistenceDTOTableTabLayout();
		this.setUpRawMaterialExistenceDTOFormTabLayout(new RawMaterialExistenceDTO());
		this.setUpRawMaterialPurchaseRequestDTOFormTabLayout(new RawMaterialPurchaseRequestDTO());
		if(this.flagEnableRawMaterialPurchaseRequestDTOFormTabLayout == false){
			this.setFlagEnableRawMaterialPurchaseRequestDTOFormTabLayout();
			this.rawMaterialPurchaseRequestDTOFormTabLayoutTab.setEnabled(this.flagEnableRawMaterialPurchaseRequestDTOFormTabLayout);
			this.tabs.markAsDirty();
		}
	}
	
    public void cancelButtonRawMaterialExistenceDTOFormTabLayout(boolean rawMaterialExistenceDTOFormEditMode) throws PmsServiceException,Exception{
    	
    	this.setUpRawMaterialExistenceDTOTableTabLayout();
    	this.setUpRawMaterialExistenceDTOFormTabLayout(new RawMaterialExistenceDTO());
    	this.tabs.setSelectedTab(1);
    	/*if(rawMaterialExistenceDTOFormEditMode)this.setUpRawMaterialExistenceDTOTableTabLayout();
    	else this.tabs.setSelectedTab(1);    	*/
    }
    
    public void editRawMaterialPurchaseRequestDTO(RawMaterialPurchaseRequestDTO vRawMaterialPurchaseRequestDTO) throws Exception{
    	this.setUpRawMaterialPurchaseRequestDTOFormTabLayout(vRawMaterialPurchaseRequestDTO);
    }
    
    private void setUpRawMaterialPurchaseRequestDTOTableTabLayout(){
    	if(this.rawMaterialPurchaseRequestDTOTableTabLayoutTab!=null){
    		this.tabs.removeTab(this.rawMaterialPurchaseRequestDTOTableTabLayoutTab);
    		this.rawMaterialPurchaseRequestDTOTableTabLayoutReFreshed = true;
    	}
    	
    	RawMaterialPurchaseRequestDTOTableTabLayout vRawMaterialPurchaseRequestDTOTableTabLayout = 
    			new RawMaterialPurchaseRequestDTOTableTabLayout(this, this.rawMaterialDTO);
    	vRawMaterialPurchaseRequestDTOTableTabLayout.setId("vRawMaterialPurchaseRequestDTOTableTabLayout");
    	this.rawMaterialPurchaseRequestDTOTableTabLayoutTab = 
    			this.tabs.addTab(vRawMaterialPurchaseRequestDTOTableTabLayout, this.messages.get(this.VIEW_NAME + "tab.rawmaterial.purchase.request"), 
    					FontAwesome.REORDER, 3);
    	//this.reSetTabsInPositions();
    	if(this.rawMaterialPurchaseRequestDTOTableTabLayoutReFreshed)this.tabs.setSelectedTab(3);
    	this.rawMaterialPurchaseRequestDTOTableTabLayoutTab.setVisible(false);
    }

    public void saveButtonRawMaterialPurchaseRequestDTOFormTabLayout(RawMaterialPurchaseRequestDTO rawMaterialPurchaseRequestDTO,
			boolean rawMaterialExistenceDTOFormEditMode) throws PmsServiceException,Exception{
    	if(!rawMaterialExistenceDTOFormEditMode){
    		rawMaterialPurchaseRequestDTO.setId_raw_material(this.rawMaterialDTO.getId());
    		this.stockManagementService.insertRawMaterialPurchaseRequestDTO(rawMaterialPurchaseRequestDTO);
    	}else
    		this.stockManagementService.updateRawMaterialPurchaseRequestDTO(rawMaterialPurchaseRequestDTO);
    	
    	this.setUpRawMaterialPurchaseRequestDTOFormTabLayout(new RawMaterialPurchaseRequestDTO());
    	this.setUpRawMaterialPurchaseRequestDTOTableTabLayout();
    }
    
    public void cancelButtonRawMaterialPurchaseRequestDTOFormTabLayout(boolean rawMaterialExistenceDTOFormEditMode) throws Exception{
    	this.setUpRawMaterialPurchaseRequestDTOFormTabLayout(new RawMaterialPurchaseRequestDTO());
    	this.tabs.setSelectedTab(3);
    }
    
    private void setUpRawMaterialPurchaseRequestDTOFormTabLayout(RawMaterialPurchaseRequestDTO rawMaterialPurchaseRequestDTO) throws Exception{
    	if(this.rawMaterialPurchaseRequestDTOFormTabLayoutTab!=null)this.tabs.removeTab(this.rawMaterialPurchaseRequestDTOFormTabLayoutTab);
    	RawMaterialPurchaseRequestDTOFormTabLayout vRawMaterialPurchaseRequestDTOFormTabLayout = 
    			new RawMaterialPurchaseRequestDTOFormTabLayout(this,this.rawMaterialDTO,rawMaterialPurchaseRequestDTO, rawMaterialPurchaseRequestDTO.getId()!=null);
    	vRawMaterialPurchaseRequestDTOFormTabLayout.setId("vRawMaterialPurchaseRequestDTOFormTabLayout");
    	this.rawMaterialPurchaseRequestDTOFormTabLayoutTab = 
    			this.tabs.addTab(vRawMaterialPurchaseRequestDTOFormTabLayout, this.messages.get(this.VIEW_NAME + "tab.rawmaterial.purchase.request.form"), 
    					FontAwesome.CREATIVE_COMMONS, 4);
    	this.rawMaterialPurchaseRequestDTOFormTabLayoutTab.setVisible(bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
				.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "tab.rawmaterial.purchase.request.form"));
    	//this.reSetTabsInPositions();
    	this.rawMaterialPurchaseRequestDTOFormTabLayoutTab.setEnabled(this.flagEnableRawMaterialPurchaseRequestDTOFormTabLayout);
    	if(rawMaterialPurchaseRequestDTO.getId()!=null)this.tabs.setSelectedTab(4);
    	this.rawMaterialPurchaseRequestDTOFormTabLayoutTab.setVisible(false);
    }
    
    
    private void setFlagEnableRawMaterialPurchaseRequestDTOFormTabLayout() throws PmsServiceException{
    	List<RawMaterialExistenceDTO> listRawMaterialExistenceDTO = this.stockManagementService.listRawMaterialExistenceDTO(new RawMaterialExistenceDTO(this.rawMaterialDTO.getId(), null));
    	if(listRawMaterialExistenceDTO!=null && !listRawMaterialExistenceDTO.isEmpty())
    		flagEnableRawMaterialPurchaseRequestDTOFormTabLayout = true;
    	else
    		flagEnableRawMaterialPurchaseRequestDTOFormTabLayout = false;
    }
    /*private void reSetTabsInPositions(){
    	if(this.rawMaterialDTOTabLayoutTab!=null
    			&& this.rawMaterialExistenceDTOTableTabLayout!=null
    			&& this.rawMaterialExistenceDTOFormTabLayoutTab!=null
    			&& this.rawMaterialPurchaseRequestDTOTableTabLayoutTab!=null
    			&& this.rawMaterialPurchaseRequestDTOFormTabLayoutTab!=null){

        	this.tabs.setTabPosition(this.rawMaterialDTOTabLayoutTab, 0);
        	this.tabs.setTabPosition(this.rawMaterialExistenceDTOTableTabLayout, 1);
        	this.tabs.setTabPosition(this.rawMaterialExistenceDTOFormTabLayoutTab, 2);
        	this.tabs.setTabPosition(this.rawMaterialPurchaseRequestDTOTableTabLayoutTab, 3);
        	this.tabs.setTabPosition(this.rawMaterialPurchaseRequestDTOFormTabLayoutTab, 4);
    	}
    }*/
}
