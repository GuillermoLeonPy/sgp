/**
 * 
 */
package py.com.kyron.sgp.bussines.comercialmanagement.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import py.com.kyron.sgp.bussines.domain.GenericDTO;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;

/**
 * @author testuser
 *
 */
public class OrderDTO extends GenericDTO {

	private Long id_person;
	private Long id_currency;
	private String payment_condition;
	private String status;
	private Date registration_date;
	private Date estimated_completion_date;
	private Date completion_date;
	private Date cancellation_date;
	private String cancellation_reason_description;
	private Long identifier_number;
	private BigDecimal amount;
	private BigDecimal exempt_amount;
	private BigDecimal value_added_tax_10_amount;
	private BigDecimal value_added_tax_5_amount;
	private List<OrderItemDTO> listOrderItemDTO;
	private PersonDTO personDTO;
	private CurrencyDTO currencyDTO;
	private Long credit_order_fee_quantity;
	private Long credit_order_fee_periodicity_days_quantity;
	private BigDecimal credit_order_payment_condition_surcharge_percentage;
	private Long id_credit_order_charge_condition;
	private Date production_activities_instantiation_date;
	
	private String previous_status;
	private Boolean production_activities_instantiated_today;
	private Boolean havingAnyOrderItemWithPendingToInstanciateQuantity;
	/**
	 * 
	 */
	public OrderDTO() {
		// TODO Auto-generated constructor stub
	}

	public OrderDTO(Long id) {
		super();
		this.setId(id);
	}
	
	public OrderDTO(Long id,Long id_person) {
		super();
		this.setId(id);
		this.id_person = id_person;
	}
	
	public OrderDTO(String status,Boolean production_activities_instantiated_today) {
		// TODO Auto-generated constructor stub
		this.status = status;
		this.production_activities_instantiated_today = production_activities_instantiated_today;
	}

