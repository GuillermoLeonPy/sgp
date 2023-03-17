package py.com.kyron.sgp.gui.view.comercialmanagement;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyExchangeRateDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.event.DashboardEvent.EditMonedaEvent;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.SgpEvent.RegisterCurrencyViewEvent;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.DashboardViewType;
import py.com.kyron.sgp.gui.view.comercialmanagement.registercurrencyformview.components.CurrencyExchangeRateFormTabLayout;
import py.com.kyron.sgp.gui.view.comercialmanagement.registercurrencyformview.components.CurrencyExchangeRateTableTabLayout;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.ConfirmWindow;
import py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow.Decision;

@SuppressWarnings("serial")
public class RegisterCurrencyFormView extends VerticalLayout implements View {
	private final Logger logger = LoggerFactory.getLogger(RegisterCurrencyFormView.class);
	private SgpForm<CurrencyDTO> monedaForm;
	//private Moneda moneda;
	private Map<String,String> messages;
	private final String VIEW_NAME = "register.currency.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private WebBrowser webBrowser;
	private ComercialManagementService gestionComercialService;
	private ConfirmWindow confirmWindow;
	private CurrencyDTO currencyDTO;
	private boolean editFormMode;
	
	private BussinesSessionUtils bussinesSessionUtils;
	private TabSheet tabs;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private VerticalLayout mainViewLayout;
	private boolean massiveInsertMode;
	private String CALLER_VIEW;
	private Tab currencyExchangeRateFormTabLayoutTab;
	private Tab currencyExchangeRateTableTabLayoutTab;
	private boolean enableCurrencyExchangeRateFormTabLayoutTab;
	private String selectedTabContentComponentId;
	private boolean updatedFlagCurrencyExchangeRateTableTabLayoutTab;
	/**
	 * 
	 */
	public RegisterCurrencyFormView() {
		//crear
		super();
		try{
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			//this.prepareMessages();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
	        this.addStyleName("v-scrollable");
	        this.setHeight("100%");
			
			DashboardEventBus.register(this);
			this.initServices();
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	}//public MonedaRegistroFormView() 

	private void initServices() throws Exception{
		this.gestionComercialService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}//private void initServices() throws Exception
	
	
    @Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void initTitles(){
        setSpacing(true);
        setMargin(true);
        //Label title = new Label(formMode.substring(0, 1).toUpperCase() + formMode.substring(1) + " moneda");
        Label title = new Label
        (!this.editFormMode ? this.messages.get(this.VIEW_NAME + "main.title.register") : this.messages.get(this.VIEW_NAME + "main.title.edit"));
        title.addStyleName("h1");
        this.mainViewLayout.addComponent(title);	
	}//private void initTitles()
	
	private void setUpMainTabForm(CurrencyDTO moneda){
		if(moneda == null){
			this.currencyDTO = new CurrencyDTO();
			//this.monedaForm = new SgpForm<CurrencyDTO>(CurrencyDTO.class, new BeanItem<CurrencyDTO>(this.currencyDTO), "light", true);
		}else{
			this.currencyDTO = moneda;
			//this.monedaForm = new SgpForm<CurrencyDTO>(CurrencyDTO.class, new BeanItem<CurrencyDTO>(moneda), "light", true);
		}
		this.monedaForm = new SgpForm<CurrencyDTO>(CurrencyDTO.class, new BeanItem<CurrencyDTO>(this.currencyDTO), "light", true);
		this.monedaForm.bindAndSetPositionFormLayoutTextField("id_code", this.messages.get(this.VIEW_NAME + "text.field.currency.id.label")/*"Id"*/, true, 25, true,this.messages.get(this.VIEW_NAME + "text.field.required.message"), true);/*"requerido valor"*/
		this.monedaForm.bindAndSetPositionFormLayoutTextField("description", this.messages.get(this.VIEW_NAME + "text.field.currency.description.label")/*"Descripción"*/, true, 50, true,this.messages.get(this.VIEW_NAME + "text.field.required.message"), true);/*"requerido valor"*/
		this.monedaForm.bindAndSetPositionFormLayoutCheckBox("currency_local", this.messages.get(this.VIEW_NAME + "check.box.local.currency.label")/*"Moneda local"*/, "",/*false*/this.currencyDTO.getCurrency_local() == null ? false: this.currencyDTO.getCurrency_local(), true);
		final Button saveButton = new Button(this.messages.get(this.VIEW_NAME + "button.save"));/*"Guardar"*/
		saveButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{

		            		logger.info("\nraw session data: " 
		            		+ gestionComercialService.getBussinesSessionUtils().getRawSessionData().getUserName()
		            		+"\n" + SgpUtils.dateToString(gestionComercialService.getBussinesSessionUtils().getRawSessionData().getStartSessionTime())
		            		+ "\n" + gestionComercialService.getBussinesSessionUtils().getRawSessionData().getUserSessionLocale());		            		
		            		monedaForm.commit();
		            		logger.info("\n 1) guardar moneda en BD\n 2)agregar moneda a lista\n 3)navegacion a RegistrarMonedaView");		            		
		            		logger.info("\nmonedaForm.getItemDataSource().getBean().toString(): " + monedaForm.getItemDataSource().getBean().toString()
		            				+"\ncurrencyDTO : " + currencyDTO);
		            		
		            		if(currencyDTO.getCurrency_local())
		            			setAndShowConfirmWindow();
		            		else{
		            			saveButtonAction();		            	
		            			navigateToRegisterCurrencyView();
		            		}
		            	}catch(Exception e){		            		
		            		//showErrorMessageNotification(e);
		            		showErrorMessageNotification(e);
		            	}
		            }
		        }
			);
		final Button cancelButton = new Button(this.messages.get(this.VIEW_NAME + "button.cancel"));/*"Cancelar"*/
		cancelButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		monedaForm.discard();
		            		logger.info("\n ...navegacion a RegistrarMonedaView...");
		            		navigateToRegisterCurrencyView();
		            	}catch(Exception e){
		            		logger.error("\nerror", e);		            		
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
		this.monedaForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);
	}//private void setUpForm()
	/**
	 * @param children
	 */
	public RegisterCurrencyFormView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
    
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		logger.info("\nMonedaFormView.browserResized(final py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent event)...");
	}
	
    @Subscribe
    public void setMonedaToEdit(final EditMonedaEvent editMonedaEvent){
    	try{
		    	logger.info("\nMonedaFormView.setMonedaToEdit--@Subscribe--editMonedaEvent.getMoneda() : " + editMonedaEvent.getMoneda());
		    	if(editMonedaEvent.getMoneda() != null)this.editFormMode = true;
		    	else this.editFormMode = false;
	    	
				this.initMainViewLayout();
				this.initTitles();
				this.setUpMainTabForm(editMonedaEvent.getMoneda());
				this.setUpcurrencyDataTab();
			    this.setUpCurrencyExchangeRateTableTab();
			    this.editCurrencyExchangeRate(null);
			    this.mainViewLayout.addComponent(this.tabs);
			    this.removeAndReAddMainViewLayout();
			    this.refreshTabSelection();
		}catch(Exception e){
			logger.error("\nerror", e);
		}
    }//public void setMonedaToEdit(final EditMonedaEvent editMonedaEvent)
    
	private void refreshTabSelection(){
		logger.info("\n##############################"
					+"\n refreshing tab selection"
					+"\n##############################");
		//this.tabs.setSelectedTab(1);
		//this.tabs.markAsDirty();
		int compomentCount = this.tabs.getComponentCount();
		for(int counter = (compomentCount - 1); counter >= 0 ; counter--){
			this.tabs.setSelectedTab(counter);
			logger.info("\n tab in position: "+ counter + " has been selected...");
		}
		this.tabs.markAsDirty();
		this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
	}
    private void prepareMessages() throws Exception{    	
    	BussinesSessionUtils bussinesSessionUtils =	(BussinesSessionUtils)SgpApplicationContextAware.getXmlWebApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
    	this.setLocale(bussinesSessionUtils.getRawSessionData().getUserSessionLocale());
    	this.messages = bussinesSessionUtils.getApplicationUtils().loadMessagesByViewAndKeysAndUserSessionLocale(this.VIEW_NAME, this.getLocale());
    }
    
    private String buildExceptionMessage(String generarKeyMessage, Exception e){
    	String message = this.messages.get(VIEW_NAME + generarKeyMessage);
    	if (e instanceof PmsServiceException)
    		message+=": " + e.getMessage();
    	return message;
    }

    private String buildExceptionMessage(Exception e){
    	
    	String message = "";
    	if (e instanceof PmsServiceException)
    		message+=e.getMessage();
    	if(e instanceof CommitException)
    		message+=this.messages.get("application.common.form.error.message");
    	return message;
    }
    private void saveButtonAction() throws PmsServiceException{
    	CurrencyDTO localCurrencyDTO = null;
    	if(!this.editFormMode){
    		localCurrencyDTO = gestionComercialService.registrarMoneda(monedaForm.getItemDataSource().getBean());
			logger.info("\ngestionComercialService.registrarMoneda(monedaForm.getItemDataSource().getBean()) : " + localCurrencyDTO);
    	}else{
    		localCurrencyDTO = gestionComercialService.updateCurrency(this.currencyDTO);
    		logger.info("\ngestionComercialService.registrarMoneda(this.currencyDTO) : " + localCurrencyDTO);
    	}
    }
    
    private void setAndShowConfirmWindow(){
    	final ConfirmWindow window = new ConfirmWindow(this.messages.get("application.common.confirmation.view.title"),
    			this.messages.get(this.VIEW_NAME + "save.currency.action.confirm.dialog.content"), 
    			this.messages.get("application.common.confirmation.view.buttonlabel.yes"),
    			this.messages.get("application.common.confirmation.view.buttonlabel.no"),
    			true);
    	window.setDecision(new Decision() {
				@Override
				public void yes(ClickEvent event) {
		    			// fetch user					
					try{						
						saveButtonAction();
						window.close();
					}catch(Exception e){
						showErrorMessageNotification(e);
						window.resetYesNoAccionFlags();
					}					
				}
				@Override
				public void no(ClickEvent event) {
		    				// do nothing?
					try{					
						window.close();
					}catch(Exception e){
						showErrorMessageNotification(e);
						window.resetYesNoAccionFlags();
					}
				}
    	});
    	window.addShortcutListener(new ShortcutListener(this.messages.get("application.common.confirmation.view.shortcut.close"),ShortcutAction.KeyCode.ESCAPE, null) {
			@Override
			public void handleAction(Object sender, Object target) {
					window.close();
				}
			});
    	window.addCloseListener(new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				logger.info("\nwindow.addCloseListener\nwindow.isYesAccionRealized() : " + window.isYesAccionRealized()
						+"\nwindow.isNoAccionRealized() : " + window.isNoAccionRealized());
				
				if(window.isYesAccionRealized())
					navigateToRegisterCurrencyView();
			}
    		
    	});
    }//private void setAndShowConfirmWindow(){
    
    private void navigateToRegisterCurrencyView(){
		try{
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.REGISTER_CURRENCY.getViewName());
			DashboardEventBus.post(new RegisterCurrencyViewEvent(this.currencyDTO));
			//DashboardEventBus.post(new EditMonedaEvent(moneda));
		}catch(Exception e){
			logger.error(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null));
			logger.error(e.getMessage(), e);
		}
    }
    
    private void showErrorMessageNotification(Exception e){
		logger.error("\nerror", e);		            		
		//Notification.show("Error: ",buildExceptionMessage("button.save.error.message", e), Notification.Type.ERROR_MESSAGE);/*"Favor re ingrese campos indicados"*/
		Notification.show("Error: ",buildExceptionMessage(e), Notification.Type.ERROR_MESSAGE);/*"Favor re ingrese campos indicados"*/
    }
    
	private void initMainViewLayout(){
		this.mainViewLayout = new VerticalLayout();
		this.mainViewLayout.setSpacing(true);
		this.mainViewLayout.setMargin(true);
	}
	
	private void setUpcurrencyDataTab(){
    	this.tabs = new TabSheet();
    	//this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
    	this.tabs.addStyleName("framed");
    	VerticalLayout currencyDataContent = new VerticalLayout();
    	currencyDataContent.setMargin(true);
    	currencyDataContent.setSpacing(true);		
		currencyDataContent.addComponent(this.monedaForm.getSgpFormLayout());    	
        Tab currencyDataTab = this.tabs.addTab(currencyDataContent, this.messages.get(this.VIEW_NAME + "tab.currency.data"));        
        currencyDataTab.setClosable(false);
        currencyDataTab.setEnabled(true);
        currencyDataTab.setIcon(FontAwesome.BOOK);
	}
	
	 private SelectedTabChangeListener setUpTabsSelectionListener(){
	  	return new SelectedTabChangeListener(){
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				// TODO Auto-generated method stub				
				selectedTabContentComponentId =  event.getTabSheet().getSelectedTab().getId();
				logger.info("\ntab selection listener"
						+"\n selectedTabContentComponentId : " + selectedTabContentComponentId);
					
				if(selectedTabContentComponentId!=null && selectedTabContentComponentId.equals("vCurrencyExchangeRateTableTabLayout")
					&& !updatedFlagCurrencyExchangeRateTableTabLayoutTab){
					logger.info("\nupdating asociated exchange rate data source...");
					try {
						updatedFlagCurrencyExchangeRateTableTabLayoutTab = true;
						setUpCurrencyExchangeRateTableTab();						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("\nerror", e);
						commonExceptionErrorNotification.showErrorMessageNotification(e);
					}
				}
			}	    		
	    };
	 }
	 
	 private void setUpCurrencyExchangeRateTableTab() {
		 try{
			 CurrencyExchangeRateTableTabLayout vCurrencyExchangeRateTableTabLayout = new CurrencyExchangeRateTableTabLayout(this, this.currencyDTO);
			 vCurrencyExchangeRateTableTabLayout.setId("vCurrencyExchangeRateTableTabLayout");
		       
	    	if(this.currencyExchangeRateTableTabLayoutTab!=null)this.tabs.removeTab(this.currencyExchangeRateTableTabLayoutTab);
	    	
		    this.currencyExchangeRateTableTabLayoutTab = 
		        		this.tabs.addTab(vCurrencyExchangeRateTableTabLayout, 
		        				this.messages.get(this.VIEW_NAME + "tab.exchangerate.table"), 
		        				FontAwesome.REGISTERED, 
		        				1);
		    this.currencyExchangeRateTableTabLayoutTab.setClosable(false);
		    this.currencyExchangeRateTableTabLayoutTab.setEnabled(!this.currencyDTO.getCurrency_local());
		    if(this.updatedFlagCurrencyExchangeRateTableTabLayoutTab){
		      	this.tabs.setSelectedTab(1);
		      	this.updatedFlagCurrencyExchangeRateTableTabLayoutTab = false;
		    }
		}catch(Exception e){
			logger.error("\nerror", e);
		}
	 }//private void setUpCredentialsDataTab(){
	 
	 
	 public void editCurrencyExchangeRate(CurrencyExchangeRateDTO vCurrencyExchangeRateDTO) throws PmsServiceException,Exception{
	  	CurrencyExchangeRateFormTabLayout vCurrencyExchangeRateFormTabLayout = 
	   			new CurrencyExchangeRateFormTabLayout
	   			(this,
	   			vCurrencyExchangeRateDTO, 
	   			vCurrencyExchangeRateDTO!=null && vCurrencyExchangeRateDTO.getId()!=null);
	  	vCurrencyExchangeRateFormTabLayout.setId("vCurrencyExchangeRateFormTabLayout");
	  	if(this.currencyExchangeRateFormTabLayoutTab!=null)this.tabs.removeTab(this.currencyExchangeRateFormTabLayoutTab);
	   	this.currencyExchangeRateFormTabLayoutTab = this.tabs.addTab(vCurrencyExchangeRateFormTabLayout, this.messages.get(this.VIEW_NAME + "tab.exchangerate.register.form"));
	   	this.currencyExchangeRateFormTabLayoutTab.setIcon(FontAwesome.MONEY);
	    this.currencyExchangeRateFormTabLayoutTab.setClosable(false);
	        
	    if(vCurrencyExchangeRateDTO!=null && vCurrencyExchangeRateDTO.getId()!=null)
	       	this.enableCurrencyExchangeRateFormTabLayoutTab = true;
	    else
	      	this.setEnableCurrencyExchangeRateDTOFormFlag();
	    this.currencyExchangeRateFormTabLayoutTab.setEnabled(
	    		this.editFormMode	
	    		&& this.enableCurrencyExchangeRateFormTabLayoutTab
	    		&& !this.currencyDTO.getCurrency_local());
	        
	    this.currencyExchangeRateFormTabLayoutTab.setVisible(
	       		this.bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
	       		.contains(this.SECURED_PROGRAM_PREFIX + this.VIEW_NAME + "tab.exchangerate.register.form"));
	        
	    if(vCurrencyExchangeRateDTO!=null && vCurrencyExchangeRateDTO.getId()!=null)
	       	this.tabs.setSelectedTab(2);
	 }
	 
	 private void setEnableCurrencyExchangeRateDTOFormFlag() throws PmsServiceException{
		 Long id = this.gestionComercialService.getCurrencyExchangeRateValidRowId(this.currencyDTO.getId());
		 if(id != null)this.enableCurrencyExchangeRateFormTabLayoutTab = false; else this.enableCurrencyExchangeRateFormTabLayoutTab = true;
	 }
	 
	private void removeAndReAddMainViewLayout(){
		this.removeAllComponents();
		this.addComponent(this.mainViewLayout);
	}
	
    public void saveButtonActionCurrencyExchangeRateDTOFormTab(CurrencyExchangeRateDTO vCurrencyExchangeRateDTO,boolean currencyExchangeRateFormTabLayoutEditMode) throws PmsServiceException,Exception{

    	if(!currencyExchangeRateFormTabLayoutEditMode){
    		vCurrencyExchangeRateDTO.setId_currency(this.currencyDTO.getId());
    		this.gestionComercialService.insertCurrencyExchangeRateDTO(vCurrencyExchangeRateDTO);
    	}else    		
    		this.gestionComercialService.updateCurrencyExchangeRateDTO(vCurrencyExchangeRateDTO);
    	
    	this.editCurrencyExchangeRate(null);
    	this.updateStatusCurrencyExchangeRateDTOFormTab();
		this.commonExceptionErrorNotification.showCommonApplicationSuccesfulOperationNotificaction();

	}
	
	public void updateStatusCurrencyExchangeRateDTOFormTab() throws PmsServiceException{
		this.setEnableCurrencyExchangeRateDTOFormFlag();
		this.tabs.setSelectedTab(1);
		this.currencyExchangeRateFormTabLayoutTab.setEnabled(this.editFormMode && this.enableCurrencyExchangeRateFormTabLayoutTab);
		this.tabs.markAsDirty();    	
	}
	

}//public class MonedaRegistroFormView extends VerticalLayout implements View 
