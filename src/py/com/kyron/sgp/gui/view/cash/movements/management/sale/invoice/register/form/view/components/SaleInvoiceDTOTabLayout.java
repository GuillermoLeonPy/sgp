/**
 * 
 */
package py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.register.form.view.components;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceItemDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.service.CashMovementsManagementService;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.cash.movements.management.SaleInvoiceRegisterFormView;
import py.com.kyron.sgp.gui.view.utils.commponents.editquantitywindow.EditQuantityWindow;

import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

/**
 * @author testuser
 *
 */
@SuppressWarnings("serial")
public class SaleInvoiceDTOTabLayout extends VerticalLayout implements SaleInvoiceGenerationFunction,SaleInvoiceItemDTOTableFunctions {

	private final Logger logger = LoggerFactory.getLogger(SaleInvoiceDTOTabLayout.class);	
	private Map<String,String> messages;
	private final String VIEW_NAME = "sale.invoice.management.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private final SaleInvoiceRegisterFormView saleInvoiceRegisterFormView;
	private SaleInvoiceDTO saleInvoiceDTO;
	private final OrderDTO orderDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private CashMovementsManagementService cashMovementsManagementService;
	private BussinesSessionUtils bussinesSessionUtils;
	private BranchOfficeAndSaleStationSelectorLayout branchOfficeAndSaleStationSelectorLayout;
	private SaleInvoiceHeaderLayout saleInvoiceHeaderLayout;
	private SaleInvoiceItemDTOTableLayout saleInvoiceItemDTOTableLayout;
	private SaleInvoiceItemDTOTableTotalsLayout saleInvoiceItemDTOTableTotalsLayout;
	private EditQuantityWindow editQuantityWindow;
	/**
	 * 
	 */
	public SaleInvoiceDTOTabLayout(
			final SaleInvoiceRegisterFormView saleInvoiceRegisterFormView,
			final SaleInvoiceDTO saleInvoiceDTO,
			final OrderDTO orderDTO) {
		// TODO Auto-generated constructor stub
		this.saleInvoiceRegisterFormView = saleInvoiceRegisterFormView;
		this.saleInvoiceDTO = saleInvoiceDTO;
		this.orderDTO = orderDTO;
		try{
			logger.info("\n SaleInvoiceDTOTabLayout..");
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

	/**
	 * @param children
	 */
	/*public SaleInvoiceDTOTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	private void initServices() throws Exception{
		this.cashMovementsManagementService = (CashMovementsManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.CASH_MOVEMENTS_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}

	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
		logger.info("\n==========================================="
				+"\n SaleInvoiceDTOTabLayout" + this.hashCode()
				+"\n detaching ..."
				+"\n===========================================");
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void setUpLayoutContent(){
		this.removeAllComponents();
		if(this.orderDTO!=null && this.saleInvoiceDTO == null){
			this.branchOfficeAndSaleStationSelectorLayout = new BranchOfficeAndSaleStationSelectorLayout(this.VIEW_NAME, this, this.orderDTO);
			this.addComponent(this.branchOfficeAndSaleStationSelectorLayout);
		}
		if(this.saleInvoiceDTO!=null){
			this.saleInvoiceHeaderLayout = new SaleInvoiceHeaderLayout(this.VIEW_NAME, this.saleInvoiceDTO);
			this.addComponent(this.saleInvoiceHeaderLayout);
			this.saleInvoiceItemDTOTableLayout = new SaleInvoiceItemDTOTableLayout(this.VIEW_NAME,this,this.saleInvoiceDTO.getListSaleInvoiceItemDTO());
			this.addComponent(this.saleInvoiceItemDTOTableLayout);
			this.saleInvoiceItemDTOTableTotalsLayout = 
					new SaleInvoiceItemDTOTableTotalsLayout(
							this.VIEW_NAME,
							this.saleInvoiceDTO.getListSaleInvoiceItemDTO(), 
							this.saleInvoiceDTO.getOrderDTO().getCredit_order_payment_condition_surcharge_percentage());
			this.addComponent(this.saleInvoiceItemDTOTableTotalsLayout);
			this.addComponent(this.setUpOkCancelButtons());
		}
	}

	@Override
	public void generateSaleInvoice() throws Exception {
		// TODO Auto-generated method stub
		
		this.saleInvoiceDTO = this.cashMovementsManagementService.generateSaleInvoiceDTO
				(new SaleInvoiceDTO(null,null,this.orderDTO.getId(),
					this.branchOfficeAndSaleStationSelectorLayout.getBranchOfficeSaleStationDTO().getId()));
		this.saleInvoiceDTO.setOrderDTO(this.orderDTO);
		this.saleInvoiceRegisterFormView.reStartLayout(saleInvoiceDTO);
	}

	
    private HorizontalLayout setUpOkCancelButtons(){
		final Button annulSaleInvoiceButton = new Button(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.form.button.annul.sale.invoice.label"));/*"Cancelar"*/
		annulSaleInvoiceButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.form.button.annul.sale.invoice.description"));
		annulSaleInvoiceButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		saleInvoiceRegisterFormView.annulSaleInvoiceDTO(saleInvoiceDTO);
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		annulSaleInvoiceButton.setVisible(
				bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
				.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "edit.revision.status.sale.invoice"));    	
		annulSaleInvoiceButton.setEnabled((saleInvoiceDTO.getStatus().equals("application.common.status.pending")
				|| saleInvoiceDTO.getStatus().equals("application.common.status.payment.in.progress")));
		
    	final Button regenerateSaleInvoiceButton = new Button(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.form.button.re.generate.sale.invoice.from.order.label"));/*"Cancelar"*/
		regenerateSaleInvoiceButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.form.button.re.generate.sale.invoice.from.order.description"));
		regenerateSaleInvoiceButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		saleInvoiceDTO.setPrevious_status(saleInvoiceDTO.getStatus());
		            		saleInvoiceDTO.setStatus("application.common.status.revision");
		            		saleInvoiceRegisterFormView.reGenerateSaleInvoiceFromOrder(saleInvoiceDTO);
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		regenerateSaleInvoiceButton.setVisible(
				bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
				.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "edit.revision.status.sale.invoice"));
		regenerateSaleInvoiceButton.setVisible(saleInvoiceDTO.getStatus().equals("application.common.status.revision"));
    	
		final Button switchToRevisionStatusButton = new Button(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.form.button.switch.to.revision.status.label"));/*"Cancelar"*/
		switchToRevisionStatusButton.setDescription(this.messages.get(this.VIEW_NAME + "tab.sale.invoice.form.button.switch.to.revision.status.description"));
		switchToRevisionStatusButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		saleInvoiceDTO.setPrevious_status(saleInvoiceDTO.getStatus());
		            		saleInvoiceDTO.setStatus("application.common.status.revision");
		            		saleInvoiceRegisterFormView.updateSaleInvoiceDTO(saleInvoiceDTO);
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		switchToRevisionStatusButton.setVisible(
				bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
				.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "edit.revision.status.sale.invoice"));
		switchToRevisionStatusButton.setEnabled(this.checkSaleInvoiceDTOForChanceToSwitchToRevisionStatus());
    	
		final Button okButton = new Button(this.messages.get("application.common.button.save.label"));/*"Cancelar"*/
		okButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		saleInvoiceDTO.setValue_added_tax_10_amount(saleInvoiceItemDTOTableTotalsLayout.getTotalValueAdded_10_Tax());
		            		saleInvoiceDTO.setValue_added_tax_5_amount(saleInvoiceItemDTOTableTotalsLayout.getTotalValueAdded_5_Tax());
		            		saleInvoiceDTO.setExempt_amount(saleInvoiceItemDTOTableTotalsLayout.getExcemptTotal());
		            		
		            		saleInvoiceRegisterFormView.updateSaleInvoiceDTO(saleInvoiceDTO);
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		okButton.setVisible(false
				/*bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
				.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "edit.revision.status.sale.invoice")
				&& this.saleInvoiceDTO.getStatus().equals("application.common.status.revision")*/);
    	
		final Button cancelButton = new Button(this.messages.get("application.common.button.cancel.label"));/*"Cancelar"*/
		cancelButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		saleInvoiceRegisterFormView.navigateToCallerView();
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		

    	final Button downloadButton = new Button();
    	downloadButton.setId("downloadButtonId" + "vSaleInvoiceDTO");
    	downloadButton.setPrimaryStyleName(".dashboard .myCustomyInvisibleButtonCssRule");
    	//downloadButton.setVisible(false);
    	this.saleInvoiceRegisterFormView.setUpDownloadButton(downloadButton);
		final Button printButton = new Button(this.messages.get("application.common.button.print.label"));/*"Cancelar"*/
		printButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		saleInvoiceRegisterFormView.printSaleInvoiceDTO(saleInvoiceDTO);
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		printButton.setVisible(this.saleInvoiceDTO.getStatus().equals("application.common.status.partial.balance")
				|| 
				this.saleInvoiceDTO.getStatus().equals("application.common.status.canceled"));
		final HorizontalLayout okCancelHorizontalLayout = new HorizontalLayout();
		okCancelHorizontalLayout.setMargin(true);
		okCancelHorizontalLayout.setSpacing(true);
		okCancelHorizontalLayout.setSizeFull();
		okCancelHorizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		final HorizontalLayout wrapperHorizontalLayout = new HorizontalLayout();
		wrapperHorizontalLayout.setSpacing(true);
		
		wrapperHorizontalLayout.addComponent(downloadButton);
		wrapperHorizontalLayout.addComponent(printButton);
		wrapperHorizontalLayout.addComponent(annulSaleInvoiceButton);
		wrapperHorizontalLayout.addComponent(regenerateSaleInvoiceButton);
		wrapperHorizontalLayout.addComponent(switchToRevisionStatusButton);
		wrapperHorizontalLayout.addComponent(okButton);
		wrapperHorizontalLayout.addComponent(cancelButton);
		okCancelHorizontalLayout.addComponent(wrapperHorizontalLayout);
		return okCancelHorizontalLayout;
    }

