package py.com.kyron.sgp.bussines.stockmanagement.domain;

import java.math.BigDecimal;

public class InsufficiencyRawMaterialReportDTO {

	private String raw_material_id;
	private String measurment_unit_id;
	private BigDecimal required_quantity;
	private BigDecimal cost_amount;
	private String required_quantity_formatted;
	private String cost_amount_formatted;
	
	public InsufficiencyRawMaterialReportDTO() {
		// TODO Auto-generated constructor stub
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
	 * @return the required_quantity
	 */
	public BigDecimal getRequired_quantity() {
		return required_quantity;
	}

	/**
	 * @param required_quantity the required_quantity to set
	 */
	public void setRequired_quantity(BigDecimal required_quantity) {
		this.required_quantity = required_quantity;
	}

	/**
	 * @return the cost_amount
	 */
	public BigDecimal getCost_amount() {
		return cost_amount;
	}

	/**
	 * @param cost_amount the cost_amount to set
	 */
	public void setCost_amount(BigDecimal cost_amount) {
		this.cost_amount = cost_amount;
	}

	/**
	 * @return the required_quantity_formatted
	 */
	public String getRequired_quantity_formatted() {
		return required_quantity_formatted;
	}

	/**
	 * @param required_quantity_formatted the required_quantity_formatted to set
	 */
	public void setRequired_quantity_formatted(String required_quantity_formatted) {
		this.required_quantity_formatted = required_quantity_formatted;
	}

	/**
	 * @return the cost_amount_formatted
	 */
	public String getCost_amount_formatted() {
		return cost_amount_formatted;
	}

	/**
	 * @param cost_amount_formatted the cost_amount_formatted to set
	 */
	public void setCost_amount_formatted(String cost_amount_formatted) {
		this.cost_amount_formatted = cost_amount_formatted;
	}

}
