/**
 * 
 */
package py.com.kyron.sgp.bussines.productionmanagement.domain;

import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

/**
 * @author testuser
 *
 */
public class MachineDTO extends GenericDTO {
	@Size(min=9, max=100)
	private String machine_id;
	@Size(min=9, max=250)
	private String machine_description;
	private Boolean has_valid_cost_registry;
	
	/**
	 * @param machine_id
	 * @param machine_description
	 */
	public MachineDTO(Long id) {
		super();
		this.setId(id);
	}

	/**
	 * 
	 */
	public MachineDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public MachineDTO(Long id, String creation_user, Date creation_date,
			String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	public String getMachine_id() {
		return machine_id;
	}

	public void setMachine_id(String machine_id) {
		this.machine_id = machine_id;
	}

	public String getMachine_description() {
		return machine_description;
	}

	public void setMachine_description(String machine_description) {
		this.machine_description = machine_description;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MachineDTO [machine_id=" + machine_id
				+ ", machine_description=" + machine_description
				+ ", has_valid_cost_registry=" + has_valid_cost_registry
				+ ", getId()=" + getId() + ", getCreation_user()="
				+ getCreation_user() + ", getCreation_date()="
				+ getCreation_date() + ", getLast_modif_user()="
				+ getLast_modif_user() + ", getLast_modif_date()="
				+ getLast_modif_date() + ", hashCode()=" + hashCode()
				+ ", getClass()=" + getClass() + ", toString()="
				+ super.toString() + "]";
	}

	/**
	 * @return the has_valid_cost_registry
	 */
	public Boolean getHas_valid_cost_registry() {
		return has_valid_cost_registry;
	}

	/**
	 * @param has_valid_cost_registry the has_valid_cost_registry to set
	 */
	public void setHas_valid_cost_registry(Boolean has_valid_cost_registry) {
		this.has_valid_cost_registry = has_valid_cost_registry;
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
		MachineDTO other = (MachineDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

}
