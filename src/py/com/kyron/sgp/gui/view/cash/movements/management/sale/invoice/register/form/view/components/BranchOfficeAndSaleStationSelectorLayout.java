package py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.register.form.view.components;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.BranchOfficeDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.BranchOfficeSaleStationDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.service.CashMovementsManagementService;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class BranchOfficeAndSaleStationSelectorLayout extends HorizontalLayout {

	private final Logger logger = LoggerFactory.getLogger(BranchOfficeAndSaleStationSelectorLayout.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private List<BranchOfficeDTO> listBranchOfficeDTO;
	private BranchOfficeDTO branchOfficeDTO;
	private BranchOfficeSaleStationDTO branchOfficeSaleStationDTO;
	private ComboBox branchOfficeDTOComboBox;
	private ComboBox branchOfficeSaleStationDTOComboBox;
	private CashMovementsManagementService cashMovementsManagementService;
	private Button generateSaleInvoiceButton;
	private final SaleInvoiceGenerationFunction saleInvoiceGenerationFunction;
	private final OrderDTO orderDTO;
	private Label orderNumberIndicatorLabel;
	private Label orderNumberLabel;
	private HorizontalLayout officeSaleStationHorizontalLayout;
	
	public BranchOfficeAndSaleStationSelectorLayout(
			final String VIEW_NAME,
			final SaleInvoiceGenerationFunction saleInvoiceGenerationFunction,
			final OrderDTO orderDTO) {
		// TODO Auto-generated constructor stub
		this.VIEW_NAME = VIEW_NAME;
		this.saleInvoiceGenerationFunction = saleInvoiceGenerationFunction;
		this.orderDTO = orderDTO;
		try{
			logger.info("\n BranchOfficeAndSaleStationSelectorLayout()...");
			this.initServices();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
	        //this.setSpacing(true);
			this.setMargin(new MarginInfo(true, true, true, true));
			//this.setSpacing(true);
			//this.setSizeFull();
	        Responsive.makeResponsive(this);
	        this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public BranchOfficeAndSaleStationSelectorLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	
	private void initServices() throws Exception{
		this.cashMovementsManagementService = (CashMovementsManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.CASH_MOVEMENTS_MANAGEMENT_SERVICE);
	}
	
	private void setUpLayoutContent() throws PmsServiceException{
		if(this.orderNumberIndicatorLabel == null){
			this.orderNumberLabel = new Label(this.messages.get("application.common.order.number.indicator.label"));
			this.orderNumberLabel.addStyleName(ValoTheme.LABEL_COLORED);
			final ObjectProperty<Long> property =	new ObjectProperty<Long>(this.orderDTO.getIdentifier_number());
			this.orderNumberIndicatorLabel = new Label(property);
			HorizontalLayout vHorizontalLayout = new HorizontalLayout();			
			vHorizontalLayout.addComponent(this.orderNumberLabel);
			vHorizontalLayout.addComponent(this.orderNumberIndicatorLabel);
			vHorizontalLayout.setSpacing(true);
			vHorizontalLayout.setMargin(new MarginInfo(false, true, false, false));
			this.addComponent(vHorizontalLayout);
			this.setComponentAlignment(vHorizontalLayout, Alignment.BOTTOM_LEFT);
		}
		
		
		if(this.branchOfficeDTOComboBox == null){
			this.officeSaleStationHorizontalLayout = new HorizontalLayout();
			this.officeSaleStationHorizontalLayout.setSpacing(true);
			this.officeSaleStationHorizontalLayout.setMargin(new MarginInfo(false, true, false, true));
			this.buildbranchOfficeDTOComboBox();
			this.branchOfficeDTOComboBox.select(this.listBranchOfficeDTO.get(0));
			this.branchOfficeDTOComboBox.setValue(this.listBranchOfficeDTO.get(0));
			this.officeSaleStationHorizontalLayout.addComponent(this.branchOfficeDTOComboBox);
		}
		if(this.branchOfficeSaleStationDTOComboBox != null){
			this.officeSaleStationHorizontalLayout.removeComponent(this.branchOfficeSaleStationDTOComboBox);
			this.removeComponent(this.officeSaleStationHorizontalLayout);
		}
		this.buildbranchOfficeSaleStationDTOComboBox();
		this.officeSaleStationHorizontalLayout.addComponent(this.branchOfficeSaleStationDTOComboBox);
		this.addComponent(this.officeSaleStationHorizontalLayout);
		this.setComponentAlignment(this.officeSaleStationHorizontalLayout, Alignment.BOTTOM_LEFT);
		if(this.generateSaleInvoiceButton != null)this.removeComponent(this.generateSaleInvoiceButton);
		else this.buildGenerateSaleInvoiceButton();	
		this.addComponent(this.generateSaleInvoiceButton);
		this.setComponentAlignment(this.generateSaleInvoiceButton, Alignment.BOTTOM_LEFT);
		
	}
	
	private void buildbranchOfficeDTOComboBox() throws PmsServiceException{
		if(this.listBranchOfficeDTO == null)this.listBranchOfficeDTO = this.cashMovementsManagementService.listBranchOfficeDTO(new BranchOfficeDTO(null, 1L));
		this.branchOfficeDTOComboBox = new ComboBox(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.form.branch.office.combo.box.label"));
		this.branchOfficeDTOComboBox.setInputPrompt(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.form.branch.office.combo.box.input.prompt"));
        BeanItemContainer<BranchOfficeDTO> BranchOfficeDTOBeanItemContainer = new BeanItemContainer<BranchOfficeDTO>(BranchOfficeDTO.class);
        BranchOfficeDTOBeanItemContainer.addAll(this.listBranchOfficeDTO);
        this.branchOfficeDTOComboBox.setContainerDataSource(BranchOfficeDTOBeanItemContainer);
        this.branchOfficeDTOComboBox.setItemCaptionPropertyId("description");
        
        this.branchOfficeDTOComboBox.setNullSelectionAllowed(false);
        this.branchOfficeDTOComboBox.addStyleName("small");
        this.branchOfficeDTOComboBox.addValueChangeListener(this.setUpValueChangeListenerForbranchOfficeDTOComboBox());
        this.branchOfficeDTOComboBox.setRequired(true);
        this.branchOfficeDTOComboBox.setRequiredError(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.form.branch.office.required.message"));
        //this.branchOfficeDTOComboBox.setEnabled(!this.rawMaterialPurchaseRequestDTOFormEditMode);
        this.branchOfficeDTOComboBox.setWidth(80, Unit.PERCENTAGE);
	}
	
	private Property.ValueChangeListener setUpValueChangeListenerForbranchOfficeDTOComboBox(){
    	return new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				try{
					logger.info("\n branchOfficeDTOComboBox combo box: value change event"
							+"\n======================================"	+"\nevent.getProperty().getType().getName()) : " 
							+ event.getProperty().getType().getName()
							+"\nevent.getProperty().getValue() : \n" + event.getProperty().getValue());
					branchOfficeDTO = (BranchOfficeDTO)event.getProperty().getValue();
					setUpLayoutContent();
				}catch(Exception e){
					logger.error("\nerror: ",e);
				}	
			}
    	};
	}
	
	private void buildbranchOfficeSaleStationDTOComboBox() throws PmsServiceException{
		this.branchOfficeSaleStationDTOComboBox = new ComboBox(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.form.branch.office.sale.station.combo.box.label"));
		this.branchOfficeSaleStationDTOComboBox.setInputPrompt(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.form.branch.office.sale.station.combo.box.input.prompt"));
        BeanItemContainer<BranchOfficeSaleStationDTO> BranchOfficeSaleStationDTOBeanItemContainer = new BeanItemContainer<BranchOfficeSaleStationDTO>(BranchOfficeSaleStationDTO.class);
        BranchOfficeSaleStationDTOBeanItemContainer.addAll(this.branchOfficeDTO.getListBranchOfficeSaleStationDTO());
        this.branchOfficeSaleStationDTOComboBox.setContainerDataSource(BranchOfficeSaleStationDTOBeanItemContainer);
        this.branchOfficeSaleStationDTOComboBox.setItemCaptionPropertyId("sale_invoice_description");

        
        this.branchOfficeSaleStationDTOComboBox.setNullSelectionAllowed(false);
        this.branchOfficeSaleStationDTOComboBox.addStyleName("small");
        this.branchOfficeSaleStationDTOComboBox.addValueChangeListener(this.setUpValueChangeListenerForbranchOfficeSaleStationDTOComboBox());
        
        this.branchOfficeSaleStationDTOComboBox.select(this.branchOfficeDTO.getListBranchOfficeSaleStationDTO().get(0));
        this.branchOfficeSaleStationDTOComboBox.setValue(this.branchOfficeDTO.getListBranchOfficeSaleStationDTO().get(0));
        
        this.branchOfficeSaleStationDTOComboBox.setRequired(true);
        this.branchOfficeSaleStationDTOComboBox.setRequiredError(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.form.branch.office.sale.station.combo.box.required.message"));
        //this.branchOfficeDTOComboBox.setEnabled(!this.rawMaterialPurchaseRequestDTOFormEditMode);
        this.branchOfficeSaleStationDTOComboBox.setWidth(40, Unit.PERCENTAGE);
	}

	private Property.ValueChangeListener setUpValueChangeListenerForbranchOfficeSaleStationDTOComboBox(){
    	return new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				logger.info("\n branchOfficeSaleStationDTOComboBox combo box: value change event"
							+"\n======================================"	+"\nevent.getProperty().getType().getName()) : " 
							+ event.getProperty().getType().getName()
							+"\nevent.getProperty().getValue() : \n" + event.getProperty().getValue());
						branchOfficeSaleStationDTO = (BranchOfficeSaleStationDTO)event.getProperty().getValue();							
			}
    	};
	}

	/**
	 * @return the branchOfficeDTO
	 */
	public BranchOfficeDTO getBranchOfficeDTO() {
		return branchOfficeDTO;
	}

	/**
	 * @param branchOfficeDTO the branchOfficeDTO to set
	 */
	public void setBranchOfficeDTO(BranchOfficeDTO branchOfficeDTO) {
		this.branchOfficeDTO = branchOfficeDTO;
	}

	/**
	 * @return the branchOfficeSaleStationDTO
	 */
	public BranchOfficeSaleStationDTO getBranchOfficeSaleStationDTO() {
		return branchOfficeSaleStationDTO;
	}

	/**
	 * @param branchOfficeSaleStationDTO the branchOfficeSaleStationDTO to set
	 */
	public void setBranchOfficeSaleStationDTO(
			BranchOfficeSaleStationDTO branchOfficeSaleStationDTO) {
		this.branchOfficeSaleStationDTO = branchOfficeSaleStationDTO;
	}
	
	private void buildGenerateSaleInvoiceButton(){
        this.generateSaleInvoiceButton = new Button(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.form.button.generate.sale.invoice.label"));
        this.generateSaleInvoiceButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.form.button.generate.sale.invoice.description"));
        this.generateSaleInvoiceButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
            	try{
            		logger.info("\n GenerateSaleInvoiceButton clicked !");
            		branchOfficeDTOComboBox.commit();
            		branchOfficeSaleStationDTOComboBox.commit();
            		saleInvoiceGenerationFunction.generateSaleInvoice();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					commonExceptionErrorNotification.showErrorMessageNotification(e);
				}
            }
        });
        this.generateSaleInvoiceButton.setEnabled(true);
        this.generateSaleInvoiceButton.setVisible(true);
	}
}
