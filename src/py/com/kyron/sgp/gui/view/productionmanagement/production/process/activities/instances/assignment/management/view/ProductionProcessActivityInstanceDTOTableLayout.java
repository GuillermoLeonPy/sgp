package py.com.kyron.sgp.gui.view.productionmanagement.production.process.activities.instances.assignment.management.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityInstanceDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.utils.TableNumericColumnCellLabelHelper;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ProductionProcessActivityInstanceDTOTableLayout extends
		VerticalLayout implements SearchProductionProcessActivityInstanceDTOComponentHostViewFunctions{

	private final Logger logger = LoggerFactory.getLogger(ProductionProcessActivityInstanceDTOTableLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.activities.instances.assignment.management.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Table productionProcessActivityInstanceDTOTable;
	private static final String[] DEFAULT_COLLAPSIBLE = {"g_require_parcial_product_recall",
			"g_activity_id","g_assignment_date","g_person","g_next_status","g_activity_start_work_date","g_activity_finish_work_date","g_delivers_partial_result"};

    private final ProductionProcessActivityInstanceDTOTableFunctions productionProcessActivityInstanceDTOTableFunctions;
    private BussinesSessionUtils bussinesSessionUtils;
    private SearchProductionProcessActivityInstanceDTOComponent searchProductionProcessActivityInstanceDTOComponent;
    
	public ProductionProcessActivityInstanceDTOTableLayout(
			final ProductionProcessActivityInstanceDTOTableFunctions productionProcessActivityInstanceDTOTableFunctions) {
		// TODO Auto-generated constructor stub
		this.productionProcessActivityInstanceDTOTableFunctions = productionProcessActivityInstanceDTOTableFunctions;
		try{
			logger.info("\n ProductionProcessActivityInstanceDTOTableLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        this.searchProductionProcessActivityInstanceDTOComponent = new SearchProductionProcessActivityInstanceDTOComponent(this,this.VIEW_NAME);
	        this.addComponent(this.searchProductionProcessActivityInstanceDTOComponent);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public ProductionProcessActivityInstanceDTOTableLayout(
			Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	private void initServices(){
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	
	@Override
	public void buildProductionProcessActivityInstanceDTOTable(List<ProductionProcessActivityInstanceDTO> listProductionProcessActivityInstanceDTO){
		if(this.productionProcessActivityInstanceDTOTable!=null)this.removeComponent(this.productionProcessActivityInstanceDTOTable);
		if(listProductionProcessActivityInstanceDTO == null)listProductionProcessActivityInstanceDTO = new ArrayList<ProductionProcessActivityInstanceDTO>();
    	this.productionProcessActivityInstanceDTOTable = new Table();
    	BeanItemContainer<ProductionProcessActivityInstanceDTO> ProductionProcessActivityInstanceDTOBeanItemContainer	= new BeanItemContainer<ProductionProcessActivityInstanceDTO>(ProductionProcessActivityInstanceDTO.class);
    	ProductionProcessActivityInstanceDTOBeanItemContainer.addAll(listProductionProcessActivityInstanceDTO);    	
    	this.productionProcessActivityInstanceDTOTable.setContainerDataSource(ProductionProcessActivityInstanceDTOBeanItemContainer); 
    	
    	this.productionProcessActivityInstanceDTOTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessActivityInstanceDTO vProductionProcessActivityInstanceDTO = (ProductionProcessActivityInstanceDTO)itemId;
				return buildOperationsButtonPanel(vProductionProcessActivityInstanceDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//productionProcessActivityInstanceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessActivityInstanceDTOTable.addGeneratedColumn("g_product_instance_unique_number", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessActivityInstanceDTO vProductionProcessActivityInstanceDTO = (ProductionProcessActivityInstanceDTO)itemId;
				final Label label = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vProductionProcessActivityInstanceDTO.getProduct_instance_unique_number());
    			label.addStyleName("colored");
    			final HorizontalLayout vHorizontalLayout = new HorizontalLayout(label);
				vHorizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//productionProcessActivityInstanceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessActivityInstanceDTOTable.addGeneratedColumn("g_activity_instance_unique_number", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessActivityInstanceDTO vProductionProcessActivityInstanceDTO = (ProductionProcessActivityInstanceDTO)itemId;
				final Label label = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vProductionProcessActivityInstanceDTO.getActivity_instance_unique_number());
    			label.addStyleName("colored");
    			final HorizontalLayout vHorizontalLayout = new HorizontalLayout(label);
				vHorizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//productionProcessActivityInstanceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessActivityInstanceDTOTable.addGeneratedColumn("g_require_parcial_product_recall", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessActivityInstanceDTO vProductionProcessActivityInstanceDTO = (ProductionProcessActivityInstanceDTO)itemId;
				if(vProductionProcessActivityInstanceDTO.getRequire_parcial_product_recall() == true){
					final Label labelLockerNumber = new Label(messages.get(VIEW_NAME + "tab.activities.instances.table.column.require.parcial.product.recall.label.locker.number"));
					final Label labelLocker = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vProductionProcessActivityInstanceDTO.getRecall_locker_number());
					labelLocker.addStyleName("colored");
	    			final Label labelRecallDate = new Label(
	    					vProductionProcessActivityInstanceDTO.getParcial_product_recall_date()!= null ?
	    					SgpUtils.dateFormater.format(vProductionProcessActivityInstanceDTO.getParcial_product_recall_date())
	    					: messages.get("application.common.not.recalled.label"));
	    			final HorizontalLayout vHorizontalLayout = new HorizontalLayout(labelLockerNumber,labelLocker,labelRecallDate);
					vHorizontalLayout.setSpacing(true);
					return vHorizontalLayout;
				}else{
					final Label label = new Label(messages.get("application.common.do.not.apply.label"));					
					return label;
				}					
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//productionProcessActivityInstanceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessActivityInstanceDTOTable.addGeneratedColumn("g_activity_id", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessActivityInstanceDTO vProductionProcessActivityInstanceDTO = (ProductionProcessActivityInstanceDTO)itemId;
				return vProductionProcessActivityInstanceDTO.getProductionProcessActivityDTO().getActivity_id();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//productionProcessActivityInstanceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessActivityInstanceDTOTable.addGeneratedColumn("g_assignment_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessActivityInstanceDTO vProductionProcessActivityInstanceDTO = (ProductionProcessActivityInstanceDTO)itemId;
				return vProductionProcessActivityInstanceDTO.getAssignment_date()!=null ? SgpUtils.dateFormater.format(vProductionProcessActivityInstanceDTO.getAssignment_date()) : messages.get("application.common.not.assigned.label");
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessActivityInstanceDTOTable.addGeneratedColumn("g_person", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessActivityInstanceDTO vProductionProcessActivityInstanceDTO = (ProductionProcessActivityInstanceDTO)itemId;					
				final Label label= vProductionProcessActivityInstanceDTO.getPersonDTO()!= null ? 
						new Label
						(vProductionProcessActivityInstanceDTO.getPersonDTO().getPersonal_name() + ", " + vProductionProcessActivityInstanceDTO.getPersonDTO().getPersonal_last_name())
						: new Label(messages.get("application.common.not.assigned.label"));
				label.addStyleName("colored");
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(label);
				vHorizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//productionProcessActivityInstanceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessActivityInstanceDTOTable.addGeneratedColumn("g_status", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessActivityInstanceDTO vProductionProcessActivityInstanceDTO = (ProductionProcessActivityInstanceDTO)itemId;
				final Label statusLabel = new Label(messages.get(vProductionProcessActivityInstanceDTO.getStatus()));
				statusLabel.addStyleName("colored");
				return statusLabel;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//productionProcessActivityInstanceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessActivityInstanceDTOTable.addGeneratedColumn("g_next_status", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessActivityInstanceDTO vProductionProcessActivityInstanceDTO = (ProductionProcessActivityInstanceDTO)itemId;
				final Label statusLabel = new Label(messages.get(vProductionProcessActivityInstanceDTO.getNext_status()));
				statusLabel.addStyleName("colored");
				return statusLabel;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//productionProcessActivityInstanceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessActivityInstanceDTOTable.addGeneratedColumn("g_activity_start_work_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessActivityInstanceDTO vProductionProcessActivityInstanceDTO = (ProductionProcessActivityInstanceDTO)itemId;
				return vProductionProcessActivityInstanceDTO.getActivity_start_work_date()!=null ? SgpUtils.dateFormater.format(vProductionProcessActivityInstanceDTO.getActivity_start_work_date()) : messages.get("application.common.not.started.label");
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessActivityInstanceDTOTable.addGeneratedColumn("g_activity_finish_work_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessActivityInstanceDTO vProductionProcessActivityInstanceDTO = (ProductionProcessActivityInstanceDTO)itemId;
				return vProductionProcessActivityInstanceDTO.getActivity_finish_work_date()!=null ? SgpUtils.dateFormater.format(vProductionProcessActivityInstanceDTO.getActivity_finish_work_date()) : messages.get("application.common.not.finished.label");
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//machineUseCostTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessActivityInstanceDTOTable.addGeneratedColumn("g_delivers_partial_result", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final ProductionProcessActivityInstanceDTO vProductionProcessActivityInstanceDTO = (ProductionProcessActivityInstanceDTO)itemId;
				if(vProductionProcessActivityInstanceDTO.getDelivers_partial_result() == true){
					if(vProductionProcessActivityInstanceDTO.getPartial_result_delivery_date()!=null){
						final Label labelLocker = TableNumericColumnCellLabelHelper.buildNumericLabelByClass(vProductionProcessActivityInstanceDTO.getOccupied_locker_number());
						labelLocker.addStyleName("colored");
						final Label labelRecallDate = new Label(SgpUtils.dateFormater.format(vProductionProcessActivityInstanceDTO.getPartial_result_delivery_date()));
						final HorizontalLayout vHorizontalLayout = new HorizontalLayout(labelLocker,labelRecallDate);
						vHorizontalLayout.setSpacing(true);
						return vHorizontalLayout;
					}else{
						final Label label = new Label(messages.get("application.common.not.delivered.label"));					
						return label;						
					}
				}else{
					final Label label = new Label(messages.get("application.common.do.not.apply.label"));					
					return label;
				}					
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//productionProcessActivityInstanceDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.productionProcessActivityInstanceDTOTable.setVisibleColumns(new Object[] 
    			{"operations","g_product_instance_unique_number","g_activity_instance_unique_number","g_require_parcial_product_recall",
    			 "g_activity_id","g_assignment_date","g_person","g_status","g_next_status",
    			 "g_activity_start_work_date","g_activity_finish_work_date","g_delivers_partial_result"});
    	this.productionProcessActivityInstanceDTOTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.productionProcessActivityInstanceDTOTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.productionProcessActivityInstanceDTOTable.setColumnHeader("g_product_instance_unique_number", this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.column.product.instance.unique.number"));
    	this.productionProcessActivityInstanceDTOTable.setColumnAlignment("g_product_instance_unique_number", Table.Align.RIGHT);
    	
    	this.productionProcessActivityInstanceDTOTable.setColumnHeader("g_activity_instance_unique_number", this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.column.activity.instance.unique.number"));
    	this.productionProcessActivityInstanceDTOTable.setColumnAlignment("g_activity_instance_unique_number", Table.Align.RIGHT);
    	
    	this.productionProcessActivityInstanceDTOTable.setColumnHeader("g_require_parcial_product_recall", this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.column.require.parcial.product.recall"));
    	this.productionProcessActivityInstanceDTOTable.setColumnAlignment("g_require_parcial_product_recall", Table.Align.LEFT);
    	
    	this.productionProcessActivityInstanceDTOTable.setColumnHeader("g_activity_id", this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.column.activity.id"));
    	this.productionProcessActivityInstanceDTOTable.setColumnAlignment("g_activity_id", Table.Align.LEFT);
    	
    	this.productionProcessActivityInstanceDTOTable.setColumnHeader("g_assignment_date", this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.column.assignment.date"));
    	this.productionProcessActivityInstanceDTOTable.setColumnAlignment("g_assignment_date", Table.Align.LEFT);
  	
    	this.productionProcessActivityInstanceDTOTable.setColumnHeader("g_person", this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.column.person"));
    	this.productionProcessActivityInstanceDTOTable.setColumnAlignment("g_person", Table.Align.LEFT);
    	
    	this.productionProcessActivityInstanceDTOTable.setColumnHeader("g_status", this.messages.get("application.common.status.label"));
    	this.productionProcessActivityInstanceDTOTable.setColumnAlignment("g_status", Table.Align.LEFT);

    	this.productionProcessActivityInstanceDTOTable.setColumnHeader("g_next_status", this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.column.next.status"));
    	this.productionProcessActivityInstanceDTOTable.setColumnAlignment("g_next_status", Table.Align.LEFT);
    	
    	this.productionProcessActivityInstanceDTOTable.setColumnHeader("g_activity_start_work_date", this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.column.activity.start.work.date"));
    	this.productionProcessActivityInstanceDTOTable.setColumnAlignment("g_activity_start_work_date", Table.Align.LEFT);
    	
    	this.productionProcessActivityInstanceDTOTable.setColumnHeader("g_activity_finish_work_date", this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.column.activity.finish.work.date"));
    	this.productionProcessActivityInstanceDTOTable.setColumnAlignment("g_activity_finish_work_date", Table.Align.LEFT);
    	
    	this.productionProcessActivityInstanceDTOTable.setColumnHeader("g_delivers_partial_result", this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.column.delivers.partial.result"));
    	this.productionProcessActivityInstanceDTOTable.setColumnAlignment("g_delivers_partial_result", Table.Align.LEFT);
    	
    	this.productionProcessActivityInstanceDTOTable.setSizeFull();
    	this.productionProcessActivityInstanceDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.productionProcessActivityInstanceDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.productionProcessActivityInstanceDTOTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.productionProcessActivityInstanceDTOTable.setColumnExpandRatio("operations", 0.0009f);
    	this.productionProcessActivityInstanceDTOTable.setColumnExpandRatio("g_product_instance_unique_number", 0.0005f);   
    	this.productionProcessActivityInstanceDTOTable.setColumnExpandRatio("g_activity_instance_unique_number", 0.0005f);    	
    	this.productionProcessActivityInstanceDTOTable.setColumnExpandRatio("g_require_parcial_product_recall", 0.0014f);
    	this.productionProcessActivityInstanceDTOTable.setColumnExpandRatio("g_activity_id", 0.0017f);
    	this.productionProcessActivityInstanceDTOTable.setColumnExpandRatio("g_assignment_date", 0.0009f);
    	this.productionProcessActivityInstanceDTOTable.setColumnExpandRatio("g_person", 0.0011f);
    	this.productionProcessActivityInstanceDTOTable.setColumnExpandRatio("g_status", 0.0009f);
    	this.productionProcessActivityInstanceDTOTable.setColumnExpandRatio("g_next_status", 0.0009f);
    	this.productionProcessActivityInstanceDTOTable.setColumnExpandRatio("g_activity_start_work_date", 0.0011f);
    	this.productionProcessActivityInstanceDTOTable.setColumnExpandRatio("g_activity_finish_work_date", 0.0011f);
    	this.productionProcessActivityInstanceDTOTable.setColumnExpandRatio("g_delivers_partial_result", 0.0014f);
    	this.productionProcessActivityInstanceDTOTable.setSelectable(true);
    	this.productionProcessActivityInstanceDTOTable.setColumnCollapsingAllowed(true);
    	this.productionProcessActivityInstanceDTOTable.setColumnCollapsible("operations", false);
    	this.productionProcessActivityInstanceDTOTable.setColumnCollapsible("g_activity_instance_unique_number", true);
    	this.productionProcessActivityInstanceDTOTable.setColumnCollapsible("g_status", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.productionProcessActivityInstanceDTOTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.productionProcessActivityInstanceDTOTable.setSortAscending(false);
    	this.productionProcessActivityInstanceDTOTable.setColumnReorderingAllowed(true);
    	this.productionProcessActivityInstanceDTOTable.setFooterVisible(true);
    	this.productionProcessActivityInstanceDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
    	
        this.addComponent(productionProcessActivityInstanceDTOTable);
        this.setExpandRatio(productionProcessActivityInstanceDTOTable, 1);
	}
	
	private HorizontalLayout buildOperationsButtonPanel(final ProductionProcessActivityInstanceDTO vProductionProcessActivityInstanceDTO){	
		final Button assignActivityButton = new Button();
		assignActivityButton.setIcon(FontAwesome.USER);
		assignActivityButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.column.operations.button.assign.activity.description"));
		assignActivityButton.addStyleName("borderless");
		assignActivityButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar ProductionProcessActivityInstanceDTO...\n" + vProductionProcessActivityInstanceDTO.toString());
                try{		                	
                	productionProcessActivityInstanceDTOTableFunctions.assignProductionProcessActivityInstanceDTO(vProductionProcessActivityInstanceDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		
		assignActivityButton.setVisible(
				vProductionProcessActivityInstanceDTO.getIs_asignable()
				&& vProductionProcessActivityInstanceDTO.getAssignment_date() == null
				&& vProductionProcessActivityInstanceDTO.getStatus().equals("application.common.status.pending"));

		final Button doPartialProductRecallButton = new Button();
		doPartialProductRecallButton.setIcon(FontAwesome.BITBUCKET);
		doPartialProductRecallButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.column.operations.button.do.partial.product.recall.description"));
		doPartialProductRecallButton.addStyleName("borderless");
		doPartialProductRecallButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar ProductionProcessActivityInstanceDTO...\n" + vProductionProcessActivityInstanceDTO.toString());
                try{		                	
                	productionProcessActivityInstanceDTOTableFunctions.doParcialProductRecall(vProductionProcessActivityInstanceDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		
		doPartialProductRecallButton.setVisible(
				vProductionProcessActivityInstanceDTO.getRequire_parcial_product_recall()
				&& vProductionProcessActivityInstanceDTO.getStatus().equals("application.common.status.assigned")
				&& vProductionProcessActivityInstanceDTO.getNext_status().equals("application.common.status.partial.result.recalled"));
		
		final Button doRawMaterialSupplylButton = new Button();
		doRawMaterialSupplylButton.setIcon(FontAwesome.FLASK);
		doRawMaterialSupplylButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.column.operations.button.supply.raw.material.description"));
		doRawMaterialSupplylButton.addStyleName("borderless");
		doRawMaterialSupplylButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar ProductionProcessActivityInstanceDTO...\n" + vProductionProcessActivityInstanceDTO.toString());
                try{		                	
                	productionProcessActivityInstanceDTOTableFunctions.effectuateRawMaterialSupply(vProductionProcessActivityInstanceDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		
		doRawMaterialSupplylButton.setVisible(
				(vProductionProcessActivityInstanceDTO.getStatus().equals("application.common.status.partial.result.recalled")
				|| vProductionProcessActivityInstanceDTO.getStatus().equals("application.common.status.assigned"))
				&& vProductionProcessActivityInstanceDTO.getNext_status().equals("application.common.status.in.progress"));
		
		final Button finalizeButton = new Button();
		finalizeButton.setIcon(FontAwesome.FLAG_CHECKERED);
		finalizeButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.column.operations.button.finalize.description"));
		finalizeButton.addStyleName("borderless");
		finalizeButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n finalize ProductionProcessActivityInstanceDTO...\n" + vProductionProcessActivityInstanceDTO.toString());
                try{		                	
                	productionProcessActivityInstanceDTOTableFunctions.finalizeProductionProcessActivityInstanceDTO(vProductionProcessActivityInstanceDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		
		finalizeButton.setVisible(vProductionProcessActivityInstanceDTO.getStatus().equals("application.common.status.in.progress"));
		
		final Button deliverPartialProductButton = new Button();
		deliverPartialProductButton.setIcon(FontAwesome.CART_PLUS);
		deliverPartialProductButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.column.operations.button.deliver.partial.result.description"));
		deliverPartialProductButton.addStyleName("borderless");
		deliverPartialProductButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar ProductionProcessActivityInstanceDTO...\n" + vProductionProcessActivityInstanceDTO.toString());
                try{		                	
                	productionProcessActivityInstanceDTOTableFunctions.deliverPartialResult(vProductionProcessActivityInstanceDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		
		deliverPartialProductButton.setVisible(
				vProductionProcessActivityInstanceDTO.getDelivers_partial_result()
				&& vProductionProcessActivityInstanceDTO.getStatus().equals("application.common.status.finalized")
				&& vProductionProcessActivityInstanceDTO.getNext_status().equals("application.common.status.partial.result.delivered"));
		
		final Button deliverFinalProductButton = new Button();
		deliverFinalProductButton.setIcon(FontAwesome.STAR);
		deliverFinalProductButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.activities.instances.table.column.operations.button.deliver.final.product.description"));
		deliverFinalProductButton.addStyleName("borderless");
		deliverFinalProductButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar ProductionProcessActivityInstanceDTO...\n" + vProductionProcessActivityInstanceDTO.toString());
                try{		                	
                	productionProcessActivityInstanceDTOTableFunctions.deliverFinalProduct(vProductionProcessActivityInstanceDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		
		deliverFinalProductButton.setVisible(
				vProductionProcessActivityInstanceDTO.getDelivers_product_instance()
				&& vProductionProcessActivityInstanceDTO.getStatus().equals("application.common.status.finalized")
				&& vProductionProcessActivityInstanceDTO.getNext_status().equals("application.common.status.final.product.delivered"));
		/*instantiateProductionProcessActivitiesButton.setVisible(bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
				.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "instantiate.production.process.activities"));*/
		
		
		final Button queryOrderNumberButton = new Button();
		queryOrderNumberButton.setIcon(FontAwesome.SEARCH);
		queryOrderNumberButton.setDescription(this.messages.get("application.common.query.label") + " " + this.messages.get("application.common.order.number.indicator.label").toLowerCase());
		queryOrderNumberButton.addStyleName("borderless");
		queryOrderNumberButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
            	logger.info("\n editar ProductionProcessActivityInstanceDTO...\n" + vProductionProcessActivityInstanceDTO.toString());
                try{		                	
                	productionProcessActivityInstanceDTOTableFunctions.queryOrderNumber(vProductionProcessActivityInstanceDTO);
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }			                
            }
        });
		
		
		final HorizontalLayout operationsButtonPanel = 
				new HorizontalLayout(assignActivityButton,doPartialProductRecallButton,doRawMaterialSupplylButton,finalizeButton,
						deliverPartialProductButton,deliverFinalProductButton,queryOrderNumberButton);
		return operationsButtonPanel;
	}
	
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		logger.info("\nbrowserResized");
	    // Some columns are collapsed when browser window width gets small
	    // enough to make the table fit better.
	    if (this.productionProcessActivityInstanceDTOTable!=null && defaultColumnsVisible()) {
	       for (String propertyId : DEFAULT_COLLAPSIBLE) {
	    	   this.productionProcessActivityInstanceDTOTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	       }
	    }
	}//public void browserResized(final BrowserResizeEvent event)
	
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (this.productionProcessActivityInstanceDTOTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	    }
	    return result;
	}//private boolean defaultColumnsVisible()
}
