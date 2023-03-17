package py.com.kyron.sgp.gui.view.stockmanagement;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.event.SgpEvent.RawMaterialStockManagementFormViewEvent;

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
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.PopupView.Content;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class RawMaterialStockManagementView extends VerticalLayout implements
		View {

	private final Logger logger = LoggerFactory.getLogger(RawMaterialStockManagementView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "rawmaterial.stock.management.";
	private Table rawMaterialTable = null;
    private static final String[] DEFAULT_COLLAPSIBLE = {"raw_material_description"};
    private ProductionManagementService productionManagementService;
    private CommonExceptionErrorNotification commonExceptionErrorNotification;
    
	public RawMaterialStockManagementView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n RawMaterialStockManagementView()...");
			this.initServices();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
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

	public RawMaterialStockManagementView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}


	private void initServices() throws Exception{
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		logger.info("\n==========================================="
					+"\n RawMaterialStockManagementView"
					+"\n a view change event has been registered"
					+"\n===========================================");
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n RawMaterialStockManagementView" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
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
	    	this.rawMaterialTable.setColumnHeader("raw_material_id", this.messages.get("application.common.rawmaterialid.label"));
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
	 
	 private void goToRawMaterialStockManagementFormView(RawMaterialDTO vRawMaterialDTO){
		try{
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.RAW_MATERIAL_STOCK_MANAGEMENT_FORM.getViewName());
			DashboardEventBus.post(new RawMaterialStockManagementFormViewEvent(vRawMaterialDTO, DashboardViewType.RAW_MATERIAL_STOCK_MANAGEMENT.getViewName()));
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
		
        /*final PopupView has_no_existence_recordPopupView = new PopupView(new Content() {
            @Override
            public Component getPopupComponent() {
                return new VerticalLayout() {
                    {
                        setMargin(true);setWidth("300px");                        
                        addComponent(new Label(messages.get(VIEW_NAME + "table.raw.material.colum.operations.button.has.no.existence.record.descrption")));
                    }
                };
            }

            @Override
            public String getMinimizedValueAsHTML() {
                return "Click to view";
            }        
        });
        has_no_existence_recordPopupView.setIcon(FontAwesome.EXCLAMATION_CIRCLE);
        */
		
        
        
		final Button has_no_existence_recordButton = new Button();
		has_no_existence_recordButton.addStyleName("borderless");
		has_no_existence_recordButton.setIcon(FontAwesome.EXCLAMATION_CIRCLE);		
		has_no_existence_recordButton.setVisible(!vRawMaterialDTO.getHas_existence_record());		
		has_no_existence_recordButton.setDescription(messages.get(VIEW_NAME + "table.raw.material.colum.operations.button.has.no.existence.record.descrption"));
		has_no_existence_recordButton.setResponsive(false);

		final Button has_existence_with_warningButton = new Button();
		has_existence_with_warningButton.addStyleName("borderless");
		has_existence_with_warningButton.setIcon(FontAwesome.SORT_AMOUNT_DESC);		
		has_existence_with_warningButton.setVisible(vRawMaterialDTO.getHas_existence_with_warning());		
		has_existence_with_warningButton.setDescription(messages.get(VIEW_NAME + "table.raw.material.colum.operations.button.has.existence.record.with.warning.descrption"));
		has_existence_with_warningButton.setResponsive(false);
		
		final Button has_existence_out_of_stockButton = new Button();
		has_existence_out_of_stockButton.addStyleName("borderless");
		has_existence_out_of_stockButton.setIcon(FontAwesome.BATTERY_EMPTY);		
		has_existence_out_of_stockButton.setVisible(vRawMaterialDTO.getHas_existence_out_of_stock());		
		has_existence_out_of_stockButton.setDescription(messages.get(VIEW_NAME + "table.raw.material.colum.operations.button.has.existence.out.of.stock.descrption"));
		has_existence_out_of_stockButton.setResponsive(false);
		
		final Button has_no_purchase_request_registeredButton = new Button();
		has_no_purchase_request_registeredButton.setIcon(FontAwesome.EXCLAMATION_TRIANGLE);
		has_no_purchase_request_registeredButton.addStyleName("borderless");
		//has_no_purchase_request_registeredButton.setVisible(vRawMaterialDTO.getHas_existence_record() && !vRawMaterialDTO.getHas_any_purchase_request());
		has_no_purchase_request_registeredButton.setVisible(false);
		has_no_purchase_request_registeredButton.setDescription(messages.get(VIEW_NAME + "table.raw.material.colum.operations.button.has.no.purchase.request.registered.descrption"));
		has_no_purchase_request_registeredButton.setResponsive(false);
		
		final Button has_pending_purchase_request = new Button();
		has_pending_purchase_request.setIcon(FontAwesome.FLAG_CHECKERED);
		has_pending_purchase_request.addStyleName("borderless");
		//has_pending_purchase_request.setVisible(vRawMaterialDTO.getHas_any_purchase_request() && vRawMaterialDTO.getHas_pending_purchase_request());
		has_pending_purchase_request.setVisible(false);
		has_pending_purchase_request.setDescription(messages.get(VIEW_NAME + "table.raw.material.colum.operations.button.has.pending.purchase.request.descrption"));
		has_pending_purchase_request.setResponsive(false);
		
		final Button editButton = new Button();
		editButton.setIcon(FontAwesome.SEARCH);
		editButton.setDescription(this.messages.get("application.common.query.label") /*+ " " + this.messages.get(this.VIEW_NAME + "main.title").toLowerCase()*/);
		editButton.addStyleName("borderless");
		editButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar RawMaterialDTO...\n" + vRawMaterialDTO.toString());
                try{		                	
                	goToRawMaterialStockManagementFormView(vRawMaterialDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });	
		
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(editButton,has_no_existence_recordButton,has_no_purchase_request_registeredButton,has_pending_purchase_request,has_existence_out_of_stockButton,has_existence_with_warningButton);
		//operationsButtonPanel.addStyleName("wrapping");
		return operationsButtonPanel;
	}
}
