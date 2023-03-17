package py.com.kyron.sgp.bussines.applicationsecurity.domain;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class ApplicationSecurityRolDTO extends GenericDTO {

	@Size(min=9, max=50)
	private String role_name;
	@Size(min=9, max=250)
	private String role_description;
	private Boolean is_editable;
	private List<ApplicationSecurityProgramDTO> roleProgramList;

	public ApplicationSecurityRolDTO() {
		// TODO Auto-generated constructor stub
	}

	public ApplicationSecurityRolDTO(Boolean is_editable) {
		// TODO Auto-generated constructor stub
		this.is_editable = is_editable;
	}
	public ApplicationSecurityRolDTO(Long id) {
		// TODO Auto-generated constructor stub
		super();
		super.setId(id);
	}
	
	public ApplicationSecurityRolDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getRole_description() {
		return role_description;
	}

	public void setRole_description(String role_description) {
		this.role_description = role_description;
	}



	@Override
	public String toString() {
		return "ApplicationSecurityRolDTO [role_name=" + role_name
				+ ", role_description=" + role_description + ", is_editable="
				+ is_editable + ", getId()=" + getId()
				+ ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

	public Boolean getIs_editable() {
		return is_editable;
	}

	public void setIs_editable(Boolean is_editable) {
		this.is_editable = is_editable;
	}

	public List<ApplicationSecurityProgramDTO> getRoleProgramList() {
		return roleProgramList;
	}

	public void setRoleProgramList(
			List<ApplicationSecurityProgramDTO> roleProgramList) {
		this.roleProgramList = roleProgramList;
	}

}
