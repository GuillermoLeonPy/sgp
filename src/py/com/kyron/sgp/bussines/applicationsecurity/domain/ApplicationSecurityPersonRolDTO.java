package py.com.kyron.sgp.bussines.applicationsecurity.domain;

import java.util.Date;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class ApplicationSecurityPersonRolDTO extends GenericDTO {

	private Long id_app_sec_rol;
	private Long id_person;
	private Boolean is_editable;

	
	public ApplicationSecurityPersonRolDTO() {
		// TODO Auto-generated constructor stub
	}

	public ApplicationSecurityPersonRolDTO(Long id, Long id_person, Long id_app_sec_rol, boolean is_editable) {
		// TODO Auto-generated constructor stub
		this.setId(id);
		this.id_person = id_person;
		this.id_app_sec_rol = id_app_sec_rol;
		this.is_editable = is_editable;
	}
	
	public ApplicationSecurityPersonRolDTO(Long id, Long id_person, Long id_app_sec_rol) {
		// TODO Auto-generated constructor stub
		this.setId(id);
		this.id_person = id_person;
		this.id_app_sec_rol = id_app_sec_rol;
	}
	
	public ApplicationSecurityPersonRolDTO(Boolean is_editable) {
		// TODO Auto-generated constructor stub
		this.is_editable = is_editable;
	}
	
	public ApplicationSecurityPersonRolDTO(Long id) {
		// TODO Auto-generated constructor stub
		super();
		super.setId(id);
	}

	public ApplicationSecurityPersonRolDTO(Long id, String creation_user,
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

	public Long getId_person() {
		return id_person;
	}

	public void setId_person(Long id_person) {
		this.id_person = id_person;
	}


	@Override
	public String toString() {
		return "ApplicationSecurityPersonRolDTO [id_app_sec_rol="
				+ id_app_sec_rol + ", id_person=" + id_person
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
