/**
 * 
 */
package py.com.kyron.sgp.bussines.cash.movements.management.domain;

import java.util.Date;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

/**
 * @author testuser
 *
 */
public class BranchOfficeSaleStationDTO extends GenericDTO {

	private Long id_branch_office;
	private String id_code_ruc;
	private String sale_invoice_description;
	
	/**
	 * 
	 */
	public BranchOfficeSaleStationDTO() {
		// TODO Auto-generated constructor stub
	}

	public BranchOfficeSaleStationDTO(Long id) {
		super();
		this.setId(id);
	}
	
	public BranchOfficeSaleStationDTO(Long id,Long id_branch_office) {
		super();
		this.id_branch_office = id_branch_office;
	}
	
	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public BranchOfficeSaleStationDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id_branch_office
	 */
	public Long getId_branch_office() {
		return id_branch_office;
	}

	/**
	 * @param id_branch_office the id_branch_office to set
	 */
	public void setId_branch_office(Long id_branch_office) {
		this.id_branch_office = id_branch_office;
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
	 * @return the sale_invoice_description
	 */
	public String getSale_invoice_description() {
		return sale_invoice_description;
	}

	/**
	 * @param sale_invoice_description the sale_invoice_description to set
	 */
	public void setSale_invoice_description(String sale_invoice_description) {
		this.sale_invoice_description = sale_invoice_description;
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
		BranchOfficeSaleStationDTO other = (BranchOfficeSaleStationDTO) obj;
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
		return "BranchOfficeSaleStationDTO [id_branch_office="
				+ id_branch_office + ", id_code_ruc=" + id_code_ruc
				+ ", sale_invoice_description=" + sale_invoice_description
				+ ", getId()=" + getId() + ", getCreation_user()="
				+ getCreation_user() + ", getCreation_date()="
				+ getCreation_date() + ", getLast_modif_user()="
				+ getLast_modif_user() + ", getLast_modif_date()="
				+ getLast_modif_date() + ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
	}
}
