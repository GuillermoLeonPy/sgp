package py.com.kyron.sgp.gui.view.comercialmanagement.orderregisterformview.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.ProductDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ProductFinderWindow extends Window {

	private final Logger logger = LoggerFactory.getLogger(ProductFinderWindow.class);
	private final String VIEW_NAME = "product.finder.window.";
	private Map<String,String> messages;
	private VerticalLayout mainViewLayout;
	private ProductDTO productDTO;
	private SgpForm<ProductDTO> productDTOForm;
	private ProductDTO productDTOTableRowSelected;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Table productDTOTable;
	private List<ProductDTO> listProductDTO;
	private ComercialManagementService comercialManagementService;
	private final List<ShortcutListener> listShortcutListener;
	
	public ProductFinderWindow() {
		// TODO Auto-generated constructor stub
		this.listShortcutListener = this.buildSearchShortcutListeners();
		try{
			
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			//this.addStyleName("v-scrollable");
			this.addStyleName("profile-window");
			this.setUpWindowCaptionAndMainTitle();
			this.initServices();
			this.initMainViewLayout();
			this.setPersonDTOForm();
			this.setContent(this.mainViewLayout);
			Responsive.makeResponsive(this);
			UI.getCurrent().addWindow(this);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public ProductFinderWindow(String caption) {
		super(caption);
		// TODO Auto-generated constructor stub
	}

	public ProductFinderWindow(String caption, Component content) {
		super(caption, content);
		// TODO Auto-generated constructor stub
	}*/
	
	private void initServices() throws Exception{
		this.comercialManagementService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
	}//private void initServices() throws Exception
	
	private List<ShortcutListener> buildSearchShortcutListeners(){
		ShortcutListener enterShortcutListener =  new ShortcutListener("Enter", KeyCode.ENTER, null) {
	    	 @Override
	         public void handleAction(final Object sender, final Object target) {
	    		try{
		    		 logger.info("\n*************************************"
		    				 	+"\nENTER LISTENER"
		    				 	+"\nsender class: "+sender.getClass()
		    				 	+"\ntarget class: "+target.getClass());
		    		final TextField vTextField = (TextField)target;
		    		logger.info("\nsender text field value: "+vTextField.getValue()
		    					+"\n*************************************");
		    		productDTOForm.commit();
	    			doSearch();
	         	}catch(Exception e){		
	        		commonExceptionErrorNotification.showErrorMessageNotification(e);
	        	}
	         }
	     };
	     List<ShortcutListener> listShortcutListener = new ArrayList<ShortcutListener>();
	     listShortcutListener.add(enterShortcutListener);
	     listShortcutListener.add(this.buildEscapeKeyShortcutListener());
	     return listShortcutListener;
	}
	
	private ShortcutListener buildEscapeKeyShortcutListener(){
		return new ShortcutListener("Clear", KeyCode.ESCAPE, null) {
	    	 @Override
	         public void handleAction(final Object sender, final Object target) {
	    		 try{
		    		 logger.info("\n*************************************"
		    				 	+"\nESCAPE LISTENER"
		    				 	+"\nsender class: "+sender.getClass()
		    				 	+"\ntarget class: "+target.getClass());
		            final TextField vTextField = (TextField)target;
			    	logger.info("\nsender text field value: "+vTextField.getValue()
		    					+"\n*************************************");
		             vTextField.setValue("");
		             productDTOForm.discard();
	         	}catch(Exception e){		
	        		commonExceptionErrorNotification.showErrorMessageNotification(e);
	        	}
	         }
	     };
	}
	
	private void setUpWindowCaptionAndMainTitle(){
		this.setCaption(this.messages.get(this.VIEW_NAME + "caption"));
		this.setModal(true);
		this.setClosable(true);
		this.center();
		this.setSizeFull();//
		this.setResizable(true);
        this.setIcon(FontAwesome.PRODUCT_HUNT);
	}
	
	private void initMainViewLayout(){
		this.mainViewLayout = new VerticalLayout();
		this.mainViewLayout.setSpacing(true);
		this.mainViewLayout.setMargin(true);
		//this.mainViewLayout.addStyleName("v-scrollable");
		this.mainViewLayout.setSizeFull();
		this.mainViewLayout.setMargin(new MarginInfo(true, false, false, false));
		this.mainViewLayout.setIcon(FontAwesome.COGS);
	}
	
	private void setPersonDTOForm(){
		this.productDTO = new ProductDTO(null,1L);
		this.productDTOForm = new SgpForm<ProductDTO>(ProductDTO.class, new BeanItem<ProductDTO>(this.productDTO), ValoTheme.FORMLAYOUT_LIGHT/*"light"*/, true);
		this.initTitles();		
		this.productDTOForm.bindAndSetPositionFormLayoutTextField("product_id", this.messages.get("application.common.identifier.label")/**/, true, 90, false,null, true, this.setUpTextFieldFocusListener(),this.setUpTextFieldBlurListener());
    	this.productDTOForm.bindAndSetPositionFormLayoutTextField("product_description", this.messages.get("application.common.description.label")/**/, true, 90, false,null, true, this.setUpTextFieldFocusListener(),this.setUpTextFieldBlurListener());
    	this.productDTOForm.bindAndSetPositionFormLayoutTextField("orderItemQuantity", this.messages.get("application.common.quantity.label")/**/, true, 90, true,this.messages.get("application.common.quantity.tex.field.required.message"), true);
		
		
		 HorizontalLayout root = new HorizontalLayout();
	        
	        root.setWidth(100.0f, Unit.PERCENTAGE);
	        root.setSpacing(true);
	        root.setMargin(true);
	        root.addStyleName("profile-form");
	        root.addComponent(this.productDTOForm.getSgpFormLayout());
	        root.setExpandRatio(this.productDTOForm.getSgpFormLayout(), 1);
		this.mainViewLayout.addComponent(root);
	}
	
	private void initTitles(){
		Label title = new Label(this.messages.get(this.VIEW_NAME + "form.main.title"));
        title.addStyleName(ValoTheme.LABEL_H4);
        title.addStyleName(ValoTheme.LABEL_COLORED);
        this.productDTOForm.getSgpFormLayout().addComponent(title);
	}
	
	private FocusListener setUpTextFieldFocusListener(){
		return new FocusListener(){

			@Override
			public void focus(FocusEvent event) {
				// TODO Auto-generated method stub
				logger.info("\n************************"
						+"\n focus listener in text field"
						+"\n adding enter and escape keys shortcut listeners"
						+"\n************************");
				final TextField textField = (TextField)event.getSource();
				//final List<ShortcutListener> listShortcutListener = buildSearchShortcutListeners();
				for(ShortcutListener vShortcutListener : listShortcutListener)
					textField.addShortcutListener(vShortcutListener);
			}
			
		};
	}
	
	private BlurListener setUpTextFieldBlurListener(){
		return new BlurListener(){

			@Override
			public void blur(BlurEvent event) {
				// TODO Auto-generated method stub
				logger.info("\n************************"
							+"\n blur listener in text field"
							+"\n removing enter and escape keys shortcut listeners"
							+ "\n object class type: "+event.getSource().getClass());
				final TextField textField = (TextField)event.getSource();
				int listenersCount = 0;
				for(ShortcutListener vShortcutListener : listShortcutListener){
					listenersCount++;
					textField.removeListener(ShortcutListener.class, vShortcutListener);
					textField.removeShortcutListener(vShortcutListener);
				}
				logger.info("\n-------------------------"
							+"\n removed listeners count : " + listenersCount
							+"\n************************");
			}
			
		};
	}
	
	public void adjuntWindowSizeAccordingToCientDisplay(){
		logger.info("\n*************************************"
					+"\n adjusting window width and height according to client display size"
					+"\n browser width: " + Page.getCurrent().getBrowserWindowWidth()
					+"\n browser height: " + Page.getCurrent().getBrowserWindowHeight()
					+"\n*************************************");
		if(Page.getCurrent().getBrowserWindowWidth() < 800)
			this.setSizeFull();
		else{
			this.setWidth("650px");
			this.setHeight("800px");
		}			
	}
	
	private void doSearch() throws PmsServiceException{
		logger.info("\n*********************** search product filter"
					+"\n" + this.productDTO.toString()
					+"\n***********************");
		this.listProductDTO = this.comercialManagementService.listProductDTO(this.productDTO);
		if(this.productDTOTable!=null)this.mainViewLayout.removeComponent(this.productDTOTable);
		if(this.listProductDTO!=null && !this.listProductDTO.isEmpty()){
			this.buildpersonDTOTable();
			this.mainViewLayout.addComponent(this.productDTOTable);
		}else
			throw new PmsServiceException("py.com.kyron.sgp.persistence.common.search.no.results.found.error", null, null);
	}
	
	 private void buildpersonDTOTable() throws PmsServiceException{
	    	this.productDTOTable = new Table();
	    	BeanItemContainer<ProductDTO> ProductDTOBeanItemContainer	= new BeanItemContainer<ProductDTO>(ProductDTO.class);
	    	ProductDTOBeanItemContainer.addAll(this.listProductDTO);
	    	this.productDTOTable.setContainerDataSource(ProductDTOBeanItemContainer);	    	
	    	this.productDTOTable.setVisibleColumns(new Object[] {"product_id","product_description"});
	    	this.productDTOTable.setColumnHeader("product_id", this.messages.get("application.common.product.label"));
	    	this.productDTOTable.setColumnAlignment("product_id", Table.Align.LEFT);
	    	this.productDTOTable.setColumnHeader("product_description", this.messages.get("application.common.description.label"));
	    	this.productDTOTable.setColumnAlignment("product_description", Table.Align.LEFT);
	    	this.productDTOTable.setSizeFull();
	    	//this.personDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
	    	this.productDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
	    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
	    	this.productDTOTable.addStyleName(ValoTheme.TABLE_SMALL);
	    	this.productDTOTable.setColumnExpandRatio("product_id", 0.1f);    	
	    	this.productDTOTable.setColumnExpandRatio("product_description", 0.1f);
	    	this.productDTOTable.setSelectable(true);
	    	this.productDTOTable.setColumnCollapsingAllowed(true);
	    	this.productDTOTable.setColumnCollapsible("product_id", false);
	    	//this.rolesTable.setColumnCollapsible("role_description", false);
	    	//this.personDTOTable.setSortContainerPropertyId("commercial_name");
	    	//this.personDTOTable.setSortAscending(false);
	    	this.productDTOTable.setColumnReorderingAllowed(true);
	    	this.productDTOTable.setFooterVisible(true);
	    	this.productDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	    	this.productDTOTable.addShortcutListener(this.buildTableEnterKeyShortcutListener());
	 }//private void buildMachinesTable(){
	 
	 private ShortcutListener buildTableEnterKeyShortcutListener(){
		 return new ShortcutListener("Enter", KeyCode.ENTER, null) {
	    	 @Override
	         public void handleAction(final Object sender, final Object target) {
	    		try{
		    		 logger.info("\n*************************************"
		    				 	+"\nTABLE ENTER LISTENER"
		    				 	+"\nsender class: "+sender.getClass()
		    				 	+"\ntarget class: "+target.getClass());
		    		if (target instanceof Table){
			    		productDTOForm.commit();
			    		productDTOTableRowSelected = null;
			    		final Table table = (Table)target;
			    		productDTOTableRowSelected = (ProductDTO)table.getValue();
			    		if(productDTOTableRowSelected!=null){
			    			productDTOTableRowSelected.setOrderItemQuantity(productDTO.getOrderItemQuantity());			    		
			    			close();
			    		}
		    		}
	         	}catch(Exception e){		
	        		commonExceptionErrorNotification.showErrorMessageNotification(e);
	        	}
	         }
	     };
	 }

	/**
	 * @return the productDTOTableRowSelected
	 */
	public ProductDTO getProductDTOTableRowSelected() {
		return productDTOTableRowSelected;
	}

	/**
	 * @param productDTOTableRowSelected the productDTOTableRowSelected to set
	 */
	public void setProductDTOTableRowSelected(ProductDTO productDTOTableRowSelected) {
		this.productDTOTableRowSelected = productDTOTableRowSelected;
	}
}
