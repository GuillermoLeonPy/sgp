package py.com.kyron.sgp.gui.view.stockmanagement.rawmaterialstockmanagementformview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialPurchaseRequestDTO;
import py.com.kyron.sgp.bussines.stockmanagement.service.StockManagementService;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.stockmanagement.RawMaterialStockManagementFormView;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class RawMaterialPurchaseRequestDTOTableTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(RawMaterialPurchaseRequestDTOTableTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "rawmaterial.stock.management.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	final private RawMaterialDTO rawMaterialDTO;
	private List<RawMaterialPurchaseRequestDTO> listRawMaterialPurchaseRequestDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	final private RawMaterialStockManagementFormView rawMaterialStockManagementFormView;
	private Table rawMaterialPurchaseRequestDTOTable = null;
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_registration_date","g_supplier_data"};
    private StockManagementService stockManagementService;
    private BussinesSessionUtils bussinesSessionUtils;
	
	public RawMaterialPurchaseRequestDTOTableTabLayout(RawMaterialStockManagementFormView rawMaterialStockManagementFormView,
			RawMaterialDTO rawMaterialDTO) {
		// TODO Auto-generated constructor stub
		this.rawMaterialStockManagementFormView = rawMaterialStockManagementFormView;
		this.rawMaterialDTO = rawMaterialDTO;
		try{
			logger.info("\n RawMaterialPurchaseRequestDTOTableTabLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
	        this.setSpacing(true);
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public RawMaterialPurchaseRequestDTOTableTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	
	private void initServices(){
		this.stockManagementService = (StockManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.STOCK_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	private void setUpLayoutContent() throws PmsServiceException{
		this.buildrawMaterialPurchaseRequestDTOTable();
        addComponent(rawMaterialPurchaseRequestDTOTable);
        setExpandRatio(rawMaterialPurchaseRequestDTOTable, 1);
	}

	
	private void buildrawMaterialPurchaseRequestDTOTable() throws PmsServiceException{
    	this.rawMaterialPurchaseRequestDTOTable = new Table();
    	BeanItemContainer<RawMaterialPurchaseRequestDTO> RawMaterialPurchaseRequestDTOBeanItemContainer	= new BeanItemContainer<RawMaterialPurchaseRequestDTO>(RawMaterialPurchaseRequestDTO.class);
    	this.updateListRawMaterialPurchaseRequestDTO();
    	RawMaterialPurchaseRequestDTOBeanItemContainer.addAll(this.listRawMaterialPurchaseRequestDTO);    	
    	this.rawMaterialPurchaseRequestDTOTable.setContainerDataSource(RawMaterialPurchaseRequestDTOBeanItemContainer); 
    	
    	this.rawMaterialPurchaseRequestDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final RawMaterialPurchaseRequestDTO vRawMaterialPurchaseRequestDTO = (RawMaterialPurchaseRequestDTO)itemId;
				final Button editButton = new Button();
				editButton.setDescription(messages.get(VIEW_NAME + "tab.rawmaterial.purchase.request.table.column.operations.edit"));
				editButton.setIcon(FontAwesome.EDIT);
				editButton.addStyleName("borderless");
				editButton.addClickListener(new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		                logger.info("\n editar RawMaterialPurchaseRequestDTO...\n" + vRawMaterialPurchaseRequestDTO.toString());
		                try{		                	
		                	rawMaterialStockManagementFormView.editRawMaterialPurchaseRequestDTO(vRawMaterialPurchaseRequestDTO);
		                }catch(Exception e){
		                	commonExceptionErrorNotification.showErrorMessageNotification(e);
		                }			                
		            }
		        });
				
				try {
					editButton.setEnabled(vRawMaterialPurchaseRequestDTO.getStatus().equals("application.common.status.pending")
					&& bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
							.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "tab.rawmaterial.purchase.request.form"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("\nerror",e);
				}
				return editButton;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//rawMaterialPurchaseRequestDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.rawMaterialPurchaseRequestDTOTable.addGeneratedColumn("g_measurment_unit_description", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final RawMaterialPurchaseRequestDTO vRawMaterialPurchaseRequestDTO = (RawMaterialPurchaseRequestDTO)itemId;					
				return vRawMaterialPurchaseRequestDTO.getMeasurmentUnitDTO().getMeasurment_unit_id();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//rawMaterialPurchaseRequestDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.rawMaterialPurchaseRequestDTOTable.addGeneratedColumn("g_supplier_data", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final RawMaterialPurchaseRequestDTO vRawMaterialPurchaseRequestDTO = (RawMaterialPurchaseRequestDTO)itemId;					
				return vRawMaterialPurchaseRequestDTO.getPersonDTO().getCommercial_name();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//rawMaterialPurchaseRequestDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.rawMaterialPurchaseRequestDTOTable.addGeneratedColumn("g_registration_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final RawMaterialPurchaseRequestDTO vRawMaterialPurchaseRequestDTO = (RawMaterialPurchaseRequestDTO)itemId;
				return SgpUtils.dateFormater.format(vRawMaterialPurchaseRequestDTO.getRegistration_date());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.rawMaterialPurchaseRequestDTOTable.addGeneratedColumn("g_status", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final RawMaterialPurchaseRequestDTO vRawMaterialPurchaseRequestDTO = (RawMaterialPurchaseRequestDTO)itemId;	
				final Label statusLabel = new Label(messages.get(vRawMaterialPurchaseRequestDTO.getStatus()));
				statusLabel.addStyleName("colored");
				return statusLabel;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//rawMaterialPurchaseRequestDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.rawMaterialPurchaseRequestDTOTable.setVisibleColumns(new Object[] {"operations","g_measurment_unit_description","quantity","g_supplier_data","g_registration_date","g_status"});
    	this.rawMaterialPurchaseRequestDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.rawMaterialPurchaseRequestDTOTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.rawMaterialPurchaseRequestDTOTable.setColumnHeader("g_measurment_unit_description", this.messages.get("application.common.measurmentunitid.label"));
    	this.rawMaterialPurchaseRequestDTOTable.setColumnAlignment("g_measurment_unit_description", Table.Align.LEFT);
    	
    	this.rawMaterialPurchaseRequestDTOTable.setColumnHeader("quantity", this.messages.get("application.common.quantity.label"));
    	this.rawMaterialPurchaseRequestDTOTable.setColumnAlignment("quantity", Table.Align.RIGHT);
    	
    	this.rawMaterialPurchaseRequestDTOTable.setColumnHeader("g_supplier_data", this.messages.get("application.common.supplier.label"));
    	this.rawMaterialPurchaseRequestDTOTable.setColumnAlignment("g_supplier_data", Table.Align.LEFT);
    	
    	this.rawMaterialPurchaseRequestDTOTable.setColumnHeader("g_registration_date", this.messages.get("application.common.table.column.registration.date.label"));
    	this.rawMaterialPurchaseRequestDTOTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
  	
    	this.rawMaterialPurchaseRequestDTOTable.setColumnHeader("g_status", this.messages.get("application.common.status.label"));
    	this.rawMaterialPurchaseRequestDTOTable.setColumnAlignment("g_status", Table.Align.LEFT);
    	
    	this.rawMaterialPurchaseRequestDTOTable.setSizeFull();
    	this.rawMaterialPurchaseRequestDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.rawMaterialPurchaseRequestDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.rawMaterialPurchaseRequestDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.rawMaterialPurchaseRequestDTOTable.setColumnExpandRatio("operations", 0.012f); 
    	this.rawMaterialPurchaseRequestDTOTable.setColumnExpandRatio("g_measurment_unit_description", 0.019f);    	
    	this.rawMaterialPurchaseRequestDTOTable.setColumnExpandRatio("quantity", 0.011f);
    	this.rawMaterialPurchaseRequestDTOTable.setColumnExpandRatio("g_supplier_data", 0.025f);
    	this.rawMaterialPurchaseRequestDTOTable.setColumnExpandRatio("g_registration_date", 0.016f);
    	this.rawMaterialPurchaseRequestDTOTable.setColumnExpandRatio("g_status", 0.009f);
    	this.rawMaterialPurchaseRequestDTOTable.setSelectable(true);
    	this.rawMaterialPurchaseRequestDTOTable.setColumnCollapsingAllowed(true);
    	this.rawMaterialPurchaseRequestDTOTable.setColumnCollapsible("operations", false);
    	this.rawMaterialPurchaseRequestDTOTable.setColumnCollapsible("g_supplier_data", true);
    	this.rawMaterialPurchaseRequestDTOTable.setColumnCollapsible("g_registration_date", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.rawMaterialPurchaseRequestDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.rawMaterialPurchaseRequestDTOTable.setSortAscending(false);
    	this.rawMaterialPurchaseRequestDTOTable.setColumnReorderingAllowed(true);
    	this.rawMaterialPurchaseRequestDTOTable.setFooterVisible(true);
    	this.rawMaterialPurchaseRequestDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
    private void updateListRawMaterialPurchaseRequestDTO() throws PmsServiceException{
    	this.listRawMaterialPurchaseRequestDTO = this.stockManagementService.listRawMaterialPurchaseRequestDTO(new RawMaterialPurchaseRequestDTO(this.rawMaterialDTO.getId(), null,null));
    	if(this.listRawMaterialPurchaseRequestDTO == null) this.listRawMaterialPurchaseRequestDTO = new ArrayList<RawMaterialPurchaseRequestDTO>();
    }
    
	 @Subscribe
	 public void browserResized(final BrowserResizeEvent event) {
	     // Some columns are collapsed when browser window width gets small
	     // enough to make the table fit better.
	     if (defaultColumnsVisible()) {
	        for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        	rawMaterialPurchaseRequestDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	        }
	     }
	 }//public void browserResized(final BrowserResizeEvent event)
	 
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (rawMaterialPurchaseRequestDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
	
	@Override
	public void detach() {
	    super.detach();
	    // A new instance of TransactionsView is created every time it's
	    // navigated to so we'll need to clean up references to it on detach.
	    DashboardEventBus.unregister(this);
	}//public void detach()

}
