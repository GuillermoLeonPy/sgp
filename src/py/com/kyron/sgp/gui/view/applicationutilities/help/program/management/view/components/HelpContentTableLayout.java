package py.com.kyron.sgp.gui.view.applicationutilities.help.program.management.view.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class HelpContentTableLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(HelpContentTableLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Table helpContentListTable;
	private static final String[] DEFAULT_COLLAPSIBLE = {"operations","moduleKeyValue"};
	private final HelpContentTableLayoutFunctions helpContentTableLayoutFunctions;
	private BussinesSessionUtils bussinesSessionUtils;
	private List<HelpContent> listHelpContent;
	
	public HelpContentTableLayout(
			final String VIEW_NAME,
			final HelpContentTableLayoutFunctions helpContentTableLayoutFunctions
			) {
		// TODO Auto-generated constructor stub
		this.VIEW_NAME = VIEW_NAME;
		this.helpContentTableLayoutFunctions = helpContentTableLayoutFunctions;
		try{
			logger.info("\n HelpContentTableLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);			
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

	/*public HelpContentItemTableLayout(Component... children) {
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
		this.buildHelpContentTable();
		addComponent(this.buildToolbar());
        addComponent(helpContentListTable);
        setExpandRatio(helpContentListTable, 1);
	}
	
	private void buildHelpContentTable() throws Exception{
		if(this.helpContentListTable!=null)this.removeComponent(this.helpContentListTable);
		this.setUpHelpContentList();
		if(listHelpContent==null)this.listHelpContent = new ArrayList<HelpContent>();
    	this.helpContentListTable = new Table();
    	BeanItemContainer<HelpContent> HelpContentBeanItemContainer	= new BeanItemContainer<HelpContent>(HelpContent.class);
    	HelpContentBeanItemContainer.addAll(this.listHelpContent);    	
    	this.helpContentListTable.setContainerDataSource(HelpContentBeanItemContainer); 
    	
    	this.helpContentListTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final HelpContent vHelpContent = (HelpContent)itemId;
				return buildOperationsButtonPanel(vHelpContent);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//helpContentListTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {

    	this.helpContentListTable.setVisibleColumns(new Object[] 
    			{"operations","mainMenuItemKeyValue","moduleKeyValue"});
    	this.helpContentListTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.helpContentListTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.helpContentListTable.setColumnHeader("mainMenuItemKeyValue", this.messages.get(this.VIEW_NAME + "tab.help.content.table.column.main.menu.item"));
    	this.helpContentListTable.setColumnAlignment("mainMenuItemKeyValue", Table.Align.LEFT);
    	
    	this.helpContentListTable.setColumnHeader("moduleKeyValue", this.messages.get(this.VIEW_NAME + "tab.help.content.table.column.module"));
    	this.helpContentListTable.setColumnAlignment("moduleKeyValue", Table.Align.LEFT);
    	

    	this.helpContentListTable.setSizeFull();
    	this.helpContentListTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.helpContentListTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.helpContentListTable.addStyleName(ValoTheme.TABLE_SMALL);    	 
    	this.helpContentListTable.setColumnExpandRatio("mainMenuItemKeyValue", 0.0052f);    	
    	this.helpContentListTable.setColumnExpandRatio("moduleKeyValue", 0.019f);
    	this.helpContentListTable.setColumnExpandRatio("operations", 0.0025f);
    	this.helpContentListTable.setSelectable(true);
    	this.helpContentListTable.setColumnCollapsingAllowed(true);
    	this.helpContentListTable.setColumnCollapsible("mainMenuItemKeyValue", false);
    	this.helpContentListTable.setColumnCollapsible("moduleKeyValue", true);
    	this.helpContentListTable.setColumnCollapsible("operations", true);    	
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	this.helpContentListTable.setSortContainerPropertyId("moduleKeyValue");
    	this.helpContentListTable.setSortAscending(true);
    	this.helpContentListTable.setSortEnabled(true);
    	this.helpContentListTable.setColumnReorderingAllowed(true);
    	this.helpContentListTable.setFooterVisible(true);
    	this.helpContentListTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}

	private void setUpHelpContentList() throws Exception{
		this.listHelpContent = this.helpContentTableLayoutFunctions.setUpHelpContentList();
	}
	
	private HorizontalLayout buildOperationsButtonPanel(final HelpContent vHelpContent){	
		final Button queryButton = new Button();
		queryButton.setIcon(FontAwesome.SEARCH);
		queryButton.setDescription(this.messages.get("application.common.query.label"));
		queryButton.addStyleName("borderless");
		queryButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar vHelpContent...\n" + vHelpContent.toString());
                try{		                	
                	helpContentTableLayoutFunctions.queryListHelpContentItem(vHelpContent);
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
	    if (this.helpContentListTable!=null && defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.helpContentListTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.helpContentListTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
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
        Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.help.content.table.main.title"));
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
}