	@Override
	public String getSaleInvoiceDTOStatus() {
		// TODO Auto-generated method stub
		return this.saleInvoiceDTO.getStatus();
	}

	@Override
	public void editQuantity(final SaleInvoiceItemDTO vSaleInvoiceItemDTO) {
		// TODO Auto-generated method stub
		if(vSaleInvoiceItemDTO.getPrevious_quantity() == null)vSaleInvoiceItemDTO.setPrevious_quantity(vSaleInvoiceItemDTO.getQuantity());
		this.editQuantityWindow = 
				new EditQuantityWindow(
						this.messages.get("application.common.table.column.operations.edit") 
						+ " " + this.messages.get("application.common.quantity.label").toLowerCase(), 
						vSaleInvoiceItemDTO.getProductDTO().getProduct_id(), 
						vSaleInvoiceItemDTO.getPrevious_quantity(),
						1L,
						vSaleInvoiceItemDTO.getPrevious_quantity());
		this.editQuantityWindow.addCloseListener(this.setUpEditQuantityWindowCloseListener(vSaleInvoiceItemDTO));
		this.editQuantityWindow.adjuntWindowSizeAccordingToCientDisplay();
	}

	@Override
	public void reBuildTableAndTotalsPanel() {
		// TODO Auto-generated method stub
		this.setUpLayoutContent();
	}

