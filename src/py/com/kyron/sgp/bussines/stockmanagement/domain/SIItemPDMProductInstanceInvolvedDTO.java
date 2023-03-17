package py.com.kyron.sgp.bussines.stockmanagement.domain;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class SIItemPDMProductInstanceInvolvedDTO extends GenericDTO{

	private Long id_si_item_product_deposit_movement;
	private Long product_instance_unique_number;
	private Boolean returnUnit;
	
	/**/
	private Long product_deposit_movement_identifier_number;
	private Long id_sale_invoice;
	private Long id_sale_invoice_item;
	private Long id_credit_note;
	private Long id_credit_note_item;
	private Long return_quantity;
	
	
	public SIItemPDMProductInstanceInvolvedDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public SIItemPDMProductInstanceInvolvedDTO(Long id_si_item_product_deposit_movement) {
		// TODO Auto-generated constructor stub
		this.id_si_item_product_deposit_movement = id_si_item_product_deposit_movement;
	}

	/**
	 * @return the id_si_item_product_deposit_movement
	 */
	public Long getId_si_item_product_deposit_movement() {
		return id_si_item_product_deposit_movement;
	}

	/**
	 * @param id_si_item_product_deposit_movement the id_si_item_product_deposit_movement to set
	 */
	public void setId_si_item_product_deposit_movement(
			Long id_si_item_product_deposit_movement) {
		this.id_si_item_product_deposit_movement = id_si_item_product_deposit_movement;
	}

	/**
	 * @return the product_instance_unique_number
	 */
	public Long getProduct_instance_unique_number() {
		return product_instance_unique_number;
	}

	/**
	 * @param product_instance_unique_number the product_instance_unique_number to set
	 */
	public void setProduct_instance_unique_number(
			Long product_instance_unique_number) {
		this.product_instance_unique_number = product_instance_unique_number;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SIItemPDMProductInstanceInvolvedDTO [id_si_item_product_deposit_movement="
				+ id_si_item_product_deposit_movement
				+ ", product_instance_unique_number="
				+ product_instance_unique_number
				+ ", returnUnit="
				+ returnUnit
				+ ", product_deposit_movement_identifier_number="
				+ product_deposit_movement_identifier_number
				+ ", id_sale_invoice="
				+ id_sale_invoice
				+ ", id_sale_invoice_item="
				+ id_sale_invoice_item
				+ ", id_credit_note="
				+ id_credit_note
				+ ", id_credit_note_item="
				+ id_credit_note_item
				+ ", return_quantity="
				+ return_quantity
				+ ", getClass()="
				+ getClass() + ", toString()=" + super.toString() + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((this.product_instance_unique_number == null) ? 0 : this.product_instance_unique_number.hashCode());
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
		SIItemPDMProductInstanceInvolvedDTO other = (SIItemPDMProductInstanceInvolvedDTO) obj;
		if (this.product_instance_unique_number == null) {
			if (other.product_instance_unique_number != null)
				return false;
		} else if (!this.product_instance_unique_number.equals(other.product_instance_unique_number))
			return false;
		return true;
	}

	/**
	 * @return the returnUnit
	 */
	public Boolean getReturnUnit() {
		return returnUnit;
	}

	/**
	 * @param returnUnit the returnUnit to set
	 */
	public void setReturnUnit(Boolean returnUnit) {
		this.returnUnit = returnUnit;
	}

	/**
	 * @return the product_deposit_movement_identifier_number
	 */
	public Long getProduct_deposit_movement_identifier_number() {
		return product_deposit_movement_identifier_number;
	}

	/**
	 * @param product_deposit_movement_identifier_number the product_deposit_movement_identifier_number to set
	 */
	public void setProduct_deposit_movement_identifier_number(
			Long product_deposit_movement_identifier_number) {
		this.product_deposit_movement_identifier_number = product_deposit_movement_identifier_number;
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
	 * @return the id_credit_note_item
	 */
	public Long getId_credit_note_item() {
		return id_credit_note_item;
	}

	/**
	 * @param id_credit_note_item the id_credit_note_item to set
	 */
	public void setId_credit_note_item(Long id_credit_note_item) {
		this.id_credit_note_item = id_credit_note_item;
	}

	/**
	 * @return the return_quantity
	 */
	public Long getReturn_quantity() {
		return return_quantity;
	}

	/**
	 * @param return_quantity the return_quantity to set
	 */
	public void setReturn_quantity(Long return_quantity) {
		this.return_quantity = return_quantity;
	}
}
