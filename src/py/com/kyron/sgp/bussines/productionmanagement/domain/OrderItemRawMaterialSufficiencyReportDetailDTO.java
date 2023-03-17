package py.com.kyron.sgp.bussines.productionmanagement.domain;

import java.math.BigDecimal;
import java.util.Date;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class OrderItemRawMaterialSufficiencyReportDetailDTO extends GenericDTO {

	
	private Long id_order_item_raw_material_sufficiency_report;
	private Long id_order;
	private Long id_order_item;
	private Long id_raw_material;
	private String raw_material_id;
	private Long id_measurment_unit;
	private String measurment_unit_id;
	private BigDecimal available_quantity;
	private BigDecimal sum_required_quantity;
	private BigDecimal missing_quantity;
	
	public OrderItemRawMaterialSufficiencyReportDetailDTO() {
		// TODO Auto-generated constructor stub
	}

	public OrderItemRawMaterialSufficiencyReportDetailDTO(Long id,
			String creation_user, Date creation_date, String last_modif_user,
			Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id_order_item_raw_material_sufficiency_report
	 */
	public Long getId_order_item_raw_material_sufficiency_report() {
		return id_order_item_raw_material_sufficiency_report;
	}

	/**
	 * @param id_order_item_raw_material_sufficiency_report the id_order_item_raw_material_sufficiency_report to set
	 */
	public void setId_order_item_raw_material_sufficiency_report(
			Long id_order_item_raw_material_sufficiency_report) {
		this.id_order_item_raw_material_sufficiency_report = id_order_item_raw_material_sufficiency_report;
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
	 * @return the id_raw_material
	 */
	public Long getId_raw_material() {
		return id_raw_material;
	}

	/**
	 * @param id_raw_material the id_raw_material to set
	 */
	public void setId_raw_material(Long id_raw_material) {
		this.id_raw_material = id_raw_material;
	}

	/**
	 * @return the raw_material_id
	 */
	public String getRaw_material_id() {
		return raw_material_id;
	}

	/**
	 * @param raw_material_id the raw_material_id to set
	 */
	public void setRaw_material_id(String raw_material_id) {
		this.raw_material_id = raw_material_id;
	}

	/**
	 * @return the id_measurment_unit
	 */
	public Long getId_measurment_unit() {
		return id_measurment_unit;
	}

	/**
	 * @param id_measurment_unit the id_measurment_unit to set
	 */
	public void setId_measurment_unit(Long id_measurment_unit) {
		this.id_measurment_unit = id_measurment_unit;
	}

	/**
	 * @return the measurment_unit_id
	 */
	public String getMeasurment_unit_id() {
		return measurment_unit_id;
	}

	/**
	 * @param measurment_unit_id the measurment_unit_id to set
	 */
	public void setMeasurment_unit_id(String measurment_unit_id) {
		this.measurment_unit_id = measurment_unit_id;
	}

	/**
	 * @return the available_quantity
	 */
	public BigDecimal getAvailable_quantity() {
		return available_quantity;
	}

	/**
	 * @param available_quantity the available_quantity to set
	 */
	public void setAvailable_quantity(BigDecimal available_quantity) {
		this.available_quantity = available_quantity;
	}

	/**
	 * @return the sum_required_quantity
	 */
	public BigDecimal getSum_required_quantity() {
		return sum_required_quantity;
	}

	/**
	 * @param sum_required_quantity the sum_required_quantity to set
	 */
	public void setSum_required_quantity(BigDecimal sum_required_quantity) {
		this.sum_required_quantity = sum_required_quantity;
	}

	/**
	 * @return the missing_quantity
	 */
	public BigDecimal getMissing_quantity() {
		return missing_quantity;
	}

	/**
	 * @param missing_quantity the missing_quantity to set
	 */
	public void setMissing_quantity(BigDecimal missing_quantity) {
		this.missing_quantity = missing_quantity;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrderItemRawMaterialSufficiencyReportDetailDTO [id_order_item_raw_material_sufficiency_report="
				+ id_order_item_raw_material_sufficiency_report
				+ ", id_order="
				+ id_order
				+ ", id_order_item="
				+ id_order_item
				+ ", id_raw_material="
				+ id_raw_material
				+ ", raw_material_id="
				+ raw_material_id
				+ ", id_measurment_unit="
				+ id_measurment_unit
				+ ", measurment_unit_id="
				+ measurment_unit_id
				+ ", available_quantity="
				+ available_quantity
				+ ", sum_required_quantity="
				+ sum_required_quantity
				+ ", missing_quantity="
				+ missing_quantity
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
		OrderItemRawMaterialSufficiencyReportDetailDTO other = (OrderItemRawMaterialSufficiencyReportDetailDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}
}