	private CloseListener setUpEditQuantityWindowCloseListener(final SaleInvoiceItemDTO vSaleInvoiceItemDTO){
		return new CloseListener(){
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				try{					
					logger.info("\n******************************"
								+"\n the sale invoice item quantity editor window has been closed"
								+"\n******************************");
			    	//to recalculate table totals
					vSaleInvoiceItemDTO.setQuantity(editQuantityWindow.getQuantityEditor().getQuantity());
					vSaleInvoiceItemDTO.setValue_added_tax_10_unit_price_amount(vSaleInvoiceItemDTO.getUnit_price_amount().multiply(BigDecimal.valueOf(vSaleInvoiceItemDTO.getQuantity())));
					vSaleInvoiceItemDTO.setExempt_unit_price_amount(BigDecimal.ZERO);
					vSaleInvoiceItemDTO.setValue_added_tax_5_unit_price_amount(BigDecimal.ZERO);
					logger.info( "\n =================================================="
								+"\n" + vSaleInvoiceItemDTO.toString()
								+"\n ==================================================");
					setUpLayoutContent();
				}catch(Exception ex){
					logger.error("\n error",e);
				}
			}			
		};
	}
	
	private boolean checkSaleInvoiceDTOForChanceToSwitchToRevisionStatus(){
		if((/*this.saleInvoiceDTO.getStatus().equals("application.common.status.pending")		
		||*/ this.saleInvoiceDTO.getStatus().equals("application.common.status.payment.in.progress"))
		&& (this.saleInvoiceDTO.getPrevious_status() == null || !this.saleInvoiceDTO.getPrevious_status().equals("application.common.status.revision"))
		)
			return true;
		else return false;
	}
}
