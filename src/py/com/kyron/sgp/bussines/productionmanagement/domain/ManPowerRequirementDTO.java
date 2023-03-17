/**
 * 
 */
package py.com.kyron.sgp.bussines.productionmanagement.domain;

import java.util.Date;

import org.hibernate.validator.constraints.Range;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

/**
 * @author testuser
 *
 */
public class ManPowerRequirementDTO extends GenericDTO {

	private Long id_production_process_activity;
	@Range(min=1L,max=120L)
	private Long minutes_quantity;
	private Date registration_date;
	private Date validity_end_date;
	private Boolean is_active;
	private Long temporal_id;
	
	/**
	 * 
	 */
	public ManPowerRequirementDTO() {
		// TODO Auto-generated constructor stub
	}

	public ManPowerRequirementDTO(Long id) {
		super();
		this.setId(id);
	}
	
	public ManPowerRequirementDTO(Long id,Long id_production_process_activity) {
		super();
		this.setId(id);
		this.id_production_process_activity = id_production_process_activity;
	}
	
	public ManPowerRequirementDTO(Long id,Long id_production_process_activity,Long temporal_id) {
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
	public ManPowerRequirementDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
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
	 * @return the minutes_quantity
	 */
	public Long getMinutes_quantity() {
		return minutes_quantity;
	}

	/**
	 * @param minutes_quantity the minutes_quantity to set
	 */
	public void setMinutes_quantity(Long minutes_quantity) {
		this.minutes_quantity = minutes_quantity;
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
	 * @see java.lang.Object#hashCode()
	 */
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
		ManPowerRequirementDTO other = (ManPowerRequirementDTO) obj;
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
