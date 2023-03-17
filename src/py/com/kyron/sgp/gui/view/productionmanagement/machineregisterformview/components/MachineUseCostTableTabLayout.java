package py.com.kyron.sgp.gui.view.productionmanagement.machineregisterformview.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineUseCostDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialCostDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.productionmanagement.MachineRegisterFormView;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
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
public class MachineUseCostTableTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(MachineUseCostTableTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "machine.register.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private Table machineUseCostTable = null;
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_registration_date","g_validity_end_date"};
    private ProductionManagementService productionManagementService;
    private BussinesSessionUtils bussinesSessionUtils;
    private CommonExceptionErrorNotification commonExceptionErrorNotification;
    private Label isValidLabel = null;
    private List<MachineUseCostDTO> listMachineUseCostDTO = null;
    private MachineDTO machineDTO;
    private MachineRegisterFormView machineRegisterFormView;
    
	public MachineUseCostTableTabLayout(MachineRegisterFormView machineRegisterFormView, MachineDTO machineDTO) {
		try{			
			logger.info("\n MachineUseCostTableTabLayout()...");
			this.machineRegisterFormView = machineRegisterFormView;
			this.machineDTO = machineDTO;
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
	        setSizeFull();
	        addStyleName("transactions");
	        this.setId("MachineUseCostTableTabLayout");
			this.setSpacing(true);
			this.setMargin(true);
	        Responsive.makeResponsive(this);
	        this.buildMachineUseCostTable();
	        addComponent(machineUseCostTable);
	        setExpandRatio(machineUseCostTable, 1);
	        this.isValidLabel = new Label(this.messages.get("application.common.validity.end.date.is.valid.value"));
	        this.isValidLabel.addStyleName("colored");
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	public MachineUseCostTableTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	private void initServices() throws Exception{
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	

	private void buildMachineUseCostTable() throws PmsServiceException{
    	this.machineUseCostTable = new Table();
    	BeanItemContainer<MachineUseCostDTO> machineUseCostDTOBeanItemContainer	= new BeanItemContainer<MachineUseCostDTO>(MachineUseCostDTO.class);
    	this.updateListRawMaterialCostDTO();
    	machineUseCostDTOBeanItemContainer.addAll(this.listMachineUseCostDTO);    	
    	this.machineUseCostTable.setContainerDataSource(machineUseCostDTOBeanItemContainer); 
    	
    	this.machineUseCostTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final MachineUseCostDTO vMachineUseCostDTO = (MachineUseCostDTO)itemId;
				final Button editButton = new Button();
				editButton.setDescription(messages.get("application.common.table.column.operations.apply.validity.end.date.button.description"));
				editButton.setIcon(FontAwesome.EDIT);
				editButton.addStyleName("borderless");
				editButton.addClickListener(new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		                logger.info("\n editar MachineUseCostDTO...\n" + vMachineUseCostDTO.toString());
		                try{		                	
		                	machineRegisterFormView.editMachineUseCost(vMachineUseCostDTO);
		                }catch(Exception e){
		                	commonExceptionErrorNotification.showErrorMessageNotification(e);
		                }			                
		            }
		        });
				
				try {
					editButton.setEnabled(vMachineUseCostDTO.getValidity_end_date()==null
					&& bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
					.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "tab.machine.use.cost.register.form"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("\nerror",e);
				}
				return editButton;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.machineUseCostTable.addGeneratedColumn("g_tariff_id", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final MachineUseCostDTO vMachineUseCostDTO = (MachineUseCostDTO)itemId;					
				return vMachineUseCostDTO.getTariffDTO().getTariff_id();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.machineUseCostTable.addGeneratedColumn("g_registration_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final MachineUseCostDTO vMachineUseCostDTO = (MachineUseCostDTO)itemId;					
				return SgpUtils.dateFormater.format(vMachineUseCostDTO.getRegistration_date());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.machineUseCostTable.addGeneratedColumn("g_validity_end_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final MachineUseCostDTO vMachineUseCostDTO = (MachineUseCostDTO)itemId;
				return (vMachineUseCostDTO.getValidity_end_date()!=null ? 
						SgpUtils.dateFormater.format(vMachineUseCostDTO.getValidity_end_date()):isValidLabel);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.machineUseCostTable.setVisibleColumns(new Object[] {"operations","g_tariff_id","tariff_amount","g_registration_date","g_validity_end_date"});
    	this.machineUseCostTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.machineUseCostTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.machineUseCostTable.setColumnHeader("g_tariff_id", this.messages.get("application.common.table.column.tariff.label"));
    	this.machineUseCostTable.setColumnAlignment("tariff_amount", Table.Align.LEFT);
    	
    	this.machineUseCostTable.setColumnHeader("tariff_amount", this.messages.get("application.common.table.column.any.amount.label"));
    	this.machineUseCostTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
    	
    	this.machineUseCostTable.setColumnHeader("g_registration_date", this.messages.get("application.common.table.column.registration.date.label"));
    	this.machineUseCostTable.setColumnAlignment("g_validity_end_date", Table.Align.LEFT);
    	
    	this.machineUseCostTable.setColumnHeader("g_validity_end_date", this.messages.get("application.common.table.column.validity.end.date.label"));
    	this.machineUseCostTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
    	this.machineUseCostTable.setSizeFull();
    	this.machineUseCostTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.machineUseCostTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.machineUseCostTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.machineUseCostTable.setColumnExpandRatio("operations", 0.009f); 
    	this.machineUseCostTable.setColumnExpandRatio("g_tariff_id", 0.025f);    	
    	this.machineUseCostTable.setColumnExpandRatio("tariff_amount", 0.015f);
    	this.machineUseCostTable.setColumnExpandRatio("g_registration_date", 0.018f);
    	this.machineUseCostTable.setColumnExpandRatio("g_validity_end_date", 0.018f);
    	this.machineUseCostTable.setSelectable(true);
    	this.machineUseCostTable.setColumnCollapsingAllowed(true);
    	this.machineUseCostTable.setColumnCollapsible("operations", false);
    	this.machineUseCostTable.setColumnCollapsible("g_registration_date", true);
    	this.machineUseCostTable.setColumnCollapsible("g_validity_end_date", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.machineUseCostTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.machineUseCostTable.setSortAscending(false);
    	this.machineUseCostTable.setColumnReorderingAllowed(true);
    	this.machineUseCostTable.setFooterVisible(true);
    	this.machineUseCostTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
    private void updateListRawMaterialCostDTO() throws PmsServiceException{
    	if(this.machineDTO.getId()!=null)
    		this.listMachineUseCostDTO = this.productionManagementService.listMachineUseCostDTO(new MachineUseCostDTO(this.machineDTO.getId(), null));
    	if(this.listMachineUseCostDTO == null) this.listMachineUseCostDTO = new ArrayList<MachineUseCostDTO>();
    }
    
    
}
