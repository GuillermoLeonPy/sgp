package py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
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
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class RawMaterialFinderWindow extends Window {

	private final Logger logger = LoggerFactory.getLogger(RawMaterialFinderWindow.class);
	private final String VIEW_NAME = "raw.material.finder.window.";
	private Map<String,String> messages;
	private VerticalLayout mainViewLayout;
	private RawMaterialDTO rawMaterialDTO;
	private SgpForm<RawMaterialDTO> rawMaterialDTOForm;
	private RawMaterialDTO rawMaterialDTOTableRowSelected;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private Table rawMaterialDTOTable;
	private List<RawMaterialDTO> listRawMaterialDTO;
	private ProductionManagementService productionManagementService;
	private final List<ShortcutListener> listShortcutListener;
	
	public RawMaterialFinderWindow() {
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
			this.setUpRawMaterialDTOForm();
			this.setContent(this.mainViewLayout);
			Responsive.makeResponsive(this);
			UI.getCurrent().addWindow(this);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public RawMaterialFinderWindow(String caption) {
		super(caption);
		// TODO Auto-generated constructor stub
	}*/

	/*public RawMaterialFinderWindow(String caption, Component content) {
		super(caption, content);
		// TODO Auto-generated constructor stub
	}*/
	
	private void initServices() throws Exception{
		this.productionManagementService = (ProductionManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PRODUCTION_MANAGEMENT_SERVICE);
	}//private void initServices() throws Exception
	
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
	
	private void setUpRawMaterialDTOForm(){
		this.rawMaterialDTO = new RawMaterialDTO();
		this.rawMaterialDTOForm = new SgpForm<RawMaterialDTO>(RawMaterialDTO.class, new BeanItem<RawMaterialDTO>(this.rawMaterialDTO),ValoTheme.FORMLAYOUT_LIGHT/*"light"*/, true);
		this.initTitles();	
		this.rawMaterialDTOForm.bindAndSetPositionFormLayoutTextField("raw_material_id", this.messages.get("application.common.identifier.label")/**/, false, 90, false,null, true, this.setUpTextFieldFocusListener(),this.setUpTextFieldBlurListener());
		this.rawMaterialDTOForm.bindAndSetPositionFormLayoutTextField("raw_material_description", this.messages.get("application.common.description.label")/**/, false, 90, false,null, true, this.setUpTextFieldFocusListener(),this.setUpTextFieldBlurListener());
		
		 HorizontalLayout root = new HorizontalLayout();
	        
	        root.setWidth(100.0f, Unit.PERCENTAGE);
	        root.setSpacing(true);
	        root.setMargin(true);
	        root.addStyleName("profile-form");
	        root.addComponent(this.rawMaterialDTOForm.getSgpFormLayout());
	        root.setExpandRatio(this.rawMaterialDTOForm.getSgpFormLayout(), 1);
		this.mainViewLayout.addComponent(root);
	}
	
	private void initTitles(){
		Label title = new Label(this.messages.get(this.VIEW_NAME + "form.main.title"));
        title.addStyleName(ValoTheme.LABEL_H4);
        title.addStyleName(ValoTheme.LABEL_COLORED);
        this.rawMaterialDTOForm.getSgpFormLayout().addComponent(title);
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
		    		rawMaterialDTOForm.commit();
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
		             rawMaterialDTOForm.discard();
	         	}catch(Exception e){		
	        		commonExceptionErrorNotification.showErrorMessageNotification(e);
	        	}
	         }
	     };
	}

	private void doSearch() throws PmsServiceException{
		logger.info("\n*********************** search product filter"
					+"\n" + this.rawMaterialDTO
					+"\n***********************");
		this.listRawMaterialDTO = this.productionManagementService.listRawMaterialDTO(this.rawMaterialDTO);
		if(this.rawMaterialDTOTable!=null)this.mainViewLayout.removeComponent(this.rawMaterialDTOTable);
		if(this.listRawMaterialDTO!=null && !this.listRawMaterialDTO.isEmpty()){
			this.buildrawMaterialDTOTable();
			this.mainViewLayout.addComponent(this.rawMaterialDTOTable);
		}else
			throw new PmsServiceException("py.com.kyron.sgp.persistence.common.search.no.results.found.error", null, null);
	}
	
	 private void buildrawMaterialDTOTable() throws PmsServiceException{
	    	this.rawMaterialDTOTable = new Table();
	    	BeanItemContainer<RawMaterialDTO> RawMaterialDTOBeanItemContainer	= new BeanItemContainer<RawMaterialDTO>(RawMaterialDTO.class);
	    	RawMaterialDTOBeanItemContainer.addAll(this.listRawMaterialDTO);
	    	this.rawMaterialDTOTable.setContainerDataSource(RawMaterialDTOBeanItemContainer);	    	
	    	this.rawMaterialDTOTable.setVisibleColumns(new Object[] {"raw_material_id","raw_material_description"});
	    	this.rawMaterialDTOTable.setColumnHeader("raw_material_id", this.messages.get("application.common.rawmaterialid.label"));
	    	this.rawMaterialDTOTable.setColumnAlignment("raw_material_id", Table.Align.LEFT);
	    	this.rawMaterialDTOTable.setColumnHeader("raw_material_description", this.messages.get("application.common.description.label"));
	    	this.rawMaterialDTOTable.setColumnAlignment("raw_material_description", Table.Align.LEFT);
	    	this.rawMaterialDTOTable.setSizeFull();
	    	//this.personDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
	    	this.rawMaterialDTOTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
	    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
	    	this.rawMaterialDTOTable.addStyleName(ValoTheme.TABLE_SMALL);
	    	this.rawMaterialDTOTable.setColumnExpandRatio("raw_material_id", 0.1f);    	
	    	this.rawMaterialDTOTable.setColumnExpandRatio("raw_material_description", 0.1f);
	    	this.rawMaterialDTOTable.setSelectable(true);
	    	this.rawMaterialDTOTable.setColumnCollapsingAllowed(true);
	    	this.rawMaterialDTOTable.setColumnCollapsible("raw_material_id", false);
	    	//this.rolesTable.setColumnCollapsible("role_description", false);
	    	//this.personDTOTable.setSortContainerPropertyId("commercial_name");
	    	//this.personDTOTable.setSortAscending(false);
	    	this.rawMaterialDTOTable.setColumnReorderingAllowed(true);
	    	this.rawMaterialDTOTable.setFooterVisible(true);
	    	this.rawMaterialDTOTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	    	this.rawMaterialDTOTable.addShortcutListener(this.buildTableEnterKeyShortcutListener());
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
		    			rawMaterialDTOForm.commit();
		    			rawMaterialDTOTableRowSelected = null;
			    		final Table table = (Table)target;
			    		rawMaterialDTOTableRowSelected = (RawMaterialDTO)table.getValue();
			    		if(rawMaterialDTOTableRowSelected!=null){			    						    		
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
	 * @return the rawMaterialDTOTableRowSelected
	 */
	public RawMaterialDTO getRawMaterialDTOTableRowSelected() {
		return rawMaterialDTOTableRowSelected;
	}

	/**
	 * @param rawMaterialDTOTableRowSelected the rawMaterialDTOTableRowSelected to set
	 */
	public void setRawMaterialDTOTableRowSelected(
			RawMaterialDTO rawMaterialDTOTableRowSelected) {
		this.rawMaterialDTOTableRowSelected = rawMaterialDTOTableRowSelected;
	}
}
