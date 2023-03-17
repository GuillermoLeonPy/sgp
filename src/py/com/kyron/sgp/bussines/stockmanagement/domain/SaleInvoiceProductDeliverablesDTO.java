package py.com.kyron.sgp.bussines.stockmanagement.domain;

import java.util.Date;
import java.util.List;

import py.com.kyron.sgp.bussines.domain.GenericDTO;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;

public class SaleInvoiceProductDeliverablesDTO extends GenericDTO {

	private Long id_sale_invoice;
	private String invoice_identifier_number;
	private String invoice_status;
	private Long id_person;
	private Long invoice_product_physical_quantity_in_stock;
	private Long invoice_product_delivered_quantity;
	private Long invoice_product_returned_quantity_stock;
	private Long id_order;
	private Long order_identifier_number;
	private String order_status;
	private Long invoice_product_exigible_quantity;
	private Long order_product_canceled_entering_production;
	private Long order_product_quantity_pending_to_production;
	private Long order_product_quantity_in_progress;
	private Long order_product_finished_quantity;
	private Long product_deposit_movement_identifier_number;
	private PersonDTO personDTO;
	private List<SaleInvoiceItemProductDeliverablesDTO> listSaleInvoiceItemProductDeliverablesDTO;
	private Boolean transactionRealized;
	
	public SaleInvoiceProductDeliverablesDTO() {
		// TODO Auto-generated constructor stub
	}

	public SaleInvoiceProductDeliverablesDTO(String invoice_identifier_number) {
		// TODO Auto-generated constructor stub
		this.invoice_identifier_number = invoice_identifier_number;
	}
	
	public SaleInvoiceProductDeliverablesDTO(Long id_sale_invoice) {
		// TODO Auto-generated constructor stub
		this.id_sale_invoice = id_sale_invoice;
	}
	
	public SaleInvoiceProductDeliverablesDTO(Long id_sale_invoice,Long id_person) {
		// TODO Auto-generated constructor stub
		this.id_person = id_person;
	}

