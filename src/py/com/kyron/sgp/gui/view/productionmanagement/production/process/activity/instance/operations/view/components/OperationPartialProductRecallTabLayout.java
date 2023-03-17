package py.com.kyron.sgp.gui.view.productionmanagement.production.process.activity.instance.operations.view.components;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityInstanceDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.bussines.stockmanagement.domain.PAIRawMaterialSupplyDTO;
import py.com.kyron.sgp.bussines.stockmanagement.service.StockManagementService;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class OperationPartialProductRecallTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(OperationPartialProductRecallTabLayout.class);	
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
	private ProductionProcessActivityInstanceDTO partialProductProducerActivity; 
	private List<PAIRawMaterialSupplyDTO> listPartialProductProducerActivityAIRawMaterialSupplyDTO;
	
	public OperationPartialProductRecallTabLayout(
			final ProductionProcessActivityInstanceOperationsViewFunctions productionProcessActivityInstanceOperationsViewFunctions,
			final String VIEW_NAME,
			final ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO, final boolean setUpLayoutContent) {
		// TODO Auto-generated constructor stub
		this.productionProcessActivityInstanceOperationsViewFunctions = productionProcessActivityInstanceOperationsViewFunctions;
		this.VIEW_NAME = VIEW_NAME;
		this.productionProcessActivityInstanceDTO = productionProcessActivityInstanceDTO;
		try{
			logger.info("\n OperationPartialProductRecallTabLayout..");
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

	/*public OperationPartialProductRecallTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	private void initServices() throws Exception{
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
		this.stockManagementService = (StockManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.STOCK_MANAGEMENT_SERVICE);
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n OperationPartialProductRecallTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void setUpLayoutContent() throws PmsServiceException{
		this.retrievePartialProductProducerActivity();
		this.retrieveListPAIRawMaterialSupplyDTO();		
		this.addComponent(this.setUpHeader()); 
		this.addComponent(this.setUpOkCancelButtons());
	}
	
	private void retrieveListPAIRawMaterialSupplyDTO() throws PmsServiceException{
		this.listPAIRawMaterialSupplyDTO = 
				this.stockManagementService.listPAIRawMaterialSupplyDTO
				(new PAIRawMaterialSupplyDTO(null, this.productionProcessActivityInstanceDTO.getId()));
		this.listPartialProductProducerActivityAIRawMaterialSupplyDTO = this.partialProductProducerActivity.getListPAIRawMaterialSupplyDTO();
	}
	
	private void retrievePartialProductProducerActivity() throws PmsServiceException{
		this.partialProductProducerActivity = 
				this.productionManagementService.listProductionProcessActivityInstanceDTOHistory
				(new ProductionProcessActivityInstanceDTO(
						null, 
						null, 
						this.productionProcessActivityInstanceDTO.getActivity_instance_unique_number() - 1L)
				).get(0);
	}
	
	private HorizontalLayout setUpHeader(){

		VerticalLayout column01 = new VerticalLayout();
		/*VerticalLayout column02 = new VerticalLayout();
		VerticalLayout column03 = new VerticalLayout();*/
		
		column01.addComponent(this.setUpMainTitle());
		
		Label locker_numberLabel = new Label(this.messages.get(this.VIEW_NAME + "tab.operation.partial.product.recall.label.consumer.locker.number") + ":");
		locker_numberLabel.addStyleName(ValoTheme.LABEL_COLORED);
		locker_numberLabel.addStyleName(ValoTheme.LABEL_H2);
		Label locker_numberValue = new Label("" + this.productionProcessActivityInstanceDTO.getRecall_locker_number());
		locker_numberValue.addStyleName(ValoTheme.LABEL_H2);
		HorizontalLayout locker_numberHorizontalLayout = new HorizontalLayout(locker_numberLabel,locker_numberValue);
		locker_numberHorizontalLayout.setSpacing(true);
		column01.addComponent(locker_numberHorizontalLayout);		
		
		Label process_descriptionLabel = new Label(this.messages.get("application.common.process.label") + ":");
		process_descriptionLabel.addStyleName(ValoTheme.LABEL_COLORED);
		Label process_descriptionValue = new Label(this.listPAIRawMaterialSupplyDTO.get(0).getProcess_description());
		HorizontalLayout process_descriptionHorizontalLayout = new HorizontalLayout(process_descriptionLabel,process_descriptionValue);
		process_descriptionHorizontalLayout.setSpacing(true);
		column01.addComponent(process_descriptionHorizontalLayout);
		
		Label partialProductProducerActivity_descriptionLabel = new Label(this.messages.get(this.VIEW_NAME + "tab.operation.partial.product.recall.label.producer.activity.description") + ":");
		partialProductProducerActivity_descriptionLabel.addStyleName(ValoTheme.LABEL_COLORED);
		Label partialProductProducerActivity_descriptionValue = new Label(this.listPartialProductProducerActivityAIRawMaterialSupplyDTO.get(0).getActivity_description());
		HorizontalLayout partialProductProducerActivity_descriptionHorizontalLayout = new HorizontalLayout(partialProductProducerActivity_descriptionLabel,partialProductProducerActivity_descriptionValue);
		partialProductProducerActivity_descriptionHorizontalLayout.setSpacing(true);
		column01.addComponent(partialProductProducerActivity_descriptionHorizontalLayout);		

		
		Label partialProductProducerActivity_allocation_date_Label = new Label(this.messages.get(this.VIEW_NAME + "tab.operation.allocate.half.way.product.label.allocation.time") + ":");
		partialProductProducerActivity_allocation_date_Label.addStyleName(ValoTheme.LABEL_COLORED);
		Label partialProductProducerActivity_allocation_date_Value = new Label(SgpUtils.dateFormater.format(this.partialProductProducerActivity.getPartial_result_delivery_date()));
		HorizontalLayout partialProductProducerActivity_allocation_date_HorizontalLayout = new HorizontalLayout(partialProductProducerActivity_allocation_date_Label,partialProductProducerActivity_allocation_date_Value);
		partialProductProducerActivity_allocation_date_HorizontalLayout.setSpacing(true);
		column01.addComponent(partialProductProducerActivity_allocation_date_HorizontalLayout);
		
		Label consumer_activity_descriptionLabel = new Label(this.messages.get(this.VIEW_NAME + "tab.operation.partial.product.recall.label.consumer.activity.description") + ":");
		consumer_activity_descriptionLabel.addStyleName(ValoTheme.LABEL_COLORED);
		Label consumer_activity_descriptionValue = new Label(this.listPAIRawMaterialSupplyDTO.get(0).getActivity_description());
		HorizontalLayout consumer_activity_descriptionHorizontalLayout = new HorizontalLayout(consumer_activity_descriptionLabel,consumer_activity_descriptionValue);
		consumer_activity_descriptionHorizontalLayout.setSpacing(true);
		column01.addComponent(consumer_activity_descriptionHorizontalLayout);		
	
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
        Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.operation.partial.product.recall.main.title"));
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        HorizontalLayout header = new HorizontalLayout(title);
        header.addComponent(title);
        header.setMargin(new MarginInfo(false, false, true, false));
        return header;
	}
	
    private HorizontalLayout setUpOkCancelButtons(){

		final Button okButton = new Button(this.messages.get("application.common.button.save.label"));/*"Cancelar"*/
		okButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		productionProcessActivityInstanceOperationsViewFunctions.effectuatePartialProductRecall();
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		
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
}
