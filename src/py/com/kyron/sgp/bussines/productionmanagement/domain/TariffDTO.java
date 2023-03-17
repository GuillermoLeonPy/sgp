/**
 * 
 */
package py.com.kyron.sgp.bussines.productionmanagement.domain;

import java.util.Date;

import javax.validation.constraints.Size;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

/**
 * @author testuser
 *
 */
public class TariffDTO extends GenericDTO {

	@Size(min=6, max=50)
	private String tariff_id;
	private Long id_currency;
	private Long id_measurment_unit;
	/**
	 * 
	 */
	public TariffDTO() {
		// TODO Auto-generated constructor stub
	}

	public TariffDTO(Long id) {
		super();
		this.setId(id);
	}
	
	public TariffDTO(Long id_measurment_unit, Long id_currency) {
		// TODO Auto-generated constructor stub
		this.id_measurment_unit = id_measurment_unit;
		this.id_currency = id_currency;
	}
	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public TariffDTO(Long id, String creation_user, Date creation_date,
			String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	public String getTariff_id() {
		return tariff_id;
	}

	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}

	public Long getId_currency() {
		return id_currency;
	}

	public void setId_currency(Long id_currency) {
		this.id_currency = id_currency;
	}

	public Long getId_measurment_unit() {
		return id_measurment_unit;
	}

	public void setId_measurment_unit(Long id_measurment_unit) {
		this.id_measurment_unit = id_measurment_unit;
	}

	@Override
	public String toString() {
		return "TariffDTO [tariff_id=" + tariff_id + ", id_currency="
				+ id_currency + ", id_measurment_unit=" + id_measurment_unit
				+ ", getId()=" + getId() + ", getCreation_user()="
				+ getCreation_user() + ", getCreation_date()="
				+ getCreation_date() + ", getLast_modif_user()="
				+ getLast_modif_user() + ", getLast_modif_date()="
				+ getLast_modif_date() + ", hashCode()=" + hashCode()
				+ ", getClass()=" + getClass() + ", toString()="
				+ super.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((this.getId() == null) ? 0 : this.getId().hashCode());
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
		TariffDTO other = (TariffDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

}
