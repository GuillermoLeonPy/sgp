package py.com.kyron.sgp.bussines.comercialmanagement.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.Range;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class OrderItemDTO extends GenericDTO {

	private Long id_order;
	private Long item_number;
	private Long id_product;
	@Range(min=1L,max=999L)
	private Long quantity;
	private BigDecimal unit_price_amount;
	private BigDecimal product_unit_manufacture_cost;
	private BigDecimal exempt_unit_price_amount;
	private BigDecimal value_added_tax_10_unit_price_amount;
	private BigDecimal value_added_tax_5_unit_price_amount;
	private ProductDTO productDTO;
	private Long temporal_id;
	private String status;
	private String previous_status;
	
	/* comodin properties */
	private Long order_identifier_number;
	private Long order_id_currency;
	
	public OrderItemDTO() {
		// TODO Auto-generated constructor stub
	}

	public OrderItemDTO(Long id) {
		super();
		this.setId(id);
	}

	public OrderItemDTO(Long id,Long id_order) {
		super();
		this.setId(id);
		this.id_order = id_order;
	}

	public OrderItemDTO(Long id,Long id_order,Long temporal_id) {
		super();
		this.setId(id);
		this.id_order = id_order;
		this.temporal_id = temporal_id;
	}
	
	public OrderItemDTO(Long id, String creation_user, Date creation_date,
			String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id_order
	 */
	public Long getId_order() {
		return id_order;
	}

	/**
	 * @param id_order the id_order to set
	 */
	public void setId_order(Long id_order) {
		this.id_order = id_order;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrderItemDTO [id_order=" + id_order + ", item_number="
				+ item_number + ", id_product=" + id_product + ", quantity="
				+ quantity + ", unit_price_amount=" + unit_price_amount
				+ ", product_unit_manufacture_cost="
				+ product_unit_manufacture_cost + ", exempt_unit_price_amount="
				+ exempt_unit_price_amount
				+ ", value_added_tax_10_unit_price_amount="
				+ value_added_tax_10_unit_price_amount
				+ ", value_added_tax_5_unit_price_amount="
				+ value_added_tax_5_unit_price_amount + ", productDTO="
				+ productDTO + ", temporal_id=" + temporal_id + ", status="
				+ status + ", previous_status=" + previous_status
				+ ", order_identifier_number=" + order_identifier_number
				+ ", order_id_currency=" + order_id_currency + ", getId()="
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
		OrderItemDTO other = (OrderItemDTO) obj;
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
	 * @return the order_id_currency
	 */
	public Long getOrder_id_currency() {
		return order_id_currency;
	}

	/**
	 * @param order_id_currency the order_id_currency to set
	 */
	public void setOrder_id_currency(Long order_id_currency) {
		this.order_id_currency = order_id_currency;
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
	 * @return the order_identifier_number
	 */
	public Long getOrder_identifier_number() {
		return order_identifier_number;
	}

	/**
	 * @param order_identifier_number the order_identifier_number to set
	 */
	public void setOrder_identifier_number(Long order_identifier_number) {
		this.order_identifier_number = order_identifier_number;
	}

	/**
	 * @return the product_unit_manufacture_cost
	 */
	public BigDecimal getProduct_unit_manufacture_cost() {
		return product_unit_manufacture_cost;
	}

	/**
	 * @param product_unit_manufacture_cost the product_unit_manufacture_cost to set
	 */
	public void setProduct_unit_manufacture_cost(
			BigDecimal product_unit_manufacture_cost) {
		this.product_unit_manufacture_cost = product_unit_manufacture_cost;
	}

}
