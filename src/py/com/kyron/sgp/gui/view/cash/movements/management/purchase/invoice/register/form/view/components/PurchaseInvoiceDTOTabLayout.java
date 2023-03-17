package py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceItemDTO;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.personmanagement.service.PersonManagementService;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MeasurmentUnitDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.utils.commponents.personmanagement.PersonFinderWindow;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

@SuppressWarnings("serial")
public class PurchaseInvoiceDTOTabLayout extends VerticalLayout 
implements PurchaseInvoiceHeaderLayoutFunctions,PurchaseInvoiceItemDTOTableFunctions {

	private final Logger logger = LoggerFactory.getLogger(PurchaseInvoiceDTOTabLayout.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final PurchaseInvoiceDTO purchaseInvoiceDTO;
	private PersonManagementService personManagementService;
	private PersonFinderWindow personFinderWindow;
	private BussinesSessionUtils bussinesSessionUtils;
	private PurchaseInvoiceHeaderLayout purchaseInvoiceHeaderLayout;
	private RawMaterialFinderWindow rawMaterialFinderWindow;
	private MeasurmentUnitFinderWindow measurmentUnitFinderWindow;
	private PurchaseInvoiceItemDTOWindow purchaseInvoiceItemDTOWindow;
	private PurchaseInvoiceItemDTOTableLayout purchaseInvoiceItemDTOTableLayout;
	private PurchaseInvoiceItemDTOTableTotalsLayout purchaseInvoiceItemDTOTableTotalsLayout;
	private Button okButton;
	private HorizontalLayout okCancelHorizontalLayout;
	private final PurchaseInvoiceRegisterFormViewFunctions purchaseInvoiceRegisterFormViewFunctions;
	private HorizontalLayout addItemButtonHorizontalLayout;
	
	public PurchaseInvoiceDTOTabLayout(final String VIEW_NAME,
			final PurchaseInvoiceRegisterFormViewFunctions purchaseInvoiceRegisterFormViewFunctions,
			final PurchaseInvoiceDTO purchaseInvoiceDTO,
			final boolean setUpLayoutContent) {
		// TODO Auto-generated constructor stub
		this.VIEW_NAME = VIEW_NAME;
		this.purchaseInvoiceRegisterFormViewFunctions = purchaseInvoiceRegisterFormViewFunctions;
		this.purchaseInvoiceDTO = purchaseInvoiceDTO;
		try{
			logger.info("\n PurchaseInvoiceDTOTabLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
	        this.setSizeFull();
	        //this.addStyleName("v-scrollable");
	        //this.setHeight("100%");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        if(setUpLayoutContent)this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}
	
	/*public PurchaseInvoiceDTOTabLayout(Component... children) {
	super(children);
	// TODO Auto-generated constructor stub
	}*/
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n PurchaseInvoiceDTOTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//
	
	private void initServices() throws Exception{
		this.personManagementService = (PersonManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PERSON_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}


	private void setUpLayoutContent(){
		this.purchaseInvoiceHeaderLayout = new PurchaseInvoiceHeaderLayout(this.VIEW_NAME, this, this.purchaseInvoiceDTO);		
		this.setUpaddItemButtonHorizontalLayout();		
		if(this.purchaseInvoiceDTO.getListPurchaseInvoiceItemDTO() == null)this.purchaseInvoiceDTO.setListPurchaseInvoiceItemDTO(new ArrayList<PurchaseInvoiceItemDTO>());
		this.purchaseInvoiceItemDTOTableLayout = new PurchaseInvoiceItemDTOTableLayout(this.VIEW_NAME, this, this.purchaseInvoiceDTO.getListPurchaseInvoiceItemDTO());
		this.purchaseInvoiceItemDTOTableTotalsLayout = new PurchaseInvoiceItemDTOTableTotalsLayout(this.VIEW_NAME, this.purchaseInvoiceDTO.getListPurchaseInvoiceItemDTO());
		this.setUpOkCancelButtons();
		
		this.addComponent(this.purchaseInvoiceHeaderLayout);
		this.addComponent(this.addItemButtonHorizontalLayout);
		this.addComponent(this.purchaseInvoiceItemDTOTableLayout);
		this.addComponent(this.purchaseInvoiceItemDTOTableTotalsLayout);
		this.addComponent(this.okCancelHorizontalLayout);
	}	
	
	
	@Override
	public void searchProvider() {
		// TODO Auto-generated method stub
    	personFinderWindow = new PersonFinderWindow(false,true,false);
    	personFinderWindow.addCloseListener(setUpPersonFinderWindowCloseListener());
    	personFinderWindow.adjuntWindowSizeAccordingToCientDisplay();		
	}
	
	private CloseListener setUpPersonFinderWindowCloseListener(){
		return new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{
					PersonDTO vPersonDTO = personFinderWindow.getPersonDTOTableRowSelected();
					logger.info("\n******************************"
								+"\nthe person finder window has been closed"
								+"\nperson found: \n" + vPersonDTO
								+"\n******************************");
					if(vPersonDTO!=null){
						purchaseInvoiceDTO.setPersonDTO(vPersonDTO);
						purchaseInvoiceDTO.setId_person(vPersonDTO.getId());
						purchaseInvoiceHeaderLayout.refreshRucAndComercialNameValues();
					}
				}catch(Exception ex){
					commonExceptionErrorNotification.showErrorMessageNotification(ex);
				}
			}			
		};
	}
	
    private void setUpaddItemButtonHorizontalLayout(){
		final Button addItemButton = new Button(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.button.add.item.description"));/*"Cancelar"*/
		addItemButton.setIcon(FontAwesome.PLUS_CIRCLE);
		addItemButton.addStyleName("borderless");
		addItemButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		logger.info( "\n==================================="
		            					+"\n reSetLayoutAfterAnItemHasBeenAdded"
		            					+"\n===================================");
		                    try{
		                    	rawMaterialFinderWindow = new RawMaterialFinderWindow();
		                    	rawMaterialFinderWindow.addCloseListener(setUpRawMaterialFinderWindowCloseListener());
		                    	rawMaterialFinderWindow.adjuntWindowSizeAccordingToCientDisplay();
		                    }catch(Exception e){
		                    	commonExceptionErrorNotification.showErrorMessageNotification(e);
		                    }		                    
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		this.addItemButtonHorizontalLayout = new HorizontalLayout(addItemButton);
		this.addItemButtonHorizontalLayout.setMargin(new MarginInfo(false, false, false, true));
    }
    
	private CloseListener setUpRawMaterialFinderWindowCloseListener(){
		return new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{
					final RawMaterialDTO vRawMaterialDTO = rawMaterialFinderWindow.getRawMaterialDTOTableRowSelected();
					logger.info("\n******************************"
								+"\n the raw material finder window has been closed"
								+"\n person found: \n" + vRawMaterialDTO
								+"\n******************************");
					if(vRawMaterialDTO!=null){
						showMeasurmentUnitFinderWindow(vRawMaterialDTO);
					}					
				}catch(Exception ex){
					commonExceptionErrorNotification.showErrorMessageNotification(ex);
				}
			}			
		};
	}
	
	private void showMeasurmentUnitFinderWindow(final RawMaterialDTO vRawMaterialDTO){
		measurmentUnitFinderWindow = new MeasurmentUnitFinderWindow(vRawMaterialDTO.getRaw_material_id());
		measurmentUnitFinderWindow.addCloseListener(setUpMeasurmentUnitFinderWindowCloseListener(vRawMaterialDTO));
		measurmentUnitFinderWindow.adjuntWindowSizeAccordingToCientDisplay();
	}
	
	private CloseListener setUpMeasurmentUnitFinderWindowCloseListener(final RawMaterialDTO vRawMaterialDTO){
		return new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{
					final MeasurmentUnitDTO vMeasurmentUnitDTO = measurmentUnitFinderWindow.getMeasurmentUnitDTOTableRowSelected();
					logger.info("\n******************************"
								+"\n the measurment unit finder window has been closed"
								+"\n person found: \n" + vMeasurmentUnitDTO
								+"\n******************************");
					if(vMeasurmentUnitDTO!=null){
						showPurchaseInvoiceItemDTOWindow(vRawMaterialDTO,vMeasurmentUnitDTO);
					}					
				}catch(Exception ex){
					commonExceptionErrorNotification.showErrorMessageNotification(ex);
				}
			}			
		};
	}
	
	private void showPurchaseInvoiceItemDTOWindow(
			final RawMaterialDTO vRawMaterialDTO,final MeasurmentUnitDTO vMeasurmentUnitDTO){
		this.purchaseInvoiceItemDTOWindow = new PurchaseInvoiceItemDTOWindow(this.initAndSetOrderItem(vRawMaterialDTO, vMeasurmentUnitDTO));
		this.purchaseInvoiceItemDTOWindow.addCloseListener(setUpPurchaseInvoiceItemDTOWindowCloseListener());
		this.purchaseInvoiceItemDTOWindow.adjuntWindowSizeAccordingToCientDisplay();
	}
	
	private PurchaseInvoiceItemDTO initAndSetOrderItem(RawMaterialDTO vRawMaterialDTO, final MeasurmentUnitDTO vMeasurmentUnitDTO){		
		final PurchaseInvoiceItemDTO vPurchaseInvoiceItemDTO = new PurchaseInvoiceItemDTO(null, null, Math.round(Math.random() * 10000));		
		vPurchaseInvoiceItemDTO.setRawMaterialDTO(vRawMaterialDTO);
		vPurchaseInvoiceItemDTO.setMeasurmentUnitDTO(vMeasurmentUnitDTO);
		vPurchaseInvoiceItemDTO.setId_raw_material(vRawMaterialDTO.getId());
		vPurchaseInvoiceItemDTO.setId_measurment_unit(vMeasurmentUnitDTO.getId());
		return vPurchaseInvoiceItemDTO;
	}
	
	private CloseListener setUpPurchaseInvoiceItemDTOWindowCloseListener(){
		return new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{
					final PurchaseInvoiceItemDTO vPurchaseInvoiceItemDTO = purchaseInvoiceItemDTOWindow.getPurchaseInvoiceItemDTO();
					logger.info("\n******************************"
								+"\n the PurchaseInvoiceItemDTOWindow has been closed"
								+"\n found: \n" + vPurchaseInvoiceItemDTO
								+"\n******************************");
					addPurchaseInvoiceItemDTO(vPurchaseInvoiceItemDTO);
        			reSetLayoutAfterAnItemHasBeenAdded();
        			refreshOkButtonStatus();
				}catch(Exception ex){
					commonExceptionErrorNotification.showErrorMessageNotification(ex);
				}
			}			
		};
	}
	
	private void addPurchaseInvoiceItemDTO(final PurchaseInvoiceItemDTO vPurchaseInvoiceItemDTO){
		if(vPurchaseInvoiceItemDTO.getUnit_price_amount()!=null && vPurchaseInvoiceItemDTO.getQuantity()!=null){
			vPurchaseInvoiceItemDTO.setValue_added_tax_10_amount(vPurchaseInvoiceItemDTO.getUnit_price_amount().multiply(vPurchaseInvoiceItemDTO.getQuantity()));
			vPurchaseInvoiceItemDTO.setExempt_amount(BigDecimal.ZERO);
			vPurchaseInvoiceItemDTO.setValue_added_tax_5_amount(BigDecimal.ZERO);
			this.purchaseInvoiceDTO.getListPurchaseInvoiceItemDTO().add(vPurchaseInvoiceItemDTO);
		}
	}
	
    public void reSetLayoutAfterAnItemHasBeenAdded(){
		this.removeComponent(this.purchaseInvoiceItemDTOTableLayout);
		this.removeComponent(this.purchaseInvoiceItemDTOTableTotalsLayout);
		this.removeComponent(this.okCancelHorizontalLayout);
		
		
		this.purchaseInvoiceItemDTOTableLayout = new PurchaseInvoiceItemDTOTableLayout(this.VIEW_NAME, this, this.purchaseInvoiceDTO.getListPurchaseInvoiceItemDTO());
		this.purchaseInvoiceItemDTOTableTotalsLayout = 
				new PurchaseInvoiceItemDTOTableTotalsLayout(this.VIEW_NAME,this.purchaseInvoiceDTO.getListPurchaseInvoiceItemDTO());
		//estimated finalize date panel date
		this.setUpOkCancelButtons();
		
		this.addComponent(this.purchaseInvoiceItemDTOTableLayout);
		this.addComponent(this.purchaseInvoiceItemDTOTableTotalsLayout);
		this.addComponent(this.okCancelHorizontalLayout);   	
    }
    
	private void refreshOkButtonStatus(){
		this.okButton.setEnabled(
				this.purchaseInvoiceDTO.getListPurchaseInvoiceItemDTO()!=null 
				&& !this.purchaseInvoiceDTO.getListPurchaseInvoiceItemDTO().isEmpty()
				&& 
				(		this.purchaseInvoiceDTO.getStatus() == null ||
						this.purchaseInvoiceDTO.getStatus().equals("application.common.status.pending")	
						|| this.purchaseInvoiceDTO.getStatus().equals("application.common.status.revision")
				));
		this.okButton.markAsDirty();
	}
	
    private void setUpOkCancelButtons(){
		this.okButton = new Button(this.messages.get("application.common.button.save.label"));/*"Cancelar"*/
		this.okButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		purchaseInvoiceHeaderLayout.commitFormsValues();
		            		            		
		            		purchaseInvoiceDTO.setValue_added_tax_5_amount(purchaseInvoiceItemDTOTableTotalsLayout.getTotalValueAdded_5_Tax());
		            		purchaseInvoiceDTO.setValue_added_tax_10_amount(purchaseInvoiceItemDTOTableTotalsLayout.getTotalValueAdded_10_Tax());
		            		purchaseInvoiceDTO.setExempt_amount(purchaseInvoiceItemDTOTableTotalsLayout.getExcemptTotal());
		            		purchaseInvoiceRegisterFormViewFunctions.saveButtonActionPurchaseInvoiceDTOTabLayout(purchaseInvoiceDTO);
		            		commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		this.refreshOkButtonStatus();
		
		final Button cancelButton = new Button(this.messages.get("application.common.button.cancel.label"));/*"Cancelar"*/
		cancelButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		purchaseInvoiceHeaderLayout.discardFormsValues();
		            		purchaseInvoiceRegisterFormViewFunctions.navigateToCallerView();
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);


		
		this.okCancelHorizontalLayout = new HorizontalLayout();
		this.okCancelHorizontalLayout.setMargin(true);
		this.okCancelHorizontalLayout.setSpacing(true);
		this.okCancelHorizontalLayout.setSizeFull();
		this.okCancelHorizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		final HorizontalLayout wrapperHorizontalLayout = new HorizontalLayout();
		wrapperHorizontalLayout.setSpacing(true);		
		if(this.purchaseInvoiceDTO.getId()!=null && this.purchaseInvoiceDTO.getStatus().equals("application.common.status.pending"))
			wrapperHorizontalLayout.addComponent(this.prepareConfirmOrderCheckBox());
		wrapperHorizontalLayout.addComponent(this.okButton);
		wrapperHorizontalLayout.addComponent(cancelButton);		
		this.okCancelHorizontalLayout.addComponent(wrapperHorizontalLayout);
		
    }
    
	private HorizontalLayout prepareConfirmOrderCheckBox(){
		Boolean vBoolean = new Boolean(false);
		final ObjectProperty<Boolean> property =	new ObjectProperty<Boolean>(vBoolean);
		CheckBox vCheckBox = new CheckBox(this.messages.get(this.VIEW_NAME + "tab.purchase.invoice.form.checkbox.confirm.purchase.invoice.caption"), property);
		vCheckBox.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				Boolean vBoolean = (Boolean)event.getProperty().getValue();
				logger.info( "\n=========================="
							+"\n confirm purchaseInvoiceDTO check : " + vBoolean	
							+"\n==========================");
				
				if(vBoolean)purchaseInvoiceDTO.setStatus("application.common.status.confirmed");
				else purchaseInvoiceDTO.setStatus("application.common.status.pending");
			}			
		});
		HorizontalLayout vHorizontalLayout = new HorizontalLayout(vCheckBox);
		//vHorizontalLayout.setCaption(this.messages.get(this.VIEW_NAME + "tab.order.form.checkbox.confirm.order.caption"));
		return vHorizontalLayout;
	}

	@Override
	public void deletePurchaseInvoiceItemDTOFromPreliminaryList(
			PurchaseInvoiceItemDTO vPurchaseInvoiceItemDTO) {
		// TODO Auto-generated method stub
		this.purchaseInvoiceDTO.getListPurchaseInvoiceItemDTO().remove(vPurchaseInvoiceItemDTO);
		this.reSetLayoutAfterAnItemHasBeenAdded();
		this.refreshOkButtonStatus();
	}

	@Override
	public String getPurchaseInvoiceDTOStatus() {
		// TODO Auto-generated method stub
		return this.purchaseInvoiceDTO.getStatus();
	}
}
