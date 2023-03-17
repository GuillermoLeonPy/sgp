package py.com.kyron.sgp.gui.view.comercialmanagement.orderregisterformview.components;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyExchangeRateDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderItemDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.ProductDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.personmanagement.service.PersonManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.comercialmanagement.OrderRegisterFormView;
import py.com.kyron.sgp.gui.view.utils.commponents.personmanagement.PersonFinderWindow;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class OrderDTOTabLayout extends VerticalLayout implements OrderItemDTOTableFunctions{

	private final Logger logger = LoggerFactory.getLogger(OrderDTOTabLayout.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME = "order.management.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private final OrderDTO orderDTO;
	private final OrderRegisterFormView orderRegisterFormView;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private ComercialManagementService comercialManagementService;
	private PersonManagementService personManagementService;
	private PersonDTO personDTO;
	private SgpForm<PersonDTO> personDTOForm;
	private VerticalLayout personDTOFormVerticalLayout;
	private Button searchCustomerButton;
	private PersonFinderWindow personFinderWindow;
	private ComboBox listCurrencyDTOComboBox;
	private OptionGroup paymentConditionOptionGroup;
	private HorizontalLayout currencyPaymentConditionHorizontalLayout;
	private OrderItemDTOTableLayout orderItemDTOTableLayout;
	private OrderItemDTOTableTotalsLayout orderItemDTOTableTotalsLayout;
	private Button okButton;
	private HorizontalLayout okCancelHorizontalLayout;
	private HorizontalLayout firstRowElementInLayout;
	private ProductFinderWindow productFinderWindow;
	private CreditOrderCriteriaLayout creditOrderCriteriaLayout;
	private List<OrderItemDTO> vListOrderItemDTO;
	private BussinesSessionUtils bussinesSessionUtils;
	
	public OrderDTOTabLayout(final OrderRegisterFormView orderRegisterFormView,final OrderDTO orderDTO) {
		// TODO Auto-generated constructor stub
		this.orderRegisterFormView = orderRegisterFormView;
		this.orderDTO = orderDTO;
		try{
			logger.info("\n OrderDTOTabLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
	        this.setSizeFull();
	        //this.addStyleName("v-scrollable");
	        //this.setHeight("100%");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	private void initServices() throws Exception{
		this.comercialManagementService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
		this.personManagementService = (PersonManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.PERSON_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	/*public OrderDTOTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n OrderDTOTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void setUpLayoutContent() throws PmsServiceException {
		this.addComponent(this.setUpOrderNumberIndicator());
		this.setUpPersonFormVerticalLayout(this.orderDTO.getPersonDTO());
		this.creditOrderCriteriaLayout = new CreditOrderCriteriaLayout(this,this.orderDTO);
		this.creditOrderCriteriaLayout.setVisible(this.orderDTO.getCredit_order_payment_condition_surcharge_percentage()!=null);
		this.setUpCurrencyPaymentConditionHorizontalLayout();
		
		logger.info( "\n==========================================="
					+"\n payment condition affects the item total"
					+"\n===========================================");
		if(this.orderDTO.getListOrderItemDTO()==null)this.orderDTO.setListOrderItemDTO(new ArrayList<OrderItemDTO>());
		this.orderItemDTOTableLayout = new OrderItemDTOTableLayout(this, this.orderDTO.getListOrderItemDTO());
		this.orderItemDTOTableTotalsLayout = 
				new OrderItemDTOTableTotalsLayout(
						this.orderDTO.getListOrderItemDTO(),
						this.orderDTO.getCredit_order_payment_condition_surcharge_percentage());
		//estimated finalize date panel date
		this.setUpOkCancelButtons();
		
		//after set up the layaouts must be added to this
		this.setInPositionPersonFormVerticalLayout();
		this.addComponent(this.firstRowElementInLayout);
		this.addComponent(this.setUpaddItemButtonHorizontalLayout());
		this.addComponent(this.orderItemDTOTableLayout);
		this.addComponent(this.orderItemDTOTableTotalsLayout);
		this.addComponent(this.okCancelHorizontalLayout);
	}
	
	private void setUpPersonDTOForm(){
		this.personDTOForm = new SgpForm<PersonDTO>(PersonDTO.class, new BeanItem<PersonDTO>(this.personDTO), "light", true);
		//this.personDTOForm.getSgpFormLayout().addComponent(this.buildSearchSupplierButton());
    	if(this.personDTO.getCommercial_name()!=null){
    		this.personDTOForm.bindAndSetPositionFormLayoutTextField("ruc", this.messages.get(this.VIEW_NAME + "tab.order.form.text.field.ruc.label")/**/, true, 100, false,null, false);
    		this.personDTOForm.bindAndSetPositionFormLayoutTextField("commercial_name", this.messages.get(this.VIEW_NAME + "tab.order.form.text.field.comercial.name.label")/**/, true, 100, false,null, false);
    	}if(this.personDTO.getPersonal_name()!=null && this.personDTO.getPersonal_last_name()!=null){
    		this.personDTOForm.bindAndSetPositionFormLayoutTextField("personal_civil_id_document", this.messages.get(this.VIEW_NAME + "tab.order.form.text.field.personal.civil.id.document.label")/**/, true, 100, false,null, false);
    		this.personDTOForm.bindAndSetPositionFormLayoutTextField("personal_name", this.messages.get(this.VIEW_NAME + "tab.order.form.text.field.personal.name.label")/**/, true, 100, false,null, false);
    		this.personDTOForm.bindAndSetPositionFormLayoutTextField("personal_last_name", this.messages.get(this.VIEW_NAME + "tab.order.form.text.field.personal.last.name.label")/**/, true, 100, false,null, false);
    	}
	}
	
	private void initPersonDTOFormVerticalLayout(){
		if(this.personDTOFormVerticalLayout == null){
			this.personDTOFormVerticalLayout = new VerticalLayout();
			this.personDTOFormVerticalLayout.setSpacing(true);
			this.personDTOFormVerticalLayout.setMargin(new MarginInfo(true, true, false, true));
		}else this.personDTOFormVerticalLayout.removeAllComponents();	
		this.personDTOFormVerticalLayout.addComponent(this.buildSearchSupplierButton());
	}
	
	private void setUpPersonFormVerticalLayout(PersonDTO vPersonDTO){
		this.initPersonDTOFormVerticalLayout();
		if(vPersonDTO!=null)this.personDTO = vPersonDTO;
		else this.personDTO = new PersonDTO();
		this.orderDTO.setId_person(this.personDTO.getId());
		this.setUpPersonDTOForm();
		this.personDTOFormVerticalLayout.addComponent(this.personDTOForm.getSgpFormLayout());
	}
	
	private void setInPositionPersonFormVerticalLayout(){
		if(this.firstRowElementInLayout == null)this.firstRowElementInLayout = new HorizontalLayout();
		else this.firstRowElementInLayout.removeAllComponents();
		this.firstRowElementInLayout.addComponent(this.personDTOFormVerticalLayout);
		this.firstRowElementInLayout.setComponentAlignment(this.personDTOFormVerticalLayout, Alignment.MIDDLE_LEFT);
		this.firstRowElementInLayout.addComponent(this.currencyPaymentConditionHorizontalLayout);
		this.firstRowElementInLayout.setComponentAlignment(this.currencyPaymentConditionHorizontalLayout, Alignment.MIDDLE_RIGHT);
	}
	
	private HorizontalLayout buildSearchSupplierButton(){
		HorizontalLayout header = new HorizontalLayout();
		header.setSpacing(true);
		//header.setMargin(true);
		header.addStyleName("toolbar");
		Label title = new Label(this.messages.get("application.common.customer.label"));
		title.addStyleName("colored");
		header.addComponent(title);
		header.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
		if(this.searchCustomerButton == null)this.setUpSearchCustomerButton();
		header.addComponent(this.searchCustomerButton);
		header.setComponentAlignment(this.searchCustomerButton, Alignment.MIDDLE_RIGHT);
		return header;		
	}
	
	private void setUpSearchCustomerButton(){
		this.searchCustomerButton = new Button(FontAwesome.SEARCH_PLUS);
		this.searchCustomerButton.setDescription(this.messages.get("application.common.button.search.customer.description"));
		this.searchCustomerButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                try{
                	personFinderWindow = new PersonFinderWindow(true,false,false);
                	personFinderWindow.addCloseListener(setUpPersonFinderWindowCloseListener());
                	personFinderWindow.adjuntWindowSizeAccordingToCientDisplay();
                }catch(Exception e){
                	commonExceptionErrorNotification.showErrorMessageNotification(e);
                }
            }
        });
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
					setUpPersonFormVerticalLayout(vPersonDTO);
					setInPositionPersonFormVerticalLayout();
				}catch(Exception ex){
					commonExceptionErrorNotification.showErrorMessageNotification(ex);
				}
			}			
		};
	}
	
	private void setUpCurrencyDTOComboBox() throws PmsServiceException{
		this.listCurrencyDTOComboBox = new ComboBox(this.messages.get("application.common.currency.label"));
		this.listCurrencyDTOComboBox.setInputPrompt(this.messages.get(this.VIEW_NAME + "tab.order.form.combo.box.currency.input.prompt"));
        BeanItemContainer<CurrencyDTO> vCurrencyDTOBeanItemContainer = new BeanItemContainer<CurrencyDTO>(CurrencyDTO.class);
        vCurrencyDTOBeanItemContainer.addAll(this.determinateCurrencyDTOList());
        this.listCurrencyDTOComboBox.setContainerDataSource(vCurrencyDTOBeanItemContainer);
        this.listCurrencyDTOComboBox.setItemCaptionPropertyId("id_code");
        
        if(this.orderDTO.getId_currency()!=null){
        	this.listCurrencyDTOComboBox.select(this.orderDTO.getCurrencyDTO());
        	this.listCurrencyDTOComboBox.setValue(this.orderDTO.getCurrencyDTO());
        }else{
        	this.listCurrencyDTOComboBox.select(vCurrencyDTOBeanItemContainer.getIdByIndex(0));
        	this.orderDTO.setId_currency(vCurrencyDTOBeanItemContainer.getIdByIndex(0).getId());
        }
        this.listCurrencyDTOComboBox.setNullSelectionAllowed(false);
        this.listCurrencyDTOComboBox.addStyleName("small");
        this.listCurrencyDTOComboBox.addValueChangeListener(this.setUpValueChangeListenerCurrencyDTOComboBox());
        this.listCurrencyDTOComboBox.setRequired(true);
        this.listCurrencyDTOComboBox.setRequiredError(this.messages.get(this.VIEW_NAME + "combo.box.currency.required.message"));
        //this.measurmentUnitComboBox.setEnabled(!this.rawMaterialPurchaseRequestDTOFormEditMode);
        this.listCurrencyDTOComboBox.setWidth(35, Unit.PERCENTAGE);
               
	}
	
	private List<CurrencyDTO> determinateCurrencyDTOList() throws PmsServiceException{
		List<CurrencyExchangeRateDTO> listCurrencyExchangeRateDTO = this.comercialManagementService.listCurrencyExchangeRateDTO(new CurrencyExchangeRateDTO(true));
		List<CurrencyDTO> currencyDTOList = new ArrayList<CurrencyDTO>();
		for(CurrencyExchangeRateDTO vCurrencyExchangeRateDTO:listCurrencyExchangeRateDTO){
			currencyDTOList.add(this.comercialManagementService.listCurrencyDTO(new CurrencyDTO(vCurrencyExchangeRateDTO.getId_currency())).get(0));
		}
		currencyDTOList.add(0, this.comercialManagementService.listCurrencyDTO(new CurrencyDTO(true)).get(0));
		return currencyDTOList;
	}
	
	private Property.ValueChangeListener setUpValueChangeListenerCurrencyDTOComboBox(){
    	return new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				try{
					logger.info("\n CurrencyDTO combo box: value change event"
								+"\n======================================"	+"\nevent.getProperty().getType().getName()) : " 
								+ event.getProperty().getType().getName()
								+"\nevent.getProperty().getValue() : \n" + event.getProperty().getValue());
					orderDTO.setId_currency(((CurrencyDTO)event.getProperty().getValue()).getId());
					recalculateItemsAndTotalsDueToCurrencyChange();
				}catch(Exception e){
					logger.error("\n error", e);
				}
			}
    	};
	}
	
	private void recalculateItemsAndTotalsDueToCurrencyChange() throws PmsServiceException{
		if(this.orderDTO.getListOrderItemDTO()!=null && !this.orderDTO.getListOrderItemDTO().isEmpty()){
			this.vListOrderItemDTO = new ArrayList<OrderItemDTO>(this.orderDTO.getListOrderItemDTO());
			this.orderDTO.setListOrderItemDTO(new ArrayList<OrderItemDTO>());
			for(OrderItemDTO vOrderItemDTO : this.vListOrderItemDTO){
				this.initAndSetOrderItem(vOrderItemDTO.getProductDTO());
			}
			reSetLayoutAfterAnItemHasBeenAdded();//to recalculate totals by cash or credit
		}
		/*
		 * 
		 * 
		 * reSetLayoutAfterAnItemHasBeenAdded();//to recalculate totals by cash or credit
		 * 
		 * 
		 * 
		 * */
	}
	
	private void setUpPaymentConditionOptionGroup(){
		List<SelectOneOption> listSelectOneOption = new ArrayList<SelectOneOption>();
		listSelectOneOption.add(
		new SelectOneOption("application.common.payment.condition.cash",this.messages.get("application.common.payment.condition.cash"))
		);
		listSelectOneOption.add(
		new SelectOneOption("application.common.payment.condition.credit",this.messages.get("application.common.payment.condition.credit"))
		);		
		
    	this.paymentConditionOptionGroup = new OptionGroup();//
    	this.paymentConditionOptionGroup.addStyleName("small");
    	this.paymentConditionOptionGroup.setMultiSelect(false);
    	this.paymentConditionOptionGroup.setCaption(this.messages.get("application.common.payment.condition.selector.description"));
    	this.paymentConditionOptionGroup.setDescription(this.messages.get("application.common.payment.condition.selector.description"));
    	BeanItemContainer<SelectOneOption> vSelectOneOptionBeanItemContainer = new BeanItemContainer<SelectOneOption>(SelectOneOption.class);
    	vSelectOneOptionBeanItemContainer.addAll(listSelectOneOption);
    	this.paymentConditionOptionGroup.setContainerDataSource(vSelectOneOptionBeanItemContainer);
    	if(this.orderDTO.getPayment_condition()!=null){
    		SelectOneOption vSelectOneOption = new SelectOneOption(this.orderDTO.getPayment_condition(),null);
    		this.paymentConditionOptionGroup.select(vSelectOneOption);
    		this.paymentConditionOptionGroup.setValue(vSelectOneOption);
    	}else{
    		this.paymentConditionOptionGroup.select(new SelectOneOption("application.common.payment.condition.cash",this.messages.get("application.common.payment.condition.cash")));
    		this.orderDTO.setPayment_condition("application.common.payment.condition.cash");
    	}
    	this.paymentConditionOptionGroup.setItemCaptionPropertyId("value");
    	this.paymentConditionOptionGroup.setNullSelectionAllowed(false);
    	this.paymentConditionOptionGroup.addValueChangeListener(this.setUpValueChangeListenerForSelectOptionGroup());    	
	}

    private Property.ValueChangeListener setUpValueChangeListenerForSelectOptionGroup(){
    	return new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				try{
					if(((SelectOneOption)event.getProperty().getValue()).getKey().equals("application.common.payment.condition.credit")){
						creditOrderCriteriaLayout.setVisible(true);
						orderDTO.setPayment_condition("application.common.payment.condition.credit");
					}else{
						creditOrderCriteriaLayout.restartLayout();
						creditOrderCriteriaLayout.setVisible(false);
						orderDTO.setPayment_condition("application.common.payment.condition.cash");
					}				
					creditOrderCriteriaLayout.markAsDirty();
					
					reSetLayoutAfterAnItemHasBeenAdded();//to recalculate totals by cash or credit					
				}catch(Exception e){
					logger.error("\n error", e);
				}
			}//public void valueChange(ValueChangeEvent event)
        };//return new Property.ValueChangeListener() 
    }
    
    private void setUpCurrencyPaymentConditionHorizontalLayout() throws PmsServiceException{
		this.setUpCurrencyDTOComboBox();
		this.setUpPaymentConditionOptionGroup();
    	this.currencyPaymentConditionHorizontalLayout = 
    			new HorizontalLayout(
    					this.listCurrencyDTOComboBox, 
    					this.paymentConditionOptionGroup,
    					this.creditOrderCriteriaLayout);
    	Responsive.makeResponsive(this.currencyPaymentConditionHorizontalLayout);
    	this.currencyPaymentConditionHorizontalLayout.setSpacing(true);
    	this.currencyPaymentConditionHorizontalLayout.setMargin(new MarginInfo(true,true,false,true));
    	
    }
    
    public void editOrderItemDTO(OrderItemDTO vOrderItemDTO, boolean editFormMode){
    	
    }
    
    private void setUpOkCancelButtons(){
		this.okButton = new Button(this.messages.get("application.common.button.save.label"));/*"Cancelar"*/
		this.okButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		/* temporary */
		            		if(orderDTO.getEstimated_completion_date() == null){
		            			Calendar vCalendar = Calendar.getInstance();
		            			vCalendar.setTime(new Date());
		            			vCalendar.add(Calendar.DAY_OF_MONTH, 15);
		            			orderDTO.setEstimated_completion_date(vCalendar.getTime());
		            		}
		            		/* */
		            		
		            		listCurrencyDTOComboBox.validate();
		            		listCurrencyDTOComboBox.commit();
		            		
		            		paymentConditionOptionGroup.validate();
		            		paymentConditionOptionGroup.commit();
		            		if(orderDTO.getPayment_condition().equals("application.common.payment.condition.credit"))
		            			creditOrderCriteriaLayout.commitOrderDTOCreditCriteriaForm();
		            		
		            		orderDTO.setValue_added_tax_5_amount(orderItemDTOTableTotalsLayout.getTotalValueAdded_5_Tax());
		            		orderDTO.setValue_added_tax_10_amount(orderItemDTOTableTotalsLayout.getTotalValueAdded_10_Tax());
		            		orderDTO.setExempt_amount(orderItemDTOTableTotalsLayout.getExcemptTotal());
		            		orderRegisterFormView.saveButtonActionOrderDTOTabLayout(orderDTO);
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
		            		listCurrencyDTOComboBox.discard();		            		
		            		paymentConditionOptionGroup.discard();
		            		creditOrderCriteriaLayout.restartLayout();
		            		orderRegisterFormView.cancelButtonActionOrderDTOTabLayout();
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);

		final Button switchToRevisionStatusButton = new Button(this.messages.get(this.VIEW_NAME + "tab.order.form.button.switch.to.revision.status.label"));/*"Cancelar"*/
		switchToRevisionStatusButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.order.form.button.switch.to.revision.status.description"));
		switchToRevisionStatusButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		orderDTO.setPrevious_status(orderDTO.getStatus());
		            		orderDTO.setStatus("application.common.status.revision");
		            		orderRegisterFormView.saveButtonActionOrderDTOTabLayout(orderDTO);
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		/*switchToRevisionStatusButton.setVisible(
				bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
				.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "edit.revision.status.order")
				&& this.checkOrderDTOForChanceToSwitchToRevisionStatus());*/
		switchToRevisionStatusButton.setVisible(false);
		this.okCancelHorizontalLayout = new HorizontalLayout();
		this.okCancelHorizontalLayout.setMargin(true);
		this.okCancelHorizontalLayout.setSpacing(true);
		this.okCancelHorizontalLayout.setSizeFull();
		this.okCancelHorizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		final HorizontalLayout wrapperHorizontalLayout = new HorizontalLayout();
		wrapperHorizontalLayout.setSpacing(true);
		wrapperHorizontalLayout.addComponent(switchToRevisionStatusButton);
		if(this.orderDTO.getId()!=null && this.orderDTO.getStatus().equals("application.common.status.pending"))
			wrapperHorizontalLayout.addComponent(this.prepareConfirmOrderCheckBox());
		wrapperHorizontalLayout.addComponent(this.okButton);
		wrapperHorizontalLayout.addComponent(cancelButton);		
		this.okCancelHorizontalLayout.addComponent(wrapperHorizontalLayout);
		
    }
    
    private HorizontalLayout setUpaddItemButtonHorizontalLayout(){
		final Button addItemButton = new Button(this.messages.get(this.VIEW_NAME + "tab.order.form.button.add.item.description"));/*"Cancelar"*/
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
		                    	productFinderWindow = new ProductFinderWindow();
		                    	productFinderWindow.addCloseListener(setUpProductFinderWindowCloseListener());
		                    	productFinderWindow.adjuntWindowSizeAccordingToCientDisplay();
		                    }catch(Exception e){
		                    	commonExceptionErrorNotification.showErrorMessageNotification(e);
		                    }		                    
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		HorizontalLayout vHorizontalLayout = new HorizontalLayout(addItemButton);
		vHorizontalLayout.setMargin(new MarginInfo(false, false, false, true));
		return vHorizontalLayout;
    }
	
    
    public void reSetLayoutAfterAnItemHasBeenAdded(){
		this.removeComponent(this.orderItemDTOTableLayout);
		this.removeComponent(this.orderItemDTOTableTotalsLayout);
		this.removeComponent(this.okCancelHorizontalLayout);
		
		
		this.orderItemDTOTableLayout = new OrderItemDTOTableLayout(this, this.orderDTO.getListOrderItemDTO());
		this.orderItemDTOTableTotalsLayout = 
				new OrderItemDTOTableTotalsLayout(
						this.orderDTO.getListOrderItemDTO(),
						this.orderDTO.getCredit_order_payment_condition_surcharge_percentage());
		//estimated finalize date panel date
		this.setUpOkCancelButtons();
		
		this.addComponent(this.orderItemDTOTableLayout);
		this.addComponent(this.orderItemDTOTableTotalsLayout);
		this.addComponent(this.okCancelHorizontalLayout);   	
    }
    
	
	private CloseListener setUpProductFinderWindowCloseListener(){
		return new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{
					final ProductDTO vProductDTO = productFinderWindow.getProductDTOTableRowSelected();
					logger.info("\n******************************"
								+"\n the product finder window has been closed"
								+"\n person found: \n" + vProductDTO
								+"\n******************************");
					if(vProductDTO!=null){
						initAndSetOrderItem(vProductDTO);
            			reSetLayoutAfterAnItemHasBeenAdded();
            			refreshOkButtonStatus();
					}					
				}catch(Exception ex){
					commonExceptionErrorNotification.showErrorMessageNotification(ex);
				}
			}			
		};
	}
	
	private void initAndSetOrderItem(final ProductDTO vProductDTO) throws PmsServiceException{
		final OrderItemDTO vOrderItemDTO = new OrderItemDTO(null, null, Math.round(Math.random() * 10000));
		vOrderItemDTO.setProductDTO(vProductDTO);
		vOrderItemDTO.setId_product(vProductDTO.getId());
		vOrderItemDTO.setOrder_id_currency(this.orderDTO.getId_currency());
		vOrderItemDTO.setOrder_identifier_number(this.orderDTO.getIdentifier_number());
		vOrderItemDTO.setQuantity(vProductDTO.getOrderItemQuantity());
		/* calculate unit price amount */
		//BigDecimal unitPriceAmount = new BigDecimal((Math.random() * 10000));
		//BigDecimal unitPriceAmount = this.comercialManagementService.determinateProductPriceByProductIdCurrencyIdIdentifyingOrder(vOrderItemDTO);
		final BigDecimal vProduct_unit_manufacture_cost = this.comercialManagementService.determinateProductPriceByProductIdCurrencyIdIdentifyingOrder(vOrderItemDTO);
		
		vOrderItemDTO.setUnit_price_amount(
				vProduct_unit_manufacture_cost.add(
						vProduct_unit_manufacture_cost.multiply(
								BigDecimal.valueOf(vProductDTO.getIncrease_over_cost_for_sale_price()).divide(BigDecimal.valueOf(100L))
						)));
		//set the product cost
		vOrderItemDTO.setProduct_unit_manufacture_cost(vProduct_unit_manufacture_cost);
		
		vOrderItemDTO.setValue_added_tax_10_unit_price_amount(vOrderItemDTO.getUnit_price_amount().multiply(BigDecimal.valueOf(vOrderItemDTO.getQuantity())));
		vOrderItemDTO.setExempt_unit_price_amount(BigDecimal.ZERO);
		vOrderItemDTO.setValue_added_tax_5_unit_price_amount(BigDecimal.ZERO);
		logger.info( "\n==========================================="
				+"\n payment condition affects the item total"
				+"\n===========================================");
		this.orderDTO.getListOrderItemDTO().add(vOrderItemDTO);
	}
	
	
	public void deleteOrderItemFromPreliminaryList(final OrderItemDTO vOrderItemDTO){
		this.orderDTO.getListOrderItemDTO().remove(vOrderItemDTO);
		this.reSetLayoutAfterAnItemHasBeenAdded();
		this.refreshOkButtonStatus();
	}
	

	private void setOrderNumber() throws PmsServiceException{
		this.orderDTO.setIdentifier_number(this.comercialManagementService.pmsOrderDTOIdentifierNumberBySequence());
	}
	
	private HorizontalLayout setUpOrderNumberIndicator() throws PmsServiceException{
		if(this.orderDTO.getId() == null){
			this.setOrderNumber();
			this.orderDTO.setRegistration_date(new Date());
		}
		final Label orderNumberLabel = new Label(this.messages.get("application.common.order.number.indicator.label"));
		orderNumberLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<Long> property =	new ObjectProperty<Long>(this.orderDTO.getIdentifier_number());
		final Label orderNumberValue = new Label(property);
		final HorizontalLayout orderNumberHorizontalLayout = new HorizontalLayout(orderNumberLabel,orderNumberValue);
		orderNumberHorizontalLayout.setMargin(new MarginInfo(false, false, false, true));
		orderNumberHorizontalLayout.setSpacing(true);
		
		final Label orderRegistrationDateLabel = new Label(this.messages.get("application.common.table.column.registration.date.label"));
		orderRegistrationDateLabel.addStyleName(ValoTheme.LABEL_COLORED);
		final ObjectProperty<String> dateProperty =	new ObjectProperty<String>(SgpUtils.dateToString(this.orderDTO.getRegistration_date()));
		final Label orderRegistrationDateValue = new Label(dateProperty);
		final HorizontalLayout registrationDateHorizontalLayout = new HorizontalLayout(orderRegistrationDateLabel,orderRegistrationDateValue);
		registrationDateHorizontalLayout.setMargin(new MarginInfo(false, true, false, true));
		registrationDateHorizontalLayout.setSpacing(true);
		
		HorizontalLayout statusHorizontalLayout = null;
		if(this.orderDTO.getId() != null){
			final Label statusLabel = new Label(this.messages.get("application.common.status.label"));
			statusLabel.addStyleName(ValoTheme.LABEL_COLORED);
			final Label statusValue = new Label(this.messages.get(this.orderDTO.getStatus()));
			statusHorizontalLayout = new HorizontalLayout(statusLabel,statusValue);
			statusHorizontalLayout.setMargin(new MarginInfo(false, true, false, true));
			statusHorizontalLayout.setSpacing(true);		
		}
		
		final HorizontalLayout mainHorizontalLayout = new HorizontalLayout(orderNumberHorizontalLayout,registrationDateHorizontalLayout);
		mainHorizontalLayout.setComponentAlignment(orderNumberHorizontalLayout, Alignment.MIDDLE_RIGHT);		
		mainHorizontalLayout.setComponentAlignment(registrationDateHorizontalLayout, Alignment.MIDDLE_RIGHT);
		if(statusHorizontalLayout != null){
			mainHorizontalLayout.addComponent(statusHorizontalLayout);
			mainHorizontalLayout.setComponentAlignment(statusHorizontalLayout, Alignment.MIDDLE_RIGHT);
		}
		
		final HorizontalLayout wrapperHorizontalLayout = new HorizontalLayout(mainHorizontalLayout);
		wrapperHorizontalLayout.setComponentAlignment(mainHorizontalLayout, Alignment.MIDDLE_RIGHT);
		wrapperHorizontalLayout.setSizeFull();
		return wrapperHorizontalLayout;
	}
	
	private void refreshOkButtonStatus(){
		this.okButton.setEnabled(
				this.orderDTO.getListOrderItemDTO()!=null 
				&& !this.orderDTO.getListOrderItemDTO().isEmpty()
				&& 
				(		this.orderDTO.getStatus() == null ||
						this.orderDTO.getStatus().equals("application.common.status.pending")	
						|| this.orderDTO.getStatus().equals("application.common.status.revision")
				));
		this.okButton.markAsDirty();
	}
	
	private HorizontalLayout prepareConfirmOrderCheckBox(){
		Boolean vBoolean = new Boolean(false);
		final ObjectProperty<Boolean> property =	new ObjectProperty<Boolean>(vBoolean);
		CheckBox vCheckBox = new CheckBox(this.messages.get(this.VIEW_NAME + "tab.order.form.checkbox.confirm.order.caption"), property);
		vCheckBox.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				Boolean vBoolean = (Boolean)event.getProperty().getValue();
				logger.info( "\n=========================="
							+"\n confirm order check : " + vBoolean	
							+"\n==========================");
				
				if(vBoolean)orderDTO.setStatus("application.common.status.confirmed");
				else orderDTO.setStatus("application.common.status.pending");
			}			
		});
		HorizontalLayout vHorizontalLayout = new HorizontalLayout(vCheckBox);
		//vHorizontalLayout.setCaption(this.messages.get(this.VIEW_NAME + "tab.order.form.checkbox.confirm.order.caption"));
		return vHorizontalLayout;
	}
	
	private boolean checkOrderDTOForChanceToSwitchToRevisionStatus(){
		if((this.orderDTO.getStatus()!=null && (/*this.orderDTO.getStatus().equals("application.common.status.confirmed") 
				||*/ this.orderDTO.getStatus().equals("application.common.status.invoiced") 
				/*|| this.orderDTO.getStatus().equals("application.common.status.in.progress")*/))
				/*&& this.orderDTO.getPrevious_status() == null*/)
			return true;
		else return false;
	}
	
	
	@Override
	public String getOrderDTOStatus() {
		// TODO Auto-generated method stub
		return this.orderDTO.getStatus();
	}
	
	public class SelectOneOption{
		private String key;
		private String value;

		/**
		 * @param key
		 * @param value
		 */
		public SelectOneOption(String key, String value) {
			super();
			this.key = key;
			this.value = value;
		}

		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * @param key the key to set
		 */
		public void setKey(String key) {
			this.key = key;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "SelectOneOption [key=" + key + ", value=" + value + "]";
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SelectOneOption other = (SelectOneOption) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			return true;
		}

		private OrderDTOTabLayout getOuterType() {
			return OrderDTOTabLayout.this;
		}
	}
}
