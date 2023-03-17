package py.com.kyron.sgp.gui.view.productionmanagement;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ManPowerCostDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.ManPowerCostRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
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
public class ManPowerCostManagementView extends VerticalLayout implements View {

	private final Logger logger = LoggerFactory.getLogger(ProductManagementView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "manpowercost.management.";
	private Table manPowerCostTable = null;
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_registration_date","g_validity_end_date"};
    private ProductionManagementService productionManagementService;
    private CommonExceptionErrorNotification commonExceptionErrorNotification;
    private boolean enableRegisterManPowerCost;
    private Label isValidLabel = null;
    private Button registerManPowerCost = null;
	public ManPowerCostManagementView() {
		try{
			logger.info("\n ManPowerCostManagementView()...");
			this.initServices();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        this.buildManPowerCostTable();
	        
	        addComponent(this.buildToolbar());//toolbar: buildFilter() requiere tabla inicializada
	        addComponent(manPowerCostTable);
	        setExpandRatio(manPowerCostTable, 1);
	        this.isValidLabel = new Label(this.messages.get("application.common.validity.end.date.is.valid.value"));
	        this.isValidLabel.addStyleName("colored");
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	public ManPowerCostManagementView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
	
	private void initServices() throws Exception{
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
	}
	
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	 private void buildManPowerCostTable() throws PmsServiceException{
	    	this.manPowerCostTable = new Table();
	    	BeanItemContainer<ManPowerCostDTO> manPowerCostDTOBeanItemContainer	= new BeanItemContainer<ManPowerCostDTO>(ManPowerCostDTO.class);
	    	manPowerCostDTOBeanItemContainer.addAll(this.productionManagementService.listManPowerCostDTO(null));
	    	this.manPowerCostTable.setContainerDataSource(manPowerCostDTOBeanItemContainer);
	    	this.manPowerCostTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					// TODO Auto-generated method stub
					final ManPowerCostDTO vManPowerCostDTO = (ManPowerCostDTO)itemId;
					final Button editButton = new Button();
					editButton.setDescription(messages.get(VIEW_NAME + "table.manpowercost.column.operations.button.description"));
					editButton.setIcon(FontAwesome.EDIT);
					editButton.addStyleName("borderless");
					editButton.addClickListener(new ClickListener() {
			            @Override
			            public void buttonClick(final ClickEvent event) {
			                logger.info("\n editar ManPowerCostDTO...\n" + vManPowerCostDTO.toString());
			                try{		                	
			                	editManPowerCost(vManPowerCostDTO, false);
			                }catch(Exception e){
			                	commonExceptionErrorNotification.showErrorMessageNotification(e);
			                }			                
			            }
			        });
					editButton.setEnabled(vManPowerCostDTO.getValidity_end_date()==null);
					return editButton;
				}//public Object generateCell(Table source, Object itemId, Object columnId)
			});//manPowerCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
	    	this.manPowerCostTable.addGeneratedColumn("g_tariff_id", new Table.ColumnGenerator() {				
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					// TODO Auto-generated method stub
					final ManPowerCostDTO vManPowerCostDTO = (ManPowerCostDTO)itemId;					
					return vManPowerCostDTO.getTariffDTO().getTariff_id();
				}//public Object generateCell(Table source, Object itemId, Object columnId)
			});//manPowerCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
	    	this.manPowerCostTable.addGeneratedColumn("g_registration_date", new Table.ColumnGenerator() {				
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					// TODO Auto-generated method stub
					final ManPowerCostDTO vManPowerCostDTO = (ManPowerCostDTO)itemId;					
					return SgpUtils.dateFormater.format(vManPowerCostDTO.getRegistration_date());
				}//public Object generateCell(Table source, Object itemId, Object columnId)
			});//manPowerCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
	    	this.manPowerCostTable.addGeneratedColumn("g_validity_end_date", new Table.ColumnGenerator() {				
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					// TODO Auto-generated method stub
					final ManPowerCostDTO vManPowerCostDTO = (ManPowerCostDTO)itemId;
					return (vManPowerCostDTO.getValidity_end_date()!=null ? 
							SgpUtils.dateFormater.format(vManPowerCostDTO.getValidity_end_date()):isValidLabel);
				}//public Object generateCell(Table source, Object itemId, Object columnId)
			});//manPowerCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
	    	this.manPowerCostTable.setVisibleColumns(new Object[] {"operations","g_tariff_id","tariff_amount","g_registration_date","g_validity_end_date"});
	    	this.manPowerCostTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
	    	this.manPowerCostTable.setColumnAlignment("operations", Table.Align.LEFT);
	    	
	    	this.manPowerCostTable.setColumnHeader("g_tariff_id", this.messages.get(this.VIEW_NAME + "table.manpowercost.column.tariff.label"));
	    	this.manPowerCostTable.setColumnAlignment("tariff_amount", Table.Align.LEFT);
	    	
	    	this.manPowerCostTable.setColumnHeader("tariff_amount", this.messages.get(this.VIEW_NAME + "table.manpowercost.column.tariffamount.label"));
	    	this.manPowerCostTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
	    	
	    	this.manPowerCostTable.setColumnHeader("g_registration_date", this.messages.get(this.VIEW_NAME + "table.manpowercost.column.registration.date.label"));
	    	this.manPowerCostTable.setColumnAlignment("g_validity_end_date", Table.Align.LEFT);
	    	
	    	this.manPowerCostTable.setColumnHeader("g_validity_end_date", this.messages.get(this.VIEW_NAME + "table.manpowercost.column.validity.end.date.label"));
	    	this.manPowerCostTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
	    	this.manPowerCostTable.setSizeFull();
	    	this.manPowerCostTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
	    	this.manPowerCostTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
	    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
	    	this.manPowerCostTable.addStyleName(ValoTheme.TABLE_SMALL);    
	    	this.manPowerCostTable.setColumnExpandRatio("operations", 0.009f); 
	    	this.manPowerCostTable.setColumnExpandRatio("g_tariff_id", 0.015f);    	
	    	this.manPowerCostTable.setColumnExpandRatio("tariff_amount", 0.015f);
	    	this.manPowerCostTable.setColumnExpandRatio("g_registration_date", 0.025f);
	    	this.manPowerCostTable.setColumnExpandRatio("g_validity_end_date", 0.025f);
	    	this.manPowerCostTable.setSelectable(true);
	    	this.manPowerCostTable.setColumnCollapsingAllowed(true);
	    	this.manPowerCostTable.setColumnCollapsible("operations", false);
	    	this.manPowerCostTable.setColumnCollapsible("g_registration_date", true);
	    	this.manPowerCostTable.setColumnCollapsible("g_validity_end_date", true);
	    	//this.rolesTable.setColumnCollapsible("role_description", false);
	    	//this.manPowerCostTable.setSortContainerPropertyId("measurment_unit_id");
	    	//this.manPowerCostTable.setSortAscending(false);
	    	this.manPowerCostTable.setColumnReorderingAllowed(true);
	    	this.manPowerCostTable.setFooterVisible(true);
	    	this.manPowerCostTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	 }//private void buildMachinesTable(){
	 
	 private void editManPowerCost(ManPowerCostDTO vManPowerCostDTO,  boolean massiveInsertMode){
		try{
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.REGISTER_MAN_POWER_COST_FORM.getViewName());
			DashboardEventBus.post(new ManPowerCostRegisterFormViewEvent(vManPowerCostDTO, DashboardViewType.MAN_POWER_COST_MANAGEMENT.getViewName(), massiveInsertMode));
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
	        
	        HorizontalLayout tools = new HorizontalLayout(this.buildFilter(),this.createRegisterManPowerCostButton());	        	        
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
	                Filterable data = (Filterable) manPowerCostTable.getContainerDataSource();
	                data.removeAllContainerFilters();
	                data.addContainerFilter(new Filter() {
	                    @Override
	                    public boolean passesFilter(final Object itemId,final Item item) {
	                        if (event.getText() == null || event.getText().equals("")) {
	                            return true;
	                        }

	                        return filterByProperty("tariffDTO.tariff_id", item, event.getText()) || filterByProperty("g_validity_end_date", item, event.getText());
	                    }
	                    @Override
	                    public boolean appliesToProperty(final Object propertyId) {
	                        if (propertyId.equals("tariffDTO.tariff_id") || propertyId.equals("g_validity_end_date")) {
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
	             ((Filterable) manPowerCostTable.getContainerDataSource()).removeAllContainerFilters();
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

	 private Button createRegisterManPowerCostButton(){
	        //final Button registerManPowerCost = new Button(this.messages.get(this.VIEW_NAME + "button.register.manpowercost"));
		 	this.registerManPowerCost = new Button(this.messages.get(this.VIEW_NAME + "button.register.manpowercost"));
		 	this.registerManPowerCost.setDescription(this.messages.get(this.VIEW_NAME + "button.register.manpowercost.description"));
		 	this.registerManPowerCost.addClickListener(new ClickListener() {
	            @Override
	            public void buttonClick(final ClickEvent event) {
	            	editManPowerCost(null, true);
	            }
	        });
		 	this.updateEnableRegisterManPowerCostButton();
	        return this.registerManPowerCost;
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
	    	   this.manPowerCostTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.manPowerCostTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
	
	@Subscribe
	public void returnFromManPowerCostRegisterFormView(final ManPowerCostRegisterFormViewEvent manPowerCostRegisterFormViewEvent){
		this.updateEnableRegisterManPowerCostButton();
	}
	
	private void updateEnableRegisterManPowerCostButton(){
		try{
			this.setEnableRegisterManPowerCostFlag();
			logger.info("\n==========================================================="
					+"\nupdating register man power cost button with enabled value: "+this.enableRegisterManPowerCost
					+"\n===========================================================");
			this.registerManPowerCost.setEnabled(this.enableRegisterManPowerCost);
			this.registerManPowerCost.markAsDirty();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("\nerror",e);
		}
	}
	
	 private void setEnableRegisterManPowerCostFlag() throws PmsServiceException{
		 Long id = this.productionManagementService.getManPowerCostValidRowId();
		 if(id != null)this.enableRegisterManPowerCost = false; else this.enableRegisterManPowerCost = true;
	 }

}