	public SaleInvoiceProductDeliverablesDTO(Long id, String creation_user,
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
	 * @return the invoice_identifier_number
	 */
	public String getInvoice_identifier_number() {
		return invoice_identifier_number;
	}

	/**
	 * @param invoice_identifier_number the invoice_identifier_number to set
	 */
	public void setInvoice_identifier_number(String invoice_identifier_number) {
		this.invoice_identifier_number = invoice_identifier_number;
	}

	/**
	 * @return the invoice_status
	 */
	public String getInvoice_status() {
		return invoice_status;
	}

	/**
	 * @param invoice_status the invoice_status to set
	 */
	public void setInvoice_status(String invoice_status) {
		this.invoice_status = invoice_status;
	}

	/**
	 * @return the invoice_product_physical_quantity_in_stock
	 */
	public Long getInvoice_product_physical_quantity_in_stock() {
		return invoice_product_physical_quantity_in_stock;
	}

	/**
	 * @param invoice_product_physical_quantity_in_stock the invoice_product_physical_quantity_in_stock to set
	 */
	public void setInvoice_product_physical_quantity_in_stock(
			Long invoice_product_physical_quantity_in_stock) {
		this.invoice_product_physical_quantity_in_stock = invoice_product_physical_quantity_in_stock;
	}

	/**
	 * @return the invoice_product_delivered_quantity
	 */
	public Long getInvoice_product_delivered_quantity() {
		return invoice_product_delivered_quantity;
	}

	/**
	 * @param invoice_product_delivered_quantity the invoice_product_delivered_quantity to set
	 */
	public void setInvoice_product_delivered_quantity(
			Long invoice_product_delivered_quantity) {
		this.invoice_product_delivered_quantity = invoice_product_delivered_quantity;
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
	 * @return the order_status
	 */
	public String getOrder_status() {
		return order_status;
	}

	/**
	 * @param order_status the order_status to set
	 */
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	/**
	 * @return the invoice_product_exigible_quantity
	 */
	public Long getInvoice_product_exigible_quantity() {
		return invoice_product_exigible_quantity;
	}

	/**
	 * @param invoice_product_exigible_quantity the invoice_product_exigible_quantity to set
	 */
	public void setInvoice_product_exigible_quantity(
			Long invoice_product_exigible_quantity) {
		this.invoice_product_exigible_quantity = invoice_product_exigible_quantity;
	}

	/**
	 * @return the order_product_quantity_pending_to_production
	 */
	public Long getOrder_product_quantity_pending_to_production() {
		return order_product_quantity_pending_to_production;
	}

	/**
	 * @param order_product_quantity_pending_to_production the order_product_quantity_pending_to_production to set
	 */
	public void setOrder_product_quantity_pending_to_production(
			Long order_product_quantity_pending_to_production) {
		this.order_product_quantity_pending_to_production = order_product_quantity_pending_to_production;
	}

	/**
	 * @return the order_product_quantity_in_progress
	 */
	public Long getOrder_product_quantity_in_progress() {
		return order_product_quantity_in_progress;
	}

	/**
	 * @param order_product_quantity_in_progress the order_product_quantity_in_progress to set
	 */
	public void setOrder_product_quantity_in_progress(
			Long order_product_quantity_in_progress) {
		this.order_product_quantity_in_progress = order_product_quantity_in_progress;
	}

	/**
	 * @return the order_product_finished_quantity
	 */
	public Long getOrder_product_finished_quantity() {
		return order_product_finished_quantity;
	}

	/**
	 * @param order_product_finished_quantity the order_product_finished_quantity to set
	 */
	public void setOrder_product_finished_quantity(
			Long order_product_finished_quantity) {
		this.order_product_finished_quantity = order_product_finished_quantity;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SaleInvoiceProductDeliverablesDTO [id_sale_invoice="
				+ id_sale_invoice + ", invoice_identifier_number="
				+ invoice_identifier_number + ", invoice_status="
				+ invoice_status + ", id_person=" + id_person
				+ ", invoice_product_physical_quantity_in_stock="
				+ invoice_product_physical_quantity_in_stock
				+ ", invoice_product_delivered_quantity="
				+ invoice_product_delivered_quantity
				+ ", invoice_product_returned_quantity_stock="
				+ invoice_product_returned_quantity_stock + ", id_order="
				+ id_order + ", order_identifier_number="
				+ order_identifier_number + ", order_status=" + order_status
				+ ", invoice_product_exigible_quantity="
				+ invoice_product_exigible_quantity
				+ ", order_product_canceled_entering_production="
				+ order_product_canceled_entering_production
				+ ", order_product_quantity_pending_to_production="
				+ order_product_quantity_pending_to_production
				+ ", order_product_quantity_in_progress="
				+ order_product_quantity_in_progress
				+ ", order_product_finished_quantity="
				+ order_product_finished_quantity
				+ ", product_deposit_movement_identifier_number="
				+ product_deposit_movement_identifier_number
				+ ", transactionRealized=" + transactionRealized + ", getId()="
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
				+ ((this.id_sale_invoice == null) ? 0 : this.id_sale_invoice.hashCode());
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
		SaleInvoiceProductDeliverablesDTO other = (SaleInvoiceProductDeliverablesDTO) obj;
		if (this.id_sale_invoice == null) {
			if (other.id_sale_invoice != null)
				return false;
		} else if (!this.id_sale_invoice.equals(other.id_sale_invoice))
			return false;
		return true;
	}

	/**
	 * @return the listSaleInvoiceItemProductDeliverablesDTO
	 */
	public List<SaleInvoiceItemProductDeliverablesDTO> getListSaleInvoiceItemProductDeliverablesDTO() {
		return listSaleInvoiceItemProductDeliverablesDTO;
	}

	/**
	 * @param listSaleInvoiceItemProductDeliverablesDTO the listSaleInvoiceItemProductDeliverablesDTO to set
	 */
	public void setListSaleInvoiceItemProductDeliverablesDTO(
			List<SaleInvoiceItemProductDeliverablesDTO> listSaleInvoiceItemProductDeliverablesDTO) {
		this.listSaleInvoiceItemProductDeliverablesDTO = listSaleInvoiceItemProductDeliverablesDTO;
	}

	/**
	 * @return the id_person
	 */
	public Long getId_person() {
		return id_person;
	}

	/**
	 * @param id_person the id_person to set
	 */
	public void setId_person(Long id_person) {
		this.id_person = id_person;
	}

	/**
	 * @return the personDTO
	 */
	public PersonDTO getPersonDTO() {
		return personDTO;
	}

	/**
	 * @param personDTO the personDTO to set
	 */
	public void setPersonDTO(PersonDTO personDTO) {
		this.personDTO = personDTO;
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
	 * @return the transactionRealized
	 */
	public Boolean getTransactionRealized() {
		return transactionRealized;
	}

	/**
	 * @param transactionRealized the transactionRealized to set
	 */
	public void setTransactionRealized(Boolean transactionRealized) {
		this.transactionRealized = transactionRealized;
	}

	/**
	 * @return the order_product_canceled_entering_production
	 */
	public Long getOrder_product_canceled_entering_production() {
		return order_product_canceled_entering_production;
	}

	/**
	 * @param order_product_canceled_entering_production the order_product_canceled_entering_production to set
	 */
	public void setOrder_product_canceled_entering_production(
			Long order_product_canceled_entering_production) {
		this.order_product_canceled_entering_production = order_product_canceled_entering_production;
	}

	/**
	 * @return the invoice_product_returned_quantity_stock
	 */
	public Long getInvoice_product_returned_quantity_stock() {
		return invoice_product_returned_quantity_stock;
	}

	/**
	 * @param invoice_product_returned_quantity_stock the invoice_product_returned_quantity_stock to set
	 */
	public void setInvoice_product_returned_quantity_stock(
			Long invoice_product_returned_quantity_stock) {
		this.invoice_product_returned_quantity_stock = invoice_product_returned_quantity_stock;
	}

}
