/**
 * 
 */
package py.com.kyron.sgp.bussines.cash.movements.management.domain;

import java.util.Date;
import java.util.List;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

/**
 * @author testuser
 *
 */
public class BranchOfficeDTO extends GenericDTO {

	private Long id_company;
	private String id_code_ruc;
	private String description;
	private String telephone_number;
	private List<BranchOfficeSaleStationDTO> listBranchOfficeSaleStationDTO;
	/**
	 * 
	 */
	public BranchOfficeDTO() {
		// TODO Auto-generated constructor stub
	}

	public BranchOfficeDTO(Long id) {
		super();
		this.setId(id);
	}
	
	public BranchOfficeDTO(Long id,Long id_company) {
		// TODO Auto-generated constructor stub
		this.id_company = id_company;
	}
	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public BranchOfficeDTO(Long id, String creation_user, Date creation_date,
			String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id_company
	 */
	public Long getId_company() {
		return id_company;
	}

	/**
	 * @param id_company the id_company to set
	 */
	public void setId_company(Long id_company) {
		this.id_company = id_company;
	}

	/**
	 * @return the id_code_ruc
	 */
	public String getId_code_ruc() {
		return id_code_ruc;
	}

	/**
	 * @param id_code_ruc the id_code_ruc to set
	 */
	public void setId_code_ruc(String id_code_ruc) {
		this.id_code_ruc = id_code_ruc;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the telephone_number
	 */
	public String getTelephone_number() {
		return telephone_number;
	}

	/**
	 * @param telephone_number the telephone_number to set
	 */
	public void setTelephone_number(String telephone_number) {
		this.telephone_number = telephone_number;
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
		BranchOfficeDTO other = (BranchOfficeDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BranchOfficeDTO [id_company=" + id_company + ", id_code_ruc="
				+ id_code_ruc + ", description=" + description
				+ ", telephone_number=" + telephone_number + ", getId()="
				+ getId() + ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", getClass()=" + getClass() + ", toString()="
				+ super.toString() + "]";
	}

	/**
	 * @return the listBranchOfficeSaleStationDTO
	 */
	public List<BranchOfficeSaleStationDTO> getListBranchOfficeSaleStationDTO() {
		return listBranchOfficeSaleStationDTO;
	}

	/**
	 * @param listBranchOfficeSaleStationDTO the listBranchOfficeSaleStationDTO to set
	 */
	public void setListBranchOfficeSaleStationDTO(
			List<BranchOfficeSaleStationDTO> listBranchOfficeSaleStationDTO) {
		this.listBranchOfficeSaleStationDTO = listBranchOfficeSaleStationDTO;
	}

}
