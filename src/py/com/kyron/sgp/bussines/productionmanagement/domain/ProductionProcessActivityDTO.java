/**
 * 
 */
package py.com.kyron.sgp.bussines.productionmanagement.domain;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

/**
 * @author testuser
 *
 */
public class ProductionProcessActivityDTO extends GenericDTO {

	@Size(min=9, max=100)
	private String activity_id;
	@Size(min=9, max=250)
	private String activity_description;
	private Long id_production_process;
	private Long id_previous_activity;
	@Range(min=1,max=120L)
	private Long minutes_quantity;
	private Date registration_date;
	private Date validity_end_date;
	private List<RawMaterialRequirementDTO> listRawMaterialRequirementDTO;
	private List<ManPowerRequirementDTO> listManPowerRequirementDTO;
	private List<MachineRequirementDTO> listMachineRequirementDTO;
	private Long temporal_id;
	/**
	 * 
	 */
	public ProductionProcessActivityDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public ProductionProcessActivityDTO(Long id) {
		super();
		this.setId(id);
	}
	
	public ProductionProcessActivityDTO(Long id, Long temporal_id) {
		super();
		this.setId(id);
		this.temporal_id = temporal_id;
	}

	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public ProductionProcessActivityDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the activity_id
	 */
	public String getActivity_id() {
		return activity_id;
	}

	/**
	 * @param activity_id the activity_id to set
	 */
	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
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
	 * @return the id_production_process
	 */
	public Long getId_production_process() {
		return id_production_process;
	}

	/**
	 * @param id_production_process the id_production_process to set
	 */
	public void setId_production_process(Long id_production_process) {
		this.id_production_process = id_production_process;
	}

	/**
	 * @return the id_previous_activity
	 */
	public Long getId_previous_activity() {
		return id_previous_activity;
	}

	/**
	 * @param id_previous_activity the id_previous_activity to set
	 */
	public void setId_previous_activity(Long id_previous_activity) {
		this.id_previous_activity = id_previous_activity;
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
	 * @return the listRawMaterialRequirementDTO
	 */
	public List<RawMaterialRequirementDTO> getListRawMaterialRequirementDTO() {
		return listRawMaterialRequirementDTO;
	}

	/**
	 * @param listRawMaterialRequirementDTO the listRawMaterialRequirementDTO to set
	 */
	public void setListRawMaterialRequirementDTO(
			List<RawMaterialRequirementDTO> listRawMaterialRequirementDTO) {
		this.listRawMaterialRequirementDTO = listRawMaterialRequirementDTO;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProductionProcessActivityDTO [activity_id=" + activity_id
				+ ", activity_description=" + activity_description
				+ ", id_production_process=" + id_production_process
				+ ", id_previous_activity=" + id_previous_activity
				+ ", minutes_quantity=" + minutes_quantity
				+ ", registration_date=" + registration_date
				+ ", validity_end_date=" + validity_end_date
				+ ", listRawMaterialRequirementDTO="
				+ listRawMaterialRequirementDTO
				+ ", listManPowerRequirementDTO=" + listManPowerRequirementDTO
				+ ", listMachineRequirementDTO=" + listMachineRequirementDTO
				+ ", temporal_id=" + temporal_id + ", getId()=" + getId()
				+ ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
	}

	/**
	 * @return the listManPowerRequirementDTO
	 */
	public List<ManPowerRequirementDTO> getListManPowerRequirementDTO() {
		return listManPowerRequirementDTO;
	}

	/**
	 * @param listManPowerRequirementDTO the listManPowerRequirementDTO to set
	 */
	public void setListManPowerRequirementDTO(
			List<ManPowerRequirementDTO> listManPowerRequirementDTO) {
		this.listManPowerRequirementDTO = listManPowerRequirementDTO;
	}

	/**
	 * @return the listMachineRequirementDTO
	 */
	public List<MachineRequirementDTO> getListMachineRequirementDTO() {
		return listMachineRequirementDTO;
	}

	/**
	 * @param listMachineRequirementDTO the listMachineRequirementDTO to set
	 */
	public void setListMachineRequirementDTO(
			List<MachineRequirementDTO> listMachineRequirementDTO) {
		this.listMachineRequirementDTO = listMachineRequirementDTO;
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
		ProductionProcessActivityDTO other = (ProductionProcessActivityDTO) obj;
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

}
