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
public class HelpContentItemTableLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(HelpContentItemTableLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Table helpContentItemListTable;
	private static final String[] DEFAULT_COLLAPSIBLE = {"operations"};
	private BussinesSessionUtils bussinesSessionUtils;
	private List<HelpContentItem> listHelpContentItem;
	private HelpContentItemTableLayoutFunctions helpContentItemTableLayoutFunctions;
	private final HelpContent helpContent;
	private String title;
	
	public HelpContentItemTableLayout(
			final String VIEW_NAME,
			final HelpContentItemTableLayoutFunctions helpContentItemTableLayoutFunctions,
			final HelpContent helpContent) {
		// TODO Auto-generated constructor stub
		this.VIEW_NAME = VIEW_NAME;
		this.helpContentItemTableLayoutFunctions = helpContentItemTableLayoutFunctions;
		this.helpContent = helpContent;
		try{
			logger.info("\n HelpContentItemTableLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);			
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        if(this.helpContent != null)this.listHelpContentItem = helpContent.getListHelpContentItem();
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
        addComponent(helpContentItemListTable);
        setExpandRatio(helpContentItemListTable, 1);
	}
	
	private void buildHelpContentTable() throws Exception{
		if(this.helpContentItemListTable!=null)this.removeComponent(this.helpContentItemListTable);
		
		if(listHelpContentItem==null)this.listHelpContentItem = new ArrayList<HelpContentItem>();
    	this.helpContentItemListTable = new Table();
    	BeanItemContainer<HelpContentItem> HelpContentItemBeanItemContainer	= new BeanItemContainer<HelpContentItem>(HelpContentItem.class);
    	HelpContentItemBeanItemContainer.addAll(this.listHelpContentItem);    	
    	this.helpContentItemListTable.setContainerDataSource(HelpContentItemBeanItemContainer); 
    	
    	this.helpContentItemListTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final HelpContentItem vHelpContentItem = (HelpContentItem)itemId;
				return buildOperationsButtonPanel(vHelpContentItem);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//helpContentItemListTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {

    	this.helpContentItemListTable.setVisibleColumns(new Object[] 
    			{"operations","helpContentItemKeyValue"});
    	this.helpContentItemListTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.helpContentItemListTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.helpContentItemListTable.setColumnHeader("helpContentItemKeyValue", this.messages.get(this.VIEW_NAME + "tab.help.content.item.table.column.item"));
    	this.helpContentItemListTable.setColumnAlignment("helpContentItemKeyValue", Table.Align.LEFT);

    	this.helpContentItemListTable.setSizeFull();
    	this.helpContentItemListTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.helpContentItemListTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.helpContentItemListTable.addStyleName(ValoTheme.TABLE_SMALL);    	 
    	this.helpContentItemListTable.setColumnExpandRatio("helpContentItemKeyValue", 0.015f);
    	this.helpContentItemListTable.setColumnExpandRatio("operations", 0.0011f);
    	this.helpContentItemListTable.setSelectable(true);
    	this.helpContentItemListTable.setColumnCollapsingAllowed(true);
    	this.helpContentItemListTable.setColumnCollapsible("helpContentItemKeyValue", false);
    	this.helpContentItemListTable.setColumnCollapsible("operations", true);    	
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	this.helpContentItemListTable.setSortContainerPropertyId("helpContentItemKeyValue");
    	this.helpContentItemListTable.setSortAscending(true);
    	this.helpContentItemListTable.setSortEnabled(true);
    	this.helpContentItemListTable.setColumnReorderingAllowed(true);
    	this.helpContentItemListTable.setFooterVisible(true);
    	this.helpContentItemListTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}


	
	private HorizontalLayout buildOperationsButtonPanel(final HelpContentItem vHelpContentItem){	
		final Button queryButton = new Button();
		queryButton.setIcon(FontAwesome.SEARCH);
		queryButton.setDescription(this.messages.get("application.common.query.label"));
		queryButton.addStyleName("borderless");
		queryButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar vHelpContentItem...\n" + vHelpContentItem.toString());
                try{		                	
                	helpContentItemTableLayoutFunctions.queryHelpContentItem(vHelpContentItem);
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
	    if (this.helpContentItemListTable!=null && defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.helpContentItemListTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.helpContentItemListTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
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
        if(this.helpContent!= null)
        	this.title = " : " + this.helpContent.getMainMenuItemKeyValue();
        else
        	this.title = "";
        
        Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.help.content.item.table.main.title") + this.title);
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
