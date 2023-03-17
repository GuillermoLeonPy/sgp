package py.com.kyron.sgp.gui.view.application.report.management.report.excecution.management.view.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyExchangeRateDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.application.report.management.report.list.management.view.components.ReportProgramListLayout.ReportProgram;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ReportFiltersLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(ReportFiltersLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final ReportFiltersLayoutFunctions reportFiltersLayoutFunctions;
	private final ReportProgram reportProgram;
	private BussinesSessionUtils bussinesSessionUtils;
	private ComboBox listCurrencyDTOComboBox;
	private DateField beginDate_DateField;
	private DateField endDate_DateField;
	private VerticalLayout column01;
	private VerticalLayout column02;
	private VerticalLayout column03;
	private HorizontalLayout filtersLayout;
	private SgpForm<FilterValues> filterValuesForm;
	private FilterValues filterValues;
	private ComercialManagementService comercialManagementService;
	private DateField monthYearDate_DateField;
	
	public ReportFiltersLayout(
			final String VIEW_NAME,
			final ReportFiltersLayoutFunctions reportFiltersLayoutFunctions,
			final ReportProgram reportProgram,
			final boolean setUpLayoutContent) {
		// TODO Auto-generated constructor stub
		this.VIEW_NAME = VIEW_NAME;
		this.reportFiltersLayoutFunctions = reportFiltersLayoutFunctions;
		this.reportProgram = reportProgram;
		try{
			logger.info("\n ReportFiltersLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();	
	        this.setSizeFull();
	        this.initServices();
	        //this.addStyleName("v-scrollable");
	        //this.setHeight("100%");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        if(setUpLayoutContent)this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public ReportFiltersLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	
	private void initServices() throws Exception{
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		this.comercialManagementService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n ReportFiltersLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()

	private void setUpLayoutContent() throws PmsServiceException{
		this.addComponent(this.buildToolbar());
		this.buildFiltersLayout();
		this.addComponent(this.filtersLayout);
		this.addComponent(this.setUpOkCancelButtons());
	}
	
	private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);
        logger.info("\n*********************remember: an HorizontalLayout is set with the instrucction: Responsive.makeResponsive(header); !!! "
        		+"\n*********************");
        Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.report.excecution.main.title") + this.reportProgram.getReport());
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);
        
        /*HorizontalLayout tools = new HorizontalLayout(this.createRegisterProcessButton());	        	        
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);*/
        header.setMargin(true);
        return header;
	}//private Component buildToolbar()
	
	private void buildFiltersLayout() throws PmsServiceException{
		this.prepareColumnsLayout();
		this.initForm();
		this.prepareSpecificFilterControls();
		this.filtersLayout = new HorizontalLayout(this.column01,this.column02,this.column03);
		this.filtersLayout.setMargin(true);
				
	}
	
	private void prepareColumnsLayout(){
		this.column01 = new VerticalLayout();
		this.column01.setMargin(new MarginInfo(false, true, false, false));
		this.column02 = new VerticalLayout();
		this.column02.setMargin(new MarginInfo(false, true, false, false));
		this.column03 = new VerticalLayout();
		this.column03.setMargin(new MarginInfo(false, true, false, false));
	}
	
	private void initForm(){
		this.filterValues = new FilterValues();
		this.filterValuesForm = new SgpForm<FilterValues>(FilterValues.class,new BeanItem<FilterValues>(this.filterValues),10,1);
	}
	
	private void prepareSpecificFilterControls() throws PmsServiceException{
		this.setUpReportProgramFlags();
		if(this.reportProgram.isRequireBeginEndDateSelector()){
			this.beginDate_DateField = this.filterValuesForm.bindAndBuildDateField("beginDate", this.messages.get(this.VIEW_NAME + "tab.report.excecution.date.field.begin.date")/*caption*/, /*immediate*/true, 10, true/*required*/, this.messages.get(this.VIEW_NAME + "tab.report.excecution.date.field.begin.date.required.message")/*required message*/,false);
			this.endDate_DateField = this.filterValuesForm.bindAndBuildDateField("endDate", this.messages.get(this.VIEW_NAME + "tab.report.excecution.date.field.end.date")/*caption*/, /*immediate*/true, 10, true/*required*/, this.messages.get(this.VIEW_NAME + "tab.report.excecution.date.field.end.date.required.message")/*required message*/,false);
			
			final HorizontalLayout beginDate_DateFieldHorizontalLayout = new HorizontalLayout(this.beginDate_DateField);
			beginDate_DateFieldHorizontalLayout.setMargin(true);
			this.column01.addComponent(beginDate_DateFieldHorizontalLayout);
			
			final HorizontalLayout endDate_DateFieldHorizontalLayout = new HorizontalLayout(this.endDate_DateField);
			endDate_DateFieldHorizontalLayout.setMargin(true);
			this.column02.addComponent(endDate_DateFieldHorizontalLayout);			
		}//if(this.reportProgram.isRequireBeginEndDateSelector()){
		
		if(this.reportProgram.isRequireCurrencySelector()){
			this.setUpCurrencyDTOComboBox();
			final HorizontalLayout currencyDTOComboBoxHorizontalLayout = new HorizontalLayout(this.listCurrencyDTOComboBox);
			currencyDTOComboBoxHorizontalLayout.setMargin(true);
			this.column03.addComponent(currencyDTOComboBoxHorizontalLayout);
		}
		
		if(this.reportProgram.isRequireDateSelectorFormMonthYear()){
			this.monthYearDate_DateField = this.filterValuesForm.bindAndBuildDateField("monthYearDate", this.messages.get(this.VIEW_NAME + "tab.report.excecution.date.field.month.year.date")/*caption*/, /*immediate*/true, 10, true/*required*/, this.messages.get(this.VIEW_NAME + "tab.report.excecution.date.field.month.year.date.required.message")/*required message*/,false);
			this.monthYearDate_DateField.setResolution(Resolution.MONTH);
			final HorizontalLayout monthYearDate_DateFieldHorizontalLayout = new HorizontalLayout(this.monthYearDate_DateField);
			monthYearDate_DateFieldHorizontalLayout.setMargin(true);
			this.column01.addComponent(monthYearDate_DateFieldHorizontalLayout);
		}
	}
	
	private void setUpCurrencyDTOComboBox() throws PmsServiceException{
		this.listCurrencyDTOComboBox = new ComboBox(this.messages.get("application.common.currency.label"));
		//this.listCurrencyDTOComboBox.setInputPrompt(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.combo.box.currency.input.prompt"));
        BeanItemContainer<CurrencyDTO> vCurrencyDTOBeanItemContainer = new BeanItemContainer<CurrencyDTO>(CurrencyDTO.class);
        vCurrencyDTOBeanItemContainer.addAll(this.determinateCurrencyDTOList());
        this.listCurrencyDTOComboBox.setContainerDataSource(vCurrencyDTOBeanItemContainer);
        this.listCurrencyDTOComboBox.setItemCaptionPropertyId("id_code");
        
        if(this.filterValues.getId_currency()!=null){
        	this.listCurrencyDTOComboBox.select(this.filterValues.getCurrencyDTO());
        	this.listCurrencyDTOComboBox.setValue(this.filterValues.getCurrencyDTO());
        }else{
        	this.listCurrencyDTOComboBox.select(vCurrencyDTOBeanItemContainer.getIdByIndex(0));
        	this.filterValues.setId_currency(vCurrencyDTOBeanItemContainer.getIdByIndex(0).getId());
        	this.filterValues.setCurrencyDTO(vCurrencyDTOBeanItemContainer.getIdByIndex(0));
        }
        this.listCurrencyDTOComboBox.setNullSelectionAllowed(false);
        this.listCurrencyDTOComboBox.addStyleName("small");
        this.listCurrencyDTOComboBox.addValueChangeListener(this.setUpValueChangeListenerCurrencyDTOComboBox());
        this.listCurrencyDTOComboBox.setRequired(true);
        this.listCurrencyDTOComboBox.setRequiredError(this.messages.get(this.VIEW_NAME + "tab.report.excecution.combo.box.currency.required.message="));
        this.listCurrencyDTOComboBox.setWidth(35, Unit.PERCENTAGE);
               
	}
	
	private List<CurrencyDTO> determinateCurrencyDTOList() throws PmsServiceException{
		List<CurrencyExchangeRateDTO> listCurrencyExchangeRateDTO = this.comercialManagementService.listCurrencyExchangeRateDTO(new CurrencyExchangeRateDTO(true));
		List<CurrencyDTO> currencyDTOList = new ArrayList<CurrencyDTO>();
		for(CurrencyExchangeRateDTO vCurrencyExchangeRateDTO:listCurrencyExchangeRateDTO){
			currencyDTOList.add(this.comercialManagementService.listCurrencyDTO(new CurrencyDTO(vCurrencyExchangeRateDTO.getId_currency())).get(0));
		}
		currencyDTOList.add(0, this.comercialManagementService.listCurrencyDTO(new CurrencyDTO(true)).get(0));
		return currencyDTOList;
	}
	
	private Property.ValueChangeListener setUpValueChangeListenerCurrencyDTOComboBox(){
    	return new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				try{
					logger.info("\n CurrencyDTO combo box: value change event"
								+"\n======================================"	+"\nevent.getProperty().getType().getName()) : " 
								+ event.getProperty().getType().getName()
								+"\nevent.getProperty().getValue() : \n" + event.getProperty().getValue());
					filterValues.setId_currency(((CurrencyDTO)event.getProperty().getValue()).getId());
					filterValues.setCurrencyDTO((CurrencyDTO)event.getProperty().getValue());
				}catch(Exception e){
					logger.error("\n error", e);
				}
			}
    	};
	}
	
    private HorizontalLayout setUpOkCancelButtons(){
    	final Button downloadButton = new Button();
    	downloadButton.setId("downloadButtonId");
    	downloadButton.setPrimaryStyleName(".dashboard .myCustomyInvisibleButtonCssRule");
    	//downloadButton.setVisible(false);
    	this.reportFiltersLayoutFunctions.setUpDownloadButton(downloadButton);
    	final Button okButton = new Button(this.messages.get("application.common.button.print.label"));/*"Cancelar"*/
		okButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		filterValuesForm.commit();
		            		reportFiltersLayoutFunctions.queryReport(filterValues, downloadButton);		            		
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
		            		filterValuesForm.discard();
		            		reportFiltersLayoutFunctions.navigateToCallerView();
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		
		HorizontalLayout okCancelHorizontalLayout = new HorizontalLayout();
		okCancelHorizontalLayout.setMargin(true);
		okCancelHorizontalLayout.setSpacing(true);
		okCancelHorizontalLayout.setSizeFull();
		okCancelHorizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		final HorizontalLayout wrapperHorizontalLayout = new HorizontalLayout();
		wrapperHorizontalLayout.setSpacing(true);
		wrapperHorizontalLayout.addComponent(okButton);
		wrapperHorizontalLayout.addComponent(cancelButton);
		wrapperHorizontalLayout.addComponent(downloadButton);	
		okCancelHorizontalLayout.addComponent(wrapperHorizontalLayout);
		return okCancelHorizontalLayout;
    }

    private void setUpReportProgramFlags(){
    	this.reportFiltersLayoutFunctions.setUpReportProgramFlags(this.reportProgram);
    }
    
	public class FilterValues{
		private Date beginDate;
		private Date endDate;
		private Long id_currency;
		private CurrencyDTO currencyDTO;
		private Date monthYearDate;
		
		/**
		 * @return the beginDate
		 */
		public Date getBeginDate() {
			return beginDate;
		}
		/**
		 * @param beginDate the beginDate to set
		 */
		public void setBeginDate(Date beginDate) {
			this.beginDate = beginDate;
		}
		/**
		 * @return the endDate
		 */
		public Date getEndDate() {
			return endDate;
		}
		/**
		 * @param endDate the endDate to set
		 */
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
		/**
		 * @return the id_currency
		 */
		public Long getId_currency() {
			return id_currency;
		}
		/**
		 * @param id_currency the id_currency to set
		 */
		public void setId_currency(Long id_currency) {
			this.id_currency = id_currency;
		}
		/**
		 * @return the currencyDTO
		 */
		public CurrencyDTO getCurrencyDTO() {
			return currencyDTO;
		}
		/**
		 * @param currencyDTO the currencyDTO to set
		 */
		public void setCurrencyDTO(CurrencyDTO currencyDTO) {
			this.currencyDTO = currencyDTO;
		}
		/**
		 * @return the monthYearDate
		 */
		public Date getMonthYearDate() {
			return monthYearDate;
		}
		/**
		 * @param monthYearDate the monthYearDate to set
		 */
		public void setMonthYearDate(Date monthYearDate) {
			this.monthYearDate = monthYearDate;
		}
		
	}
}
