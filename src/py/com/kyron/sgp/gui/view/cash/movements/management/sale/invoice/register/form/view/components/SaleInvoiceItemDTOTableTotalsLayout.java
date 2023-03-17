package py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.register.form.view.components;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceItemDTO;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.utils.TableNumericColumnCellLabelHelper;

import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class SaleInvoiceItemDTOTableTotalsLayout extends HorizontalLayout {

	private final Logger logger = LoggerFactory.getLogger(SaleInvoiceItemDTOTableTotalsLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME;
    private final List<SaleInvoiceItemDTO> listSaleInvoiceItemDTO;
    private BigDecimal totalValueAdded_10_Tax;
    private BigDecimal totalValueAdded_5_Tax;
    private BigDecimal totalValueAddedTax;
    private BigDecimal excemptTotal;
    private Label totalValueAddedTaxTextField;
    private Label totalValueAdded_10_TaxTextField;
    private Label totalValueAdded_5_TaxTextField;
    private Label excemptTotalTextField;
    private Label valueAddedTaxTextField;
    private Label valueAdded_10_TaxTextField;
    private Label valueAdded_5_TaxTextField;
    private BigDecimal credit_order_payment_condition_surcharge_percentage;
    
	public SaleInvoiceItemDTOTableTotalsLayout(
			final String VIEW_NAME,
			final List<SaleInvoiceItemDTO> listSaleInvoiceItemDTO,
			final BigDecimal credit_order_payment_condition_surcharge_percentage) {
		// TODO Auto-generated constructor stub
		this.VIEW_NAME = VIEW_NAME;
		this.listSaleInvoiceItemDTO = listSaleInvoiceItemDTO;
		this.credit_order_payment_condition_surcharge_percentage = credit_order_payment_condition_surcharge_percentage;
		try{
			logger.info("\n SaleInvoiceItemDTOTableTotalsLayout()...");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			//this.setSpacing(true);
			//this.setMargin(true);
			Responsive.makeResponsive(this);
			DashboardEventBus.register(this);
			this.setUpValueAddedTaxTotals();
			this.setUpValueAddedTaxTotalsTextFields();
			this.setUpValueAddedTaxTextFields();
			VerticalLayout col0 = new VerticalLayout(					
					this.totalValueAdded_5_TaxTextField,
					this.valueAdded_5_TaxTextField);
			col0.setSpacing(true);
			col0.setMargin(new MarginInfo(false, false, false, true));
			
			VerticalLayout col1 = new VerticalLayout(
					this.totalValueAdded_10_TaxTextField,					
					this.valueAdded_10_TaxTextField);
			col1.setSpacing(true);
			col1.setMargin(new MarginInfo(false, true, false, true));

			VerticalLayout col2 = new VerticalLayout(
					this.totalValueAddedTaxTextField,
					this.valueAddedTaxTextField);
			col2.setSpacing(true);
			col2.setMargin(new MarginInfo(false, true, false, true));
			
			VerticalLayout col3 = new VerticalLayout(
					this.excemptTotalTextField);
			col3.setSpacing(true);
			col3.setMargin(new MarginInfo(false, true, false, true));
			this.addComponent(col0);
			this.addComponent(col1);
			this.addComponent(col2);
			this.addComponent(col3);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public SaleInvoiceItemDTOTableTotalsLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/

	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void setUpValueAddedTaxTotals(){		
		this.totalValueAdded_10_Tax = BigDecimal.ZERO;
		this.totalValueAdded_5_Tax = BigDecimal.ZERO;
		this.excemptTotal = BigDecimal.ZERO;
		if(this.listSaleInvoiceItemDTO != null)
			for(SaleInvoiceItemDTO vSaleInvoiceItemDTO: this.listSaleInvoiceItemDTO){
				if(vSaleInvoiceItemDTO.getValue_added_tax_10_unit_price_amount()!=null 
						&& !vSaleInvoiceItemDTO.getStatus().equals("application.common.status.discarded") 
						/*&& vSaleInvoiceItemDTO.getPrevious_status() == null*/)
					this.totalValueAdded_10_Tax = totalValueAdded_10_Tax.add(vSaleInvoiceItemDTO.getValue_added_tax_10_unit_price_amount());
				if(vSaleInvoiceItemDTO.getValue_added_tax_5_unit_price_amount()!=null
						&& !vSaleInvoiceItemDTO.getStatus().equals("application.common.status.discarded") 
						/*&& vSaleInvoiceItemDTO.getPrevious_status() == null*/)
					this.totalValueAdded_5_Tax = totalValueAdded_5_Tax.add(vSaleInvoiceItemDTO.getValue_added_tax_5_unit_price_amount());
				if(vSaleInvoiceItemDTO.getExempt_unit_price_amount()!=null
						&& !vSaleInvoiceItemDTO.getStatus().equals("application.common.status.discarded") 
						/*&& vSaleInvoiceItemDTO.getPrevious_status() == null*/)
					this.excemptTotal = this.excemptTotal.add(vSaleInvoiceItemDTO.getExempt_unit_price_amount());
			}
		
		if(this.credit_order_payment_condition_surcharge_percentage == null)this.credit_order_payment_condition_surcharge_percentage = BigDecimal.ZERO;
		
		this.totalValueAdded_10_Tax=
				this.totalValueAdded_10_Tax.add(
						this.totalValueAdded_10_Tax.multiply(
								this.credit_order_payment_condition_surcharge_percentage.divide(new BigDecimal(100l))
															)
												);
		this.totalValueAdded_5_Tax=
				this.totalValueAdded_5_Tax.add(
						this.totalValueAdded_5_Tax.multiply(
								this.credit_order_payment_condition_surcharge_percentage.divide(new BigDecimal(100l))
															)
												);
		this.excemptTotal=
				this.excemptTotal.add(
						this.excemptTotal.multiply(
								this.credit_order_payment_condition_surcharge_percentage.divide(new BigDecimal(100l))
															)
												);
		this.totalValueAddedTax = this.totalValueAdded_10_Tax.add(this.totalValueAdded_5_Tax);
	}
	
	private void setUpValueAddedTaxTotalsTextFields(){
		this.totalValueAddedTaxTextField = 
		TableNumericColumnCellLabelHelper.buildNumericLabelByBigDecimalNumber(this.totalValueAddedTax);
		this.totalValueAddedTaxTextField.setCaption(this.messages.get("application.common.text.field.total.value.added.tax"));
		
		this.totalValueAdded_5_TaxTextField = 
		TableNumericColumnCellLabelHelper.buildNumericLabelByBigDecimalNumber(this.totalValueAdded_5_Tax);
		this.totalValueAdded_5_TaxTextField.setCaption(this.messages.get("application.common.text.field.total.value.added.tax.5"));
		
		this.totalValueAdded_10_TaxTextField = 
		TableNumericColumnCellLabelHelper.buildNumericLabelByBigDecimalNumber(this.totalValueAdded_10_Tax);
		this.totalValueAdded_10_TaxTextField.setCaption(this.messages.get("application.common.text.field.total.value.added.tax.10"));
		
		this.excemptTotalTextField = 
		TableNumericColumnCellLabelHelper.buildNumericLabelByBigDecimalNumber(this.excemptTotal);
		this.excemptTotalTextField.setCaption(this.messages.get("application.common.text.field.total.value.excempt.tax"));
	}
	
	private void setUpValueAddedTaxTextFields(){
		this.valueAdded_5_TaxTextField = 
		TableNumericColumnCellLabelHelper.buildNumericLabelByBigDecimalNumber(
		this.totalValueAdded_5_Tax.multiply(BigDecimal.valueOf(0.047619)));
		this.valueAdded_5_TaxTextField.setCaption(this.messages.get("application.common.text.field.value.added.tax.5"));
		
		this.valueAdded_10_TaxTextField = 
		TableNumericColumnCellLabelHelper.buildNumericLabelByBigDecimalNumber(
		this.totalValueAdded_10_Tax.multiply(BigDecimal.valueOf(0.090909)));
		this.valueAdded_10_TaxTextField.setCaption(this.messages.get("application.common.text.field.value.added.tax.10"));
		
		this.valueAddedTaxTextField = 
		TableNumericColumnCellLabelHelper.buildNumericLabelByBigDecimalNumber(
		this.totalValueAdded_10_Tax.multiply(BigDecimal.valueOf(0.090909)).add(
				this.totalValueAdded_5_Tax.multiply(BigDecimal.valueOf(0.047619))
				)
		);
		this.valueAddedTaxTextField.setCaption(this.messages.get("application.common.text.field.value.added.tax"));		
	}

	/**
	 * @return the totalValueAdded_10_Tax
	 */
	public BigDecimal getTotalValueAdded_10_Tax() {
		return totalValueAdded_10_Tax;
	}

	/**
	 * @param totalValueAdded_10_Tax the totalValueAdded_10_Tax to set
	 */
	public void setTotalValueAdded_10_Tax(BigDecimal totalValueAdded_10_Tax) {
		this.totalValueAdded_10_Tax = totalValueAdded_10_Tax;
	}

	/**
	 * @return the totalValueAdded_5_Tax
	 */
	public BigDecimal getTotalValueAdded_5_Tax() {
		return totalValueAdded_5_Tax;
	}

	/**
	 * @param totalValueAdded_5_Tax the totalValueAdded_5_Tax to set
	 */
	public void setTotalValueAdded_5_Tax(BigDecimal totalValueAdded_5_Tax) {
		this.totalValueAdded_5_Tax = totalValueAdded_5_Tax;
	}

	/**
	 * @return the totalValueAddedTax
	 */
	public BigDecimal getTotalValueAddedTax() {
		return totalValueAddedTax;
	}

	/**
	 * @param totalValueAddedTax the totalValueAddedTax to set
	 */
	public void setTotalValueAddedTax(BigDecimal totalValueAddedTax) {
		this.totalValueAddedTax = totalValueAddedTax;
	}

	/**
	 * @return the excemptTotal
	 */
	public BigDecimal getExcemptTotal() {
		return excemptTotal;
	}

	/**
	 * @param excemptTotal the excemptTotal to set
	 */
	public void setExcemptTotal(BigDecimal excemptTotal) {
		this.excemptTotal = excemptTotal;
	}
}
