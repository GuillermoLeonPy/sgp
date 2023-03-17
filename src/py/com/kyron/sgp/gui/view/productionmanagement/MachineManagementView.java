package py.com.kyron.sgp.gui.view.productionmanagement;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.event.SgpEvent.MachineRegisterFormViewEvent;

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

@SuppressWarnings("serial")
public class MachineManagementView extends VerticalLayout implements View {

	private final Logger logger = LoggerFactory.getLogger(MachineManagementView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "machine.management.";
	private Table machineTable = null;
    private static final String[] DEFAULT_COLLAPSIBLE = {"machine_description"};
    private ProductionManagementService productionManagementService;
    private CommonExceptionErrorNotification commonExceptionErrorNotification;
	
	public MachineManagementView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n MachineManagementView()...");
			this.initServices();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        this.buildMachinesTable();
	        addComponent(this.buildToolbar());//toolbar: buildFilter() requiere tabla inicializada
	        addComponent(machineTable);
	        setExpandRatio(machineTable, 1);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}
	
	private void initServices() throws Exception{
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
	}

	public MachineManagementView(Component... children) {
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

	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    // Some columns are collapsed when browser window width gets small
	    // enough to make the table fit better.
	    if (defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.machineTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	 }//public void browserResized(final BrowserResizeEvent event)
	 
	 private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.machineTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	 }//private boolean defaultColumnsVisible()
	 
	 private void buildMachinesTable() throws PmsServiceException{
	    	this.machineTable = new Table();
	    	BeanItemContainer<MachineDTO> machineDTOBeanItemContainer	= new BeanItemContainer<MachineDTO>(MachineDTO.class);
	    	machineDTOBeanItemContainer.addAll(this.productionManagementService.listMachineDTO(null));
	    	this.machineTable.setContainerDataSource(machineDTOBeanItemContainer);
	    	this.machineTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					// TODO Auto-generated method stub
					final MachineDTO vMachineDTO = (MachineDTO)itemId;
					return buildOperationsButtonPanel(vMachineDTO);
				}//public Object generateCell(Table source, Object itemId, Object columnId)
			});//machineTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
	    	
	    	this.machineTable.setVisibleColumns(new Object[] {"operations","machine_id","machine_description"});
	    	this.machineTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
	    	this.machineTable.setColumnAlignment("operations", Table.Align.LEFT);
	    	this.machineTable.setColumnHeader("machine_id", this.messages.get(this.VIEW_NAME + "table.machines.column.machineid.label"));
	    	this.machineTable.setColumnAlignment("machine_id", Table.Align.LEFT);
	    	this.machineTable.setColumnHeader("machine_description", this.messages.get("application.common.table.description.column.label"));
	    	this.machineTable.setColumnAlignment("machine_description", Table.Align.LEFT);
	    	this.machineTable.setSizeFull();
	    	this.machineTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
	    	this.machineTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
	    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
	    	this.machineTable.addStyleName(ValoTheme.TABLE_SMALL);    
	    	this.machineTable.setColumnExpandRatio("operations", 0.019f); 
	    	this.machineTable.setColumnExpandRatio("machine_id", 0.05f);    	
	    	this.machineTable.setColumnExpandRatio("machine_description", 0.1f);
	    	this.machineTable.setSelectable(true);
	    	this.machineTable.setColumnCollapsingAllowed(true);
	    	this.machineTable.setColumnCollapsible("operations", false);
	    	this.machineTable.setColumnCollapsible("machine_id", false);
	    	//this.rolesTable.setColumnCollapsible("role_description", false);
	    	this.machineTable.setSortContainerPropertyId("machine_id");
	    	this.machineTable.setSortAscending(false);
	    	this.machineTable.setColumnReorderingAllowed(true);
	    	this.machineTable.setFooterVisible(true);
	    	this.machineTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	 }//private void buildMachinesTable(){
	 
	 private void editMachine(MachineDTO vMachineDTO,  boolean massiveInsertMode){
		 
		try{
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.REGISTER_MACHINE_FORM.getViewName());
			DashboardEventBus.post(new MachineRegisterFormViewEvent(vMachineDTO, DashboardViewType.MACHINE_MANAGEMENT.getViewName(), massiveInsertMode));
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
		  
	 }
	 
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
	        
	        HorizontalLayout tools = new HorizontalLayout(this.buildFilter(),this.createRegisterMachineButton());	        	        
	        tools.setSpacing(true);
	        tools.addStyleName("toolbar");
	        header.addComponent(tools);
	        return header;
	 }//private Component buildToolbar()
	 
	 private Component buildFilter() {
	        final TextField filter = new TextField();
	        filter.addTextChangeListener(new TextChangeListener() {
	            @Override
	            public void textChange(final TextChangeEvent event) {
	                Filterable data = (Filterable) machineTable.getContainerDataSource();
	                data.removeAllContainerFilters();
	                data.addContainerFilter(new Filter() {
	                    @Override
	                    public boolean passesFilter(final Object itemId,final Item item) {
	                        if (event.getText() == null || event.getText().equals("")) {
	                            return true;
	                        }

	                        return filterByProperty("machine_id", item, event.getText()) || filterByProperty("machine_description", item, event.getText());
	                    }
	                    @Override
	                    public boolean appliesToProperty(final Object propertyId) {
	                        if (propertyId.equals("machine_id") || propertyId.equals("machine_description")) {
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
	             ((Filterable) machineTable.getContainerDataSource()).removeAllContainerFilters();
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
	 
	 private Button createRegisterMachineButton(){
	        final Button registerMachine = new Button(this.messages.get(this.VIEW_NAME + "button.register.machine"));
	        registerMachine.setDescription(this.messages.get(this.VIEW_NAME + "button.register.machine.description"));
	        registerMachine.addClickListener(new ClickListener() {
	            @Override
	            public void buttonClick(final ClickEvent event) {
	            	editMachine(null, true);
	            }
	        });
	        registerMachine.setEnabled(true);
	        return registerMachine;
	 }//private Button createRegisterMachineButton()
	 
	private HorizontalLayout buildOperationsButtonPanel(final MachineDTO vMachineDTO){
		final Button has_no_valid_cost_registryButton = new Button();
		has_no_valid_cost_registryButton.addStyleName("borderless");
		has_no_valid_cost_registryButton.setIcon(FontAwesome.EXCLAMATION_CIRCLE);		
		has_no_valid_cost_registryButton.setVisible(!vMachineDTO.getHas_valid_cost_registry());		
		has_no_valid_cost_registryButton.setDescription(messages.get(VIEW_NAME + "table.machines.column.operations.button.has.no.valid.cost.registry.descrption"));
		has_no_valid_cost_registryButton.setResponsive(false);
		
		final Button editButton = new Button();
		editButton.setIcon(FontAwesome.EDIT);
		editButton.addStyleName("borderless");
		editButton.setDescription(messages.get(VIEW_NAME + "table.machines.column.operations.button.edit"));
		editButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar MachineDTO...\n" + vMachineDTO.toString());
                try{		                	
                	editMachine(vMachineDTO, false);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
			
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(editButton,has_no_valid_cost_registryButton);
		return operationsButtonPanel;
	}
}
