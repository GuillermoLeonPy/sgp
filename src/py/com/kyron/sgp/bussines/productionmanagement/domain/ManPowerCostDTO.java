/**
 * 
 */
package py.com.kyron.sgp.bussines.productionmanagement.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import org.hibernate.validator.constraints.Range;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

/**
 * @author testuser
 *
 */
public class ManPowerCostDTO extends GenericDTO {

	private Long id_tariff;
	@DecimalMax(value="99999.99")
	@DecimalMin(value="0.1")
	private BigDecimal tariff_amount;
	private Date registration_date;
	private Date validity_end_date;
	private Boolean active;
	private TariffDTO tariffDTO;
	
	/**
	 * 
	 */
	public ManPowerCostDTO() {
		// TODO Auto-generated constructor stub
	}

	public ManPowerCostDTO(Long id) {
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
	public ManPowerCostDTO(Long id, String creation_user, Date creation_date,
			String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	public Long getId_tariff() {
		return id_tariff;
	}

	public void setId_tariff(Long id_tariff) {
		this.id_tariff = id_tariff;
	}

	public BigDecimal getTariff_amount() {
		return tariff_amount;
	}

	public void setTariff_amount(BigDecimal tariff_amount) {
		this.tariff_amount = tariff_amount;
	}

	public Date getRegistration_date() {
		return registration_date;
	}

	public void setRegistration_date(Date registration_date) {
		this.registration_date = registration_date;
	}

	public Date getValidity_end_date() {
		return validity_end_date;
	}

	public void setValidity_end_date(Date validity_end_date) {
		this.validity_end_date = validity_end_date;
	}

	@Override
	public String toString() {
		return "ManPowerCostDTO [id_tariff=" + id_tariff + ", tariff_amount="
				+ tariff_amount + ", registration_date=" + registration_date
				+ ", validity_end_date=" + validity_end_date + ", active="
				+ active + ", tariffDTO=" + tariffDTO + ", getId()=" + getId()
				+ ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}


	public TariffDTO getTariffDTO() {
		return tariffDTO;
	}

	public void setTariffDTO(TariffDTO tariffDTO) {
		this.tariffDTO = tariffDTO;
	}

}
