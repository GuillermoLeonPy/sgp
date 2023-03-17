package py.com.kyron.sgp.gui.view.stockmanagement;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SaleInvoiceProductDeliverablesDTO;
import py.com.kyron.sgp.bussines.stockmanagement.service.StockManagementService;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.SaleInvoiceProductDeliverablesFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.OnDemandFileDownloader;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.utils.OnDemandFileDownloader.OnDemandStreamResource;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.stockmanagement.sale.invoice.product.deliverables.form.view.components.SaleInvoiceProductDeliverablesDTOTabLayout;
import py.com.kyron.sgp.gui.view.stockmanagement.sale.invoice.product.deliverables.form.view.components.SaleInvoiceProductDeliverablesDTOTabLayoutFunctions;

import com.google.common.eventbus.Subscribe;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedHttpSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;

@SuppressWarnings("serial")
public class SaleInvoiceProductDeliverablesFormView extends VerticalLayout
		implements View, SaleInvoiceProductDeliverablesDTOTabLayoutFunctions{

	
	private final Logger logger = LoggerFactory.getLogger(SaleInvoiceProductDeliverablesFormView.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME = "sale.invoice.product.deliverables.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private boolean editFormMode;
	private boolean queryFormMode;
	private TabSheet tabs;
	private Tab saleInvoiceProductDeliverablesDTOTabLayoutTab;
	private Tab saleInvoiceProductDeliverablesDTOTabLayoutTabTemporary;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private VerticalLayout mainViewLayout;
	private String CALLER_VIEW;
	private SgpUtils sgpUtils;
	private BussinesSessionUtils bussinesSessionUtils;
	private SaleInvoiceProductDeliverablesDTO saleInvoiceProductDeliverablesDTO;
	private StockManagementService stockManagementService;
	private String selectedTabContentComponentId;
	private byte[] downloadData;
	private String downloadFileName;
	
	public SaleInvoiceProductDeliverablesFormView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n SaleInvoiceProductDeliverablesFormView..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.sgpUtils = new SgpUtils();
			this.initServices();			
	        this.addStyleName("v-scrollable");
	        this.setHeight("100%");	        
	        Responsive.makeResponsive(this);
			DashboardEventBus.register(this);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public SaleInvoiceProductDeliverablesFormView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	
	private void initServices() throws Exception{
		this.stockManagementService = (StockManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.STOCK_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}

	@Subscribe
	public void handleCallingEvent(final SaleInvoiceProductDeliverablesFormViewEvent vSaleInvoiceProductDeliverablesFormViewEvent){
		try{
			this.CALLER_VIEW = vSaleInvoiceProductDeliverablesFormViewEvent.getCallerView();
			this.editFormMode = vSaleInvoiceProductDeliverablesFormViewEvent.isEditFormMode();
			this.queryFormMode = vSaleInvoiceProductDeliverablesFormViewEvent.isQueryFormMode();
			this.saleInvoiceProductDeliverablesDTO = vSaleInvoiceProductDeliverablesFormViewEvent.getSaleInvoiceProductDeliverablesDTO();			
			this.initMainViewLayout();
			this.initTitles();
			this.initTabSheet();
			this.setUpSaleInvoiceProductDeliverablesDTOTabLayout();
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
		int compomentCount = this.tabs.getComponentCount();
		for(int counter = (compomentCount - 1); counter >= 0 ; counter--){
			this.tabs.setSelectedTab(counter);
			logger.info("\n tab in position: "+ counter + " has been selected...");
		}

    	this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
		this.tabs.markAsDirty();
	}
	
    private SelectedTabChangeListener setUpTabsSelectionListener(){
    	return new SelectedTabChangeListener(){
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				// TODO Auto-generated method stub
				try{
					selectedTabContentComponentId =  event.getTabSheet().getSelectedTab().getId();
					logger.info("\ntab selection listener"
							+"\n selectedTabContentComponentId : " + selectedTabContentComponentId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("\nerror", e);
				}
			}    		
    	};
    }
	
	private void initMainViewLayout(){
		this.mainViewLayout = new VerticalLayout();
		this.mainViewLayout.setSpacing(true);
		this.mainViewLayout.setMargin(true);
	}
	
	private void removeAndReAddMainViewLayout(){
		this.removeAllComponents();
		this.addComponent(this.mainViewLayout);
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
    	this.tabs.addStyleName("framed");
	}

	@Override
	public void effectuateProductDeliverSaleInvoiceProductDeliverablesDTO(
			SaleInvoiceProductDeliverablesDTO saleInvoiceProductDeliverablesDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		this.saleInvoiceProductDeliverablesDTO = this.stockManagementService.effectuateProductDeliverSaleInvoiceProductDeliverablesDTO(saleInvoiceProductDeliverablesDTO);
		this.reStartLayout();
		this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
	}

	@Override
	public void navigateToCallerView() {
		// TODO Auto-generated method stub
		logger.info("\n SaleInvoiceProductDeliverablesFormViewEvent.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new SaleInvoiceProductDeliverablesFormViewEvent(
					this.saleInvoiceProductDeliverablesDTO, 
					DashboardViewType.SALE_INVOICE_PRODUCT_DELIVERABLES_FORM.getViewName(),
					this.editFormMode,
					this.queryFormMode));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	private void setUpSaleInvoiceProductDeliverablesDTOTabLayout(){
		SaleInvoiceProductDeliverablesDTOTabLayout vSaleInvoiceProductDeliverablesDTOTabLayout = 
				new SaleInvoiceProductDeliverablesDTOTabLayout
				(this,this.VIEW_NAME,this.saleInvoiceProductDeliverablesDTO,this.editFormMode,this.queryFormMode,true);
		vSaleInvoiceProductDeliverablesDTOTabLayout.setId("vSaleInvoiceProductDeliverablesDTOTabLayout");
		this.saleInvoiceProductDeliverablesDTOTabLayoutTab = 
				this.tabs.addTab(vSaleInvoiceProductDeliverablesDTOTabLayout, 
				this.messages.get(this.VIEW_NAME + "tab.sale.invoice.product.deliverables"),
				FontAwesome.SHOPPING_BAG, 
				0);
		
		this.saleInvoiceProductDeliverablesDTOTabLayoutTab.setVisible(true);
		/* temporal while one tab in the layout*/ 
		this.saleInvoiceProductDeliverablesDTOTabLayoutTabTemporary = 
				this.tabs.addTab(new SaleInvoiceProductDeliverablesDTOTabLayout
						(this,this.VIEW_NAME,this.saleInvoiceProductDeliverablesDTO,this.editFormMode,this.queryFormMode,false), 
				this.messages.get(this.VIEW_NAME + "tab.sale.invoice.product.deliverables"),
				FontAwesome.OPENCART, 
				1);
		this.saleInvoiceProductDeliverablesDTOTabLayoutTabTemporary.setVisible(false);
		
	}
	
	private void reStartLayout(){
		this.initMainViewLayout();
		this.initTitles();
		this.initTabSheet();
		this.setUpSaleInvoiceProductDeliverablesDTOTabLayout();
	    this.mainViewLayout.addComponent(this.tabs);
	    this.removeAndReAddMainViewLayout();
	    this.refreshTabSelection();
	}

	@Override
	public void setUpDownloadButton(Button downloadButton) {
		// TODO Auto-generated method stub
		downloadButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	logger.info( "\n ======================================== "
		            				+"\n downloadButton clicked !"
		            				+"\n ======================================== ");
		            }
		        }
			);
		OnDemandStreamResource myResource = createOnDemandResource();
		OnDemandFileDownloader fileDownloader = new OnDemandFileDownloader(myResource);
		fileDownloader.extend(downloadButton);
	}
	
	private OnDemandStreamResource createOnDemandResource() {
		return new OnDemandStreamResource() {
			@Override
			public InputStream getStream() {
				// TODO Auto-generated method stub
				logger.info( "\n ======================================== "
							+"\n OnDemandStreamResource.getStream() method executed"
							+"\n ======================================== ");
				return new ByteArrayInputStream(downloadData);
			}
			@Override
			public String getFilename() {
				// TODO Auto-generated method stub
				return downloadFileName;
			}			
		};
	}

	@Override
	public void print(
			SaleInvoiceProductDeliverablesDTO saleInvoiceProductDeliverablesDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		final WrappedHttpSession vWrappedHttpSession = (WrappedHttpSession)VaadinSession.getCurrent().getSession();
		final HttpSession vHttpSession = vWrappedHttpSession.getHttpSession();
		this.downloadData = this.stockManagementService.orderAndInvoiceStatusReport(saleInvoiceProductDeliverablesDTO, vHttpSession);
		this.downloadFileName = SgpUtils.formatDate_yyyyMMddHHmmss_ByLocale(
				new Date(), 
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()) + ".pdf";		
		Page.getCurrent().getJavaScript().execute("document.getElementById('downloadButtonId" + "vSaleInvoiceOrderStatusReport" + "').click();");
		
	}
}