	public OrderDTO(
			String status,
			Boolean havingAnyOrderItemWithPendingToInstanciateQuantity,Boolean production_activities_instantiated_today) {
		// TODO Auto-generated constructor stub
		this.status = status;
		this.havingAnyOrderItemWithPendingToInstanciateQuantity = havingAnyOrderItemWithPendingToInstanciateQuantity;
		this.production_activities_instantiated_today = production_activities_instantiated_today;
	}
	
	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public OrderDTO(Long id, String creation_user, Date creation_date,
			String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id_person
	 */
	public Long getId_person() {
		return id_person;
	}

	/**
	 * @param id_person the id_person to set
	 */
	public void setId_person(Long id_person) {
		this.id_person = id_person;
	}

	/**
	 * @return the id_currency
	 */
	public Long getId_currency() {
		return id_currency;
	}

	/**
	 * @param id_currency the id_currency to set
	 */
	public void setId_currency(Long id_currency) {
		this.id_currency = id_currency;
	}

	/**
	 * @return the payment_condition
	 */
	public String getPayment_condition() {
		return payment_condition;
	}

	/**
	 * @param payment_condition the payment_condition to set
	 */
	public void setPayment_condition(String payment_condition) {
		this.payment_condition = payment_condition;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the estimated_completion_date
	 */
	public Date getEstimated_completion_date() {
		return estimated_completion_date;
	}

	/**
	 * @param estimated_completion_date the estimated_completion_date to set
	 */
	public void setEstimated_completion_date(Date estimated_completion_date) {
		this.estimated_completion_date = estimated_completion_date;
	}

	/**
	 * @return the completion_date
	 */
	public Date getCompletion_date() {
		return completion_date;
	}

	/**
	 * @param completion_date the completion_date to set
	 */
	public void setCompletion_date(Date completion_date) {
		this.completion_date = completion_date;
	}

	/**
	 * @return the cancellation_date
	 */
	public Date getCancellation_date() {
		return cancellation_date;
	}

	/**
	 * @param cancellation_date the cancellation_date to set
	 */
	public void setCancellation_date(Date cancellation_date) {
		this.cancellation_date = cancellation_date;
	}

	/**
	 * @return the cancellation_reason_description
	 */
	public String getCancellation_reason_description() {
		return cancellation_reason_description;
	}

	/**
	 * @param cancellation_reason_description the cancellation_reason_description to set
	 */
	public void setCancellation_reason_description(
			String cancellation_reason_description) {
		this.cancellation_reason_description = cancellation_reason_description;
	}

	/**
	 * @return the identifier_number
	 */
	public Long getIdentifier_number() {
		return identifier_number;
	}

	/**
	 * @param identifier_number the identifier_number to set
	 */
	public void setIdentifier_number(Long identifier_number) {
		this.identifier_number = identifier_number;
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
	 * @return the exempt_amount
	 */
	public BigDecimal getExempt_amount() {
		return exempt_amount;
	}

	/**
	 * @param exempt_amount the exempt_amount to set
	 */
	public void setExempt_amount(BigDecimal exempt_amount) {
		this.exempt_amount = exempt_amount;
	}

	/**
	 * @return the value_added_tax_10_amount
	 */
	public BigDecimal getValue_added_tax_10_amount() {
		return value_added_tax_10_amount;
	}

	/**
	 * @param value_added_tax_10_amount the value_added_tax_10_amount to set
	 */
	public void setValue_added_tax_10_amount(BigDecimal value_added_tax_10_amount) {
		this.value_added_tax_10_amount = value_added_tax_10_amount;
	}

	/**
	 * @return the value_added_tax_5_amount
	 */
	public BigDecimal getValue_added_tax_5_amount() {
		return value_added_tax_5_amount;
	}

	/**
	 * @param value_added_tax_5_amount the value_added_tax_5_amount to set
	 */
	public void setValue_added_tax_5_amount(BigDecimal value_added_tax_5_amount) {
		this.value_added_tax_5_amount = value_added_tax_5_amount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrderDTO [id_person=" + id_person + ", id_currency="
				+ id_currency + ", payment_condition=" + payment_condition
				+ ", status=" + status + ", registration_date="
				+ registration_date + ", estimated_completion_date="
				+ estimated_completion_date + ", completion_date="
				+ completion_date + ", cancellation_date=" + cancellation_date
				+ ", cancellation_reason_description="
				+ cancellation_reason_description + ", identifier_number="
				+ identifier_number + ", amount=" + amount + ", exempt_amount="
				+ exempt_amount + ", value_added_tax_10_amount="
				+ value_added_tax_10_amount + ", value_added_tax_5_amount="
				+ value_added_tax_5_amount + ", personDTO=" + personDTO
				+ ", currencyDTO=" + currencyDTO
				+ ", credit_order_fee_quantity=" + credit_order_fee_quantity
				+ ", credit_order_fee_periodicity_days_quantity="
				+ credit_order_fee_periodicity_days_quantity
				+ ", credit_order_payment_condition_surcharge_percentage="
				+ credit_order_payment_condition_surcharge_percentage
				+ ", id_credit_order_charge_condition="
				+ id_credit_order_charge_condition
				+ ", production_activities_instantiation_date="
				+ production_activities_instantiation_date
				+ ", previous_status=" + previous_status
				+ ", production_activities_instantiated_today="
				+ production_activities_instantiated_today
				+ ", havingAnyOrderItemWithPendingToInstanciateQuantity="
				+ havingAnyOrderItemWithPendingToInstanciateQuantity
				+ ", getId()=" + getId() + ", getCreation_user()="
				+ getCreation_user() + ", getCreation_date()="
				+ getCreation_date() + ", getLast_modif_user()="
				+ getLast_modif_user() + ", getLast_modif_date()="
				+ getLast_modif_date() + ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
	}

	/**
	 * @return the listOrderItemDTO
	 */
	public List<OrderItemDTO> getListOrderItemDTO() {
		return listOrderItemDTO;
	}

	/**
	 * @param listOrderItemDTO the listOrderItemDTO to set
	 */
	public void setListOrderItemDTO(List<OrderItemDTO> listOrderItemDTO) {
		this.listOrderItemDTO = listOrderItemDTO;
	}

	/**
	 * @return the personDTO
	 */
	public PersonDTO getPersonDTO() {
		return personDTO;
	}

	/**
	 * @param personDTO the personDTO to set
	 */
	public void setPersonDTO(PersonDTO personDTO) {
		this.personDTO = personDTO;
	}

	/**
	 * @return the currencyDTO
	 */
	public CurrencyDTO getCurrencyDTO() {
		return currencyDTO;
	}

	/**
	 * @param currencyDTO the currencyDTO to set
	 */
	public void setCurrencyDTO(CurrencyDTO currencyDTO) {
		this.currencyDTO = currencyDTO;
	}

	/**
	 * @return the credit_order_fee_quantity
	 */
	public Long getCredit_order_fee_quantity() {
		return credit_order_fee_quantity;
	}

	/**
	 * @param credit_order_fee_quantity the credit_order_fee_quantity to set
	 */
	public void setCredit_order_fee_quantity(Long credit_order_fee_quantity) {
		this.credit_order_fee_quantity = credit_order_fee_quantity;
	}

	/**
	 * @return the credit_order_fee_periodicity_days_quantity
	 */
	public Long getCredit_order_fee_periodicity_days_quantity() {
		return credit_order_fee_periodicity_days_quantity;
	}

	/**
	 * @param credit_order_fee_periodicity_days_quantity the credit_order_fee_periodicity_days_quantity to set
	 */
	public void setCredit_order_fee_periodicity_days_quantity(
			Long credit_order_fee_periodicity_days_quantity) {
		this.credit_order_fee_periodicity_days_quantity = credit_order_fee_periodicity_days_quantity;
	}

	/**
	 * @return the credit_order_payment_condition_surcharge_percentage
	 */
	public BigDecimal getCredit_order_payment_condition_surcharge_percentage() {
		return credit_order_payment_condition_surcharge_percentage;
	}

	/**
	 * @param credit_order_payment_condition_surcharge_percentage the credit_order_payment_condition_surcharge_percentage to set
	 */
	public void setCredit_order_payment_condition_surcharge_percentage(
			BigDecimal credit_order_payment_condition_surcharge_percentage) {
		this.credit_order_payment_condition_surcharge_percentage = credit_order_payment_condition_surcharge_percentage;
	}

	/**
	 * @return the id_credit_order_charge_condition
	 */
	public Long getId_credit_order_charge_condition() {
		return id_credit_order_charge_condition;
	}

	/**
	 * @param id_credit_order_charge_condition the id_credit_order_charge_condition to set
	 */
	public void setId_credit_order_charge_condition(
			Long id_credit_order_charge_condition) {
		this.id_credit_order_charge_condition = id_credit_order_charge_condition;
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
		OrderDTO other = (OrderDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @return the previous_status
	 */
	public String getPrevious_status() {
		return previous_status;
	}

	/**
	 * @param previous_status the previous_status to set
	 */
	public void setPrevious_status(String previous_status) {
		this.previous_status = previous_status;
	}

	/**
	 * @return the production_activities_instantiation_date
	 */
	public Date getProduction_activities_instantiation_date() {
		return production_activities_instantiation_date;
	}

	/**
	 * @param production_activities_instantiation_date the production_activities_instantiation_date to set
	 */
	public void setProduction_activities_instantiation_date(
			Date production_activities_instantiation_date) {
		this.production_activities_instantiation_date = production_activities_instantiation_date;
	}

	/**
	 * @return the production_activities_instantiated_today
	 */
	public Boolean getProduction_activities_instantiated_today() {
		return production_activities_instantiated_today;
	}

	/**
	 * @param production_activities_instantiated_today the production_activities_instantiated_today to set
	 */
	public void setProduction_activities_instantiated_today(
			Boolean production_activities_instantiated_today) {
		this.production_activities_instantiated_today = production_activities_instantiated_today;
	}

	/**
	 * @return the havingAnyOrderItemWithPendingToInstanciateQuantity
	 */
	public Boolean getHavingAnyOrderItemWithPendingToInstanciateQuantity() {
		return havingAnyOrderItemWithPendingToInstanciateQuantity;
	}

	/**
	 * @param havingAnyOrderItemWithPendingToInstanciateQuantity the havingAnyOrderItemWithPendingToInstanciateQuantity to set
	 */
	public void setHavingAnyOrderItemWithPendingToInstanciateQuantity(
			Boolean havingAnyOrderItemWithPendingToInstanciateQuantity) {
		this.havingAnyOrderItemWithPendingToInstanciateQuantity = havingAnyOrderItemWithPendingToInstanciateQuantity;
	}

}
