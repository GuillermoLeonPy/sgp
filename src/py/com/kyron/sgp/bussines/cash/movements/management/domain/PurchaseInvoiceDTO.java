package py.com.kyron.sgp.bussines.cash.movements.management.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.domain.GenericDTO;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;

public class PurchaseInvoiceDTO extends GenericDTO {

	private Long id_person;
	private Long id_currency;
	private String payment_condition;
	private String status;
	private Date registration_date;
	//@NotNull
	private Date emission_date;
	private Date cancellation_date;
	private Date annulment_date;
	private String annulment_reason_description;
	//@NotEmpty
	@Pattern(regexp = "^[0-9]+(-?[0-9]+)+$")
	@Size(min=15, max=15)
	private String identifier_number;
	//@NotNull
	@Range(min=10000000L,max=99999999L)
	private Long stamping_number;
	//@NotNull
	private Date stamping_number_start_validity_date;
	private Date stamping_number_end_validity_date;
	private String branch_office_address;
	private String branch_office_telephone_nbr;
	private BigDecimal total_amount;
	private BigDecimal value_added_amount;
	private BigDecimal total_tax_amount;
	private BigDecimal exempt_amount;
	private BigDecimal value_added_tax_10_amount;
	private BigDecimal value_added_tax_5_amount;
	private BigDecimal tax_10_amount;
	private BigDecimal tax_5_amount;
	@Size(min=5, max=500)
	private String bussines_name;
	@Size(min=5, max=50)
	private String bussines_ci_ruc;	
	//@NotNull
	@Range(min=2L,max=5L)
	private Long credit_purchase_fee_quantity;
	//@NotNull
	@Range(min=15L,max=30L)
	private Long credit_purchase_fee_periodicity_days_quantity;
	private Date credit_purchase_first_payment_fee_date;
	
	private CurrencyDTO currencyDTO;
	private PersonDTO personDTO;
	private List<PurchaseInvoiceItemDTO> listPurchaseInvoiceItemDTO;
	private List<PurchaseInvoicePaymentDTO> listPurchaseInvoicePaymentDTO;
	
	private Date emissionDateBeginFilter;
	private Date emissionDateEndFilter;
	private Boolean queryForExpenditureTaxReport;
	
	public PurchaseInvoiceDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public PurchaseInvoiceDTO(Long id) {
		super();
		this.setId(id);
	}
	
	public PurchaseInvoiceDTO(Long id,Long id_person) {
		// TODO Auto-generated constructor stub
		super();
		this.setId(id);
		this.id_person = id_person;
	}

	public PurchaseInvoiceDTO(Date emissionDateBeginFilter, Date emissionDateEndFilter, Boolean queryForExpenditureTaxReport) {
		// TODO Auto-generated constructor stub
		this.emissionDateBeginFilter = emissionDateBeginFilter;
		this.emissionDateEndFilter = emissionDateEndFilter;
		this.queryForExpenditureTaxReport = queryForExpenditureTaxReport;
	}
	
	public PurchaseInvoiceDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
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
	 * @return the emission_date
	 */
	public Date getEmission_date() {
		return emission_date;
	}

