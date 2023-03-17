package py.com.kyron.sgp.bussines.cash.movements.management.domain;

import java.math.BigDecimal;
import java.util.Date;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.domain.GenericDTO;

public class IncomeExpeditureReportDTO extends GenericDTO {

	private String operational_concept;
	private String operational_concept_message;
	private BigDecimal total_amount;
	private BigDecimal value_added_tax_10_amount;
	private BigDecimal value_added_tax_5_amount;
	private BigDecimal value_added_amount;
	private BigDecimal exempt_amount;
	private BigDecimal tax_10_amount;
	private BigDecimal tax_5_amount;
	private BigDecimal total_tax_amount;
	private Date beginDate;
	private Date endDate;
	private Long id_currency;
	private CurrencyDTO currencyDTO;
	
	private String total_amount_formatted;
	private String value_added_tax_10_amount_formatted;
	private String value_added_tax_5_amount_formatted;
	private String value_added_amount_formatted;
	private String exempt_amount_formatted;
	private String tax_10_amount_formatted;
	private String tax_5_amount_formatted;
	private String total_tax_amount_formatted;
	
	public IncomeExpeditureReportDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public IncomeExpeditureReportDTO(Date beginDate, Date endDate, Long id_currency,CurrencyDTO currencyDTO) {
		// TODO Auto-generated constructor stub
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.id_currency = id_currency;
		this.currencyDTO = currencyDTO;
	}

	public IncomeExpeditureReportDTO(Long id, String creation_user,
			Date creation_date, String last_modif_user, Date last_modif_date) {
		super(id, creation_user, creation_date, last_modif_user,
				last_modif_date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the operational_concept
	 */
	public String getOperational_concept() {
		return operational_concept;
	}

	/**
	 * @param operational_concept the operational_concept to set
	 */
	public void setOperational_concept(String operational_concept) {
		this.operational_concept = operational_concept;
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
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
		IncomeExpeditureReportDTO other = (IncomeExpeditureReportDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @return the total_amount_formatted
	 */
	public String getTotal_amount_formatted() {
		return total_amount_formatted;
	}

	/**
	 * @param total_amount_formatted the total_amount_formatted to set
	 */
	public void setTotal_amount_formatted(String total_amount_formatted) {
		this.total_amount_formatted = total_amount_formatted;
	}

	/**
	 * @return the value_added_tax_10_amount_formatted
	 */
	public String getValue_added_tax_10_amount_formatted() {
		return value_added_tax_10_amount_formatted;
	}

	/**
	 * @param value_added_tax_10_amount_formatted the value_added_tax_10_amount_formatted to set
	 */
	public void setValue_added_tax_10_amount_formatted(
			String value_added_tax_10_amount_formatted) {
		this.value_added_tax_10_amount_formatted = value_added_tax_10_amount_formatted;
	}


	/**
	 * @return the value_added_amount_formatted
	 */
	public String getValue_added_amount_formatted() {
		return value_added_amount_formatted;
	}

	/**
	 * @param value_added_amount_formatted the value_added_amount_formatted to set
	 */
	public void setValue_added_amount_formatted(String value_added_amount_formatted) {
		this.value_added_amount_formatted = value_added_amount_formatted;
	}

	/**
	 * @return the exempt_amount_formatted
	 */
	public String getExempt_amount_formatted() {
		return exempt_amount_formatted;
	}

	/**
	 * @param exempt_amount_formatted the exempt_amount_formatted to set
	 */
	public void setExempt_amount_formatted(String exempt_amount_formatted) {
		this.exempt_amount_formatted = exempt_amount_formatted;
	}

	/**
	 * @return the tax_10_amount_formatted
	 */
	public String getTax_10_amount_formatted() {
		return tax_10_amount_formatted;
	}

	/**
	 * @param tax_10_amount_formatted the tax_10_amount_formatted to set
	 */
	public void setTax_10_amount_formatted(String tax_10_amount_formatted) {
		this.tax_10_amount_formatted = tax_10_amount_formatted;
	}

	/**
	 * @return the tax_5_amount_formatted
	 */
	public String getTax_5_amount_formatted() {
		return tax_5_amount_formatted;
	}

	/**
	 * @param tax_5_amount_formatted the tax_5_amount_formatted to set
	 */
	public void setTax_5_amount_formatted(String tax_5_amount_formatted) {
		this.tax_5_amount_formatted = tax_5_amount_formatted;
	}

	/**
	 * @return the total_tax_amount_formatted
	 */
	public String getTotal_tax_amount_formatted() {
		return total_tax_amount_formatted;
	}

	/**
	 * @param total_tax_amount_formatted the total_tax_amount_formatted to set
	 */
	public void setTotal_tax_amount_formatted(String total_tax_amount_formatted) {
		this.total_tax_amount_formatted = total_tax_amount_formatted;
	}

	/**
	 * @return the value_added_tax_5_amount_formatted
	 */
	public String getValue_added_tax_5_amount_formatted() {
		return value_added_tax_5_amount_formatted;
	}

	/**
	 * @param value_added_tax_5_amount_formatted the value_added_tax_5_amount_formatted to set
	 */
	public void setValue_added_tax_5_amount_formatted(
			String value_added_tax_5_amount_formatted) {
		this.value_added_tax_5_amount_formatted = value_added_tax_5_amount_formatted;
	}

	/**
	 * @return the operational_concept_message
	 */
	public String getOperational_concept_message() {
		return operational_concept_message;
	}

	/**
	 * @param operational_concept_message the operational_concept_message to set
	 */
	public void setOperational_concept_message(String operational_concept_message) {
		this.operational_concept_message = operational_concept_message;
	}
}
