package py.com.kyron.sgp.bussines.cash.movements.management.domain;

import java.math.BigDecimal;
import java.util.Date;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;

public class ManPowerExpenditurePerFunctionaryDTO {
	
	private Long personal_civil_id_document;
	private String functionary_name;
	private Long activities_carried_out_count;
	private BigDecimal sum_activity_expected_minutes_quantity;
	private BigDecimal sum_activity_effective_minutes_quantity;
	private BigDecimal saved_minutes;
	private BigDecimal sum_man_power_requeriment_minutes_quantity;
	private BigDecimal sum_man_power_cost;
	
	private String personal_civil_id_document_formatted;
	private String activities_carried_out_count_formatted;
	private String sum_activity_expected_minutes_quantity_formatted;
	private String sum_activity_effective_minutes_quantity_formatted;
	private String saved_minutes_formatted;
	private String sum_man_power_cost_formatted;
	
	private Date beginDate;
	private Date endDate;
	private Long id_currency;
	private CurrencyDTO currencyDTO;
	
	public ManPowerExpenditurePerFunctionaryDTO() {
		// TODO Auto-generated constructor stub
	}

	public ManPowerExpenditurePerFunctionaryDTO(Date beginDate, Date endDate, Long id_currency, CurrencyDTO currencyDTO) {
		// TODO Auto-generated constructor stub
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.id_currency = id_currency;
		this.currencyDTO = currencyDTO;
	}
	
	/**
	 * @return the personal_civil_id_document
	 */
	public Long getPersonal_civil_id_document() {
		return personal_civil_id_document;
	}

	/**
	 * @param personal_civil_id_document the personal_civil_id_document to set
	 */
	public void setPersonal_civil_id_document(Long personal_civil_id_document) {
		this.personal_civil_id_document = personal_civil_id_document;
	}

	/**
	 * @return the functionary_name
	 */
	public String getFunctionary_name() {
		return functionary_name;
	}

	/**
	 * @param functionary_name the functionary_name to set
	 */
	public void setFunctionary_name(String functionary_name) {
		this.functionary_name = functionary_name;
	}

	/**
	 * @return the activities_carried_out_count
	 */
	public Long getActivities_carried_out_count() {
		return activities_carried_out_count;
	}

	/**
	 * @param activities_carried_out_count the activities_carried_out_count to set
	 */
	public void setActivities_carried_out_count(Long activities_carried_out_count) {
		this.activities_carried_out_count = activities_carried_out_count;
	}

	/**
	 * @return the sum_activity_expected_minutes_quantity
	 */
	public BigDecimal getSum_activity_expected_minutes_quantity() {
		return sum_activity_expected_minutes_quantity;
	}

	/**
	 * @param sum_activity_expected_minutes_quantity the sum_activity_expected_minutes_quantity to set
	 */
	public void setSum_activity_expected_minutes_quantity(
			BigDecimal sum_activity_expected_minutes_quantity) {
		this.sum_activity_expected_minutes_quantity = sum_activity_expected_minutes_quantity;
	}

	/**
	 * @return the sum_activity_effective_minutes_quantity
	 */
	public BigDecimal getSum_activity_effective_minutes_quantity() {
		return sum_activity_effective_minutes_quantity;
	}

	/**
	 * @param sum_activity_effective_minutes_quantity the sum_activity_effective_minutes_quantity to set
	 */
	public void setSum_activity_effective_minutes_quantity(
			BigDecimal sum_activity_effective_minutes_quantity) {
		this.sum_activity_effective_minutes_quantity = sum_activity_effective_minutes_quantity;
	}

	/**
	 * @return the sum_man_power_requeriment_minutes_quantity
	 */
	public BigDecimal getSum_man_power_requeriment_minutes_quantity() {
		return sum_man_power_requeriment_minutes_quantity;
	}

