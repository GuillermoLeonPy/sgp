package py.com.kyron.sgp.gui.view.productionmanagement;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.RawMaterialRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.comercialmanagement.ProductManagementView;

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
public class RawMaterialManagementView extends VerticalLayout implements View {

	private final Logger logger = LoggerFactory.getLogger(RawMaterialManagementView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "rawmaterial.management.";
	private Table rawMaterialTable = null;
    private static final String[] DEFAULT_COLLAPSIBLE = {"raw_material_description"};
    private ProductionManagementService productionManagementService;
    private CommonExceptionErrorNotification commonExceptionErrorNotification;
    
	public RawMaterialManagementView() {
		try{
			logger.info("\n RawMaterialManagementView()...");
			this.initServices();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        this.buildRawMaterialTable();
	        addComponent(this.buildToolbar());//toolbar: buildFilter() requiere tabla inicializada
	        addComponent(rawMaterialTable);
	        setExpandRatio(rawMaterialTable, 1);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}
	
	private void initServices() throws Exception{
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
	}

	public RawMaterialManagementView(Component... children) {
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
	
	 private void buildRawMaterialTable() throws PmsServiceException{
	    	this.rawMaterialTable = new Table();
	    	BeanItemContainer<RawMaterialDTO> RawMaterialDTOBeanItemContainer	= new BeanItemContainer<RawMaterialDTO>(RawMaterialDTO.class);
	    	RawMaterialDTOBeanItemContainer.addAll(this.productionManagementService.listRawMaterialDTO(null));
	    	this.rawMaterialTable.setContainerDataSource(RawMaterialDTOBeanItemContainer);
	    	this.rawMaterialTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					// TODO Auto-generated method stub
					final RawMaterialDTO vRawMaterialDTO = (RawMaterialDTO)itemId;
					return buildOperationsButtonPanel(vRawMaterialDTO);
				}//public Object generateCell(Table source, Object itemId, Object columnId)
			});//rawMaterialTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
	    	
	    	this.rawMaterialTable.setVisibleColumns(new Object[] {"operations","raw_material_id","raw_material_description"});
	    	this.rawMaterialTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
	    	this.rawMaterialTable.setColumnAlignment("operations", Table.Align.LEFT);
	    	this.rawMaterialTable.setColumnHeader("raw_material_id", this.messages.get(this.VIEW_NAME + "table.rawmaterial.column.rawmaterialid.label"));
	    	this.rawMaterialTable.setColumnAlignment("raw_material_id", Table.Align.LEFT);
	    	this.rawMaterialTable.setColumnHeader("raw_material_description", this.messages.get("application.common.table.description.column.label"));
	    	this.rawMaterialTable.setColumnAlignment("raw_material_description", Table.Align.LEFT);
	    	this.rawMaterialTable.setSizeFull();
	    	this.rawMaterialTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
	    	this.rawMaterialTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
	    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
	    	this.rawMaterialTable.addStyleName(ValoTheme.TABLE_SMALL);    
	    	this.rawMaterialTable.setColumnExpandRatio("operations", 0.019f); 
	    	this.rawMaterialTable.setColumnExpandRatio("raw_material_id", 0.05f);    	
	    	this.rawMaterialTable.setColumnExpandRatio("raw_material_description", 0.1f);
	    	this.rawMaterialTable.setSelectable(true);
	    	this.rawMaterialTable.setColumnCollapsingAllowed(true);
	    	this.rawMaterialTable.setColumnCollapsible("operations", false);
	    	this.rawMaterialTable.setColumnCollapsible("raw_material_id", false);
	    	//this.rolesTable.setColumnCollapsible("role_description", false);
	    	this.rawMaterialTable.setSortContainerPropertyId("raw_material_id");
	    	this.rawMaterialTable.setSortAscending(false);
	    	this.rawMaterialTable.setColumnReorderingAllowed(true);
	    	this.rawMaterialTable.setFooterVisible(true);
	    	this.rawMaterialTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	 }//private void buildMachinesTable(){
	 
	 private void editRawMaterial(RawMaterialDTO vRawMaterialDTO,  boolean massiveInsertMode){
		try{
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.REGISTER_RAW_MATERIAL_FORM.getViewName());
			DashboardEventBus.post(new RawMaterialRegisterFormViewEvent(vRawMaterialDTO, DashboardViewType.RAW_MATERIAL_MANAGEMENT.getViewName(), massiveInsertMode));
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
	        
	        HorizontalLayout tools = new HorizontalLayout(this.buildFilter(),this.createRegisterRawMaterialButton());	        	        
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
	                Filterable data = (Filterable) rawMaterialTable.getContainerDataSource();
	                data.removeAllContainerFilters();
	                data.addContainerFilter(new Filter() {
	                    @Override
	                    public boolean passesFilter(final Object itemId,final Item item) {
	                        if (event.getText() == null || event.getText().equals("")) {
	                            return true;
	                        }

	                        return filterByProperty("raw_material_id", item, event.getText()) || filterByProperty("raw_material_description", item, event.getText());
	                    }
	                    @Override
	                    public boolean appliesToProperty(final Object propertyId) {
	                        if (propertyId.equals("raw_material_id") || propertyId.equals("raw_material_description")) {
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
	             ((Filterable) rawMaterialTable.getContainerDataSource()).removeAllContainerFilters();
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

	 private Button createRegisterRawMaterialButton(){
	        final Button registerRawMaterial = new Button(this.messages.get(this.VIEW_NAME + "button.register.rawmaterial"));
	        registerRawMaterial.setDescription(this.messages.get(this.VIEW_NAME + "button.register.rawmaterial.description"));
	        registerRawMaterial.addClickListener(new ClickListener() {
	            @Override
	            public void buttonClick(final ClickEvent event) {
	            	editRawMaterial(null, true);
	            }
	        });
	        registerRawMaterial.setEnabled(true);
	        return registerRawMaterial;
	 }//private Button createRegisterMachineButton()
	 
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    // Some columns are collapsed when browser window width gets small
	    // enough to make the table fit better.
	    if (defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.rawMaterialTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.rawMaterialTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
	
	private HorizontalLayout buildOperationsButtonPanel(final RawMaterialDTO vRawMaterialDTO){
		final Button has_no_valid_cost_registryButton = new Button();
		has_no_valid_cost_registryButton.addStyleName("borderless");
		has_no_valid_cost_registryButton.setIcon(FontAwesome.EXCLAMATION_CIRCLE);		
		has_no_valid_cost_registryButton.setVisible(!vRawMaterialDTO.getHas_valid_cost_registry());		
		has_no_valid_cost_registryButton.setDescription(messages.get(VIEW_NAME + "table.rawmaterial.column.operations.button.has.no.valid.cost.registry.descrption"));
		has_no_valid_cost_registryButton.setResponsive(false);
		
		final Button editButton = new Button();
		editButton.setIcon(FontAwesome.EDIT);
		editButton.addStyleName("borderless");
		editButton.setDescription(this.messages.get(this.VIEW_NAME + "main.title"));
		editButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar RawMaterialDTO...\n" + vRawMaterialDTO.toString());
                try{		                	
                	editRawMaterial(vRawMaterialDTO, false);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(editButton,has_no_valid_cost_registryButton);
		return operationsButtonPanel;
	}

}
