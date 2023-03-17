package py.com.kyron.sgp.bussines.stockmanagement.domain;

import java.util.Date;
import java.util.List;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class SaleInvoiceItemProductDepositMovementDTO extends GenericDTO {

	private Long id_sale_invoice_product_deposit_movement;
	private Long id_sale_invoice_item;
	private Long id_product;
	private String product_id;
	private Long quantity;
	private List<SIItemPDMProductInstanceInvolvedDTO> listSIItemPDMProductInstanceInvolvedDTO;
	
	public SaleInvoiceItemProductDepositMovementDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public SaleInvoiceItemProductDepositMovementDTO(Long id_sale_invoice_product_deposit_movement) {
		// TODO Auto-generated constructor stub
		this.id_sale_invoice_product_deposit_movement = id_sale_invoice_product_deposit_movement;
	}

	public SaleInvoiceItemProductDepositMovementDTO(Long id,
			String creation_user, Date creation_date, String last_modif_user,
			Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id_sale_invoice_product_deposit_movement
	 */
	public Long getId_sale_invoice_product_deposit_movement() {
		return id_sale_invoice_product_deposit_movement;
	}

	/**
	 * @param id_sale_invoice_product_deposit_movement the id_sale_invoice_product_deposit_movement to set
	 */
	public void setId_sale_invoice_product_deposit_movement(
			Long id_sale_invoice_product_deposit_movement) {
		this.id_sale_invoice_product_deposit_movement = id_sale_invoice_product_deposit_movement;
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
	 * @return the product_id
	 */
	public String getProduct_id() {
		return product_id;
	}

	/**
	 * @param product_id the product_id to set
	 */
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SaleInvoiceItemProductDepositMovementDTO [id_sale_invoice_product_deposit_movement="
				+ id_sale_invoice_product_deposit_movement
				+ ", id_sale_invoice_item="
				+ id_sale_invoice_item
				+ ", id_product="
				+ id_product
				+ ", product_id="
				+ product_id
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
		SaleInvoiceItemProductDepositMovementDTO other = (SaleInvoiceItemProductDepositMovementDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @return the listSIItemPDMProductInstanceInvolvedDTO
	 */
	public List<SIItemPDMProductInstanceInvolvedDTO> getListSIItemPDMProductInstanceInvolvedDTO() {
		return listSIItemPDMProductInstanceInvolvedDTO;
	}

	/**
	 * @param listSIItemPDMProductInstanceInvolvedDTO the listSIItemPDMProductInstanceInvolvedDTO to set
	 */
	public void setListSIItemPDMProductInstanceInvolvedDTO(
			List<SIItemPDMProductInstanceInvolvedDTO> listSIItemPDMProductInstanceInvolvedDTO) {
		this.listSIItemPDMProductInstanceInvolvedDTO = listSIItemPDMProductInstanceInvolvedDTO;
	}
}