	/**
	 * @param sum_man_power_requeriment_minutes_quantity the sum_man_power_requeriment_minutes_quantity to set
	 */
	public void setSum_man_power_requeriment_minutes_quantity(
			BigDecimal sum_man_power_requeriment_minutes_quantity) {
		this.sum_man_power_requeriment_minutes_quantity = sum_man_power_requeriment_minutes_quantity;
	}

	/**
	 * @return the sum_man_power_cost
	 */
	public BigDecimal getSum_man_power_cost() {
		return sum_man_power_cost;
	}

	/**
	 * @param sum_man_power_cost the sum_man_power_cost to set
	 */
	public void setSum_man_power_cost(BigDecimal sum_man_power_cost) {
		this.sum_man_power_cost = sum_man_power_cost;
	}

	/**
	 * @return the activities_carried_out_count_formatted
	 */
	public String getActivities_carried_out_count_formatted() {
		return activities_carried_out_count_formatted;
	}

	/**
	 * @param activities_carried_out_count_formatted the activities_carried_out_count_formatted to set
	 */
	public void setActivities_carried_out_count_formatted(
			String activities_carried_out_count_formatted) {
		this.activities_carried_out_count_formatted = activities_carried_out_count_formatted;
	}

	/**
	 * @return the sum_activity_expected_minutes_quantity_formatted
	 */
	public String getSum_activity_expected_minutes_quantity_formatted() {
		return sum_activity_expected_minutes_quantity_formatted;
	}

	/**
	 * @param sum_activity_expected_minutes_quantity_formatted the sum_activity_expected_minutes_quantity_formatted to set
	 */
	public void setSum_activity_expected_minutes_quantity_formatted(
			String sum_activity_expected_minutes_quantity_formatted) {
		this.sum_activity_expected_minutes_quantity_formatted = sum_activity_expected_minutes_quantity_formatted;
	}

	/**
	 * @return the sum_activity_effective_minutes_quantity_formatted
	 */
	public String getSum_activity_effective_minutes_quantity_formatted() {
		return sum_activity_effective_minutes_quantity_formatted;
	}

	/**
	 * @param sum_activity_effective_minutes_quantity_formatted the sum_activity_effective_minutes_quantity_formatted to set
	 */
	public void setSum_activity_effective_minutes_quantity_formatted(
			String sum_activity_effective_minutes_quantity_formatted) {
		this.sum_activity_effective_minutes_quantity_formatted = sum_activity_effective_minutes_quantity_formatted;
	}

	/**
	 * @return the sum_man_power_cost_formatted
	 */
	public String getSum_man_power_cost_formatted() {
		return sum_man_power_cost_formatted;
	}

	/**
	 * @param sum_man_power_cost_formatted the sum_man_power_cost_formatted to set
	 */
	public void setSum_man_power_cost_formatted(String sum_man_power_cost_formatted) {
		this.sum_man_power_cost_formatted = sum_man_power_cost_formatted;
	}

	/**
	 * @return the personal_civil_id_document_formatted
	 */
	public String getPersonal_civil_id_document_formatted() {
		return personal_civil_id_document_formatted;
	}

	/**
	 * @param personal_civil_id_document_formatted the personal_civil_id_document_formatted to set
	 */
	public void setPersonal_civil_id_document_formatted(
			String personal_civil_id_document_formatted) {
		this.personal_civil_id_document_formatted = personal_civil_id_document_formatted;
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
	 * @return the saved_minutes
	 */
	public BigDecimal getSaved_minutes() {
		return saved_minutes;
	}

	/**
	 * @param saved_minutes the saved_minutes to set
	 */
	public void setSaved_minutes(BigDecimal saved_minutes) {
		this.saved_minutes = saved_minutes;
	}

	/**
	 * @return the saved_minutes_formatted
	 */
	public String getSaved_minutes_formatted() {
		return saved_minutes_formatted;
	}

	/**
	 * @param saved_minutes_formatted the saved_minutes_formatted to set
	 */
	public void setSaved_minutes_formatted(String saved_minutes_formatted) {
		this.saved_minutes_formatted = saved_minutes_formatted;
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

}
