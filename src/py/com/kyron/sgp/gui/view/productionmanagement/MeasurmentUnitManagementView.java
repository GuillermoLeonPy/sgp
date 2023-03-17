package py.com.kyron.sgp.gui.view.productionmanagement;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MeasurmentUnitDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.MeasurmentUnitRegisterFormViewEvent;
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
public class MeasurmentUnitManagementView extends VerticalLayout implements
		View {

	private final Logger logger = LoggerFactory.getLogger(ProductManagementView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "measurmentunit.management.";
	private Table measurmentUnitTable = null;
    private static final String[] DEFAULT_COLLAPSIBLE = {"measurment_unit_description"};
    private ProductionManagementService productionManagementService;
    private CommonExceptionErrorNotification commonExceptionErrorNotification;
    
	public MeasurmentUnitManagementView() {
		try{
			logger.info("\n MeasurmentUnitManagementView()...");
			this.initServices();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        this.buildMeasurmentUnitTable();
	        addComponent(this.buildToolbar());//toolbar: buildFilter() requiere tabla inicializada
	        addComponent(measurmentUnitTable);
	        setExpandRatio(measurmentUnitTable, 1);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}
	
	
	private void initServices() throws Exception{
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
	}

	public MeasurmentUnitManagementView(Component... children) {
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
	
	 private void buildMeasurmentUnitTable() throws PmsServiceException{
	    	this.measurmentUnitTable = new Table();
	    	BeanItemContainer<MeasurmentUnitDTO> measurmentUnitDTOBeanItemContainer	= new BeanItemContainer<MeasurmentUnitDTO>(MeasurmentUnitDTO.class);
	    	measurmentUnitDTOBeanItemContainer.addAll(this.productionManagementService.listMeasurmentUnitDTO(null));
	    	this.measurmentUnitTable.setContainerDataSource(measurmentUnitDTOBeanItemContainer);
	    	this.measurmentUnitTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					// TODO Auto-generated method stub
					final MeasurmentUnitDTO vMeasurmentUnitDTO = (MeasurmentUnitDTO)itemId;
					final Button editButton = new Button();
					editButton.setDescription(messages.get(VIEW_NAME + "table.measurmentunit.column.operations.button.description"));
					editButton.setIcon(FontAwesome.EDIT);
					editButton.addStyleName("borderless");
					editButton.addClickListener(new ClickListener() {
			            @Override
			            public void buttonClick(final ClickEvent event) {
			                logger.info("\n editar MeasurmentUnitDTO...\n" + vMeasurmentUnitDTO.toString());
			                try{		                	
			                	editMeasurmentUnit(vMeasurmentUnitDTO, false);
			                }catch(Exception e){
			                	commonExceptionErrorNotification.showErrorMessageNotification(e);
			                }			                
			            }
			        });
					return editButton;
				}//public Object generateCell(Table source, Object itemId, Object columnId)
			});//measurmentUnitTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
	    	
	    	this.measurmentUnitTable.setVisibleColumns(new Object[] {"operations","measurment_unit_id","measurment_unit_description"});
	    	this.measurmentUnitTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
	    	this.measurmentUnitTable.setColumnAlignment("operations", Table.Align.LEFT);
	    	this.measurmentUnitTable.setColumnHeader("measurment_unit_id", this.messages.get(this.VIEW_NAME + "table.measurmentunit.column.measurmentunitid.label"));
	    	this.measurmentUnitTable.setColumnAlignment("measurment_unit_id", Table.Align.LEFT);
	    	this.measurmentUnitTable.setColumnHeader("measurment_unit_description", this.messages.get("application.common.table.description.column.label"));
	    	this.measurmentUnitTable.setColumnAlignment("measurment_unit_description", Table.Align.LEFT);
	    	this.measurmentUnitTable.setSizeFull();
	    	this.measurmentUnitTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
	    	this.measurmentUnitTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
	    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
	    	this.measurmentUnitTable.addStyleName(ValoTheme.TABLE_SMALL);    
	    	this.measurmentUnitTable.setColumnExpandRatio("operations", 0.017f); 
	    	this.measurmentUnitTable.setColumnExpandRatio("measurment_unit_id", 0.05f);    	
	    	this.measurmentUnitTable.setColumnExpandRatio("measurment_unit_description", 0.1f);
	    	this.measurmentUnitTable.setSelectable(true);
	    	this.measurmentUnitTable.setColumnCollapsingAllowed(true);
	    	this.measurmentUnitTable.setColumnCollapsible("operations", false);
	    	this.measurmentUnitTable.setColumnCollapsible("measurment_unit_id", false);
	    	//this.rolesTable.setColumnCollapsible("role_description", false);
	    	this.measurmentUnitTable.setSortContainerPropertyId("measurment_unit_id");
	    	this.measurmentUnitTable.setSortAscending(false);
	    	this.measurmentUnitTable.setColumnReorderingAllowed(true);
	    	this.measurmentUnitTable.setFooterVisible(true);
	    	this.measurmentUnitTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	 }//private void buildMachinesTable(){
	 
	 private void editMeasurmentUnit(MeasurmentUnitDTO vMeasurmentUnitDTO,  boolean massiveInsertMode){
		try{
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.REGISTER_MEASURMENT_UNIT_FORM.getViewName());
			DashboardEventBus.post(new MeasurmentUnitRegisterFormViewEvent(vMeasurmentUnitDTO, DashboardViewType.MEASURMENT_UNIT_MANAGEMENT.getViewName(), massiveInsertMode));
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
	        
	        HorizontalLayout tools = new HorizontalLayout(this.buildFilter(),this.createRegisterMeasurmentUnitButton());	        	        
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
	                Filterable data = (Filterable) measurmentUnitTable.getContainerDataSource();
	                data.removeAllContainerFilters();
	                data.addContainerFilter(new Filter() {
	                    @Override
	                    public boolean passesFilter(final Object itemId,final Item item) {
	                        if (event.getText() == null || event.getText().equals("")) {
	                            return true;
	                        }

	                        return filterByProperty("measurment_unit_id", item, event.getText()) || filterByProperty("measurment_unit_description", item, event.getText());
	                    }
	                    @Override
	                    public boolean appliesToProperty(final Object propertyId) {
	                        if (propertyId.equals("measurment_unit_id") || propertyId.equals("measurment_unit_description")) {
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
	             ((Filterable) measurmentUnitTable.getContainerDataSource()).removeAllContainerFilters();
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

	 private Button createRegisterMeasurmentUnitButton(){
	        final Button registerMeasurmentUnit = new Button(this.messages.get(this.VIEW_NAME + "button.register.measurmentunit"));
	        registerMeasurmentUnit.setDescription(this.messages.get(this.VIEW_NAME + "button.register.measurmentunit.description"));
	        registerMeasurmentUnit.addClickListener(new ClickListener() {
	            @Override
	            public void buttonClick(final ClickEvent event) {
	            	editMeasurmentUnit(null, true);
	            }
	        });
	        registerMeasurmentUnit.setEnabled(true);
	        return registerMeasurmentUnit;
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
	    	   this.measurmentUnitTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.measurmentUnitTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()


}
