package py.com.kyron.sgp.bussines.cash.movements.management.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class CreditNoteDTO extends GenericDTO {

	private Long id_person;
	private Long id_currency;
	private String modified_documens_identifier_numbers;
	private Long id_branch_office_sale_station;
	private String status;
	private Date emission_date;
	private String emission_reason_description;
	private Date validity_end_date;
	private Date cancellation_date;
	private Date annulment_date;
	private String annulment_reason_description;
	private String identifier_number;
	private BigDecimal credit_note_stamp_number;
	private Long id_credit_note_stamping_numeration;
	private BigDecimal total_amount;
	private BigDecimal value_added_amount;
	private BigDecimal total_tax_amount;
	private BigDecimal exempt_amount;
	private BigDecimal value_added_tax_10_amount;
	private BigDecimal value_added_tax_5_amount;
	private BigDecimal tax_10_amount;
	private BigDecimal tax_5_amount;
	
	private String bussines_name;
	private String bussines_ci_ruc;
	private BigDecimal credit_note_balance;	
	
	/* comodin property */
	private Long id_sale_invoice;
	
	private List<CreditNoteItemDTO> listCreditNoteItemDTO;
	private CurrencyDTO currencyDTO;
	
	private Date emissionDateBeginFilter;
	private Date emissionDateEndFilter;	
	private Boolean hasUsableCreditNoteBalance;
	
	public CreditNoteDTO() {
		// TODO Auto-generated constructor stub
	}

	public CreditNoteDTO(Long id) {
		super();
		this.setId(id);
	}
	
	public CreditNoteDTO(Long id,Long id_sale_invoice) {
		this.id_sale_invoice = id_sale_invoice;
	}
	
	public CreditNoteDTO(Long id_person,Boolean hasUsableCreditNoteBalance) {
		this.id_person = id_person;
		this.hasUsableCreditNoteBalance = hasUsableCreditNoteBalance;
	}
	
	public CreditNoteDTO(Date emissionDateBeginFilter, Date emissionDateEndFilter) {
		// TODO Auto-generated constructor stub
		this.emissionDateBeginFilter = emissionDateBeginFilter;
		this.emissionDateEndFilter = emissionDateEndFilter;
	}
	
	public CreditNoteDTO(Long id, String creation_user, Date creation_date,
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
	 * @return the modified_documens_identifier_numbers
	 */
	public String getModified_documens_identifier_numbers() {
		return modified_documens_identifier_numbers;
	}

	/**
	 * @param modified_documens_identifier_numbers the modified_documens_identifier_numbers to set
	 */
	public void setModified_documens_identifier_numbers(
			String modified_documens_identifier_numbers) {
		this.modified_documens_identifier_numbers = modified_documens_identifier_numbers;
	}

	/**
	 * @return the id_branch_office_sale_station
	 */
	public Long getId_branch_office_sale_station() {
		return id_branch_office_sale_station;
	}

	/**
	 * @param id_branch_office_sale_station the id_branch_office_sale_station to set
	 */
	public void setId_branch_office_sale_station(Long id_branch_office_sale_station) {
		this.id_branch_office_sale_station = id_branch_office_sale_station;
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
	 * @return the emission_reason_description
	 */
	public String getEmission_reason_description() {
		return emission_reason_description;
	}

	/**
	 * @param emission_reason_description the emission_reason_description to set
	 */
	public void setEmission_reason_description(String emission_reason_description) {
		this.emission_reason_description = emission_reason_description;
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
	 * @return the credit_note_stamp_number
	 */
	public BigDecimal getCredit_note_stamp_number() {
		return credit_note_stamp_number;
	}

	/**
	 * @param credit_note_stamp_number the credit_note_stamp_number to set
	 */
	public void setCredit_note_stamp_number(BigDecimal credit_note_stamp_number) {
		this.credit_note_stamp_number = credit_note_stamp_number;
	}

	/**
	 * @return the id_credit_note_stamping_numeration
	 */
	public Long getId_credit_note_stamping_numeration() {
		return id_credit_note_stamping_numeration;
	}

	/**
	 * @param id_credit_note_stamping_numeration the id_credit_note_stamping_numeration to set
	 */
	public void setId_credit_note_stamping_numeration(
			Long id_credit_note_stamping_numeration) {
		this.id_credit_note_stamping_numeration = id_credit_note_stamping_numeration;
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

	/**
	 * @return the credit_note_balance
	 */
	public BigDecimal getCredit_note_balance() {
		return credit_note_balance;
	}

	/**
	 * @param credit_note_balance the credit_note_balance to set
	 */
	public void setCredit_note_balance(BigDecimal credit_note_balance) {
		this.credit_note_balance = credit_note_balance;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CreditNoteDTO [id_person=" + id_person + ", id_currency="
				+ id_currency + ", modified_documens_identifier_numbers="
				+ modified_documens_identifier_numbers
				+ ", id_branch_office_sale_station="
				+ id_branch_office_sale_station + ", status=" + status
				+ ", emission_date=" + emission_date
				+ ", emission_reason_description="
				+ emission_reason_description + ", validity_end_date="
				+ validity_end_date + ", cancellation_date="
				+ cancellation_date + ", annulment_date=" + annulment_date
				+ ", annulment_reason_description="
				+ annulment_reason_description + ", identifier_number="
				+ identifier_number + ", credit_note_stamp_number="
				+ credit_note_stamp_number
				+ ", id_credit_note_stamping_numeration="
				+ id_credit_note_stamping_numeration + ", total_amount="
				+ total_amount + ", value_added_amount=" + value_added_amount
				+ ", total_tax_amount=" + total_tax_amount + ", exempt_amount="
				+ exempt_amount + ", value_added_tax_10_amount="
				+ value_added_tax_10_amount + ", value_added_tax_5_amount="
				+ value_added_tax_5_amount + ", tax_10_amount=" + tax_10_amount
				+ ", tax_5_amount=" + tax_5_amount + ", bussines_name="
				+ bussines_name + ", bussines_ci_ruc=" + bussines_ci_ruc
				+ ", credit_note_balance=" + credit_note_balance
				+ ", id_sale_invoice=" + id_sale_invoice + ", getId()="
				+ getId() + ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
	}

	/**
	 * @return the id_sale_invoice
	 */
	public Long getId_sale_invoice() {
		return id_sale_invoice;
	}

	/**
	 * @param id_sale_invoice the id_sale_invoice to set
	 */
	public void setId_sale_invoice(Long id_sale_invoice) {
		this.id_sale_invoice = id_sale_invoice;
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
		CreditNoteDTO other = (CreditNoteDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @return the listCreditNoteItemDTO
	 */
	public List<CreditNoteItemDTO> getListCreditNoteItemDTO() {
		return listCreditNoteItemDTO;
	}

	/**
	 * @param listCreditNoteItemDTO the listCreditNoteItemDTO to set
	 */
	public void setListCreditNoteItemDTO(
			List<CreditNoteItemDTO> listCreditNoteItemDTO) {
		this.listCreditNoteItemDTO = listCreditNoteItemDTO;
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
	 * @return the hasUsableCreditNoteBalance
	 */
	public Boolean getHasUsableCreditNoteBalance() {
		return hasUsableCreditNoteBalance;
	}

	/**
	 * @param hasUsableCreditNoteBalance the hasUsableCreditNoteBalance to set
	 */
	public void setHasUsableCreditNoteBalance(Boolean hasUsableCreditNoteBalance) {
		this.hasUsableCreditNoteBalance = hasUsableCreditNoteBalance;
	}
}
