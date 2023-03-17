package py.com.kyron.sgp.bussines.applicationsecurity.domain;

import java.util.Date;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class ApplicationSecurityProgramDTO extends GenericDTO {

	private String program_key;
	private String program_key_value;
	
	public ApplicationSecurityProgramDTO() {
		// TODO Auto-generated constructor stub
	}

	public ApplicationSecurityProgramDTO(Long id) {
		// TODO Auto-generated constructor stub
		super();
		super.setId(id);
	}
	
	public ApplicationSecurityProgramDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	public String getProgram_key() {
		return program_key;
	}

	public void setProgram_key(String program_key) {
		this.program_key = program_key;
	}

	@Override
	public String toString() {
		return "ApplicationSecurityProgramDTO [program_key=" + program_key
				+ ", program_key_value=" + program_key_value + ", getId()="
				+ getId() + ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

	public String getProgram_key_value() {
		return program_key_value;
	}

	public void setProgram_key_value(String program_key_value) {
		this.program_key_value = program_key_value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((program_key == null) ? 0 : program_key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApplicationSecurityProgramDTO other = (ApplicationSecurityProgramDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

}
