/**
 * 
 */
package py.com.kyron.sgp.bussines.productionmanagement.domain;

import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

/**
 * @author testuser
 *
 */
public class MeasurmentUnitDTO extends GenericDTO {

	@Size(min=5, max=100)
	private String measurment_unit_id;
	@Size(min=9, max=250)
	private String measurment_unit_description;
	
	
	private String rawMaterialDescription;
	/**
	 * 
	 */
	public MeasurmentUnitDTO() {
		// TODO Auto-generated constructor stub
	}

	public MeasurmentUnitDTO(Long id) {
		super();
		this.setId(id);
	}
	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public MeasurmentUnitDTO(Long id, String creation_user, Date creation_date,
			String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	public String getMeasurment_unit_id() {
		return measurment_unit_id;
	}

	public void setMeasurment_unit_id(String measurment_unit_id) {
		this.measurment_unit_id = measurment_unit_id;
	}

	public String getMeasurment_unit_description() {
		return measurment_unit_description;
	}

	public void setMeasurment_unit_description(String measurment_unit_description) {
		this.measurment_unit_description = measurment_unit_description;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MeasurmentUnitDTO [measurment_unit_id=" + measurment_unit_id
				+ ", measurment_unit_description="
				+ measurment_unit_description + ", rawMaterialDescription="
				+ rawMaterialDescription + ", getId()=" + getId()
				+ ", getCreation_user()=" + getCreation_user()
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
		result = prime
				* result
				+ ((this.getId() == null) ? 0 : this.getId()
						.hashCode());
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
		MeasurmentUnitDTO other = (MeasurmentUnitDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @return the rawMaterialDescription
	 */
	public String getRawMaterialDescription() {
		return rawMaterialDescription;
	}

	/**
	 * @param rawMaterialDescription the rawMaterialDescription to set
	 */
	public void setRawMaterialDescription(String rawMaterialDescription) {
		this.rawMaterialDescription = rawMaterialDescription;
	}



}
