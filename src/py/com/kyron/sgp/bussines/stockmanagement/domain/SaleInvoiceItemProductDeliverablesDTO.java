package py.com.kyron.sgp.bussines.stockmanagement.domain;

import java.util.Date;
import java.util.List;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class SaleInvoiceItemProductDeliverablesDTO extends GenericDTO {

	private Long id_order;
	private Long id_order_item;
	private Long id_sale_invoice;
	private Long id_sale_invoice_item;
	private Long id_product;
	private String product_id;
	private Long ord_item_canceled_entering_production;
	private Long ord_item_pending_to_production;
	private Long ord_item_in_progress_quantity;
	private Long invoice_item_total_exigible_quantity;
	private Long invoice_item_remain_exigible_quantity;
	private Long invoice_item_delivered_quantity;
	private Long invoice_item_product_stock_quantity;
	private Long invoice_item_returned_quantity_stock;
	private Long deliver_quantity;
	private Long product_deposit_movement_identifier_number;	
	private List<SIItemPDMProductInstanceInvolvedDTO> listSIItemPDMProductInstanceInvolvedDTO;
	private String ord_item_canceled_entering_production_formatted;
	private String ord_item_pending_to_production_formatted;
	private String ord_item_in_progress_quantity_formatted;
	private String invoice_item_total_exigible_quantity_formatted;
	private String invoice_item_remain_exigible_quantity_formatted;
	private String invoice_item_delivered_quantity_formatted;
	private String invoice_item_product_stock_quantity_formatted;
	private String invoice_item_returned_quantity_stock_formatted;
	
	public SaleInvoiceItemProductDeliverablesDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public SaleInvoiceItemProductDeliverablesDTO(Long id_sale_invoice) {
		// TODO Auto-generated constructor stub
		this.id_sale_invoice = id_sale_invoice;
	}
	
	public SaleInvoiceItemProductDeliverablesDTO(Long id_sale_invoice,Long id_sale_invoice_item) {
		// TODO Auto-generated constructor stub
		this.id_sale_invoice_item = id_sale_invoice_item;
	}

	public SaleInvoiceItemProductDeliverablesDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
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
	 * @return the id_order_item
	 */
	public Long getId_order_item() {
		return id_order_item;
	}

	/**
	 * @param id_order_item the id_order_item to set
	 */
	public void setId_order_item(Long id_order_item) {
		this.id_order_item = id_order_item;
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
	 * @return the ord_item_pending_to_production
	 */
	public Long getOrd_item_pending_to_production() {
		return ord_item_pending_to_production;
	}

	/**
	 * @param ord_item_pending_to_production the ord_item_pending_to_production to set
	 */
	public void setOrd_item_pending_to_production(
			Long ord_item_pending_to_production) {
		this.ord_item_pending_to_production = ord_item_pending_to_production;
	}

	/**
	 * @return the ord_item_in_progress_quantity
	 */
	public Long getOrd_item_in_progress_quantity() {
		return ord_item_in_progress_quantity;
	}

	/**
	 * @param ord_item_in_progress_quantity the ord_item_in_progress_quantity to set
	 */
	public void setOrd_item_in_progress_quantity(Long ord_item_in_progress_quantity) {
		this.ord_item_in_progress_quantity = ord_item_in_progress_quantity;
	}

	/**
	 * @return the invoice_item_total_exigible_quantity
	 */
	public Long getInvoice_item_total_exigible_quantity() {
		return invoice_item_total_exigible_quantity;
	}

	/**
	 * @param invoice_item_total_exigible_quantity the invoice_item_total_exigible_quantity to set
	 */
	public void setInvoice_item_total_exigible_quantity(
			Long invoice_item_total_exigible_quantity) {
		this.invoice_item_total_exigible_quantity = invoice_item_total_exigible_quantity;
	}

	/**
	 * @return the invoice_item_remain_exigible_quantity
	 */
	public Long getInvoice_item_remain_exigible_quantity() {
		return invoice_item_remain_exigible_quantity;
	}

	/**
	 * @param invoice_item_remain_exigible_quantity the invoice_item_remain_exigible_quantity to set
	 */
	public void setInvoice_item_remain_exigible_quantity(
			Long invoice_item_remain_exigible_quantity) {
		this.invoice_item_remain_exigible_quantity = invoice_item_remain_exigible_quantity;
	}

	/**
	 * @return the invoice_item_delivered_quantity
	 */
	public Long getInvoice_item_delivered_quantity() {
		return invoice_item_delivered_quantity;
	}

	/**
	 * @param invoice_item_delivered_quantity the invoice_item_delivered_quantity to set
	 */
	public void setInvoice_item_delivered_quantity(
			Long invoice_item_delivered_quantity) {
		this.invoice_item_delivered_quantity = invoice_item_delivered_quantity;
	}

	/**
	 * @return the invoice_item_product_stock_quantity
	 */
	public Long getInvoice_item_product_stock_quantity() {
		return invoice_item_product_stock_quantity;
	}

	/**
	 * @param invoice_item_product_stock_quantity the invoice_item_product_stock_quantity to set
	 */
	public void setInvoice_item_product_stock_quantity(
			Long invoice_item_product_stock_quantity) {
		this.invoice_item_product_stock_quantity = invoice_item_product_stock_quantity;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SaleInvoiceItemProductDeliverablesDTO [id_order=" + id_order
				+ ", id_order_item=" + id_order_item + ", id_sale_invoice="
				+ id_sale_invoice + ", id_sale_invoice_item="
				+ id_sale_invoice_item + ", id_product=" + id_product
				+ ", product_id=" + product_id
				+ ", ord_item_canceled_entering_production="
				+ ord_item_canceled_entering_production
				+ ", ord_item_pending_to_production="
				+ ord_item_pending_to_production
				+ ", ord_item_in_progress_quantity="
				+ ord_item_in_progress_quantity
				+ ", invoice_item_total_exigible_quantity="
				+ invoice_item_total_exigible_quantity
				+ ", invoice_item_remain_exigible_quantity="
				+ invoice_item_remain_exigible_quantity
				+ ", invoice_item_delivered_quantity="
				+ invoice_item_delivered_quantity
				+ ", invoice_item_product_stock_quantity="
				+ invoice_item_product_stock_quantity
				+ ", invoice_item_returned_quantity_stock="
				+ invoice_item_returned_quantity_stock + ", deliver_quantity="
				+ deliver_quantity
				+ ", product_deposit_movement_identifier_number="
				+ product_deposit_movement_identifier_number + ", getId()="
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
				+ ((this.id_sale_invoice_item == null) ? 0 : this.id_sale_invoice_item.hashCode());
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
		SaleInvoiceItemProductDeliverablesDTO other = (SaleInvoiceItemProductDeliverablesDTO) obj;
		if (this.id_sale_invoice_item == null) {
			if (other.id_sale_invoice_item != null)
				return false;
		} else if (!this.id_sale_invoice_item.equals(other.id_sale_invoice_item))
			return false;
		return true;
	}

	/**
	 * @return the deliver_quantity
	 */
	public Long getDeliver_quantity() {
		return deliver_quantity;
	}

	/**
	 * @param deliver_quantity the deliver_quantity to set
	 */
	public void setDeliver_quantity(Long deliver_quantity) {
		this.deliver_quantity = deliver_quantity;
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

	/**
	 * @return the ord_item_canceled_entering_production
	 */
	public Long getOrd_item_canceled_entering_production() {
		return ord_item_canceled_entering_production;
	}

	/**
	 * @param ord_item_canceled_entering_production the ord_item_canceled_entering_production to set
	 */
	public void setOrd_item_canceled_entering_production(
			Long ord_item_canceled_entering_production) {
		this.ord_item_canceled_entering_production = ord_item_canceled_entering_production;
	}

	/**
	 * @return the invoice_item_returned_quantity_stock
	 */
	public Long getInvoice_item_returned_quantity_stock() {
		return invoice_item_returned_quantity_stock;
	}

	/**
	 * @param invoice_item_returned_quantity_stock the invoice_item_returned_quantity_stock to set
	 */
	public void setInvoice_item_returned_quantity_stock(
			Long invoice_item_returned_quantity_stock) {
		this.invoice_item_returned_quantity_stock = invoice_item_returned_quantity_stock;
	}

	/**
	 * @return the ord_item_canceled_entering_production_formatted
	 */
	public String getOrd_item_canceled_entering_production_formatted() {
		return ord_item_canceled_entering_production_formatted;
	}

	/**
	 * @param ord_item_canceled_entering_production_formatted the ord_item_canceled_entering_production_formatted to set
	 */
	public void setOrd_item_canceled_entering_production_formatted(
			String ord_item_canceled_entering_production_formatted) {
		this.ord_item_canceled_entering_production_formatted = ord_item_canceled_entering_production_formatted;
	}

	/**
	 * @return the ord_item_pending_to_production_formatted
	 */
	public String getOrd_item_pending_to_production_formatted() {
		return ord_item_pending_to_production_formatted;
	}

	/**
	 * @param ord_item_pending_to_production_formatted the ord_item_pending_to_production_formatted to set
	 */
	public void setOrd_item_pending_to_production_formatted(
			String ord_item_pending_to_production_formatted) {
		this.ord_item_pending_to_production_formatted = ord_item_pending_to_production_formatted;
	}

	/**
	 * @return the ord_item_in_progress_quantity_formatted
	 */
	public String getOrd_item_in_progress_quantity_formatted() {
		return ord_item_in_progress_quantity_formatted;
	}

	/**
	 * @param ord_item_in_progress_quantity_formatted the ord_item_in_progress_quantity_formatted to set
	 */
	public void setOrd_item_in_progress_quantity_formatted(
			String ord_item_in_progress_quantity_formatted) {
		this.ord_item_in_progress_quantity_formatted = ord_item_in_progress_quantity_formatted;
	}

	/**
	 * @return the invoice_item_total_exigible_quantity_formatted
	 */
	public String getInvoice_item_total_exigible_quantity_formatted() {
		return invoice_item_total_exigible_quantity_formatted;
	}

	/**
	 * @param invoice_item_total_exigible_quantity_formatted the invoice_item_total_exigible_quantity_formatted to set
	 */
	public void setInvoice_item_total_exigible_quantity_formatted(
			String invoice_item_total_exigible_quantity_formatted) {
		this.invoice_item_total_exigible_quantity_formatted = invoice_item_total_exigible_quantity_formatted;
	}

	/**
	 * @return the invoice_item_remain_exigible_quantity_formatted
	 */
	public String getInvoice_item_remain_exigible_quantity_formatted() {
		return invoice_item_remain_exigible_quantity_formatted;
	}

	/**
	 * @param invoice_item_remain_exigible_quantity_formatted the invoice_item_remain_exigible_quantity_formatted to set
	 */
	public void setInvoice_item_remain_exigible_quantity_formatted(
			String invoice_item_remain_exigible_quantity_formatted) {
		this.invoice_item_remain_exigible_quantity_formatted = invoice_item_remain_exigible_quantity_formatted;
	}

	/**
	 * @return the invoice_item_delivered_quantity_formatted
	 */
	public String getInvoice_item_delivered_quantity_formatted() {
		return invoice_item_delivered_quantity_formatted;
	}

	/**
	 * @param invoice_item_delivered_quantity_formatted the invoice_item_delivered_quantity_formatted to set
	 */
	public void setInvoice_item_delivered_quantity_formatted(
			String invoice_item_delivered_quantity_formatted) {
		this.invoice_item_delivered_quantity_formatted = invoice_item_delivered_quantity_formatted;
	}

	/**
	 * @return the invoice_item_product_stock_quantity_formatted
	 */
	public String getInvoice_item_product_stock_quantity_formatted() {
		return invoice_item_product_stock_quantity_formatted;
	}

	/**
	 * @param invoice_item_product_stock_quantity_formatted the invoice_item_product_stock_quantity_formatted to set
	 */
	public void setInvoice_item_product_stock_quantity_formatted(
			String invoice_item_product_stock_quantity_formatted) {
		this.invoice_item_product_stock_quantity_formatted = invoice_item_product_stock_quantity_formatted;
	}

	/**
	 * @return the invoice_item_returned_quantity_stock_formatted
	 */
	public String getInvoice_item_returned_quantity_stock_formatted() {
		return invoice_item_returned_quantity_stock_formatted;
	}

	/**
	 * @param invoice_item_returned_quantity_stock_formatted the invoice_item_returned_quantity_stock_formatted to set
	 */
	public void setInvoice_item_returned_quantity_stock_formatted(
			String invoice_item_returned_quantity_stock_formatted) {
		this.invoice_item_returned_quantity_stock_formatted = invoice_item_returned_quantity_stock_formatted;
	}
}
