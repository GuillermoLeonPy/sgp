package py.com.kyron.sgp.gui.view.productionmanagement.productionprocessmanagementoperationview.components;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.ProductDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.productionmanagement.ProductionProcessManagementOperationView;

import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ProductDTOTabLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(ProductDTOTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "production.process.management.operation.";
	private final ProductDTO productDTO;
	private SgpForm<ProductDTO> productDTOForm;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final ProductionProcessManagementOperationView productionProcessManagementOperationView;
	
	public ProductDTOTabLayout(ProductionProcessManagementOperationView productionProcessManagementOperationView, ProductDTO productDTO) {
		// TODO Auto-generated constructor stub
		this.productDTO = productDTO;
		this.productionProcessManagementOperationView = productionProcessManagementOperationView;
		try{
			logger.info("\n ProductDTOTabLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();			
	        //this.addStyleName("v-scrollable");
	        //this.setHeight("100%");	        
	        Responsive.makeResponsive(this);
			DashboardEventBus.register(this);
			this.setSpacing(true);
			this.setMargin(true);
			this.setUpLayoutContent();
			
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public ProductDTOTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/

	private void initTitles(){
		Label title = new Label(this.messages.get("application.common.product.label"));
        title.addStyleName("h1");
        this.addComponent(title);

	}
	
	private void setUpProductDTOForm(){
		this.productDTOForm = new SgpForm<ProductDTO>(ProductDTO.class, new BeanItem<ProductDTO>(this.productDTO), /*ValoTheme.FORMLAYOUT_LIGHT*/"light", true);
		this.productDTOForm.bindAndSetPositionFormLayoutTextField("product_id", this.messages.get(this.VIEW_NAME + "tab.product.data.text.field.product.id.label")/**/, true, 80, true,null, false);
		this.productDTOForm.bindAndSetPositionFormLayoutTextField("product_description", this.messages.get(this.VIEW_NAME + "tab.product.data.text.field.product.description.label")/**/, true, 85, true,null, false);
	}
	
	private void setUpLayoutContent(){
		this.initTitles();
    	this.setUpProductDTOForm();      
		final Button cancelButton = new Button(this.messages.get("application.common.button.cancel.label"));/*"Cancelar"*/
		cancelButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		productDTOForm.discard();
		            		productionProcessManagementOperationView.navigateToCallerView(DashboardViewType.PRODUCTION_PROCESS_MANAGEMENT.getViewName());
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
		okCancelHorizontalLayout.addComponent(cancelButton);		
		this.productDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);
		this.addComponent(this.productDTOForm.getSgpFormLayout());
	}
	

	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n ProductDTOTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
}
