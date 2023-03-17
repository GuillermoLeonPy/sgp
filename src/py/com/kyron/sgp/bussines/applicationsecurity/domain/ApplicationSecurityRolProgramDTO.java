package py.com.kyron.sgp.bussines.applicationsecurity.domain;

import java.util.Date;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class ApplicationSecurityRolProgramDTO extends GenericDTO {

	private Long id_app_sec_rol;
	private Long id_app_sec_program;
	private Boolean is_editable;

	
	public ApplicationSecurityRolProgramDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id_app_sec_rol
	 * @param id_app_sec_program
	 * @param is_editable
	 */
	public ApplicationSecurityRolProgramDTO(Long id_app_sec_rol,
			Long id_app_sec_program, Boolean is_editable) {
		super();
		this.id_app_sec_rol = id_app_sec_rol;
		this.id_app_sec_program = id_app_sec_program;
		this.is_editable = is_editable;
	}

	public ApplicationSecurityRolProgramDTO(Long id, Long id_app_sec_rol,
			Long id_app_sec_program, Boolean is_editable) {
		super();
		this.setId(id);
		this.id_app_sec_rol = id_app_sec_rol;
		this.id_app_sec_program = id_app_sec_program;
		this.is_editable = is_editable;
	}
	public ApplicationSecurityRolProgramDTO(Boolean is_editable) {
		// TODO Auto-generated constructor stub
		this.is_editable = is_editable;
	}
	
	public ApplicationSecurityRolProgramDTO(Long id) {
		// TODO Auto-generated constructor stub
		super();
		super.setId(id);
	}

	public ApplicationSecurityRolProgramDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	public Long getId_app_sec_rol() {
		return id_app_sec_rol;
	}

	public void setId_app_sec_rol(Long id_app_sec_rol) {
		this.id_app_sec_rol = id_app_sec_rol;
	}

	public Long getId_app_sec_program() {
		return id_app_sec_program;
	}

	public void setId_app_sec_program(Long id_app_sec_program) {
		this.id_app_sec_program = id_app_sec_program;
	}


	@Override
	public String toString() {
		return "ApplicationSecurityRolProgramDTO [id_app_sec_rol="
				+ id_app_sec_rol + ", id_app_sec_program=" + id_app_sec_program
				+ ", is_editable=" + is_editable + ", getId()=" + getId()
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

}
