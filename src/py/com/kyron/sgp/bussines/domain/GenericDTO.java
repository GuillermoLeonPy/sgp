package py.com.kyron.sgp.bussines.domain;

import java.util.Date;

public abstract class GenericDTO {

	private Long id;
	private String creation_user;
	private Date creation_date;
	private String last_modif_user;
	private Date last_modif_date;
	
	public GenericDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public GenericDTO(Long id, String creation_user, Date creation_date,
			String last_modif_user, Date last_modif_date) {
		super();
		this.id = id;
		this.creation_user = creation_user;
		this.creation_date = creation_date;
		this.last_modif_user = last_modif_user;
		this.last_modif_date = last_modif_date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreation_user() {
		return creation_user;
	}

	public void setCreation_user(String creation_user) {
		this.creation_user = creation_user;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}

	public String getLast_modif_user() {
		return last_modif_user;
	}

	public void setLast_modif_user(String last_modif_user) {
		this.last_modif_user = last_modif_user;
	}

	public Date getLast_modif_date() {
		return last_modif_date;
	}

	public void setLast_modif_date(Date last_modif_date) {
		this.last_modif_date = last_modif_date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericDTO other = (GenericDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}
