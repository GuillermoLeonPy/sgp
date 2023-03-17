/**
 * 
 */
package py.com.kyron.sgp.bussines.cash.movements.management.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.domain.GenericDTO;

/**
 * @author testuser
 *
 */
public class SaleInvoiceDTO extends GenericDTO {

	
	private Long id_person;
	private Long id_currency;
	private Long id_order;
	private String payment_condition;
	private String status;
	private Date emission_date;
	private Date cancellation_date;
	private Date annulment_date;
	private String annulment_reason_description;
	private String identifier_number;
	
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
	
	private Long id_branch_office_sale_station;
	private BigDecimal sale_invoice_stamp_number;
	private Long id_sale_invoice_stamping_numeration;	
	
	private OrderDTO orderDTO;
	private List<SaleInvoicePaymentDTO> listSaleInvoicePaymentDTO;
	private List<SaleInvoiceItemDTO> listSaleInvoiceItemDTO;
	
	private String previous_status;
	
	private Date emissionDateBeginFilter;
	private Date emissionDateEndFilter;
	private Boolean queryForSalesTaxReport;
	/**
	 * 
	 */
	public SaleInvoiceDTO() {
		// TODO Auto-generated constructor stub
	}

	public SaleInvoiceDTO(Long id) {
		super();
		this.setId(id);
	}
	
	public SaleInvoiceDTO(Long id,Long id_person) {
		super();
		this.id_person = id_person;
	}

	public SaleInvoiceDTO(Long id,Long id_person,Long id_order,Long id_branch_office_sale_station) {
		super();
		this.id_order = id_order;
		this.id_branch_office_sale_station = id_branch_office_sale_station;
	}
	
	public SaleInvoiceDTO(Date emissionDateBeginFilter, Date emissionDateEndFilter, Boolean queryForSalesTaxReport) {
		// TODO Auto-generated constructor stub
		this.emissionDateBeginFilter = emissionDateBeginFilter;
		this.emissionDateEndFilter = emissionDateEndFilter;
		this.queryForSalesTaxReport = queryForSalesTaxReport;
	}
	/**
	 * @param id
	 * @param creation_user
	 * @param creation_date
	 * @param last_modif_user
	 * @param last_modif_date
	 */
	public SaleInvoiceDTO(Long id, String creation_user, Date creation_date,
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
	 * @return the id_order
	 */
	public Long getId_order() {
		return id_order;
	}

	/**
	 * @param id_order the id_order to set
	 */
	public void setId_order(Long id_order) {
		this.id_order = id_order;
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
	 * @return the sale_invoice_stamp_number
	 */
	public BigDecimal getSale_invoice_stamp_number() {
		return sale_invoice_stamp_number;
	}

	/**
	 * @param sale_invoice_stamp_number the sale_invoice_stamp_number to set
	 */
	public void setSale_invoice_stamp_number(BigDecimal sale_invoice_stamp_number) {
		this.sale_invoice_stamp_number = sale_invoice_stamp_number;
	}

	/**
	 * @return the id_sale_invoice_stamping_numeration
	 */
	public Long getId_sale_invoice_stamping_numeration() {
		return id_sale_invoice_stamping_numeration;
	}

	/**
	 * @param id_sale_invoice_stamping_numeration the id_sale_invoice_stamping_numeration to set
	 */
	public void setId_sale_invoice_stamping_numeration(
			Long id_sale_invoice_stamping_numeration) {
		this.id_sale_invoice_stamping_numeration = id_sale_invoice_stamping_numeration;
	}

	/**
	 * @return the orderDTO
	 */
	public OrderDTO getOrderDTO() {
		return orderDTO;
	}

	/**
	 * @param orderDTO the orderDTO to set
	 */
	public void setOrderDTO(OrderDTO orderDTO) {
		this.orderDTO = orderDTO;
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
		SaleInvoiceDTO other = (SaleInvoiceDTO) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SaleInvoiceDTO [id_person=" + id_person + ", id_currency="
				+ id_currency + ", id_order=" + id_order
				+ ", payment_condition=" + payment_condition + ", status="
				+ status + ", emission_date=" + emission_date
				+ ", cancellation_date=" + cancellation_date
				+ ", annulment_date=" + annulment_date
				+ ", annulment_reason_description="
				+ annulment_reason_description + ", identifier_number="
				+ identifier_number + ", total_amount=" + total_amount
				+ ", value_added_amount=" + value_added_amount
				+ ", total_tax_amount=" + total_tax_amount + ", exempt_amount="
				+ exempt_amount + ", value_added_tax_10_amount="
				+ value_added_tax_10_amount + ", value_added_tax_5_amount="
				+ value_added_tax_5_amount + ", tax_10_amount=" + tax_10_amount
				+ ", tax_5_amount=" + tax_5_amount + ", bussines_name="
				+ bussines_name + ", bussines_ci_ruc=" + bussines_ci_ruc
				+ ", id_branch_office_sale_station="
				+ id_branch_office_sale_station
				+ ", sale_invoice_stamp_number=" + sale_invoice_stamp_number
				+ ", id_sale_invoice_stamping_numeration="
				+ id_sale_invoice_stamping_numeration + ", previous_status="
				+ previous_status + ", getId()=" + getId()
				+ ", getCreation_user()=" + getCreation_user()
				+ ", getCreation_date()=" + getCreation_date()
				+ ", getLast_modif_user()=" + getLast_modif_user()
				+ ", getLast_modif_date()=" + getLast_modif_date()
				+ ", getClass()=" + getClass() + ", toString()="
				+ super.toString() + "]";
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
	 * @return the listSaleInvoicePaymentDTO
	 */
	public List<SaleInvoicePaymentDTO> getListSaleInvoicePaymentDTO() {
		return listSaleInvoicePaymentDTO;
	}

	/**
	 * @param listSaleInvoicePaymentDTO the listSaleInvoicePaymentDTO to set
	 */
	public void setListSaleInvoicePaymentDTO(
			List<SaleInvoicePaymentDTO> listSaleInvoicePaymentDTO) {
		this.listSaleInvoicePaymentDTO = listSaleInvoicePaymentDTO;
	}

	/**
	 * @return the listSaleInvoiceItemDTO
	 */
	public List<SaleInvoiceItemDTO> getListSaleInvoiceItemDTO() {
		return listSaleInvoiceItemDTO;
	}

	/**
	 * @param listSaleInvoiceItemDTO the listSaleInvoiceItemDTO to set
	 */
	public void setListSaleInvoiceItemDTO(
			List<SaleInvoiceItemDTO> listSaleInvoiceItemDTO) {
		this.listSaleInvoiceItemDTO = listSaleInvoiceItemDTO;
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
	 * @return the queryForSalesTaxReport
	 */
	public Boolean getQueryForSalesTaxReport() {
		return queryForSalesTaxReport;
	}

	/**
	 * @param queryForSalesTaxReport the queryForSalesTaxReport to set
	 */
	public void setQueryForSalesTaxReport(Boolean queryForSalesTaxReport) {
		this.queryForSalesTaxReport = queryForSalesTaxReport;
	}
}