	/**
	 * @param emission_date the emission_date to set
	 */
	public void setEmission_date(Date emission_date) {
		this.emission_date = emission_date;
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
	 * @return the annulment_date
	 */
	public Date getAnnulment_date() {
		return annulment_date;
	}

	/**
	 * @param annulment_date the annulment_date to set
	 */
	public void setAnnulment_date(Date annulment_date) {
		this.annulment_date = annulment_date;
	}

	/**
	 * @return the annulment_reason_description
	 */
	public String getAnnulment_reason_description() {
		return annulment_reason_description;
	}

	/**
	 * @param annulment_reason_description the annulment_reason_description to set
	 */
	public void setAnnulment_reason_description(String annulment_reason_description) {
		this.annulment_reason_description = annulment_reason_description;
	}

	/**
	 * @return the identifier_number
	 */
	public String getIdentifier_number() {
		return identifier_number;
	}

	/**
	 * @param identifier_number the identifier_number to set
	 */
	public void setIdentifier_number(String identifier_number) {
		this.identifier_number = identifier_number;
	}

	/**
	 * @return the stamping_number
	 */
	public Long getStamping_number() {
		return stamping_number;
	}

	/**
	 * @param stamping_number the stamping_number to set
	 */
	public void setStamping_number(Long stamping_number) {
		this.stamping_number = stamping_number;
	}

	/**
	 * @return the stamping_number_start_validity_date
	 */
	public Date getStamping_number_start_validity_date() {
		return stamping_number_start_validity_date;
	}

	/**
	 * @param stamping_number_start_validity_date the stamping_number_start_validity_date to set
	 */
	public void setStamping_number_start_validity_date(
			Date stamping_number_start_validity_date) {
		this.stamping_number_start_validity_date = stamping_number_start_validity_date;
	}

	/**
	 * @return the stamping_number_end_validity_date
	 */
	public Date getStamping_number_end_validity_date() {
		return stamping_number_end_validity_date;
	}

	/**
	 * @param stamping_number_end_validity_date the stamping_number_end_validity_date to set
	 */
	public void setStamping_number_end_validity_date(
			Date stamping_number_end_validity_date) {
		this.stamping_number_end_validity_date = stamping_number_end_validity_date;
	}

	/**
	 * @return the branch_office_address
	 */
	public String getBranch_office_address() {
		return branch_office_address;
	}

	/**
	 * @param branch_office_address the branch_office_address to set
	 */
	public void setBranch_office_address(String branch_office_address) {
		this.branch_office_address = branch_office_address;
	}

	/**
	 * @return the branch_office_telephone_nbr
	 */
	public String getBranch_office_telephone_nbr() {
		return branch_office_telephone_nbr;
	}

	/**
	 * @param branch_office_telephone_nbr the branch_office_telephone_nbr to set
	 */
	public void setBranch_office_telephone_nbr(String branch_office_telephone_nbr) {
		this.branch_office_telephone_nbr = branch_office_telephone_nbr;
	}

	/**
	 * @return the total_amount
	 */
	public BigDecimal getTotal_amount() {
		return total_amount;
	}

	/**
	 * @param total_amount the total_amount to set
	 */
	public void setTotal_amount(BigDecimal total_amount) {
		this.total_amount = total_amount;
	}

	/**
	 * @return the value_added_amount
	 */
	public BigDecimal getValue_added_amount() {
		return value_added_amount;
	}

	/**
	 * @param value_added_amount the value_added_amount to set
	 */
	public void setValue_added_amount(BigDecimal value_added_amount) {
		this.value_added_amount = value_added_amount;
	}

	/**
	 * @return the total_tax_amount
	 */
	public BigDecimal getTotal_tax_amount() {
		return total_tax_amount;
	}

	/**
	 * @param total_tax_amount the total_tax_amount to set
	 */
	public void setTotal_tax_amount(BigDecimal total_tax_amount) {
		this.total_tax_amount = total_tax_amount;
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

	/**
	 * @return the tax_10_amount
	 */
	public BigDecimal getTax_10_amount() {
		return tax_10_amount;
	}

	/**
	 * @param tax_10_amount the tax_10_amount to set
	 */
	public void setTax_10_amount(BigDecimal tax_10_amount) {
		this.tax_10_amount = tax_10_amount;
	}

	/**
	 * @return the tax_5_amount
	 */
	public BigDecimal getTax_5_amount() {
		return tax_5_amount;
	}

	/**
	 * @param tax_5_amount the tax_5_amount to set
	 */
	public void setTax_5_amount(BigDecimal tax_5_amount) {
		this.tax_5_amount = tax_5_amount;
	}

	/**
	 * @return the bussines_name
	 */
	public String getBussines_name() {
		return bussines_name;
	}

	/**
	 * @param bussines_name the bussines_name to set
	 */
	public void setBussines_name(String bussines_name) {
		this.bussines_name = bussines_name;
	}

	/**
	 * @return the bussines_ci_ruc
	 */
	public String getBussines_ci_ruc() {
		return bussines_ci_ruc;
	}

	/**
	 * @param bussines_ci_ruc the bussines_ci_ruc to set
	 */
	public void setBussines_ci_ruc(String bussines_ci_ruc) {
		this.bussines_ci_ruc = bussines_ci_ruc;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PurchaseInvoiceDTO [id_person=" + id_person + ", id_currency="
				+ id_currency + ", payment_condition=" + payment_condition
				+ ", status=" + status + ", registration_date="
				+ registration_date + ", emission_date=" + emission_date
				+ ", cancellation_date=" + cancellation_date
				+ ", annulment_date=" + annulment_date
				+ ", annulment_reason_description="
				+ annulment_reason_description + ", identifier_number="
				+ identifier_number + ", stamping_number=" + stamping_number
				+ ", stamping_number_start_validity_date="
				+ stamping_number_start_validity_date
				+ ", stamping_number_end_validity_date="
				+ stamping_number_end_validity_date
				+ ", branch_office_address=" + branch_office_address
				+ ", branch_office_telephone_nbr="
				+ branch_office_telephone_nbr + ", total_amount="
				+ total_amount + ", value_added_amount=" + value_added_amount
				+ ", total_tax_amount=" + total_tax_amount + ", exempt_amount="
				+ exempt_amount + ", value_added_tax_10_amount="
				+ value_added_tax_10_amount + ", value_added_tax_5_amount="
				+ value_added_tax_5_amount + ", tax_10_amount=" + tax_10_amount
				+ ", tax_5_amount=" + tax_5_amount + ", bussines_name="
				+ bussines_name + ", bussines_ci_ruc=" + bussines_ci_ruc
				+ ", credit_purchase_fee_quantity="
				+ credit_purchase_fee_quantity
				+ ", credit_purchase_fee_periodicity_days_quantity="
				+ credit_purchase_fee_periodicity_days_quantity
				+ ", credit_purchase_first_payment_fee_date="
				+ credit_purchase_first_payment_fee_date + ", currencyDTO="
				+ currencyDTO + ", personDTO=" + personDTO
				+ ", listPurchaseInvoiceItemDTO=" + listPurchaseInvoiceItemDTO
				+ ", getId()=" + getId() + ", getCreation_user()="
				+ getCreation_user() + ", getCreation_date()="
				+ getCreation_date() + ", getLast_modif_user()="
				+ getLast_modif_user() + ", getLast_modif_date()="
				+ getLast_modif_date() + ", getClass()=" + getClass()
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
		PurchaseInvoiceDTO other = (PurchaseInvoiceDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @return the listPurchaseInvoiceItemDTO
	 */
	public List<PurchaseInvoiceItemDTO> getListPurchaseInvoiceItemDTO() {
		return listPurchaseInvoiceItemDTO;
	}

	/**
	 * @param listPurchaseInvoiceItemDTO the listPurchaseInvoiceItemDTO to set
	 */
	public void setListPurchaseInvoiceItemDTO(
			List<PurchaseInvoiceItemDTO> listPurchaseInvoiceItemDTO) {
		this.listPurchaseInvoiceItemDTO = listPurchaseInvoiceItemDTO;
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
	 * @return the credit_purchase_fee_quantity
	 */
	public Long getCredit_purchase_fee_quantity() {
		return credit_purchase_fee_quantity;
	}

	/**
	 * @param credit_purchase_fee_quantity the credit_purchase_fee_quantity to set
	 */
	public void setCredit_purchase_fee_quantity(Long credit_purchase_fee_quantity) {
		this.credit_purchase_fee_quantity = credit_purchase_fee_quantity;
	}

	/**
	 * @return the credit_purchase_fee_periodicity_days_quantity
	 */
	public Long getCredit_purchase_fee_periodicity_days_quantity() {
		return credit_purchase_fee_periodicity_days_quantity;
	}

	/**
	 * @param credit_purchase_fee_periodicity_days_quantity the credit_purchase_fee_periodicity_days_quantity to set
	 */
	public void setCredit_purchase_fee_periodicity_days_quantity(
			Long credit_purchase_fee_periodicity_days_quantity) {
		this.credit_purchase_fee_periodicity_days_quantity = credit_purchase_fee_periodicity_days_quantity;
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
	 * @return the credit_purchase_first_payment_fee_date
	 */
	public Date getCredit_purchase_first_payment_fee_date() {
		return credit_purchase_first_payment_fee_date;
	}

	/**
	 * @param credit_purchase_first_payment_fee_date the credit_purchase_first_payment_fee_date to set
	 */
	public void setCredit_purchase_first_payment_fee_date(
			Date credit_purchase_first_payment_fee_date) {
		this.credit_purchase_first_payment_fee_date = credit_purchase_first_payment_fee_date;
	}

	/**
	 * @return the listPurchaseInvoicePaymentDTO
	 */
	public List<PurchaseInvoicePaymentDTO> getListPurchaseInvoicePaymentDTO() {
		return listPurchaseInvoicePaymentDTO;
	}

	/**
	 * @param listPurchaseInvoicePaymentDTO the listPurchaseInvoicePaymentDTO to set
	 */
	public void setListPurchaseInvoicePaymentDTO(
			List<PurchaseInvoicePaymentDTO> listPurchaseInvoicePaymentDTO) {
		this.listPurchaseInvoicePaymentDTO = listPurchaseInvoicePaymentDTO;
	}

	/**
	 * @return the emissionDateBeginFilter
	 */
	public Date getEmissionDateBeginFilter() {
		return emissionDateBeginFilter;
	}

	/**
	 * @param emissionDateBeginFilter the emissionDateBeginFilter to set
	 */
	public void setEmissionDateBeginFilter(Date emissionDateBeginFilter) {
		this.emissionDateBeginFilter = emissionDateBeginFilter;
	}

	/**
	 * @return the emissionDateEndFilter
	 */
	public Date getEmissionDateEndFilter() {
		return emissionDateEndFilter;
	}

	/**
	 * @param emissionDateEndFilter the emissionDateEndFilter to set
	 */
	public void setEmissionDateEndFilter(Date emissionDateEndFilter) {
		this.emissionDateEndFilter = emissionDateEndFilter;
	}

	/**
	 * @return the queryForExpenditureTaxReport
	 */
	public Boolean getQueryForExpenditureTaxReport() {
		return queryForExpenditureTaxReport;
	}

	/**
	 * @param queryForExpenditureTaxReport the queryForExpenditureTaxReport to set
	 */
	public void setQueryForExpenditureTaxReport(Boolean queryForExpenditureTaxReport) {
		this.queryForExpenditureTaxReport = queryForExpenditureTaxReport;
	}
}
