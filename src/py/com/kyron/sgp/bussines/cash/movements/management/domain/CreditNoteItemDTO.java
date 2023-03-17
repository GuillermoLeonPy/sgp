package py.com.kyron.sgp.bussines.cash.movements.management.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Range;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.ProductDTO;
import py.com.kyron.sgp.bussines.domain.GenericDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SIItemPDMProductInstanceInvolvedDTO;

public class CreditNoteItemDTO extends GenericDTO {

	private Long id_credit_note;
	private Long id_product;
	private Long devolution_quantity;
	@Range(min=1L,max=999L)
	private Long cancellation_withdrawal_quantity;
	private BigDecimal unit_price_amount;
	private BigDecimal exempt_unit_price_amount;
	private BigDecimal value_added_tax_10_unit_price_amount;
	private BigDecimal value_added_tax_5_unit_price_amount;
	private Long id_sale_invoice_item;
	private String status;
	
	private ProductDTO productDTO;
	private SaleInvoiceItemDTO saleInvoiceItemDTO;
	private Long id_sale_invoice;
	private List<SIItemPDMProductInstanceInvolvedDTO> listProductInstances;
	private Long temporal_id;
	
	private String quantity_formatted;
	private String unit_price_amount_formatted;
	private String value_added_tax_10_unit_price_amount_formatted;
	
	public CreditNoteItemDTO() {
		// TODO Auto-generated constructor stub
	}

	public CreditNoteItemDTO(Long id) {
		super();
		this.setId(id);
	}

	public CreditNoteItemDTO(Long id,Long id_credit_note) {
		this.id_credit_note = id_credit_note;
	}
	
	
	/**
	 * @param saleInvoiceItemDTO
	 */
	public CreditNoteItemDTO(SaleInvoiceItemDTO saleInvoiceItemDTO) {
		super();
		this.saleInvoiceItemDTO = saleInvoiceItemDTO;
	}

