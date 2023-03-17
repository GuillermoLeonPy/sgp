package py.com.kyron.sgp.bussines.productionmanagement.domain;

import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class RawMaterialDTO extends GenericDTO {
	@Size(min=9, max=100)
	private String raw_material_id;
	@Size(min=9, max=250)
	private String raw_material_description;
	private Boolean has_existence_record;
	private Boolean has_any_purchase_request;
	private Boolean has_pending_purchase_request;
	private Boolean has_valid_cost_registry;
	private Boolean has_existence_with_warning;
	private Boolean has_existence_out_of_stock;
	
	public RawMaterialDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public RawMaterialDTO(Long id) {
		super();
		this.setId(id);
	}

	public RawMaterialDTO(Long id, String creation_user, Date creation_date,
			String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	public String getRaw_material_id() {
		return raw_material_id;
	}

	public void setRaw_material_id(String raw_material_id) {
		this.raw_material_id = raw_material_id;
	}

	public String getRaw_material_description() {
		return raw_material_description;
	}

	public void setRaw_material_description(String raw_material_description) {
		this.raw_material_description = raw_material_description;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RawMaterialDTO [raw_material_id=" + raw_material_id
				+ ", raw_material_description=" + raw_material_description
				+ ", has_existence_record=" + has_existence_record
				+ ", has_any_purchase_request=" + has_any_purchase_request
				+ ", has_pending_purchase_request="
				+ has_pending_purchase_request + ", has_valid_cost_registry="
				+ has_valid_cost_registry + ", getId()=" + getId()
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
		result = prime * result
				+ ((raw_material_id == null) ? 0 : raw_material_id.hashCode());
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
		RawMaterialDTO other = (RawMaterialDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @return the has_existence_record
	 */
	public Boolean getHas_existence_record() {
		return has_existence_record;
	}

	/**
	 * @param has_existence_record the has_existence_record to set
	 */
	public void setHas_existence_record(Boolean has_existence_record) {
		this.has_existence_record = has_existence_record;
	}

	/**
	 * @return the has_any_purchase_request
	 */
	public Boolean getHas_any_purchase_request() {
		return has_any_purchase_request;
	}

	/**
	 * @param has_any_purchase_request the has_any_purchase_request to set
	 */
	public void setHas_any_purchase_request(Boolean has_any_purchase_request) {
		this.has_any_purchase_request = has_any_purchase_request;
	}

	/**
	 * @return the has_pending_purchase_request
	 */
	public Boolean getHas_pending_purchase_request() {
		return has_pending_purchase_request;
	}

	/**
	 * @param has_pending_purchase_request the has_pending_purchase_request to set
	 */
	public void setHas_pending_purchase_request(Boolean has_pending_purchase_request) {
		this.has_pending_purchase_request = has_pending_purchase_request;
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

	/**
	 * @return the has_existence_with_warning
	 */
	public Boolean getHas_existence_with_warning() {
		return has_existence_with_warning;
	}

	/**
	 * @param has_existence_with_warning the has_existence_with_warning to set
	 */
	public void setHas_existence_with_warning(Boolean has_existence_with_warning) {
		this.has_existence_with_warning = has_existence_with_warning;
	}

	/**
	 * @return the has_existence_out_of_stock
	 */
	public Boolean getHas_existence_out_of_stock() {
		return has_existence_out_of_stock;
	}

	/**
	 * @param has_existence_out_of_stock the has_existence_out_of_stock to set
	 */
	public void setHas_existence_out_of_stock(Boolean has_existence_out_of_stock) {
		this.has_existence_out_of_stock = has_existence_out_of_stock;
	}

}
