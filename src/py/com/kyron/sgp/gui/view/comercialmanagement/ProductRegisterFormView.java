/**
 * 
 */
package py.com.kyron.sgp.gui.view.comercialmanagement;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.ProductDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.ProductRegisterFormViewEvent;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * @author testuser
 *
 */
@SuppressWarnings("serial")
public class ProductRegisterFormView extends VerticalLayout implements View {

	private final Logger logger = LoggerFactory.getLogger(ProductRegisterFormView.class);
	private SgpForm<ProductDTO> productDTOForm;
	private Map<String,String> messages;
	private final String VIEW_NAME = "product.register.form.";
	//private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private boolean editFormMode;
	private ProductDTO productDTO;
	private ComercialManagementService comercialManagementService;
	private TabSheet tabs;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private VerticalLayout mainViewLayout;
	private boolean massiveInsertMode;
	private String CALLER_VIEW;
	
	/**
	 * 
	 */
	public ProductRegisterFormView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\nMachineRegisterFormView..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();			
	        this.addStyleName("v-scrollable");
	        this.setHeight("100%");
	        
	        Responsive.makeResponsive(this);
			DashboardEventBus.register(this);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	private void initServices() throws Exception{
		this.comercialManagementService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
	}
	/**
	 * @param children
	 */
	public ProductRegisterFormView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
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
	
	@Subscribe
	public void setProductToEdit(final ProductRegisterFormViewEvent productRegisterFormViewEvent){
		try{
			this.CALLER_VIEW = productRegisterFormViewEvent.getCallerView();
			this.massiveInsertMode = productRegisterFormViewEvent.isMassiveInsertMode();
			this.editFormMode = productRegisterFormViewEvent.getProductDTO()!=null;
			this.initMainViewLayout();
			this.initTitles();
			this.setUpMainTabForm(productRegisterFormViewEvent.getProductDTO());
			this.setUpProductDataTab();
		    this.mainViewLayout.addComponent(this.tabs);
		    this.removeAndReAddMainViewLayout();
		}catch(Exception e){
			logger.error("\nerror", e);
		}		
	}

	private void initMainViewLayout(){
		this.mainViewLayout = new VerticalLayout();
		this.mainViewLayout.setSpacing(true);
		this.mainViewLayout.setMargin(true);
	}
	
	private void initTitles(){
		try{
			Label title = new Label(!this.editFormMode ? this.messages.get(this.VIEW_NAME + "main.title.register") : this.messages.get(this.VIEW_NAME + "main.title.edit"));
        	title.addStyleName("h1");
        	this.mainViewLayout.addComponent(title);
		}catch(Exception e){
			commonExceptionErrorNotification.showErrorMessageNotification(e);
		}
	}
	
	private void setUpMainTabForm(ProductDTO productDTO){
		this.productDTO = (productDTO == null ? new ProductDTO() : productDTO);
		if(this.editFormMode)this.printProductDTOToEdit();
		this.productDTOForm = new SgpForm<ProductDTO>(ProductDTO.class, new BeanItem<ProductDTO>(this.productDTO), "light", true);
		this.productDTOForm.bindAndSetPositionFormLayoutTextField("product_id", this.messages.get(this.VIEW_NAME + "text.field.product.id.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "text.field.product.id.required.message"), true);
		this.productDTOForm.bindAndSetPositionFormLayoutTextField("product_description", this.messages.get(this.VIEW_NAME + "text.field.product.description.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "text.field.product.description.required.message"), true);
		this.productDTOForm.bindAndSetPositionFormLayoutTextField("increase_over_cost_for_sale_price", this.messages.get(this.VIEW_NAME + "text.field.increase.over.cost.for.sale.price.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "text.field.increase.over.cost.for.sale.price.required.message"), true);
	}
	
	private void printProductDTOToEdit(){
		logger.info("\n**********\nProductDTOToEdit\n**********\nthis.productDTO.toString() : \n" + this.productDTO.toString());

	}
	
	private void setUpProductDataTab(){
    	this.tabs = new TabSheet();    	
    	this.tabs.addStyleName("framed");
    	VerticalLayout productDataContent = new VerticalLayout();
    	productDataContent.setMargin(true);
    	productDataContent.setSpacing(true);
    	
        final Button saveButton = new Button(this.messages.get("application.common.button.save.label"));/*"Guardar"*/
        saveButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{            		
		            		productDTOForm.commit();
		            		logger.info("\nproductDTO.toString():\n" + productDTO.toString());
		            		saveButtonAction();
		            	}catch(Exception e){		
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);
		            	}
		            }
		        }
			);
        
        saveButton.setEnabled(true);
        
		final Button cancelButton = new Button(this.messages.get("application.common.button.cancel.label"));/*"Cancelar"*/
		cancelButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		productDTOForm.discard();
		            		navigateToCallerView();
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		HorizontalLayout okCancelHorizontalLayout = new HorizontalLayout();
		okCancelHorizontalLayout.setMargin(new MarginInfo(true, false, true, false));
		okCancelHorizontalLayout.setSpacing(true);
		okCancelHorizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		okCancelHorizontalLayout.addComponent(saveButton);
		okCancelHorizontalLayout.addComponent(cancelButton);
		
		this.productDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);
		productDataContent.addComponent(this.productDTOForm.getSgpFormLayout());    	
        Tab productDataTab = this.tabs.addTab(productDataContent, this.messages.get(this.VIEW_NAME + "tab.product.data"));
        
        productDataTab.setClosable(false);
        productDataTab.setEnabled(true);
        //TestIcon testIcon = new TestIcon(60);
        //productDataTab.setIcon(testIcon.get(false));
        productDataTab.setIcon(FontAwesome.SHOPPING_CART);
	}
	
    private void saveButtonAction() throws PmsServiceException{
    	if(!this.editFormMode)
    		this.comercialManagementService.insertProductDTO(this.productDTO);
    	else{
    		logger.info("\nhere call update method, then return to caller view, because its a fast edit");
    		this.navigateToCallerView();
    	}
    	
		commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();
		
		if(!this.editFormMode && this.massiveInsertMode) 
			this.restartFormAfterSuccesfulInsertOperation();    	
    }
    
	private void navigateToCallerView(){
		logger.info("\nProductRegisterFormView.navigateToCallerView()\nthis.CALLER_VIEW : " + this.CALLER_VIEW
				+"\n==================================================================================================="
				+"\n no error araised by posting an event to the event bus when the destination view does not subscribe"
				+"\n to listen a method with the event param"
				+"\n===================================================================================================");
		try{
			UI.getCurrent().getNavigator().navigateTo(this.CALLER_VIEW);
			DashboardEventBus.post(new ProductRegisterFormViewEvent(this.productDTO, this.CALLER_VIEW, false /*always return false from this view*/));
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}
	
	private void restartFormAfterSuccesfulInsertOperation(){
		this.initMainViewLayout();
		this.editFormMode = false;
	    this.initTitles();
	    this.setUpMainTabForm(null);
	    this.setUpProductDataTab();
	    this.mainViewLayout.addComponent(this.tabs);
	    this.removeAndReAddMainViewLayout();
	}
	
	private void removeAndReAddMainViewLayout(){
		this.removeAllComponents();
		this.addComponent(this.mainViewLayout);
	}
}
