package py.com.kyron.sgp.gui.view.productionmanagement;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.ProductDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.ProductionProcessManagementOperationViewEvent;
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

@SuppressWarnings("serial")
public class ProductionProcessManagementView extends VerticalLayout implements
		View {

	private final Logger logger = LoggerFactory.getLogger(ProductionProcessManagementView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.management.";
	private Table productTable = null;
    private static final String[] DEFAULT_COLLAPSIBLE = {"product_description"};
    private ComercialManagementService comercialManagementService;
    private CommonExceptionErrorNotification commonExceptionErrorNotification;
	
	public ProductionProcessManagementView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n ProductionProcessManagementView()...");
			this.initServices();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        this.buildProductTable();
	        addComponent(this.buildToolbar());//toolbar: buildFilter() requiere tabla inicializada
	        addComponent(productTable);
	        setExpandRatio(productTable, 1);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	public ProductionProcessManagementView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}


	private void initServices() throws Exception{
		this.comercialManagementService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		logger.info("\n==========================================="
					+"\n ProductionProcessManagementView"
					+"\n a view change event has been registered"
					+"\n===========================================");
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n ProductionProcessManagementView" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	 private void buildProductTable() throws PmsServiceException{
	    	this.productTable = new Table();
	    	BeanItemContainer<ProductDTO> productDTOBeanItemContainer	= new BeanItemContainer<ProductDTO>(ProductDTO.class);
	    	productDTOBeanItemContainer.addAll(this.comercialManagementService.listProductDTO(null));
	    	this.productTable.setContainerDataSource(productDTOBeanItemContainer);
	    	this.productTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					// TODO Auto-generated method stub
					final ProductDTO vProductDTO = (ProductDTO)itemId;
					return buildOperationsButtonPanel(vProductDTO);
				}//public Object generateCell(Table source, Object itemId, Object columnId)
			});//productTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
	    	
	    	this.productTable.setVisibleColumns(new Object[] {"operations","product_id","product_description"});
	    	this.productTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
	    	this.productTable.setColumnAlignment("operations", Table.Align.LEFT);
	    	this.productTable.setColumnHeader("product_id", this.messages.get(this.VIEW_NAME + "table.product.column.productid.label"));
	    	this.productTable.setColumnAlignment("product_id", Table.Align.LEFT);
	    	this.productTable.setColumnHeader("product_description", this.messages.get("application.common.table.description.column.label"));
	    	this.productTable.setColumnAlignment("product_description", Table.Align.LEFT);
	    	this.productTable.setSizeFull();
	    	this.productTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
	    	this.productTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
	    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
	    	this.productTable.addStyleName(ValoTheme.TABLE_SMALL);    
	    	this.productTable.setColumnExpandRatio("operations", 0.025f); 
	    	this.productTable.setColumnExpandRatio("product_id", 0.05f);    	
	    	this.productTable.setColumnExpandRatio("product_description", 0.1f);
	    	this.productTable.setSelectable(true);
	    	this.productTable.setColumnCollapsingAllowed(true);
	    	this.productTable.setColumnCollapsible("operations", false);
	    	this.productTable.setColumnCollapsible("product_id", false);
	    	//this.rolesTable.setColumnCollapsible("role_description", false);
	    	this.productTable.setSortContainerPropertyId("product_id");
	    	this.productTable.setSortAscending(false);
	    	this.productTable.setColumnReorderingAllowed(true);
	    	this.productTable.setFooterVisible(true);
	    	this.productTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	 }//private void buildMachinesTable(){
	 
	 private void editProductProductionProcess(ProductDTO vProductDTO,  boolean massiveInsertMode){
		try{
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PRODUCTION_PROCESS_MANAGEMENT_OPERATION.getViewName());
			DashboardEventBus.post(new ProductionProcessManagementOperationViewEvent(vProductDTO, DashboardViewType.PRODUCTION_PROCESS_MANAGEMENT.getViewName()));
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
	        
	        HorizontalLayout tools = new HorizontalLayout(this.buildFilter());	        	        
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
	                Filterable data = (Filterable) productTable.getContainerDataSource();
	                data.removeAllContainerFilters();
	                data.addContainerFilter(new Filter() {
	                    @Override
	                    public boolean passesFilter(final Object itemId,final Item item) {
	                        if (event.getText() == null || event.getText().equals("")) {
	                            return true;
	                        }

	                        return filterByProperty("product_id", item, event.getText()) || filterByProperty("product_description", item, event.getText());
	                    }
	                    @Override
	                    public boolean appliesToProperty(final Object propertyId) {
	                        if (propertyId.equals("product_id") || propertyId.equals("product_description")) {
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
	             ((Filterable) productTable.getContainerDataSource()).removeAllContainerFilters();
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


	 
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    // Some columns are collapsed when browser window width gets small
	    // enough to make the table fit better.
	    if (defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.productTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.productTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
	
	private HorizontalLayout buildOperationsButtonPanel(final ProductDTO vProductDTO){
       
		final Button has_no_production_processButton = new Button();
		has_no_production_processButton.addStyleName("borderless");
		has_no_production_processButton.setIcon(FontAwesome.EXCLAMATION_CIRCLE);		
		has_no_production_processButton.setVisible(!vProductDTO.getHas_production_process());		
		has_no_production_processButton.setDescription(messages.get(VIEW_NAME + "table.product.colum.operations.button.has.no.production.process.descrption"));
		has_no_production_processButton.setResponsive(false);
		
		final Button editButton = new Button();
		editButton.setIcon(FontAwesome.EDIT);
		editButton.addStyleName("borderless");
		editButton.setDescription(this.messages.get(this.VIEW_NAME + "main.title"));
		editButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar ProductDTO...\n" + vProductDTO.toString());
                try{		                	
                	editProductProductionProcess(vProductDTO, false);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(editButton,has_no_production_processButton);
		//operationsButtonPanel.addStyleName("wrapping");
		return operationsButtonPanel;
	}
}
