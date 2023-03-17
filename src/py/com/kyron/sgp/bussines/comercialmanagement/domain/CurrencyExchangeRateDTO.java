/**
 * 
 */
package py.com.kyron.sgp.bussines.comercialmanagement.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.Range;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

/**
 * @author testuser
 *
 */
public class CurrencyExchangeRateDTO extends GenericDTO {

	private Long id_currency;
	private Long id_currency_local;
	@Range(min=0L,max=999999999L)
	private BigDecimal amount;
	private Date registration_date;
	private Date validity_end_date;
	private Boolean active;
	private CurrencyDTO localCurrencyDTO;
	
	/**
	 * 
	 */
	public CurrencyExchangeRateDTO() {
		// TODO Auto-generated constructor stub
	}

	public CurrencyExchangeRateDTO(Long id_currency,Long id_currency_local) {
		// TODO Auto-generated constructor stub
		this.id_currency = id_currency;
		this.id_currency_local = id_currency_local;
	}
	
	public CurrencyExchangeRateDTO(Long id) {
		super();
		this.setId(id);
	}
	
	public CurrencyExchangeRateDTO(Boolean active) {
		// TODO Auto-generated constructor stub
		this.active = active;
	}
	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public CurrencyExchangeRateDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	public Long getId_currency() {
		return id_currency;
	}

	public void setId_currency(Long id_currency) {
		this.id_currency = id_currency;
	}

	public Long getId_currency_local() {
		return id_currency_local;
	}

	public void setId_currency_local(Long id_currency_local) {
		this.id_currency_local = id_currency_local;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public CurrencyDTO getLocalCurrencyDTO() {
		return localCurrencyDTO;
	}

	public void setLocalCurrencyDTO(CurrencyDTO localCurrencyDTO) {
		this.localCurrencyDTO = localCurrencyDTO;
	}

	@Override
	public String toString() {
		return "CurrencyExchangeRateDTO [id_currency=" + id_currency
				+ ", id_currency_local=" + id_currency_local + ", amount="
				+ amount + ", registration_date=" + registration_date
				+ ", validity_end_date=" + validity_end_date + ", active="
				+ active + ", localCurrencyDTO=" + localCurrencyDTO
				+ ", getId()=" + getId() + ", getCreation_user()="
				+ getCreation_user() + ", getCreation_date()="
				+ getCreation_date() + ", getLast_modif_user()="
				+ getLast_modif_user() + ", getLast_modif_date()="
				+ getLast_modif_date() + ", hashCode()=" + hashCode()
				+ ", getClass()=" + getClass() + ", toString()="
				+ super.toString() + "]";
	}

}
