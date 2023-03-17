package py.com.kyron.sgp.bussines.stockmanagement.domain;

import java.util.Date;
import java.util.List;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class SaleInvoiceProductDepositMovementDTO extends GenericDTO {

	private Long id_sale_invoice;
	private String sale_invoice_identifier_number;
	private Date movement_date;
	private String movement_type;
	private Long product_deposit_movement_identifier_number;
	private List<SaleInvoiceItemProductDepositMovementDTO> listSaleInvoiceItemProductDepositMovementDTO;

	
	public SaleInvoiceProductDepositMovementDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public SaleInvoiceProductDepositMovementDTO(Long product_deposit_movement_identifier_number) {
		// TODO Auto-generated constructor stub
		this.product_deposit_movement_identifier_number = product_deposit_movement_identifier_number;
	}

	public SaleInvoiceProductDepositMovementDTO(Long id, String creation_user,
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
	 * @return the sale_invoice_identifier_number
	 */
	public String getSale_invoice_identifier_number() {
		return sale_invoice_identifier_number;
	}

	/**
	 * @param sale_invoice_identifier_number the sale_invoice_identifier_number to set
	 */
	public void setSale_invoice_identifier_number(
			String sale_invoice_identifier_number) {
		this.sale_invoice_identifier_number = sale_invoice_identifier_number;
	}

	/**
	 * @return the movement_date
	 */
	public Date getMovement_date() {
		return movement_date;
	}

	/**
	 * @param movement_date the movement_date to set
	 */
	public void setMovement_date(Date movement_date) {
		this.movement_date = movement_date;
	}

	/**
	 * @return the movement_type
	 */
	public String getMovement_type() {
		return movement_type;
	}

	/**
	 * @param movement_type the movement_type to set
	 */
	public void setMovement_type(String movement_type) {
		this.movement_type = movement_type;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SaleInvoiceProductDepositMovementDTO [id_sale_invoice="
				+ id_sale_invoice + ", sale_invoice_identifier_number="
				+ sale_invoice_identifier_number + ", movement_date="
				+ movement_date + ", movement_type=" + movement_type
				+ ", product_deposit_movement_identifier_number="
				+ product_deposit_movement_identifier_number + ", getId()="
				+ getId() + ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass()
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
		SaleInvoiceProductDepositMovementDTO other = (SaleInvoiceProductDepositMovementDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @return the listSaleInvoiceItemProductDepositMovementDTO
	 */
	public List<SaleInvoiceItemProductDepositMovementDTO> getListSaleInvoiceItemProductDepositMovementDTO() {
		return listSaleInvoiceItemProductDepositMovementDTO;
	}

	/**
	 * @param listSaleInvoiceItemProductDepositMovementDTO the listSaleInvoiceItemProductDepositMovementDTO to set
	 */
	public void setListSaleInvoiceItemProductDepositMovementDTO(
			List<SaleInvoiceItemProductDepositMovementDTO> listSaleInvoiceItemProductDepositMovementDTO) {
		this.listSaleInvoiceItemProductDepositMovementDTO = listSaleInvoiceItemProductDepositMovementDTO;
	}
}
