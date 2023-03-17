/**
 * 
 */
package py.com.kyron.sgp.bussines.productionmanagement.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import org.hibernate.validator.constraints.Range;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

/**
 * @author testuser
 *
 */
public class RawMaterialRequirementDTO extends GenericDTO {

	private Long id_raw_material;
	private Long id_measurment_unit;
	private Long id_production_process_activity;
	//@Range(min=0,max=9999L)
	@DecimalMax(value="9999.99")
	@DecimalMin(value="0.1")
	private BigDecimal quantity;
	private Date registration_date;
	private Date validity_end_date;
	private Boolean is_active;
	private MeasurmentUnitDTO measurmentUnitDTO;
	private RawMaterialDTO rawMaterialDTO;
	private Long temporal_id;
	/**
	 * 
	 */
	public RawMaterialRequirementDTO() {
		// TODO Auto-generated constructor stub
	}

	public RawMaterialRequirementDTO(Long id) {
		super();
		this.setId(id);
	}
	
	public RawMaterialRequirementDTO(Long id,Long id_production_process_activity) {
		super();
		this.setId(id);
		this.id_production_process_activity = id_production_process_activity;
	}
	
	public RawMaterialRequirementDTO(Long id,Long id_production_process_activity,Long temporal_id) {
		super();
		this.setId(id);
		this.id_production_process_activity = id_production_process_activity;
		this.temporal_id = temporal_id;
	}
	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public RawMaterialRequirementDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
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
	 * @return the id_production_process_activity
	 */
	public Long getId_production_process_activity() {
		return id_production_process_activity;
	}

	/**
	 * @param id_production_process_activity the id_production_process_activity to set
	 */
	public void setId_production_process_activity(
			Long id_production_process_activity) {
		this.id_production_process_activity = id_production_process_activity;
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
	 * @return the validity_end_date
	 */
	public Date getValidity_end_date() {
		return validity_end_date;
	}

	/**
	 * @param validity_end_date the validity_end_date to set
	 */
	public void setValidity_end_date(Date validity_end_date) {
		this.validity_end_date = validity_end_date;
	}

	/**
	 * @return the is_active
	 */
	public Boolean getIs_active() {
		return is_active;
	}

	/**
	 * @param is_active the is_active to set
	 */
	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RawMaterialRequirementDTO [id_raw_material=" + id_raw_material
				+ ", id_measurment_unit=" + id_measurment_unit
				+ ", id_production_process_activity="
				+ id_production_process_activity + ", quantity=" + quantity
				+ ", registration_date=" + registration_date
				+ ", validity_end_date=" + validity_end_date + ", is_active="
				+ is_active + ", measurmentUnitDTO=" + measurmentUnitDTO
				+ ", rawMaterialDTO=" + rawMaterialDTO + ", temporal_id="
				+ temporal_id + ", getId()=" + getId()
				+ ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", getClass()=" + getClass() + ", toString()="
				+ super.toString() + "]";
	}

	/**
	 * @return the measurmentUnitDTO
	 */
	public MeasurmentUnitDTO getMeasurmentUnitDTO() {
		return measurmentUnitDTO;
	}

	/**
	 * @param measurmentUnitDTO the measurmentUnitDTO to set
	 */
	public void setMeasurmentUnitDTO(MeasurmentUnitDTO measurmentUnitDTO) {
		this.measurmentUnitDTO = measurmentUnitDTO;
	}

	/**
	 * @return the rawMaterialDTO
	 */
	public RawMaterialDTO getRawMaterialDTO() {
		return rawMaterialDTO;
	}

	/**
	 * @param rawMaterialDTO the rawMaterialDTO to set
	 */
	public void setRawMaterialDTO(RawMaterialDTO rawMaterialDTO) {
		this.rawMaterialDTO = rawMaterialDTO;
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
		result = prime * result
				+ ((temporal_id == null) ? 0 : temporal_id.hashCode());
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
		RawMaterialRequirementDTO other = (RawMaterialRequirementDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (temporal_id == null) {
			if (other.temporal_id != null)
				return false;
		} else if (!temporal_id.equals(other.temporal_id))
			return false;
		return true;
	}

	/**
	 * @return the temporal_id
	 */
	public Long getTemporal_id() {
		return temporal_id;
	}

	/**
	 * @param temporal_id the temporal_id to set
	 */
	public void setTemporal_id(Long temporal_id) {
		this.temporal_id = temporal_id;
	}

}
