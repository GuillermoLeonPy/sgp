/**
 * 
 */
package py.com.kyron.sgp.bussines.comercialmanagement.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

/**
 * @author testuser
 *
 */
public class CreditOrderChargeConditionDTO extends GenericDTO {

	/**
	 * 
	 */
	@NotNull
	@Range(min=1L,max=60L)
	private Long days_interval;
	@NotNull
	@DecimalMax(value="50.00")
	@DecimalMin(value="0.1")
	private BigDecimal days_interval_percent_increment;
	private Date registration_date;
	private Date validity_end_date;
	
	public CreditOrderChargeConditionDTO() {
		// TODO Auto-generated constructor stub
	}

	public CreditOrderChargeConditionDTO(Long id) {
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
	public CreditOrderChargeConditionDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the days_interval
	 */
	public Long getDays_interval() {
		return days_interval;
	}

	/**
	 * @param days_interval the days_interval to set
	 */
	public void setDays_interval(Long days_interval) {
		this.days_interval = days_interval;
	}

	/**
	 * @return the days_interval_percent_increment
	 */
	public BigDecimal getDays_interval_percent_increment() {
		return days_interval_percent_increment;
	}

	/**
	 * @param days_interval_percent_increment the days_interval_percent_increment to set
	 */
	public void setDays_interval_percent_increment(
			BigDecimal days_interval_percent_increment) {
		this.days_interval_percent_increment = days_interval_percent_increment;
	}

	/**
	 * @return the registration_date
	 */
	public Date getRegistration_date() {
		return registration_date;
	}

	/**
	 * @param registration_date the registration_date to set
	 */
	public void setRegistration_date(Date registration_date) {
		this.registration_date = registration_date;
	}

	/**
	 * @return the validity_end_date
	 */
	public Date getValidity_end_date() {
		return validity_end_date;
	}

	/**
	 * @param validity_end_date the validity_end_date to set
	 */
	public void setValidity_end_date(Date validity_end_date) {
		this.validity_end_date = validity_end_date;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CreditOrderChargeConditionDTO [days_interval=" + days_interval
				+ ", days_interval_percent_increment="
				+ days_interval_percent_increment + ", registration_date="
				+ registration_date + ", validity_end_date="
				+ validity_end_date + ", getId()=" + getId()
				+ ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
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
		CreditOrderChargeConditionDTO other = (CreditOrderChargeConditionDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

}
