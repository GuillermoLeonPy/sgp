package py.com.kyron.sgp.bussines.productionmanagement.domain;

import java.util.Date;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class OrderItemRawMaterialSufficiencyReportDTO extends GenericDTO {

	
	private Long id_order_raw_material_sufficiency_report;
	private Long id_order_item;
	private Long id_order;
	private Long id_product;
	private String product_id;
	private String product_description;
	private Long item_quantity;
	private Long entered_into_producction_quantity;
	private Long in_progress_quantity;
	private Long pending_to_instanciate_quantity;
	private Long canceled_entering_production_by_document_quantity;					

	
	public OrderItemRawMaterialSufficiencyReportDTO() {
		// TODO Auto-generated constructor stub
	}

	public OrderItemRawMaterialSufficiencyReportDTO(Long id_order_raw_material_sufficiency_report) {
		// TODO Auto-generated constructor stub
		this.id_order_raw_material_sufficiency_report = id_order_raw_material_sufficiency_report;
	}
	
	public OrderItemRawMaterialSufficiencyReportDTO(Long id,
			String creation_user, Date creation_date, String last_modif_user,
			Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id_order_raw_material_sufficiency_report
	 */
	public Long getId_order_raw_material_sufficiency_report() {
		return id_order_raw_material_sufficiency_report;
	}

	/**
	 * @param id_order_raw_material_sufficiency_report the id_order_raw_material_sufficiency_report to set
	 */
	public void setId_order_raw_material_sufficiency_report(
			Long id_order_raw_material_sufficiency_report) {
		this.id_order_raw_material_sufficiency_report = id_order_raw_material_sufficiency_report;
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
	 * @return the product_description
	 */
	public String getProduct_description() {
		return product_description;
	}

	/**
	 * @param product_description the product_description to set
	 */
	public void setProduct_description(String product_description) {
		this.product_description = product_description;
	}

	/**
	 * @return the item_quantity
	 */
	public Long getItem_quantity() {
		return item_quantity;
	}

	/**
	 * @param item_quantity the item_quantity to set
	 */
	public void setItem_quantity(Long item_quantity) {
		this.item_quantity = item_quantity;
	}

	/**
	 * @return the entered_into_producction_quantity
	 */
	public Long getEntered_into_producction_quantity() {
		return entered_into_producction_quantity;
	}

	/**
	 * @param entered_into_producction_quantity the entered_into_producction_quantity to set
	 */
	public void setEntered_into_producction_quantity(
			Long entered_into_producction_quantity) {
		this.entered_into_producction_quantity = entered_into_producction_quantity;
	}

	/**
	 * @return the pending_to_instanciate_quantity
	 */
	public Long getPending_to_instanciate_quantity() {
		return pending_to_instanciate_quantity;
	}

	/**
	 * @param pending_to_instanciate_quantity the pending_to_instanciate_quantity to set
	 */
	public void setPending_to_instanciate_quantity(
			Long pending_to_instanciate_quantity) {
		this.pending_to_instanciate_quantity = pending_to_instanciate_quantity;
	}

	/**
	 * @return the canceled_entering_production_by_document_quantity
	 */
	public Long getCanceled_entering_production_by_document_quantity() {
		return canceled_entering_production_by_document_quantity;
	}

	/**
	 * @param canceled_entering_production_by_document_quantity the canceled_entering_production_by_document_quantity to set
	 */
	public void setCanceled_entering_production_by_document_quantity(
			Long canceled_entering_production_by_document_quantity) {
		this.canceled_entering_production_by_document_quantity = canceled_entering_production_by_document_quantity;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrderItemRawMaterialSufficiencyReportDTO [id_order_raw_material_sufficiency_report="
				+ id_order_raw_material_sufficiency_report
				+ ", id_order_item="
				+ id_order_item
				+ ", id_order="
				+ id_order
				+ ", id_product="
				+ id_product
				+ ", product_id="
				+ product_id
				+ ", product_description="
				+ product_description
				+ ", item_quantity="
				+ item_quantity
				+ ", entered_into_producction_quantity="
				+ entered_into_producction_quantity
				+ ", pending_to_instanciate_quantity="
				+ pending_to_instanciate_quantity
				+ ", canceled_entering_production_by_document_quantity="
				+ canceled_entering_production_by_document_quantity
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
				+ ", toString()="
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
		OrderItemRawMaterialSufficiencyReportDTO other = (OrderItemRawMaterialSufficiencyReportDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @return the in_progress_quantity
	 */
	public Long getIn_progress_quantity() {
		return in_progress_quantity;
	}

	/**
	 * @param in_progress_quantity the in_progress_quantity to set
	 */
	public void setIn_progress_quantity(Long in_progress_quantity) {
		this.in_progress_quantity = in_progress_quantity;
	}
}
