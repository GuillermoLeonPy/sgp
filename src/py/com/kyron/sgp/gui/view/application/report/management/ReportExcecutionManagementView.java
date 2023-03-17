package py.com.kyron.sgp.gui.view.application.report.management;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import javax.print.PrintException;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.IncomeExpeditureReportDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.ManPowerExpenditurePerFunctionaryDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.ProductCostSaleComparisonDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.service.CashMovementsManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.bussines.stockmanagement.service.StockManagementService;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.ReportExcecutionManagementViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.OnDemandFileDownloader;
import py.com.kyron.sgp.gui.utils.OnDemandFileDownloader.OnDemandStreamResource;
import py.com.kyron.sgp.gui.utils.SgpPrintService;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.application.report.management.report.excecution.management.view.components.ReportFiltersLayout;
import py.com.kyron.sgp.gui.view.application.report.management.report.excecution.management.view.components.ReportFiltersLayout.FilterValues;
import py.com.kyron.sgp.gui.view.application.report.management.report.excecution.management.view.components.ReportFiltersLayoutFunctions;
import py.com.kyron.sgp.gui.view.application.report.management.report.list.management.view.components.ReportProgramListLayout.ReportProgram;

import com.google.common.eventbus.Subscribe;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedHttpSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class ReportExcecutionManagementView extends VerticalLayout implements
		View,ReportFiltersLayoutFunctions {
	private final Logger logger = LoggerFactory.getLogger(ReportExcecutionManagementView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "report.excecution.management.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private BussinesSessionUtils bussinesSessionUtils;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private TabSheet tabs;
	private Tab reportFiltersLayoutTab;
	private Tab temporaryReportFiltersLayoutTab;
	private VerticalLayout mainViewLayout;
	private String selectedTabContentComponentId;
	private String CALLER_VIEW;
	private ReportProgram reportProgram;
	private CashMovementsManagementService cashMovementsManagementService;
	private SgpPrintService sgpPrintService;
	private byte[] downloadData;
	private String downloadFileName;
	private StockManagementService stockManagementService;
			
	public ReportExcecutionManagementView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n ReportExcecutionManagementView()...");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.sgpPrintService = new SgpPrintService();
			this.initServices();			
	        this.addStyleName("v-scrollable");
	        this.setHeight("100%");
	        Responsive.makeResponsive(this);
			DashboardEventBus.register(this);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	public ReportExcecutionManagementView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	private void initServices() throws Exception{
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		this.cashMovementsManagementService = (CashMovementsManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.CASH_MOVEMENTS_MANAGEMENT_SERVICE);
		this.stockManagementService = (StockManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.STOCK_MANAGEMENT_SERVICE);
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	@Subscribe
	public void handleCallingEvent(final ReportExcecutionManagementViewEvent vReportExcecutionManagementViewEvent){
		this.CALLER_VIEW = vReportExcecutionManagementViewEvent.getCallerView();
		this.reportProgram = vReportExcecutionManagementViewEvent.getReportProgram();
		this.initMainViewLayout();
		this.initTitles();
		this.initTabSheet();
		this.setUpReportFiltersLayoutTab();
		this.setUpTemporaryReportFiltersLayoutTab();
	    this.mainViewLayout.addComponent(this.tabs);
	    this.removeAndReAddMainViewLayout();
	    this.refreshTabSelection();
	}
	
	private void initMainViewLayout(){
		this.mainViewLayout = new VerticalLayout();
		this.mainViewLayout.setSpacing(true);
		this.mainViewLayout.setMargin(true);
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
    	//this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
    	this.tabs.addStyleName("framed");	
	}
	
	private void setUpReportFiltersLayoutTab(){
		ReportFiltersLayout vReportFiltersLayout = new ReportFiltersLayout(this.VIEW_NAME,this,this.reportProgram,true);
		vReportFiltersLayout.setId("vReportFiltersLayout");
		if(this.reportFiltersLayoutTab!=null)this.tabs.removeTab(this.reportFiltersLayoutTab);
		this.reportFiltersLayoutTab = this.tabs.addTab(
				vReportFiltersLayout, 
				this.messages.get(this.VIEW_NAME + "tab.report.excecution"),
				FontAwesome.SEARCH, 
				0);
	}

	private void setUpTemporaryReportFiltersLayoutTab(){
		ReportFiltersLayout vReportFiltersLayout = new ReportFiltersLayout(this.VIEW_NAME,this,this.reportProgram,false);
		vReportFiltersLayout.setId("vTemporaryReportFiltersLayout");		
		this.temporaryReportFiltersLayoutTab = this.tabs.addTab(
				vReportFiltersLayout, 
				this.messages.get(this.VIEW_NAME + "tab.report.excecution"),
				FontAwesome.SEARCH, 
				1);
		this.temporaryReportFiltersLayoutTab.setVisible(false);
	}
	
	@Override
	public void queryReport(FilterValues filterValues, Button downloadButton) throws PmsServiceException, PrintException, IOException {
		// TODO Auto-generated method stub
		if(this.reportProgram.getReportKey().equals("secured.access.program.report.list.element.cash.movements.management.income.expenditure.report"))
			this.executeCashMovementsManagementIncomeExpenditureReport(filterValues);
		else if(this.reportProgram.getReportKey().equals("secured.access.program.report.list.element.cash.movements.management.purchases.form.obligation.tax.report"))
			this.executeCashMovementsManagementPurchasesFormObligationTaxReport(filterValues, downloadButton);
		else if(this.reportProgram.getReportKey().equals("secured.access.program.report.list.element.cash.movements.management.sales.form.obligation.tax.report"))
			this.executeCashMovementsManagementSalesFormObligationTaxReport(filterValues);
		else if(this.reportProgram.getReportKey().equals("secured.access.program.report.list.element.cash.movements.management.product.cost.sale.comparison.report"))
			this.executeProductCostSaleComparisonReport(filterValues);
		else if(this.reportProgram.getReportKey().equals("secured.access.program.report.list.element.stock.management.current.raw.material.insufficiency.report"))
			this.executeCurrentInsufficiencyRawMaterialReport(filterValues);
		else if(this.reportProgram.getReportKey().equals("secured.access.program.report.list.element.cash.movements.management.man.power.expenditure.per.functionary.report"))
			this.executeManPowerExpenditurePerFunctionaryReport(filterValues);
	}

	@Override
	public void navigateToCallerView() {
		// TODO Auto-generated method stub
		logger.info("\n ReportExcecutionManagementView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new ReportExcecutionManagementViewEvent(
					this.reportProgram,
					DashboardViewType.REPORT_EXCECUTION_MANAGEMENT.getViewName()));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	/*private void executeCashMovementsManagementIncomeExpenditureReport(final FilterValues filterValues) throws PmsServiceException, PrintException, IOException{		
		final WrappedHttpSession vWrappedHttpSession = (WrappedHttpSession)VaadinSession.getCurrent().getSession();
		final HttpSession vHttpSession = vWrappedHttpSession.getHttpSession();
		byte[] vIncomeExpeditureReport = this.cashMovementsManagementService.getIncomeExpeditureReport
				(new IncomeExpeditureReportDTO(filterValues.getBeginDate(),filterValues.getEndDate(),filterValues.getId_currency(),filterValues.getCurrencyDTO()), 
						vHttpSession);
		this.sgpPrintService.printPdfFileByByteArray(
				vIncomeExpeditureReport, 
				this.messages.get("application.common.print.service.pdf.creator.printer.windows"));
		this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
	}*/

	private void executeCashMovementsManagementIncomeExpenditureReport(final FilterValues filterValues) throws PmsServiceException, PrintException, IOException{		
		final WrappedHttpSession vWrappedHttpSession = (WrappedHttpSession)VaadinSession.getCurrent().getSession();
		final HttpSession vHttpSession = vWrappedHttpSession.getHttpSession();
		this.downloadData = this.cashMovementsManagementService.getIncomeExpeditureReport
				(new IncomeExpeditureReportDTO(filterValues.getBeginDate(),filterValues.getEndDate(),filterValues.getId_currency(),filterValues.getCurrencyDTO()), 
						vHttpSession);
		this.downloadFileName = SgpUtils.formatDate_yyyyMMddHHmmss_ByLocale(
				new Date(), 
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()) + ".pdf";
		Page.getCurrent().getJavaScript().execute("document.getElementById('downloadButtonId').click();");
	}
	
	@Override
	public void setUpReportProgramFlags(ReportProgram vReportProgram) {
		// TODO Auto-generated method stub
		if(vReportProgram.getReportKey().equals("secured.access.program.report.list.element.cash.movements.management.income.expenditure.report")
				||
		vReportProgram.getReportKey().equals("secured.access.program.report.list.element.cash.movements.management.product.cost.sale.comparison.report")
		||
		vReportProgram.getReportKey().equals("secured.access.program.report.list.element.cash.movements.management.man.power.expenditure.per.functionary.report"))
		{
			vReportProgram.setRequireBeginEndDateSelector(true);
			vReportProgram.setRequireCurrencySelector(true);
		}else if(vReportProgram.getReportKey().equals("secured.access.program.report.list.element.cash.movements.management.purchases.form.obligation.tax.report")
		|| vReportProgram.getReportKey().equals("secured.access.program.report.list.element.cash.movements.management.sales.form.obligation.tax.report")){
			vReportProgram.setRequireDateSelectorFormMonthYear(true);
		}else if(vReportProgram.getReportKey().equals("secured.access.program.report.list.element.stock.management.current.raw.material.insufficiency.report")){
			vReportProgram.setRequireCurrencySelector(true);
		}
	}
	
	private void executeCashMovementsManagementPurchasesFormObligationTaxReport(final FilterValues filerValues, final Button downloadButton) throws IOException, PmsServiceException{
		/*this.setUpDownloadDialogWindow(
				this.cashMovementsManagementService.getPurchasesFormObligationTaxReport(filerValues.getMonthYearDate()), 
				"", 
				".txt", downloadButton);*/
		
		this.downloadData = this.cashMovementsManagementService.getPurchasesFormObligationTaxReport(filerValues.getMonthYearDate());
		this.downloadFileName = SgpUtils.formatDate_yyyyMMddHHmmss_ByLocale(
				new Date(), 
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()) + ".txt";
		//downloadButton.click();
		//Simulate the click on downloadInvisibleButton by JavaScript
	    Page.getCurrent().getJavaScript().execute("document.getElementById('downloadButtonId').click();");
	}
	
	private void executeCashMovementsManagementSalesFormObligationTaxReport(final FilterValues filerValues) throws PmsServiceException{
		this.downloadData = this.cashMovementsManagementService.getSalesFormObligationTaxReport(filerValues.getMonthYearDate());
		this.downloadFileName = SgpUtils.formatDate_yyyyMMddHHmmss_ByLocale(
				new Date(), 
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()) + ".txt";
		//downloadButton.click();
		//Simulate the click on downloadInvisibleButton by JavaScript
	    Page.getCurrent().getJavaScript().execute("document.getElementById('downloadButtonId').click();");		
	}
	
//	private void setUpDownloadDialogWindow(byte[] data, String fileName, String fileExtension, final Button downloadButton) throws IOException{        
//        final StreamResource vStreamResource = this.createResource(
//        		data, 
//        		fileName 
//        		+ SgpUtils.formatDate_yyyyMMddHHmmss_ByLocale(
//        				new Date(), 
//        				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale())
//        		+ fileExtension);
//		vStreamResource.setMIMEType("text/plain");
//		vStreamResource.getStream().setParameter("Content-Disposition", "attachment; filename=" + vStreamResource.getFilename());
//        final FileDownloader downloader = new FileDownloader(vStreamResource);
//        logger.info( "\n ================================================== "
//    			+"\n data.length : "+ data.length
//    			+"\n ================================================== ");
//        downloader.extend(downloadButton);
//        BrowserWindowOpener opener = new BrowserWindowOpener(vStreamResource);
//        opener.extend(downloadButton);
//        downloadButton.click();
//        //UI.getCurrent().getPage().open(vStreamResource, "test", false);
//	}
//	
//    private StreamResource createResource(final byte[] data, final String fileName) {
//        return new StreamResource(new StreamSource() {
//            @Override
//            public InputStream getStream() {
//                    return new ByteArrayInputStream(data);
//            }
//        }, fileName);
//    }

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
	/*private void saveOnDisk(byte[] data) throws IOException{
	final OutputStream out = 
	new FileOutputStream(
			"/home/testuser/docs/" 
	+ SgpUtils.formatDate_yyyyMMddHHmmss_ByLocale(
			new Date(), 
			this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale())
	+ ".txt"
	);
	out.write(data, 0, data.length);
	}*/
	
	private void executeProductCostSaleComparisonReport(final FilterValues filterValues) throws PmsServiceException, PrintException, IOException{		
		final WrappedHttpSession vWrappedHttpSession = (WrappedHttpSession)VaadinSession.getCurrent().getSession();
		final HttpSession vHttpSession = vWrappedHttpSession.getHttpSession();
		this.downloadData = this.cashMovementsManagementService.getProductCostSaleComparisonReport
				(new ProductCostSaleComparisonDTO(filterValues.getId_currency(),filterValues.getBeginDate(),filterValues.getEndDate(),filterValues.getCurrencyDTO()), 
						vHttpSession);
		this.downloadFileName = SgpUtils.formatDate_yyyyMMddHHmmss_ByLocale(
				new Date(), 
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()) + ".pdf";
		Page.getCurrent().getJavaScript().execute("document.getElementById('downloadButtonId').click();");
	}
	
	private void executeCurrentInsufficiencyRawMaterialReport(final FilterValues filterValues) throws PmsServiceException, PrintException, IOException{
		final WrappedHttpSession vWrappedHttpSession = (WrappedHttpSession)VaadinSession.getCurrent().getSession();
		final HttpSession vHttpSession = vWrappedHttpSession.getHttpSession();
		this.downloadData = this.stockManagementService.currentInsufficiencyRawMaterialReport(filterValues.getCurrencyDTO().getId(), vHttpSession);
		if(this.downloadData == null)throw new PmsServiceException("py.com.kyron.sgp.persistence.common.search.no.results.found.error", null, null);
		this.downloadFileName = SgpUtils.formatDate_yyyyMMddHHmmss_ByLocale(
				new Date(), 
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()) + ".pdf";
		Page.getCurrent().getJavaScript().execute("document.getElementById('downloadButtonId').click();");		
	}
	
	private void executeManPowerExpenditurePerFunctionaryReport(final FilterValues filterValues) throws PmsServiceException, PrintException, IOException{
		final WrappedHttpSession vWrappedHttpSession = (WrappedHttpSession)VaadinSession.getCurrent().getSession();
		final HttpSession vHttpSession = vWrappedHttpSession.getHttpSession();
		this.downloadData = this.cashMovementsManagementService.manPowerExpenditurePerFunctionaryReport
				(new ManPowerExpenditurePerFunctionaryDTO(
						filterValues.getBeginDate(), 
						filterValues.getEndDate(), 
						filterValues.getCurrencyDTO().getId(), 
						filterValues.getCurrencyDTO()), 
						vHttpSession);
		this.downloadFileName = SgpUtils.formatDate_yyyyMMddHHmmss_ByLocale(
				new Date(), 
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()) + ".pdf";
		Page.getCurrent().getJavaScript().execute("document.getElementById('downloadButtonId').click();");
	}
}
