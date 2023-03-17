package py.com.kyron.sgp.gui.view.productionmanagement.production.process.activity.instance.operations.view.components;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineRequirementDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityInstanceDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.bussines.stockmanagement.domain.PAIRawMaterialSupplyDTO;
import py.com.kyron.sgp.bussines.stockmanagement.service.StockManagementService;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class OperationRawMaterialSupplyTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(OperationRawMaterialSupplyTabLayout.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO;
	private final ProductionProcessActivityInstanceOperationsViewFunctions productionProcessActivityInstanceOperationsViewFunctions;
	private BussinesSessionUtils bussinesSessionUtils;
	private ProductionManagementService productionManagementService;
	private List<PAIRawMaterialSupplyDTO> listPAIRawMaterialSupplyDTO;
	private StockManagementService stockManagementService;
	private MachineRequirementDTO machineRequirementDTO;
	private Table listPAIRawMaterialSupplyDTOTable;
	private static final String[] DEFAULT_COLLAPSIBLE = {"measurment_unit_description","quantity"};
	
	public OperationRawMaterialSupplyTabLayout(
			final ProductionProcessActivityInstanceOperationsViewFunctions productionProcessActivityInstanceOperationsViewFunctions,
			final String VIEW_NAME,
			final ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO, final boolean setUpLayoutContent 
			) {
		// TODO Auto-generated constructor stub
		this.productionProcessActivityInstanceOperationsViewFunctions = productionProcessActivityInstanceOperationsViewFunctions;
		this.VIEW_NAME = VIEW_NAME;
		this.productionProcessActivityInstanceDTO = productionProcessActivityInstanceDTO;
		try{
			logger.info("\n OrderRawMaterialSufficiencyReportDTOTabLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();	
	        this.setSizeFull();
	        this.initServices();
	        //this.addStyleName("v-scrollable");
	        //this.setHeight("100%");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        if(setUpLayoutContent)this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	private void initServices() throws Exception{
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
		this.stockManagementService = (StockManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.STOCK_MANAGEMENT_SERVICE);
	}
	/*public OperationRawMaterialSupplyTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n OperationRawMaterialSupplyTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void setUpLayoutContent() throws PmsServiceException{
		this.retrieveListPAIRawMaterialSupplyDTO();
		this.retrieveMachineRequirementDTO();
		this.addComponent(this.setUpHeader());
		this.buildPAIRawMaterialSupplyDTOTable();
		this.addComponent(this.listPAIRawMaterialSupplyDTOTable);
		this.setExpandRatio(this.listPAIRawMaterialSupplyDTOTable, 1); 
		this.addComponent(this.setUpOkCancelButtons());
	}
	
	private void retrieveListPAIRawMaterialSupplyDTO() throws PmsServiceException{
		this.listPAIRawMaterialSupplyDTO = this.stockManagementService.listPAIRawMaterialSupplyDTO(new PAIRawMaterialSupplyDTO(null, this.productionProcessActivityInstanceDTO.getId()));
	}
	private void retrieveMachineRequirementDTO() throws PmsServiceException{
		this.machineRequirementDTO = 
				this.productionManagementService.listMachineRequirementDTO(
						new MachineRequirementDTO(
								this.productionManagementService.getMachineRequerimentByIdOrderIdProductIdProductionProcessActivity
								(productionProcessActivityInstanceDTO)
								)
				).get(0);
	}
	
	private HorizontalLayout setUpHeader(){

		VerticalLayout column01 = new VerticalLayout();
		/*VerticalLayout column02 = new VerticalLayout();
		VerticalLayout column03 = new VerticalLayout();*/
		
		column01.addComponent(this.setUpMainTitle());
		Label process_descriptionLabel = new Label(this.messages.get("application.common.process.label") + ":");
		process_descriptionLabel.addStyleName(ValoTheme.LABEL_COLORED);
		Label process_descriptionValue = new Label(this.listPAIRawMaterialSupplyDTO.get(0).getProcess_description());
		HorizontalLayout process_descriptionHorizontalLayout = new HorizontalLayout(process_descriptionLabel,process_descriptionValue);
		process_descriptionHorizontalLayout.setSpacing(true);
		column01.addComponent(process_descriptionHorizontalLayout);
		
		Label activity_descriptionLabel = new Label(this.messages.get("application.common.activity.label") + ":");
		activity_descriptionLabel.addStyleName(ValoTheme.LABEL_COLORED);
		Label activity_descriptionValue = new Label(this.listPAIRawMaterialSupplyDTO.get(0).getActivity_description());
		HorizontalLayout activity_descriptionHorizontalLayout = new HorizontalLayout(activity_descriptionLabel,activity_descriptionValue);
		activity_descriptionHorizontalLayout.setSpacing(true);
		column01.addComponent(activity_descriptionHorizontalLayout);
		
		Label machine_descriptionLabel = new Label(this.messages.get("application.common.machine.label") + ":");
		machine_descriptionLabel.addStyleName(ValoTheme.LABEL_COLORED);
		Label machine_descriptionValue = new Label(
		this.machineRequirementDTO.getMachineDTO().getMachine_id() + "; " +	this.machineRequirementDTO.getMachineDTO().getMachine_description());
		HorizontalLayout machine_descriptionHorizontalLayout = new HorizontalLayout(machine_descriptionLabel,machine_descriptionValue);
		machine_descriptionHorizontalLayout.setSpacing(true);
		column01.addComponent(machine_descriptionHorizontalLayout);
		
		Label status_Label = new Label(this.messages.get("application.common.status.label") + ":");
		status_Label.addStyleName(ValoTheme.LABEL_COLORED);
		Label status_Value = new Label(this.messages.get(this.productionProcessActivityInstanceDTO.getStatus()));
		HorizontalLayout statusHorizontalLayout = new HorizontalLayout(status_Label,status_Value);
		statusHorizontalLayout.setSpacing(true);
		column01.addComponent(statusHorizontalLayout);
		
		Label next_statusLabel = new Label(this.messages.get("application.common.next.status.label") + ":");
		next_statusLabel.addStyleName(ValoTheme.LABEL_COLORED);
		Label next_statusValue = new Label(this.messages.get(this.productionProcessActivityInstanceDTO.getNext_status()));
		HorizontalLayout next_statusHorizontalLayout = new HorizontalLayout(next_statusLabel,next_statusValue);
		next_statusHorizontalLayout.setSpacing(true);
		column01.addComponent(next_statusHorizontalLayout);
		
		HorizontalLayout header = new HorizontalLayout(column01);
		header.addStyleName("viewheader");
		header.setMargin(true);
		header.setSizeFull();
		header.setComponentAlignment(column01, Alignment.TOP_LEFT);
		return header;
	}
	
	private HorizontalLayout setUpMainTitle(){
        Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.operation.raw.material.supply.main.title"));
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        HorizontalLayout header = new HorizontalLayout(title);
        header.addComponent(title);
        header.setMargin(new MarginInfo(false, false, true, false));
        return header;
	}
	
	 private void buildPAIRawMaterialSupplyDTOTable() { 
	    	this.listPAIRawMaterialSupplyDTOTable = new Table();
	    	BeanItemContainer<PAIRawMaterialSupplyDTO> machineDTOBeanItemContainer	= new BeanItemContainer<PAIRawMaterialSupplyDTO>(PAIRawMaterialSupplyDTO.class);
	    	machineDTOBeanItemContainer.addAll(this.listPAIRawMaterialSupplyDTO);
	    	this.listPAIRawMaterialSupplyDTOTable.setContainerDataSource(machineDTOBeanItemContainer);
	    	this.listPAIRawMaterialSupplyDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					// TODO Auto-generated method stub
					final PAIRawMaterialSupplyDTO vPAIRawMaterialSupplyDTO = (PAIRawMaterialSupplyDTO)itemId;
					return buildOperationsButtonPanel(vPAIRawMaterialSupplyDTO);
				}//public Object generateCell(Table source, Object itemId, Object columnId)
			});//orderDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
	    	this.listPAIRawMaterialSupplyDTOTable.setVisibleColumns(new Object[] 
	    			{"operations","raw_material_description","measurment_unit_description","quantity"});
	    	this.listPAIRawMaterialSupplyDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
	    	this.listPAIRawMaterialSupplyDTOTable.setColumnAlignment("operations", Table.Align.LEFT);
	    	this.listPAIRawMaterialSupplyDTOTable.setColumnHeader("raw_material_description", this.messages.get("application.common.rawmaterialid.label"));
	    	this.listPAIRawMaterialSupplyDTOTable.setColumnAlignment("raw_material_description", Table.Align.LEFT);
	    	this.listPAIRawMaterialSupplyDTOTable.setColumnHeader("measurment_unit_description", this.messages.get("application.common.measurmentunitid.label"));
	    	this.listPAIRawMaterialSupplyDTOTable.setColumnAlignment("measurment_unit_description", Table.Align.RIGHT);
	    	this.listPAIRawMaterialSupplyDTOTable.setColumnHeader("quantity", this.messages.get("application.common.quantity.label"));
	    	this.listPAIRawMaterialSupplyDTOTable.setColumnAlignment("quantity", Table.Align.RIGHT);
	    	this.listPAIRawMaterialSupplyDTOTable.setSizeFull();
	    	this.listPAIRawMaterialSupplyDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
	    	this.listPAIRawMaterialSupplyDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
	    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
	    	this.listPAIRawMaterialSupplyDTOTable.addStyleName(ValoTheme.TABLE_SMALL);
	    	this.listPAIRawMaterialSupplyDTOTable.setColumnExpandRatio("operations", 0.0101f);
	    	this.listPAIRawMaterialSupplyDTOTable.setColumnExpandRatio("raw_material_description", 0.09f);
	    	this.listPAIRawMaterialSupplyDTOTable.setColumnExpandRatio("measurment_unit_description", 0.05f);
	    	this.listPAIRawMaterialSupplyDTOTable.setColumnExpandRatio("quantity", 0.01f);
	    	this.listPAIRawMaterialSupplyDTOTable.setSelectable(true);
	    	this.listPAIRawMaterialSupplyDTOTable.setColumnCollapsingAllowed(true);
	    	this.listPAIRawMaterialSupplyDTOTable.setColumnCollapsible("raw_material_description", false);
	    	//this.listPAIRawMaterialSupplyDTOTable.setSortContainerPropertyId("product_description");
	    	this.listPAIRawMaterialSupplyDTOTable.setSortAscending(false);
	    	this.listPAIRawMaterialSupplyDTOTable.setColumnReorderingAllowed(true);
	    	this.listPAIRawMaterialSupplyDTOTable.setFooterVisible(true);
	    	this.listPAIRawMaterialSupplyDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	    	this.listPAIRawMaterialSupplyDTOTable.setHeight(150f + (this.listPAIRawMaterialSupplyDTO.size() * 25), Unit.PIXELS);
	 }//private void buildMachinesTable(){
		
	 
	 @Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    // Some columns are collapsed when browser window width gets small
	    // enough to make the table fit better.
	    if (defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.listPAIRawMaterialSupplyDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
		 
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.listPAIRawMaterialSupplyDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
	
    private HorizontalLayout setUpOkCancelButtons(){

		final Button okButton = new Button(this.messages.get("application.common.button.save.label"));/*"Cancelar"*/
		okButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		productionProcessActivityInstanceOperationsViewFunctions.effectuateRawMaterialDeparture();
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		okButton.setEnabled(!this.productionProcessActivityInstanceDTO.getStatus().equals("application.common.status.in.progress"));
		final Button cancelButton = new Button(this.messages.get("application.common.button.cancel.label"));/*"Cancelar"*/
		cancelButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		productionProcessActivityInstanceOperationsViewFunctions.navigateToCallerView();
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);

		final HorizontalLayout okCancelHorizontalLayout = new HorizontalLayout();
		okCancelHorizontalLayout.setMargin(true);
		okCancelHorizontalLayout.setSpacing(true);
		okCancelHorizontalLayout.setSizeFull();
		okCancelHorizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		final HorizontalLayout wrapperHorizontalLayout = new HorizontalLayout();
		wrapperHorizontalLayout.setSpacing(true);
		wrapperHorizontalLayout.addComponent(okButton);
		wrapperHorizontalLayout.addComponent(cancelButton);
		okCancelHorizontalLayout.addComponent(wrapperHorizontalLayout);
		return okCancelHorizontalLayout;
    }
    
    private HorizontalLayout buildOperationsButtonPanel(final PAIRawMaterialSupplyDTO vPAIRawMaterialSupplyDTO){
		final Button queryListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTOButton = new Button();
		queryListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTOButton.setIcon(FontAwesome.SEARCH);
		queryListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTOButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.operation.raw.material.supply.table.pai.raw.material.supply.column.operations.button.query.purchase.invoice.affected.description"));
		queryListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTOButton.addStyleName("borderless");
		queryListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTOButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n query vPAIRawMaterialSupplyDTO...\n" + vPAIRawMaterialSupplyDTO.toString());
                try{		                	
                	productionProcessActivityInstanceOperationsViewFunctions.queryListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO(vPAIRawMaterialSupplyDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		queryListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTOButton.setVisible(
				vPAIRawMaterialSupplyDTO.getListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO() != null
				&& !vPAIRawMaterialSupplyDTO.getListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO().isEmpty());
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(queryListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTOButton);
		//operationsButtonPanel.addStyleName("wrapping");
		return operationsButtonPanel;
    }
}
