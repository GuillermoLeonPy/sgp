package py.com.kyron.sgp.bussines.comercialmanagement.domain;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.*;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class CurrencyDTO extends GenericDTO{
	
	@NotEmpty//@NotNull//@NotBlank	
	@Size(min=3, max=5)//@Length(min=3,max=5)//
	private String id_code;
		
	//@Length(min=8,max=50)
	//@NotBlank//@NotNull
	//@Email
	@NotEmpty
	@Size(min=8, max=50)
	private String description;
	@NotNull
	private Boolean currency_local;
	private Boolean has_valid_exchange_rate;
	
	/* for currency amount convertion */
	private Long id_currency_amount;
	private BigDecimal amount;
	private Long id_currency_destination;
	
	public CurrencyDTO(Long id_currency_amount, Long id_currency_destination, BigDecimal amount) {
		super();
		// TODO Auto-generated constructor stub
		this.id_currency_amount = id_currency_amount;
		this.id_currency_destination = id_currency_destination;
		this.amount = amount;
	}

	public CurrencyDTO(Long id) {
		super();
		super.setId(id);
		// TODO Auto-generated constructor stub
	}

	public CurrencyDTO(Boolean currency_local) {
		super();
		// TODO Auto-generated constructor stub
		this.currency_local = currency_local;
	}
	
	public CurrencyDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public CurrencyDTO(Long id, String creation_user, Date creation_date,String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user, last_modif_date);
		// TODO Auto-generated constructor stub
	}


	/**
	 * @param id_code
	 * @param description
	 * @param currency_local
	 */
	public CurrencyDTO(String id_code, String description,Boolean currency_local) {
		super();
		this.id_code = id_code;
		this.description = description;
		this.currency_local = currency_local;
	}

	public CurrencyDTO(Long id, String creation_user, Date creation_date,String last_modif_user, Date last_modif_date,String id_code, String description,Boolean currency_local) {
		super(id, creation_user, creation_date, last_modif_user, last_modif_date);
		this.id_code = id_code;
		this.description = description;
		this.currency_local = currency_local;
	}

	public String getId_code() {
		return id_code;
	}

	public void setId_code(String id_code) {
		this.id_code = id_code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getCurrency_local() {
		return currency_local;
	}

	public void setCurrency_local(Boolean currency_local) {
		this.currency_local = currency_local;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CurrencyDTO [id_code=" + id_code + ", description="
				+ description + ", currency_local=" + currency_local
				+ ", has_valid_exchange_rate=" + has_valid_exchange_rate
				+ ", id_currency_amount=" + id_currency_amount + ", amount="
				+ amount + ", id_currency_destination="
				+ id_currency_destination + ", getId()=" + getId()
				+ ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", getClass()=" + getClass() + ", toString()="
				+ super.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
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
		CurrencyDTO other = (CurrencyDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @return the has_valid_exchange_rate
	 */
	public Boolean getHas_valid_exchange_rate() {
		return has_valid_exchange_rate;
	}

	/**
	 * @param has_valid_exchange_rate the has_valid_exchange_rate to set
	 */
	public void setHas_valid_exchange_rate(Boolean has_valid_exchange_rate) {
		this.has_valid_exchange_rate = has_valid_exchange_rate;
	}

	/**
	 * @return the id_currency_amount
	 */
	public Long getId_currency_amount() {
		return id_currency_amount;
	}

	/**
	 * @param id_currency_amount the id_currency_amount to set
	 */
	public void setId_currency_amount(Long id_currency_amount) {
		this.id_currency_amount = id_currency_amount;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the id_currency_destination
	 */
	public Long getId_currency_destination() {
		return id_currency_destination;
	}

	/**
	 * @param id_currency_destination the id_currency_destination to set
	 */
	public void setId_currency_destination(Long id_currency_destination) {
		this.id_currency_destination = id_currency_destination;
	}



	

}
