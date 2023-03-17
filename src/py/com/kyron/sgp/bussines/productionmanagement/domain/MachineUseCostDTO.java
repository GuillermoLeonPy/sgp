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
public class MachineUseCostDTO extends GenericDTO {

	private Long id_machine;
	private Long id_tariff;
	@DecimalMax(value="9999.99")
	@DecimalMin(value="0.1")
	private BigDecimal tariff_amount;
	private Date registration_date;
	private Date validity_end_date;
	private Boolean active;
	private TariffDTO tariffDTO;
	
	/**
	 * 
	 */
	public MachineUseCostDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public MachineUseCostDTO(Long id) {
		super();
		this.setId(id);
	}

	public MachineUseCostDTO(Long id_machine,Long id_tariff) {
		// TODO Auto-generated constructor stub
		this.id_machine = id_machine;
		this.id_tariff = id_tariff;
	}
	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public MachineUseCostDTO(Long id, String creation_user, Date creation_date,
			String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	public Long getId_machine() {
		return id_machine;
	}

	public void setId_machine(Long id_machine) {
		this.id_machine = id_machine;
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

	@Override
	public String toString() {
		return "MachineUseCostDTO [id_machine=" + id_machine + ", id_tariff="
				+ id_tariff + ", tariff_amount=" + tariff_amount
				+ ", registration_date=" + registration_date
				+ ", validity_end_date=" + validity_end_date + ", active="
				+ active + ", tariffDTO=" + tariffDTO + ", getId()=" + getId()
				+ ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
	}

}
