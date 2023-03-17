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
import py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialExistenceDTO;
import py.com.kyron.sgp.bussines.stockmanagement.service.StockManagementService;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.stockmanagement.RawMaterialStockManagementFormView;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class RawMaterialExistenceDTOTableTabLayout extends VerticalLayout {
	
	private final Logger logger = LoggerFactory.getLogger(RawMaterialExistenceDTOTableTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "rawmaterial.stock.management.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	final private RawMaterialDTO rawMaterialDTO;
	private List<RawMaterialExistenceDTO> listRawMaterialExistenceDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	final private RawMaterialStockManagementFormView rawMaterialStockManagementFormView;
	private Table rawMaterialExistenceDTOTable = null;
    private static final String[] DEFAULT_COLLAPSIBLE = {"operations"};
    private StockManagementService stockManagementService;
    private BussinesSessionUtils bussinesSessionUtils;
	
	public RawMaterialExistenceDTOTableTabLayout(RawMaterialStockManagementFormView vRawMaterialStockManagementFormView, RawMaterialDTO rawMaterialDTO) {
		// TODO Auto-generated constructor stub
		this.rawMaterialStockManagementFormView = vRawMaterialStockManagementFormView;
		this.rawMaterialDTO = rawMaterialDTO;
		try{
			logger.info("\nRawMaterialExistenceDTOTableTabLayout..");
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

	/*public RawMaterialExistenceDTOTableTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	
	private void initServices(){
		this.stockManagementService = (StockManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.STOCK_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	private void setUpLayoutContent() throws PmsServiceException{
		this.buildRawMaterialExistenceDTOTable();
        addComponent(rawMaterialExistenceDTOTable);
        setExpandRatio(rawMaterialExistenceDTOTable, 1);
	}

	
	private void buildRawMaterialExistenceDTOTable() throws PmsServiceException{
    	this.rawMaterialExistenceDTOTable = new Table();
    	BeanItemContainer<RawMaterialExistenceDTO> rawMaterialExistenceDTOBeanItemContainer	= new BeanItemContainer<RawMaterialExistenceDTO>(RawMaterialExistenceDTO.class);
    	this.updateListRawMaterialExistenceDTO();
    	rawMaterialExistenceDTOBeanItemContainer.addAll(this.listRawMaterialExistenceDTO);    	
    	this.rawMaterialExistenceDTOTable.setContainerDataSource(rawMaterialExistenceDTOBeanItemContainer); 
    	
    	/*this.rawMaterialExistenceDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final RawMaterialExistenceDTO vRawMaterialExistenceDTO = (RawMaterialExistenceDTO)itemId;
				final Button editButton = new Button();
				editButton.setDescription(messages.get(VIEW_NAME + "tab.rawmaterialexistence.table.column.operations.edit.parameters"));
				editButton.setIcon(FontAwesome.EDIT);
				editButton.addStyleName("borderless");
				editButton.addClickListener(new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		                logger.info("\n editar RawMaterialExistenceDTO...\n" + vRawMaterialExistenceDTO.toString());
		                try{		                	
		                	rawMaterialStockManagementFormView.editRawMaterialExistenceDTO(vRawMaterialExistenceDTO);
		                }catch(Exception e){
		                	commonExceptionErrorNotification.showErrorMessageNotification(e);
		                }			                
		            }
		        });
				
				try {
					editButton.setEnabled(bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
					.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "tab.rawmaterialexistence.form"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("\nerror",e);
				}
				return editButton;
			}
		});*/
    	this.rawMaterialExistenceDTOTable.addGeneratedColumn("g_measurment_unit_description", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final RawMaterialExistenceDTO vRawMaterialExistenceDTO = (RawMaterialExistenceDTO)itemId;					
				return vRawMaterialExistenceDTO.getMeasurmentUnitDTO().getMeasurment_unit_id();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//rawMaterialExistenceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.rawMaterialExistenceDTOTable.setVisibleColumns(new Object[] {/*"operations",*/"g_measurment_unit_description","calculated_quantity","efective_quantity"});
    	/*this.rawMaterialExistenceDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.rawMaterialExistenceDTOTable.setColumnAlignment("operations", Table.Align.LEFT);*/
    	
    	this.rawMaterialExistenceDTOTable.setColumnHeader("g_measurment_unit_description", this.messages.get("application.common.measurmentunitid.label"));
    	this.rawMaterialExistenceDTOTable.setColumnAlignment("g_measurment_unit_description", Table.Align.LEFT);
    	
    	this.rawMaterialExistenceDTOTable.setColumnHeader("calculated_quantity", this.messages.get(this.VIEW_NAME + "tab.rawmaterialexistence.table.column.calculated.quantity.label"));
    	this.rawMaterialExistenceDTOTable.setColumnAlignment("calculated_quantity", Table.Align.RIGHT);
    	
    	this.rawMaterialExistenceDTOTable.setColumnHeader("efective_quantity", this.messages.get(this.VIEW_NAME + "tab.rawmaterialexistence.table.column.efective.quantity.label"));
    	this.rawMaterialExistenceDTOTable.setColumnAlignment("efective_quantity", Table.Align.RIGHT);

    	
    	this.rawMaterialExistenceDTOTable.setSizeFull();
    	this.rawMaterialExistenceDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.rawMaterialExistenceDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.rawMaterialExistenceDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	/*this.rawMaterialExistenceDTOTable.setColumnExpandRatio("operations", 0.009f); */
    	this.rawMaterialExistenceDTOTable.setColumnExpandRatio("g_measurment_unit_description", 0.019f);
    	this.rawMaterialExistenceDTOTable.setColumnExpandRatio("calculated_quantity", 0.011f);
    	this.rawMaterialExistenceDTOTable.setColumnExpandRatio("efective_quantity", 0.011f);
    	
    	this.rawMaterialExistenceDTOTable.setSelectable(true);
    	this.rawMaterialExistenceDTOTable.setColumnCollapsingAllowed(true);
    	/*this.rawMaterialExistenceDTOTable.setColumnCollapsible("operations", true);*/
    	this.rawMaterialExistenceDTOTable.setColumnCollapsible("g_measurment_unit_description", false);
    	this.rawMaterialExistenceDTOTable.setColumnCollapsible("calculated_quantity", true);
    	this.rawMaterialExistenceDTOTable.setColumnCollapsible("efective_quantity", true);
    	
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.rawMaterialExistenceDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.rawMaterialExistenceDTOTable.setSortAscending(false);
    	this.rawMaterialExistenceDTOTable.setColumnReorderingAllowed(true);
    	this.rawMaterialExistenceDTOTable.setFooterVisible(true);
    	this.rawMaterialExistenceDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
    private void updateListRawMaterialExistenceDTO() throws PmsServiceException{
    	this.listRawMaterialExistenceDTO = this.stockManagementService.listRawMaterialExistenceDTO(new RawMaterialExistenceDTO(this.rawMaterialDTO.getId(), null));
    	if(this.listRawMaterialExistenceDTO == null) this.listRawMaterialExistenceDTO = new ArrayList<RawMaterialExistenceDTO>();
    }
    
	 @Subscribe
	 public void browserResized(final BrowserResizeEvent event) {
	     // Some columns are collapsed when browser window width gets small
	     // enough to make the table fit better.
	     if (defaultColumnsVisible()) {
	        for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        	rawMaterialExistenceDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	        }
	     }
	 }//public void browserResized(final BrowserResizeEvent event)
	 
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (rawMaterialExistenceDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
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
