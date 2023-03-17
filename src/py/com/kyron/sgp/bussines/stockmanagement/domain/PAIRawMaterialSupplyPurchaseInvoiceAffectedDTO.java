package py.com.kyron.sgp.bussines.stockmanagement.domain;

import java.math.BigDecimal;
import java.util.Date;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO extends GenericDTO {

	private Long id_pai_raw_material_supply;
	private Long id_purchase_invoice;
	private String purchase_invoice_identifier_number;
	private Long id_purchase_invoice_item;
	private BigDecimal quantity;

	
	public PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO() {
		// TODO Auto-generated constructor stub
	}

	public PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO(Long id) {
		// TODO Auto-generated constructor stub
		this.setId(id);
	}
	
	public PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO(Long id, Long id_pai_raw_material_supply) {
		// TODO Auto-generated constructor stub
		this.id_pai_raw_material_supply = id_pai_raw_material_supply;
	}
	public PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO(Long id,
			String creation_user, Date creation_date, String last_modif_user,
			Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id_pai_raw_material_supply
	 */
	public Long getId_pai_raw_material_supply() {
		return id_pai_raw_material_supply;
	}

	/**
	 * @param id_pai_raw_material_supply the id_pai_raw_material_supply to set
	 */
	public void setId_pai_raw_material_supply(Long id_pai_raw_material_supply) {
		this.id_pai_raw_material_supply = id_pai_raw_material_supply;
	}

	/**
	 * @return the id_purchase_invoice
	 */
	public Long getId_purchase_invoice() {
		return id_purchase_invoice;
	}

	/**
	 * @param id_purchase_invoice the id_purchase_invoice to set
	 */
	public void setId_purchase_invoice(Long id_purchase_invoice) {
		this.id_purchase_invoice = id_purchase_invoice;
	}

	/**
	 * @return the purchase_invoice_identifier_number
	 */
	public String getPurchase_invoice_identifier_number() {
		return purchase_invoice_identifier_number;
	}

	/**
	 * @param purchase_invoice_identifier_number the purchase_invoice_identifier_number to set
	 */
	public void setPurchase_invoice_identifier_number(
			String purchase_invoice_identifier_number) {
		this.purchase_invoice_identifier_number = purchase_invoice_identifier_number;
	}

	/**
	 * @return the id_purchase_invoice_item
	 */
	public Long getId_purchase_invoice_item() {
		return id_purchase_invoice_item;
	}

	/**
	 * @param id_purchase_invoice_item the id_purchase_invoice_item to set
	 */
	public void setId_purchase_invoice_item(Long id_purchase_invoice_item) {
		this.id_purchase_invoice_item = id_purchase_invoice_item;
	}

	/**
	 * @return the quantity
	 */
	public BigDecimal getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO [id_pai_raw_material_supply="
				+ id_pai_raw_material_supply
				+ ", id_purchase_invoice="
				+ id_purchase_invoice
				+ ", purchase_invoice_identifier_number="
				+ purchase_invoice_identifier_number
				+ ", id_purchase_invoice_item="
				+ id_purchase_invoice_item
				+ ", quantity="
				+ quantity
				+ ", getId()="
				+ getId()
				+ ", getCreation_user()="
				+ getCreation_user()
				+ ", getCreation_date()="
				+ getCreation_date()
				+ ", getLast_modif_user()="
				+ getLast_modif_user()
				+ ", getLast_modif_date()="
				+ getLast_modif_date()
				+ ", hashCode()="
				+ hashCode()
				+ ", getClass()="
				+ getClass()
				+ ", toString()=" + super.toString() + "]";
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
		PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO other = (PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}
}