	public CreditNoteItemDTO(Long id, String creation_user, Date creation_date,
			String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id_credit_note
	 */
	public Long getId_credit_note() {
		return id_credit_note;
	}

	/**
	 * @param id_credit_note the id_credit_note to set
	 */
	public void setId_credit_note(Long id_credit_note) {
		this.id_credit_note = id_credit_note;
	}

	/**
	 * @return the id_product
	 */
	public Long getId_product() {
		return id_product;
	}

	/**
	 * @param id_product the id_product to set
	 */
	public void setId_product(Long id_product) {
		this.id_product = id_product;
	}

	/**
	 * @return the unit_price_amount
	 */
	public BigDecimal getUnit_price_amount() {
		return unit_price_amount;
	}

	/**
	 * @param unit_price_amount the unit_price_amount to set
	 */
	public void setUnit_price_amount(BigDecimal unit_price_amount) {
		this.unit_price_amount = unit_price_amount;
	}

	/**
	 * @return the exempt_unit_price_amount
	 */
	public BigDecimal getExempt_unit_price_amount() {
		return exempt_unit_price_amount;
	}

	/**
	 * @param exempt_unit_price_amount the exempt_unit_price_amount to set
	 */
	public void setExempt_unit_price_amount(BigDecimal exempt_unit_price_amount) {
		this.exempt_unit_price_amount = exempt_unit_price_amount;
	}

	/**
	 * @return the value_added_tax_10_unit_price_amount
	 */
	public BigDecimal getValue_added_tax_10_unit_price_amount() {
		return value_added_tax_10_unit_price_amount;
	}

	/**
	 * @param value_added_tax_10_unit_price_amount the value_added_tax_10_unit_price_amount to set
	 */
	public void setValue_added_tax_10_unit_price_amount(
			BigDecimal value_added_tax_10_unit_price_amount) {
		this.value_added_tax_10_unit_price_amount = value_added_tax_10_unit_price_amount;
	}

	/**
	 * @return the value_added_tax_5_unit_price_amount
	 */
	public BigDecimal getValue_added_tax_5_unit_price_amount() {
		return value_added_tax_5_unit_price_amount;
	}

	/**
	 * @param value_added_tax_5_unit_price_amount the value_added_tax_5_unit_price_amount to set
	 */
	public void setValue_added_tax_5_unit_price_amount(
			BigDecimal value_added_tax_5_unit_price_amount) {
		this.value_added_tax_5_unit_price_amount = value_added_tax_5_unit_price_amount;
	}

	/**
	 * @return the id_sale_invoice_item
	 */
	public Long getId_sale_invoice_item() {
		return id_sale_invoice_item;
	}

	/**
	 * @param id_sale_invoice_item the id_sale_invoice_item to set
	 */
	public void setId_sale_invoice_item(Long id_sale_invoice_item) {
		this.id_sale_invoice_item = id_sale_invoice_item;
	}

	/**
	 * @return the productDTO
	 */
	public ProductDTO getProductDTO() {
		return productDTO;
	}

	/**
	 * @param productDTO the productDTO to set
	 */
	public void setProductDTO(ProductDTO productDTO) {
		this.productDTO = productDTO;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CreditNoteItemDTO [id_credit_note=" + id_credit_note
				+ ", id_product=" + id_product + ", devolution_quantity="
				+ devolution_quantity + ", cancellation_withdrawal_quantity="
				+ cancellation_withdrawal_quantity + ", unit_price_amount="
				+ unit_price_amount + ", exempt_unit_price_amount="
				+ exempt_unit_price_amount
				+ ", value_added_tax_10_unit_price_amount="
				+ value_added_tax_10_unit_price_amount
				+ ", value_added_tax_5_unit_price_amount="
				+ value_added_tax_5_unit_price_amount
				+ ", id_sale_invoice_item=" + id_sale_invoice_item
				+ ", status=" + status + ", productDTO=" + productDTO
				+ ", saleInvoiceItemDTO=" + saleInvoiceItemDTO
				+ ", id_sale_invoice=" + id_sale_invoice + ", getId()="
				+ getId() + ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", getClass()=" + getClass() + ", toString()="
				+ super.toString() + "]";
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result
				+ ((temporal_id == null) ? 0 : temporal_id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreditNoteItemDTO other = (CreditNoteItemDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (temporal_id == null) {
			if (other.temporal_id != null)
				return false;
		} else if (!temporal_id.equals(other.temporal_id))
			return false;
		return true;
	}

	/**
	 * @return the saleInvoiceItemDTO
	 */
	public SaleInvoiceItemDTO getSaleInvoiceItemDTO() {
		return saleInvoiceItemDTO;
	}

	/**
	 * @param saleInvoiceItemDTO the saleInvoiceItemDTO to set
	 */
	public void setSaleInvoiceItemDTO(SaleInvoiceItemDTO saleInvoiceItemDTO) {
		this.saleInvoiceItemDTO = saleInvoiceItemDTO;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the devolution_quantity
	 */
	public Long getDevolution_quantity() {
		return devolution_quantity;
	}

	/**
	 * @param devolution_quantity the devolution_quantity to set
	 */
	public void setDevolution_quantity(Long devolution_quantity) {
		this.devolution_quantity = devolution_quantity;
	}

	/**
	 * @return the cancellation_withdrawal_quantity
	 */
	public Long getCancellation_withdrawal_quantity() {
		return cancellation_withdrawal_quantity;
	}

	/**
	 * @param cancellation_withdrawal_quantity the cancellation_withdrawal_quantity to set
	 */
	public void setCancellation_withdrawal_quantity(
			Long cancellation_withdrawal_quantity) {
		this.cancellation_withdrawal_quantity = cancellation_withdrawal_quantity;
	}

	/**
	 * @return the id_sale_invoice
	 */
	public Long getId_sale_invoice() {
		return id_sale_invoice;
	}

	/**
	 * @param id_sale_invoice the id_sale_invoice to set
	 */
	public void setId_sale_invoice(Long id_sale_invoice) {
		this.id_sale_invoice = id_sale_invoice;
	}

	/**
	 * @return the listProductInstances
	 */
	public List<SIItemPDMProductInstanceInvolvedDTO> getListProductInstances() {
		return listProductInstances;
	}

	/**
	 * @param listProductInstances the listProductInstances to set
	 */
	public void setListProductInstances(
			List<SIItemPDMProductInstanceInvolvedDTO> listProductInstances) {
		this.listProductInstances = listProductInstances;
	}

	/**
	 * @return the temporal_id
	 */
	public Long getTemporal_id() {
		return temporal_id;
	}

	/**
	 * @param temporal_id the temporal_id to set
	 */
	public void setTemporal_id(Long temporal_id) {
		this.temporal_id = temporal_id;
	}

	/**
	 * @return the quantity_formatted
	 */
	public String getQuantity_formatted() {
		return quantity_formatted;
	}

	/**
	 * @param quantity_formatted the quantity_formatted to set
	 */
	public void setQuantity_formatted(String quantity_formatted) {
		this.quantity_formatted = quantity_formatted;
	}

	/**
	 * @return the unit_price_amount_formatted
	 */
	public String getUnit_price_amount_formatted() {
		return unit_price_amount_formatted;
	}

	/**
	 * @param unit_price_amount_formatted the unit_price_amount_formatted to set
	 */
	public void setUnit_price_amount_formatted(String unit_price_amount_formatted) {
		this.unit_price_amount_formatted = unit_price_amount_formatted;
	}

	/**
	 * @return the value_added_tax_10_unit_price_amount_formatted
	 */
	public String getValue_added_tax_10_unit_price_amount_formatted() {
		return value_added_tax_10_unit_price_amount_formatted;
	}

	/**
	 * @param value_added_tax_10_unit_price_amount_formatted the value_added_tax_10_unit_price_amount_formatted to set
	 */
	public void setValue_added_tax_10_unit_price_amount_formatted(
			String value_added_tax_10_unit_price_amount_formatted) {
		this.value_added_tax_10_unit_price_amount_formatted = value_added_tax_10_unit_price_amount_formatted;
	}
	
	/**
	 * @return the product_id
	 */
	public String getProduct_id() {
		return this.productDTO.getProduct_id();
	}

}
