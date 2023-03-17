package py.com.kyron.sgp.bussines.stockmanagement.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class PAIRawMaterialSupplyDTO extends GenericDTO {

	private Long id_production_activity_instance;
	private Long id_production_activity;
	private String activity_description;
	private String process_description;
	private Long id_raw_material_requirement;
	private Long id_raw_material;
	private String raw_material_description;
	private Long id_measurment_unit;
	private String measurment_unit_description;
	private BigDecimal quantity;
	private Long id_raw_material_existence_affected;
	private Date registration_date;
	private Date raw_material_effective_departure_date;
	private List<PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO> listPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO;

	
	public PAIRawMaterialSupplyDTO() {
		// TODO Auto-generated constructor stub
	}

	public PAIRawMaterialSupplyDTO(Long id) {
		// TODO Auto-generated constructor stub
		this.setId(id);
	}
	
	public PAIRawMaterialSupplyDTO(Long id,Long id_production_activity_instance) {
		// TODO Auto-generated constructor stub
		this.id_production_activity_instance = id_production_activity_instance;
	}

	public PAIRawMaterialSupplyDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id_production_activity_instance
	 */
	public Long getId_production_activity_instance() {
		return id_production_activity_instance;
	}

	/**
	 * @param id_production_activity_instance the id_production_activity_instance to set
	 */
	public void setId_production_activity_instance(
			Long id_production_activity_instance) {
		this.id_production_activity_instance = id_production_activity_instance;
	}

	/**
	 * @return the id_production_activity
	 */
	public Long getId_production_activity() {
		return id_production_activity;
	}

	/**
	 * @param id_production_activity the id_production_activity to set
	 */
	public void setId_production_activity(Long id_production_activity) {
		this.id_production_activity = id_production_activity;
	}

	/**
	 * @return the activity_description
	 */
	public String getActivity_description() {
		return activity_description;
	}

	/**
	 * @param activity_description the activity_description to set
	 */
	public void setActivity_description(String activity_description) {
		this.activity_description = activity_description;
	}

	/**
	 * @return the process_description
	 */
	public String getProcess_description() {
		return process_description;
	}

	/**
	 * @param process_description the process_description to set
	 */
	public void setProcess_description(String process_description) {
		this.process_description = process_description;
	}

	/**
	 * @return the id_raw_material_requirement
	 */
	public Long getId_raw_material_requirement() {
		return id_raw_material_requirement;
	}

	/**
	 * @param id_raw_material_requirement the id_raw_material_requirement to set
	 */
	public void setId_raw_material_requirement(Long id_raw_material_requirement) {
		this.id_raw_material_requirement = id_raw_material_requirement;
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
	 * @return the raw_material_description
	 */
	public String getRaw_material_description() {
		return raw_material_description;
	}

	/**
	 * @param raw_material_description the raw_material_description to set
	 */
	public void setRaw_material_description(String raw_material_description) {
		this.raw_material_description = raw_material_description;
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
	 * @return the measurment_unit_description
	 */
	public String getMeasurment_unit_description() {
		return measurment_unit_description;
	}

	/**
	 * @param measurment_unit_description the measurment_unit_description to set
	 */
	public void setMeasurment_unit_description(String measurment_unit_description) {
		this.measurment_unit_description = measurment_unit_description;
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

	/**
	 * @return the id_raw_material_existence_affected
	 */
	public Long getId_raw_material_existence_affected() {
		return id_raw_material_existence_affected;
	}

	/**
	 * @param id_raw_material_existence_affected the id_raw_material_existence_affected to set
	 */
	public void setId_raw_material_existence_affected(
			Long id_raw_material_existence_affected) {
		this.id_raw_material_existence_affected = id_raw_material_existence_affected;
	}

	/**
	 * @return the registration_date
	 */
	public Date getRegistration_date() {
		return registration_date;
	}

	/**
	 * @param registration_date the registration_date to set
	 */
	public void setRegistration_date(Date registration_date) {
		this.registration_date = registration_date;
	}

	/**
	 * @return the raw_material_effective_departure_date
	 */
	public Date getRaw_material_effective_departure_date() {
		return raw_material_effective_departure_date;
	}

	/**
	 * @param raw_material_effective_departure_date the raw_material_effective_departure_date to set
	 */
	public void setRaw_material_effective_departure_date(
			Date raw_material_effective_departure_date) {
		this.raw_material_effective_departure_date = raw_material_effective_departure_date;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PAIRawMaterialSupplyDTO [id_production_activity_instance="
				+ id_production_activity_instance + ", id_production_activity="
				+ id_production_activity + ", activity_description="
				+ activity_description + ", process_description="
				+ process_description + ", id_raw_material_requirement="
				+ id_raw_material_requirement + ", id_raw_material="
				+ id_raw_material + ", raw_material_description="
				+ raw_material_description + ", id_measurment_unit="
				+ id_measurment_unit + ", measurment_unit_description="
				+ measurment_unit_description + ", quantity=" + quantity
				+ ", id_raw_material_existence_affected="
				+ id_raw_material_existence_affected + ", registration_date="
				+ registration_date
				+ ", raw_material_effective_departure_date="
				+ raw_material_effective_departure_date + ", getId()="
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
		PAIRawMaterialSupplyDTO other = (PAIRawMaterialSupplyDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @return the listPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO
	 */
	public List<PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO> getListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO() {
		return listPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO;
	}

	/**
	 * @param listPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO the listPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO to set
	 */
	public void setListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO(
			List<PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO> listPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO) {
		this.listPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO = listPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO;
	}
}
