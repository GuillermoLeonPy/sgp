package py.com.kyron.sgp.gui.view.application.report.management.report.list.management.view.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ReportProgramListLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(ReportProgramListLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Table reportProgramListTable;
	private List<ReportProgram> listReportProgram;
	private static final String[] DEFAULT_COLLAPSIBLE = {"module","operations"};
	final ReportProgramListLayoutFunctions reportProgramListLayoutFunctions;
	private BussinesSessionUtils bussinesSessionUtils;
	private Set<String> programKeysBySession;
	private Map<String,String> applicationModules;
	
	public ReportProgramListLayout(
			final String VIEW_NAME,
			final ReportProgramListLayoutFunctions reportProgramListLayoutFunctions) {
		// TODO Auto-generated constructor stub
		this.VIEW_NAME = VIEW_NAME;
		this.reportProgramListLayoutFunctions = reportProgramListLayoutFunctions;
		try{
			logger.info("\n ReportProgramListLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.messages.putAll(ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.SECURED_PROGRAM_PREFIX));			
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public ReportProgramListLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void initServices(){
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	private void setUpLayoutContent() throws Exception{
		this.buildReportProgramTable();
		addComponent(this.buildToolbar());
        addComponent(reportProgramListTable);
        setExpandRatio(reportProgramListTable, 1);
	}
	
	private void buildReportProgramTable() throws Exception{
		if(this.reportProgramListTable!=null)this.removeComponent(this.reportProgramListTable);
		this.retrieveListReportProgram();
		if(listReportProgram==null)this.listReportProgram = new ArrayList<ReportProgram>();
    	this.reportProgramListTable = new Table();
    	BeanItemContainer<ReportProgram> ReportProgramBeanItemContainer	= new BeanItemContainer<ReportProgram>(ReportProgram.class);
    	ReportProgramBeanItemContainer.addAll(this.listReportProgram);    	
    	this.reportProgramListTable.setContainerDataSource(ReportProgramBeanItemContainer); 
    	
    	this.reportProgramListTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ReportProgram vReportProgram = (ReportProgram)itemId;
				return buildOperationsButtonPanel(vReportProgram);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//reportProgramListTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {

    	this.reportProgramListTable.setVisibleColumns(new Object[] 
    			{"operations","report","module"});
    	this.reportProgramListTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.reportProgramListTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.reportProgramListTable.setColumnHeader("module", this.messages.get(this.VIEW_NAME + "tab.report.program.list.table.program.list.column.module"));
    	this.reportProgramListTable.setColumnAlignment("module", Table.Align.LEFT);
    	
    	this.reportProgramListTable.setColumnHeader("report", this.messages.get(this.VIEW_NAME + "tab.report.program.list.table.program.list.column.report"));
    	this.reportProgramListTable.setColumnAlignment("report", Table.Align.LEFT);
    	

    	this.reportProgramListTable.setSizeFull();
    	this.reportProgramListTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.reportProgramListTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.reportProgramListTable.addStyleName(ValoTheme.TABLE_SMALL);    	 
    	this.reportProgramListTable.setColumnExpandRatio("module", 0.0052f);    	
    	this.reportProgramListTable.setColumnExpandRatio("report", 0.019f);
    	this.reportProgramListTable.setColumnExpandRatio("operations", 0.0059f);
    	this.reportProgramListTable.setSelectable(true);
    	this.reportProgramListTable.setColumnCollapsingAllowed(true);
    	this.reportProgramListTable.setColumnCollapsible("report", false);
    	this.reportProgramListTable.setColumnCollapsible("module", true);
    	this.reportProgramListTable.setColumnCollapsible("operations", true);    	
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	this.reportProgramListTable.setSortContainerPropertyId("module");
    	this.reportProgramListTable.setSortAscending(true);
    	this.reportProgramListTable.setSortEnabled(true);
    	this.reportProgramListTable.setColumnReorderingAllowed(true);
    	this.reportProgramListTable.setFooterVisible(true);
    	this.reportProgramListTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
	private void retrieveListReportProgram() throws Exception{
		this.prepareProgramKeysBySession();
		this.setUpApplicationModuleList();
		this.listReportProgram = new ArrayList<ReportProgramListLayout.ReportProgram>();
		String moduleBusinessArea = null;
		for(String progamKey : this.programKeysBySession){
			if(progamKey.startsWith(this.SECURED_PROGRAM_PREFIX + "report.list.element")){
				moduleBusinessArea = progamKey.substring(
						(this.SECURED_PROGRAM_PREFIX + "report.list.element.").length(),
						progamKey.indexOf("management.") + "management.".length());
				/*Example: 
				 * programKey = secured.access.program.report.list.element.cash.movements.management.income.expenditure.report
				 * moduleBusinessArea = cash.movements.management.
				 * */
				this.listReportProgram.add(
						new ReportProgram(
								Math.round(Math.random() * 10000), 
								"main.menu.module." + moduleBusinessArea,
								this.applicationModules.get("main.menu.module." + moduleBusinessArea),
								progamKey,
								this.messages.get(progamKey))
						);
			}//if(progamKey.startsWith(this.SECURED_PROGRAM_PREFIX + "report.list.element")){
		}//for(String progamKey : this.programKeysBySession){				
	}//private void retrieveListReportProgram() throws Exception{
	
    private void prepareProgramKeysBySession() throws Exception{
		this.programKeysBySession = this.bussinesSessionUtils.getRawSessionData().getProgramKeysBySession();    	
    }
	
	private HorizontalLayout buildOperationsButtonPanel(final ReportProgram vReportProgram){	
		final Button queryButton = new Button();
		queryButton.setIcon(FontAwesome.SEARCH);
		queryButton.setDescription(this.messages.get("application.common.query.label"));
		queryButton.addStyleName("borderless");
		queryButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar ReportProgram...\n" + vReportProgram.toString());
                try{		                	
                	reportProgramListLayoutFunctions.queryReportProgram(vReportProgram);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		queryButton.setVisible(true);
		queryButton.setEnabled(true);		
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(queryButton);
		return operationsButtonPanel;
	}
	
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    // Some columns are collapsed when browser window width gets small
	    // enough to make the table fit better.
	    if (this.reportProgramListTable!=null && defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.reportProgramListTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.reportProgramListTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
	
	private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);
        logger.info("\n*********************remember: an HorizontalLayout is set with the instrucction: Responsive.makeResponsive(header); !!! "
        		+"\n*********************");
        Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.report.program.list.main.title"));
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);
        
        /*HorizontalLayout tools = new HorizontalLayout(this.createRegisterProcessButton());	        	        
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);*/
        return header;
	}//private Component buildToolbar()

	private void setUpApplicationModuleList() throws Exception{
		this.applicationModules = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale("main.menu.module.");		
		final Iterator<String> keySetIterator = this.applicationModules.keySet().iterator();
		String key = null;
		while(keySetIterator.hasNext()){
			key = keySetIterator.next();
			if(!(key.charAt(key.length() - 1/*the last character*/) == '.'/*all module keys end with period*/)){
				//if not an application module; then remove
				keySetIterator.remove();
			}//end if
		}//while(keySetIterator.hasNext()){
	}//private void setUpApplicationModuleList() throws Exception{
	
	public class ReportProgram{
		
		private Long id;
		private String moduleKey;
		private String module;
		private String reportKey;
		private String report;
		private boolean requireCurrencySelector;
		private boolean requireBeginEndDateSelector;
		private boolean requireDateSelectorFormMonthYear;
		
		
		/**
		 * @param id
		 * @param moduleKey
		 * @param module
		 * @param reportKey
		 * @param report
		 * 
		 */
		public ReportProgram(Long id, String moduleKey, String module, String reportKey, String report) {
			super();
			this.id = id;
			this.moduleKey = moduleKey;
			this.module = module;
			this.reportKey = reportKey;
			this.report = report;
			
		}
		
		/**
		 * @return the id
		 */
		public Long getId() {
			return id;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(Long id) {
			this.id = id;
		}
		/**
		 * @return the module
		 */
		public String getModule() {
			return module;
		}
		/**
		 * @param module the module to set
		 */
		public void setModule(String module) {
			this.module = module;
		}
		/**
		 * @return the report
		 */
		public String getReport() {
			return report;
		}
		/**
		 * @param report the report to set
		 */
		public void setReport(String report) {
			this.report = report;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "ReportProgram [id=" + id + ", moduleKey=" + moduleKey
					+ ", module=" + module + ", reportKey=" + reportKey
					+ ", report=" + report + ", requireCurrencySelector="
					+ requireCurrencySelector
					+ ", requireBeginEndDateSelector="
					+ requireBeginEndDateSelector + ", getClass()="
					+ getClass() + ", toString()=" + super.toString() + "]";
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result
					+ ((this.getId() == null) ? 0 : this.getId().hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			ReportProgram other = (ReportProgram) obj;
			if (this.getId() == null) {
				if (other.getId() != null)
					return false;
			} else if (!this.getId().equals(other.getId()))
				return false;
			return true;
		}

		/**
		 * @return the reportKey
		 */
		public String getReportKey() {
			return reportKey;
		}

		/**
		 * @param reportKey the reportKey to set
		 */
		public void setReportKey(String reportKey) {
			this.reportKey = reportKey;
		}

		/**
		 * @return the moduleKey
		 */
		public String getModuleKey() {
			return moduleKey;
		}

		/**
		 * @param moduleKey the moduleKey to set
		 */
		public void setModuleKey(String moduleKey) {
			this.moduleKey = moduleKey;
		}

		/**
		 * @return the requireCurrencySelector
		 */
		public boolean isRequireCurrencySelector() {
			return requireCurrencySelector;
		}

		/**
		 * @param requireCurrencySelector the requireCurrencySelector to set
		 */
		public void setRequireCurrencySelector(boolean requireCurrencySelector) {
			this.requireCurrencySelector = requireCurrencySelector;
		}

		/**
		 * @return the requireBeginEndDateSelector
		 */
		public boolean isRequireBeginEndDateSelector() {
			return requireBeginEndDateSelector;
		}

		/**
		 * @param requireBeginEndDateSelector the requireBeginEndDateSelector to set
		 */
		public void setRequireBeginEndDateSelector(boolean requireBeginEndDateSelector) {
			this.requireBeginEndDateSelector = requireBeginEndDateSelector;
		}

		/**
		 * @return the requireDateSelectorFormMonthYear
		 */
		public boolean isRequireDateSelectorFormMonthYear() {
			return requireDateSelectorFormMonthYear;
		}

		/**
		 * @param requireDateSelectorFormMonthYear the requireDateSelectorFormMonthYear to set
		 */
		public void setRequireDateSelectorFormMonthYear(
				boolean requireDateSelectorFormMonthYear) {
			this.requireDateSelectorFormMonthYear = requireDateSelectorFormMonthYear;
		}
	}
}
