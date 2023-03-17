package py.com.kyron.sgp.bussines.cash.movements.management.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.Range;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.ProductDTO;
import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class SaleInvoiceItemDTO extends GenericDTO {

	private Long id_sale_invoice;
	private Long item_number;
	private Long id_product;
	@Range(min=1L,max=999L)
	private Long quantity;
	private BigDecimal unit_price_amount;
	private BigDecimal exempt_unit_price_amount;
	private BigDecimal value_added_tax_10_unit_price_amount;
	private BigDecimal value_added_tax_5_unit_price_amount;
	
	private String status;
	private String previous_status;
	private Long previous_quantity;
	
	private ProductDTO productDTO;
	
	private String quantity_formatted;
	private String unit_price_amount_formatted;
	private String value_added_tax_10_unit_price_amount_formatted;
	
	public SaleInvoiceItemDTO() {
		// TODO Auto-generated constructor stub
	}

	public SaleInvoiceItemDTO(Long id) {
		super();
		this.setId(id);
	}

	public SaleInvoiceItemDTO(Long id,Long id_sale_invoice) {
		this.id_sale_invoice = id_sale_invoice;
	}
	
	public SaleInvoiceItemDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
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
	 * @return the item_number
	 */
	public Long getItem_number() {
		return item_number;
	}

	/**
	 * @param item_number the item_number to set
	 */
	public void setItem_number(Long item_number) {
		this.item_number = item_number;
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
	 * @return the quantity
	 */
	public Long getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
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
	 * @return the previous_status
	 */
	public String getPrevious_status() {
		return previous_status;
	}

	/**
	 * @param previous_status the previous_status to set
	 */
	public void setPrevious_status(String previous_status) {
		this.previous_status = previous_status;
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
		SaleInvoiceItemDTO other = (SaleInvoiceItemDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SaleInvoiceItemDTO [id_sale_invoice=" + id_sale_invoice
				+ ", item_number=" + item_number + ", id_product=" + id_product
				+ ", quantity=" + quantity + ", unit_price_amount="
				+ unit_price_amount + ", exempt_unit_price_amount="
				+ exempt_unit_price_amount
				+ ", value_added_tax_10_unit_price_amount="
				+ value_added_tax_10_unit_price_amount
				+ ", value_added_tax_5_unit_price_amount="
				+ value_added_tax_5_unit_price_amount + ", status=" + status
				+ ", previous_status=" + previous_status
				+ ", previous_quantity=" + previous_quantity + ", getId()="
				+ getId() + ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", getClass()=" + getClass() + ", toString()="
				+ super.toString() + "]";
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

	/**
	 * @return the previous_quantity
	 */
	public Long getPrevious_quantity() {
		return previous_quantity;
	}

	/**
	 * @param previous_quantity the previous_quantity to set
	 */
	public void setPrevious_quantity(Long previous_quantity) {
		this.previous_quantity = previous_quantity;
	}

	/**
	 * @return the product_id
	 */
	public String getProduct_id() {
		return this.productDTO.getProduct_id();
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
}
