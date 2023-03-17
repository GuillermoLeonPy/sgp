package py.com.kyron.sgp.gui.view.personmanagement;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityRolDTO;
import py.com.kyron.sgp.bussines.applicationsecurity.service.ApplicationSecurityService;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.event.DashboardEvent.EditMonedaEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Item;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

import py.com.kyron.sgp.gui.event.SgpEvent.RoleAdministrationViewEvent;;

@SuppressWarnings("serial")
public class RoleManagementView extends VerticalLayout implements View {

	private final Logger logger = LoggerFactory.getLogger(RoleManagementView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "role.management.";
	private ApplicationSecurityService applicationSecurityService;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Table rolesTable = null;
	private static final String[] DEFAULT_COLLAPSIBLE = {"role_description"};
	
	public RoleManagementView() {
		// TODO Auto-generated constructor stub
		try{				
			logger.info("\n RoleManagementView()...");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.initServices();			
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        buildRolesTable();
	        addComponent(buildToolbar());//toolbar: buildFilter() requiere tabla inicializada
	        addComponent(rolesTable);
	        setExpandRatio(rolesTable, 1);
		}catch(Exception e){
			logger.error(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null));
			logger.error(e.getMessage(), e);
			addComponent(new Label(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null)));
		}
	}
	
	private void initServices() throws Exception{
		this.applicationSecurityService = (ApplicationSecurityService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.APPLICATION_SECURITY_SERVICE);
	}//private void initServices() throws Exception

	public RoleManagementView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

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
    
    private Table buildRolesTable() throws PmsServiceException{
    	this.rolesTable = new Table();
    	BeanItemContainer<ApplicationSecurityRolDTO> applicationSecurityRolDTOBeanItemContainer	= new BeanItemContainer<ApplicationSecurityRolDTO>(ApplicationSecurityRolDTO.class);
    	applicationSecurityRolDTOBeanItemContainer.addAll(this.applicationSecurityService.listApplicationSecurityRolDTO(null));
    	this.rolesTable.setContainerDataSource(applicationSecurityRolDTOBeanItemContainer);
    	this.rolesTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ApplicationSecurityRolDTO applicationSecurityRolDTO = (ApplicationSecurityRolDTO)itemId;
				final Button editButton = new Button();
				editButton.setIcon(FontAwesome.EDIT);
				editButton.setDescription(messages.get("application.common.table.column.operations.edit"));
				editButton.addStyleName("borderless");
				editButton.addClickListener(new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		                logger.info("\n editar applicationSecurityRolDTO...\n" + applicationSecurityRolDTO.toString());
		                try{		                	
		                	editRole(applicationSecurityRolDTO, false);
		                }catch(Exception e){
		                	commonExceptionErrorNotification.showErrorMessageNotification(e);
		                }			                
		            }
		        });
				return editButton;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//currencyTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	
    	
    	this.rolesTable.setVisibleColumns(new Object[] {"operations","role_name","role_description"});
    	this.rolesTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.rolesTable.setColumnAlignment("operations", Table.Align.LEFT);
    	this.rolesTable.setColumnHeader("role_name", this.messages.get(this.VIEW_NAME + "table.roles.column.rolename.label"));
    	this.rolesTable.setColumnAlignment("role_name", Table.Align.LEFT);
    	this.rolesTable.setColumnHeader("role_description", this.messages.get("application.common.table.description.column.label"));
    	this.rolesTable.setColumnAlignment("role_description", Table.Align.LEFT);
    	this.rolesTable.setSizeFull();
    	//this.rolesTable.setWidth("100%");
    	this.rolesTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.rolesTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.rolesTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.rolesTable.setColumnExpandRatio("operations", 0.025f); 
    	this.rolesTable.setColumnExpandRatio("role_name", 0.05f);    	
    	this.rolesTable.setColumnExpandRatio("role_description", 0.1f);
    	this.rolesTable.setSelectable(true);
    	this.rolesTable.setColumnCollapsingAllowed(true);
    	this.rolesTable.setColumnCollapsible("operations", false);
    	this.rolesTable.setColumnCollapsible("role_name", false);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	this.rolesTable.setSortContainerPropertyId("role_name");
    	this.rolesTable.setSortAscending(false);
    	this.rolesTable.setColumnReorderingAllowed(true);
    	this.rolesTable.setFooterVisible(true);
    	this.rolesTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");   
    	//Responsive.makeResponsive(this.rolesTable);
    	return this.rolesTable;
    }
    
    /*private void reSetTableColumnsExpandRatio(){
    	this.rolesTable.setColumnExpandRatio("operations", 0.025f); 
    	this.rolesTable.setColumnExpandRatio("role_name", 0.05f);    	
    	this.rolesTable.setColumnExpandRatio("role_description", 0.1f);
    	this.rolesTable.markAsDirty();
    }*/
    
	 private Component buildToolbar() {
	        HorizontalLayout header = new HorizontalLayout();
	        header.addStyleName("viewheader");
	        header.setSpacing(true);
	        Responsive.makeResponsive(header);
	        logger.info("\n*********************remember: an HorizontalLayout is set with the instrucction: Responsive.makeResponsive(header); !!! "
	        		+"\n*********************");
	        Label title = new Label(this.messages.get(this.VIEW_NAME + "main.title"));
	        title.setSizeUndefined();
	        title.addStyleName(ValoTheme.LABEL_H1);
	        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	        header.addComponent(title);
	        
	        HorizontalLayout tools = new HorizontalLayout(buildFilter(),createRegisterRoleButton());	        	        
	        tools.setSpacing(true);
	        tools.addStyleName("toolbar");
	        header.addComponent(tools);
	        return header;
	 }//private Component buildToolbar()
	 
    private void editRole(ApplicationSecurityRolDTO applicationSecurityRolDTO, boolean massiveInsertMode){
		try{
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.REGISTER_ROLE_FORM.getViewName());
			DashboardEventBus.post(new RoleAdministrationViewEvent(applicationSecurityRolDTO, DashboardViewType.ROLE_MANAGEMENT.getViewName(), massiveInsertMode));
		}catch(Exception e){
			logger.error(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null));
			logger.error(e.getMessage(), e);
		}
    }
    
	 private Component buildFilter() {
	        final TextField filter = new TextField();
	        filter.addTextChangeListener(new TextChangeListener() {
	            @Override
	            public void textChange(final TextChangeEvent event) {
	                Filterable data = (Filterable) rolesTable.getContainerDataSource();
	                data.removeAllContainerFilters();
	                data.addContainerFilter(new Filter() {
	                    @Override
	                    public boolean passesFilter(final Object itemId,final Item item) {
	                        if (event.getText() == null || event.getText().equals("")) {
	                            return true;
	                        }

	                        return filterByProperty("role_name", item, event.getText()) || filterByProperty("role_description", item, event.getText());
	                    }
	                    @Override
	                    public boolean appliesToProperty(final Object propertyId) {
	                        if (propertyId.equals("role_name") || propertyId.equals("role_description")) {
	                            return true;
	                        }
	                        return false;
	                    }
	                });
	            }
	        });
		 
	     filter.setInputPrompt(this.messages.get("application.common.operation.filter.label"));
	     filter.setIcon(FontAwesome.SEARCH);
	     filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
	     filter.addShortcutListener(new ShortcutListener("Clear", KeyCode.ESCAPE, null) {
	    	 @Override
	         public void handleAction(final Object sender, final Object target) {
	    		 filter.setValue("");
	             ((Filterable) rolesTable.getContainerDataSource()).removeAllContainerFilters();
	         }
	     });
	     return filter;
	 }//private Component buildFilter()
	 
	    private boolean filterByProperty(final String prop, final Item item, final String text) {
	    	//logger.info("\n************************ MARK TABLE AS DIRTY AFTER FILTER ACTION\n************************");
	    	//this.reSetTableColumnsExpandRatio();
	        if (item == null || item.getItemProperty(prop) == null || item.getItemProperty(prop).getValue() == null) {
	            return false;
	        }
	        String val = item.getItemProperty(prop).getValue().toString().trim().toLowerCase();
	        if (val.contains(text.toLowerCase().trim())) {
	            return true;
	        }
	        return false;
	    }//private boolean filterByProperty(final String prop, final Item item, final String text)
	    
		 private Button createRegisterRoleButton(){
		        final Button registerCurrency = new Button(this.messages.get(this.VIEW_NAME + "button.register.role"));
		        registerCurrency.setDescription(this.messages.get(this.VIEW_NAME + "button.register.role.description"));
		        registerCurrency.addClickListener(new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	editRole(null, true);
		            }
		        });
		        registerCurrency.setEnabled(true);
		        return registerCurrency;
		 }//private Button createRegisterCurrencyButton()
		 
	@Subscribe
	public void backNavigation(final RoleAdministrationViewEvent roleAdministrationViewEvent){
		try{
		
			//logger.info("\nRoleManagementView.backNavigation\n this.rolesTable.markAsDirty()");
			//logger.info("\n************************ MARK TABLE AS DIRTY AFTER NAVIGATION BACK ACTION\n************************");
			//this.buildRolesTable();
			//this.rolesTable.refreshRowCache();
			//this.reSetTableColumnsExpandRatio();
//		removeComponent(rolesTable);
//        addComponent(rolesTable);
//        setExpandRatio(rolesTable, 1);
		}catch(Exception e){
			logger.error(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null));
			logger.error(e.getMessage(), e);			
		}
	}
	
	 @Subscribe
	 public void browserResized(final BrowserResizeEvent event) {
	     // Some columns are collapsed when browser window width gets small
	     // enough to make the table fit better.
	     if (defaultColumnsVisible()) {
	        for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        	this.rolesTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	        }
	     }
	 }//public void browserResized(final BrowserResizeEvent event)
	 
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.rolesTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	     }
	     return result;
	}//private boolean defaultColumnsVisible()

}
